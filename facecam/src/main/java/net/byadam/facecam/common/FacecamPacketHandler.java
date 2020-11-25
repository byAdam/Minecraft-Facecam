package net.byadam.facecam.common;

import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class FacecamPacketHandler {
	
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
	    new ResourceLocation("facecam", "webcams"),
	    () -> PROTOCOL_VERSION,
	    PROTOCOL_VERSION::equals,
	    PROTOCOL_VERSION::equals
	);
	

	public void register()
	{
		INSTANCE.registerMessage(0, FacecamMessage.class, FacecamMessage::encode, FacecamMessage::decode, FacecamMessage.Handler::handle);
	}
	
	public void sendToServer(FacecamMessage msg)
	{
		INSTANCE.sendToServer(msg);
	}
	
	public void sendToAll(FacecamMessage msg)
	{
		INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
	}
}
