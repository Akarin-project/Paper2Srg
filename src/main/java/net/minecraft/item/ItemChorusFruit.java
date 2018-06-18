package net.minecraft.item;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

// CraftBukkit end

public class ItemChorusFruit extends ItemFood {

    public ItemChorusFruit(int i, float f) {
        super(i, f, false);
    }

    public ItemStack onItemUseFinish(ItemStack itemstack, World world, EntityLivingBase entityliving) {
        ItemStack itemstack1 = super.onItemUseFinish(itemstack, world, entityliving);

        if (!world.isRemote) {
            double d0 = entityliving.posX;
            double d1 = entityliving.posY;
            double d2 = entityliving.posZ;

            for (int i = 0; i < 16; ++i) {
                double d3 = entityliving.posX + (entityliving.getRNG().nextDouble() - 0.5D) * 16.0D;
                double d4 = MathHelper.clamp(entityliving.posY + (double) (entityliving.getRNG().nextInt(16) - 8), 0.0D, (double) (world.getActualHeight() - 1));
                double d5 = entityliving.posZ + (entityliving.getRNG().nextDouble() - 0.5D) * 16.0D;

                // CraftBukkit start
                if (entityliving instanceof EntityPlayerMP) {
                    Player player = ((EntityPlayerMP) entityliving).getBukkitEntity();
                    PlayerTeleportEvent teleEvent = new PlayerTeleportEvent(player, player.getLocation(), new Location(player.getWorld(), d3, d4, d5), PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT);
                    world.getServer().getPluginManager().callEvent(teleEvent);
                    if (teleEvent.isCancelled()) {
                        break;
                    }
                    d3 = teleEvent.getTo().getX();
                    d4 = teleEvent.getTo().getY();
                    d5 = teleEvent.getTo().getZ();
                }
                // CraftBukkit end

                if (entityliving.isRiding()) {
                    entityliving.dismountRidingEntity();
                }

                if (entityliving.attemptTeleport(d3, d4, d5)) {
                    world.playSound((EntityPlayer) null, d0, d1, d2, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    entityliving.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                    break;
                }
            }

            if (entityliving instanceof EntityPlayer) {
                ((EntityPlayer) entityliving).getCooldownTracker().setCooldown(this, 20);
            }
        }

        return itemstack1;
    }
}
