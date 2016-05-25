package net.mcsproject.master.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import net.mcsproject.master.logging.Log;
import net.mcsproject.master.network.packet.PacketMessageHandler;

public class DaemonServer {

    @Getter
    private Thread thread;

    public DaemonServer(int port) {
        this.thread = new Thread(() -> {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();

            try {
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup, workerGroup);
                bootstrap.channel(NioServerSocketChannel.class);
                bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                            .addLast(new PacketDecoder())
                            .addLast(new PacketEncoder())
                            .addLast(new PacketMessageHandler());
                    }
                });
                bootstrap.option(ChannelOption.SO_BACKLOG, 50);
                bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

                ChannelFuture future = bootstrap.bind(port).sync();
                Log.info("Server started!");
                future.channel().closeFuture().sync();
            } catch(Exception ex) {
                ex.printStackTrace();
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        });

        thread.setName("DaemonServer Thread");
        thread.start();
    }

}
