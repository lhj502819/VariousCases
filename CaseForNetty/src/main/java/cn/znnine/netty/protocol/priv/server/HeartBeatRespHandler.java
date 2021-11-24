package cn.znnine.netty.protocol.priv.server;

import cn.znnine.netty.protocol.priv.constant.MessageType;
import cn.znnine.netty.protocol.priv.struct.Header;
import cn.znnine.netty.protocol.priv.struct.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Description：心跳应答Handler
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2021/11/24
 */
public class HeartBeatRespHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message  = (NettyMessage) msg;
        //返回心跳应答消息
        if(message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.getValue()){
            System.out.println("Receive client heart bea message：" + message);
            NettyMessage heartBeat = buildHeartBeat();
            System.out.println("Send heart beat message ：" + message);
            ctx.writeAndFlush(heartBeat);
        }else {
            ctx.fireChannelRead(msg);
        }
    }
    private NettyMessage buildHeartBeat() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP.getValue());
        message.setHeader(header);
        return message;
    }

}
