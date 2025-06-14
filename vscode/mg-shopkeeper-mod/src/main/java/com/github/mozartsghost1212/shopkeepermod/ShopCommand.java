package com.github.mozartsghost1212.shopkeepermod;

import java.util.UUID;

import com.google.common.base.Supplier;
import com.jcraft.jorbis.Block;
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
                                .suggests((context, builder) -> {
                                    for (String type : ShopTypeManager.getShopTypes()) {
                                        builder.suggest(type);
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> spawnShopkeeper(ctx.getSource(),
                                        StringArgumentType.getString(ctx, "shop_type")))))
                .then(CommandManager.literal("list")
                        .then(CommandManager.argument("shop_type", StringArgumentType.word())
                                .executes(ctx -> listShopkeepers(ctx.getSource())))));
        // Uncomment the following lines if you want to implement the remove and reload
        // commands;
        // .then(CommandManager.literal("remove")
        // .then(CommandManager.argument("shopId", StringArgumentType.word())
        // .executes(ctx -> removeShopkeeper(ctx.getSource(),
        // StringArgumentType.getString(ctx, "shopId")))))
        // .then(CommandManager.literal("reloadTypes")
        // .executes(ctx -> reloadShopTypes(ctx.getSource())))
        // .then(CommandManager.literal("reload")
        // .executes(ctx -> reloadShopkeepers(ctx.getSource()))));
        // .then(CommandManager.literal("remove")
        // .then(CommandManager.argument("uuid", UuidArgumentType.uuid())
        // .executes(ctx -> removeShopkeeper(ctx.getSource(),
        // UuidArgumentType.getUuid(ctx, "uuid")))))
        // .then(CommandManager.literal("reloadTypes")
        // .executes(ctx -> reloadShopTypes(ctx.getSource())))
        // .then(CommandManager.literal("reload")
        // .executes(ctx -> reloadShopkeepers(ctx.getSource()))));
    }

    public static int spawnShopkeeper(ServerCommandSource source, String shopType) {
        ServerWorld world = source.getWorld();
        ServerPlayerEntity player = source.getPlayer();
        
        BlockPos originBlock = player.getBlockPos().add(4, 0, 4);
        int shopSize = ShopTypeManager.getShopSize(shopType);
        UUID shopUuid = UUID.randomUUID();
        String shopOwner = source.getPlayer().getGameProfile().getName();
        Shop shop = new Shop(shopUuid, originBlock, shopType, shopOwner, shopSize);
        
        for (int x = -shopSize; x <= shopSize; x++) {
            for (int z = -shopSize; z <= shopSize; z++) {
                // Set floor block
                BlockPos floorBlock = originBlock.add(x, -1, z);
                world.setBlockState(floorBlock, Blocks.STONE.getDefaultState());
                shop.getBlocks().add(floorBlock);
                
                // Set wall blocks 3 high
                for (int y = 0; y <= 3; y++) {
                    if (x == -shopSize || x == shopSize || z == -shopSize || z == shopSize) {
                        BlockPos wallBlock = originBlock.add(x, y, z);
                        world.setBlockState(wallBlock, Blocks.OAK_PLANKS.getDefaultState());
                        shop.getBlocks().add(wallBlock);
                    }
                }
                
                // Set roof slab
                BlockPos roofBlock = originBlock.add(x, 4, z);
                world.setBlockState(roofBlock, Blocks.OAK_SLAB.getDefaultState());
                shop.getBlocks().add(roofBlock);
            }
        }

        // Spawn villager
        VillagerEntity villager = EntityType.VILLAGER.create(
                world,
                null, // No consumer needed
                originBlock,
                SpawnReason.EVENT,
                false, // alignPosition
                false // invertY
        );

        if (villager != null) {
            // Set villager properties
            villager.refreshPositionAndAngles(
                originBlock.getX() + 0.5, 
                originBlock.getY(), 
                originBlock.getZ() + 0.5, 0, 0);
            villager.setAiDisabled(true);
            villager.setInvulnerable(true);
            villager.setPersistent();
            villager.setCustomName(Text.of(shopType + " Shopkeeper"));
            villager.setCustomNameVisible(true);

            // Add trades
            for (ShopTypeManager.TradeDefinition trade : ShopTypeManager.getTrades(shopType)) {
                villager.getOffers().add(new net.minecraft.village.TradeOffer(
                    new TradedItem(Registries.ITEM.get(Identifier.of(trade.costItem)), trade.costCount),
                    new ItemStack(Registries.ITEM.get(Identifier.of(trade.resultItem)), trade.resultCount),
                    trade.maxUses, 5, 0.05f));
            }

            // Spawn the villager in the world
            world.spawnEntity(villager);
            shop.setEntityUuid(villager.getUuid());
            ShopkeeperManager.addShop(shop);
        }

        Supplier<Text> feedback = () -> Text.of("Spawned shopkeeper of type: " + shopType + " at " + originBlock);
        source.sendFeedback(feedback, false);
        return 1;
    }

    public static int listShopkeepers(ServerCommandSource source) {
        for (Shop shop : ShopkeeperManager.getShops()) {
            Supplier<Text> feedback = () -> Text
                    .of("Shopkeeper UUID: " + shop.getUuid() + " | Owner: " + shop.getOwner() + " | Size: "
                            + shop.getSize()
                            + " | UUID: " + shop.getEntityUuid() + " at " + shop.getOrigin() + " type: "
                            + shop.getType());
            source.sendFeedback(feedback, false);
        }
        return 1;
    }

    // public static int removeShopkeeper(ServerCommandSource source, UUID uuid) {
    // ShopkeeperManager.removeShopById(uuid.toString());
    // Supplier<Text> feedback = () -> Text.of("Removed shopkeeper with UUID: " +
    // uuid);
    // source.sendFeedback(feedback, false);
    // return 1;
    // }

    // public static int reloadShopTypes(ServerCommandSource source) {
    // ShopTypeManager.loadShopTypes();
    // Supplier<Text> feedback = () -> Text.of("Shop types reloaded.");
    // source.sendFeedback(feedback, false);
    // return 1;
    // }

    // public static int reloadShopkeepers(ServerCommandSource source) {
    // ServerWorld world = source.getWorld();
    // for (Shop shop : ShopkeeperManager.getShops()) {
    // // Rebuild structure
    // BlockPos origin = shop.firstBlock;
    // int size = shop.size;
    // for (int x = -size; x <= size; x++) {
    // for (int z = -size; z <= size; z++) {
    // world.setBlockState(origin.add(x, -1, z),
    // net.minecraft.block.Blocks.STONE.getDefaultState());
    // for (int y = 0; y <= 3; y++) {
    // if (x == -size || x == size || z == -size || z == size) {
    // world.setBlockState(origin.add(x, y, z),
    // net.minecraft.block.Blocks.OAK_PLANKS.getDefaultState());
    // }
    // }
    // world.setBlockState(origin.add(x, 4, z),
    // net.minecraft.block.Blocks.OAK_SLAB.getDefaultState());
    // }
    // }

    // // Check if Villager exists
    // boolean exists =
    // world.getEntitiesByClass(net.minecraft.entity.passive.VillagerEntity.class,
    // new net.minecraft.util.math.Box(
    // origin.add(-1, -1, -1).getX(), origin.add(-1, -1, -1).getY(), origin.add(-1,
    // -1, -1).getZ(),
    // origin.add(1, 3, 1).getX(), origin.add(1, 3, 1).getY(), origin.add(1, 3,
    // 1).getZ()),
    // v -> v.getUuid().equals(shop.entityUuid)).size() > 0;

    // if (!exists) {
    // // Respawn Villager
    // VillagerEntity villager = EntityType.VILLAGER.create(
    // world,
    // null, // No consumer needed
    // origin,
    // SpawnReason.EVENT,
    // false, // alignPosition
    // false // invertY
    // );

    // if (villager != null) {
    // villager.refreshPositionAndAngles(origin.getX() + 0.5, origin.getY(),
    // origin.getZ() + 0.5, 0, 0);
    // villager.setAiDisabled(true);
    // villager.setPersistent();
    // villager.setCustomName(Text.of(shop.shopType + " Shopkeeper"));
    // villager.setCustomNameVisible(true);

    // // Add trades
    // for (TradeDefinition trade : ShopTypeManager.getTrades(shop.shopType)) {
    // villager.getOffers().add(new net.minecraft.village.TradeOffer(
    // new TradedItem(Registries.ITEM.get(Identifier.of(trade.costItem)),
    // trade.costCount),
    // new ItemStack(Registries.ITEM.get(Identifier.of(trade.resultItem)),
    // trade.resultCount),
    // trade.maxUses, 5, 0.05f));
    // }

    // world.spawnEntity(villager);
    // shop.entityUuid = villager.getUuid();
    // }
    // }
    // }
    // ShopkeeperManager.saveShops();
    // Supplier<Text> feedback = () -> Text.of("Shopkeepers reloaded.");
    // source.sendFeedback(feedback, false);
    // return 1;
    // }

}