package net.minecraft.entity.item;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityPainting extends EntityHanging {

    public EntityPainting.EnumArt field_70522_e;

    public EntityPainting(World world) {
        super(world);
        this.field_70522_e = EnumArt.values()[this.field_70146_Z.nextInt(EnumArt.values().length)]; // CraftBukkit - generate a non-null painting
    }

    public EntityPainting(World world, BlockPos blockposition, EnumFacing enumdirection) {
        super(world, blockposition);
        ArrayList arraylist = Lists.newArrayList();
        int i = 0;
        EntityPainting.EnumArt[] aentitypainting_enumart = EntityPainting.EnumArt.values();
        int j = aentitypainting_enumart.length;

        for (int k = 0; k < j; ++k) {
            EntityPainting.EnumArt entitypainting_enumart = aentitypainting_enumart[k];

            this.field_70522_e = entitypainting_enumart;
            this.func_174859_a(enumdirection);
            if (this.func_70518_d()) {
                arraylist.add(entitypainting_enumart);
                int l = entitypainting_enumart.field_75703_B * entitypainting_enumart.field_75704_C;

                if (l > i) {
                    i = l;
                }
            }
        }

        if (!arraylist.isEmpty()) {
            Iterator iterator = arraylist.iterator();

            while (iterator.hasNext()) {
                EntityPainting.EnumArt entitypainting_enumart1 = (EntityPainting.EnumArt) iterator.next();

                if (entitypainting_enumart1.field_75703_B * entitypainting_enumart1.field_75704_C < i) {
                    iterator.remove();
                }
            }

            this.field_70522_e = (EntityPainting.EnumArt) arraylist.get(this.field_70146_Z.nextInt(arraylist.size()));
        }

        this.func_174859_a(enumdirection);
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        nbttagcompound.func_74778_a("Motive", this.field_70522_e.field_75702_A);
        super.func_70014_b(nbttagcompound);
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        String s = nbttagcompound.func_74779_i("Motive");
        EntityPainting.EnumArt[] aentitypainting_enumart = EntityPainting.EnumArt.values();
        int i = aentitypainting_enumart.length;

        for (int j = 0; j < i; ++j) {
            EntityPainting.EnumArt entitypainting_enumart = aentitypainting_enumart[j];

            if (entitypainting_enumart.field_75702_A.equals(s)) {
                this.field_70522_e = entitypainting_enumart;
            }
        }

        if (this.field_70522_e == null) {
            this.field_70522_e = EntityPainting.EnumArt.KEBAB;
        }

        super.func_70037_a(nbttagcompound);
    }

    public int func_82329_d() {
        return this.field_70522_e.field_75703_B;
    }

    public int func_82330_g() {
        return this.field_70522_e.field_75704_C;
    }

    public void func_110128_b(@Nullable Entity entity) {
        if (this.field_70170_p.func_82736_K().func_82766_b("doEntityDrops")) {
            this.func_184185_a(SoundEvents.field_187691_dJ, 1.0F, 1.0F);
            if (entity instanceof EntityPlayer) {
                EntityPlayer entityhuman = (EntityPlayer) entity;

                if (entityhuman.field_71075_bZ.field_75098_d) {
                    return;
                }
            }

            this.func_70099_a(new ItemStack(Items.field_151159_an), 0.0F);
        }
    }

    public void func_184523_o() {
        this.func_184185_a(SoundEvents.field_187694_dK, 1.0F, 1.0F);
    }

    public void func_70012_b(double d0, double d1, double d2, float f, float f1) {
        this.func_70107_b(d0, d1, d2);
    }

    public static enum EnumArt {

        KEBAB("Kebab", 16, 16, 0, 0), AZTEC("Aztec", 16, 16, 16, 0), ALBAN("Alban", 16, 16, 32, 0), AZTEC_2("Aztec2", 16, 16, 48, 0), BOMB("Bomb", 16, 16, 64, 0), PLANT("Plant", 16, 16, 80, 0), WASTELAND("Wasteland", 16, 16, 96, 0), POOL("Pool", 32, 16, 0, 32), COURBET("Courbet", 32, 16, 32, 32), SEA("Sea", 32, 16, 64, 32), SUNSET("Sunset", 32, 16, 96, 32), CREEBET("Creebet", 32, 16, 128, 32), WANDERER("Wanderer", 16, 32, 0, 64), GRAHAM("Graham", 16, 32, 16, 64), MATCH("Match", 32, 32, 0, 128), BUST("Bust", 32, 32, 32, 128), STAGE("Stage", 32, 32, 64, 128), VOID("Void", 32, 32, 96, 128), SKULL_AND_ROSES("SkullAndRoses", 32, 32, 128, 128), WITHER("Wither", 32, 32, 160, 128), FIGHTERS("Fighters", 64, 32, 0, 96), POINTER("Pointer", 64, 64, 0, 192), PIGSCENE("Pigscene", 64, 64, 64, 192), BURNING_SKULL("BurningSkull", 64, 64, 128, 192), SKELETON("Skeleton", 64, 48, 192, 64), DONKEY_KONG("DonkeyKong", 64, 48, 192, 112);

        public static final int field_180001_A = "SkullAndRoses".length();
        public final String field_75702_A;
        public final int field_75703_B;
        public final int field_75704_C;
        public final int field_75699_D;
        public final int field_75700_E;

        private EnumArt(String s, int i, int j, int k, int l) {
            this.field_75702_A = s;
            this.field_75703_B = i;
            this.field_75704_C = j;
            this.field_75699_D = k;
            this.field_75700_E = l;
        }
    }
}
