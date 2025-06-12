package com.github.mozartsghost1212.shopkeepermod;

import java.util.UUID;

import com.google.common.base.Supplier;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.block.Blocks;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.TradedItem;

public class ShopCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("shopkeeper")
                .requires(source -> source.hasPermissionLevel(2)) // Restrict to Half operators
                .then(CommandManager.literal("spawn")
                        .then(CommandManager.argument("shop_type", StringArgumentType.word())
                                .executes(ctx -> spawnShopkeeper(ctx.getSource(),
                                        StringArgumentType.getString(ctx, "shop_type")))))
                .then(CommandManager.literal("list")
                        .executes(ctx -> listShopkeepers(ctx.getSource())))
                .then(CommandManager.literal("remove")
                        .then(CommandManager.argument("shopId", StringArgumentType.word())
                                .executes(ctx -> removeShopkeeper(ctx.getSource(),
                                        StringArgumentType.getString(ctx, "shopId")))))
                .then(CommandManager.literal("reloadTypes")
                        .executes(ctx -> reloadShopTypes(ctx.getSource())))
                .then(CommandManager.literal("reload")
                        .executes(ctx -> reloadShopkeepers(ctx.getSource()))));
    }

    public static int spawnShopkeeper(ServerCommandSource source, String shopType) {
        ServerWorld world = source.getWorld();
        ServerPlayerEntity player = source.getPlayer();
        BlockPos origin = player.getBlockPos().add(2, 0, 2);

        // Build simple 5x5 shop
        int size = ShopTypeManager.getShopSize(shopType);
        for (int x = -size; x <= size; x++) {
            for (int z = -2; z <= 2; z++) {
                world.setBlockState(origin.add(x, -1, z), Blocks.STONE.getDefaultState());
                for (int y = 0; y <= 3; y++) {
                    if (x == -2 || x == 2 || z == -2 || z == 2) {
                        world.setBlockState(origin.add(x, y, z), Blocks.OAK_PLANKS.getDefaultState());
                    }
                }
                world.setBlockState(origin.add(x, 4, z), Blocks.OAK_SLAB.getDefaultState());
            }
        }

        // Spawn villager
        VillagerEntity villager = EntityType.VILLAGER.create(
                world,
                null, // No consumer needed
                origin,
                SpawnReason.EVENT,
                false, // alignPosition
                false // invertY
        );
        if (villager != null) {
            villager.refreshPositionAndAngles(origin.getX() + 0.5, origin.getY(), origin.getZ() + 0.5, 0, 0);
            villager.setAiDisabled(true);
            villager.setPersistent();
            villager.setCustomName(Text.of(shopType + " Shopkeeper"));
            villager.setCustomNameVisible(true);

            // Example trades
            if (shopType.equalsIgnoreCase("blacksmith")) {
                villager.getOffers().add(new net.minecraft.village.TradeOffer(
                        new TradedItem(Items.EMERALD, 5),
                        new ItemStack(Items.DIAMOND_SWORD),
                        10, 5, 0.05f));
            } else if (shopType.equalsIgnoreCase("foodstall")) {
                villager.getOffers().add(new net.minecraft.village.TradeOffer(
                        new TradedItem(Items.EMERALD, 1),
                        new ItemStack(Items.BREAD),
                        10, 5, 0.05f));
            }

            world.spawnEntity(villager);
            String shopId = "shop_" + System.currentTimeMillis();
            String owner = source.getPlayer().getGameProfile().getName();
            Shop shop = new Shop(villager.getUuid(), origin, shopType, shopId, owner, size);
            ShopkeeperManager.addShop(shop);
        }

        Supplier<Text> feedback = () -> Text.of("Spawned shopkeeper of type: " + shopType + " at " + origin);
        source.sendFeedback(feedback, false);
        return 1;
    }

    public static int listShopkeepers(ServerCommandSource source) {
        for (Shop shop : ShopkeeperManager.getShops()) {
            Supplier<Text> feedback = () -> Text
                    .of("Shopkeeper ID: " + shop.shopId + " | Owner: " + shop.owner + " | Size: " + shop.size
                            + " | UUID: " + shop.villagerUuid + " at " + shop.pos + " type: " + shop.shopType);
            source.sendFeedback(feedback, false);
        }
        return 1;
    }

    public static int removeShopkeeper(ServerCommandSource source, String shopId) {
        ShopkeeperManager.removeShopById(shopId);
        Supplier<Text> feedback = () -> Text.of("Removed shopkeeper with shopId: " + shopId);
        source.sendFeedback(feedback, false);
        return 1;
    }

    public static int reloadShopTypes(ServerCommandSource source) {
        ShopTypeManager.loadShopTypes();
        Supplier<Text> feedback = () -> Text.of("Shop types reloaded.");
        source.sendFeedback(feedback, false);
        return 1;
    }

    public static int reloadShopkeepers(ServerCommandSource source) {
        ServerWorld world = source.getWorld();
        for (Shop shop : ShopkeeperManager.getShops()) {
            // Rebuild structure
            BlockPos origin = shop.pos;
            int size = shop.size;
            for (int x = -size; x <= size; x++) {
                for (int z = -2; z <= 2; z++) {
                    world.setBlockState(origin.add(x, -1, z), net.minecraft.block.Blocks.STONE.getDefaultState());
                    for (int y = 0; y <= 3; y++) {
                        if (x == -2 || x == 2 || z == -2 || z == 2) {
                            world.setBlockState(origin.add(x, y, z),
                                    net.minecraft.block.Blocks.OAK_PLANKS.getDefaultState());
                        }
                    }
                    world.setBlockState(origin.add(x, 4, z), net.minecraft.block.Blocks.OAK_SLAB.getDefaultState());
                }
            }

            // Check if Villager exists
            boolean exists = world.getEntitiesByClass(net.minecraft.entity.passive.VillagerEntity.class,
                    new net.minecraft.util.math.Box(
                            origin.add(-1, -1, -1).getX(), origin.add(-1, -1, -1).getY(), origin.add(-1, -1, -1).getZ(),
                            origin.add(1, 3, 1).getX(), origin.add(1, 3, 1).getY(), origin.add(1, 3, 1).getZ()),
                    v -> v.getUuid().equals(shop.villagerUuid)).size() > 0;

            if (!exists) {
                // Respawn Villager
                VillagerEntity villager = EntityType.VILLAGER.create(
                        world,
                        null, // No consumer needed
                        origin,
                        SpawnReason.EVENT,
                        false, // alignPosition
                        false // invertY
                );

                if (villager != null) {
                    villager.refreshPositionAndAngles(origin.getX() + 0.5, origin.getY(), origin.getZ() + 0.5, 0, 0);
                    villager.setAiDisabled(true);
                    villager.setPersistent();
                    villager.setCustomName(Text.of(shop.shopType + " Shopkeeper"));
                    villager.setCustomNameVisible(true);

                    // Add trades
                    for (TradeDefinition trade : ShopTypeManager.getTrades(shop.shopType)) {
                        villager.getOffers().add(new net.minecraft.village.TradeOffer(
                                new TradedItem(Registries.ITEM.get(Identifier.of(trade.costItem)), trade.costCount),
                                new ItemStack(Registries.ITEM.get(Identifier.of(trade.resultItem)), trade.resultCount),
                                trade.maxUses, 5, 0.05f));
                    }

                    world.spawnEntity(villager);
                    shop.villagerUuid = villager.getUuid();
                }
            }
        }
        ShopkeeperManager.saveShops();
        Supplier<Text> feedback = () -> Text.of("Shopkeepers reloaded.");
        source.sendFeedback(feedback, false);
        return 1;
    }

}