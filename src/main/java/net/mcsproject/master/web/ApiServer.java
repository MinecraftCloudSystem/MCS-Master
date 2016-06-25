/*
 *     MCS - Minecraft Cloud System
 *     Copyright (C) 2016
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.mcsproject.master.web;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.mcsproject.master.web.packet.ListenerRegistry;
import net.mcsproject.master.web.packet.PacketEncoder;
import net.mcsproject.master.web.packet.PacketMessageHandler;
import net.mcsproject.master.web.packet.PacketRegistry;
import net.mcsproject.master.web.packets.*;

@Log4j2
public class ApiServer {

	@Getter
	private PacketRegistry packetRegistry;
	@Getter
	private ListenerRegistry listenerRegistry;

	public ApiServer(int port) {
		this.packetRegistry = new PacketRegistry();
		this.listenerRegistry = new ListenerRegistry();

		new Thread(() -> {
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
								.addLast(new StringDecoder())
								.addLast(new PacketEncoder(packetRegistry))
								.addLast(new PacketMessageHandler(packetRegistry, listenerRegistry));
					}
				});
				bootstrap.option(ChannelOption.SO_BACKLOG, 50);
				bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

				ChannelFuture future = bootstrap.bind(port);
				log.info("Web api server started!");
				future.channel().closeFuture().sync();
				log.info("Web api server stopped!");
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				bossGroup.shutdownGracefully();
				workerGroup.shutdownGracefully();
			}
		}).start();
	}

	private void registerPackets() {
		PacketRegistry reg = this.packetRegistry;
		reg.addPacket("daemons", PacketDeamons.class);
		reg.addPacket("log", PacketLog.class);
		reg.addPacket("login-cookie", PacketLoginCookie.class);
		reg.addPacket("login-password", PacketLoginPassword.class);
		reg.addPacket("login-response", PacketLoginResponse.class);
		reg.addPacket("logout", PacketLogout.class);
		reg.addPacket("plugins", PacketPlugins.class);
		reg.addPacket("request", PacketRequest.class);
		reg.addPacket("servertypes", PacketServertypes.class);
		reg.addPacket("worlds", PacketWorlds.class);
	}

}
