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

    public EntityPainting.EnumArt art;

    public EntityPainting(World world) {
        super(world);
        this.art = EnumArt.values()[this.rand.nextInt(EnumArt.values().length)]; // CraftBukkit - generate a non-null painting
    }

    public EntityPainting(World world, BlockPos blockposition, EnumFacing enumdirection) {
        super(world, blockposition);
        ArrayList arraylist = Lists.newArrayList();
        int i = 0;
        EntityPainting.EnumArt[] aentitypainting_enumart = EntityPainting.EnumArt.values();
        int j = aentitypainting_enumart.length;

        for (int k = 0; k < j; ++k) {
            EntityPainting.EnumArt entitypainting_enumart = aentitypainting_enumart[k];

            this.art = entitypainting_enumart;
            this.updateFacingWithBoundingBox(enumdirection);
            if (this.onValidSurface()) {
                arraylist.add(entitypainting_enumart);
                int l = entitypainting_enumart.sizeX * entitypainting_enumart.sizeY;

                if (l > i) {
                    i = l;
                }
            }
        }

        if (!arraylist.isEmpty()) {
            Iterator iterator = arraylist.iterator();

            while (iterator.hasNext()) {
                EntityPainting.EnumArt entitypainting_enumart1 = (EntityPainting.EnumArt) iterator.next();

                if (entitypainting_enumart1.sizeX * entitypainting_enumart1.sizeY < i) {
                    iterator.remove();
                }
            }

            this.art = (EntityPainting.EnumArt) arraylist.get(this.rand.nextInt(arraylist.size()));
        }

        this.updateFacingWithBoundingBox(enumdirection);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setString("Motive", this.art.title);
        super.writeEntityToNBT(nbttagcompound);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        String s = nbttagcompound.getString("Motive");
        EntityPainting.EnumArt[] aentitypainting_enumart = EntityPainting.EnumArt.values();
        int i = aentitypainting_enumart.length;

        for (int j = 0; j < i; ++j) {
            EntityPainting.EnumArt entitypainting_enumart = aentitypainting_enumart[j];

            if (entitypainting_enumart.title.equals(s)) {
                this.art = entitypainting_enumart;
            }
        }

        if (this.art == null) {
            this.art = EntityPainting.EnumArt.KEBAB;
        }

        super.readEntityFromNBT(nbttagcompound);
    }

    public int getWidthPixels() {
        return this.art.sizeX;
    }

    public int getHeightPixels() {
        return this.art.sizeY;
    }

    public void onBroken(@Nullable Entity entity) {
        if (this.world.getGameRules().getBoolean("doEntityDrops")) {
            this.playSound(SoundEvents.ENTITY_PAINTING_BREAK, 1.0F, 1.0F);
            if (entity instanceof EntityPlayer) {
                EntityPlayer entityhuman = (EntityPlayer) entity;

                if (entityhuman.capabilities.isCreativeMode) {
                    return;
                }
            }

            this.entityDropItem(new ItemStack(Items.PAINTING), 0.0F);
        }
    }

    public void playPlaceSound() {
        this.playSound(SoundEvents.ENTITY_PAINTING_PLACE, 1.0F, 1.0F);
    }

    public void setLocationAndAngles(double d0, double d1, double d2, float f, float f1) {
        this.setPosition(d0, d1, d2);
    }

    public static enum EnumArt {

        KEBAB("Kebab", 16, 16, 0, 0), AZTEC("Aztec", 16, 16, 16, 0), ALBAN("Alban", 16, 16, 32, 0), AZTEC_2("Aztec2", 16, 16, 48, 0), BOMB("Bomb", 16, 16, 64, 0), PLANT("Plant", 16, 16, 80, 0), WASTELAND("Wasteland", 16, 16, 96, 0), POOL("Pool", 32, 16, 0, 32), COURBET("Courbet", 32, 16, 32, 32), SEA("Sea", 32, 16, 64, 32), SUNSET("Sunset", 32, 16, 96, 32), CREEBET("Creebet", 32, 16, 128, 32), WANDERER("Wanderer", 16, 32, 0, 64), GRAHAM("Graham", 16, 32, 16, 64), MATCH("Match", 32, 32, 0, 128), BUST("Bust", 32, 32, 32, 128), STAGE("Stage", 32, 32, 64, 128), VOID("Void", 32, 32, 96, 128), SKULL_AND_ROSES("SkullAndRoses", 32, 32, 128, 128), WITHER("Wither", 32, 32, 160, 128), FIGHTERS("Fighters", 64, 32, 0, 96), POINTER("Pointer", 64, 64, 0, 192), PIGSCENE("Pigscene", 64, 64, 64, 192), BURNING_SKULL("BurningSkull", 64, 64, 128, 192), SKELETON("Skeleton", 64, 48, 192, 64), DONKEY_KONG("DonkeyKong", 64, 48, 192, 112);

        public static final int MAX_NAME_LENGTH = "SkullAndRoses".length();
        public final String title;
        public final int sizeX;
        public final int sizeY;
        public final int offsetX;
        public final int offsetY;

        private EnumArt(String s, int i, int j, int k, int l) {
            this.title = s;
            this.sizeX = i;
            this.sizeY = j;
            this.offsetX = k;
            this.offsetY = l;
        }
    }
}
