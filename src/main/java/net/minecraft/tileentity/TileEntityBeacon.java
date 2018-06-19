package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

import com.destroystokyo.paper.event.block.BeaconEffectEvent;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.entity.HumanEntity;
import org.bukkit.potion.PotionEffect;
// CraftBukkit end

// Paper start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import com.destroystokyo.paper.event.block.BeaconEffectEvent;
// Paper end

public class TileEntityBeacon extends TileEntityLockable implements ITickable, ISidedInventory {

    public static final Potion[][] field_146009_a = new Potion[][] { { MobEffects.field_76424_c, MobEffects.field_76422_e}, { MobEffects.field_76429_m, MobEffects.field_76430_j}, { MobEffects.field_76420_g}, { MobEffects.field_76428_l}};
    private static final Set<Potion> field_184280_f = Sets.newHashSet();
    private final List<TileEntityBeacon.BeamSegment> field_174909_f = Lists.newArrayList();
    private boolean field_146015_k;
    public int field_146012_l = -1;
    @Nullable
    public Potion field_146013_m;
    @Nullable
    public Potion field_146010_n;
    private ItemStack field_146011_o;
    private String field_146008_p;
    // CraftBukkit start - add fields and methods
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;

    public List<ItemStack> getContents() {
        return Arrays.asList(this.field_146011_o);
    }

    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
    }

    public List<HumanEntity> getViewers() {
        return transaction;
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }

    public PotionEffect getPrimaryEffect() {
        return (this.field_146013_m != null) ? CraftPotionUtil.toBukkit(new PotionEffect(this.field_146013_m, getLevel(), getAmplification(), true, true)) : null;
    }

    public PotionEffect getSecondaryEffect() {
        return (hasSecondaryEffect()) ? CraftPotionUtil.toBukkit(new PotionEffect(this.field_146010_n, getLevel(), getAmplification(), true, true)) : null;
    }
    // CraftBukkit end

    public TileEntityBeacon() {
        this.field_146011_o = ItemStack.field_190927_a;
    }

    public void func_73660_a() {
        if (this.field_145850_b.func_82737_E() % 80L == 0L) {
            this.func_174908_m();
        }

    }

    public void func_174908_m() {
        if (this.field_145850_b != null) {
            this.func_146003_y();
            this.func_146000_x();
        }

    }

    // CraftBukkit start - split into components
    private byte getAmplification() {
        {
            byte b0 = 0;

            if (this.field_146012_l >= 4 && this.field_146013_m == this.field_146010_n) {
                b0 = 1;
            }

            return b0;
        }
    }

    private int getLevel() {
        {
            int i = (9 + this.field_146012_l * 2) * 20;
            return i;
        }
    }

    public List getHumansInRange() {
        {
            double d0 = (double) (this.field_146012_l * 10 + 10);

            int j = this.field_174879_c.func_177958_n();
            int k = this.field_174879_c.func_177956_o();
            int l = this.field_174879_c.func_177952_p();
            AxisAlignedBB axisalignedbb = (new AxisAlignedBB((double) j, (double) k, (double) l, (double) (j + 1), (double) (k + 1), (double) (l + 1))).func_186662_g(d0).func_72321_a(0.0D, (double) this.field_145850_b.func_72800_K(), 0.0D);
            List list = this.field_145850_b.func_72872_a(EntityPlayer.class, axisalignedbb);

            return list;
        }
    }

    private void applyEffect(List list, Potion effects, int i, int b0) {
        // Paper - BeaconEffectEvent
        applyEffect(list, effects, i, b0, true);
    }

    private void applyEffect(List list, Potion effects, int i, int b0, boolean isPrimary) {
        // Paper - BeaconEffectEvent
        {
            Iterator iterator = list.iterator();

            EntityPlayer entityhuman;

            // Paper start - BeaconEffectEvent
            org.bukkit.block.Block block = field_145850_b.getWorld().getBlockAt(field_174879_c.func_177958_n(), field_174879_c.func_177956_o(), field_174879_c.func_177952_p());
            PotionEffect effect = CraftPotionUtil.toBukkit(new PotionEffect(effects, i, b0, true, true));
            // Paper end

            while (iterator.hasNext()) {
                entityhuman = (EntityPlayer) iterator.next();
                // Paper start - BeaconEffectEvent
                BeaconEffectEvent event = new BeaconEffectEvent(block, effect, (Player) entityhuman.getBukkitEntity(), isPrimary);
                if (CraftEventFactory.callEvent(event).isCancelled()) continue;
                PotionEffect eventEffect = event.getEffect();
                entityhuman.getBukkitEntity().addPotionEffect(eventEffect, true);
                // Paper end
            }
        }
    }

    private boolean hasSecondaryEffect() {
        {
            if (this.field_146012_l >= 4 && this.field_146013_m != this.field_146010_n && this.field_146010_n != null) {
                return true;
            }

            return false;
        }
    }

    private void func_146000_x() {
        if (this.field_146015_k && this.field_146012_l > 0 && !this.field_145850_b.field_72995_K && this.field_146013_m != null) {
            byte b0 = getAmplification();

            int i = getLevel();
            List list = getHumansInRange();

            applyEffect(list, this.field_146013_m, i, b0, true); // Paper - BeaconEffectEvent

            if (hasSecondaryEffect()) {
                applyEffect(list, this.field_146010_n, i, 0, false); // Paper - BeaconEffectEvent
            }
        }

    }
    // CraftBukkit end

    private void func_146003_y() {
        int i = this.field_174879_c.func_177958_n();
        int j = this.field_174879_c.func_177956_o();
        int k = this.field_174879_c.func_177952_p();
        int l = this.field_146012_l;

        this.field_146012_l = 0;
        this.field_174909_f.clear();
        this.field_146015_k = true;
        TileEntityBeacon.BeamSegment tileentitybeacon_beaconcolortracker = new TileEntityBeacon.BeamSegment(EnumDyeColor.WHITE.func_193349_f());

        this.field_174909_f.add(tileentitybeacon_beaconcolortracker);
        boolean flag = true;
        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

        int i1;

        for (i1 = j + 1; i1 < 256; ++i1) {
            IBlockState iblockdata = this.field_145850_b.func_180495_p(blockposition_mutableblockposition.func_181079_c(i, i1, k));
            float[] afloat;

            if (iblockdata.func_177230_c() == Blocks.field_150399_cn) {
                afloat = ((EnumDyeColor) iblockdata.func_177229_b(BlockStainedGlass.field_176547_a)).func_193349_f();
            } else {
                if (iblockdata.func_177230_c() != Blocks.field_150397_co) {
                    if (iblockdata.func_185891_c() >= 15 && iblockdata.func_177230_c() != Blocks.field_150357_h) {
                        this.field_146015_k = false;
                        this.field_174909_f.clear();
                        break;
                    }

                    tileentitybeacon_beaconcolortracker.func_177262_a();
                    continue;
                }

                afloat = ((EnumDyeColor) iblockdata.func_177229_b(BlockStainedGlassPane.field_176245_a)).func_193349_f();
            }

            if (!flag) {
                afloat = new float[] { (tileentitybeacon_beaconcolortracker.func_177263_b()[0] + afloat[0]) / 2.0F, (tileentitybeacon_beaconcolortracker.func_177263_b()[1] + afloat[1]) / 2.0F, (tileentitybeacon_beaconcolortracker.func_177263_b()[2] + afloat[2]) / 2.0F};
            }

            if (Arrays.equals(afloat, tileentitybeacon_beaconcolortracker.func_177263_b())) {
                tileentitybeacon_beaconcolortracker.func_177262_a();
            } else {
                tileentitybeacon_beaconcolortracker = new TileEntityBeacon.BeamSegment(afloat);
                this.field_174909_f.add(tileentitybeacon_beaconcolortracker);
            }

            flag = false;
        }

        if (this.field_146015_k) {
            for (i1 = 1; i1 <= 4; this.field_146012_l = i1++) {
                int j1 = j - i1;

                if (j1 < 0) {
                    break;
                }

                boolean flag1 = true;

                for (int k1 = i - i1; k1 <= i + i1 && flag1; ++k1) {
                    for (int l1 = k - i1; l1 <= k + i1; ++l1) {
                        Block block = this.field_145850_b.func_180495_p(new BlockPos(k1, j1, l1)).func_177230_c();

                        if (block != Blocks.field_150475_bE && block != Blocks.field_150340_R && block != Blocks.field_150484_ah && block != Blocks.field_150339_S) {
                            flag1 = false;
                            break;
                        }
                    }
                }

                if (!flag1) {
                    break;
                }
            }

            if (this.field_146012_l == 0) {
                this.field_146015_k = false;
            }
        }

        if (!this.field_145850_b.field_72995_K && l < this.field_146012_l) {
            Iterator iterator = this.field_145850_b.func_72872_a(EntityPlayerMP.class, (new AxisAlignedBB((double) i, (double) j, (double) k, (double) i, (double) (j - 4), (double) k)).func_72314_b(10.0D, 5.0D, 10.0D)).iterator();

            while (iterator.hasNext()) {
                EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

                CriteriaTriggers.field_192131_k.func_192180_a(entityplayer, this);
            }
        }

    }

    public int func_191979_s() {
        return this.field_146012_l;
    }

    @Nullable
    public SPacketUpdateTileEntity func_189518_D_() {
        return new SPacketUpdateTileEntity(this.field_174879_c, 3, this.func_189517_E_());
    }

    public NBTTagCompound func_189517_E_() {
        return this.func_189515_b(new NBTTagCompound());
    }

    @Nullable
    private static Potion func_184279_f(int i) {
        Potion mobeffectlist = Potion.func_188412_a(i);

        return TileEntityBeacon.field_184280_f.contains(mobeffectlist) ? mobeffectlist : null;
    }

    public void func_145839_a(NBTTagCompound nbttagcompound) {
        super.func_145839_a(nbttagcompound);
        // Craftbukkit start - persist manually set non-default beacon effects (SPIGOT-3598)
        this.field_146013_m = Potion.func_188412_a(nbttagcompound.func_74762_e("Primary"));
        this.field_146010_n = Potion.func_188412_a(nbttagcompound.func_74762_e("Secondary"));
        // Craftbukkit end
        this.field_146012_l = nbttagcompound.func_74762_e("Levels");
    }

    public NBTTagCompound func_189515_b(NBTTagCompound nbttagcompound) {
        super.func_189515_b(nbttagcompound);
        nbttagcompound.func_74768_a("Primary", Potion.func_188409_a(this.field_146013_m));
        nbttagcompound.func_74768_a("Secondary", Potion.func_188409_a(this.field_146010_n));
        nbttagcompound.func_74768_a("Levels", this.field_146012_l);
        return nbttagcompound;
    }

    public int func_70302_i_() {
        return 1;
    }

    public boolean func_191420_l() {
        return this.field_146011_o.func_190926_b();
    }

    public ItemStack func_70301_a(int i) {
        return i == 0 ? this.field_146011_o : ItemStack.field_190927_a;
    }

    public ItemStack func_70298_a(int i, int j) {
        if (i == 0 && !this.field_146011_o.func_190926_b()) {
            if (j >= this.field_146011_o.func_190916_E()) {
                ItemStack itemstack = this.field_146011_o;

                this.field_146011_o = ItemStack.field_190927_a;
                return itemstack;
            } else {
                return this.field_146011_o.func_77979_a(j);
            }
        } else {
            return ItemStack.field_190927_a;
        }
    }

    public ItemStack func_70304_b(int i) {
        if (i == 0) {
            ItemStack itemstack = this.field_146011_o;

            this.field_146011_o = ItemStack.field_190927_a;
            return itemstack;
        } else {
            return ItemStack.field_190927_a;
        }
    }

    public void func_70299_a(int i, ItemStack itemstack) {
        if (i == 0) {
            this.field_146011_o = itemstack;
        }

    }

    public String func_70005_c_() {
        return this.func_145818_k_() ? this.field_146008_p : "container.beacon";
    }

    public boolean func_145818_k_() {
        return this.field_146008_p != null && !this.field_146008_p.isEmpty();
    }

    public void func_145999_a(String s) {
        this.field_146008_p = s;
    }

    public int func_70297_j_() {
        return 1;
    }

    public boolean func_70300_a(EntityPlayer entityhuman) {
        return this.field_145850_b.func_175625_s(this.field_174879_c) != this ? false : entityhuman.func_70092_e((double) this.field_174879_c.func_177958_n() + 0.5D, (double) this.field_174879_c.func_177956_o() + 0.5D, (double) this.field_174879_c.func_177952_p() + 0.5D) <= 64.0D;
    }

    public void func_174889_b(EntityPlayer entityhuman) {}

    public void func_174886_c(EntityPlayer entityhuman) {}

    public boolean func_94041_b(int i, ItemStack itemstack) {
        return itemstack.func_77973_b() == Items.field_151166_bC || itemstack.func_77973_b() == Items.field_151045_i || itemstack.func_77973_b() == Items.field_151043_k || itemstack.func_77973_b() == Items.field_151042_j;
    }

    public String func_174875_k() {
        return "minecraft:beacon";
    }

    public Container func_174876_a(InventoryPlayer playerinventory, EntityPlayer entityhuman) {
        return new ContainerBeacon(playerinventory, this);
    }

    public int func_174887_a_(int i) {
        switch (i) {
        case 0:
            return this.field_146012_l;

        case 1:
            return Potion.func_188409_a(this.field_146013_m);

        case 2:
            return Potion.func_188409_a(this.field_146010_n);

        default:
            return 0;
        }
    }

    public void func_174885_b(int i, int j) {
        switch (i) {
        case 0:
            this.field_146012_l = j;
            break;

        case 1:
            this.field_146013_m = func_184279_f(j);
            break;

        case 2:
            this.field_146010_n = func_184279_f(j);
        }

    }

    public int func_174890_g() {
        return 3;
    }

    public void func_174888_l() {
        this.field_146011_o = ItemStack.field_190927_a;
    }

    public boolean func_145842_c(int i, int j) {
        if (i == 1) {
            this.func_174908_m();
            return true;
        } else {
            return super.func_145842_c(i, j);
        }
    }

    public int[] func_180463_a(EnumFacing enumdirection) {
        return new int[0];
    }

    public boolean func_180462_a(int i, ItemStack itemstack, EnumFacing enumdirection) {
        return false;
    }

    public boolean func_180461_b(int i, ItemStack itemstack, EnumFacing enumdirection) {
        return false;
    }

    static {
        Potion[][] amobeffectlist = TileEntityBeacon.field_146009_a;
        int i = amobeffectlist.length;

        for (int j = 0; j < i; ++j) {
            Potion[] amobeffectlist1 = amobeffectlist[j];

            Collections.addAll(TileEntityBeacon.field_184280_f, amobeffectlist1);
        }

    }

    public static class BeamSegment {

        private final float[] field_177266_a;
        private int field_177265_b;

        public BeamSegment(float[] afloat) {
            this.field_177266_a = afloat;
            this.field_177265_b = 1;
        }

        protected void func_177262_a() {
            ++this.field_177265_b;
        }

        public float[] func_177263_b() {
            return this.field_177266_a;
        }
    }
}
