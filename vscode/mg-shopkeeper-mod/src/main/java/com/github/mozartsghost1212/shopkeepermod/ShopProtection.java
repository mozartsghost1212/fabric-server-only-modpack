package com.github.mozartsghost1212.shopkeepermod;



import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class ShopProtection {
    public static void registerProtection() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            if (isProtected(pos)) {
                player.sendMessage(net.minecraft.text.Text.of("You cannot break blocks inside a protected shop!"), true);
                return false;
            }
            return true;
        });

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            BlockPos pos = hitResult.getBlockPos();
            if (isProtected(pos)) {
                player.sendMessage(net.minecraft.text.Text.of("You cannot place or use blocks inside a protected shop!"), true);
                return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });
    }

    private static boolean isProtected(BlockPos pos) {
        for (Shop shop : ShopkeeperManager.getShops()) {
            BlockPos center = shop.pos;
            int size = shop.size;
            if (pos.getX() >= center.getX() - size && pos.getX() <= center.getX() + size &&
                pos.getY() >= center.getY() - 1 && pos.getY() <= center.getY() + 4 &&
                pos.getZ() >= center.getZ() - 2 && pos.getZ() <= center.getZ() + 2) {
                return true;
            }
        }
        return false;
    }
}