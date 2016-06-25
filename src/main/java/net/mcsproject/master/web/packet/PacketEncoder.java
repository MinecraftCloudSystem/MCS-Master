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
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class PacketEncoder extends MessageToMessageDecoder<Packet> {

	private Gson gson = new Gson();
	private JsonParser parser = new JsonParser();

	private PacketRegistry packetRegistry;

	public PacketEncoder(PacketRegistry packetRegistry) {
		this.packetRegistry = packetRegistry;
	}

	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, Packet packet, List<Object> list) throws Exception {
		JsonObject object = gson.toJsonTree(packet).getAsJsonObject();
		object.addProperty("type", this.packetRegistry.getTypeByPacket(packet.getClass()));

		list.add(gson.toJson(object));
	}

}
