package com.github.mozartsghost1212.shopkeepermod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ShopkeeperMod implements ModInitializer {

    @Override
    public void onInitialize() {
        ShopkeeperManager.loadShops();
        ShopTypeManager.loadShopTypes();
        ShopProtection.register();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ShopCommand.register(dispatcher);
        });

        System.out.println("[ShopkeeperMod] Shopkeeper Mod successfully registered!");
    }
}