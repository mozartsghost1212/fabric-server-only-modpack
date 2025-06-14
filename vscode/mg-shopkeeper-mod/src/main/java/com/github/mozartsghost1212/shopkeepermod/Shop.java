package com.github.mozartsghost1212.shopkeepermod;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
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
    private BlockPos originBlock;
    private String shopType;
    private UUID shopUuid;
    private String owner;
    private int size;

    private final List<BlockPos> allBlocks = new ArrayList<>();

    public Shop(UUID shopUuid, BlockPos originPos, String type, String owner, int size) {
        this.originBlock = originPos;
        this.shopType = type;
        this.owner = owner;
        this.size = size;
        this.allBlocks.add(originPos);
    }

    public Shop(UUID entityUuid, UUID shopUuid, BlockPos originBlock, List<BlockPos> allBlocks, String shopType, String owner, int size) {
        this(shopUuid, originBlock, shopType, owner, size);
        this.entityUuid = entityUuid;
        this.shopUuid = shopUuid;
        this.allBlocks.addAll(allBlocks);
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
    public BlockPos getOriginBlock() {
        return originBlock;
    }

    /**
     * Sets the position of the first block associated with the shop.
     *
     * @param firstBlock the {@link BlockPos} representing the new position of the first block
     */
    public void setOriginBlock(BlockPos firstBlock) {
        this.originBlock = firstBlock;
    }

    /**
     * Returns the type of the shop.
     *
     * @return the shop type as a String
     */
    public String getShopType() {
        return shopType;
    }

    /**
     * Sets the type of the shop.
     *
     * @param shopType the type of the shop to set
     */
    public void setShopType(String shopType) {
        this.shopType = shopType;
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
    public List<BlockPos> getAllBlocks() {
        return allBlocks;
    }
}