package net.minecraft.tileentity;

import javax.annotation.Nullable;

import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TileEntitySign.ISignCommandListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.World;

public class TileEntitySign extends TileEntity {

    public final ITextComponent[] field_145915_a = new ITextComponent[] { new TextComponentString(""), new TextComponentString(""), new TextComponentString(""), new TextComponentString("")};
    public int field_145918_i = -1;
    public boolean field_145916_j = true;
    private EntityPlayer field_145917_k;
    private final CommandResultStats field_174883_i = new CommandResultStats();

    public TileEntitySign() {}

    public NBTTagCompound func_189515_b(NBTTagCompound nbttagcompound) {
        super.func_189515_b(nbttagcompound);

        for (int i = 0; i < 4; ++i) {
            String s = ITextComponent.Serializer.func_150696_a(this.field_145915_a[i]);

            nbttagcompound.func_74778_a("Text" + (i + 1), s);
        }

        // CraftBukkit start
        if (Boolean.getBoolean("convertLegacySigns")) {
            nbttagcompound.func_74757_a("Bukkit.isConverted", true);
        }
        // CraftBukkit end

        this.field_174883_i.func_179670_b(nbttagcompound);
        return nbttagcompound;
    }

    protected void func_190201_b(World world) {
        this.func_145834_a(world);
    }

    public void func_145839_a(NBTTagCompound nbttagcompound) {
        this.field_145916_j = false;
        super.func_145839_a(nbttagcompound);
        ICommandSender icommandlistener = new ISignCommandListener() { // Paper
            public String func_70005_c_() {
                return "Sign";
            }

            public boolean func_70003_b(int i, String s) {
                return true;
            }

            public BlockPos func_180425_c() {
                return TileEntitySign.this.field_174879_c;
            }

            public Vec3d func_174791_d() {
                return new Vec3d((double) TileEntitySign.this.field_174879_c.func_177958_n() + 0.5D, (double) TileEntitySign.this.field_174879_c.func_177956_o() + 0.5D, (double) TileEntitySign.this.field_174879_c.func_177952_p() + 0.5D);
            }

            public World func_130014_f_() {
                return TileEntitySign.this.field_145850_b;
            }

            public MinecraftServer func_184102_h() {
                return MinecraftServer.getServer(); // Paper - world may be null
            }
        };

        // CraftBukkit start - Add an option to convert signs correctly
        // This is done with a flag instead of all the time because
        // we have no way to tell whether a sign is from 1.7.10 or 1.8

        boolean oldSign = Boolean.getBoolean("convertLegacySigns") && !nbttagcompound.func_74767_n("Bukkit.isConverted");

        for (int i = 0; i < 4; ++i) {
            String s = nbttagcompound.func_74779_i("Text" + (i + 1));
            if (s != null && s.length() > 2048) {
                s = "\"\"";
            }

            try {
                //IChatBaseComponent ichatbasecomponent = IChatBaseComponent.ChatSerializer.a(s); // Paper - move down - the old format might throw a json error

                if (oldSign && !isLoadingStructure) { // Paper - saved structures will be in the new format, but will not have isConverted
                    field_145915_a[i] = org.bukkit.craftbukkit.util.CraftChatMessage.fromString(s)[0];
                    continue;
                }
                // CraftBukkit end
                ITextComponent ichatbasecomponent = ITextComponent.Serializer.func_150699_a(s); // Paper - after old sign

                try {
                    this.field_145915_a[i] = TextComponentUtils.func_179985_a(icommandlistener, ichatbasecomponent, (Entity) null);
                } catch (CommandException commandexception) {
                    this.field_145915_a[i] = ichatbasecomponent;
                }
            } catch (com.google.gson.JsonParseException jsonparseexception) {
                this.field_145915_a[i] = new TextComponentString(s);
            }
        }

        this.field_174883_i.func_179668_a(nbttagcompound);
    }

    @Nullable
    public SPacketUpdateTileEntity func_189518_D_() {
        return new SPacketUpdateTileEntity(this.field_174879_c, 9, this.func_189517_E_());
    }

    public NBTTagCompound func_189517_E_() {
        return this.func_189515_b(new NBTTagCompound());
    }

    public boolean func_183000_F() {
        return true;
    }

    public boolean func_145914_a() {
        return this.field_145916_j;
    }

    public void func_145912_a(EntityPlayer entityhuman) {
        this.field_145917_k = entityhuman;
    }

    public EntityPlayer func_145911_b() {
        return this.field_145917_k;
    }

    public boolean func_174882_b(final EntityPlayer entityhuman) {
        ICommandSender icommandlistener = new ISignCommandListener() { // Paper
            public String func_70005_c_() {
                return entityhuman.func_70005_c_();
            }

            public ITextComponent func_145748_c_() {
                return entityhuman.func_145748_c_();
            }

            public void func_145747_a(ITextComponent ichatbasecomponent) {}

            public boolean func_70003_b(int i, String s) {
                return i <= 2;
            }

            public BlockPos func_180425_c() {
                return TileEntitySign.this.field_174879_c;
            }

            public Vec3d func_174791_d() {
                return new Vec3d((double) TileEntitySign.this.field_174879_c.func_177958_n() + 0.5D, (double) TileEntitySign.this.field_174879_c.func_177956_o() + 0.5D, (double) TileEntitySign.this.field_174879_c.func_177952_p() + 0.5D);
            }

            public World func_130014_f_() {
                return entityhuman.func_130014_f_();
            }

            public Entity func_174793_f() {
                return entityhuman;
            }

            public boolean func_174792_t_() {
                return false;
            }

            public void func_174794_a(CommandResultStats.Type commandobjectiveexecutor_enumcommandresult, int i) {
                if (TileEntitySign.this.field_145850_b != null && !TileEntitySign.this.field_145850_b.field_72995_K) {
                    TileEntitySign.this.field_174883_i.func_184932_a(TileEntitySign.this.field_145850_b.func_73046_m(), this, commandobjectiveexecutor_enumcommandresult, i);
                }

            }

            public MinecraftServer func_184102_h() {
                return entityhuman.func_184102_h();
            }
        };
        ITextComponent[] aichatbasecomponent = this.field_145915_a;
        int i = aichatbasecomponent.length;

        for (int j = 0; j < i; ++j) {
            ITextComponent ichatbasecomponent = aichatbasecomponent[j];
            Style chatmodifier = ichatbasecomponent == null ? null : ichatbasecomponent.func_150256_b();

            if (chatmodifier != null && chatmodifier.func_150235_h() != null) {
                ClickEvent chatclickable = chatmodifier.func_150235_h();

                if (chatclickable.func_150669_a() == ClickEvent.Action.RUN_COMMAND) {
                    // CraftBukkit start
                    // entityhuman.C_().getCommandHandler().a(icommandlistener, chatclickable.b());
                    CommandBlockBaseLogic.executeSafely(icommandlistener, new org.bukkit.craftbukkit.command.ProxiedNativeCommandSender(
                            icommandlistener,
                            new org.bukkit.craftbukkit.command.CraftBlockCommandSender(icommandlistener),
                            entityhuman.getBukkitEntity()
                    ), chatclickable.func_150668_b());
                    // CraftBukkit end
                }
            }
        }

        return true;
    }

    public CommandResultStats func_174880_d() {
        return this.field_174883_i;
    }
    public interface ISignCommandListener extends ICommandSender {} // Paper
}
