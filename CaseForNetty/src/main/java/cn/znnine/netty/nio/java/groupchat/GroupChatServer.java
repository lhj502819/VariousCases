package cn.znnine.netty.nio.java.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 群聊Server端
 *
 * @author lihongjian
 * @since 2021/12/11
 */
public class GroupChatServer {

    //相关属性

    private ServerSocketChannel listenChannel;

    private Selector selector;

    private static final int PORT = 8084;

    /**
     * 初始化工作
     */
    public GroupChatServer() {
        try {
            //得到选择器Selector
            selector = Selector.open();
            //创建ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.bind(new InetSocketAddress(PORT));
            //设置为非阻塞
            listenChannel.configureBlocking(false);
            //将ServerSocketChannel注册到Selector上，对接受请求事件感兴趣
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 监听
     */
    public void listen() {
        try {
            while (true) {
                int count = selector.select();
                if (count > 0) {
                    //有事件要处理
                    //遍历SelectionKey
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        //监听到Accept事件
                        if (key.isAcceptable()) {
                            SocketChannel socketChannel = listenChannel.accept();
                            //配置为非阻塞
                            socketChannel.configureBlocking(false);
                            //将该客户端的Channel注册到Selector，并设置感兴趣的事件为READ
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            //提示上线
                            System.out.println(socketChannel.getRemoteAddress() + " 上线了.");
                        }
                        //监听到读事件
                        if (key.isReadable()) {
                            //处理读的事件
                            readData(key);
                        }
                        //把当前的key删除，防止重复
                        iterator.remove();
                    }
                } else {
                    System.out.println("无事件要处理，等待.......");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    /**
     * 读取客户端消息
     */
    private void readData(SelectionKey selectionKey) {
        //定义一个SocketChannel
        SocketChannel socketChannel = null;
        try {
            socketChannel = (SocketChannel) selectionKey.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            //读取到的字节数
            int readCount = socketChannel.read(buffer);
            if (readCount >= 0) {
                //把缓冲区的数据转换成字符串
                String msg = new String(buffer.array());
                //输出该消息
                System.out.println("接收到客户端消息：" + msg);
                //向其他客户端转发消息
                forwardMsgToOther(msg, socketChannel);
            }
        } catch (Exception e) {
            //如果异常了，说明客户端断开连接了
            try {
                System.out.println(socketChannel.getRemoteAddress() + "离线了....");
                socketChannel.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            } finally {
                //取消注册
                selectionKey.cancel();
                ;
                //关闭通道
                try {
                    socketChannel.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }

    }

    /**
     * 转发消息给其他客户端
     *
     * @param msg  消息
     * @param self 当前客户端，用于排除自己，不给自己发
     */
    private void forwardMsgToOther(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中...");
        //遍历所有注册到Selector上的SocketChannel，排除自己
        Set<SelectionKey> keys = selector.keys();
        for (SelectionKey key : keys) {
            //通过Key取出SocketChannel
            Channel targetChannel = key.channel();
            //排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                //转型
                SocketChannel dest = (SocketChannel) targetChannel;
                //将msg存储到buffer中
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8));
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {

        GroupChatServer chatServer = new GroupChatServer();
        chatServer.listen();

    }
}
