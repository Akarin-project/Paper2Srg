package net.minecraft.entity.item;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

// CraftBukkit end

public class EntityXPOrb extends Entity {

    public int field_70533_a;
    public int field_70531_b;
    public int field_70532_c;
    private int field_70529_d = 5;
    public int field_70530_e;
    private EntityPlayer field_80001_f;
    private int field_80002_g;
    // Paper start
    public java.util.UUID sourceEntityId;
    public java.util.UUID triggerEntityId;
    public org.bukkit.entity.ExperienceOrb.SpawnReason spawnReason = org.bukkit.entity.ExperienceOrb.SpawnReason.UNKNOWN;

    private void loadPaperNBT(NBTTagCompound nbttagcompound) {
        if (!nbttagcompound.func_150297_b("Paper.ExpData", 10)) { // 10 = compound
            return;
        }
        NBTTagCompound comp = nbttagcompound.func_74775_l("Paper.ExpData");
        if (comp.hasUUID("source")) {
            this.sourceEntityId = comp.getUUID("source");
        }
        if (comp.hasUUID("trigger")) {
            this.triggerEntityId = comp.getUUID("trigger");
        }
        if (comp.func_74764_b("reason")) {
            String reason = comp.func_74779_i("reason");
            try {
                spawnReason = org.bukkit.entity.ExperienceOrb.SpawnReason.valueOf(reason);
            } catch (Exception e) {
                this.field_70170_p.getServer().getLogger().warning("Invalid spawnReason set for experience orb: " + e.getMessage() + " - " + reason);
            }
        }
    }
    private void savePaperNBT(NBTTagCompound nbttagcompound) {
        NBTTagCompound comp = new NBTTagCompound();
        if (sourceEntityId != null) {
            comp.setUUID("source", sourceEntityId);
        }
        if (triggerEntityId != null) {
            comp.setUUID("trigger", triggerEntityId);
        }
        if (spawnReason != null && spawnReason != org.bukkit.entity.ExperienceOrb.SpawnReason.UNKNOWN) {
            comp.func_74778_a("reason", spawnReason.name());
        }
        nbttagcompound.func_74782_a("Paper.ExpData", comp);
    }
    public EntityXPOrb(World world, double d0, double d1, double d2, int i, org.bukkit.entity.ExperienceOrb.SpawnReason reason, Entity triggerId) {
        this(world, d0, d1, d2, i, reason, triggerId, null);
    }

    public EntityXPOrb(World world, double d0, double d1, double d2, int i, org.bukkit.entity.ExperienceOrb.SpawnReason reason, Entity triggerId, Entity sourceId) {
        super(world);
        this.sourceEntityId = sourceId != null ? sourceId.func_110124_au() : null;
        this.triggerEntityId = triggerId != null ? triggerId.func_110124_au() : null;
        this.spawnReason = reason != null ? reason : org.bukkit.entity.ExperienceOrb.SpawnReason.UNKNOWN;
        // Paper end
        this.func_70105_a(0.5F, 0.5F);
        this.func_70107_b(d0, d1, d2);
        this.field_70177_z = (float) (Math.random() * 360.0D);
        this.field_70159_w = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
        this.field_70181_x = (double) ((float) (Math.random() * 0.2D) * 2.0F);
        this.field_70179_y = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
        this.field_70530_e = i;
    }

    protected boolean func_70041_e_() {
        return false;
    }

    public EntityXPOrb(World world) {
        super(world);
        this.func_70105_a(0.25F, 0.25F);
    }

    protected void func_70088_a() {}

