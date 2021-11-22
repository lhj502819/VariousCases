package cn.znnine.netty.protocol.priv.struct;

/**
 * 消息定义
 *
 * @author lihongjian
 * @since 2021/11/22
 */
public class NettyMessage {
    /**
     * 消息头
     */
    private Header header;

    /**
     * 消息体
     */
    private Object body;

    public final Header getHeader() {
        return header;
    }

    public final void setHeader(Header header) {
        this.header = header;
    }

    public final Object getBody() {
        return body;
    }

    public final void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "NettyMessage{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }
}
