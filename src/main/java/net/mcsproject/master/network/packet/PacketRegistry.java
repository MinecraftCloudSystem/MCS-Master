package net.mcsproject.master.network.packet;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.mcsproject.master.logging.Log;

import java.lang.reflect.InvocationTargetException;

public class PacketRegistry {

    private static PacketRegistry instance;
    public static PacketRegistry getInstance() {
        if(instance == null) instance = new PacketRegistry();
        return instance;
    }

    private BiMap<Byte, Class<? extends Packet>> packets = HashBiMap.create();

    private PacketRegistry() {
    }

    public void addPacket(byte id, Class<? extends Packet> packetClass) {
        this.packets.put(id, packetClass);
    }

    public Packet getPacketById(byte id) {
        try {
            return (Packet) this.packets.get(id).getConstructors()[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Log.error(ex.getMessage());
        }
        return null;
    }

    public byte getIdByPacket(Class<? extends Packet> clazz) {
        return this.packets.inverse().getOrDefault(clazz, (byte) 0x00);
    }

}
