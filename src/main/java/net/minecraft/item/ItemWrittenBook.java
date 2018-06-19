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
        this.func_77625_d(1);
    }

    public static boolean func_77828_a(NBTTagCompound nbttagcompound) {
        if (!ItemWritableBook.func_150930_a(nbttagcompound)) {
            return false;
        } else if (!nbttagcompound.func_150297_b("title", 8)) {
            return false;
        } else {
            String s = nbttagcompound.func_74779_i("title");

            return s != null && s.length() <= 32 ? nbttagcompound.func_150297_b("author", 8) : false;
        }
    }

    public static int func_179230_h(ItemStack itemstack) {
        return itemstack.func_77978_p().func_74762_e("generation");
    }

    public String func_77653_i(ItemStack itemstack) {
        if (itemstack.func_77942_o()) {
            NBTTagCompound nbttagcompound = itemstack.func_77978_p();
            String s = nbttagcompound.func_74779_i("title");

            if (!StringUtils.func_151246_b(s)) {
                return s;
            }
        }

        return super.func_77653_i(itemstack);
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (!world.field_72995_K) {
            this.func_179229_a(itemstack, entityhuman);
        }

        entityhuman.func_184814_a(itemstack, enumhand);
        entityhuman.func_71029_a(StatList.func_188057_b((Item) this));
        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }

    private void func_179229_a(ItemStack itemstack, EntityPlayer entityhuman) {
        if (itemstack.func_77978_p() != null) {
            NBTTagCompound nbttagcompound = itemstack.func_77978_p();

            if (!nbttagcompound.func_74767_n("resolved")) {
                nbttagcompound.func_74757_a("resolved", true);
                if (func_77828_a(nbttagcompound)) {
                    NBTTagList nbttaglist = nbttagcompound.func_150295_c("pages", 8);

                    for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
                        String s = nbttaglist.func_150307_f(i);

                        Object object;

                        // CraftBukkit start
                        // Some commands use the worldserver variable but we leave it full of null values,
                        // so we must temporarily populate it with the world of the commandsender
                        WorldServer[] prev = MinecraftServer.getServer().field_71305_c;
                        MinecraftServer server = MinecraftServer.getServer();
                        server.field_71305_c = new WorldServer[server.worlds.size()];
                        server.field_71305_c[0] = (WorldServer) entityhuman.func_130014_f_();
                        int bpos = 0;
                        for (int pos = 1; pos < server.field_71305_c.length; pos++) {
                            WorldServer world = server.worlds.get(bpos++);
                            if (server.field_71305_c[0] == world) {
                                pos--;
                                continue;
                            }
                            server.field_71305_c[pos] = world;
                        }
                        // CraftBukkit end
                        try {
                            ITextComponent ichatbasecomponent = ITextComponent.Serializer.func_186877_b(s);

                            object = TextComponentUtils.func_179985_a(entityhuman, ichatbasecomponent, entityhuman);
                        } catch (Exception exception) {
                            object = new TextComponentString(s);
                        }
                        finally { MinecraftServer.getServer().field_71305_c = prev; } // CraftBukkit

                        nbttaglist.func_150304_a(i, new NBTTagString(ITextComponent.Serializer.func_150696_a((ITextComponent) object)));
                    }

                    nbttagcompound.func_74782_a("pages", nbttaglist);
                    if (entityhuman instanceof EntityPlayerMP && entityhuman.func_184614_ca() == itemstack) {
                        Slot slot = entityhuman.field_71070_bA.func_75147_a(entityhuman.field_71071_by, entityhuman.field_71071_by.field_70461_c);

                        ((EntityPlayerMP) entityhuman).field_71135_a.func_147359_a(new SPacketSetSlot(0, slot.field_75222_d, itemstack));
                    }

                }
            }
        }
    }
}
