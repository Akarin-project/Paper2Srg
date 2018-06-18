package net.minecraft.command;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraft.world.World;

public class EntitySelector {

    private static final Pattern TOKEN_PATTERN = Pattern.compile("^@([pares])(?:\\[([^ ]*)\\])?$");
    private static final Splitter COMMA_SPLITTER = Splitter.on(',').omitEmptyStrings();
    private static final Splitter EQUAL_SPLITTER = Splitter.on('=').limit(2);
    private static final Set<String> VALID_ARGUMENTS = Sets.newHashSet();
    private static final String ARGUMENT_RANGE_MAX = addArgument("r");
    private static final String ARGUMENT_RANGE_MIN = addArgument("rm");
    private static final String ARGUMENT_LEVEL_MAX = addArgument("l");
    private static final String ARGUMENT_LEVEL_MIN = addArgument("lm");
    private static final String ARGUMENT_COORDINATE_X = addArgument("x");
    private static final String ARGUMENT_COORDINATE_Y = addArgument("y");
    private static final String ARGUMENT_COORDINATE_Z = addArgument("z");
    private static final String ARGUMENT_DELTA_X = addArgument("dx");
    private static final String ARGUMENT_DELTA_Y = addArgument("dy");
    private static final String ARGUMENT_DELTA_Z = addArgument("dz");
    private static final String ARGUMENT_ROTX_MAX = addArgument("rx");
    private static final String ARGUMENT_ROTX_MIN = addArgument("rxm");
    private static final String ARGUMENT_ROTY_MAX = addArgument("ry");
    private static final String ARGUMENT_ROTY_MIN = addArgument("rym");
    private static final String ARGUMENT_COUNT = addArgument("c");
    private static final String ARGUMENT_MODE = addArgument("m");
    private static final String ARGUMENT_TEAM_NAME = addArgument("team");
    private static final String ARGUMENT_PLAYER_NAME = addArgument("name");
    private static final String ARGUMENT_ENTITY_TYPE = addArgument("type");
    private static final String ARGUMENT_ENTITY_TAG = addArgument("tag");
    private static final Predicate<String> IS_VALID_ARGUMENT = new Predicate() {
        public boolean a(@Nullable String s) {
            return s != null && (EntitySelector.VALID_ARGUMENTS.contains(s) || s.length() > "score_".length() && s.startsWith("score_"));
        }

        public boolean apply(@Nullable Object object) {
            return this.a((String) object);
        }
    };
    private static final Set<String> WORLD_BINDING_ARGS = Sets.newHashSet(new String[] { EntitySelector.ARGUMENT_COORDINATE_X, EntitySelector.ARGUMENT_COORDINATE_Y, EntitySelector.ARGUMENT_COORDINATE_Z, EntitySelector.ARGUMENT_DELTA_X, EntitySelector.ARGUMENT_DELTA_Y, EntitySelector.ARGUMENT_DELTA_Z, EntitySelector.ARGUMENT_RANGE_MIN, EntitySelector.ARGUMENT_RANGE_MAX});

    private static String addArgument(String s) {
        EntitySelector.VALID_ARGUMENTS.add(s);
        return s;
    }

    @Nullable
    public static EntityPlayerMP matchOnePlayer(ICommandSender icommandlistener, String s) throws CommandException {
        return (EntityPlayerMP) matchOneEntity(icommandlistener, s, EntityPlayerMP.class);
    }

    public static List<EntityPlayerMP> getPlayers(ICommandSender icommandlistener, String s) throws CommandException {
        return matchEntities(icommandlistener, s, EntityPlayerMP.class);
    }

    @Nullable
    public static <T extends Entity> T matchOneEntity(ICommandSender icommandlistener, String s, Class<? extends T> oclass) throws CommandException {
        List list = matchEntities(icommandlistener, s, oclass);

        return list.size() == 1 ? (Entity) list.get(0) : null;
    }

