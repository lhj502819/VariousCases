package cn.znnine.netty.protocol.priv.constant;

/**
 * 消息类型标识
 *
 * @author lihongjian
 * @since 2021/11/22
 */
public enum MessageType {

    SERVICE_REQ((byte) 0), SERVICE_RESP((byte) 1),
    ONE_WAY((byte) 2), LOGIN_REQ((byte) 3), LOGIN_RESP((byte) 4),
    HEARTBEAT_REQ((byte) 5), HEARTBEAT_RESP((byte) 6);

    private byte value;

    private MessageType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return this.value;
    }
}
