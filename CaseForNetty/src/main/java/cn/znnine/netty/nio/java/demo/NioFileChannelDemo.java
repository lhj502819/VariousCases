package cn.znnine.netty.nio.java.demo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 测试读取1.txt文件内容，写入到2.txt中
 *
 * @author lihongjian
 * @since 2021/12/1
 */
public class NioFileChannelDemo {
    public static void main(String[] args) throws Exception {
        try
                (FileInputStream fileInputStream = new FileInputStream("E:\\workspeace\\VariousCases\\CaseForNetty\\src\\main\\java\\cn\\znnine\\netty\\nio\\java\\demo\\1.txt");
                 FileOutputStream fileOutputStream = new FileOutputStream("E:\\workspeace\\VariousCases\\CaseForNetty\\src\\main\\java\\cn\\znnine\\netty\\nio\\java\\demo\\2.txt");) {
            FileChannel inputStreamChannel = fileInputStream.getChannel();
            FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();

            ByteBuffer buffer = ByteBuffer.allocate(5);

            while (true) {
                int read = inputStreamChannel.read(buffer);
                if (read == -1) {
                    break;
                }
                buffer.flip();
                fileOutputStreamChannel.write(buffer);
                buffer.clear();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }
}
