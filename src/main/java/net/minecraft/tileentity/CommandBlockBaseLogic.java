package net.minecraft.tileentity;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Nullable;

import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.logging.Level;
import net.minecraft.advancements.FunctionManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.CommandSenderWrapper;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.item.EntityMinecartCommandBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ReportedException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.command.VanillaCommandWrapper;
import org.bukkit.event.server.ServerCommandEvent;

// CraftBukkit start
import java.util.ArrayList;
import org.bukkit.craftbukkit.command.VanillaCommandWrapper;
import com.google.common.base.Joiner;
import java.util.logging.Level;
import org.bukkit.command.CommandSender;
import org.bukkit.event.server.ServerCommandEvent;
// CraftBukkit end

public abstract class CommandBlockBaseLogic implements ICommandSender {

    private static final SimpleDateFormat field_145766_a = new SimpleDateFormat("HH:mm:ss");
    private long field_193041_b = -1L;
    private boolean field_193042_c = true;
    private int field_145764_b;
    private boolean field_145765_c = true;
    private ITextComponent field_145762_d;
    private String field_145763_e = "";
    private String field_145761_f = "@";
    private final CommandResultStats field_175575_g = new CommandResultStats();
    protected org.bukkit.command.CommandSender sender; // CraftBukkit - add sender

    public CommandBlockBaseLogic() {}

    public int func_145760_g() {
        return this.field_145764_b;
    }

    public void func_184167_a(int i) {
        this.field_145764_b = i;
    }

    public ITextComponent func_145749_h() {
        return (ITextComponent) (this.field_145762_d == null ? new TextComponentString("") : this.field_145762_d);
    }

    public NBTTagCompound func_189510_a(NBTTagCompound nbttagcompound) {
        nbttagcompound.func_74778_a("Command", this.field_145763_e);
        nbttagcompound.func_74768_a("SuccessCount", this.field_145764_b);
        nbttagcompound.func_74778_a("CustomName", this.field_145761_f);
        nbttagcompound.func_74757_a("TrackOutput", this.field_145765_c);
        if (this.field_145762_d != null && this.field_145765_c) {
            nbttagcompound.func_74778_a("LastOutput", ITextComponent.Serializer.func_150696_a(this.field_145762_d));
        }

        nbttagcompound.func_74757_a("UpdateLastExecution", this.field_193042_c);
        if (this.field_193042_c && this.field_193041_b > 0L) {
            nbttagcompound.func_74772_a("LastExecution", this.field_193041_b);
        }

        this.field_175575_g.func_179670_b(nbttagcompound);
        return nbttagcompound;
    }

    public void func_145759_b(NBTTagCompound nbttagcompound) {
        this.field_145763_e = nbttagcompound.func_74779_i("Command");
        this.field_145764_b = nbttagcompound.func_74762_e("SuccessCount");
        if (nbttagcompound.func_150297_b("CustomName", 8)) {
            this.field_145761_f = nbttagcompound.func_74779_i("CustomName");
        }

        if (nbttagcompound.func_150297_b("TrackOutput", 1)) {
            this.field_145765_c = nbttagcompound.func_74767_n("TrackOutput");
        }

        if (nbttagcompound.func_150297_b("LastOutput", 8) && this.field_145765_c) {
            try {
                this.field_145762_d = ITextComponent.Serializer.func_150699_a(nbttagcompound.func_74779_i("LastOutput"));
            } catch (Throwable throwable) {
                this.field_145762_d = new TextComponentString(throwable.getMessage());
            }
        } else {
            this.field_145762_d = null;
        }

        if (nbttagcompound.func_74764_b("UpdateLastExecution")) {
            this.field_193042_c = nbttagcompound.func_74767_n("UpdateLastExecution");
        }

        if (this.field_193042_c && nbttagcompound.func_74764_b("LastExecution")) {
            this.field_193041_b = nbttagcompound.func_74763_f("LastExecution");
        } else {
            this.field_193041_b = -1L;
        }

        this.field_175575_g.func_179668_a(nbttagcompound);
    }

    public boolean func_70003_b(int i, String s) {
        return i <= 2;
    }

    public void func_145752_a(String s) {
        this.field_145763_e = s;
        this.field_145764_b = 0;
    }

    public String func_145753_i() {
        return this.field_145763_e;
    }

