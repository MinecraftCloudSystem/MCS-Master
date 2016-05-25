package net.mcsproject.master.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.mcsproject.master.network.packet.Packet;
import net.mcsproject.master.network.packet.PacketRegistry;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) throws Exception {
        byte id = PacketRegistry.getInstance().getIdByPacket(packet.getClass());

        byteBuf.writeByte(id);
        packet.write(byteBuf);
    }

}
