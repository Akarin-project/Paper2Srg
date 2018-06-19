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

    private final EntityPlayer field_75229_a;public EntityPlayer getPlayer() { return field_75229_a; } // Paper OBFHELPER
    private int field_75228_b;

    public SlotFurnaceOutput(EntityPlayer entityhuman, IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
        this.field_75229_a = entityhuman;
    }

    public boolean func_75214_a(ItemStack itemstack) {
        return false;
    }

    public ItemStack func_75209_a(int i) {
        if (this.func_75216_d()) {
            this.field_75228_b += Math.min(i, this.func_75211_c().func_190916_E());
        }

        return super.func_75209_a(i);
    }

    public ItemStack func_190901_a(EntityPlayer entityhuman, ItemStack itemstack) {
        this.func_75208_c(itemstack);
        super.func_190901_a(entityhuman, itemstack);
        return itemstack;
    }

    protected void func_75210_a(ItemStack itemstack, int i) {
        this.field_75228_b += i;
        this.func_75208_c(itemstack);
    }

    protected void func_75208_c(ItemStack itemstack) {
        itemstack.func_77980_a(this.field_75229_a.field_70170_p, this.field_75229_a, this.field_75228_b);
        if (!this.field_75229_a.field_70170_p.field_72995_K) {
            int i = this.field_75228_b;
            float f = FurnaceRecipes.func_77602_a().func_151398_b(itemstack);
            int j;

            if (f == 0.0F) {
                i = 0;
            } else if (f < 1.0F) {
                j = MathHelper.func_76141_d((float) i * f);
                if (j < MathHelper.func_76123_f((float) i * f) && Math.random() < (double) ((float) i * f - (float) j)) {
                    ++j;
                }

                i = j;
            }

            // CraftBukkit start - fire FurnaceExtractEvent
            Player player = (Player) field_75229_a.getBukkitEntity();
            TileEntityFurnace furnace = ((TileEntityFurnace) this.field_75224_c);
            org.bukkit.block.Block block = field_75229_a.field_70170_p.getWorld().getBlockAt(furnace.field_174879_c.func_177958_n(), furnace.field_174879_c.func_177956_o(), furnace.field_174879_c.func_177952_p());

            if (field_75228_b != 0) {
                FurnaceExtractEvent event = new FurnaceExtractEvent(player, block, org.bukkit.craftbukkit.util.CraftMagicNumbers.getMaterial(itemstack.func_77973_b()), field_75228_b, i);
                field_75229_a.field_70170_p.getServer().getPluginManager().callEvent(event);
                i = event.getExpToDrop();
            }
            // CraftBukkit end

            while (i > 0) {
                j = EntityXPOrb.func_70527_a(i);
                i -= j;
                this.field_75229_a.field_70170_p.func_72838_d(new EntityXPOrb(this.field_75229_a.field_70170_p, this.field_75229_a.field_70165_t, this.field_75229_a.field_70163_u + 0.5D, this.field_75229_a.field_70161_v + 0.5D, j, org.bukkit.entity.ExperienceOrb.SpawnReason.FURNACE, getPlayer())); // Paper
            }
        }

        this.field_75228_b = 0;
    }
}
