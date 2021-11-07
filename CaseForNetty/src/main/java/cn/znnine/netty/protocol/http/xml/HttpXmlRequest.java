package cn.znnine.netty.protocol.http.xml;

import io.netty.handler.codec.http.FullHttpRequest;

/**
 * 请求消息
 *
 * @author lihongjian
 * @since 2021/11/7
 */
public class HttpXmlRequest {
    private FullHttpRequest request;
    private Object body;

    public HttpXmlRequest(FullHttpRequest request, Object body) {
        this.request = request;
        this.body = body;
    }

    public final FullHttpRequest getRequest() {
        return request;
    }

    public final void setRequest(FullHttpRequest request){
        this.request = request;
    }

    public final Object getBody(){
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
