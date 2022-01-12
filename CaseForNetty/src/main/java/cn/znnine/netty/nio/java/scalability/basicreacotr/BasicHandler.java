package cn.znnine.netty.nio.java.scalability.basicreacotr;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author lihongjian
 * @since 2022/1/9
 */
final class BasicHandler implements Runnable {
    final SocketChannel socketChannel;
    final SelectionKey selectionKey;
    ByteBuffer input = ByteBuffer.allocate(1024);
    ByteBuffer output = ByteBuffer.allocate(1024);
    static final int READING = 0, SENDING = 1;
    int state = READING;
    public BasicHandler(SocketChannel socketChannel, Selector selector) throws Exception {
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
            process();
            state = SENDING;
            selectionKey.interestOps(SelectionKey.OP_WRITE);
            //doWrite
        }
    }
    void send() throws Exception {
        socketChannel.write(output);
        if (outputIsComplete()) {
            selectionKey.cancel();
        }
    }
}
