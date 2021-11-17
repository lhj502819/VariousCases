package cn.znnine.netty.protocol.http.xml;

import io.netty.handler.codec.http.FullHttpResponse;

/**
 * 应答消息HttpXmlResponse
 *
 * @author lihongjian
 * @since 2021/11/17
 */
public class HttpXmlResponse {

    private FullHttpResponse httpResponse;

    /**
     * 业务需要发送的应答POJO对象
     */
    private Object result;

    public HttpXmlResponse(FullHttpResponse httpResponse, Object result) {
        this.httpResponse = httpResponse;
        this.result = result;
    }

    public FullHttpResponse getHttpResponse() {
        return httpResponse;
    }

    public void setHttpResponse(FullHttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
