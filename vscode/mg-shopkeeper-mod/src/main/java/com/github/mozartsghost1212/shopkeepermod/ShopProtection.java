package com.github.mozartsghost1212.shopkeepermod;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class ShopProtection {
    public static void register() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            if (isProtected(pos)) {
                if (player.hasPermissionLevel(2)) {
                    // Allow admins to break blocks in protected shops
                    return true;
                } else {
                    // Regular players cannot break blocks in protected shops   
                    player.sendMessage(Text.of("You cannot break blocks inside a protected shop!"), true);
                    return false;
                }
            }
            return true;
        });

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            BlockPos pos = hitResult.getBlockPos();
            if (isProtected(pos)) {
                if (player.hasPermissionLevel(2)) {
                    // Allow admins to place blocks in protected shops
                    return ActionResult.PASS;
                } else {
                    // Regular players cannot place blocks in protected shops
                    player.sendMessage(Text.of("You cannot place blocks inside a protected shop!"), true);
                    return ActionResult.FAIL;
                }
            }
            return ActionResult.PASS;
        });
    }

    private static boolean isProtected(BlockPos pos) {
        if (ShopkeeperManager.getAllShopBlocks().contains(pos)) {
            return true;
        }
        return false;
    }
}