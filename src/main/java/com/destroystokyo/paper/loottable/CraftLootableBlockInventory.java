package com.destroystokyo.paper.loottable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.world.World;
import org.bukkit.Chunk;
import org.bukkit.block.Block;

public interface CraftLootableBlockInventory extends LootableBlockInventory, CraftLootableInventory {

    TileEntityLockableLoot getTileEntity();

    @Override
    default LootableInventory getAPILootableInventory() {
        return this;
    }

    @Override
    default World getNMSWorld() {
        return getTileEntity().func_145831_w();
    }

    default Block getBlock() {
        final BlockPos position = getTileEntity().func_174877_v();
        final Chunk bukkitChunk = getTileEntity().func_145831_w().func_175726_f(position).bukkitChunk;
        return bukkitChunk.getBlock(position.func_177958_n(), position.func_177956_o(), position.func_177952_p());
    }

    @Override
    default CraftLootableInventoryData getLootableData() {
        return getTileEntity().getLootableData();
    }
}
