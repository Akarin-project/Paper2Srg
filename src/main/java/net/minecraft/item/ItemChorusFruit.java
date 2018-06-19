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

    public ItemStack func_77654_b(ItemStack itemstack, World world, EntityLivingBase entityliving) {
        ItemStack itemstack1 = super.func_77654_b(itemstack, world, entityliving);

        if (!world.field_72995_K) {
            double d0 = entityliving.field_70165_t;
            double d1 = entityliving.field_70163_u;
            double d2 = entityliving.field_70161_v;

            for (int i = 0; i < 16; ++i) {
                double d3 = entityliving.field_70165_t + (entityliving.func_70681_au().nextDouble() - 0.5D) * 16.0D;
                double d4 = MathHelper.func_151237_a(entityliving.field_70163_u + (double) (entityliving.func_70681_au().nextInt(16) - 8), 0.0D, (double) (world.func_72940_L() - 1));
                double d5 = entityliving.field_70161_v + (entityliving.func_70681_au().nextDouble() - 0.5D) * 16.0D;

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

                if (entityliving.func_184218_aH()) {
                    entityliving.func_184210_p();
                }

                if (entityliving.func_184595_k(d3, d4, d5)) {
                    world.func_184148_a((EntityPlayer) null, d0, d1, d2, SoundEvents.field_187544_ad, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    entityliving.func_184185_a(SoundEvents.field_187544_ad, 1.0F, 1.0F);
                    break;
                }
            }

            if (entityliving instanceof EntityPlayer) {
                ((EntityPlayer) entityliving).func_184811_cZ().func_185145_a(this, 20);
            }
        }

        return itemstack1;
    }
}
