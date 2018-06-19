package net.minecraft.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkProviderServer;

public class CommandSpreadPlayers extends CommandBase {

    public CommandSpreadPlayers() {}

    public String func_71517_b() {
        return "spreadplayers";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.spreadplayers.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 6) {
            throw new WrongUsageException("commands.spreadplayers.usage", new Object[0]);
        } else {
            byte b0 = 0;
            BlockPos blockposition = icommandlistener.func_180425_c();
            double d0 = (double) blockposition.func_177958_n();
            int i = b0 + 1;
            double d1 = func_175761_b(d0, astring[b0], true);
            double d2 = func_175761_b((double) blockposition.func_177952_p(), astring[i++], true);
            double d3 = func_180526_a(astring[i++], 0.0D);
            double d4 = func_180526_a(astring[i++], d3 + 1.0D);
            boolean flag = func_180527_d(astring[i++]);
            ArrayList arraylist = Lists.newArrayList();

            while (i < astring.length) {
                String s = astring[i++];

                if (EntitySelector.func_82378_b(s)) {
                    List list = EntitySelector.func_179656_b(icommandlistener, s, Entity.class);

                    if (list.isEmpty()) {
                        throw new EntityNotFoundException("commands.generic.selector.notFound", new Object[] { s});
                    }

                    arraylist.addAll(list);
                } else {
                    EntityPlayerMP entityplayer = minecraftserver.func_184103_al().func_152612_a(s);

                    if (entityplayer == null) {
                        throw new PlayerNotFoundException("commands.generic.player.notFound", new Object[] { s});
                    }

                    arraylist.add(entityplayer);
                }
            }

            icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_ENTITIES, arraylist.size());
            if (arraylist.isEmpty()) {
                throw new EntityNotFoundException("commands.spreadplayers.noop");
            } else {
                icommandlistener.func_145747_a(new TextComponentTranslation("commands.spreadplayers.spreading." + (flag ? "teams" : "players"), new Object[] { Integer.valueOf(arraylist.size()), Double.valueOf(d4), Double.valueOf(d1), Double.valueOf(d2), Double.valueOf(d3)}));
                this.func_110669_a(icommandlistener, arraylist, new CommandSpreadPlayers.Position(d1, d2), d3, d4, ((Entity) arraylist.get(0)).field_70170_p, flag);
            }
        }
    }

    private void func_110669_a(ICommandSender icommandlistener, List<Entity> list, CommandSpreadPlayers.Position commandspreadplayers_location2d, double d0, double d1, World world, boolean flag) throws CommandException {
        Random random = new Random();
        double d2 = commandspreadplayers_location2d.field_111101_a - d1;
        double d3 = commandspreadplayers_location2d.field_111100_b - d1;
        double d4 = commandspreadplayers_location2d.field_111101_a + d1;
        double d5 = commandspreadplayers_location2d.field_111100_b + d1;
        CommandSpreadPlayers.Position[] acommandspreadplayers_location2d = this.func_110670_a(random, flag ? this.func_110667_a(list) : list.size(), d2, d3, d4, d5);
        int i = this.func_110668_a(commandspreadplayers_location2d, d0, world, random, d2, d3, d4, d5, acommandspreadplayers_location2d, flag);
        double d6 = this.func_110671_a(list, world, acommandspreadplayers_location2d, flag);

        func_152373_a(icommandlistener, (ICommand) this, "commands.spreadplayers.success." + (flag ? "teams" : "players"), new Object[] { Integer.valueOf(acommandspreadplayers_location2d.length), Double.valueOf(commandspreadplayers_location2d.field_111101_a), Double.valueOf(commandspreadplayers_location2d.field_111100_b)});
        if (acommandspreadplayers_location2d.length > 1) {
            icommandlistener.func_145747_a(new TextComponentTranslation("commands.spreadplayers.info." + (flag ? "teams" : "players"), new Object[] { String.format("%.2f", new Object[] { Double.valueOf(d6)}), Integer.valueOf(i)}));
        }

    }

    private int func_110667_a(List<Entity> list) {
        HashSet hashset = Sets.newHashSet();
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();

            if (entity instanceof EntityPlayer) {
                hashset.add(entity.func_96124_cp());
            } else {
                hashset.add((Object) null);
            }
        }

        return hashset.size();
    }

    private int func_110668_a(CommandSpreadPlayers.Position commandspreadplayers_location2d, double d0, World world, Random random, double d1, double d2, double d3, double d4, CommandSpreadPlayers.Position[] acommandspreadplayers_location2d, boolean flag) throws CommandException {
        boolean flag1 = true;
        double d5 = 3.4028234663852886E38D;

        int i;

        for (i = 0; i < 10000 && flag1; ++i) {
            flag1 = false;
            d5 = 3.4028234663852886E38D;

            int j;
            CommandSpreadPlayers.Position commandspreadplayers_location2d1;

            for (int k = 0; k < acommandspreadplayers_location2d.length; ++k) {
                CommandSpreadPlayers.Position commandspreadplayers_location2d2 = acommandspreadplayers_location2d[k];

                j = 0;
                commandspreadplayers_location2d1 = new CommandSpreadPlayers.Position();

                for (int l = 0; l < acommandspreadplayers_location2d.length; ++l) {
                    if (k != l) {
                        CommandSpreadPlayers.Position commandspreadplayers_location2d3 = acommandspreadplayers_location2d[l];
                        double d6 = commandspreadplayers_location2d2.func_111099_a(commandspreadplayers_location2d3);

                        d5 = Math.min(d6, d5);
                        if (d6 < d0) {
                            ++j;
                            commandspreadplayers_location2d1.field_111101_a += commandspreadplayers_location2d3.field_111101_a - commandspreadplayers_location2d2.field_111101_a;
                            commandspreadplayers_location2d1.field_111100_b += commandspreadplayers_location2d3.field_111100_b - commandspreadplayers_location2d2.field_111100_b;
                        }
                    }
                }

                if (j > 0) {
                    commandspreadplayers_location2d1.field_111101_a /= (double) j;
                    commandspreadplayers_location2d1.field_111100_b /= (double) j;
                    double d7 = (double) commandspreadplayers_location2d1.func_111096_b();

                    if (d7 > 0.0D) {
                        commandspreadplayers_location2d1.func_111095_a();
                        commandspreadplayers_location2d2.func_111094_b(commandspreadplayers_location2d1);
                    } else {
                        commandspreadplayers_location2d2.func_111097_a(random, d1, d2, d3, d4);
                    }

                    flag1 = true;
                }

                if (commandspreadplayers_location2d2.func_111093_a(d1, d2, d3, d4)) {
                    flag1 = true;
                }
            }

            if (!flag1) {
                CommandSpreadPlayers.Position[] acommandspreadplayers_location2d1 = acommandspreadplayers_location2d;
                int i1 = acommandspreadplayers_location2d.length;

                for (j = 0; j < i1; ++j) {
                    commandspreadplayers_location2d1 = acommandspreadplayers_location2d1[j];
                    if (!commandspreadplayers_location2d1.func_111098_b(world)) {
                        commandspreadplayers_location2d1.func_111097_a(random, d1, d2, d3, d4);
                        flag1 = true;
                    }
                }
            }
        }

        if (i >= 10000) {
            throw new CommandException("commands.spreadplayers.failure." + (flag ? "teams" : "players"), new Object[] { Integer.valueOf(acommandspreadplayers_location2d.length), Double.valueOf(commandspreadplayers_location2d.field_111101_a), Double.valueOf(commandspreadplayers_location2d.field_111100_b), String.format("%.2f", new Object[] { Double.valueOf(d5)})});
        } else {
            return i;
        }
    }

    private double func_110671_a(List<Entity> list, World world, CommandSpreadPlayers.Position[] acommandspreadplayers_location2d, boolean flag) {
        double d0 = 0.0D;
        int i = 0;
        HashMap hashmap = Maps.newHashMap();

        for (int j = 0; j < list.size(); ++j) {
            Entity entity = (Entity) list.get(j);
            CommandSpreadPlayers.Position commandspreadplayers_location2d;

            if (flag) {
                Team scoreboardteambase = entity instanceof EntityPlayer ? entity.func_96124_cp() : null;

                if (!hashmap.containsKey(scoreboardteambase)) {
                    hashmap.put(scoreboardteambase, acommandspreadplayers_location2d[i++]);
                }

                commandspreadplayers_location2d = (CommandSpreadPlayers.Position) hashmap.get(scoreboardteambase);
            } else {
                commandspreadplayers_location2d = acommandspreadplayers_location2d[i++];
            }

            entity.func_70634_a((double) ((float) MathHelper.func_76128_c(commandspreadplayers_location2d.field_111101_a) + 0.5F), (double) commandspreadplayers_location2d.func_111092_a(world), (double) MathHelper.func_76128_c(commandspreadplayers_location2d.field_111100_b) + 0.5D);
            double d1 = Double.MAX_VALUE;
            CommandSpreadPlayers.Position[] acommandspreadplayers_location2d1 = acommandspreadplayers_location2d;
            int k = acommandspreadplayers_location2d.length;

            for (int l = 0; l < k; ++l) {
                CommandSpreadPlayers.Position commandspreadplayers_location2d1 = acommandspreadplayers_location2d1[l];

                if (commandspreadplayers_location2d != commandspreadplayers_location2d1) {
                    double d2 = commandspreadplayers_location2d.func_111099_a(commandspreadplayers_location2d1);

                    d1 = Math.min(d2, d1);
                }
            }

            d0 += d1;
        }

        d0 /= (double) list.size();
        return d0;
    }

    private CommandSpreadPlayers.Position[] func_110670_a(Random random, int i, double d0, double d1, double d2, double d3) {
        CommandSpreadPlayers.Position[] acommandspreadplayers_location2d = new CommandSpreadPlayers.Position[i];

        for (int j = 0; j < acommandspreadplayers_location2d.length; ++j) {
            CommandSpreadPlayers.Position commandspreadplayers_location2d = new CommandSpreadPlayers.Position();

            commandspreadplayers_location2d.func_111097_a(random, d0, d1, d2, d3);
            acommandspreadplayers_location2d[j] = commandspreadplayers_location2d;
        }

        return acommandspreadplayers_location2d;
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length >= 1 && astring.length <= 2 ? func_181043_b(astring, 0, blockposition) : Collections.<String>emptyList(); // CraftBukkit - decompile error
    }

    // CraftBukkit start - fix decompile error
    @Override
    public int compareTo(ICommand o) {
        return compareTo(o);
    }
    // CraftBukkit end

    static class Position {

        double field_111101_a;
        double field_111100_b;

        Position() {}

        Position(double d0, double d1) {
            this.field_111101_a = d0;
            this.field_111100_b = d1;
        }

        double func_111099_a(CommandSpreadPlayers.Position commandspreadplayers_location2d) {
            double d0 = this.field_111101_a - commandspreadplayers_location2d.field_111101_a;
            double d1 = this.field_111100_b - commandspreadplayers_location2d.field_111100_b;

            return Math.sqrt(d0 * d0 + d1 * d1);
        }

        void func_111095_a() {
            double d0 = (double) this.func_111096_b();

            this.field_111101_a /= d0;
            this.field_111100_b /= d0;
        }

        float func_111096_b() {
            return MathHelper.func_76133_a(this.field_111101_a * this.field_111101_a + this.field_111100_b * this.field_111100_b);
        }

        public void func_111094_b(CommandSpreadPlayers.Position commandspreadplayers_location2d) {
            this.field_111101_a -= commandspreadplayers_location2d.field_111101_a;
            this.field_111100_b -= commandspreadplayers_location2d.field_111100_b;
        }

        public boolean func_111093_a(double d0, double d1, double d2, double d3) {
            boolean flag = false;

            if (this.field_111101_a < d0) {
                this.field_111101_a = d0;
                flag = true;
            } else if (this.field_111101_a > d2) {
                this.field_111101_a = d2;
                flag = true;
            }

            if (this.field_111100_b < d1) {
                this.field_111100_b = d1;
                flag = true;
            } else if (this.field_111100_b > d3) {
                this.field_111100_b = d3;
                flag = true;
            }

            return flag;
        }

        public int func_111092_a(World world) {
            BlockPos blockposition = new BlockPos(this.field_111101_a, 256.0D, this.field_111100_b);

            do {
                if (blockposition.func_177956_o() <= 0) {
                    return 257;
                }

                blockposition = blockposition.func_177977_b();
            } while (getType(world, blockposition).func_185904_a() == Material.field_151579_a); // CraftBukkit

            return blockposition.func_177956_o() + 1;
        }

        public boolean func_111098_b(World world) {
            BlockPos blockposition = new BlockPos(this.field_111101_a, 256.0D, this.field_111100_b);

            Material material;

            do {
                if (blockposition.func_177956_o() <= 0) {
                    return false;
                }

                blockposition = blockposition.func_177977_b();
                material = getType(world, blockposition).func_185904_a(); // CraftBukkit
            } while (material == Material.field_151579_a);

            return !material.func_76224_d() && material != Material.field_151581_o;
        }

        public void func_111097_a(Random random, double d0, double d1, double d2, double d3) {
            this.field_111101_a = MathHelper.func_82716_a(random, d0, d2);
            this.field_111100_b = MathHelper.func_82716_a(random, d1, d3);
        }

        // CraftBukkit start - add a version of getType which force loads chunks
        private static IBlockState getType(World world, BlockPos position) {
            ((ChunkProviderServer) world.field_73020_y).func_186025_d(position.func_177958_n() >> 4, position.func_177952_p() >> 4);
            return world.func_180495_p(position);
        }
        // CraftBukkit end
    }
}
