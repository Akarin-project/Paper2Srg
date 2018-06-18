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

    private static ICommandListener commandListener;
    private static final Splitter COMMA_SPLITTER = Splitter.on(',');
    private static final Splitter EQUAL_SPLITTER = Splitter.on('=').limit(2);

    public CommandBase() {}

    protected static SyntaxErrorException toSyntaxException(JsonParseException jsonparseexception) {
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

    public static NBTTagCompound entityToNBT(Entity entity) {
        NBTTagCompound nbttagcompound = entity.writeToNBT(new NBTTagCompound());

        if (entity instanceof EntityPlayer) {
            ItemStack itemstack = ((EntityPlayer) entity).inventory.getCurrentItem();

            if (!itemstack.isEmpty()) {
                nbttagcompound.setTag("SelectedItem", itemstack.writeToNBT(new NBTTagCompound()));
            }
        }

        return nbttagcompound;
    }

    public int getRequiredPermissionLevel() {
        return 4;
    }

    public List<String> getAliases() {
        return Collections.emptyList();
    }

    public boolean checkPermission(MinecraftServer minecraftserver, ICommandSender icommandlistener) {
        return icommandlistener.canUseCommand(this.getRequiredPermissionLevel(), this.getName());
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return Collections.emptyList();
    }

    public static int parseInt(String s) throws NumberInvalidException {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException numberformatexception) {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { s});
        }
    }

    public static int parseInt(String s, int i) throws NumberInvalidException {
        return parseInt(s, i, Integer.MAX_VALUE);
    }

    public static int parseInt(String s, int i, int j) throws NumberInvalidException {
        int k = parseInt(s);

        if (k < i) {
            throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] { Integer.valueOf(k), Integer.valueOf(i)});
        } else if (k > j) {
            throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] { Integer.valueOf(k), Integer.valueOf(j)});
        } else {
            return k;
        }
    }

    public static long parseLong(String s) throws NumberInvalidException {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException numberformatexception) {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { s});
        }
    }

    public static long parseLong(String s, long i, long j) throws NumberInvalidException {
        long k = parseLong(s);

        if (k < i) {
            throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] { Long.valueOf(k), Long.valueOf(i)});
        } else if (k > j) {
            throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] { Long.valueOf(k), Long.valueOf(j)});
        } else {
            return k;
        }
    }

    public static BlockPos parseBlockPos(ICommandSender icommandlistener, String[] astring, int i, boolean flag) throws NumberInvalidException {
        BlockPos blockposition = icommandlistener.getPosition();

        return new BlockPos(parseDouble((double) blockposition.getX(), astring[i], -30000000, 30000000, flag), parseDouble((double) blockposition.getY(), astring[i + 1], 0, 256, false), parseDouble((double) blockposition.getZ(), astring[i + 2], -30000000, 30000000, flag));
    }

    public static double parseDouble(String s) throws NumberInvalidException {
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

    public static double parseDouble(String s, double d0) throws NumberInvalidException {
        return parseDouble(s, d0, Double.MAX_VALUE);
    }

    public static double parseDouble(String s, double d0, double d1) throws NumberInvalidException {
        double d2 = parseDouble(s);

        if (d2 < d0) {
            throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] { String.format("%.2f", new Object[] { Double.valueOf(d2)}), String.format("%.2f", new Object[] { Double.valueOf(d0)})});
        } else if (d2 > d1) {
            throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] { String.format("%.2f", new Object[] { Double.valueOf(d2)}), String.format("%.2f", new Object[] { Double.valueOf(d1)})});
        } else {
            return d2;
        }
    }

    public static boolean parseBoolean(String s) throws CommandException {
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

    public static EntityPlayerMP getCommandSenderAsPlayer(ICommandSender icommandlistener) throws PlayerNotFoundException {
        if (icommandlistener instanceof EntityPlayerMP) {
            return (EntityPlayerMP) icommandlistener;
        } else {
            throw new PlayerNotFoundException("commands.generic.player.unspecified");
        }
    }

    public static List<EntityPlayerMP> getPlayers(MinecraftServer minecraftserver, ICommandSender icommandlistener, String s) throws CommandException {
        List list = EntitySelector.getPlayers(icommandlistener, s);

        return (List) (list.isEmpty() ? Lists.newArrayList(new EntityPlayerMP[] { getPlayer(minecraftserver, (EntityPlayerMP) null, s)}) : list);
    }

    public static EntityPlayerMP getPlayer(MinecraftServer minecraftserver, ICommandSender icommandlistener, String s) throws CommandException {
        return getPlayer(minecraftserver, EntitySelector.matchOnePlayer(icommandlistener, s), s);
    }

    private static EntityPlayerMP getPlayer(MinecraftServer minecraftserver, @Nullable EntityPlayerMP entityplayer, String s) throws CommandException {
        if (entityplayer == null) {
            try {
                entityplayer = minecraftserver.getPlayerList().getPlayerByUUID(UUID.fromString(s));
            } catch (IllegalArgumentException illegalargumentexception) {
                ;
            }
        }

        if (entityplayer == null) {
            entityplayer = minecraftserver.getPlayerList().getPlayerByUsername(s);
        }

        if (entityplayer == null) {
            throw new PlayerNotFoundException("commands.generic.player.notFound", new Object[] { s});
        } else {
            return entityplayer;
        }
    }

    public static Entity getEntity(MinecraftServer minecraftserver, ICommandSender icommandlistener, String s) throws CommandException {
        return getEntity(minecraftserver, icommandlistener, s, Entity.class);
    }

    public static <T extends Entity> T getEntity(MinecraftServer minecraftserver, ICommandSender icommandlistener, String s, Class<? extends T> oclass) throws CommandException {
        Object object = EntitySelector.matchOneEntity(icommandlistener, s, oclass);

        if (object == null) {
            object = minecraftserver.getPlayerList().getPlayerByUsername(s);
        }

        if (object == null) {
            try {
                UUID uuid = UUID.fromString(s);

                object = minecraftserver.getEntityFromUuid(uuid);
                if (object == null) {
                    object = minecraftserver.getPlayerList().getPlayerByUUID(uuid);
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

    public static List<Entity> getEntityList(MinecraftServer minecraftserver, ICommandSender icommandlistener, String s) throws CommandException {
        return (List) (EntitySelector.isSelector(s) ? EntitySelector.matchEntities(icommandlistener, s, Entity.class) : Lists.newArrayList(new Entity[] { getEntity(minecraftserver, icommandlistener, s)}));
    }

    public static String getPlayerName(MinecraftServer minecraftserver, ICommandSender icommandlistener, String s) throws CommandException {
        try {
            return getPlayer(minecraftserver, icommandlistener, s).getName();
        } catch (CommandException commandexception) {
            if (EntitySelector.isSelector(s)) {
                throw commandexception;
            } else {
                return s;
            }
        }
    }

    public static String getEntityName(MinecraftServer minecraftserver, ICommandSender icommandlistener, String s) throws CommandException {
        try {
            return getPlayer(minecraftserver, icommandlistener, s).getName();
        } catch (PlayerNotFoundException exceptionplayernotfound) {
            try {
                return getEntity(minecraftserver, icommandlistener, s).getCachedUniqueIdString();
            } catch (EntityNotFoundException exceptionentitynotfound) {
                if (EntitySelector.isSelector(s)) {
                    throw exceptionentitynotfound;
                } else {
                    return s;
                }
            }
        }
    }

    public static ITextComponent getChatComponentFromNthArg(ICommandSender icommandlistener, String[] astring, int i) throws CommandException {
        return getChatComponentFromNthArg(icommandlistener, astring, i, false);
    }

    public static ITextComponent getChatComponentFromNthArg(ICommandSender icommandlistener, String[] astring, int i, boolean flag) throws CommandException {
        TextComponentString chatcomponenttext = new TextComponentString("");

        for (int j = i; j < astring.length; ++j) {
            if (j > i) {
                chatcomponenttext.appendText(" ");
            }

            Object object = new TextComponentString(astring[j]);

            if (flag) {
                ITextComponent ichatbasecomponent = EntitySelector.matchEntitiesToTextComponent(icommandlistener, astring[j]);

                if (ichatbasecomponent == null) {
                    if (EntitySelector.isSelector(astring[j])) {
                        throw new PlayerNotFoundException("commands.generic.selector.notFound", new Object[] { astring[j]});
                    }
                } else {
                    object = ichatbasecomponent;
                }
            }

            chatcomponenttext.appendSibling((ITextComponent) object);
        }

        return chatcomponenttext;
    }

    public static String buildString(String[] astring, int i) {
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

    public static CommandBase.CoordinateArg parseCoordinate(double d0, String s, boolean flag) throws NumberInvalidException {
        return parseCoordinate(d0, s, -30000000, 30000000, flag);
    }

    public static CommandBase.CoordinateArg parseCoordinate(double d0, String s, int i, int j, boolean flag) throws NumberInvalidException {
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

                d1 += parseDouble(s);
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

    public static double parseDouble(double d0, String s, boolean flag) throws NumberInvalidException {
        return parseDouble(d0, s, -30000000, 30000000, flag);
    }

    public static double parseDouble(double d0, String s, int i, int j, boolean flag) throws NumberInvalidException {
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

                d1 += parseDouble(s);
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

    public static Item getItemByText(ICommandSender icommandlistener, String s) throws NumberInvalidException {
        ResourceLocation minecraftkey = new ResourceLocation(s);
        Item item = (Item) Item.REGISTRY.getObject(minecraftkey);

        if (item == null) {
            throw new NumberInvalidException("commands.give.item.notFound", new Object[] { minecraftkey});
        } else {
            return item;
        }
    }

    public static Block getBlockByText(ICommandSender icommandlistener, String s) throws NumberInvalidException {
        ResourceLocation minecraftkey = new ResourceLocation(s);

        if (!Block.REGISTRY.containsKey(minecraftkey)) {
            throw new NumberInvalidException("commands.give.block.notFound", new Object[] { minecraftkey});
        } else {
            return (Block) Block.REGISTRY.getObject(minecraftkey);
        }
    }

    public static IBlockState convertArgToBlockState(Block block, String s) throws NumberInvalidException, InvalidBlockStateException {
        try {
            int i = Integer.parseInt(s);

            if (i < 0) {
                throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] { Integer.valueOf(i), Integer.valueOf(0)});
            } else if (i > 15) {
                throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] { Integer.valueOf(i), Integer.valueOf(15)});
            } else {
                return block.getStateFromMeta(Integer.parseInt(s));
            }
        } catch (RuntimeException runtimeexception) {
            try {
                Map map = getBlockStatePropertyValueMap(block, s);
                IBlockState iblockdata = block.getDefaultState();

                Entry entry;

                for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); iblockdata = getBlockState(iblockdata, (IProperty) entry.getKey(), (Comparable) entry.getValue())) {
                    entry = (Entry) iterator.next();
                }

                return iblockdata;
            } catch (RuntimeException runtimeexception1) {
                throw new InvalidBlockStateException("commands.generic.blockstate.invalid", new Object[] { s, Block.REGISTRY.getNameForObject(block)});
            }
        }
    }

    private static <T extends Comparable<T>> IBlockState getBlockState(IBlockState iblockdata, IProperty<T> iblockstate, Comparable<?> comparable) {
        return iblockdata.withProperty(iblockstate, (T) comparable); // Paper - fix decompiler error
    }

    public static Predicate<IBlockState> convertArgToBlockStatePredicate(final Block block, String s) throws InvalidBlockStateException {
        if (!"*".equals(s) && !"-1".equals(s)) {
            try {
                final int i = Integer.parseInt(s);

                return new Predicate() {
                    public boolean a(@Nullable IBlockState iblockdata) {
                        return i == iblockdata.getBlock().getMetaFromState(iblockdata);
                    }

                    public boolean apply(@Nullable Object object) {
                        return this.a((IBlockState) object);
                    }
                };
            } catch (RuntimeException runtimeexception) {
                final Map map = getBlockStatePropertyValueMap(block, s);

                return new Predicate() {
                    public boolean a(@Nullable IBlockState iblockdata) {
                        if (iblockdata != null && block == iblockdata.getBlock()) {
                            Iterator iterator = map.entrySet().iterator();

                            Entry entry;

                            do {
                                if (!iterator.hasNext()) {
                                    return true;
                                }

                                entry = (Entry) iterator.next();
                            } while (iblockdata.getValue((IProperty) entry.getKey()).equals(entry.getValue()));

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

    private static Map<IProperty<?>, Comparable<?>> getBlockStatePropertyValueMap(Block block, String s) throws InvalidBlockStateException {
        HashMap hashmap = Maps.newHashMap();

        if ("default".equals(s)) {
            return block.getDefaultState().getProperties();
        } else {
            BlockStateContainer blockstatelist = block.getBlockState();
            Iterator iterator = CommandBase.COMMA_SPLITTER.split(s).iterator();

            while (true) {
                if (!iterator.hasNext()) {
                    return hashmap;
                }

                String s1 = (String) iterator.next();
                Iterator iterator1 = CommandBase.EQUAL_SPLITTER.split(s1).iterator();

                if (!iterator1.hasNext()) {
                    break;
                }

                IProperty iblockstate = blockstatelist.getProperty((String) iterator1.next());

                if (iblockstate == null || !iterator1.hasNext()) {
                    break;
                }

                Comparable comparable = getValueHelper(iblockstate, (String) iterator1.next());

                if (comparable == null) {
                    break;
                }

                hashmap.put(iblockstate, comparable);
            }

            throw new InvalidBlockStateException("commands.generic.blockstate.invalid", new Object[] { s, Block.REGISTRY.getNameForObject(block)});
        }
    }

    @Nullable
    private static <T extends Comparable<T>> T getValueHelper(IProperty<T> iblockstate, String s) {
        return iblockstate.parseValue(s).orNull(); // Paper - fix decompiler error
    }

    public static String joinNiceString(Object[] aobject) {
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

    public static ITextComponent join(List<ITextComponent> list) {
        TextComponentString chatcomponenttext = new TextComponentString("");

        for (int i = 0; i < list.size(); ++i) {
            if (i > 0) {
                if (i == list.size() - 1) {
                    chatcomponenttext.appendText(" and ");
                } else if (i > 0) {
                    chatcomponenttext.appendText(", ");
                }
            }

            chatcomponenttext.appendSibling((ITextComponent) list.get(i));
        }

        return chatcomponenttext;
    }

    public static String joinNiceStringFromCollection(Collection<String> collection) {
        return joinNiceString(collection.toArray(new String[collection.size()]));
    }

    public static List<String> getTabCompletionCoordinate(String[] astring, int i, @Nullable BlockPos blockposition) {
        if (blockposition == null) {
            return Lists.newArrayList(new String[] { "~"});
        } else {
            int j = astring.length - 1;
            String s;

            if (j == i) {
                s = Integer.toString(blockposition.getX());
            } else if (j == i + 1) {
                s = Integer.toString(blockposition.getY());
            } else {
                if (j != i + 2) {
                    return Collections.emptyList();
                }

                s = Integer.toString(blockposition.getZ());
            }

            return Lists.newArrayList(new String[] { s});
        }
    }

    public static List<String> getTabCompletionCoordinateXZ(String[] astring, int i, @Nullable BlockPos blockposition) {
        if (blockposition == null) {
            return Lists.newArrayList(new String[] { "~"});
        } else {
            int j = astring.length - 1;
            String s;

            if (j == i) {
                s = Integer.toString(blockposition.getX());
            } else {
                if (j != i + 1) {
                    return Collections.emptyList();
                }

                s = Integer.toString(blockposition.getZ());
            }

            return Lists.newArrayList(new String[] { s});
        }
    }

    public static boolean doesStringStartWith(String s, String s1) {
        return s1.regionMatches(true, 0, s, 0, s.length());
    }

    public static List<String> getListMatchingLast(String[] args, String... matches) { return getListOfStringsMatchingLastWord(args, matches); } // Paper - OBFHELPER
    public static List<String> getListOfStringsMatchingLastWord(String[] astring, String... astring1) {
        return getListOfStringsMatchingLastWord(astring, (Collection) Arrays.asList(astring1));
    }

    public static List<String> getListOfStringsMatchingLastWord(String[] astring, Collection<?> collection) {
        String s = astring[astring.length - 1];
        ArrayList arraylist = Lists.newArrayList();

        if (!collection.isEmpty()) {
            Iterator iterator = Iterables.transform(collection, Functions.toStringFunction()).iterator();

            while (iterator.hasNext()) {
                String s1 = (String) iterator.next();

                if (doesStringStartWith(s, s1)) {
                    arraylist.add(s1);
                }
            }

            if (arraylist.isEmpty()) {
                iterator = collection.iterator();

                while (iterator.hasNext()) {
                    Object object = iterator.next();

                    if (object instanceof ResourceLocation && doesStringStartWith(s, ((ResourceLocation) object).getResourcePath())) {
                        arraylist.add(String.valueOf(object));
                    }
                }
            }
        }

        return arraylist;
    }

    public boolean isUsernameIndex(String[] astring, int i) {
        return false;
    }

    public static void notifyCommandListener(ICommandSender icommandlistener, ICommand icommand, String s, Object... aobject) {
        notifyCommandListener(icommandlistener, icommand, 0, s, aobject);
    }

    public static void notifyCommandListener(ICommandSender icommandlistener, ICommand icommand, int i, String s, Object... aobject) {
        if (CommandBase.commandListener != null) {
            CommandBase.commandListener.notifyListener(icommandlistener, icommand, i, s, aobject);
        }

    }

    public static void setCommandListener(ICommandListener icommanddispatcher) {
        CommandBase.commandListener = icommanddispatcher;
    }

    public int compareTo(ICommand icommand) {
        return this.getName().compareTo(icommand.getName());
    }

    public int compareTo(ICommand object) { // Paper - fix decompile error
        return this.compareTo((ICommand) object);
    }

    public static class CoordinateArg {

        private final double result;
        private final double amount;
        private final boolean isRelative;

        protected CoordinateArg(double d0, double d1, boolean flag) {
            this.result = d0;
            this.amount = d1;
            this.isRelative = flag;
        }

        public double getResult() {
            return this.result;
        }

        public double getAmount() {
            return this.amount;
        }

        public boolean isRelative() {
            return this.isRelative;
        }
    }
}
