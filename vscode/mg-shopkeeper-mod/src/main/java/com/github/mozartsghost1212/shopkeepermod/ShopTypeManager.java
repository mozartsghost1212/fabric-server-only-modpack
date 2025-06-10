package com.github.mozartsghost1212.shopkeepermod;

import com.google.gson.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class ShopTypeManager {
    public static class ShopTypeConfig {
        public int size = 2;
        public List<TradeDefinition> trades = new ArrayList<>();
    }

    private static final Map<String, ShopTypeConfig> shopTypes = new HashMap<>();

    public static void loadShopTypes() {
        shopTypes.clear();
        Path path = Paths.get("config/shop_types.json");
        try {
            if (!Files.exists(path))
                return;
            String json = Files.readString(path);
            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
            for (String shopType : obj.keySet()) {
                JsonObject shopObj = obj.getAsJsonObject(shopType);
                ShopTypeConfig config = new ShopTypeConfig();
                if (shopObj.has("size")) {
                    config.size = shopObj.get("size").getAsInt();
                }
                JsonArray array = shopObj.getAsJsonArray("trades");
                for (JsonElement elem : array) {
                    JsonObject tradeObj = elem.getAsJsonObject();
                    TradeDefinition trade = new TradeDefinition();
                    trade.costItem = tradeObj.get("cost").getAsJsonObject().get("item").getAsString();
                    trade.costCount = tradeObj.get("cost").getAsJsonObject().get("count").getAsInt();
                    trade.resultItem = tradeObj.get("result").getAsJsonObject().get("item").getAsString();
                    trade.resultCount = tradeObj.get("result").getAsJsonObject().get("count").getAsInt();
                    trade.maxUses = tradeObj.get("max_uses").getAsInt();
                    config.trades.add(trade);
                }
                shopTypes.put(shopType, config);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<TradeDefinition> getTrades(String shopType) {
        return shopTypes.containsKey(shopType) ? shopTypes.get(shopType).trades : Collections.emptyList();
    }

    public static int getShopSize(String shopType) {
        return shopTypes.containsKey(shopType) ? shopTypes.get(shopType).size : 2;
    }
}