    @Nullable
    public static ITextComponent matchEntitiesToTextComponent(ICommandSender icommandlistener, String s) throws CommandException {
        List list = matchEntities(icommandlistener, s, Entity.class);

        if (list.isEmpty()) {
            return null;
        } else {
            ArrayList arraylist = Lists.newArrayList();
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                arraylist.add(entity.getDisplayName());
            }

            return CommandBase.join((List) arraylist);
        }
    }

    public static <T extends Entity> List<T> matchEntities(ICommandSender icommandlistener, String s, Class<? extends T> oclass) throws CommandException {
        Matcher matcher = EntitySelector.TOKEN_PATTERN.matcher(s);

        if (matcher.matches() && icommandlistener.canUseCommand(1, "@")) {
            Map map = getArgumentMap(matcher.group(2));

            if (!isEntityTypeValid(icommandlistener, map)) {
                return Collections.emptyList();
            } else {
                String s1 = matcher.group(1);
                BlockPos blockposition = getBlockPosFromArguments(map, icommandlistener.getPosition());
                Vec3d vec3d = getPosFromArguments(map, icommandlistener.getPositionVector());
                List list = getWorlds(icommandlistener, map);
                ArrayList arraylist = Lists.newArrayList();
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    World world = (World) iterator.next();

                    if (world != null) {
                        ArrayList arraylist1 = Lists.newArrayList();

                        arraylist1.addAll(getTypePredicates(map, s1));
                        arraylist1.addAll(getXpLevelPredicates(map));
                        arraylist1.addAll(getGamemodePredicates(map));
                        arraylist1.addAll(getTeamPredicates(map));
                        arraylist1.addAll(getScorePredicates(icommandlistener, map));
                        arraylist1.addAll(getNamePredicates(map));
                        arraylist1.addAll(getTagPredicates(map));
                        arraylist1.addAll(getRadiusPredicates(map, vec3d));
                        arraylist1.addAll(getRotationsPredicates(map));
                        if ("s".equalsIgnoreCase(s1)) {
                            Entity entity = icommandlistener.getCommandSenderEntity();

                            if (entity != null && oclass.isAssignableFrom(entity.getClass())) {
                                if (map.containsKey(EntitySelector.ARGUMENT_DELTA_X) || map.containsKey(EntitySelector.ARGUMENT_DELTA_Y) || map.containsKey(EntitySelector.ARGUMENT_DELTA_Z)) {
                                    int i = getInt(map, EntitySelector.ARGUMENT_DELTA_X, 0);
                                    int j = getInt(map, EntitySelector.ARGUMENT_DELTA_Y, 0);
                                    int k = getInt(map, EntitySelector.ARGUMENT_DELTA_Z, 0);
                                    AxisAlignedBB axisalignedbb = getAABB(blockposition, i, j, k);

                                    if (!axisalignedbb.intersects(entity.getEntityBoundingBox())) {
                                        return Collections.emptyList();
                                    }
                                }

                                Iterator iterator1 = arraylist1.iterator();

                                Predicate predicate;

                                do {
                                    if (!iterator1.hasNext()) {
                                        return Lists.newArrayList(new Entity[] { entity});
                                    }

                                    predicate = (Predicate) iterator1.next();
                                } while (predicate.apply(entity));

                                return Collections.emptyList();
                            }

                            return Collections.emptyList();
                        }

                        arraylist.addAll(filterResults(map, oclass, (List) arraylist1, s1, world, blockposition));
                    }
                }

                return getEntitiesFromPredicates((List) arraylist, map, icommandlistener, oclass, s1, vec3d);
            }
        } else {
            return Collections.emptyList();
        }
    }

    private static List<World> getWorlds(ICommandSender icommandlistener, Map<String, String> map) {
        ArrayList arraylist = Lists.newArrayList();

        if (hasArgument(map)) {
            arraylist.add(icommandlistener.getEntityWorld());
        } else {
            Collections.addAll(arraylist, icommandlistener.getServer().worlds);
        }

        return arraylist;
    }

    private static <T extends Entity> boolean isEntityTypeValid(ICommandSender icommandlistener, Map<String, String> map) {
        String s = getArgument(map, EntitySelector.ARGUMENT_ENTITY_TYPE);

        if (s == null) {
            return true;
        } else {
            ResourceLocation minecraftkey = new ResourceLocation(s.startsWith("!") ? s.substring(1) : s);

            if (EntityList.isRegistered(minecraftkey)) {
                return true;
            } else {
                TextComponentTranslation chatmessage = new TextComponentTranslation("commands.generic.entity.invalidType", new Object[] { minecraftkey});

                chatmessage.getStyle().setColor(TextFormatting.RED);
                icommandlistener.sendMessage(chatmessage);
                return false;
            }
        }
    }

    private static List<Predicate<Entity>> getTypePredicates(Map<String, String> map, String s) {
        String s1 = getArgument(map, EntitySelector.ARGUMENT_ENTITY_TYPE);

        if (s1 != null && (s.equals("e") || s.equals("r") || s.equals("s"))) {
            final boolean flag = s1.startsWith("!");
            final ResourceLocation minecraftkey = new ResourceLocation(flag ? s1.substring(1) : s1);

            return Collections.singletonList(new Predicate() {
                public boolean a(@Nullable Entity entity) {
                    return EntityList.isMatchingName(entity, minecraftkey) != flag;
                }

                public boolean apply(@Nullable Object object) {
                    return this.a((Entity) object);
                }
            });
        } else {
            return !s.equals("e") && !s.equals("s") ? Collections.singletonList(new Predicate() {
                public boolean a(@Nullable Entity entity) {
                    return entity instanceof EntityPlayer;
                }

                public boolean apply(@Nullable Object object) {
                    return this.a((Entity) object);
                }
            }) : Collections.emptyList();
        }
    }

    private static List<Predicate<Entity>> getXpLevelPredicates(Map<String, String> map) {
        ArrayList arraylist = Lists.newArrayList();
        final int i = getInt(map, EntitySelector.ARGUMENT_LEVEL_MIN, -1);
        final int j = getInt(map, EntitySelector.ARGUMENT_LEVEL_MAX, -1);

        if (i > -1 || j > -1) {
            arraylist.add(new Predicate() {
                public boolean a(@Nullable Entity entity) {
                    if (!(entity instanceof EntityPlayerMP)) {
                        return false;
                    } else {
                        EntityPlayerMP entityplayer = (EntityPlayerMP) entity;

                        return (i <= -1 || entityplayer.experienceLevel >= i) && (j <= -1 || entityplayer.experienceLevel <= j);
                    }
                }

                public boolean apply(@Nullable Object object) {
                    return this.a((Entity) object);
                }
            });
        }

        return arraylist;
    }

    private static List<Predicate<Entity>> getGamemodePredicates(Map<String, String> map) {
        ArrayList arraylist = Lists.newArrayList();
        String s = getArgument(map, EntitySelector.ARGUMENT_MODE);

        if (s == null) {
            return arraylist;
        } else {
            final boolean flag = s.startsWith("!");

            if (flag) {
                s = s.substring(1);
            }

            final GameType enumgamemode;

            try {
                int i = Integer.parseInt(s);

                enumgamemode = GameType.parseGameTypeWithDefault(i, GameType.NOT_SET);
            } catch (Throwable throwable) {
                enumgamemode = GameType.parseGameTypeWithDefault(s, GameType.NOT_SET);
            }

            arraylist.add(new Predicate() {
                public boolean a(@Nullable Entity entity) {
                    if (!(entity instanceof EntityPlayerMP)) {
                        return false;
                    } else {
                        EntityPlayerMP entityplayer = (EntityPlayerMP) entity;
                        GameType enumgamemode = entityplayer.interactionManager.getGameType();

                        return flag ? enumgamemode != enumgamemode1 : enumgamemode == enumgamemode1;
                    }
                }

                public boolean apply(@Nullable Object object) {
                    return this.a((Entity) object);
                }
            });
            return arraylist;
        }
    }

    private static List<Predicate<Entity>> getTeamPredicates(Map<String, String> map) {
        ArrayList arraylist = Lists.newArrayList();
        final String s = getArgument(map, EntitySelector.ARGUMENT_TEAM_NAME);
        final boolean flag = s != null && s.startsWith("!");

        if (flag) {
            s = s.substring(1);
        }

        if (s != null) {
            arraylist.add(new Predicate() {
                public boolean a(@Nullable Entity entity) {
                    if (!(entity instanceof EntityLivingBase)) {
                        return false;
                    } else {
                        EntityLivingBase entityliving = (EntityLivingBase) entity;
                        Team scoreboardteambase = entityliving.getTeam();
                        String s = scoreboardteambase == null ? "" : scoreboardteambase.getName();

                        return s.equals(s1) != flag;
                    }
                }

                public boolean apply(@Nullable Object object) {
                    return this.a((Entity) object);
                }
            });
        }

        return arraylist;
    }

    private static List<Predicate<Entity>> getScorePredicates(final ICommandSender icommandlistener, Map<String, String> map) {
        final Map map1 = getScoreMap(map);

        return (List) (map1.isEmpty() ? Collections.emptyList() : Lists.newArrayList(new Predicate[] { new Predicate() {
            public boolean a(@Nullable Entity entity) {
                if (entity == null) {
                    return false;
                } else {
                    Scoreboard scoreboard = icommandlistener.getServer().getWorld(0).getScoreboard();
                    Iterator iterator = map.entrySet().iterator();

                    Entry entry;
                    boolean flag;
                    int i;

                    do {
                        if (!iterator.hasNext()) {
                            return true;
                        }

                        entry = (Entry) iterator.next();
                        String s = (String) entry.getKey();

                        flag = false;
                        if (s.endsWith("_min") && s.length() > 4) {
                            flag = true;
                            s = s.substring(0, s.length() - 4);
                        }

                        ScoreObjective scoreboardobjective = scoreboard.getObjective(s);

                        if (scoreboardobjective == null) {
                            return false;
                        }

                        String s1 = entity instanceof EntityPlayerMP ? entity.getName() : entity.getCachedUniqueIdString();

                        if (!scoreboard.entityHasObjective(s1, scoreboardobjective)) {
                            return false;
                        }

                        Score scoreboardscore = scoreboard.getOrCreateScore(s1, scoreboardobjective);

                        i = scoreboardscore.getScorePoints();
                        if (i < ((Integer) entry.getValue()).intValue() && flag) {
                            return false;
                        }
                    } while (i <= ((Integer) entry.getValue()).intValue() || flag);

                    return false;
                }
            }

            public boolean apply(@Nullable Object object) {
                return this.a((Entity) object);
            }
        }}));
    }

    private static List<Predicate<Entity>> getNamePredicates(Map<String, String> map) {
        ArrayList arraylist = Lists.newArrayList();
        final String s = getArgument(map, EntitySelector.ARGUMENT_PLAYER_NAME);
        final boolean flag = s != null && s.startsWith("!");

        if (flag) {
            s = s.substring(1);
        }

        if (s != null) {
            arraylist.add(new Predicate() {
                public boolean a(@Nullable Entity entity) {
                    return entity != null && entity.getName().equals(s) != flag;
                }

                public boolean apply(@Nullable Object object) {
                    return this.a((Entity) object);
                }
            });
        }

        return arraylist;
    }

    private static List<Predicate<Entity>> getTagPredicates(Map<String, String> map) {
        ArrayList arraylist = Lists.newArrayList();
        final String s = getArgument(map, EntitySelector.ARGUMENT_ENTITY_TAG);
        final boolean flag = s != null && s.startsWith("!");

        if (flag) {
            s = s.substring(1);
        }

        if (s != null) {
            arraylist.add(new Predicate() {
                public boolean a(@Nullable Entity entity) {
                    return entity == null ? false : ("".equals(s) ? entity.getTags().isEmpty() != flag : entity.getTags().contains(s) != flag);
                }

                public boolean apply(@Nullable Object object) {
                    return this.a((Entity) object);
                }
            });
        }

        return arraylist;
    }

    private static List<Predicate<Entity>> getRadiusPredicates(Map<String, String> map, final Vec3d vec3d) {
        double d0 = (double) getInt(map, EntitySelector.ARGUMENT_RANGE_MIN, -1);
        double d1 = (double) getInt(map, EntitySelector.ARGUMENT_RANGE_MAX, -1);
        final boolean flag = d0 < -0.5D;
        final boolean flag1 = d1 < -0.5D;

        if (flag && flag1) {
            return Collections.emptyList();
        } else {
            double d2 = Math.max(d0, 1.0E-4D);
            final double d3 = d2 * d2;
            double d4 = Math.max(d1, 1.0E-4D);
            final double d5 = d4 * d4;

            return Lists.newArrayList(new Predicate[] { new Predicate() {
                public boolean a(@Nullable Entity entity) {
                    if (entity == null) {
                        return false;
                    } else {
                        double d0 = vec3d.squareDistanceTo(entity.posX, entity.posY, entity.posZ);

                        return (flag || d0 >= d1) && (flag1 || d0 <= d2);
                    }
                }

                public boolean apply(@Nullable Object object) {
                    return this.a((Entity) object);
                }
            }});
        }
    }

    private static List<Predicate<Entity>> getRotationsPredicates(Map<String, String> map) {
        ArrayList arraylist = Lists.newArrayList();
        final int i;
        final int j;

        if (map.containsKey(EntitySelector.ARGUMENT_ROTY_MIN) || map.containsKey(EntitySelector.ARGUMENT_ROTY_MAX)) {
            i = MathHelper.wrapDegrees(getInt(map, EntitySelector.ARGUMENT_ROTY_MIN, 0));
            j = MathHelper.wrapDegrees(getInt(map, EntitySelector.ARGUMENT_ROTY_MAX, 359));
            arraylist.add(new Predicate() {
                public boolean a(@Nullable Entity entity) {
                    if (entity == null) {
                        return false;
                    } else {
                        int i = MathHelper.wrapDegrees(MathHelper.floor(entity.rotationYaw));

                        return j > ARGUMENT_COORDINATE_Z ? i >= j || i <= ARGUMENT_COORDINATE_Z : i >= j && i <= ARGUMENT_COORDINATE_Z;
                    }
                }

                public boolean apply(@Nullable Object object) {
                    return this.a((Entity) object);
                }
            });
        }

        if (map.containsKey(EntitySelector.ARGUMENT_ROTX_MIN) || map.containsKey(EntitySelector.ARGUMENT_ROTX_MAX)) {
            i = MathHelper.wrapDegrees(getInt(map, EntitySelector.ARGUMENT_ROTX_MIN, 0));
            j = MathHelper.wrapDegrees(getInt(map, EntitySelector.ARGUMENT_ROTX_MAX, 359));
            arraylist.add(new Predicate() {
                public boolean a(@Nullable Entity entity) {
                    if (entity == null) {
                        return false;
                    } else {
                        int i = MathHelper.wrapDegrees(MathHelper.floor(entity.rotationPitch));

                        return j > ARGUMENT_COORDINATE_Z ? i >= j || i <= ARGUMENT_COORDINATE_Z : i >= j && i <= ARGUMENT_COORDINATE_Z;
                    }
                }

                public boolean apply(@Nullable Object object) {
                    return this.a((Entity) object);
                }
            });
        }

        return arraylist;
    }

    private static <T extends Entity> List<T> filterResults(Map<String, String> map, Class<? extends T> oclass, List<Predicate<Entity>> list, String s, World world, BlockPos blockposition) {
        ArrayList arraylist = Lists.newArrayList();
        String s1 = getArgument(map, EntitySelector.ARGUMENT_ENTITY_TYPE);

        s1 = s1 != null && s1.startsWith("!") ? s1.substring(1) : s1;
        boolean flag = !s.equals("e");
        boolean flag1 = s.equals("r") && s1 != null;
        int i = getInt(map, EntitySelector.ARGUMENT_DELTA_X, 0);
        int j = getInt(map, EntitySelector.ARGUMENT_DELTA_Y, 0);
        int k = getInt(map, EntitySelector.ARGUMENT_DELTA_Z, 0);
        int l = getInt(map, EntitySelector.ARGUMENT_RANGE_MAX, -1);
        Predicate predicate = Predicates.and(list);
        Predicate predicate1 = Predicates.and(EntitySelectors.IS_ALIVE, predicate);
        final AxisAlignedBB axisalignedbb;

        if (!map.containsKey(EntitySelector.ARGUMENT_DELTA_X) && !map.containsKey(EntitySelector.ARGUMENT_DELTA_Y) && !map.containsKey(EntitySelector.ARGUMENT_DELTA_Z)) {
            if (l >= 0) {
                axisalignedbb = new AxisAlignedBB((double) (blockposition.getX() - l), (double) (blockposition.getY() - l), (double) (blockposition.getZ() - l), (double) (blockposition.getX() + l + 1), (double) (blockposition.getY() + l + 1), (double) (blockposition.getZ() + l + 1));
                if (flag && !flag1) {
                    arraylist.addAll(world.getPlayers(oclass, predicate1));
                } else {
                    arraylist.addAll(world.getEntitiesWithinAABB(oclass, axisalignedbb, predicate1));
                }
            } else if (s.equals("a")) {
                arraylist.addAll(world.getPlayers(oclass, predicate));
            } else if (!s.equals("p") && (!s.equals("r") || flag1)) {
                arraylist.addAll(world.getEntities(oclass, predicate1));
            } else {
                arraylist.addAll(world.getPlayers(oclass, predicate1));
            }
        } else {
            axisalignedbb = getAABB(blockposition, i, j, k);
            if (flag && !flag1) {
                Predicate predicate2 = new Predicate() {
                    public boolean a(@Nullable Entity entity) {
                        return entity != null && axisalignedbb.intersects(entity.getEntityBoundingBox());
                    }

                    public boolean apply(@Nullable Object object) {
                        return this.a((Entity) object);
                    }
                };

                arraylist.addAll(world.getPlayers(oclass, Predicates.and(predicate1, predicate2)));
            } else {
                arraylist.addAll(world.getEntitiesWithinAABB(oclass, axisalignedbb, predicate1));
            }
        }

        return arraylist;
    }

    private static <T extends Entity> List<T> getEntitiesFromPredicates(List<T> list, Map<String, String> map, ICommandSender icommandlistener, Class<? extends T> oclass, String s, final Vec3d vec3d) {
        int i = getInt(map, EntitySelector.ARGUMENT_COUNT, !s.equals("a") && !s.equals("e") ? 1 : 0);

        if (!s.equals("p") && !s.equals("a") && !s.equals("e")) {
            if (s.equals("r")) {
                Collections.shuffle((List) list);
            }
        } else {
            Collections.sort((List) list, new Comparator() {
                public int a(Entity entity, Entity entity1) {
                    return ComparisonChain.start().compare(entity.getDistanceSq(vec3d.x, vec3d.y, vec3d.z), entity1.getDistanceSq(vec3d.x, vec3d.y, vec3d.z)).result();
                }

                public int compare(Object object, Object object1) {
                    return this.a((Entity) object, (Entity) object1);
                }
            });
        }

        Entity entity = icommandlistener.getCommandSenderEntity();

        if (entity != null && oclass.isAssignableFrom(entity.getClass()) && i == 1 && ((List) list).contains(entity) && !"r".equals(s)) {
            list = Lists.newArrayList(new Entity[] { entity});
        }

        if (i != 0) {
            if (i < 0) {
                Collections.reverse((List) list);
            }

            list = ((List) list).subList(0, Math.min(Math.abs(i), ((List) list).size()));
        }

        return (List) list;
    }

    private static AxisAlignedBB getAABB(BlockPos blockposition, int i, int j, int k) {
        boolean flag = i < 0;
        boolean flag1 = j < 0;
        boolean flag2 = k < 0;
        int l = blockposition.getX() + (flag ? i : 0);
        int i1 = blockposition.getY() + (flag1 ? j : 0);
        int j1 = blockposition.getZ() + (flag2 ? k : 0);
        int k1 = blockposition.getX() + (flag ? 0 : i) + 1;
        int l1 = blockposition.getY() + (flag1 ? 0 : j) + 1;
        int i2 = blockposition.getZ() + (flag2 ? 0 : k) + 1;

        return new AxisAlignedBB((double) l, (double) i1, (double) j1, (double) k1, (double) l1, (double) i2);
    }

    private static BlockPos getBlockPosFromArguments(Map<String, String> map, BlockPos blockposition) {
        return new BlockPos(getInt(map, EntitySelector.ARGUMENT_COORDINATE_X, blockposition.getX()), getInt(map, EntitySelector.ARGUMENT_COORDINATE_Y, blockposition.getY()), getInt(map, EntitySelector.ARGUMENT_COORDINATE_Z, blockposition.getZ()));
    }

    private static Vec3d getPosFromArguments(Map<String, String> map, Vec3d vec3d) {
        return new Vec3d(getCoordinate(map, EntitySelector.ARGUMENT_COORDINATE_X, vec3d.x, true), getCoordinate(map, EntitySelector.ARGUMENT_COORDINATE_Y, vec3d.y, false), getCoordinate(map, EntitySelector.ARGUMENT_COORDINATE_Z, vec3d.z, true));
    }

    private static double getCoordinate(Map<String, String> map, String s, double d0, boolean flag) {
        return map.containsKey(s) ? (double) MathHelper.getInt((String) map.get(s), MathHelper.floor(d0)) + (flag ? 0.5D : 0.0D) : d0;
    }

    private static boolean hasArgument(Map<String, String> map) {
        Iterator iterator = EntitySelector.WORLD_BINDING_ARGS.iterator();

        String s;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            s = (String) iterator.next();
        } while (!map.containsKey(s));

        return true;
    }

    private static int getInt(Map<String, String> map, String s, int i) {
        return map.containsKey(s) ? MathHelper.getInt((String) map.get(s), i) : i;
    }

    @Nullable
    private static String getArgument(Map<String, String> map, String s) {
        return (String) map.get(s);
    }

    public static Map<String, Integer> getScoreMap(Map<String, String> map) {
        HashMap hashmap = Maps.newHashMap();
        Iterator iterator = map.keySet().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            if (s.startsWith("score_") && s.length() > "score_".length()) {
                hashmap.put(s.substring("score_".length()), Integer.valueOf(MathHelper.getInt((String) map.get(s), 1)));
            }
        }

        return hashmap;
    }

    public static boolean matchesMultiplePlayers(String s) throws CommandException {
        Matcher matcher = EntitySelector.TOKEN_PATTERN.matcher(s);

        if (!matcher.matches()) {
            return false;
        } else {
            Map map = getArgumentMap(matcher.group(2));
            String s1 = matcher.group(1);
            int i = !"a".equals(s1) && !"e".equals(s1) ? 1 : 0;

            return getInt(map, EntitySelector.ARGUMENT_COUNT, i) != 1;
        }
    }

    public static boolean isSelector(String s) {
        return EntitySelector.TOKEN_PATTERN.matcher(s).matches();
    }

    private static Map<String, String> getArgumentMap(@Nullable String s) throws CommandException {
        HashMap hashmap = Maps.newHashMap();

        if (s == null) {
            return hashmap;
        } else {
            Iterator iterator = EntitySelector.COMMA_SPLITTER.split(s).iterator();

            while (iterator.hasNext()) {
                String s1 = (String) iterator.next();
                Iterator iterator1 = EntitySelector.EQUAL_SPLITTER.split(s1).iterator();
                String s2 = (String) iterator1.next();

                if (!EntitySelector.IS_VALID_ARGUMENT.apply(s2)) {
                    throw new CommandException("commands.generic.selector_argument", new Object[] { s1});
                }

                hashmap.put(s2, iterator1.hasNext() ? (String) iterator1.next() : "");
            }

            return hashmap;
        }
    }
}
