package net.mcsproject.master.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.mcsproject.master.network.packet.Packet;
import net.mcsproject.master.network.packet.PacketRegistry;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte id = byteBuf.readByte();

        Packet packet = PacketRegistry.getInstance().getPacketById(id);
        if(packet != null) {
            packet.read(byteBuf);
            list.add(packet);
        }
    }

}
