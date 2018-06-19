package net.minecraft.command;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandReplaceItem extends CommandBase {

    private static final Map<String, Integer> field_175785_a = Maps.newHashMap();

    public CommandReplaceItem() {}

    public String func_71517_b() {
        return "replaceitem";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.replaceitem.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 1) {
            throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
        } else {
            boolean flag;

            if ("entity".equals(astring[0])) {
                flag = false;
            } else {
                if (!"block".equals(astring[0])) {
                    throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
                }

                flag = true;
            }

            byte b0;

            if (flag) {
                if (astring.length < 6) {
                    throw new WrongUsageException("commands.replaceitem.block.usage", new Object[0]);
                }

                b0 = 4;
            } else {
                if (astring.length < 4) {
                    throw new WrongUsageException("commands.replaceitem.entity.usage", new Object[0]);
                }

                b0 = 2;
            }

            String s = astring[b0];
            int i = b0 + 1;
            int j = this.func_175783_e(astring[b0]);

            Item item;

            try {
                item = func_147179_f(icommandlistener, astring[i]);
            } catch (NumberInvalidException exceptioninvalidnumber) {
                if (Block.func_149684_b(astring[i]) != Blocks.field_150350_a) {
                    throw exceptioninvalidnumber;
                }

                item = null;
            }

            ++i;
            int k = astring.length > i ? func_175764_a(astring[i++], 1, item.func_77639_j()) : 1;
            int l = astring.length > i ? func_175755_a(astring[i++]) : 0;
            ItemStack itemstack = new ItemStack(item, k, l);

            if (astring.length > i) {
                String s1 = func_180529_a(astring, i);

                try {
                    itemstack.func_77982_d(JsonToNBT.func_180713_a(s1));
                } catch (NBTException mojangsonparseexception) {
                    throw new CommandException("commands.replaceitem.tagError", new Object[] { mojangsonparseexception.getMessage()});
                }
            }

            if (flag) {
                icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, 0);
                BlockPos blockposition = func_175757_a(icommandlistener, astring, 1, false);
                World world = icommandlistener.func_130014_f_();
                TileEntity tileentity = world.func_175625_s(blockposition);

                if (tileentity == null || !(tileentity instanceof IInventory)) {
                    throw new CommandException("commands.replaceitem.noContainer", new Object[] { Integer.valueOf(blockposition.func_177958_n()), Integer.valueOf(blockposition.func_177956_o()), Integer.valueOf(blockposition.func_177952_p())});
                }

                IInventory iinventory = (IInventory) tileentity;

                if (j >= 0 && j < iinventory.func_70302_i_()) {
                    iinventory.func_70299_a(j, itemstack);
                }
            } else {
                Entity entity = func_184885_b(minecraftserver, icommandlistener, astring[1]);

                icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, 0);
                if (entity instanceof EntityPlayer) {
                    ((EntityPlayer) entity).field_71069_bz.func_75142_b();
                }

                if (!entity.func_174820_d(j, itemstack)) {
                    throw new CommandException("commands.replaceitem.failed", new Object[] { s, Integer.valueOf(k), itemstack.func_190926_b() ? "Air" : itemstack.func_151000_E()});
                }

                if (entity instanceof EntityPlayer) {
                    ((EntityPlayer) entity).field_71069_bz.func_75142_b();
                }
            }

            icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, k);
            func_152373_a(icommandlistener, (ICommand) this, "commands.replaceitem.success", new Object[] { s, Integer.valueOf(k), itemstack.func_190926_b() ? "Air" : itemstack.func_151000_E()});
        }
    }

    private int func_175783_e(String s) throws CommandException {
        if (!CommandReplaceItem.field_175785_a.containsKey(s)) {
            throw new CommandException("commands.generic.parameter.invalid", new Object[] { s});
        } else {
            return ((Integer) CommandReplaceItem.field_175785_a.get(s)).intValue();
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, new String[] { "entity", "block"}) : (astring.length == 2 && "entity".equals(astring[0]) ? func_71530_a(astring, minecraftserver.func_71213_z()) : (astring.length >= 2 && astring.length <= 4 && "block".equals(astring[0]) ? func_175771_a(astring, 1, blockposition) : ((astring.length != 3 || !"entity".equals(astring[0])) && (astring.length != 5 || !"block".equals(astring[0])) ? ((astring.length != 4 || !"entity".equals(astring[0])) && (astring.length != 6 || !"block".equals(astring[0])) ? Collections.emptyList() : func_175762_a(astring, (Collection) Item.field_150901_e.func_148742_b())) : func_175762_a(astring, (Collection) CommandReplaceItem.field_175785_a.keySet()))));
    }

    public boolean func_82358_a(String[] astring, int i) {
        return astring.length > 0 && "entity".equals(astring[0]) && i == 1;
    }

    static {
        int i;

        for (i = 0; i < 54; ++i) {
            CommandReplaceItem.field_175785_a.put("slot.container." + i, Integer.valueOf(i));
        }

        for (i = 0; i < 9; ++i) {
            CommandReplaceItem.field_175785_a.put("slot.hotbar." + i, Integer.valueOf(i));
        }

        for (i = 0; i < 27; ++i) {
            CommandReplaceItem.field_175785_a.put("slot.inventory." + i, Integer.valueOf(9 + i));
        }

        for (i = 0; i < 27; ++i) {
            CommandReplaceItem.field_175785_a.put("slot.enderchest." + i, Integer.valueOf(200 + i));
        }

        for (i = 0; i < 8; ++i) {
            CommandReplaceItem.field_175785_a.put("slot.villager." + i, Integer.valueOf(300 + i));
        }

        for (i = 0; i < 15; ++i) {
            CommandReplaceItem.field_175785_a.put("slot.horse." + i, Integer.valueOf(500 + i));
        }

        CommandReplaceItem.field_175785_a.put("slot.weapon", Integer.valueOf(98));
        CommandReplaceItem.field_175785_a.put("slot.weapon.mainhand", Integer.valueOf(98));
        CommandReplaceItem.field_175785_a.put("slot.weapon.offhand", Integer.valueOf(99));
        CommandReplaceItem.field_175785_a.put("slot.armor.head", Integer.valueOf(100 + EntityEquipmentSlot.HEAD.func_188454_b()));
        CommandReplaceItem.field_175785_a.put("slot.armor.chest", Integer.valueOf(100 + EntityEquipmentSlot.CHEST.func_188454_b()));
        CommandReplaceItem.field_175785_a.put("slot.armor.legs", Integer.valueOf(100 + EntityEquipmentSlot.LEGS.func_188454_b()));
        CommandReplaceItem.field_175785_a.put("slot.armor.feet", Integer.valueOf(100 + EntityEquipmentSlot.FEET.func_188454_b()));
        CommandReplaceItem.field_175785_a.put("slot.horse.saddle", Integer.valueOf(400));
        CommandReplaceItem.field_175785_a.put("slot.horse.armor", Integer.valueOf(401));
        CommandReplaceItem.field_175785_a.put("slot.horse.chest", Integer.valueOf(499));
    }
}
