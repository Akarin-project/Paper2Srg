package net.minecraft.item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;


public class ItemWrittenBook extends Item {

    public ItemWrittenBook() {
        this.setMaxStackSize(1);
    }

    public static boolean validBookTagContents(NBTTagCompound nbttagcompound) {
        if (!ItemWritableBook.isNBTValid(nbttagcompound)) {
            return false;
        } else if (!nbttagcompound.hasKey("title", 8)) {
            return false;
        } else {
            String s = nbttagcompound.getString("title");

            return s != null && s.length() <= 32 ? nbttagcompound.hasKey("author", 8) : false;
        }
    }

    public static int getGeneration(ItemStack itemstack) {
        return itemstack.getTagCompound().getInteger("generation");
    }

    public String getItemStackDisplayName(ItemStack itemstack) {
        if (itemstack.hasTagCompound()) {
            NBTTagCompound nbttagcompound = itemstack.getTagCompound();
            String s = nbttagcompound.getString("title");

            if (!StringUtils.isNullOrEmpty(s)) {
                return s;
            }
        }

        return super.getItemStackDisplayName(itemstack);
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (!world.isRemote) {
            this.resolveContents(itemstack, entityhuman);
        }

        entityhuman.openBook(itemstack, enumhand);
        entityhuman.addStat(StatList.getObjectUseStats((Item) this));
        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }

    private void resolveContents(ItemStack itemstack, EntityPlayer entityhuman) {
        if (itemstack.getTagCompound() != null) {
            NBTTagCompound nbttagcompound = itemstack.getTagCompound();

            if (!nbttagcompound.getBoolean("resolved")) {
                nbttagcompound.setBoolean("resolved", true);
                if (validBookTagContents(nbttagcompound)) {
                    NBTTagList nbttaglist = nbttagcompound.getTagList("pages", 8);

                    for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                        String s = nbttaglist.getStringTagAt(i);

                        Object object;

                        // CraftBukkit start
                        // Some commands use the worldserver variable but we leave it full of null values,
                        // so we must temporarily populate it with the world of the commandsender
                        WorldServer[] prev = MinecraftServer.getServer().worlds;
                        MinecraftServer server = MinecraftServer.getServer();
                        server.worlds = new WorldServer[server.worlds.size()];
                        server.worlds[0] = (WorldServer) entityhuman.getEntityWorld();
                        int bpos = 0;
                        for (int pos = 1; pos < server.worlds.length; pos++) {
                            WorldServer world = server.worlds.get(bpos++);
                            if (server.worlds[0] == world) {
                                pos--;
                                continue;
                            }
                            server.worlds[pos] = world;
                        }
                        // CraftBukkit end
                        try {
                            ITextComponent ichatbasecomponent = ITextComponent.Serializer.fromJsonLenient(s);

                            object = TextComponentUtils.processComponent(entityhuman, ichatbasecomponent, entityhuman);
                        } catch (Exception exception) {
                            object = new TextComponentString(s);
                        }
                        finally { MinecraftServer.getServer().worlds = prev; } // CraftBukkit

                        nbttaglist.set(i, new NBTTagString(ITextComponent.Serializer.componentToJson((ITextComponent) object)));
                    }

                    nbttagcompound.setTag("pages", nbttaglist);
                    if (entityhuman instanceof EntityPlayerMP && entityhuman.getHeldItemMainhand() == itemstack) {
                        Slot slot = entityhuman.openContainer.getSlotFromInventory(entityhuman.inventory, entityhuman.inventory.currentItem);

                        ((EntityPlayerMP) entityhuman).connection.sendPacket(new SPacketSetSlot(0, slot.slotNumber, itemstack));
                    }

                }
            }
        }
    }
}
