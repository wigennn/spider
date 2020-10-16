package com.redbyte.platform.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author wangwq
 */
@Component
public class WorkerClient {

    public static List<Channel> channelList = new ArrayList<>();

    @PostConstruct
    public void init() throws Exception {
        initBootstrap("127.0.0.1", 8761);
    }

    private Bootstrap initBootstrap(String host, int port) throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup loopGroup = new NioEventLoopGroup();
        bootstrap.group(loopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ByteBuf delimiter = Unpooled.copiedBuffer("".getBytes());
                        channel.pipeline()
                                .addLast(new DelimiterBasedFrameDecoder(1024, delimiter))
                                .addLast(new StringEncoder())
                                .addLast();
                    }
                });

        bootstrap.connect(host, port).sync();
        return bootstrap;
    }
}
