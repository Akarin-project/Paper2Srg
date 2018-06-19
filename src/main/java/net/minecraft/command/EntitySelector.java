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

    private static final Pattern field_82389_a = Pattern.compile("^@([pares])(?:\\[([^ ]*)\\])?$");
    private static final Splitter field_190828_b = Splitter.on(',').omitEmptyStrings();
    private static final Splitter field_190829_c = Splitter.on('=').limit(2);
    private static final Set<String> field_190830_d = Sets.newHashSet();
    private static final String field_190831_e = func_190826_c("r");
    private static final String field_190832_f = func_190826_c("rm");
    private static final String field_190833_g = func_190826_c("l");
    private static final String field_190834_h = func_190826_c("lm");
    private static final String field_190835_i = func_190826_c("x");
    private static final String field_190836_j = func_190826_c("y");
    private static final String field_190837_k = func_190826_c("z");
    private static final String field_190838_l = func_190826_c("dx");
    private static final String field_190839_m = func_190826_c("dy");
    private static final String field_190840_n = func_190826_c("dz");
    private static final String field_190841_o = func_190826_c("rx");
    private static final String field_190842_p = func_190826_c("rxm");
    private static final String field_190843_q = func_190826_c("ry");
    private static final String field_190844_r = func_190826_c("rym");
    private static final String field_190845_s = func_190826_c("c");
    private static final String field_190846_t = func_190826_c("m");
    private static final String field_190847_u = func_190826_c("team");
    private static final String field_190848_v = func_190826_c("name");
    private static final String field_190849_w = func_190826_c("type");
    private static final String field_190850_x = func_190826_c("tag");
    private static final Predicate<String> field_190851_y = new Predicate() {
        public boolean a(@Nullable String s) {
            return s != null && (EntitySelector.field_190830_d.contains(s) || s.length() > "score_".length() && s.startsWith("score_"));
        }

        public boolean apply(@Nullable Object object) {
            return this.a((String) object);
        }
    };
    private static final Set<String> field_179666_d = Sets.newHashSet(new String[] { EntitySelector.field_190835_i, EntitySelector.field_190836_j, EntitySelector.field_190837_k, EntitySelector.field_190838_l, EntitySelector.field_190839_m, EntitySelector.field_190840_n, EntitySelector.field_190832_f, EntitySelector.field_190831_e});

    private static String func_190826_c(String s) {
        EntitySelector.field_190830_d.add(s);
        return s;
    }

    @Nullable
    public static EntityPlayerMP func_82386_a(ICommandSender icommandlistener, String s) throws CommandException {
        return (EntityPlayerMP) func_179652_a(icommandlistener, s, EntityPlayerMP.class);
    }

    public static List<EntityPlayerMP> func_193531_b(ICommandSender icommandlistener, String s) throws CommandException {
        return func_179656_b(icommandlistener, s, EntityPlayerMP.class);
    }

    @Nullable
    public static <T extends Entity> T func_179652_a(ICommandSender icommandlistener, String s, Class<? extends T> oclass) throws CommandException {
        List list = func_179656_b(icommandlistener, s, oclass);

        return list.size() == 1 ? (Entity) list.get(0) : null;
    }

    @Nullable
    public static ITextComponent func_150869_b(ICommandSender icommandlistener, String s) throws CommandException {
        List list = func_179656_b(icommandlistener, s, Entity.class);

        if (list.isEmpty()) {
            return null;
        } else {
            ArrayList arraylist = Lists.newArrayList();
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                arraylist.add(entity.func_145748_c_());
            }

            return CommandBase.func_180530_a((List) arraylist);
        }
    }

    public static <T extends Entity> List<T> func_179656_b(ICommandSender icommandlistener, String s, Class<? extends T> oclass) throws CommandException {
        Matcher matcher = EntitySelector.field_82389_a.matcher(s);

        if (matcher.matches() && icommandlistener.func_70003_b(1, "@")) {
            Map map = func_82381_h(matcher.group(2));

            if (!func_179655_b(icommandlistener, map)) {
                return Collections.emptyList();
            } else {
                String s1 = matcher.group(1);
                BlockPos blockposition = func_179664_b(map, icommandlistener.func_180425_c());
                Vec3d vec3d = func_189210_b(map, icommandlistener.func_174791_d());
                List list = func_179654_a(icommandlistener, map);
                ArrayList arraylist = Lists.newArrayList();
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    World world = (World) iterator.next();

                    if (world != null) {
                        ArrayList arraylist1 = Lists.newArrayList();

                        arraylist1.addAll(func_179663_a(map, s1));
                        arraylist1.addAll(func_179648_b(map));
                        arraylist1.addAll(func_179649_c(map));
                        arraylist1.addAll(func_179659_d(map));
                        arraylist1.addAll(func_184952_c(icommandlistener, map));
                        arraylist1.addAll(func_179647_f(map));
                        arraylist1.addAll(func_184951_f(map));
                        arraylist1.addAll(func_180698_a(map, vec3d));
                        arraylist1.addAll(func_179662_g(map));
                        if ("s".equalsIgnoreCase(s1)) {
                            Entity entity = icommandlistener.func_174793_f();

                            if (entity != null && oclass.isAssignableFrom(entity.getClass())) {
                                if (map.containsKey(EntitySelector.field_190838_l) || map.containsKey(EntitySelector.field_190839_m) || map.containsKey(EntitySelector.field_190840_n)) {
                                    int i = func_179653_a(map, EntitySelector.field_190838_l, 0);
                                    int j = func_179653_a(map, EntitySelector.field_190839_m, 0);
                                    int k = func_179653_a(map, EntitySelector.field_190840_n, 0);
                                    AxisAlignedBB axisalignedbb = func_179661_a(blockposition, i, j, k);

                                    if (!axisalignedbb.func_72326_a(entity.func_174813_aQ())) {
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

                        arraylist.addAll(func_179660_a(map, oclass, (List) arraylist1, s1, world, blockposition));
                    }
                }

                return func_179658_a((List) arraylist, map, icommandlistener, oclass, s1, vec3d);
            }
        } else {
            return Collections.emptyList();
        }
    }

    private static List<World> func_179654_a(ICommandSender icommandlistener, Map<String, String> map) {
        ArrayList arraylist = Lists.newArrayList();

        if (func_179665_h(map)) {
            arraylist.add(icommandlistener.func_130014_f_());
        } else {
            Collections.addAll(arraylist, icommandlistener.func_184102_h().field_71305_c);
        }

        return arraylist;
    }

    private static <T extends Entity> boolean func_179655_b(ICommandSender icommandlistener, Map<String, String> map) {
        String s = func_179651_b(map, EntitySelector.field_190849_w);

        if (s == null) {
            return true;
        } else {
            ResourceLocation minecraftkey = new ResourceLocation(s.startsWith("!") ? s.substring(1) : s);

            if (EntityList.func_180125_b(minecraftkey)) {
                return true;
            } else {
                TextComponentTranslation chatmessage = new TextComponentTranslation("commands.generic.entity.invalidType", new Object[] { minecraftkey});

                chatmessage.func_150256_b().func_150238_a(TextFormatting.RED);
                icommandlistener.func_145747_a(chatmessage);
                return false;
            }
        }
    }

    private static List<Predicate<Entity>> func_179663_a(Map<String, String> map, String s) {
        String s1 = func_179651_b(map, EntitySelector.field_190849_w);

        if (s1 != null && (s.equals("e") || s.equals("r") || s.equals("s"))) {
            final boolean flag = s1.startsWith("!");
            final ResourceLocation minecraftkey = new ResourceLocation(flag ? s1.substring(1) : s1);

            return Collections.singletonList(new Predicate() {
                public boolean a(@Nullable Entity entity) {
                    return EntityList.func_180123_a(entity, minecraftkey) != flag;
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

    private static List<Predicate<Entity>> func_179648_b(Map<String, String> map) {
        ArrayList arraylist = Lists.newArrayList();
        final int i = func_179653_a(map, EntitySelector.field_190834_h, -1);
        final int j = func_179653_a(map, EntitySelector.field_190833_g, -1);

        if (i > -1 || j > -1) {
            arraylist.add(new Predicate() {
                public boolean a(@Nullable Entity entity) {
                    if (!(entity instanceof EntityPlayerMP)) {
                        return false;
                    } else {
                        EntityPlayerMP entityplayer = (EntityPlayerMP) entity;

                        return (i <= -1 || entityplayer.field_71068_ca >= i) && (j <= -1 || entityplayer.field_71068_ca <= j);
                    }
                }

                public boolean apply(@Nullable Object object) {
                    return this.a((Entity) object);
                }
            });
        }

        return arraylist;
    }

    private static List<Predicate<Entity>> func_179649_c(Map<String, String> map) {
        ArrayList arraylist = Lists.newArrayList();
        String s = func_179651_b(map, EntitySelector.field_190846_t);

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

                enumgamemode = GameType.func_185329_a(i, GameType.NOT_SET);
            } catch (Throwable throwable) {
                enumgamemode = GameType.func_185328_a(s, GameType.NOT_SET);
            }

            arraylist.add(new Predicate() {
                public boolean a(@Nullable Entity entity) {
                    if (!(entity instanceof EntityPlayerMP)) {
                        return false;
                    } else {
                        EntityPlayerMP entityplayer = (EntityPlayerMP) entity;
                        GameType enumgamemode = entityplayer.field_71134_c.func_73081_b();

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

    private static List<Predicate<Entity>> func_179659_d(Map<String, String> map) {
        ArrayList arraylist = Lists.newArrayList();
        final String s = func_179651_b(map, EntitySelector.field_190847_u);
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
                        Team scoreboardteambase = entityliving.func_96124_cp();
                        String s = scoreboardteambase == null ? "" : scoreboardteambase.func_96661_b();

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

    private static List<Predicate<Entity>> func_184952_c(final ICommandSender icommandlistener, Map<String, String> map) {
        final Map map1 = func_96560_a(map);

        return (List) (map1.isEmpty() ? Collections.emptyList() : Lists.newArrayList(new Predicate[] { new Predicate() {
            public boolean a(@Nullable Entity entity) {
                if (entity == null) {
                    return false;
                } else {
                    Scoreboard scoreboard = icommandlistener.func_184102_h().func_71218_a(0).func_96441_U();
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

                        ScoreObjective scoreboardobjective = scoreboard.func_96518_b(s);

                        if (scoreboardobjective == null) {
                            return false;
                        }

                        String s1 = entity instanceof EntityPlayerMP ? entity.func_70005_c_() : entity.func_189512_bd();

                        if (!scoreboard.func_178819_b(s1, scoreboardobjective)) {
                            return false;
                        }

                        Score scoreboardscore = scoreboard.func_96529_a(s1, scoreboardobjective);

                        i = scoreboardscore.func_96652_c();
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

    private static List<Predicate<Entity>> func_179647_f(Map<String, String> map) {
        ArrayList arraylist = Lists.newArrayList();
        final String s = func_179651_b(map, EntitySelector.field_190848_v);
        final boolean flag = s != null && s.startsWith("!");

        if (flag) {
            s = s.substring(1);
        }

        if (s != null) {
            arraylist.add(new Predicate() {
                public boolean a(@Nullable Entity entity) {
                    return entity != null && entity.func_70005_c_().equals(s) != flag;
                }

                public boolean apply(@Nullable Object object) {
                    return this.a((Entity) object);
                }
            });
        }

        return arraylist;
    }

    private static List<Predicate<Entity>> func_184951_f(Map<String, String> map) {
        ArrayList arraylist = Lists.newArrayList();
        final String s = func_179651_b(map, EntitySelector.field_190850_x);
        final boolean flag = s != null && s.startsWith("!");

        if (flag) {
            s = s.substring(1);
        }

        if (s != null) {
            arraylist.add(new Predicate() {
                public boolean a(@Nullable Entity entity) {
                    return entity == null ? false : ("".equals(s) ? entity.func_184216_O().isEmpty() != flag : entity.func_184216_O().contains(s) != flag);
                }

                public boolean apply(@Nullable Object object) {
                    return this.a((Entity) object);
                }
            });
        }

        return arraylist;
    }

    private static List<Predicate<Entity>> func_180698_a(Map<String, String> map, final Vec3d vec3d) {
        double d0 = (double) func_179653_a(map, EntitySelector.field_190832_f, -1);
        double d1 = (double) func_179653_a(map, EntitySelector.field_190831_e, -1);
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
                        double d0 = vec3d.func_186679_c(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v);

                        return (flag || d0 >= d1) && (flag1 || d0 <= d2);
                    }
                }

                public boolean apply(@Nullable Object object) {
                    return this.a((Entity) object);
                }
            }});
        }
    }

    private static List<Predicate<Entity>> func_179662_g(Map<String, String> map) {
        ArrayList arraylist = Lists.newArrayList();
        final int i;
        final int j;

        if (map.containsKey(EntitySelector.field_190844_r) || map.containsKey(EntitySelector.field_190843_q)) {
            i = MathHelper.func_188209_b(func_179653_a(map, EntitySelector.field_190844_r, 0));
            j = MathHelper.func_188209_b(func_179653_a(map, EntitySelector.field_190843_q, 359));
            arraylist.add(new Predicate() {
                public boolean a(@Nullable Entity entity) {
                    if (entity == null) {
                        return false;
                    } else {
                        int i = MathHelper.func_188209_b(MathHelper.func_76141_d(entity.field_70177_z));

                        return j > field_190837_k ? i >= j || i <= field_190837_k : i >= j && i <= field_190837_k;
                    }
                }

                public boolean apply(@Nullable Object object) {
                    return this.a((Entity) object);
                }
            });
        }

        if (map.containsKey(EntitySelector.field_190842_p) || map.containsKey(EntitySelector.field_190841_o)) {
            i = MathHelper.func_188209_b(func_179653_a(map, EntitySelector.field_190842_p, 0));
            j = MathHelper.func_188209_b(func_179653_a(map, EntitySelector.field_190841_o, 359));
            arraylist.add(new Predicate() {
                public boolean a(@Nullable Entity entity) {
                    if (entity == null) {
                        return false;
                    } else {
                        int i = MathHelper.func_188209_b(MathHelper.func_76141_d(entity.field_70125_A));

                        return j > field_190837_k ? i >= j || i <= field_190837_k : i >= j && i <= field_190837_k;
                    }
                }

                public boolean apply(@Nullable Object object) {
                    return this.a((Entity) object);
                }
            });
        }

        return arraylist;
    }

    private static <T extends Entity> List<T> func_179660_a(Map<String, String> map, Class<? extends T> oclass, List<Predicate<Entity>> list, String s, World world, BlockPos blockposition) {
        ArrayList arraylist = Lists.newArrayList();
        String s1 = func_179651_b(map, EntitySelector.field_190849_w);

        s1 = s1 != null && s1.startsWith("!") ? s1.substring(1) : s1;
        boolean flag = !s.equals("e");
        boolean flag1 = s.equals("r") && s1 != null;
        int i = func_179653_a(map, EntitySelector.field_190838_l, 0);
        int j = func_179653_a(map, EntitySelector.field_190839_m, 0);
        int k = func_179653_a(map, EntitySelector.field_190840_n, 0);
        int l = func_179653_a(map, EntitySelector.field_190831_e, -1);
        Predicate predicate = Predicates.and(list);
        Predicate predicate1 = Predicates.and(EntitySelectors.field_94557_a, predicate);
        final AxisAlignedBB axisalignedbb;

        if (!map.containsKey(EntitySelector.field_190838_l) && !map.containsKey(EntitySelector.field_190839_m) && !map.containsKey(EntitySelector.field_190840_n)) {
            if (l >= 0) {
                axisalignedbb = new AxisAlignedBB((double) (blockposition.func_177958_n() - l), (double) (blockposition.func_177956_o() - l), (double) (blockposition.func_177952_p() - l), (double) (blockposition.func_177958_n() + l + 1), (double) (blockposition.func_177956_o() + l + 1), (double) (blockposition.func_177952_p() + l + 1));
                if (flag && !flag1) {
                    arraylist.addAll(world.func_175661_b(oclass, predicate1));
                } else {
                    arraylist.addAll(world.func_175647_a(oclass, axisalignedbb, predicate1));
                }
            } else if (s.equals("a")) {
                arraylist.addAll(world.func_175661_b(oclass, predicate));
            } else if (!s.equals("p") && (!s.equals("r") || flag1)) {
                arraylist.addAll(world.func_175644_a(oclass, predicate1));
            } else {
                arraylist.addAll(world.func_175661_b(oclass, predicate1));
            }
        } else {
            axisalignedbb = func_179661_a(blockposition, i, j, k);
            if (flag && !flag1) {
                Predicate predicate2 = new Predicate() {
                    public boolean a(@Nullable Entity entity) {
                        return entity != null && axisalignedbb.func_72326_a(entity.func_174813_aQ());
                    }

                    public boolean apply(@Nullable Object object) {
                        return this.a((Entity) object);
                    }
                };

                arraylist.addAll(world.func_175661_b(oclass, Predicates.and(predicate1, predicate2)));
            } else {
                arraylist.addAll(world.func_175647_a(oclass, axisalignedbb, predicate1));
            }
        }

        return arraylist;
    }

    private static <T extends Entity> List<T> func_179658_a(List<T> list, Map<String, String> map, ICommandSender icommandlistener, Class<? extends T> oclass, String s, final Vec3d vec3d) {
        int i = func_179653_a(map, EntitySelector.field_190845_s, !s.equals("a") && !s.equals("e") ? 1 : 0);

        if (!s.equals("p") && !s.equals("a") && !s.equals("e")) {
            if (s.equals("r")) {
                Collections.shuffle((List) list);
            }
        } else {
            Collections.sort((List) list, new Comparator() {
                public int a(Entity entity, Entity entity1) {
                    return ComparisonChain.start().compare(entity.func_70092_e(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c), entity1.func_70092_e(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c)).result();
                }

                public int compare(Object object, Object object1) {
                    return this.a((Entity) object, (Entity) object1);
                }
            });
        }

        Entity entity = icommandlistener.func_174793_f();

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

    private static AxisAlignedBB func_179661_a(BlockPos blockposition, int i, int j, int k) {
        boolean flag = i < 0;
        boolean flag1 = j < 0;
        boolean flag2 = k < 0;
        int l = blockposition.func_177958_n() + (flag ? i : 0);
        int i1 = blockposition.func_177956_o() + (flag1 ? j : 0);
        int j1 = blockposition.func_177952_p() + (flag2 ? k : 0);
        int k1 = blockposition.func_177958_n() + (flag ? 0 : i) + 1;
        int l1 = blockposition.func_177956_o() + (flag1 ? 0 : j) + 1;
        int i2 = blockposition.func_177952_p() + (flag2 ? 0 : k) + 1;

        return new AxisAlignedBB((double) l, (double) i1, (double) j1, (double) k1, (double) l1, (double) i2);
    }

    private static BlockPos func_179664_b(Map<String, String> map, BlockPos blockposition) {
        return new BlockPos(func_179653_a(map, EntitySelector.field_190835_i, blockposition.func_177958_n()), func_179653_a(map, EntitySelector.field_190836_j, blockposition.func_177956_o()), func_179653_a(map, EntitySelector.field_190837_k, blockposition.func_177952_p()));
    }

    private static Vec3d func_189210_b(Map<String, String> map, Vec3d vec3d) {
        return new Vec3d(func_189211_a(map, EntitySelector.field_190835_i, vec3d.field_72450_a, true), func_189211_a(map, EntitySelector.field_190836_j, vec3d.field_72448_b, false), func_189211_a(map, EntitySelector.field_190837_k, vec3d.field_72449_c, true));
    }

    private static double func_189211_a(Map<String, String> map, String s, double d0, boolean flag) {
        return map.containsKey(s) ? (double) MathHelper.func_82715_a((String) map.get(s), MathHelper.func_76128_c(d0)) + (flag ? 0.5D : 0.0D) : d0;
    }

    private static boolean func_179665_h(Map<String, String> map) {
        Iterator iterator = EntitySelector.field_179666_d.iterator();

        String s;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            s = (String) iterator.next();
        } while (!map.containsKey(s));

        return true;
    }

    private static int func_179653_a(Map<String, String> map, String s, int i) {
        return map.containsKey(s) ? MathHelper.func_82715_a((String) map.get(s), i) : i;
    }

    @Nullable
    private static String func_179651_b(Map<String, String> map, String s) {
        return (String) map.get(s);
    }

    public static Map<String, Integer> func_96560_a(Map<String, String> map) {
        HashMap hashmap = Maps.newHashMap();
        Iterator iterator = map.keySet().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            if (s.startsWith("score_") && s.length() > "score_".length()) {
                hashmap.put(s.substring("score_".length()), Integer.valueOf(MathHelper.func_82715_a((String) map.get(s), 1)));
            }
        }

        return hashmap;
    }

    public static boolean func_82377_a(String s) throws CommandException {
        Matcher matcher = EntitySelector.field_82389_a.matcher(s);

        if (!matcher.matches()) {
            return false;
        } else {
            Map map = func_82381_h(matcher.group(2));
            String s1 = matcher.group(1);
            int i = !"a".equals(s1) && !"e".equals(s1) ? 1 : 0;

            return func_179653_a(map, EntitySelector.field_190845_s, i) != 1;
        }
    }

    public static boolean func_82378_b(String s) {
        return EntitySelector.field_82389_a.matcher(s).matches();
    }

    private static Map<String, String> func_82381_h(@Nullable String s) throws CommandException {
        HashMap hashmap = Maps.newHashMap();

        if (s == null) {
            return hashmap;
        } else {
            Iterator iterator = EntitySelector.field_190828_b.split(s).iterator();

            while (iterator.hasNext()) {
                String s1 = (String) iterator.next();
                Iterator iterator1 = EntitySelector.field_190829_c.split(s1).iterator();
                String s2 = (String) iterator1.next();

                if (!EntitySelector.field_190851_y.apply(s2)) {
                    throw new CommandException("commands.generic.selector_argument", new Object[] { s1});
                }

                hashmap.put(s2, iterator1.hasNext() ? (String) iterator1.next() : "");
            }

            return hashmap;
        }
    }
}
