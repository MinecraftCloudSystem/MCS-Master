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

package net.mcsproject.master.network.packet;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.InvocationTargetException;

@Log4j2
public class PacketRegistry {

	private static PacketRegistry instance;
	private BiMap<Byte, Class<?>> packets = HashBiMap.create();

	public void addPacket(byte id, Class<?> packetClass) {
		this.packets.put(id, packetClass);
	}

	public Packet getPacketById(byte id) {
		try {
			return (Packet) this.packets.get(id).getConstructors()[0].newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			log.error(ex.getMessage());
		}
		return null;
	}

	public byte getIdByPacket(Class<?> clazz) {
		return this.packets.inverse().getOrDefault(clazz, (byte) 0x00);
	}

}
