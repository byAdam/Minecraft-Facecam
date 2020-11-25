package net.byadam.facecam.common;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.UUID;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class FacecamMessage {

	public static UUID uuid;
	public static byte[] image;
	
	public FacecamMessage(UUID uuid, byte[] image) {
		this.uuid = uuid;
		this.image = image;
	}

	public static void encode(FacecamMessage msg, PacketBuffer buf) {


		String stringUUID = uuid.toString();
	
        
		buf.writeString(stringUUID);
		buf.writeBytes(image);
		
	}
	public static FacecamMessage decode(PacketBuffer buf) {
		// Remove first character
		buf.readByte();
		buf.discardReadBytes();
	
		ByteBuf uuidBytes = buf.readBytes(36);
		UUID newUUID = UUID.fromString(uuidBytes.toString(Charset.defaultCharset()));
		buf.discardReadBytes();
		
		// Convert ByteBuf to to bytearray
		byte imageBytes[] = new byte[buf.readableBytes()];
	    buf.getBytes(buf.readerIndex(), imageBytes);
		
		return new FacecamMessage(newUUID, imageBytes);
	}

	public static class Handler {

		public static void handle(FacecamMessage msg, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {

				ServerPlayerEntity sender = ctx.get().getSender();
				
				if(sender == null)
				{
					Facecam.facecamClient.webcamData.getInstance().onNewImage(uuid, image);
				}
				
				else
				{
					Facecam.packetHandler.sendToAll(new FacecamMessage(uuid, image));
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
}