package com.isycores.driver.utils;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {
    private static final Logger log = LoggerFactory.getLogger(HttpServer.class);
    private Map<String,Map<HttpMethod, HttpFunction>> FunctionsMap = new HashMap<>();
    public Map<String, ChannelHandlerContext> channelMap = new HashMap<>();
    ServerBootstrap serverBootstrap = BootStrapManager.newServerBootStrap();
    private Channel channel = null;
    public interface HttpFunction {
        FullHttpResponse operation(ChannelHandlerContext ctx, FullHttpRequest msg);
    }
    public HttpServer() {
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                p.addLast(new HttpServerCodec());
                p.addLast(new HttpObjectAggregator(61024));
                p.addLast(new SimpleChannelInboundHandler<FullHttpRequest>() {
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                        String host = address.getHostString();
                        channelMap.put(host, ctx);
                    }

                    @Override
                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                        String host = address.getHostString();
                        channelMap.remove(host);
                    }

                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
                        FullHttpResponse response;
                        Map<HttpMethod, HttpFunction> map = FunctionsMap.get(msg.uri());

                        if (map != null) {
                            HttpFunction function = map.get(msg.method());
                            if (function != null) {
                                response = function.operation(ctx,msg);
                            } else {
                                response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.METHOD_NOT_ALLOWED);
                            }
                        } else {
                            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_GATEWAY);
                        }

                        ctx.writeAndFlush(response).addListener(future -> {
                            ctx.close();
                        });
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        ctx.close();
                    }
                });
            }
        });
    }

    public void start(int port) throws InterruptedException {
        if (channel != null) { log.warn("服务实例已启动"); return;}
        channel = serverBootstrap.bind(port).sync().await().channel();
    }

    public void close() throws InterruptedException {
        if (channel == null) { log.warn("服务实例未启动"); return;}
        channel.close().sync();
        for (ChannelHandlerContext client : channelMap.values()) {
            client.close();
        }
        channelMap.clear();
    }

    public void registerFunction(String uri, HttpMethod method, HttpFunction function) {
        Map<HttpMethod, HttpFunction> submap = FunctionsMap.get(uri);
        if (submap == null) {
            submap = new HashMap<>();
            submap.put(method,function);
            FunctionsMap.put(uri,submap);
        } else {
            HttpFunction _function = submap.get(method);
            if (_function == null) {
                submap.put(method,function);
            } else {
                submap.remove(method);
                submap.put(method,function);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        HttpServer server = new HttpServer();
        server.registerFunction("/faceNotify",HttpMethod.POST,((ctx, msg) -> {
            ByteBuf buf = msg.content();
            String requestString = buf.toString(StandardCharsets.UTF_8);
            try {
                System.out.println(requestString);
                System.out.println("\n");
            } catch (Exception e){
                e.printStackTrace();
            }

            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.copiedBuffer("{\"status\": 0,\"detail\":\"success\"}", CharsetUtil.UTF_8));
            return response;
        }));

        server.start(8089);
        Thread.sleep(100000 * 1000);
        server.close();
        BootStrapManager.exit();
    }
}
