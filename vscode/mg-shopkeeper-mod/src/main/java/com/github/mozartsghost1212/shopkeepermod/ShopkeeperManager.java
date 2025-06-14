package com.github.mozartsghost1212.shopkeepermod;

import com.google.gson.*;
import com.jcraft.jorbis.Block;

import net.minecraft.util.math.BlockPos;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class ShopkeeperManager {
    private static final List<Shop> shops = new ArrayList<>();

    public static void addShop(Shop shop) {
        shops.add(shop);
        saveShops();
    }

    // public static void removeShopById(String shopId) {
    //     shops.removeIf(shop -> shop.shopId.equals(shopId));
    //     saveShops();
    // }

    public static List<Shop> getShops() {
        return shops;
    }

    public static void loadShops() {
        shops.clear();
        try {
            Path path = Paths.get("config/shopkeepers.json");
            if (!Files.exists(path))
                return;
            String json = Files.readString(path);
            JsonArray array = JsonParser.parseString(json).getAsJsonArray();
            for (JsonElement elem : array) {
                JsonObject obj = elem.getAsJsonObject();
                UUID entityUuid = UUID.fromString(obj.get("entityUuid").getAsString());
                BlockPos originBlock = new BlockPos(
                        obj.get("originBlock").getAsJsonObject().get("x").getAsInt(),
                        obj.get("originBlock").getAsJsonObject().get("y").getAsInt(),
                        obj.get("originBlock").getAsJsonObject().get("z").getAsInt());
                List<BlockPos> allBlocks = new ArrayList<>();
                JsonArray allBlocksArray = obj.get("allBlocks").getAsJsonArray();
                for (JsonElement blockElem : allBlocksArray) {
                    JsonObject blockObj = blockElem.getAsJsonObject();
                    BlockPos blockPos = new BlockPos(
                            blockObj.get("x").getAsInt(),
                            blockObj.get("y").getAsInt(),
                            blockObj.get("z").getAsInt());
                    allBlocks.add(blockPos);
                }                        
                String type = obj.get("type").getAsString();
                String shopId = obj.has("shopId") ? obj.get("shopId").getAsString() : "";
                String owner = obj.has("owner") ? obj.get("owner").getAsString() : "";
                int size = obj.has("size") ? obj.get("size").getAsInt() : 0;
                Shop shop = new Shop(entityUuid, originBlock, allBlocks, type, shopId, owner, size);
                shops.add(shop);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveShops() {
        JsonArray array = new JsonArray();
        for (Shop shop : shops) {
            JsonObject obj = new JsonObject();
            obj.addProperty("entityUuid", shop.getEntityUuid().toString());
            JsonObject originBlock = new JsonObject();
            originBlock.addProperty("x", shop.getOriginBlock().getX());
            originBlock.addProperty("y", shop.getOriginBlock().getY());
            originBlock.addProperty("z", shop.getOriginBlock().getZ());
            obj.add("originBlock", originBlock);
            obj.addProperty("type", shop.getShopType());
            obj.addProperty("shopId", shop.getShopUuid());
            obj.addProperty("owner", shop.getOwner());
            obj.addProperty("size", shop.getSize());
            JsonArray allBlocks = new JsonArray();
            for (BlockPos block : shop.getAllBlocks()) {
                JsonObject blockObj = new JsonObject();
                blockObj.addProperty("x", block.getX());
                blockObj.addProperty("y", block.getY());
                blockObj.addProperty("z", block.getZ());
                allBlocks.add(blockObj);
            }
            obj.add("allBlocks", allBlocks);
            array.add(obj);
        }
        try {
            Files.createDirectories(Paths.get("config"));
            Files.writeString(Paths.get("config/shopkeepers.json"), array.toString(), StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}