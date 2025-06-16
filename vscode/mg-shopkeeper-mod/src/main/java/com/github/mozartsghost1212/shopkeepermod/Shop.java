package com.github.mozartsghost1212.shopkeepermod;

import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a shop in the game world, defined by its unique identifiers,
 * location,
 * type, name, description, owner, size, and the set of block positions it
 * occupies.
 * <p>
 * The {@code Shop} class provides constructors for creating shop instances with
 * varying levels of detail, as well as getter and setter methods for accessing
 * and
 * modifying shop properties.
 * </p>
 * <ul>
 * <li>{@code entityUuid}: The UUID of the entity associated with the shop
 * (optional).</li>
 * <li>{@code shopUuid}: The unique identifier for the shop.</li>
 * <li>{@code origin}: The origin block position of the shop.</li>
 * <li>{@code type}: The type/category of the shop.</li>
 * <li>{@code name}: The name of the shop.</li>
 * <li>{@code description}: A description of the shop.</li>
 * <li>{@code owner}: The owner of the shop.</li>
 * <li>{@code size}: The size of the shop (typically as a square, size x
 * size).</li>
 * <li>{@code blocks}: The set of block positions that make up the shop.</li>
 * </ul>
 * 
 * <p>
 * Example usage:
 * 
 * <pre>
 * Shop shop = new Shop(shopUuid, origin, "general", "My Shop", "A nice shop", "player123", 5);
 * </pre>
 * </p>
 */
public class Shop {
    private UUID entityUuid;
    private UUID shopUuid;
    private BlockPos origin;
    private String type;
    private String owner;
    private Set<BlockPos> blocks = new HashSet<>();
    
    private ShopTypeDefinition typeDefinition;

    /**
     * Constructs a new Shop instance with the specified parameters.
     *
     * @param shopUuid the unique identifier for the shop
     * @param origin   the origin block position of the shop
     * @param type     the type of the shop
     * @param owner    the owner of the shop
     */
    public Shop(UUID shopUuid, BlockPos origin, String type, String owner) {
        this.shopUuid = shopUuid;
        this.origin = origin;
        this.type = type;
        this.typeDefinition = ShopTypeManager.getShopType(type);
        this.owner = owner;
        this.blocks.add(origin);
    }

    /**
     * Constructs a new Shop instance with the specified parameters.
     *
     * @param entityUuid the UUID of the entity associated with the shop
     * @param shopUuid   the unique identifier for the shop
     * @param origin     the origin block position of the shop
     * @param type       the type of the shop
     * @param owner      the owner of the shop
     * @param blocks     a list of block positions that make up the shop
     */
    public Shop(UUID entityUuid, UUID shopUuid, BlockPos origin, String type, String owner, List<BlockPos> blocks) {
        this(shopUuid, origin, type, owner);
        this.entityUuid = entityUuid;
        this.blocks.addAll(blocks);
    }

    /**
     * Returns the UUID of the associated entity.
     *
     * @return the UUID of the entity
     */
    public UUID getEntityUuid() {
        return entityUuid;
    }

    /**
     * Sets the UUID of the associated entity for this shop.
     *
     * @param entityUuid the UUID to associate with this shop's entity
     */
    public void setEntityUuid(UUID entityUuid) {
        this.entityUuid = entityUuid;
    }

    /**
     * Returns the position of the first block associated with this shop.
     *
     * @return the {@link BlockPos} representing the first block's position
     */
    public BlockPos getOrigin() {
        return origin;
    }

    /**
     * Sets the position of the first block associated with the shop.
     *
     * @param firstBlock the {@link BlockPos} representing the new position of the
     *                   first block
     */
    public void setOrigin(BlockPos firstBlock) {
        this.origin = firstBlock;
    }

    /**
     * Returns the type of the shop.
     *
     * @return the shop type as a String
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the shop.
     *
     * @param shopType the type of the shop to set
     */
    public void setType(String shopType) {
        this.type = shopType;
    }

    /**
     * Returns the unique identifier of the shop.
     *
     * @return the shop's unique ID as a String
     */
    public UUID getShopUuid() {
        return shopUuid;
    }

    /**
     * Sets the unique identifier for this shop.
     *
     * @param shopId the unique identifier to assign to the shop
     */
    public void setShopUuid(UUID shopUuid) {
        this.shopUuid = shopUuid;
    }

    /**
     * Returns the owner of the shop.
     *
     * @return the owner's identifier as a String
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the owner of the shop.
     *
     * @param owner the name of the new owner to assign to this shop
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Returns the size of the shop (size x size).
     *
     * @return the size of the shop as an integer
     */
    public int getSize() {
        return typeDefinition != null ? typeDefinition.getSize() : ShopTypeDefinition.DEFAULT_SHOP_SIZE;
    }

    /**
     * Returns a list of all block positions associated with this shop.
     *
     * @return a {@link List} of {@link BlockPos} representing all blocks linked to
     *         the shop
     */
    public Set<BlockPos> getBlocks() {
        return blocks;
    }

    /**
     * Returns the name of the shop.
     *
     * @return the shop's name
     */
    public String getName() {
        return typeDefinition != null ? typeDefinition.getName() : "";
    }

    /**
     * Returns the description of the shop.
     *
     * @return the shop's description
     */
    public String getDescription() {
        return typeDefinition != null ? typeDefinition.getDescription() : "";    
    }
}