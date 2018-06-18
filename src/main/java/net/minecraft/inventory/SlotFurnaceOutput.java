package net.minecraft.inventory;

// CraftBukkit start
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.MathHelper;

// CraftBukkit end

public class SlotFurnaceOutput extends Slot {

    private final EntityPlayer player;public EntityPlayer getPlayer() { return player; } // Paper OBFHELPER
    private int removeCount;

    public SlotFurnaceOutput(EntityPlayer entityhuman, IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
        this.player = entityhuman;
    }

    public boolean isItemValid(ItemStack itemstack) {
        return false;
    }

    public ItemStack decrStackSize(int i) {
        if (this.getHasStack()) {
            this.removeCount += Math.min(i, this.getStack().getCount());
        }

        return super.decrStackSize(i);
    }

    public ItemStack onTake(EntityPlayer entityhuman, ItemStack itemstack) {
        this.onCrafting(itemstack);
        super.onTake(entityhuman, itemstack);
        return itemstack;
    }

    protected void onCrafting(ItemStack itemstack, int i) {
        this.removeCount += i;
        this.onCrafting(itemstack);
    }

    protected void onCrafting(ItemStack itemstack) {
        itemstack.onCrafting(this.player.world, this.player, this.removeCount);
        if (!this.player.world.isRemote) {
            int i = this.removeCount;
            float f = FurnaceRecipes.instance().getSmeltingExperience(itemstack);
            int j;

            if (f == 0.0F) {
                i = 0;
            } else if (f < 1.0F) {
                j = MathHelper.floor((float) i * f);
                if (j < MathHelper.ceil((float) i * f) && Math.random() < (double) ((float) i * f - (float) j)) {
                    ++j;
                }

                i = j;
            }

            // CraftBukkit start - fire FurnaceExtractEvent
            Player player = (Player) player.getBukkitEntity();
            TileEntityFurnace furnace = ((TileEntityFurnace) this.inventory);
            org.bukkit.block.Block block = player.world.getWorld().getBlockAt(furnace.pos.getX(), furnace.pos.getY(), furnace.pos.getZ());

            if (removeCount != 0) {
                FurnaceExtractEvent event = new FurnaceExtractEvent(player, block, org.bukkit.craftbukkit.util.CraftMagicNumbers.getMaterial(itemstack.getItem()), removeCount, i);
                player.world.getServer().getPluginManager().callEvent(event);
                i = event.getExpToDrop();
            }
            // CraftBukkit end

            while (i > 0) {
                j = EntityXPOrb.getXPSplit(i);
                i -= j;
                this.player.world.spawnEntity(new EntityXPOrb(this.player.world, this.player.posX, this.player.posY + 0.5D, this.player.posZ + 0.5D, j, org.bukkit.entity.ExperienceOrb.SpawnReason.FURNACE, getPlayer())); // Paper
            }
        }

        this.removeCount = 0;
    }
}
