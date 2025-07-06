package ru.harimasa;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import ru.harimasa.configuration.STHTConfig;

public class SomeTotemHandTransfer implements ClientModInitializer {
	private final TotemHandler totemHandler = new TotemHandler();

	@Override
	public void onInitializeClient() {
		STHTConfig.CONFIG.load();
		ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
	}

	private void onClientTick(MinecraftClient client) {
		totemHandler.handleTick(client);
	}
}