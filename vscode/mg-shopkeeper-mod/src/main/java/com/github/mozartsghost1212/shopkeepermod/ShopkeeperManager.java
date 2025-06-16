package com.github.mozartsghost1212.shopkeepermod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.util.math.BlockPos;

public class ShopkeeperManager {
    
    private static final List<Shop> allShops = new CopyOnWriteArrayList<>();
    private static final Set<BlockPos> allShopBlocks = ConcurrentHashMap.newKeySet();
    private static final Map<UUID, Shop> entityUuidToShop = new ConcurrentHashMap<>();
    private static final Map<UUID, Shop> shopUuidToShop = new ConcurrentHashMap<>();
    
    /**
     * Initializes the shopkeeper system by loading all shop data from the "config/shopkeepers.json" file.
     * <p>
     * This method clears the current list of shops and attempts to read the JSON file containing shopkeeper data.
     * For each shop entry in the JSON array, it parses the relevant fields such as entity UUID, origin block position,
     * all associated block positions, shop type, shop UUID, owner, and size. It then constructs a {@link Shop} object
     * and adds it to the list of all shops.
     * <p>
     * If the configuration file does not exist, the method returns without modifying the shop list.
     * Any {@link IOException} encountered during file reading is printed to the standard error stream.
     */
    public static void loadShops() {
        allShops.clear();
        allShopBlocks.clear();
        entityUuidToShop.clear();
        shopUuidToShop.clear();
        try {
            Path path = Paths.get("config/shopkeepers.json");
            if (!Files.exists(path))
                return;
            String json = Files.readString(path);
            JsonArray array = JsonParser.parseString(json).getAsJsonArray();
            for (JsonElement elem : array) {
                JsonObject obj = elem.getAsJsonObject();
                UUID entityUuid = UUID.fromString(obj.get("entityUuid").getAsString());
                UUID shopUuid = UUID.fromString(obj.get("shopUuid").getAsString());
                String type = obj.get("type").getAsString();
                String owner = obj.get("owner").getAsString();
                BlockPos origin = new BlockPos(
                        obj.get("origin").getAsJsonObject().get("x").getAsInt(),
                        obj.get("origin").getAsJsonObject().get("y").getAsInt(),
                        obj.get("origin").getAsJsonObject().get("z").getAsInt());
                List<BlockPos> blocks = new ArrayList<>();
                JsonArray allBlocksArray = obj.get("blocks").getAsJsonArray();
                for (JsonElement blockElem : allBlocksArray) {
                    JsonObject blockObj = blockElem.getAsJsonObject();
                    BlockPos blockPos = new BlockPos(
                            blockObj.get("x").getAsInt(),
                            blockObj.get("y").getAsInt(),
                            blockObj.get("z").getAsInt());
                    blocks.add(blockPos);
                }                        
                Shop shop = new Shop(entityUuid, shopUuid, origin, type, owner, blocks);
                allShops.add(shop);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves all current shops to a JSON file located at "config/shopkeepers.json".
     * <p>
     * This method serializes each {@link Shop} in the {@code allShops} collection into a JSON object,
     * including properties such as entity UUID, origin block coordinates, shop type, shop UUID, owner,
     * size, and all associated block positions. The resulting JSON array is then written to disk.
     * <p>
     * If the "config" directory does not exist, it will be created. The output file will be created or
     * overwritten as needed.
     * <p>
     * Any {@link IOException} encountered during the file operations will be printed to the standard error stream.
     */
    public static void saveShops() {
        JsonArray array = new JsonArray();
        for (Shop shop : allShops) {
            JsonObject obj = new JsonObject();
            obj.addProperty("entityUuid", shop.getEntityUuid().toString());
            obj.addProperty("shopUuid", shop.getShopUuid().toString());            
            obj.addProperty("type", shop.getType());
            obj.addProperty("owner", shop.getOwner());
            JsonObject origin = new JsonObject();
            origin.addProperty("x", shop.getOrigin().getX());
            origin.addProperty("y", shop.getOrigin().getY());
            origin.addProperty("z", shop.getOrigin().getZ());
            obj.add("origin", origin);            
            JsonArray allBlocks = new JsonArray();
            for (BlockPos block : shop.getBlocks()) {
                JsonObject blockObj = new JsonObject();
                blockObj.addProperty("x", block.getX());
                blockObj.addProperty("y", block.getY());
                blockObj.addProperty("z", block.getZ());
                allBlocks.add(blockObj);
            }
            obj.add("blocks", allBlocks);
            array.add(obj);
        }
        try {
            Files.createDirectories(Paths.get("config"));
            Files.writeString(Paths.get("config/shopkeepers.json"), 
                array.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new {@link Shop} to the list of all shops and registers its associated blocks.
     * This method is synchronized to ensure thread safety.
     * After adding the shop and its blocks, all shops are saved persistently.
     *
     * @param shop the {@link Shop} instance to add
     */
    public static void addShop(Shop shop) {
        synchronized (allShopBlocks) {
            allShopBlocks.addAll(shop.getBlocks());
            allShops.add(shop);
            entityUuidToShop.put(shop.getEntityUuid(), shop);
            shopUuidToShop.put(shop.getShopUuid(), shop);
            saveShops();
        }  
    }

    /**
     * Removes the specified {@link Shop} from the list of all shops and its associated blocks
     * from the global block collection. After removal, persists the updated shop data.
     *
     * This method is synchronized to ensure thread safety during the removal process.
     *
     * @param shop the {@link Shop} instance to be removed
     */
    public static void removeShop(Shop shop) {
        synchronized (allShopBlocks) {
            allShopBlocks.removeAll(shop.getBlocks());
            allShops.remove(shop);
            entityUuidToShop.remove(shop.getEntityUuid());
            shopUuidToShop.remove(shop.getShopUuid());  
            saveShops();
        } 
    }

    /**
     * Returns the list of all registered shops.
     *
     * @return a list containing all {@link Shop} instances managed by the ShopkeeperManager
     */
    public static List<Shop> getAllShops() {
        return allShops;
    }

    /**
     * Returns a set containing the positions of all shop blocks.
     *
     * @return a {@link Set} of {@link BlockPos} representing all shop block locations.
     */
    public static Set<BlockPos> getAllShopBlocks() {
        return allShopBlocks;
    }    
    
    /**
     * Retrieves the {@link Shop} associated with the given entity UUID.
     *
     * @param entityUuid the UUID of the entity
     * @return the {@link Shop} associated with the entity UUID, or {@code null} if no shop is found
     */    
    public static Shop getShopByEntityUuid(UUID entityUuid) {
        return entityUuidToShop.get(entityUuid);    
    }

    public static Shop getShopByShopUuid(UUID shopUuid) {
        return shopUuidToShop.get(shopUuid);
    }
}