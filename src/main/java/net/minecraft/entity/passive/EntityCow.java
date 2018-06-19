package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
// CraftBukkit end

public class EntityCow extends EntityAnimal {

    public EntityCow(World world) {
        super(world);
        this.func_70105_a(0.9F, 1.4F);
    }

    public static void func_189790_b(DataFixer dataconvertermanager) {
        EntityLiving.func_189752_a(dataconvertermanager, EntityCow.class);
    }

    protected void func_184651_r() {
        this.field_70714_bg.func_75776_a(0, new EntityAISwimming(this));
        this.field_70714_bg.func_75776_a(1, new EntityAIPanic(this, 2.0D));
        this.field_70714_bg.func_75776_a(2, new EntityAIMate(this, 1.0D));
        this.field_70714_bg.func_75776_a(3, new EntityAITempt(this, 1.25D, Items.field_151015_O, false));
        this.field_70714_bg.func_75776_a(4, new EntityAIFollowParent(this, 1.25D));
        this.field_70714_bg.func_75776_a(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.field_70714_bg.func_75776_a(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.field_70714_bg.func_75776_a(7, new EntityAILookIdle(this));
    }

    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(10.0D);
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.20000000298023224D);
    }

    protected SoundEvent func_184639_G() {
        return SoundEvents.field_187558_ak;
    }

    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        return SoundEvents.field_187562_am;
    }

    protected SoundEvent func_184615_bR() {
        return SoundEvents.field_187560_al;
    }

    protected void func_180429_a(BlockPos blockposition, Block block) {
        this.func_184185_a(SoundEvents.field_187566_ao, 0.15F, 1.0F);
    }

    protected float func_70599_aP() {
        return 0.4F;
    }

    @Nullable
    protected ResourceLocation func_184647_J() {
        return LootTableList.field_186399_G;
    }

    public boolean func_184645_a(EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (itemstack.func_77973_b() == Items.field_151133_ar && !entityhuman.field_71075_bZ.field_75098_d && !this.func_70631_g_()) {
            // CraftBukkit start - Got milk?
            org.bukkit.Location loc = this.getBukkitEntity().getLocation();
            org.bukkit.event.player.PlayerBucketFillEvent event = CraftEventFactory.callPlayerBucketFillEvent(entityhuman, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), null, itemstack, Items.field_151117_aB);

            if (event.isCancelled()) {
                return false;
            }

            ItemStack result = CraftItemStack.asNMSCopy(event.getItemStack());
            entityhuman.func_184185_a(SoundEvents.field_187564_an, 1.0F, 1.0F);
            itemstack.func_190918_g(1);
            if (itemstack.func_190926_b()) {
                entityhuman.func_184611_a(enumhand, result);
            } else if (!entityhuman.field_71071_by.func_70441_a(result)) {
                entityhuman.func_71019_a(result, false);
            }
            // CraftBukkit end

            return true;
        } else {
            return super.func_184645_a(entityhuman, enumhand);
        }
    }

    public EntityCow func_90011_a(EntityAgeable entityageable) {
        return new EntityCow(this.field_70170_p);
    }

    public float func_70047_e() {
        return this.func_70631_g_() ? this.field_70131_O : 1.3F;
    }

    public EntityAgeable func_90011_a(EntityAgeable entityageable) {
        return this.func_90011_a(entityageable);
    }
}
