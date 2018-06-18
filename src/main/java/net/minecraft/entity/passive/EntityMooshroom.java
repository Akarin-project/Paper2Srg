package net.minecraft.entity.passive;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import org.bukkit.event.player.PlayerShearEntityEvent;

public class EntityMooshroom extends EntityCow {

    public EntityMooshroom(World world) {
        super(world);
        this.setSize(0.9F, 1.4F);
        this.spawnableBlock = Blocks.MYCELIUM;
    }

    public static void registerFixesMooshroom(DataFixer dataconvertermanager) {
        EntityLiving.registerFixesMob(dataconvertermanager, EntityMooshroom.class);
    }

    public boolean processInteract(EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (itemstack.getItem() == Items.BOWL && this.getGrowingAge() >= 0 && !entityhuman.capabilities.isCreativeMode) {
            itemstack.shrink(1);
            if (itemstack.isEmpty()) {
                entityhuman.setHeldItem(enumhand, new ItemStack(Items.MUSHROOM_STEW));
            } else if (!entityhuman.inventory.addItemStackToInventory(new ItemStack(Items.MUSHROOM_STEW))) {
                entityhuman.dropItem(new ItemStack(Items.MUSHROOM_STEW), false);
            }

            return true;
        } else if (itemstack.getItem() == Items.SHEARS && this.getGrowingAge() >= 0) {
            // CraftBukkit start
            PlayerShearEntityEvent event = new PlayerShearEntityEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), this.getBukkitEntity());
            this.world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return false;
            }
            // CraftBukkit end
            this.setDead();
            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY + (double) (this.height / 2.0F), this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
            if (!this.world.isRemote) {
                EntityCow entitycow = new EntityCow(this.world);

                entitycow.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
                entitycow.setHealth(this.getHealth());
                entitycow.renderYawOffset = this.renderYawOffset;
                if (this.hasCustomName()) {
                    entitycow.setCustomNameTag(this.getCustomNameTag());
                }

                this.world.spawnEntity(entitycow);

                for (int i = 0; i < 5; ++i) {
                    this.world.spawnEntity(new EntityItem(this.world, this.posX, this.posY + (double) this.height, this.posZ, new ItemStack(Blocks.RED_MUSHROOM)));
                }

                itemstack.damageItem(1, entityhuman);
                this.playSound(SoundEvents.ENTITY_MOOSHROOM_SHEAR, 1.0F, 1.0F);
            }

            return true;
        } else {
            return super.processInteract(entityhuman, enumhand);
        }
    }

    public EntityMooshroom createChild(EntityAgeable entityageable) {
        return new EntityMooshroom(this.world);
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_MUSHROOM_COW;
    }

    public EntityCow createChild(EntityAgeable entityageable) {
        return this.createChild(entityageable);
    }

    public EntityAgeable createChild(EntityAgeable entityageable) {
        return this.createChild(entityageable);
    }
}
