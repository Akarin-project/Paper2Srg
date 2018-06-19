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

    private final World field_75537_a;
    private boolean field_75535_b;
    private int field_75536_c = -1;
    private int field_75533_d;
    private int field_75534_e;
    private Village field_75531_f;
    private int field_75532_g;
    private int field_75538_h;
    private int field_75539_i;

    public VillageSiege(World world) {
        this.field_75537_a = world;
    }

    public void func_75528_a() {
        if (this.field_75537_a.func_72935_r()) {
            this.field_75536_c = 0;
        } else if (this.field_75536_c != 2) {
            if (this.field_75536_c == 0) {
                float f = this.field_75537_a.func_72826_c(0.0F);

                if ((double) f < 0.5D || (double) f > 0.501D) {
                    return;
                }

                this.field_75536_c = this.field_75537_a.field_73012_v.nextInt(10) == 0 ? 1 : 2;
                this.field_75535_b = false;
                if (this.field_75536_c == 2) {
                    return;
                }
            }

            if (this.field_75536_c != -1) {
                if (!this.field_75535_b) {
                    if (!this.func_75529_b()) {
                        return;
                    }

                    this.field_75535_b = true;
                }

                if (this.field_75534_e > 0) {
                    --this.field_75534_e;
                } else {
                    this.field_75534_e = 2;
                    if (this.field_75533_d > 0) {
                        this.func_75530_c();
                        --this.field_75533_d;
                    } else {
                        this.field_75536_c = 2;
                    }

                }
            }
        }
    }

    private boolean func_75529_b() {
        List list = this.field_75537_a.field_73010_i;
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityhuman = (EntityPlayer) iterator.next();

            if (!entityhuman.func_175149_v()) {
                this.field_75531_f = this.field_75537_a.func_175714_ae().func_176056_a(new BlockPos(entityhuman), 1);
                if (this.field_75531_f != null && this.field_75531_f.func_75567_c() >= 10 && this.field_75531_f.func_75561_d() >= 20 && this.field_75531_f.func_75562_e() >= 20) {
                    BlockPos blockposition = this.field_75531_f.func_180608_a();
                    float f = (float) this.field_75531_f.func_75568_b();
                    boolean flag = false;
                    int i = 0;

                    while (true) {
                        if (i < 10) {
                            float f1 = this.field_75537_a.field_73012_v.nextFloat() * 6.2831855F;

                            this.field_75532_g = blockposition.func_177958_n() + (int) ((double) (MathHelper.func_76134_b(f1) * f) * 0.9D);
                            this.field_75538_h = blockposition.func_177956_o();
                            this.field_75539_i = blockposition.func_177952_p() + (int) ((double) (MathHelper.func_76126_a(f1) * f) * 0.9D);
                            flag = false;
                            Iterator iterator1 = this.field_75537_a.func_175714_ae().func_75540_b().iterator();

                            while (iterator1.hasNext()) {
                                Village village = (Village) iterator1.next();

                                if (village != this.field_75531_f && village.func_179866_a(new BlockPos(this.field_75532_g, this.field_75538_h, this.field_75539_i))) {
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

                        Vec3d vec3d = this.func_179867_a(new BlockPos(this.field_75532_g, this.field_75538_h, this.field_75539_i));

                        if (vec3d != null) {
                            this.field_75534_e = 0;
                            this.field_75533_d = 20;
                            return true;
                        }
                        break;
                    }
                }
            }
        }

        return false;
    }

    private boolean func_75530_c() {
        Vec3d vec3d = this.func_179867_a(new BlockPos(this.field_75532_g, this.field_75538_h, this.field_75539_i));

        if (vec3d == null) {
            return false;
        } else {
            EntityZombie entityzombie;

            try {
                entityzombie = new EntityZombie(this.field_75537_a);
                entityzombie.func_180482_a(this.field_75537_a.func_175649_E(new BlockPos(entityzombie)), (IEntityLivingData) null);
            } catch (Exception exception) {
                exception.printStackTrace();
                ServerInternalException.reportInternalException(exception); // Paper
                return false;
            }

            entityzombie.func_70012_b(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c, this.field_75537_a.field_73012_v.nextFloat() * 360.0F, 0.0F);
            this.field_75537_a.addEntity(entityzombie, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.VILLAGE_INVASION); // CraftBukkit
            BlockPos blockposition = this.field_75531_f.func_180608_a();

            entityzombie.func_175449_a(blockposition, this.field_75531_f.func_75568_b());
            return true;
        }
    }

    @Nullable
    private Vec3d func_179867_a(BlockPos blockposition) {
        for (int i = 0; i < 10; ++i) {
            BlockPos blockposition1 = blockposition.func_177982_a(this.field_75537_a.field_73012_v.nextInt(16) - 8, this.field_75537_a.field_73012_v.nextInt(6) - 3, this.field_75537_a.field_73012_v.nextInt(16) - 8);

            if (this.field_75531_f.func_179866_a(blockposition1) && WorldEntitySpawner.func_180267_a(EntityLiving.SpawnPlacementType.ON_GROUND, this.field_75537_a, blockposition1)) {
                return new Vec3d((double) blockposition1.func_177958_n(), (double) blockposition1.func_177956_o(), (double) blockposition1.func_177952_p());
            }
        }

        return null;
    }
}
