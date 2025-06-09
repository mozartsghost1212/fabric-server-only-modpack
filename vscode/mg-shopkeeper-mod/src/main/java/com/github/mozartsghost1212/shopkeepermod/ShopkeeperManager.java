package com.github.mozartsghost1212.shopkeepermod;



import com.google.gson.*;
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

    public static void removeShopById(String shopId) {
        shops.removeIf(shop -> shop.shopId.equals(shopId));
        saveShops();
    }

    public static List<Shop> getShops() {
        return shops;
    }

    public static void loadShops() {
        shops.clear();
        try {
            Path path = Paths.get("config/shopkeepers.json");
            if (!Files.exists(path)) return;
            String json = Files.readString(path);
            JsonArray array = JsonParser.parseString(json).getAsJsonArray();
            for (JsonElement elem : array) {
                JsonObject obj = elem.getAsJsonObject();
                UUID uuid = UUID.fromString(obj.get("uuid").getAsString());
                BlockPos pos = new BlockPos(
                        obj.get("x").getAsInt(),
                        obj.get("y").getAsInt(),
                        obj.get("z").getAsInt());
                String type = obj.get("type").getAsString();
                String shopId = obj.has("shopId") ? obj.get("shopId").getAsString() : "legacy_" + uuid;
                String owner = obj.has("owner") ? obj.get("owner").getAsString() : "unknown";
                int size = obj.has("size") ? obj.get("size").getAsInt() : 2;
                shops.add(new Shop(uuid, pos, type, shopId, owner, size));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveShops() {
        JsonArray array = new JsonArray();
        for (Shop shop : shops) {
            JsonObject obj = new JsonObject();
            obj.addProperty("uuid", shop.villagerUuid.toString());
            obj.addProperty("x", shop.pos.getX());
            obj.addProperty("y", shop.pos.getY());
            obj.addProperty("z", shop.pos.getZ());
            obj.addProperty("type", shop.shopType);
            obj.addProperty("shopId", shop.shopId);
            obj.addProperty("owner", shop.owner);
            obj.addProperty("size", shop.size);
            array.add(obj);
        }
        try {
            Files.createDirectories(Paths.get("config"));
            Files.writeString(Paths.get("config/shopkeepers.json"), array.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}