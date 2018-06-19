package net.minecraft.entity.item;

import com.google.common.base.Optional;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.end.DragonFightManager;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.ExplosionPrimeEvent;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.ExplosionPrimeEvent;
// CraftBukkit end

public class EntityEnderCrystal extends Entity {

    private static final DataParameter<Optional<BlockPos>> field_184521_b = EntityDataManager.func_187226_a(EntityEnderCrystal.class, DataSerializers.field_187201_k);
    private static final DataParameter<Boolean> field_184522_c = EntityDataManager.func_187226_a(EntityEnderCrystal.class, DataSerializers.field_187198_h);
    public int field_70261_a;

    public EntityEnderCrystal(World world) {
        super(world);
        this.field_70156_m = true;
        this.func_70105_a(2.0F, 2.0F);
        this.field_70261_a = this.field_70146_Z.nextInt(100000);
    }

    public EntityEnderCrystal(World world, double d0, double d1, double d2) {
        this(world);
        this.func_70107_b(d0, d1, d2);
    }

    protected boolean func_70041_e_() {
        return false;
    }

    protected void func_70088_a() {
        this.func_184212_Q().func_187214_a(EntityEnderCrystal.field_184521_b, Optional.absent());
        this.func_184212_Q().func_187214_a(EntityEnderCrystal.field_184522_c, Boolean.valueOf(true));
    }

    public void func_70071_h_() {
        this.field_70169_q = this.field_70165_t;
        this.field_70167_r = this.field_70163_u;
        this.field_70166_s = this.field_70161_v;
        ++this.field_70261_a;
        if (!this.field_70170_p.field_72995_K) {
            BlockPos blockposition = new BlockPos(this);

            if (this.field_70170_p.field_73011_w instanceof WorldProviderEnd && this.field_70170_p.func_180495_p(blockposition).func_177230_c() != Blocks.field_150480_ab) {
                // CraftBukkit start
                if (!CraftEventFactory.callBlockIgniteEvent(this.field_70170_p, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), this).isCancelled()) {
                    this.field_70170_p.func_175656_a(blockposition, Blocks.field_150480_ab.func_176223_P());
                }
                // CraftBukkit end
            }
        }

    }

    protected void func_70014_b(NBTTagCompound nbttagcompound) {
        if (this.func_184518_j() != null) {
            nbttagcompound.func_74782_a("BeamTarget", NBTUtil.func_186859_a(this.func_184518_j()));
        }

        nbttagcompound.func_74757_a("ShowBottom", this.func_184520_k());
    }

    protected void func_70037_a(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.func_150297_b("BeamTarget", 10)) {
            this.func_184516_a(NBTUtil.func_186861_c(nbttagcompound.func_74775_l("BeamTarget")));
        }

        if (nbttagcompound.func_150297_b("ShowBottom", 1)) {
            this.func_184517_a(nbttagcompound.func_74767_n("ShowBottom"));
        }

    }

    public boolean func_70067_L() {
        return true;
    }

    public boolean func_70097_a(DamageSource damagesource, float f) {
        if (this.func_180431_b(damagesource)) {
            return false;
        } else if (damagesource.func_76346_g() instanceof EntityDragon) {
            return false;
        } else {
            if (!this.field_70128_L && !this.field_70170_p.field_72995_K) {
                // CraftBukkit start - All non-living entities need this
                if (CraftEventFactory.handleNonLivingEntityDamageEvent(this, damagesource, f)) {
                    return false;
                }
                // CraftBukkit end
                this.func_70106_y();
                if (!this.field_70170_p.field_72995_K) {
                    if (!damagesource.func_94541_c()) {
                        // CraftBukkit start
                        ExplosionPrimeEvent event = new ExplosionPrimeEvent(this.getBukkitEntity(), 6.0F, true);
                        this.field_70170_p.getServer().getPluginManager().callEvent(event);
                        if (event.isCancelled()) {
                            this.field_70128_L = false;
                            return false;
                        }
                        this.field_70170_p.func_72876_a(this, this.field_70165_t, this.field_70163_u, this.field_70161_v, event.getRadius(), event.getFire());
                        // CraftBukkit end
                    }

                    this.func_184519_a(damagesource);
                }
            }

            return true;
        }
    }

    public void func_174812_G() {
        this.func_184519_a(DamageSource.field_76377_j);
        super.func_174812_G();
    }

    private void func_184519_a(DamageSource damagesource) {
        if (this.field_70170_p.field_73011_w instanceof WorldProviderEnd) {
            WorldProviderEnd worldprovidertheend = (WorldProviderEnd) this.field_70170_p.field_73011_w;
            DragonFightManager enderdragonbattle = worldprovidertheend.func_186063_s();

            if (enderdragonbattle != null) {
                enderdragonbattle.func_186090_a(this, damagesource);
            }
        }

    }

    public void func_184516_a(@Nullable BlockPos blockposition) {
        this.func_184212_Q().func_187227_b(EntityEnderCrystal.field_184521_b, Optional.fromNullable(blockposition));
    }

    @Nullable
    public BlockPos func_184518_j() {
        return (BlockPos) ((Optional) this.func_184212_Q().func_187225_a(EntityEnderCrystal.field_184521_b)).orNull();
    }

    public void func_184517_a(boolean flag) {
        this.func_184212_Q().func_187227_b(EntityEnderCrystal.field_184522_c, Boolean.valueOf(flag));
    }

    public boolean func_184520_k() {
        return ((Boolean) this.func_184212_Q().func_187225_a(EntityEnderCrystal.field_184522_c)).booleanValue();
    }
}
