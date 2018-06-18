package net.minecraft.item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;


public class ItemWritableBook extends Item {

    public ItemWritableBook() {
        this.setMaxStackSize(1);
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        entityhuman.openBook(itemstack, enumhand);
        entityhuman.addStat(StatList.getObjectUseStats((Item) this));
        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }

    public static boolean isNBTValid(NBTTagCompound nbttagcompound) {
        if (nbttagcompound == null) {
            return false;
        } else if (!nbttagcompound.hasKey("pages", 9)) {
            return false;
        } else {
            NBTTagList nbttaglist = nbttagcompound.getTagList("pages", 8);

            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                String s = nbttaglist.getStringTagAt(i);

                if (s.length() > 32767) {
                    return false;
                }
            }

            return true;
        }
    }
}
