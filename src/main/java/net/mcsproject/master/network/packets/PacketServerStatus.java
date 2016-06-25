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
package net.mcsproject.master.network.packets;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import lombok.AllArgsConstructor;
import net.mcsproject.master.network.packet.Packet;

import java.io.IOException;

@AllArgsConstructor
public class PacketServerStatus extends Packet {

	private int cpuUsage;
	private int ramUsage;
	private int ramMax;

	public PacketServerStatus() {
	}

	@Override
	public void read(ByteBufInputStream byteBuf) throws IOException {
		this.cpuUsage = byteBuf.readInt();
		this.ramUsage = byteBuf.readInt();
		this.ramMax = byteBuf.readInt();
	}

	@Override
	public void write(ByteBufOutputStream byteBuf) throws IOException {
		byteBuf.writeInt(this.cpuUsage);
		byteBuf.writeInt(this.ramUsage);
		byteBuf.writeInt(this.ramMax);
	}

}
