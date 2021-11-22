package cn.znnine.netty.protocol.priv.server;

import cn.znnine.netty.protocol.priv.constant.MessageType;
import cn.znnine.netty.protocol.priv.struct.Header;
import cn.znnine.netty.protocol.priv.struct.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 握手认证的客户端，用于在通道激活时发起握手请求
 *
 * @author lihongjian
 * @since 2021/11/22
 */
public class LoginAuthRequestHandler extends ChannelHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        /**
         * 当客户端和服务端TCP三次握手成功后，由客户端构造握手请求消息发送给服务端
         * 由于采用IP白名单认证机制，因此不需要携带消息体，消息体为空，消息类型为“3：握手请求消息”
         * 握手请求发送后，按照协议规范，服务端需要返回握手应答消息
         */
        ctx.writeAndFlush(buildLoginReq());
    }

    /**
     * 对握手应答消息进行处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage)msg;

        //如果是握手应答消息，需要判定是否认证成功，如果非0表示认证失败，关闭链路，重新发起连接
        //如果不是握手应答消息，直接透传给后面的ChannelHandler进行处理
        if(message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.getValue()){
            byte loginResult = (byte) message.getBody();
            if(loginResult != (byte) 0){
                //握手失败，关闭连接
                ctx.close();
            }else {
                System.out.println("Login is ok：" + message);
                ctx.fireChannelRead(msg);
            }
        }else {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildLoginReq(){
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.getValue());
        message.setHeader(header);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}


