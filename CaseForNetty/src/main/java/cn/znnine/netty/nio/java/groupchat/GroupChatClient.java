package cn.znnine.netty.nio.java.groupchat;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 群聊客户端
 *
 * @author lihongjian
 * @since 2021/12/11
 */
public class GroupChatClient {
    /**
     * 服务端的IP
     */
    private final String HOST = "127.0.0.1";
    /**
     * 服务端端口
     */
    private final int PORT = 8084;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public GroupChatClient() throws Exception {
        selector = Selector.open();
        //连接服务器
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //将Channel注册到Selector
        socketChannel.register(selector, SelectionKey.OP_READ);
        //得到userName
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username + " is ok...");
    }

    /**
     * 向服务器发送消息
     */
    public void sendMessage(String message) {
        message = username + "说：" + message;
        try {
            //发送消息给服务端
            socketChannel.write(ByteBuffer.wrap(message.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收服务端发送过来的消息
     */
    public void receiveMessage() {
        try {
            int readChannels = selector.select(2000);
            if (readChannels > 0) {
                //有可用通道
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if(key.isReadable()){
                        //得到相关通道
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        //得到Buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int read = socketChannel.read(buffer);
                        if(read >0){
                            //把缓冲区的数据转成字符串
                            String msg = new String(buffer.array());
                            System.out.println(msg.trim());
                        }
                    }
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        GroupChatClient chatClient = new GroupChatClient();

        new Thread(()->{
            while (true){
                chatClient.receiveMessage();
                try {
                    Thread.sleep(3000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        //发送数据给服务端
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String msg = scanner.nextLine();
            chatClient.sendMessage(msg);
        }
    }
}
