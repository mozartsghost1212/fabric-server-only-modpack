package com.github.mozartsghost1212.shopkeepermod;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the configuration for a shop type, including its size and trades.
 */
public class ShopTypeDefinition {

    public static final int DEFAULT_SHOP_SIZE = 5;

    private String type;
    private String name;
    private String description;
    private int size = DEFAULT_SHOP_SIZE;
    private List<TradeDefinition> trades;

    /**
     * Default constructor for ShopTypeConfig.
     * Initializes the type, name, description, size, and trades.
     */
    public ShopTypeDefinition() {
        this.type = "";
        this.name = "";
        this.description = "";
        this.size = 5; // Default size, can be adjusted as needed
        this.trades = new ArrayList<>();
    }

    /**
     * Constructs a new ShopTypeConfig with specified values.
     *
     * @param type        the type of the shop
     * @param name        the name of the shop
     * @param description a brief description of the shop
     * @param size        the size of the shop (e.g., number of inventory slots)
     * @param trades      a list of trade definitions associated with this shop type
     */ 
    public ShopTypeDefinition(String type, String name, String description, int size, List<TradeDefinition> trades) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.size = size;
        this.trades = trades != null ? trades : new ArrayList<>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<TradeDefinition> getTrades() {
        if (trades == null) {
            trades = new ArrayList<>();
        }   
        return trades;
    }

    public void setTrades(List<TradeDefinition> trades) {
        this.trades = trades;
    }

    @Override
    public String toString() {
        return "ShopTypeDefinition {" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", size=" + size +
                ", trades=" + trades +
                '}';
    }
}