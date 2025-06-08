package com.github.mozartsghost1212.customdaynightmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

public class CustomDayNightMod implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConfig.loadConfig();

        ServerTickEvents.START_SERVER_TICK.register(server -> onServerTick(server));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            CustomDayNightCommand.register(dispatcher);
        });
    }

    private void onServerTick(MinecraftServer server) {
        for (ServerWorld world : server.getWorlds()) {
            if (world.getRegistryKey() == ServerWorld.OVERWORLD) {
                long time = world.getTimeOfDay() % 24000L;
                float multiplier;

                if (ModConfig.absoluteDayLength > 0 && time < 12000L) {
                    multiplier = (24000f / 2) / ModConfig.absoluteDayLength;
                } else if (ModConfig.absoluteNightLength > 0 && time >= 12000L) {
                    multiplier = (24000f / 2) / ModConfig.absoluteNightLength;
                } else {
                    multiplier = (time < 12000L) ? ModConfig.dayMultiplier : ModConfig.nightMultiplier;
                }

                long newTime = world.getTimeOfDay() + (long) multiplier;
                world.setTimeOfDay(newTime);
            }
        }
    }
}