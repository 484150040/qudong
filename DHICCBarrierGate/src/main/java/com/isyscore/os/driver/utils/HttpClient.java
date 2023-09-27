package com.isyscore.os.driver.utils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.SSLException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpClient {
    private String host;
    private int port;
    private boolean isSsl;
    private String path = "";
    public interface OnResponse {
        void operation(ChannelHandlerContext ctx, FullHttpResponse response);
    }
    public HttpClient(String host, int port,boolean isSsl) {
        this.host = host;
        this.port = port;
        this.isSsl = isSsl;
    }
    public HttpClient(String url) throws MalformedURLException {
        URL _url = new URL(url);
        this.isSsl = "https".equals(_url.getProtocol());
        this.host = _url.getHost();
        this.port = _url.getPort() <= 0 ? _url.getDefaultPort() : _url.getPort();
        this.path = _url.getPath();
    }
    public void exec(DefHttpRequest request, OnResponse onResponse, boolean sync) {
        request.headers().set("Host",host + ":" + port);
        request.setUri(path + request.uri());
        try {
            final Bootstrap b = BootStrapManager.newBootStrap();
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, false);
            b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws SSLException {
                    if (isSsl) { //配置Https通信
                        SslContext context = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
                        ch.pipeline().addLast(context.newHandler(ch.alloc()));
                    }

                    ch.pipeline().addLast(new HttpClientCodec());
                    ch.pipeline().addLast(new HttpObjectAggregator(1024 * 1024 * 1024));
                    ch.pipeline().addLast(new HttpContentDecompressor());
                    ch.pipeline().addLast(new SimpleChannelInboundHandler<FullHttpResponse>(){
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            ctx.channel().writeAndFlush(request).addListener((ChannelFutureListener) future -> {});
                        }
                        @Override
                        public void channelInactive(ChannelHandlerContext ctx) throws Exception {

                        }
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse response) throws Exception {
                            if (response instanceof HttpContent) {
                                if (onResponse != null) {
                                    onResponse.operation(ctx,response);
                                }
                                ctx.close();
                            }
                        }
                        @Override
                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                            cause.printStackTrace();
                            ctx.channel().close();
                        }
                    });
                }
            });
            if (sync)
                b.connect(host, port).channel().closeFuture().sync();
            else
                b.connect(host, port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws MalformedURLException {
        BootStrapManager.init(1,10);
        HttpClient client = new HttpClient("https://www.baidu.com");
        DefHttpRequest request = new DefHttpRequest(HttpVersion.HTTP_1_1,HttpMethod.GET,"/");
        client.exec(request,(ctx,message) -> {
            System.out.println(message.content().toString(StandardCharsets.UTF_8));
        },true);
        BootStrapManager.exit();
    }
}
