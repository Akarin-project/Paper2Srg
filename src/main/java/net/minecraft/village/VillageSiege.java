package net.minecraft.village;

import com.destroystokyo.paper.exception.ServerInternalException;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;

public class VillageSiege {

    private final World world;
    private boolean hasSetupSiege;
    private int siegeState = -1;
    private int siegeCount;
    private int nextSpawnTime;
    private Village village;
    private int spawnX;
    private int spawnY;
    private int spawnZ;

    public VillageSiege(World world) {
        this.world = world;
    }

    public void tick() {
        if (this.world.isDaytime()) {
            this.siegeState = 0;
        } else if (this.siegeState != 2) {
            if (this.siegeState == 0) {
                float f = this.world.getCelestialAngle(0.0F);

                if ((double) f < 0.5D || (double) f > 0.501D) {
                    return;
                }

                this.siegeState = this.world.rand.nextInt(10) == 0 ? 1 : 2;
                this.hasSetupSiege = false;
                if (this.siegeState == 2) {
                    return;
                }
            }

            if (this.siegeState != -1) {
                if (!this.hasSetupSiege) {
                    if (!this.trySetupSiege()) {
                        return;
                    }

                    this.hasSetupSiege = true;
                }

                if (this.nextSpawnTime > 0) {
                    --this.nextSpawnTime;
                } else {
                    this.nextSpawnTime = 2;
                    if (this.siegeCount > 0) {
                        this.spawnZombie();
                        --this.siegeCount;
                    } else {
                        this.siegeState = 2;
                    }

                }
            }
        }
    }

    private boolean trySetupSiege() {
        List list = this.world.playerEntities;
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityhuman = (EntityPlayer) iterator.next();

            if (!entityhuman.isSpectator()) {
                this.village = this.world.getVillageCollection().getNearestVillage(new BlockPos(entityhuman), 1);
                if (this.village != null && this.village.getNumVillageDoors() >= 10 && this.village.getTicksSinceLastDoorAdding() >= 20 && this.village.getNumVillagers() >= 20) {
                    BlockPos blockposition = this.village.getCenter();
                    float f = (float) this.village.getVillageRadius();
                    boolean flag = false;
                    int i = 0;

                    while (true) {
                        if (i < 10) {
                            float f1 = this.world.rand.nextFloat() * 6.2831855F;

                            this.spawnX = blockposition.getX() + (int) ((double) (MathHelper.cos(f1) * f) * 0.9D);
                            this.spawnY = blockposition.getY();
                            this.spawnZ = blockposition.getZ() + (int) ((double) (MathHelper.sin(f1) * f) * 0.9D);
                            flag = false;
                            Iterator iterator1 = this.world.getVillageCollection().getVillageList().iterator();

                            while (iterator1.hasNext()) {
                                Village village = (Village) iterator1.next();

                                if (village != this.village && village.isBlockPosWithinSqVillageRadius(new BlockPos(this.spawnX, this.spawnY, this.spawnZ))) {
                                    flag = true;
                                    break;
                                }
                            }

                            if (flag) {
                                ++i;
                                continue;
                            }
                        }

                        if (flag) {
                            return false;
                        }

                        Vec3d vec3d = this.findRandomSpawnPos(new BlockPos(this.spawnX, this.spawnY, this.spawnZ));

                        if (vec3d != null) {
                            this.nextSpawnTime = 0;
                            this.siegeCount = 20;
                            return true;
                        }
                        break;
                    }
                }
            }
        }

        return false;
    }

    private boolean spawnZombie() {
        Vec3d vec3d = this.findRandomSpawnPos(new BlockPos(this.spawnX, this.spawnY, this.spawnZ));

        if (vec3d == null) {
            return false;
        } else {
            EntityZombie entityzombie;

            try {
                entityzombie = new EntityZombie(this.world);
                entityzombie.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entityzombie)), (IEntityLivingData) null);
            } catch (Exception exception) {
                exception.printStackTrace();
                ServerInternalException.reportInternalException(exception); // Paper
                return false;
            }

            entityzombie.setLocationAndAngles(vec3d.x, vec3d.y, vec3d.z, this.world.rand.nextFloat() * 360.0F, 0.0F);
            this.world.addEntity(entityzombie, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.VILLAGE_INVASION); // CraftBukkit
            BlockPos blockposition = this.village.getCenter();

            entityzombie.setHomePosAndDistance(blockposition, this.village.getVillageRadius());
            return true;
        }
    }

    @Nullable
    private Vec3d findRandomSpawnPos(BlockPos blockposition) {
        for (int i = 0; i < 10; ++i) {
            BlockPos blockposition1 = blockposition.add(this.world.rand.nextInt(16) - 8, this.world.rand.nextInt(6) - 3, this.world.rand.nextInt(16) - 8);

            if (this.village.isBlockPosWithinSqVillageRadius(blockposition1) && WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, this.world, blockposition1)) {
                return new Vec3d((double) blockposition1.getX(), (double) blockposition1.getY(), (double) blockposition1.getZ());
            }
        }

        return null;
    }
}