    public boolean func_145755_a(World world) {
        if (!world.field_72995_K && world.func_82737_E() != this.field_193041_b) {
            if ("Searge".equalsIgnoreCase(this.field_145763_e)) {
                this.field_145762_d = new TextComponentString("#itzlipofutzli");
                this.field_145764_b = 1;
                return true;
            } else {
                MinecraftServer minecraftserver = this.func_184102_h();

                if (minecraftserver != null && minecraftserver.func_175578_N() && minecraftserver.func_82356_Z()) {
                    try {
                        this.field_145762_d = null;
                        // CraftBukkit start - Handle command block commands using Bukkit dispatcher
                        this.field_145764_b = executeSafely(this, sender, this.field_145763_e);
                        // CraftBukkit end
                    } catch (Throwable throwable) {
                        CrashReport crashreport = CrashReport.func_85055_a(throwable, "Executing command block");
                        CrashReportCategory crashreportsystemdetails = crashreport.func_85058_a("Command to be executed");

                        crashreportsystemdetails.func_189529_a("Command", new ICrashReportDetail() {
                            public String a() throws Exception {
                                return CommandBlockBaseLogic.this.func_145753_i();
                            }

                            public Object call() throws Exception {
                                return this.a();
                            }
                        });
                        crashreportsystemdetails.func_189529_a("Name", new ICrashReportDetail() {
                            public String a() throws Exception {
                                return CommandBlockBaseLogic.this.func_70005_c_();
                            }

                            public Object call() throws Exception {
                                return this.a();
                            }
                        });
                        throw new ReportedException(crashreport);
                    }
                } else {
                    this.field_145764_b = 0;
                }

                if (this.field_193042_c) {
                    this.field_193041_b = world.func_82737_E();
                } else {
                    this.field_193041_b = -1L;
                }

                return true;
            }
        } else {
            return false;
        }
    }

    public static int executeSafely(ICommandSender sender, org.bukkit.command.CommandSender bSender, String command) {
        try {
            return executeCommand(sender, bSender, command);
        } catch (CommandException commandexception) {
            // Taken from CommandHandler
            TextComponentTranslation chatmessage = new TextComponentTranslation(commandexception.getMessage(), commandexception.func_74844_a());
            chatmessage.func_150256_b().func_150238_a(TextFormatting.RED);
            sender.func_145747_a(chatmessage);
        }

        return 0;
    }

    // CraftBukkit start
    public static int executeCommand(ICommandSender sender, org.bukkit.command.CommandSender bSender, String command) throws CommandException {
        org.bukkit.command.SimpleCommandMap commandMap = sender.func_130014_f_().getServer().getCommandMap();
        Joiner joiner = Joiner.on(" ");
        if (command.startsWith("/")) {
            command = command.substring(1);
        }

        ServerCommandEvent event = new ServerCommandEvent(bSender, command);
        org.bukkit.Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return 0;
        }
        command = event.getCommand();

        String[] args = command.split(" ");
        ArrayList<String[]> commands = new ArrayList<String[]>();

        String cmd = args[0];
        if (cmd.startsWith("minecraft:")) cmd = cmd.substring("minecraft:".length());
        if (cmd.startsWith("bukkit:")) cmd = cmd.substring("bukkit:".length());

        // Block disallowed commands
        if (cmd.equalsIgnoreCase("stop") || cmd.equalsIgnoreCase("kick") || cmd.equalsIgnoreCase("op")
                || cmd.equalsIgnoreCase("deop") || cmd.equalsIgnoreCase("ban") || cmd.equalsIgnoreCase("ban-ip")
                || cmd.equalsIgnoreCase("pardon") || cmd.equalsIgnoreCase("pardon-ip") || cmd.equalsIgnoreCase("reload")) {
            return 0;
        }

        // Handle vanilla commands;
        org.bukkit.command.Command commandBlockCommand = commandMap.getCommand(args[0]);
        if (sender.func_130014_f_().getServer().getCommandBlockOverride(args[0])) {
            commandBlockCommand = commandMap.getCommand("minecraft:" + args[0]);
        }
        if (commandBlockCommand instanceof VanillaCommandWrapper) {
            command = command.trim();
            if (command.startsWith("/")) {
                command = command.substring(1);
            }
            String as[] = command.split(" ");
            as = VanillaCommandWrapper.dropFirstArgument(as);
            if (!sender.func_130014_f_().getServer().getPermissionOverride(sender) && !((VanillaCommandWrapper) commandBlockCommand).testPermission(bSender)) {
                return 0;
            }
            return ((VanillaCommandWrapper) commandBlockCommand).dispatchVanillaCommand(bSender, sender, as);
        }

        // Make sure this is a valid command
        if (commandMap.getCommand(args[0]) == null) {
            return 0;
        }

        commands.add(args);

        // Find positions of command block syntax, if any        
        WorldServer[] prev = MinecraftServer.getServer().field_71305_c;
        MinecraftServer server = MinecraftServer.getServer();
        server.field_71305_c = new WorldServer[server.worlds.size()];
        server.field_71305_c[0] = (WorldServer) sender.func_130014_f_();
        int bpos = 0;
        for (int pos = 1; pos < server.field_71305_c.length; pos++) {
            WorldServer world = server.worlds.get(bpos++);
            if (server.field_71305_c[0] == world) {
                pos--;
                continue;
            }
            server.field_71305_c[pos] = world;
        }
        try {
            ArrayList<String[]> newCommands = new ArrayList<String[]>();
            for (int i = 0; i < args.length; i++) {
                if (EntitySelector.func_82378_b(args[i])) {
                    for (int j = 0; j < commands.size(); j++) {
                        newCommands.addAll(buildCommands(sender, commands.get(j), i));
                    }
                    ArrayList<String[]> temp = commands;
                    commands = newCommands;
                    newCommands = temp;
                    newCommands.clear();
                }
            }
        } finally {
            MinecraftServer.getServer().field_71305_c = prev;
        }

