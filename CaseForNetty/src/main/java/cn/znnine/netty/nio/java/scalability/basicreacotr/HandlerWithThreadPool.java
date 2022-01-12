package cn.znnine.netty.nio.java.scalability.basicreacotr;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池版本Handler
 *
 * @author lihongjian
 * @since 2022/1/9
 */
public class HandlerWithThreadPool implements  Runnable{

    //线程池
    static ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    static final int PROCESSING = 3;

    final SocketChannel socketChannel;
    final SelectionKey selectionKey;
    ByteBuffer input = ByteBuffer.allocate(1024);
    ByteBuffer output = ByteBuffer.allocate(1024);
    static final int READING = 0, SENDING = 1;
    int state = READING;
    public HandlerWithThreadPool(SocketChannel socketChannel, Selector selector) throws Exception {
        this.socketChannel = socketChannel;
        socketChannel.configureBlocking(false);
        selectionKey = socketChannel.register(selector, 0);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }
    boolean inputIsComplete() {
        return true;
    }
    boolean outputIsComplete() {
        return true;
    }
    void process() { }
    @Override
    public void run() {
        try {
            if(state == READING){
                read();
            }else if(state == SENDING){
                send();
            }
        }catch (Exception e){

        }
    }
    void read() throws Exception {
        socketChannel.read(input);
        if (inputIsComplete()) {
            state = PROCESSING;
            poolExecutor.execute(new Processer());
        }
    }
    void send() throws Exception {
        socketChannel.write(output);
        if (outputIsComplete()) {
            selectionKey.cancel();
        }
    }

    class Processer implements Runnable{
        @Override
        public void run() {
        }
    }
}
