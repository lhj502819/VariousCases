package cn.znnine.netty.protocol.priv.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteOutput;

import java.io.IOException;

/**
 * {@link ByteOutput} implementation which writes the data to a {@link ByteBuf}
 *
 * @author lihongjian
 * @since 2021/11/22
 */
public class ChannelBufferByteOutput implements ByteOutput {
    private final ByteBuf buffer;

    /**
     * 创建一个使用ByteBuf的实例
     *
     * @param out
     */
    public ChannelBufferByteOutput(ByteBuf out) {
        this.buffer = out;
    }

    @Override
    public void write(int b) throws IOException {
        buffer.writeByte(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        buffer.writeBytes(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        buffer.writeBytes(b,off,len);
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void flush() throws IOException {

    }

    public ByteBuf getBuffer() {
        return buffer;
    }
}
