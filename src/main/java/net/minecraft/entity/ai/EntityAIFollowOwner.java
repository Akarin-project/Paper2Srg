package net.minecraft.entity.ai;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.event.entity.EntityTeleportEvent;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

// CraftBukkit end

public class EntityAIFollowOwner extends EntityAIBase {

    private final EntityTameable field_75338_d;
    private EntityLivingBase field_75339_e;
    World field_75342_a;
    private final double field_75336_f;
    private final PathNavigate field_75337_g;
    private int field_75343_h;
    float field_75340_b;
    float field_75341_c;
    private float field_75344_i;

    public EntityAIFollowOwner(EntityTameable entitytameableanimal, double d0, float f, float f1) {
        this.field_75338_d = entitytameableanimal;
        this.field_75342_a = entitytameableanimal.field_70170_p;
        this.field_75336_f = d0;
        this.field_75337_g = entitytameableanimal.func_70661_as();
        this.field_75341_c = f;
        this.field_75340_b = f1;
        this.func_75248_a(3);
        if (!(entitytameableanimal.func_70661_as() instanceof PathNavigateGround) && !(entitytameableanimal.func_70661_as() instanceof PathNavigateFlying)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    public boolean func_75250_a() {
        EntityLivingBase entityliving = this.field_75338_d.func_70902_q();

        if (entityliving == null) {
            return false;
        } else if (entityliving instanceof EntityPlayer && ((EntityPlayer) entityliving).func_175149_v()) {
            return false;
        } else if (this.field_75338_d.func_70906_o()) {
            return false;
        } else if (this.field_75338_d.func_70068_e(entityliving) < (double) (this.field_75341_c * this.field_75341_c)) {
            return false;
        } else {
            this.field_75339_e = entityliving;
            return true;
        }
    }

    public boolean func_75253_b() {
        return !this.field_75337_g.func_75500_f() && this.field_75338_d.func_70068_e(this.field_75339_e) > (double) (this.field_75340_b * this.field_75340_b) && !this.field_75338_d.func_70906_o();
    }

    public void func_75249_e() {
        this.field_75343_h = 0;
        this.field_75344_i = this.field_75338_d.func_184643_a(PathNodeType.WATER);
        this.field_75338_d.func_184644_a(PathNodeType.WATER, 0.0F);
    }

    public void func_75251_c() {
        this.field_75339_e = null;
        this.field_75337_g.func_75499_g();
        this.field_75338_d.func_184644_a(PathNodeType.WATER, this.field_75344_i);
    }

    public void func_75246_d() {
        this.field_75338_d.func_70671_ap().func_75651_a(this.field_75339_e, 10.0F, (float) this.field_75338_d.func_70646_bf());
        if (!this.field_75338_d.func_70906_o()) {
            if (--this.field_75343_h <= 0) {
                this.field_75343_h = 10;
                if (!this.field_75337_g.func_75497_a((Entity) this.field_75339_e, this.field_75336_f)) {
                    if (!this.field_75338_d.func_110167_bD() && !this.field_75338_d.func_184218_aH()) {
                        if (this.field_75338_d.func_70068_e(this.field_75339_e) >= 144.0D) {
                            int i = MathHelper.func_76128_c(this.field_75339_e.field_70165_t) - 2;
                            int j = MathHelper.func_76128_c(this.field_75339_e.field_70161_v) - 2;
                            int k = MathHelper.func_76128_c(this.field_75339_e.func_174813_aQ().field_72338_b);

                            for (int l = 0; l <= 4; ++l) {
                                for (int i1 = 0; i1 <= 4; ++i1) {
                                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.func_192381_a(i, j, k, l, i1)) {
                                        // CraftBukkit start
                                        CraftEntity entity = this.field_75338_d.getBukkitEntity();
                                        Location to = new Location(entity.getWorld(), (double) ((float) (i + l) + 0.5F), (double) k, (double) ((float) (j + i1) + 0.5F), this.field_75338_d.field_70177_z, this.field_75338_d.field_70125_A);
                                        EntityTeleportEvent event = new EntityTeleportEvent(entity, entity.getLocation(), to);
                                        this.field_75338_d.field_70170_p.getServer().getPluginManager().callEvent(event);
                                        if (event.isCancelled()) {
                                            return;
                                        }
                                        to = event.getTo();

                                        this.field_75338_d.func_70012_b(to.getX(), to.getY(), to.getZ(), to.getYaw(), to.getPitch());
                                        // CraftBukkit end
                                        this.field_75337_g.func_75499_g();
                                        return;
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    protected boolean func_192381_a(int i, int j, int k, int l, int i1) {
        BlockPos blockposition = new BlockPos(i + l, k - 1, j + i1);
        IBlockState iblockdata = this.field_75342_a.func_180495_p(blockposition);

        return iblockdata.func_193401_d(this.field_75342_a, blockposition, EnumFacing.DOWN) == BlockFaceShape.SOLID && iblockdata.func_189884_a((Entity) this.field_75338_d) && this.field_75342_a.func_175623_d(blockposition.func_177984_a()) && this.field_75342_a.func_175623_d(blockposition.func_177981_b(2));
    }
}
