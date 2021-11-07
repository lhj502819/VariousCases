package cn.znnine.netty.http.file;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Objects;
import java.util.regex.Pattern;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.*;

/**
 * Description：文件服务器的业务处理逻辑
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2021/11/1
 */
public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String url;

    public HttpFileServerHandler(String url) {
        this.url = url;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        //如果解码失败就返回400
        if (!request.getDecoderResult().isSuccess()) {
            sendError(ctx, BAD_REQUEST);
            return;
        }

        //如果不是GET方法就返回405
        if (request.getMethod() != GET) {
            sendError(ctx, METHOD_NOT_ALLOWED);
            return;
        }
        final String uri = request.getUri();
        //分析URI
        final String path = sanitizeUri(uri);
        System.out.println(">>" + path);
        if (path == null) {
            sendError(ctx, FORBIDDEN);
            return;
        }
        File file = new File(path);
        //如果文件不存在或者隐藏文件，就404
        //如果是目录就返回目录连接
        if (file.isHidden() || !file.exists()) {
            sendError(ctx, NOT_FOUND);
            return;
        }

        if (file.isDirectory()) {
            if (uri.endsWith("/")) {
                sendListing(ctx, file);
            } else {
                sendRedirect(ctx, uri + "/");
            }
            return;
        }
        //如果不是合法文件，则返回403
        if (!file.isFile()) {
            sendError(ctx, FORBIDDEN);
            return;
        }
        RandomAccessFile randomAccessFile;
        try {
            //以只读的方式打开文件
            randomAccessFile = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException foundException) {
            sendError(ctx, NOT_FOUND);
            return;
        }

        //文件存在，一切正常  获取文件长度 发送回客户端
        long fileLength = randomAccessFile.length();
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, OK);

        setContentLength(response, fileLength);
        setContentTypeHeader(response, file);

        if (isKeepAlive(request)) {
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            //要加这个头信息，不然只会下载
            response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
        }
        ctx.write(response);
        ChannelFuture sendFileFuture;
        sendFileFuture =
                ctx.write(new ChunkedFile(randomAccessFile, 0, fileLength, 8192), ctx.newProgressivePromise());
        sendFileFuture.addListener(new ChannelProgressiveFutureListener() {

            @Override
            public void operationComplete(ChannelProgressiveFuture future) throws Exception {
                System.out.println("Transfer complete");
            }

            @Override
            public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {
                if (total < 0) {
                    System.err.println("Transfer progress: " + progress);
                } else {
                    System.err.println("Transfer progress: " + progress + "/" + total);
                }
            }
        });
        //使用chunked编码，最后需要发送一个编码结束的空消息体，将LastHttpContent.EMPTY_LAST_CONTENT发送到缓冲区中，标识所有的消息已经发送完成
        //同时调用flush方法将之前发送到缓冲区的消息刷到SocketChannel中发送给对方
        ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!isKeepAlive(request)) {
            //如果是非keepAlive的，最后一包消息发送完成后，服务端要主动关闭连接
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (ctx.channel().isActive()) {
            sendError(ctx, INTERNAL_SERVER_ERROR);
        }
    }

    private void setContentTypeHeader(HttpResponse response, File file) {
        MimetypesFileTypeMap mimetypesMap = new MimetypesFileTypeMap();
        response.headers().set(CONTENT_TYPE, mimetypesMap.getContentType(file.getPath()));
    }

    private void sendRedirect(ChannelHandlerContext ctx, String newUri) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, FOUND);
        response.headers().set(LOCATION, newUri);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");

    private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

    /**
     * 发送目录作为链接回去
     *
     * @param ctx 环境
     * @param dir 目录
     */
    private void sendListing(ChannelHandlerContext ctx, File dir) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, OK);
        response.headers().set(CONTENT_TYPE, "text/html;charset=UTF-8");
        StringBuilder buf = new StringBuilder();
        String dirPath = dir.getPath();
        buf.append("<!DOCTYPE html> \r\n");
        buf.append("<html><head><title>");
        buf.append(dirPath);
        buf.append("目录：");
        buf.append("</title></head><body>\r\n");
        buf.append("<h3>");
        buf.append(dirPath).append("目录：");
        buf.append("</h3>\r\n");
        buf.append("<ul>");
        buf.append("<li>链接：<a href=\"../\">..</a><li>\r\n");
        //遍历目录下文件
        for (File f : Objects.requireNonNull(dir.listFiles())) {
            if (f.isHidden() || !f.canRead()) {
                continue;
            }
            String name = f.getName();
            if (!ALLOWED_FILE_NAME.matcher(name).matches()) {
                continue;
            }
            buf.append("<li>链接:<a href=\"");
            buf.append(name);
            buf.append("\">");
            buf.append(name);
            buf.append("</a></li>\r\n");
        }
        buf.append("</ul></body></html>\r\n");
        //分配对应消息的缓冲对象
        ByteBuf buffer = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
        //将缓冲区中的响应消磁存放到HTTP应答消息中，释放缓冲区
        response.content().writeBytes(buffer);
        buffer.release();
        //将响应消息发送到缓冲区并刷新到SocketChannel中
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private String sanitizeUri(String uri) {
        try {
            uri = URLDecoder.decode(uri,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new Error();
        }
        if(!uri.startsWith(url)){
            return null;
        }
        if(!uri.startsWith("/")){
            return null;
        }
        uri = uri.replace('/',File.separatorChar);
        if(uri.contains(File.separator + ".")
                || uri.contains("." + File.separator) || uri.startsWith(".")
        || uri.endsWith(".") || INSECURE_URI.matcher(uri).matches()){
            return null;
        }
        return System.getProperty("user.dir") + File.separator + uri;
    }

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                Unpooled.copiedBuffer("Failure:" + status.toString() + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE, "text/plain;charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

}
