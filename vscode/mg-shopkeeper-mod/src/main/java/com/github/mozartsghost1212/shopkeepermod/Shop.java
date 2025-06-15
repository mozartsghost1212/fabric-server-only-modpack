package com.github.mozartsghost1212.shopkeepermod;

import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a shop in the game world.
 * <p>
 * Each shop is associated with a unique entity, a primary block position, a
 * type,
 * an identifier, an owner, and a size. The shop also maintains a list of all
 * block
 * positions that are part of the shop.
 * </p>
 */
public class Shop {
    private UUID entityUuid;
    private UUID uuid;
    private BlockPos origin;
    private String type;
    private String owner;
    private int size;
    private Set<BlockPos> blocks = new HashSet<>();

    /**
     * @param uuid   the unique identifier for the shop
     * @param origin the position of the origin block of the shop
     * @param type   the type of the shop
     * @param owner  the owner of the shop
     * @param size   the size of the shop
     */
    public Shop(UUID uuid, BlockPos origin, String type, String owner, int size) {
        this.origin = origin;
        this.type = type;
        this.owner = owner;
        this.size = size;
        this.blocks.add(origin);
    }

    /**
     * Constructs a new Shop instance with the specified parameters.
     *
     * @param entityUuid  the UUID of the associated entity (e.g., shopkeeper NPC)
     * @param shopUuid    the unique identifier for this shop
     * @param originBlock the origin block position of the shop
     * @param shopType    the type or category of the shop
     * @param shopOwner   the owner of the shop
     * @param shopSize    the size of the shop (e.g., inventory slots)
     * @param shopBlocks  a list of all block positions associated with the shop
     */
    public Shop(UUID entityUuid, UUID shopUuid, BlockPos originBlock, String shopType, String shopOwner, int shopSize,
            List<BlockPos> shopBlocks) {
        this(shopUuid, originBlock, shopType, shopOwner, shopSize);
        this.entityUuid = entityUuid;
        this.uuid = shopUuid;
        this.blocks.addAll(shopBlocks);
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
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Sets the unique identifier for this shop.
     *
     * @param shopId the unique identifier to assign to the shop
     */
    public void setUuid(UUID shopUuid) {
        this.uuid = shopUuid;
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
        return size;
    }

    /**
     * Sets the size of the shop (size x size).
     *
     * @param size the new size to set
     */
    public void setSize(int size) {
        this.size = size;
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
}