    public void func_70071_h_() {
        super.func_70071_h_();
        EntityPlayer prevTarget = this.field_80001_f;// CraftBukkit - store old target
        if (this.field_70532_c > 0) {
            --this.field_70532_c;
        }

        this.field_70169_q = this.field_70165_t;
        this.field_70167_r = this.field_70163_u;
        this.field_70166_s = this.field_70161_v;
        if (!this.func_189652_ae()) {
            this.field_70181_x -= 0.029999999329447746D;
        }

        if (this.field_70170_p.func_180495_p(new BlockPos(this)).func_185904_a() == Material.field_151587_i) {
            this.field_70181_x = 0.20000000298023224D;
            this.field_70159_w = (double) ((this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F);
            this.field_70179_y = (double) ((this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F);
            this.func_184185_a(SoundEvents.field_187658_bx, 0.4F, 2.0F + this.field_70146_Z.nextFloat() * 0.4F);
        }

        this.func_145771_j(this.field_70165_t, (this.func_174813_aQ().field_72338_b + this.func_174813_aQ().field_72337_e) / 2.0D, this.field_70161_v);
        double d0 = 8.0D;

        if (this.field_80002_g < this.field_70533_a - 20 + this.func_145782_y() % 100) {
            if (this.field_80001_f == null || this.field_80001_f.func_70068_e(this) > 64.0D) {
                this.field_80001_f = this.field_70170_p.func_72890_a(this, 8.0D);
            }

            this.field_80002_g = this.field_70533_a;
        }

        if (this.field_80001_f != null && this.field_80001_f.func_175149_v()) {
            this.field_80001_f = null;
        }

        if (this.field_80001_f != null) {
            // CraftBukkit start
            boolean cancelled = false;
            if (this.field_80001_f != prevTarget) {
                EntityTargetLivingEntityEvent event = CraftEventFactory.callEntityTargetLivingEvent(this, field_80001_f, EntityTargetEvent.TargetReason.CLOSEST_PLAYER);
                EntityLivingBase target = event.getTarget() == null ? null : ((org.bukkit.craftbukkit.entity.CraftLivingEntity) event.getTarget()).getHandle();
                field_80001_f = target instanceof EntityPlayer ? (EntityPlayer) target : null;
                cancelled = event.isCancelled();
            }

            if (!cancelled && field_80001_f != null) {
            double d1 = (this.field_80001_f.field_70165_t - this.field_70165_t) / 8.0D;
            double d2 = (this.field_80001_f.field_70163_u + (double) this.field_80001_f.func_70047_e() / 2.0D - this.field_70163_u) / 8.0D;
            double d3 = (this.field_80001_f.field_70161_v - this.field_70161_v) / 8.0D;
            double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
            double d5 = 1.0D - d4;

            if (d5 > 0.0D) {
                d5 *= d5;
                this.field_70159_w += d1 / d4 * d5 * 0.1D;
                this.field_70181_x += d2 / d4 * d5 * 0.1D;
                this.field_70179_y += d3 / d4 * d5 * 0.1D;
            }
            }
            // CraftBukkit end
        }

        this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
        float f = 0.98F;

        if (this.field_70122_E) {
            f = this.field_70170_p.func_180495_p(new BlockPos(MathHelper.func_76128_c(this.field_70165_t), MathHelper.func_76128_c(this.func_174813_aQ().field_72338_b) - 1, MathHelper.func_76128_c(this.field_70161_v))).func_177230_c().field_149765_K * 0.98F;
        }

        this.field_70159_w *= (double) f;
        this.field_70181_x *= 0.9800000190734863D;
        this.field_70179_y *= (double) f;
        if (this.field_70122_E) {
            this.field_70181_x *= -0.8999999761581421D;
        }

        ++this.field_70533_a;
        ++this.field_70531_b;
        if (this.field_70531_b >= 6000) {
            this.func_70106_y();
        }

    }

    public boolean func_70072_I() {
        return this.field_70170_p.func_72918_a(this.func_174813_aQ(), Material.field_151586_h, (Entity) this);
    }

    protected void burn(int i) {
        this.func_70097_a(DamageSource.field_76372_a, (float) i);
    }

    public boolean func_70097_a(DamageSource damagesource, float f) {
        if (this.func_180431_b(damagesource)) {
            return false;
        } else {
            this.func_70018_K();
            this.field_70529_d = (int) ((float) this.field_70529_d - f);
            if (this.field_70529_d <= 0) {
                this.func_70106_y();
            }

            return false;
        }
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        nbttagcompound.func_74777_a("Health", (short) this.field_70529_d);
        nbttagcompound.func_74777_a("Age", (short) this.field_70531_b);
        nbttagcompound.func_74777_a("Value", (short) this.field_70530_e);
        savePaperNBT(nbttagcompound); // Paper
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        this.field_70529_d = nbttagcompound.func_74765_d("Health");
        this.field_70531_b = nbttagcompound.func_74765_d("Age");
        this.field_70530_e = nbttagcompound.func_74765_d("Value");
        loadPaperNBT(nbttagcompound); // Paper
    }

    public void func_70100_b_(EntityPlayer entityhuman) {
        if (!this.field_70170_p.field_72995_K) {
            if (this.field_70532_c == 0 && entityhuman.field_71090_bL == 0 && new com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent(((EntityPlayerMP) entityhuman).getBukkitEntity(), (org.bukkit.entity.ExperienceOrb) this.getBukkitEntity()).callEvent()) { // Paper
                entityhuman.field_71090_bL = 2;
                entityhuman.func_71001_a(this, 1);
                ItemStack itemstack = EnchantmentHelper.func_92099_a(Enchantments.field_185296_A, (EntityLivingBase) entityhuman);

                if (!itemstack.func_190926_b() && itemstack.func_77951_h()) {
                    int i = Math.min(this.func_184514_c(this.field_70530_e), itemstack.func_77952_i());

                    // CraftBukkit start
                    org.bukkit.event.player.PlayerItemMendEvent event = CraftEventFactory.callPlayerItemMendEvent(entityhuman, this, itemstack, i);
                    i = event.getRepairAmount();
                    if (!event.isCancelled()) {
                        this.field_70530_e -= this.func_184515_b(i);
                        itemstack.func_77964_b(itemstack.func_77952_i() - i);
                    }
                    // CraftBukkit end
				}

                if (this.field_70530_e > 0) {
                    entityhuman.func_71023_q(CraftEventFactory.callPlayerExpChangeEvent(entityhuman, this).getAmount()); // CraftBukkit - this.value -> event.getAmount() // Paper - supply experience orb object
                }

                this.func_70106_y();
            }

        }
    }

    public int durToXp(int i) { return func_184515_b(i); } // Paper OBFHELPER
    private int func_184515_b(int i) {
        return i / 2;
    }

    public int xpToDur(int i) { return func_184514_c(i); } // Paper OBFHELPER
    private int func_184514_c(int i) {
        return i * 2;
    }

    public int func_70526_d() {
        return this.field_70530_e;
    }

    public static int func_70527_a(int i) {
        // CraftBukkit start
        if (i > 162670129) return i - 100000;
        if (i > 81335063) return 81335063;
        if (i > 40667527) return 40667527;
        if (i > 20333759) return 20333759;
        if (i > 10166857) return 10166857;
        if (i > 5083423) return 5083423;
        if (i > 2541701) return 2541701;
        if (i > 1270849) return 1270849;
        if (i > 635413) return 635413;
        if (i > 317701) return 317701;
        if (i > 158849) return 158849;
        if (i > 79423) return 79423;
        if (i > 39709) return 39709;
        if (i > 19853) return 19853;
        if (i > 9923) return 9923;
        if (i > 4957) return 4957;
        // CraftBukkit end
        return i >= 2477 ? 2477 : (i >= 1237 ? 1237 : (i >= 617 ? 617 : (i >= 307 ? 307 : (i >= 149 ? 149 : (i >= 73 ? 73 : (i >= 37 ? 37 : (i >= 17 ? 17 : (i >= 7 ? 7 : (i >= 3 ? 3 : 1)))))))));
    }

    public boolean func_70075_an() {
        return false;
    }
}
