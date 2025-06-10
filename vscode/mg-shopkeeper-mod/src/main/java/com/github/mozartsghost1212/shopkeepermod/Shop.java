package com.github.mozartsghost1212.shopkeepermod;

import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class Shop {
    public UUID villagerUuid;
    public BlockPos pos;
    public String shopType;
    public String shopId;
    public String owner;
    public int size;

    public Shop(UUID uuid, BlockPos pos, String type, String shopId, String owner, int size) {
        this.villagerUuid = uuid;
        this.pos = pos;
        this.shopType = type;
        this.shopId = shopId;
        this.owner = owner;
        this.size = size;
    }
}