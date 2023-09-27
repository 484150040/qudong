package com.isyscore.os.driver.utils;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class TcpServer {
    private static final Logger log = LoggerFactory.getLogger(TcpServer.class);
    ServerBootstrap serverBootstrap = BootStrapManager.newServerBootStrap();
    private Channel ch = null;
    private Map<String,ChannelHandlerContext> clientMap = new HashMap<>();
    private int port;
    public interface TcpFunction {
        void operation(ChannelHandlerContext ctx, ByteBuf buf) throws Exception;
    }

    public interface TcpConnected {
        void operation(ChannelHandlerContext ctx) throws Exception;
    }

    public interface TcpDisConnected {
        void operation(ChannelHandlerContext ctx) throws Exception;
    }

    public TcpServer(int port,
                     TcpConnected tcpConnected,
                     TcpFunction tcpFunction,
                     TcpDisConnected tcpDisConnected) {
        this.port = port;
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("handler", new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                        String clientInfo = address.getHostString() + ":" + address.getPort();
                        clientMap.put(clientInfo,ctx);
                        log.info("TCP Client {} Connected",clientInfo);
                        if (tcpConnected != null) {
                            tcpConnected.operation(ctx);
                        }
                    }
                    @Override
                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                        String clientInfo = address.getHostString() + ":" + address.getPort();
                        clientMap.remove(clientInfo);
                        log.info("TCP Client {} DisConnected",clientInfo);
                        if (tcpDisConnected != null)
                        tcpDisConnected.operation(ctx);
                    }
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
                        if (tcpFunction != null) {
                            tcpFunction.operation(ctx, buf);
                        }
                    }
                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                        log.error(cause.getMessage(),cause);
                        ctx.close();
                    }
                });
            }
        });
    }
    public void start() {
        if (ch == null) {
            ChannelFuture future = serverBootstrap.bind(port);
            ch = future.channel();
        }
    }
    public void stop() {
        if (ch != null) {
            ch.close();
            clientMap.forEach((clientInfo,ctx)->{
                ctx.close();
            });
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BootStrapManager.init(1,10);
        TcpServer client = new TcpServer(8022,
                (ctx)-> {
                    ctx.channel().id();
                    log.info("链接已经建立");
                },
                (ctx,byteBuf)-> {
                    log.info("12345");
                },
                (ctx)->{
                    log.info("链接已经关闭");
                } );
        client.start();
        Thread.sleep(60 * 1000);
        client.stop();
        log.info("关闭链接");
        Thread.sleep(5);
        BootStrapManager.exit();
    }
}
