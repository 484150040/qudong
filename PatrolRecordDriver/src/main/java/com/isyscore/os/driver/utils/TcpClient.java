package com.isyscore.os.driver.utils;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;

public class TcpClient {
    private static final Logger log = LoggerFactory.getLogger(TcpClient.class);
    private final Bootstrap bootstrap = BootStrapManager.newBootStrap();
    private boolean status = false;
    private boolean closed = false;
    private Channel ch = null;
    private String host;
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

    public TcpClient(String host, int port,
                    TcpConnected tcpConnected,
                    TcpFunction tcpFunction,
                    TcpDisConnected tcpDisConnected) {
        this.host = host;
        this.port = port;
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("handler", new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        if (tcpConnected != null) {
                            tcpConnected.operation(ctx);
                        }
                    }
                    @Override
                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                        if (!closed) {
                            log.info("客户端连接断开，3秒后重连...");
                            ctx.channel().eventLoop().schedule(() -> connect(), 3, TimeUnit.SECONDS);
                        } else {
                            log.info("客户端已关闭");
                        }
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
    public void connect() {
        if (closed) {
            return;
        }
        ChannelFuture future = bootstrap.connect(host,port);
        future.addListener((ChannelFutureListener) f -> {
            if (!f.isSuccess()) {
                log.info("客户端连接失败，3秒后重试...");
                f.channel().eventLoop().schedule(() -> connect(), 3, TimeUnit.SECONDS);
            }
        });
        ch = future.channel();
    }
    public void close() {
        closed = true;
        if (ch != null) {
            ch.close();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BootStrapManager.init(1,10);
        TcpClient client = new TcpClient("127.0.0.1",6000,
                (ctx)-> {
                    log.info("链接已经建立");
                },
                (ctx,byteBuf)-> {
                    log.info("12345");
                },
                (ctx)->{
                    log.info("链接已经关闭");
                } );
        client.connect();
        Thread.sleep(60 * 1000);
        client.close();
        log.info("关闭链接");
        Thread.sleep(5);
        BootStrapManager.exit();
    }
}
