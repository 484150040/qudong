package com.isycores.driver.utils;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class BootStrapManager {
    private static EventLoopGroup BOSS_GROUP;
    private static EventLoopGroup WORKER_GROUP;
    public static Bootstrap newBootStrap() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(WORKER_GROUP);
        return bootstrap;
    }

    public static ServerBootstrap newServerBootStrap() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(BOSS_GROUP,WORKER_GROUP);
        return bootstrap;
    }

    public static void init(int bThreads,int wThreads) {
        BOSS_GROUP = new NioEventLoopGroup(bThreads);
        WORKER_GROUP = new NioEventLoopGroup(wThreads);
    }
    public static void init(int wThreads) {
        WORKER_GROUP = new NioEventLoopGroup(wThreads);
    }
    public static void exit() {
        if (BOSS_GROUP != null) {
            BOSS_GROUP.shutdownGracefully();
        }
        WORKER_GROUP.shutdownGracefully();
    }
}