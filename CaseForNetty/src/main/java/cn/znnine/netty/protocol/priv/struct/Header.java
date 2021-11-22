package cn.znnine.netty.protocol.priv.struct;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息头定义
 *
 * @author lihongjian
 * @since 2021/11/22
 */
public class Header {
    /**
     * Netty消息的校验码，由三部分组成
     * 1、0xABEF；固定值，表明该消息是Netty协议消息，2个字节
     * 2、主版本号：1~255,1个字节
     * 3、次版本号：1~255,1个字节
     *
     * crcCode = 0xABEF + 主版本号 + 次版本号
     */
    private int crcCode = 0xabef0101;

    /**
     * 消息长度，整个消息，包括消息头和消息体
     */
    private int length;

    /**
     * 会话ID，集群节点内全局唯一，由会话ID生成器生成
     */
    private long sessionId;

    /**
     * 消息类型
     * 0：业务请求消息
     * 1：业务响应消息
     * 2：业务ONE WAY消息（既是请求又是响应消息）
     * 3：握手请求消息
     * 4：握手应答消息
     * 5：心跳请求消息
     * 6：心跳应答消息
     */
    private byte type;

    /**
     * 消息优先级：0~255
     */
    private byte priority;

    /**
     * 可选字段，用于扩展消息头
     */
    private Map<String, Object> attachment = new HashMap<>();

    public int getCrcCode() {
        return crcCode;
    }

    public void setCrcCode(int crcCode) {
        this.crcCode = crcCode;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public Map<String, Object> getAttachment() {
        return attachment;
    }

    public void setAttachment(Map<String, Object> attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "Header{" +
                "crcCode=" + crcCode +
                ", length=" + length +
                ", sessionId=" + sessionId +
                ", type=" + type +
                ", priority=" + priority +
                ", attachment=" + attachment +
                '}';
    }
}
