package dev.ftb.mods.ftbteams.net;

import dev.architectury.networking.NetworkManager;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.property.TeamPropertyCollection;
import dev.ftb.mods.ftbteams.client.FTBTeamsClient;
import dev.ftb.mods.ftbteams.data.TeamPropertyCollectionImpl;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.UUID;

public record UpdatePropertiesResponseMessage(UUID teamId, TeamPropertyCollection properties) implements CustomPacketPayload {
	public static final Type<UpdatePropertiesResponseMessage> TYPE = new Type<>(FTBTeamsAPI.rl("update_properties_response"));

	public static StreamCodec<RegistryFriendlyByteBuf, UpdatePropertiesResponseMessage> STREAM_CODEC = StreamCodec.composite(
			UUIDUtil.STREAM_CODEC, UpdatePropertiesResponseMessage::teamId,
			TeamPropertyCollectionImpl.STREAM_CODEC, UpdatePropertiesResponseMessage::properties,
			UpdatePropertiesResponseMessage::new
	);

	public static void handle(UpdatePropertiesResponseMessage message, NetworkManager.PacketContext context) {
		context.queue(() -> FTBTeamsClient.updateSettings(message.teamId, message.properties));
	}

	@Override
	public Type<UpdatePropertiesResponseMessage> type() {
		return TYPE;
	}
}
