package cn.znnine.netty.nio.java.scalability.basicreacotr;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author lihongjian
 * @since 2022/1/9
 */
public class Reactor implements Runnable {
    final Selector selector;
    final ServerSocketChannel serverSocketChannel;
    public Reactor(int port) throws Exception {

        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        selectionKey.attach(new Acceptor());
    }
    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                //发生阻塞直到有事件
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                //处理所有事件
                while (iterator.hasNext()) {
                    //分发事件
                    dispatch(iterator.next());
                }
                selectionKeys.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void dispatch(SelectionKey selectionKey) {

        Runnable r = (Runnable) selectionKey.attachment();
        if (r != null) {
            r.run();
        }
    }
    class Acceptor implements Runnable{
        @Override
        public void run() {
            try {
                SocketChannel channel = serverSocketChannel.accept();
                if(channel != null){
                    //单线程处理
                    new BasicHandler(channel,selector);
                    //多线程处理
                }
            }catch (Exception e){

            }
        }
    }
}
