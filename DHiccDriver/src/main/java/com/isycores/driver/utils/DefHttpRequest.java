package com.isycores.driver.utils;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class DefHttpRequest extends DefaultFullHttpRequest {
    private static final Logger log = LoggerFactory.getLogger(DefHttpRequest.class);
    public DefHttpRequest(HttpMethod method, String uri) {
        super(HttpVersion.HTTP_1_1, method, uri);
    }
    public DefHttpRequest(HttpMethod method, String uri,Map<AsciiString,String> headers) {
        super(HttpVersion.HTTP_1_1, method, uri);
        headers.forEach((K,V) -> this.headers().set(K,V));
    }
    public DefHttpRequest(HttpMethod method, String uri, String body) {
        super(HttpVersion.HTTP_1_1, method, uri, Unpooled.wrappedBuffer(body.getBytes(StandardCharsets.UTF_8)));
        this.headers().set(HttpHeaderNames.CONTENT_LENGTH,content().readableBytes());
    }

    public DefHttpRequest(HttpMethod method, String uri, Map<AsciiString,String> inHeaders,String body) {
        super(HttpVersion.HTTP_1_1, method, uri, Unpooled.wrappedBuffer(body.getBytes(StandardCharsets.UTF_8)));
        inHeaders.forEach((K,V) -> headers().set(K,V));
        this.headers().set("Content-Length",content().readableBytes());
    }
    public DefHttpRequest(HttpMethod method, String uri, Map<AsciiString,String> inHeaders,byte[] body) {
        super(HttpVersion.HTTP_1_1, method, uri, Unpooled.wrappedBuffer(body));
        inHeaders.forEach((K,V) -> headers().set(K,V));
        this.headers().set("Content-Length",content().readableBytes());
    }
    public DefHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri) {
        super(httpVersion, method, uri);
    }
    public DefHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri, ByteBuf content) {
        super(httpVersion, method, uri, content);
    }
    public DefHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri, boolean validateHeaders) {
        super(httpVersion, method, uri, validateHeaders);
    }
    public DefHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri, ByteBuf content, boolean validateHeaders) {
        super(httpVersion, method, uri, content, validateHeaders);
    }
    public DefHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri, ByteBuf content, HttpHeaders headers, HttpHeaders trailingHeader) {
        super(httpVersion, method, uri, content, headers, trailingHeader);
    }
}
