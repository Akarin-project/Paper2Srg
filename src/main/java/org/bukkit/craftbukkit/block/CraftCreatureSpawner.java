package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import net.minecraft.util.ResourceLocation;
import net.minecraft.tileentity.TileEntityMobSpawner;
import org.bukkit.Material;

import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

public class CraftCreatureSpawner extends CraftBlockEntityState<TileEntityMobSpawner> implements CreatureSpawner {

    public CraftCreatureSpawner(final Block block) {
        super(block, TileEntityMobSpawner.class);
    }

    public CraftCreatureSpawner(final Material material, TileEntityMobSpawner te) {
        super(material, te);
    }

    @Override
    public EntityType getSpawnedType() {
        ResourceLocation key = this.getSnapshot().func_145881_a().func_190895_g();
        return (key == null) ? EntityType.PIG : EntityType.fromName(key.func_110623_a());
    }

    @Override
    public void setSpawnedType(EntityType entityType) {
        if (entityType == null || entityType.getName() == null) {
            throw new IllegalArgumentException("Can't spawn EntityType " + entityType + " from mobspawners!");
        }

        this.getSnapshot().func_145881_a().func_190894_a(new ResourceLocation(entityType.getName()));
    }

    @Override
    public String getCreatureTypeName() {
        return this.getSnapshot().func_145881_a().func_190895_g().func_110623_a();
    }

    @Override
    public void setCreatureTypeByName(String creatureType) {
        // Verify input
        EntityType type = EntityType.fromName(creatureType);
        if (type == null) {
            return;
        }
        setSpawnedType(type);
    }

    @Override
    public int getDelay() {
        return this.getSnapshot().func_145881_a().field_98286_b;
    }

    @Override
    public void setDelay(int delay) {
        this.getSnapshot().func_145881_a().field_98286_b = delay;
    }

    @Override
    public int getMinSpawnDelay() {
        return this.getSnapshot().func_145881_a().field_98283_g;
    }

    @Override
    public void setMinSpawnDelay(int spawnDelay) {
        Preconditions.checkArgument(spawnDelay <= getMaxSpawnDelay(), "Minimum Spawn Delay must be less than or equal to Maximum Spawn Delay");
        this.getSnapshot().func_145881_a().field_98283_g = spawnDelay;
    }

    @Override
    public int getMaxSpawnDelay() {
        return this.getSnapshot().func_145881_a().field_98293_h;
    }

    @Override
    public void setMaxSpawnDelay(int spawnDelay) {
        Preconditions.checkArgument(spawnDelay > 0, "Maximum Spawn Delay must be greater than 0.");
        Preconditions.checkArgument(spawnDelay >= getMinSpawnDelay(), "Maximum Spawn Delay must be greater than or equal to Minimum Spawn Delay");
        this.getSnapshot().func_145881_a().field_98293_h = spawnDelay;
    }

    @Override
    public int getMaxNearbyEntities() {
        return this.getSnapshot().func_145881_a().field_98292_k;
    }

    @Override
    public void setMaxNearbyEntities(int maxNearbyEntities) {
        this.getSnapshot().func_145881_a().field_98292_k = maxNearbyEntities;
    }

    @Override
    public int getSpawnCount() {
        return this.getSnapshot().func_145881_a().field_98294_i;
    }

    @Override
    public void setSpawnCount(int count) {
        this.getSnapshot().func_145881_a().field_98294_i = count;
    }

    @Override
    public int getRequiredPlayerRange() {
        return this.getSnapshot().func_145881_a().field_98289_l;
    }

    @Override
    public void setRequiredPlayerRange(int requiredPlayerRange) {
        this.getSnapshot().func_145881_a().field_98289_l = requiredPlayerRange;
    }

    @Override
    public int getSpawnRange() {
        return this.getSnapshot().func_145881_a().field_98290_m;
    }

    @Override
    public void setSpawnRange(int spawnRange) {
        this.getSnapshot().func_145881_a().field_98290_m = spawnRange;
    }
}
