package com.github.mozartsghost1212.shopkeepermod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Manages the registration and configuration of different shop types for the
 * shopkeeper mod.
 * <p>
 * The {@code ShopTypeManager} is responsible for loading shop type definitions
 * from a JSON configuration
 * file, providing access to shop type properties (such as size and trade
 * definitions), and exposing
 * utility methods for querying available shop types and their associated
 * trades.
 * </p>
 * <p>
 * Shop types are defined in the {@code config/shop_types.json} file, where each
 * shop type specifies
 * its inventory size and a list of trade definitions. The manager loads these
 * definitions at class
 * initialization and provides static methods to retrieve shop type information
 * at runtime.
 * </p>
 * <h2>Usage Example</h2>
 * 
 * <pre>
 * List&lt;ShopTypeManager.TradeDefinition&gt; trades = ShopTypeManager.getTrades("basic_shop");
 * int size = ShopTypeManager.getShopSize("basic_shop");
 * Set&lt;String&gt; types = ShopTypeManager.getShopTypes();
 * </pre>
 * 
 * <h2>Thread Safety</h2>
 * <p>
 * All methods are static and the internal state is managed via a static map.
 * This class is not
 * thread-safe if shop types are reloaded concurrently.
 * </p>
 */
public class ShopTypeManager {

    /**
     * A static map that holds the configuration for each shop type,
     * where the key is the shop type's name and the value is its corresponding
     * {@link ShopTypeDefinition}.
     * This map is used to manage and retrieve configurations for different shop
     * types within the mod.
     */
    private static final Map<String, ShopTypeDefinition> shopTypes = new HashMap<>();

    /**
     * Loads shop types from the "config/shop_types.json" file into the
     * {@code shopTypes} map.
     * <p>
     * This method clears the existing shop types and attempts to read the JSON
     * configuration file.
     * For each shop type defined in the JSON, it creates a {@link ShopTypeDefinition}
     * instance,
     * sets its properties (such as size and trades), and populates it with
     * {@link TradeDefinition} objects
     * based on the trade definitions in the file.
     * <p>
     * If the configuration file does not exist, the method returns without
     * modifying the shop types.
     * Any {@link IOException} encountered during reading or parsing the file is
     * printed to the standard error stream.
     */
    public static void loadShopTypes() {
        shopTypes.clear();
        Path path = Paths.get("config/shop_types.json");
        try {
            if (!Files.exists(path)) {
                return;
            }
            // Read the JSON file and parse it into a JsonObject
            String json = Files.readString(path);
            JsonArray shopArray = JsonParser.parseString(json).getAsJsonArray();
            for (JsonElement shopTypeElem : shopArray) {
                JsonObject shopObj = shopTypeElem.getAsJsonObject();
                ShopTypeDefinition config = new ShopTypeDefinition();
                if (shopObj.has("type")) {
                    String type = shopObj.get("type").getAsString();
                    config.setType(type);
                    shopTypes.put(type, config);
                }
                if (shopObj.has("name")) {
                    config.setName(shopObj.get("name").getAsString());
                }
                if (shopObj.has("description")) {
                    config.setDescription(shopObj.get("description").getAsString());
                }
                if (shopObj.has("size")) {
                    config.setSize(shopObj.get("size").getAsInt());
                }
                JsonArray array = shopObj.getAsJsonArray("trades");
                for (JsonElement elem : array) {
                    JsonObject tradeObj = elem.getAsJsonObject();
                    TradeDefinition trade = new TradeDefinition();
                    trade.setCostItem(tradeObj.get("cost").getAsJsonObject().get("item").getAsString());
                    trade.setCostCount(tradeObj.get("cost").getAsJsonObject().get("count").getAsInt());
                    trade.setResultItem(tradeObj.get("result").getAsJsonObject().get("item").getAsString());
                    trade.setResultCount(tradeObj.get("result").getAsJsonObject().get("count").getAsInt());
                    trade.setMaxUses(tradeObj.get("max_uses").getAsInt());
                    config.getTrades().add(trade);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the list of trade definitions associated with the specified shop
     * type.
     *
     * @param shopType the identifier of the shop type whose trades are to be
     *                 retrieved
     * @return a list of {@link TradeDefinition} objects for the given shop type,
     *         or an empty list if the shop type does not exist
     */
    public static List<TradeDefinition> getTrades(String shopType) {
        return shopTypes.containsKey(shopType) ? shopTypes.get(shopType).getTrades() : Collections.emptyList();
    }

    /**
     * Returns the size of the shop for the specified shop type.
     * <p>
     * If the given shop type exists in the {@code shopTypes} map, this method
     * returns
     * the associated size. Otherwise, it returns a default size of 2.
     *
     * @param shopType the type of the shop whose size is to be retrieved
     * @return the size of the specified shop type, or 2 if the shop type is not
     *         found
     */
    public static int getShopSize(String shopType) {
        return shopTypes.containsKey(shopType) ? shopTypes.get(shopType).getSize() : ShopTypeDefinition.DEFAULT_SHOP_SIZE;
    }

    /**
     * Returns a set containing the names of all registered shop types.
     *
     * @return a {@link Set} of {@link String} representing the shop type names
     */
    public static Set<String> getShopTypes() {
        return shopTypes.keySet();
    }

    /**
     * Retrieves the {@link ShopTypeDefinition} for a given shop type.
     *
     * @param shopType the type of the shop to retrieve
     * @return the {@link ShopTypeDefinition} for the specified shop type, or null
     *         if it does not exist
     */
    public static ShopTypeDefinition getShopType(String shopType) {
        return shopTypes.get(shopType);
    }
}