package cn.znnine.netty.codec.marshalling;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;


/**
 * @author lihongjian
 * @since 2021/10/23
 */
public class SubReqServerHandler extends ChannelHandlerAdapter {

    /**
     * 由于ProtobufDecoder已经对消息进行了自动解码，因此接收到的消息可以直接使用
     *
     * 由于使用了ProtobufEncoder，所以不需要对SubscribeRespProto.SubscribeResp进行手工解码
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeReqProto.SubscribeReq req  = (SubscribeReqProto.SubscribeReq) msg;
        if("零壹玖".equalsIgnoreCase(req.getUserName())){
            System.out.println("Service accept client subscribe req :[" +req.toString() +"]");
            ctx.writeAndFlush(resp(req.getSubReqID()));
        }
    }
    private SubscribeRespProto.SubscribeResp resp(int subReqID){
        SubscribeRespProto.SubscribeResp.Builder builder = SubscribeRespProto.SubscribeResp.newBuilder();
        builder.setSubReqID(subReqID);
        builder.setRespCode(0);
        builder.setDesc("Netty book order succeed, 3 days later, sent to the designated address");
        return builder.build();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();//发生异常，关闭链路
    }


}
