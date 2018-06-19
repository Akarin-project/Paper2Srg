package net.minecraft.command;

import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Doubles;
import com.google.gson.JsonParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import org.apache.commons.lang3.exception.ExceptionUtils;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public abstract class CommandBase implements ICommand {

    private static ICommandListener field_71533_a;
    private static final Splitter field_190796_b = Splitter.on(',');
    private static final Splitter field_190797_c = Splitter.on('=').limit(2);

    public CommandBase() {}

    protected static SyntaxErrorException func_184889_a(JsonParseException jsonparseexception) {
        Throwable throwable = ExceptionUtils.getRootCause(jsonparseexception);
        String s = "";

        if (throwable != null) {
            s = throwable.getMessage();
            if (s.contains("setLenient")) {
                s = s.substring(s.indexOf("to accept ") + 10);
            }
        }

        return new SyntaxErrorException("commands.tellraw.jsonException", new Object[] { s});
    }

    public static NBTTagCompound func_184887_a(Entity entity) {
        NBTTagCompound nbttagcompound = entity.func_189511_e(new NBTTagCompound());

        if (entity instanceof EntityPlayer) {
            ItemStack itemstack = ((EntityPlayer) entity).field_71071_by.func_70448_g();

            if (!itemstack.func_190926_b()) {
                nbttagcompound.func_74782_a("SelectedItem", itemstack.func_77955_b(new NBTTagCompound()));
            }
        }

        return nbttagcompound;
    }

    public int func_82362_a() {
        return 4;
    }

    public List<String> func_71514_a() {
        return Collections.emptyList();
    }

    public boolean func_184882_a(MinecraftServer minecraftserver, ICommandSender icommandlistener) {
        return icommandlistener.func_70003_b(this.func_82362_a(), this.func_71517_b());
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return Collections.emptyList();
    }

    public static int func_175755_a(String s) throws NumberInvalidException {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException numberformatexception) {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { s});
        }
    }

    public static int func_180528_a(String s, int i) throws NumberInvalidException {
        return func_175764_a(s, i, Integer.MAX_VALUE);
    }

    public static int func_175764_a(String s, int i, int j) throws NumberInvalidException {
        int k = func_175755_a(s);

        if (k < i) {
            throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] { Integer.valueOf(k), Integer.valueOf(i)});
        } else if (k > j) {
            throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] { Integer.valueOf(k), Integer.valueOf(j)});
        } else {
            return k;
        }
    }

    public static long func_175766_b(String s) throws NumberInvalidException {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException numberformatexception) {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { s});
        }
    }

    public static long func_175760_a(String s, long i, long j) throws NumberInvalidException {
        long k = func_175766_b(s);

        if (k < i) {
            throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] { Long.valueOf(k), Long.valueOf(i)});
        } else if (k > j) {
            throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] { Long.valueOf(k), Long.valueOf(j)});
        } else {
            return k;
        }
    }

    public static BlockPos func_175757_a(ICommandSender icommandlistener, String[] astring, int i, boolean flag) throws NumberInvalidException {
        BlockPos blockposition = icommandlistener.func_180425_c();

        return new BlockPos(func_175769_b((double) blockposition.func_177958_n(), astring[i], -30000000, 30000000, flag), func_175769_b((double) blockposition.func_177956_o(), astring[i + 1], 0, 256, false), func_175769_b((double) blockposition.func_177952_p(), astring[i + 2], -30000000, 30000000, flag));
    }

    public static double func_175765_c(String s) throws NumberInvalidException {
        try {
            double d0 = Double.parseDouble(s);

            if (!Doubles.isFinite(d0)) {
                throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { s});
            } else {
                return d0;
            }
        } catch (NumberFormatException numberformatexception) {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { s});
        }
    }

    public static double func_180526_a(String s, double d0) throws NumberInvalidException {
        return func_175756_a(s, d0, Double.MAX_VALUE);
    }

    public static double func_175756_a(String s, double d0, double d1) throws NumberInvalidException {
        double d2 = func_175765_c(s);

        if (d2 < d0) {
            throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] { String.format("%.2f", new Object[] { Double.valueOf(d2)}), String.format("%.2f", new Object[] { Double.valueOf(d0)})});
        } else if (d2 > d1) {
            throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] { String.format("%.2f", new Object[] { Double.valueOf(d2)}), String.format("%.2f", new Object[] { Double.valueOf(d1)})});
        } else {
            return d2;
        }
    }

    public static boolean func_180527_d(String s) throws CommandException {
        if (!"true".equals(s) && !"1".equals(s)) {
            if (!"false".equals(s) && !"0".equals(s)) {
                throw new CommandException("commands.generic.boolean.invalid", new Object[] { s});
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public static EntityPlayerMP func_71521_c(ICommandSender icommandlistener) throws PlayerNotFoundException {
        if (icommandlistener instanceof EntityPlayerMP) {
            return (EntityPlayerMP) icommandlistener;
        } else {
            throw new PlayerNotFoundException("commands.generic.player.unspecified");
        }
    }

    public static List<EntityPlayerMP> func_193513_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String s) throws CommandException {
        List list = EntitySelector.func_193531_b(icommandlistener, s);

        return (List) (list.isEmpty() ? Lists.newArrayList(new EntityPlayerMP[] { func_193512_a(minecraftserver, (EntityPlayerMP) null, s)}) : list);
    }

    public static EntityPlayerMP func_184888_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String s) throws CommandException {
        return func_193512_a(minecraftserver, EntitySelector.func_82386_a(icommandlistener, s), s);
    }

    private static EntityPlayerMP func_193512_a(MinecraftServer minecraftserver, @Nullable EntityPlayerMP entityplayer, String s) throws CommandException {
        if (entityplayer == null) {
            try {
                entityplayer = minecraftserver.func_184103_al().func_177451_a(UUID.fromString(s));
            } catch (IllegalArgumentException illegalargumentexception) {
                ;
            }
        }

        if (entityplayer == null) {
            entityplayer = minecraftserver.func_184103_al().func_152612_a(s);
        }

        if (entityplayer == null) {
            throw new PlayerNotFoundException("commands.generic.player.notFound", new Object[] { s});
        } else {
            return entityplayer;
        }
    }

    public static Entity func_184885_b(MinecraftServer minecraftserver, ICommandSender icommandlistener, String s) throws CommandException {
        return func_184884_a(minecraftserver, icommandlistener, s, Entity.class);
    }

    public static <T extends Entity> T func_184884_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String s, Class<? extends T> oclass) throws CommandException {
        Object object = EntitySelector.func_179652_a(icommandlistener, s, oclass);

        if (object == null) {
            object = minecraftserver.func_184103_al().func_152612_a(s);
        }

        if (object == null) {
            try {
                UUID uuid = UUID.fromString(s);

                object = minecraftserver.func_175576_a(uuid);
                if (object == null) {
                    object = minecraftserver.func_184103_al().func_177451_a(uuid);
                }
            } catch (IllegalArgumentException illegalargumentexception) {
                if (s.split("-").length == 5) {
                    throw new EntityNotFoundException("commands.generic.entity.invalidUuid", new Object[] { s});
                }
            }
        }

        if (object != null && oclass.isAssignableFrom(object.getClass())) {
            return (T) object; // Paper - fix decompile error
        } else {
            throw new EntityNotFoundException(s);
        }
    }

    public static List<Entity> func_184890_c(MinecraftServer minecraftserver, ICommandSender icommandlistener, String s) throws CommandException {
        return (List) (EntitySelector.func_82378_b(s) ? EntitySelector.func_179656_b(icommandlistener, s, Entity.class) : Lists.newArrayList(new Entity[] { func_184885_b(minecraftserver, icommandlistener, s)}));
    }

    public static String func_184886_d(MinecraftServer minecraftserver, ICommandSender icommandlistener, String s) throws CommandException {
        try {
            return func_184888_a(minecraftserver, icommandlistener, s).func_70005_c_();
        } catch (CommandException commandexception) {
            if (EntitySelector.func_82378_b(s)) {
                throw commandexception;
            } else {
                return s;
            }
        }
    }

    public static String func_184891_e(MinecraftServer minecraftserver, ICommandSender icommandlistener, String s) throws CommandException {
        try {
            return func_184888_a(minecraftserver, icommandlistener, s).func_70005_c_();
        } catch (PlayerNotFoundException exceptionplayernotfound) {
            try {
                return func_184885_b(minecraftserver, icommandlistener, s).func_189512_bd();
            } catch (EntityNotFoundException exceptionentitynotfound) {
                if (EntitySelector.func_82378_b(s)) {
                    throw exceptionentitynotfound;
                } else {
                    return s;
                }
            }
        }
    }

    public static ITextComponent func_147178_a(ICommandSender icommandlistener, String[] astring, int i) throws CommandException {
        return func_147176_a(icommandlistener, astring, i, false);
    }

    public static ITextComponent func_147176_a(ICommandSender icommandlistener, String[] astring, int i, boolean flag) throws CommandException {
        TextComponentString chatcomponenttext = new TextComponentString("");

        for (int j = i; j < astring.length; ++j) {
            if (j > i) {
                chatcomponenttext.func_150258_a(" ");
            }

            Object object = new TextComponentString(astring[j]);

            if (flag) {
                ITextComponent ichatbasecomponent = EntitySelector.func_150869_b(icommandlistener, astring[j]);

                if (ichatbasecomponent == null) {
                    if (EntitySelector.func_82378_b(astring[j])) {
                        throw new PlayerNotFoundException("commands.generic.selector.notFound", new Object[] { astring[j]});
                    }
                } else {
                    object = ichatbasecomponent;
                }
            }

            chatcomponenttext.func_150257_a((ITextComponent) object);
        }

        return chatcomponenttext;
    }

    public static String func_180529_a(String[] astring, int i) {
        StringBuilder stringbuilder = new StringBuilder();

        for (int j = i; j < astring.length; ++j) {
            if (j > i) {
                stringbuilder.append(" ");
            }

            String s = astring[j];

            stringbuilder.append(s);
        }

        return stringbuilder.toString();
    }

    public static CommandBase.CoordinateArg func_175770_a(double d0, String s, boolean flag) throws NumberInvalidException {
        return func_175767_a(d0, s, -30000000, 30000000, flag);
    }

    public static CommandBase.CoordinateArg func_175767_a(double d0, String s, int i, int j, boolean flag) throws NumberInvalidException {
        boolean flag1 = s.startsWith("~");

        if (flag1 && Double.isNaN(d0)) {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { Double.valueOf(d0)});
        } else {
            double d1 = 0.0D;

            if (!flag1 || s.length() > 1) {
                boolean flag2 = s.contains(".");

                if (flag1) {
                    s = s.substring(1);
                }

                d1 += func_175765_c(s);
                if (!flag2 && !flag1 && flag) {
                    d1 += 0.5D;
                }
            }

            double d2 = d1 + (flag1 ? d0 : 0.0D);

            if (i != 0 || j != 0) {
                if (d2 < (double) i) {
                    throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] { String.format("%.2f", new Object[] { Double.valueOf(d2)}), Integer.valueOf(i)});
                }

                if (d2 > (double) j) {
                    throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] { String.format("%.2f", new Object[] { Double.valueOf(d2)}), Integer.valueOf(j)});
                }
            }

            return new CommandBase.CoordinateArg(d2, d1, flag1);
        }
    }

    public static double func_175761_b(double d0, String s, boolean flag) throws NumberInvalidException {
        return func_175769_b(d0, s, -30000000, 30000000, flag);
    }

    public static double func_175769_b(double d0, String s, int i, int j, boolean flag) throws NumberInvalidException {
        boolean flag1 = s.startsWith("~");

        if (flag1 && Double.isNaN(d0)) {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { Double.valueOf(d0)});
        } else {
            double d1 = flag1 ? d0 : 0.0D;

            if (!flag1 || s.length() > 1) {
                boolean flag2 = s.contains(".");

                if (flag1) {
                    s = s.substring(1);
                }

                d1 += func_175765_c(s);
                if (!flag2 && !flag1 && flag) {
                    d1 += 0.5D;
                }
            }

            if (i != 0 || j != 0) {
                if (d1 < (double) i) {
                    throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] { String.format("%.2f", new Object[] { Double.valueOf(d1)}), Integer.valueOf(i)});
                }

                if (d1 > (double) j) {
                    throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] { String.format("%.2f", new Object[] { Double.valueOf(d1)}), Integer.valueOf(j)});
                }
            }

            return d1;
        }
    }

    public static Item func_147179_f(ICommandSender icommandlistener, String s) throws NumberInvalidException {
        ResourceLocation minecraftkey = new ResourceLocation(s);
        Item item = (Item) Item.field_150901_e.func_82594_a(minecraftkey);

        if (item == null) {
            throw new NumberInvalidException("commands.give.item.notFound", new Object[] { minecraftkey});
        } else {
            return item;
        }
    }

    public static Block func_147180_g(ICommandSender icommandlistener, String s) throws NumberInvalidException {
        ResourceLocation minecraftkey = new ResourceLocation(s);

        if (!Block.field_149771_c.func_148741_d(minecraftkey)) {
            throw new NumberInvalidException("commands.give.block.notFound", new Object[] { minecraftkey});
        } else {
            return (Block) Block.field_149771_c.func_82594_a(minecraftkey);
        }
    }

    public static IBlockState func_190794_a(Block block, String s) throws NumberInvalidException, InvalidBlockStateException {
        try {
            int i = Integer.parseInt(s);

            if (i < 0) {
                throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] { Integer.valueOf(i), Integer.valueOf(0)});
            } else if (i > 15) {
                throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] { Integer.valueOf(i), Integer.valueOf(15)});
            } else {
                return block.func_176203_a(Integer.parseInt(s));
            }
        } catch (RuntimeException runtimeexception) {
            try {
                Map map = func_190795_c(block, s);
                IBlockState iblockdata = block.func_176223_P();

                Entry entry;

                for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); iblockdata = func_190793_a(iblockdata, (IProperty) entry.getKey(), (Comparable) entry.getValue())) {
                    entry = (Entry) iterator.next();
                }

                return iblockdata;
            } catch (RuntimeException runtimeexception1) {
                throw new InvalidBlockStateException("commands.generic.blockstate.invalid", new Object[] { s, Block.field_149771_c.func_177774_c(block)});
            }
        }
    }

    private static <T extends Comparable<T>> IBlockState func_190793_a(IBlockState iblockdata, IProperty<T> iblockstate, Comparable<?> comparable) {
        return iblockdata.func_177226_a(iblockstate, (T) comparable); // Paper - fix decompiler error
    }

    public static Predicate<IBlockState> func_190791_b(final Block block, String s) throws InvalidBlockStateException {
        if (!"*".equals(s) && !"-1".equals(s)) {
            try {
                final int i = Integer.parseInt(s);

                return new Predicate() {
                    public boolean a(@Nullable IBlockState iblockdata) {
                        return i == iblockdata.func_177230_c().func_176201_c(iblockdata);
                    }

                    public boolean apply(@Nullable Object object) {
                        return this.a((IBlockState) object);
                    }
                };
            } catch (RuntimeException runtimeexception) {
                final Map map = func_190795_c(block, s);

                return new Predicate() {
                    public boolean a(@Nullable IBlockState iblockdata) {
                        if (iblockdata != null && block == iblockdata.func_177230_c()) {
                            Iterator iterator = map.entrySet().iterator();

                            Entry entry;

                            do {
                                if (!iterator.hasNext()) {
                                    return true;
                                }

                                entry = (Entry) iterator.next();
                            } while (iblockdata.func_177229_b((IProperty) entry.getKey()).equals(entry.getValue()));

                            return false;
                        } else {
                            return false;
                        }
                    }

                    public boolean apply(@Nullable Object object) {
                        return this.a((IBlockState) object);
                    }
                };
            }
        } else {
            return Predicates.alwaysTrue();
        }
    }

    private static Map<IProperty<?>, Comparable<?>> func_190795_c(Block block, String s) throws InvalidBlockStateException {
        HashMap hashmap = Maps.newHashMap();

        if ("default".equals(s)) {
            return block.func_176223_P().func_177228_b();
        } else {
            BlockStateContainer blockstatelist = block.func_176194_O();
            Iterator iterator = CommandBase.field_190796_b.split(s).iterator();

            while (true) {
                if (!iterator.hasNext()) {
                    return hashmap;
                }

                String s1 = (String) iterator.next();
                Iterator iterator1 = CommandBase.field_190797_c.split(s1).iterator();

                if (!iterator1.hasNext()) {
                    break;
                }

                IProperty iblockstate = blockstatelist.func_185920_a((String) iterator1.next());

                if (iblockstate == null || !iterator1.hasNext()) {
                    break;
                }

                Comparable comparable = func_190792_a(iblockstate, (String) iterator1.next());

                if (comparable == null) {
                    break;
                }

                hashmap.put(iblockstate, comparable);
            }

            throw new InvalidBlockStateException("commands.generic.blockstate.invalid", new Object[] { s, Block.field_149771_c.func_177774_c(block)});
        }
    }

    @Nullable
    private static <T extends Comparable<T>> T func_190792_a(IProperty<T> iblockstate, String s) {
        return iblockstate.func_185929_b(s).orNull(); // Paper - fix decompiler error
    }

    public static String func_71527_a(Object[] aobject) {
        StringBuilder stringbuilder = new StringBuilder();

        for (int i = 0; i < aobject.length; ++i) {
            String s = aobject[i].toString();

            if (i > 0) {
                if (i == aobject.length - 1) {
                    stringbuilder.append(" and ");
                } else {
                    stringbuilder.append(", ");
                }
            }

            stringbuilder.append(s);
        }

        return stringbuilder.toString();
    }

    public static ITextComponent func_180530_a(List<ITextComponent> list) {
        TextComponentString chatcomponenttext = new TextComponentString("");

        for (int i = 0; i < list.size(); ++i) {
            if (i > 0) {
                if (i == list.size() - 1) {
                    chatcomponenttext.func_150258_a(" and ");
                } else if (i > 0) {
                    chatcomponenttext.func_150258_a(", ");
                }
            }

            chatcomponenttext.func_150257_a((ITextComponent) list.get(i));
        }

        return chatcomponenttext;
    }

    public static String func_96333_a(Collection<String> collection) {
        return func_71527_a(collection.toArray(new String[collection.size()]));
    }

    public static List<String> func_175771_a(String[] astring, int i, @Nullable BlockPos blockposition) {
        if (blockposition == null) {
            return Lists.newArrayList(new String[] { "~"});
        } else {
            int j = astring.length - 1;
            String s;

            if (j == i) {
                s = Integer.toString(blockposition.func_177958_n());
            } else if (j == i + 1) {
                s = Integer.toString(blockposition.func_177956_o());
            } else {
                if (j != i + 2) {
                    return Collections.emptyList();
                }

                s = Integer.toString(blockposition.func_177952_p());
            }

            return Lists.newArrayList(new String[] { s});
        }
    }

    public static List<String> func_181043_b(String[] astring, int i, @Nullable BlockPos blockposition) {
        if (blockposition == null) {
            return Lists.newArrayList(new String[] { "~"});
        } else {
            int j = astring.length - 1;
            String s;

            if (j == i) {
                s = Integer.toString(blockposition.func_177958_n());
            } else {
                if (j != i + 1) {
                    return Collections.emptyList();
                }

                s = Integer.toString(blockposition.func_177952_p());
            }

            return Lists.newArrayList(new String[] { s});
        }
    }

    public static boolean func_71523_a(String s, String s1) {
        return s1.regionMatches(true, 0, s, 0, s.length());
    }

    public static List<String> getListMatchingLast(String[] args, String... matches) { return func_71530_a(args, matches); } // Paper - OBFHELPER
    public static List<String> func_71530_a(String[] astring, String... astring1) {
        return func_175762_a(astring, (Collection) Arrays.asList(astring1));
    }

    public static List<String> func_175762_a(String[] astring, Collection<?> collection) {
        String s = astring[astring.length - 1];
        ArrayList arraylist = Lists.newArrayList();

        if (!collection.isEmpty()) {
            Iterator iterator = Iterables.transform(collection, Functions.toStringFunction()).iterator();

            while (iterator.hasNext()) {
                String s1 = (String) iterator.next();

                if (func_71523_a(s, s1)) {
                    arraylist.add(s1);
                }
            }

            if (arraylist.isEmpty()) {
                iterator = collection.iterator();

                while (iterator.hasNext()) {
                    Object object = iterator.next();

                    if (object instanceof ResourceLocation && func_71523_a(s, ((ResourceLocation) object).func_110623_a())) {
                        arraylist.add(String.valueOf(object));
                    }
                }
            }
        }

        return arraylist;
    }

    public boolean func_82358_a(String[] astring, int i) {
        return false;
    }

    public static void func_152373_a(ICommandSender icommandlistener, ICommand icommand, String s, Object... aobject) {
        func_152374_a(icommandlistener, icommand, 0, s, aobject);
    }

    public static void func_152374_a(ICommandSender icommandlistener, ICommand icommand, int i, String s, Object... aobject) {
        if (CommandBase.field_71533_a != null) {
            CommandBase.field_71533_a.func_152372_a(icommandlistener, icommand, i, s, aobject);
        }

    }

    public static void func_71529_a(ICommandListener icommanddispatcher) {
        CommandBase.field_71533_a = icommanddispatcher;
    }

    public int compareTo(ICommand icommand) {
        return this.func_71517_b().compareTo(icommand.func_71517_b());
    }

    public int compareTo(ICommand object) { // Paper - fix decompile error
        return this.compareTo((ICommand) object);
    }

    public static class CoordinateArg {

        private final double field_179633_a;
        private final double field_179631_b;
        private final boolean field_179632_c;

        protected CoordinateArg(double d0, double d1, boolean flag) {
            this.field_179633_a = d0;
            this.field_179631_b = d1;
            this.field_179632_c = flag;
        }

        public double func_179628_a() {
            return this.field_179633_a;
        }

        public double func_179629_b() {
            return this.field_179631_b;
        }

        public boolean func_179630_c() {
            return this.field_179632_c;
        }
    }
}
