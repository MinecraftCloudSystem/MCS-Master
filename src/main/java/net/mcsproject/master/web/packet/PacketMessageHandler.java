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

package net.mcsproject.master.web.packet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PacketMessageHandler extends SimpleChannelInboundHandler<String> {

	@NonNull
	private PacketRegistry packetRegistry;
	@NonNull
	private ListenerRegistry listenerRegistry;

	private Gson gson = new Gson();
	private JsonParser parser = new JsonParser();

	@Override
	protected void messageReceived(ChannelHandlerContext channelHandlerContext, String data) throws Exception {
		JsonObject object = parser.parse(data).getAsJsonObject();
		Class<? extends Packet> clazz = this.packetRegistry.getPacketByType(object.get("type").getAsString());
		object.remove("type");

		listenerRegistry.callEvent(gson.fromJson(object, clazz));
	}

}