        int completed = 0;

        // Now dispatch all of the commands we ended up with
        for (int i = 0; i < commands.size(); i++) {
            try {
                if (commandMap.dispatch(bSender, joiner.join(java.util.Arrays.asList(commands.get(i))))) {
                    completed++;
                }
            } catch (Throwable exception) {
                if (sender.func_174793_f() instanceof EntityMinecartCommandBlock) {
                    MinecraftServer.getServer().server.getLogger().log(Level.WARNING, String.format("MinecartCommandBlock at (%d,%d,%d) failed to handle command", sender.func_180425_c().func_177958_n(), sender.func_180425_c().func_177956_o(), sender.func_180425_c().func_177952_p()), exception);
                } else if (sender instanceof CommandBlockBaseLogic) {
                    CommandBlockBaseLogic listener = (CommandBlockBaseLogic) sender;
                    MinecraftServer.getServer().server.getLogger().log(Level.WARNING, String.format("CommandBlock at (%d,%d,%d) failed to handle command", listener.func_180425_c().func_177958_n(), listener.func_180425_c().func_177956_o(), listener.func_180425_c().func_177952_p()), exception);
                } else {
                    MinecraftServer.getServer().server.getLogger().log(Level.WARNING, String.format("Unknown CommandBlock failed to handle command"), exception);
                }
            }
        }

        return completed;
    }

    private static ArrayList<String[]> buildCommands(ICommandSender sender, String[] args, int pos) throws CommandException {
        ArrayList<String[]> commands = new ArrayList<String[]>();
        java.util.List<EntityPlayerMP> players = (java.util.List<EntityPlayerMP>)EntitySelector.func_179656_b(sender, args[pos], EntityPlayerMP.class);

        if (players != null) {
            for (EntityPlayerMP player : players) {
                if (player.field_70170_p != sender.func_130014_f_()) {
                    continue;
                }
                String[] command = args.clone();
                command[pos] = player.func_70005_c_();
                commands.add(command);
            }
        }

        return commands;
    }

    public static CommandSender unwrapSender(ICommandSender listener) {
        org.bukkit.command.CommandSender sender = null;
        while (sender == null) {
            if (listener instanceof DedicatedServer) {
                sender = ((DedicatedServer) listener).console;
            } else if (listener instanceof RConConsoleSource) {
                sender = ((RConConsoleSource) listener).func_184102_h().remoteConsole;
            } else if (listener instanceof CommandBlockBaseLogic) {
                sender = ((CommandBlockBaseLogic) listener).sender;
            } else if (listener instanceof CustomFunctionData.CustomFunctionListener) {
                sender = ((CustomFunctionData.CustomFunctionListener) listener).sender;
            } else if (listener instanceof CommandSenderWrapper) {
                listener = ((CommandSenderWrapper) listener).field_193043_a; // Search deeper
            } else if (VanillaCommandWrapper.lastSender != null) {
                sender = VanillaCommandWrapper.lastSender;
            } else if (listener.func_174793_f() != null) {
                sender = listener.func_174793_f().getBukkitEntity();
            } else {
                throw new RuntimeException("Unhandled executor " + listener.getClass().getSimpleName());
            }
        }

        return sender;
    }
    // CraftBukkit end

    public String func_70005_c_() {
        return this.field_145761_f;
    }

    public void func_145754_b(String s) {
        this.field_145761_f = s;
    }

    public void func_145747_a(ITextComponent ichatbasecomponent) {
        if (this.field_145765_c && this.func_130014_f_() != null && !this.func_130014_f_().field_72995_K) {
            this.field_145762_d = (new TextComponentString("[" + CommandBlockBaseLogic.field_145766_a.format(new Date()) + "] ")).func_150257_a(ichatbasecomponent);
            this.func_145756_e();
        }

    }

    public boolean func_174792_t_() {
        MinecraftServer minecraftserver = this.func_184102_h();

        return minecraftserver == null || !minecraftserver.func_175578_N() || minecraftserver.field_71305_c[0].func_82736_K().func_82766_b("commandBlockOutput");
    }

    public void func_174794_a(CommandResultStats.Type commandobjectiveexecutor_enumcommandresult, int i) {
        this.field_175575_g.func_184932_a(this.func_184102_h(), this, commandobjectiveexecutor_enumcommandresult, i);
    }

    public abstract void func_145756_e();

    public void func_145750_b(@Nullable ITextComponent ichatbasecomponent) {
        this.field_145762_d = ichatbasecomponent;
    }

    public void func_175573_a(boolean flag) {
        this.field_145765_c = flag;
    }

    public boolean func_175571_m() {
        return this.field_145765_c;
    }

    public boolean func_175574_a(EntityPlayer entityhuman) {
        if (!entityhuman.func_189808_dh()) {
            return false;
        } else {
            if (entityhuman.func_130014_f_().field_72995_K) {
                entityhuman.func_184809_a(this);
            }

            return true;
        }
    }

    public CommandResultStats func_175572_n() {
        return this.field_175575_g;
    }
}
