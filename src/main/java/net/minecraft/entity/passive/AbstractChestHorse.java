package net.minecraft.entity.passive;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.world.World;


public abstract class AbstractChestHorse extends AbstractHorse {

    private static final DataParameter<Boolean> field_190698_bG = EntityDataManager.func_187226_a(AbstractChestHorse.class, DataSerializers.field_187198_h);

    public AbstractChestHorse(World world) {
        super(world);
        this.field_190688_bE = false;
    }

    protected void func_70088_a() {
        super.func_70088_a();
        this.field_70180_af.func_187214_a(AbstractChestHorse.field_190698_bG, Boolean.valueOf(false));
    }

    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a((double) this.func_110267_cL());
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.17499999701976776D);
        this.func_110148_a(AbstractChestHorse.field_110271_bv).func_111128_a(0.5D);
    }

    public boolean func_190695_dh() {
        return ((Boolean) this.field_70180_af.func_187225_a(AbstractChestHorse.field_190698_bG)).booleanValue();
    }

    public void func_110207_m(boolean flag) {
        this.field_70180_af.func_187227_b(AbstractChestHorse.field_190698_bG, Boolean.valueOf(flag));
    }

    protected int func_190686_di() {
        return this.func_190695_dh() ? 17 : super.func_190686_di();
    }

    public double func_70042_X() {
        return super.func_70042_X() - 0.25D;
    }

    protected SoundEvent func_184785_dv() {
        super.func_184785_dv();
        return SoundEvents.field_187582_aw;
    }

    public void func_70645_a(DamageSource damagesource) {
        // super.die(damagesource); // CraftBukkit - moved down
        if (this.func_190695_dh()) {
            if (!this.field_70170_p.field_72995_K) {
                this.func_145779_a(Item.func_150898_a(Blocks.field_150486_ae), 1);
            }

            // this.setCarryingChest(false); // CraftBukkit - moved down
        }
        // CraftBukkit start
        super.func_70645_a(damagesource);
        this.func_110207_m(false);
        // CraftBukkit end

    }

    public static void func_190694_b(DataFixer dataconvertermanager, Class<?> oclass) {
        AbstractHorse.func_190683_c(dataconvertermanager, oclass);
        dataconvertermanager.func_188258_a(FixTypes.ENTITY, (IDataWalker) (new ItemStackDataLists(oclass, new String[] { "Items"})));
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        nbttagcompound.func_74757_a("ChestedHorse", this.func_190695_dh());
        if (this.func_190695_dh()) {
            NBTTagList nbttaglist = new NBTTagList();

            for (int i = 2; i < this.field_110296_bG.func_70302_i_(); ++i) {
                ItemStack itemstack = this.field_110296_bG.func_70301_a(i);

                if (!itemstack.func_190926_b()) {
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                    nbttagcompound1.func_74774_a("Slot", (byte) i);
                    itemstack.func_77955_b(nbttagcompound1);
                    nbttaglist.func_74742_a(nbttagcompound1);
                }
            }

            nbttagcompound.func_74782_a("Items", nbttaglist);
        }

    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        this.func_110207_m(nbttagcompound.func_74767_n("ChestedHorse"));
        if (this.func_190695_dh()) {
            NBTTagList nbttaglist = nbttagcompound.func_150295_c("Items", 10);

            this.func_110226_cD();

            for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
                NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
                int j = nbttagcompound1.func_74771_c("Slot") & 255;

                if (j >= 2 && j < this.field_110296_bG.func_70302_i_()) {
                    this.field_110296_bG.func_70299_a(j, new ItemStack(nbttagcompound1));
                }
            }
        }

        this.func_110232_cE();
    }

    public boolean func_174820_d(int i, ItemStack itemstack) {
        if (i == 499) {
            if (this.func_190695_dh() && itemstack.func_190926_b()) {
                this.func_110207_m(false);
                this.func_110226_cD();
                return true;
            }

            if (!this.func_190695_dh() && itemstack.func_77973_b() == Item.func_150898_a(Blocks.field_150486_ae)) {
                this.func_110207_m(true);
                this.func_110226_cD();
                return true;
            }
        }

        return super.func_174820_d(i, itemstack);
    }

    public boolean func_184645_a(EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (itemstack.func_77973_b() == Items.field_151063_bx) {
            return super.func_184645_a(entityhuman, enumhand);
        } else {
            if (!this.func_70631_g_()) {
                if (this.func_110248_bS() && entityhuman.func_70093_af()) {
                    this.func_110199_f(entityhuman);
                    return true;
                }

                if (this.func_184207_aI()) {
                    return super.func_184645_a(entityhuman, enumhand);
                }
            }

            if (!itemstack.func_190926_b()) {
                boolean flag = this.func_190678_b(entityhuman, itemstack);

                if (!flag && !this.func_110248_bS()) {
                    if (itemstack.func_111282_a(entityhuman, (EntityLivingBase) this, enumhand)) {
                        return true;
                    }

                    this.func_190687_dF();
                    return true;
                }

                if (!flag && !this.func_190695_dh() && itemstack.func_77973_b() == Item.func_150898_a(Blocks.field_150486_ae)) {
                    this.func_110207_m(true);
                    this.func_190697_dk();
                    flag = true;
                    this.func_110226_cD();
                }

                if (!flag && !this.func_70631_g_() && !this.func_110257_ck() && itemstack.func_77973_b() == Items.field_151141_av) {
                    this.func_110199_f(entityhuman);
                    return true;
                }

                if (flag) {
                    if (!entityhuman.field_71075_bZ.field_75098_d) {
                        itemstack.func_190918_g(1);
                    }

                    return true;
                }
            }

            if (this.func_70631_g_()) {
                return super.func_184645_a(entityhuman, enumhand);
            } else if (itemstack.func_111282_a(entityhuman, (EntityLivingBase) this, enumhand)) {
                return true;
            } else {
                this.func_110237_h(entityhuman);
                return true;
            }
        }
    }

    protected void func_190697_dk() {
        this.func_184185_a(SoundEvents.field_187584_ax, 1.0F, (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F + 1.0F);
    }

    public int func_190696_dl() {
        return 5;
    }
}
