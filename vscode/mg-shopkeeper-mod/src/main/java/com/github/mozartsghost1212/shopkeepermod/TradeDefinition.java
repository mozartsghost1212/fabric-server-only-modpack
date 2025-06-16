package com.github.mozartsghost1212.shopkeepermod;

/**
 * Represents a trade definition for a shopkeeper, specifying the items and
 * quantities involved in a trade.
 *
 * <p>
 * Each trade consists of a cost item (with its quantity) and a result item
 * (with its quantity),
 * along with the maximum number of times the trade can be used.
 * </p>
 *
 * Fields:
 * <ul>
 * <li>{@code costItem} - The identifier of the item required as payment.</li>
 * <li>{@code costCount} - The quantity of the cost item required.</li>
 * <li>{@code resultItem} - The identifier of the item given as a result of the
 * trade.</li>
 * <li>{@code resultCount} - The quantity of the result item given.</li>
 * <li>{@code maxUses} - The maximum number of times this trade can be
 * used.</li>
 * </ul>
 */
public class TradeDefinition {
    private String costItem;
    private int costCount;
    private String resultItem;
    private int resultCount;
    private int maxUses;

    /**
     * Constructs a new TradeDefinition with default values.
     */
    public TradeDefinition() {
        this.costItem = "";
        this.costCount = 0;
        this.resultItem = "";
        this.resultCount = 0;
        this.maxUses = 0;
    }

    /**
     * Constructs a new TradeDefinition with specified values.
     *
     * @param costItem    the item required for the trade
     * @param costCount   the quantity of the cost item
     * @param resultItem  the item received from the trade
     * @param resultCount the quantity of the result item
     * @param maxUses     the maximum number of times this trade can be used
     */
    public TradeDefinition(String costItem, int costCount, String resultItem, int resultCount, int maxUses) {
        this.costItem = costItem;
        this.costCount = costCount;
        this.resultItem = resultItem;
        this.resultCount = resultCount;
        this.maxUses = maxUses;
    }

    public String getCostItem() {
        return costItem;
    }

    public void setCostItem(String costItem) {
        this.costItem = costItem;
    }

    public int getCostCount() {
        return costCount;
    }

    public void setCostCount(int costCount) {
        this.costCount = costCount;
    }

    public String getResultItem() {
        return resultItem;
    }

    public void setResultItem(String resultItem) {
        this.resultItem = resultItem;
    }

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public void setMaxUses(int maxUses) {
        this.maxUses = maxUses;
    }

    @Override
    public String toString() {
        return "TradeDefinition {" +
                "costItem='" + costItem + '\'' +
                ", costCount=" + costCount +
                ", resultItem='" + resultItem + '\'' +
                ", resultCount=" + resultCount +
                ", maxUses=" + maxUses +
                '}';
    }
}