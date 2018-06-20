package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

public class WoodlandMansionPieces {

    public static void func_191153_a() {
        MapGenStructureIO.func_143031_a(WoodlandMansionPieces.i.class, "WMP");
    }

    public static void func_191152_a(TemplateManager definedstructuremanager, BlockPos blockposition, Rotation enumblockrotation, List<WoodlandMansionPieces.i> list, Random random) {
        WoodlandMansionPieces.c worldgenwoodlandmansionpieces_c = new WoodlandMansionPieces.c(random);
        WoodlandMansionPieces.d worldgenwoodlandmansionpieces_d = new WoodlandMansionPieces.d(definedstructuremanager, random);

        worldgenwoodlandmansionpieces_d.a(blockposition, enumblockrotation, list, worldgenwoodlandmansionpieces_c);
    }

    static class h extends WoodlandMansionPieces.f {

        private h() {
            super(null);
        }

        h(Object object) {
            this();
        }
    }

    static class f extends WoodlandMansionPieces.b {

        private f() {
            super(null);
        }

        @Override
        public String a(Random random) {
            return "1x1_b" + (random.nextInt(4) + 1);
        }

        @Override
        public String b(Random random) {
            return "1x1_as" + (random.nextInt(4) + 1);
        }

        @Override
        public String a(Random random, boolean flag) {
            return flag ? "1x2_c_stairs" : "1x2_c" + (random.nextInt(4) + 1);
        }

        @Override
        public String b(Random random, boolean flag) {
            return flag ? "1x2_d_stairs" : "1x2_d" + (random.nextInt(5) + 1);
        }

        @Override
        public String c(Random random) {
            return "1x2_se" + (random.nextInt(1) + 1);
        }

        @Override
        public String d(Random random) {
            return "2x2_b" + (random.nextInt(5) + 1);
        }

        @Override
        public String e(Random random) {
            return "2x2_s1";
        }

        f(Object object) {
            this();
        }
    }

    static class a extends WoodlandMansionPieces.b {

        private a() {
            super(null);
        }

        @Override
        public String a(Random random) {
            return "1x1_a" + (random.nextInt(5) + 1);
        }

        @Override
        public String b(Random random) {
            return "1x1_as" + (random.nextInt(4) + 1);
        }

        @Override
        public String a(Random random, boolean flag) {
            return "1x2_a" + (random.nextInt(9) + 1);
        }

        @Override
        public String b(Random random, boolean flag) {
            return "1x2_b" + (random.nextInt(5) + 1);
        }

        @Override
        public String c(Random random) {
            return "1x2_s" + (random.nextInt(2) + 1);
        }

        @Override
        public String d(Random random) {
            return "2x2_a" + (random.nextInt(4) + 1);
        }

        @Override
        public String e(Random random) {
            return "2x2_s1";
        }

        a(Object object) {
            this();
        }
    }

    abstract static class b {

        private b() {}

        public abstract String a(Random random);

        public abstract String b(Random random);

        public abstract String a(Random random, boolean flag);

        public abstract String b(Random random, boolean flag);

        public abstract String c(Random random);

        public abstract String d(Random random);

        public abstract String e(Random random);

        b(Object object) {
            this();
        }
    }

    static class g {

        private final int[][] a;
        private final int b;
        private final int c;
        private final int d;

        public g(int i, int j, int k) {
            this.b = i;
            this.c = j;
            this.d = k;
            this.a = new int[i][j];
        }

        public void a(int i, int j, int k) {
            if (i >= 0 && i < this.b && j >= 0 && j < this.c) {
                this.a[i][j] = k;
            }

        }

        public void a(int i, int j, int k, int l, int i1) {
            for (int j1 = j; j1 <= l; ++j1) {
                for (int k1 = i; k1 <= k; ++k1) {
                    this.a(k1, j1, i1);
                }
            }

        }

        public int a(int i, int j) {
            return i >= 0 && i < this.b && j >= 0 && j < this.c ? this.a[i][j] : this.d;
        }

        public void a(int i, int j, int k, int l) {
            if (this.a(i, j) == k) {
                this.a(i, j, l);
            }

        }

        public boolean b(int i, int j, int k) {
            return this.a(i - 1, j) == k || this.a(i + 1, j) == k || this.a(i, j + 1) == k || this.a(i, j - 1) == k;
        }
    }

    static class c {

        private final Random a;
        private final WoodlandMansionPieces.g b;
        private final WoodlandMansionPieces.g c;
        private final WoodlandMansionPieces.g[] d;
        private final int e;
        private final int f;

        public c(Random random) {
            this.a = random;
            boolean flag = true;

            this.e = 7;
            this.f = 4;
            this.b = new WoodlandMansionPieces.g(11, 11, 5);
            this.b.a(this.e, this.f, this.e + 1, this.f + 1, 3);
            this.b.a(this.e - 1, this.f, this.e - 1, this.f + 1, 2);
            this.b.a(this.e + 2, this.f - 2, this.e + 3, this.f + 3, 5);
            this.b.a(this.e + 1, this.f - 2, this.e + 1, this.f - 1, 1);
            this.b.a(this.e + 1, this.f + 2, this.e + 1, this.f + 3, 1);
            this.b.a(this.e - 1, this.f - 1, 1);
            this.b.a(this.e - 1, this.f + 2, 1);
            this.b.a(0, 0, 11, 1, 5);
            this.b.a(0, 9, 11, 11, 5);
            this.a(this.b, this.e, this.f - 2, EnumFacing.WEST, 6);
            this.a(this.b, this.e, this.f + 3, EnumFacing.WEST, 6);
            this.a(this.b, this.e - 2, this.f - 1, EnumFacing.WEST, 3);
            this.a(this.b, this.e - 2, this.f + 2, EnumFacing.WEST, 3);

            while (this.a(this.b)) {
                ;
            }

            this.d = new WoodlandMansionPieces.g[3];
            this.d[0] = new WoodlandMansionPieces.g(11, 11, 5);
            this.d[1] = new WoodlandMansionPieces.g(11, 11, 5);
            this.d[2] = new WoodlandMansionPieces.g(11, 11, 5);
            this.a(this.b, this.d[0]);
            this.a(this.b, this.d[1]);
            this.d[0].a(this.e + 1, this.f, this.e + 1, this.f + 1, 8388608);
            this.d[1].a(this.e + 1, this.f, this.e + 1, this.f + 1, 8388608);
            this.c = new WoodlandMansionPieces.g(this.b.b, this.b.c, 5);
            this.b();
            this.a(this.c, this.d[2]);
        }

        public static boolean a(WoodlandMansionPieces.g worldgenwoodlandmansionpieces_g, int i, int j) {
            int k = worldgenwoodlandmansionpieces_g.a(i, j);

            return k == 1 || k == 2 || k == 3 || k == 4;
        }

        public boolean a(WoodlandMansionPieces.g worldgenwoodlandmansionpieces_g, int i, int j, int k, int l) {
            return (this.d[k].a(i, j) & '\uffff') == l;
        }

        @Nullable
        public EnumFacing b(WoodlandMansionPieces.g worldgenwoodlandmansionpieces_g, int i, int j, int k, int l) {
            EnumFacing[] aenumdirection = EnumFacing.Plane.HORIZONTAL.func_179516_a();
            int i1 = aenumdirection.length;

            for (int j1 = 0; j1 < i1; ++j1) {
                EnumFacing enumdirection = aenumdirection[j1];

                if (this.a(worldgenwoodlandmansionpieces_g, i + enumdirection.func_82601_c(), j + enumdirection.func_82599_e(), k, l)) {
                    return enumdirection;
                }
            }

            return null;
        }

        private void a(WoodlandMansionPieces.g worldgenwoodlandmansionpieces_g, int i, int j, EnumFacing enumdirection, int k) {
            if (k > 0) {
                worldgenwoodlandmansionpieces_g.a(i, j, 1);
                worldgenwoodlandmansionpieces_g.a(i + enumdirection.func_82601_c(), j + enumdirection.func_82599_e(), 0, 1);

                EnumFacing enumdirection1;

                for (int l = 0; l < 8; ++l) {
                    enumdirection1 = EnumFacing.func_176731_b(this.a.nextInt(4));
                    if (enumdirection1 != enumdirection.func_176734_d() && (enumdirection1 != EnumFacing.EAST || !this.a.nextBoolean())) {
                        int i1 = i + enumdirection.func_82601_c();
                        int j1 = j + enumdirection.func_82599_e();

                        if (worldgenwoodlandmansionpieces_g.a(i1 + enumdirection1.func_82601_c(), j1 + enumdirection1.func_82599_e()) == 0 && worldgenwoodlandmansionpieces_g.a(i1 + enumdirection1.func_82601_c() * 2, j1 + enumdirection1.func_82599_e() * 2) == 0) {
                            this.a(worldgenwoodlandmansionpieces_g, i + enumdirection.func_82601_c() + enumdirection1.func_82601_c(), j + enumdirection.func_82599_e() + enumdirection1.func_82599_e(), enumdirection1, k - 1);
                            break;
                        }
                    }
                }

                EnumFacing enumdirection2 = enumdirection.func_176746_e();

                enumdirection1 = enumdirection.func_176735_f();
                worldgenwoodlandmansionpieces_g.a(i + enumdirection2.func_82601_c(), j + enumdirection2.func_82599_e(), 0, 2);
                worldgenwoodlandmansionpieces_g.a(i + enumdirection1.func_82601_c(), j + enumdirection1.func_82599_e(), 0, 2);
                worldgenwoodlandmansionpieces_g.a(i + enumdirection.func_82601_c() + enumdirection2.func_82601_c(), j + enumdirection.func_82599_e() + enumdirection2.func_82599_e(), 0, 2);
                worldgenwoodlandmansionpieces_g.a(i + enumdirection.func_82601_c() + enumdirection1.func_82601_c(), j + enumdirection.func_82599_e() + enumdirection1.func_82599_e(), 0, 2);
                worldgenwoodlandmansionpieces_g.a(i + enumdirection.func_82601_c() * 2, j + enumdirection.func_82599_e() * 2, 0, 2);
                worldgenwoodlandmansionpieces_g.a(i + enumdirection2.func_82601_c() * 2, j + enumdirection2.func_82599_e() * 2, 0, 2);
                worldgenwoodlandmansionpieces_g.a(i + enumdirection1.func_82601_c() * 2, j + enumdirection1.func_82599_e() * 2, 0, 2);
            }
        }

        private boolean a(WoodlandMansionPieces.g worldgenwoodlandmansionpieces_g) {
            boolean flag = false;

            for (int i = 0; i < worldgenwoodlandmansionpieces_g.c; ++i) {
                for (int j = 0; j < worldgenwoodlandmansionpieces_g.b; ++j) {
                    if (worldgenwoodlandmansionpieces_g.a(j, i) == 0) {
                        byte b0 = 0;
                        int k = b0 + (a(worldgenwoodlandmansionpieces_g, j + 1, i) ? 1 : 0);

                        k += a(worldgenwoodlandmansionpieces_g, j - 1, i) ? 1 : 0;
                        k += a(worldgenwoodlandmansionpieces_g, j, i + 1) ? 1 : 0;
                        k += a(worldgenwoodlandmansionpieces_g, j, i - 1) ? 1 : 0;
                        if (k >= 3) {
                            worldgenwoodlandmansionpieces_g.a(j, i, 2);
                            flag = true;
                        } else if (k == 2) {
                            byte b1 = 0;
                            int l = b1 + (a(worldgenwoodlandmansionpieces_g, j + 1, i + 1) ? 1 : 0);

                            l += a(worldgenwoodlandmansionpieces_g, j - 1, i + 1) ? 1 : 0;
                            l += a(worldgenwoodlandmansionpieces_g, j + 1, i - 1) ? 1 : 0;
                            l += a(worldgenwoodlandmansionpieces_g, j - 1, i - 1) ? 1 : 0;
                            if (l <= 1) {
                                worldgenwoodlandmansionpieces_g.a(j, i, 2);
                                flag = true;
                            }
                        }
                    }
                }
            }

            return flag;
        }

        private void b() {
            ArrayList arraylist = Lists.newArrayList();
            WoodlandMansionPieces.g worldgenwoodlandmansionpieces_g = this.d[1];

            int i;
            int j;

            for (int k = 0; k < this.c.c; ++k) {
                for (i = 0; i < this.c.b; ++i) {
                    int l = worldgenwoodlandmansionpieces_g.a(i, k);

                    j = l & 983040;
                    if (j == 131072 && (l & 2097152) == 2097152) {
                        arraylist.add(new Tuple(Integer.valueOf(i), Integer.valueOf(k)));
                    }
                }
            }

            if (arraylist.isEmpty()) {
                this.c.a(0, 0, this.c.b, this.c.c, 5);
            } else {
                Tuple tuple = (Tuple) arraylist.get(this.a.nextInt(arraylist.size()));

                i = worldgenwoodlandmansionpieces_g.a(((Integer) tuple.func_76341_a()).intValue(), ((Integer) tuple.func_76340_b()).intValue());
                worldgenwoodlandmansionpieces_g.a(((Integer) tuple.func_76341_a()).intValue(), ((Integer) tuple.func_76340_b()).intValue(), i | 4194304);
                EnumFacing enumdirection = this.b(this.b, ((Integer) tuple.func_76341_a()).intValue(), ((Integer) tuple.func_76340_b()).intValue(), 1, i & '\uffff');

                j = ((Integer) tuple.func_76341_a()).intValue() + enumdirection.func_82601_c();
                int i1 = ((Integer) tuple.func_76340_b()).intValue() + enumdirection.func_82599_e();

                for (int j1 = 0; j1 < this.c.c; ++j1) {
                    for (int k1 = 0; k1 < this.c.b; ++k1) {
                        if (!a(this.b, k1, j1)) {
                            this.c.a(k1, j1, 5);
                        } else if (k1 == ((Integer) tuple.func_76341_a()).intValue() && j1 == ((Integer) tuple.func_76340_b()).intValue()) {
                            this.c.a(k1, j1, 3);
                        } else if (k1 == j && j1 == i1) {
                            this.c.a(k1, j1, 3);
                            this.d[2].a(k1, j1, 8388608);
                        }
                    }
                }

                ArrayList arraylist1 = Lists.newArrayList();
                EnumFacing[] aenumdirection = EnumFacing.Plane.HORIZONTAL.func_179516_a();
                int l1 = aenumdirection.length;

                for (int i2 = 0; i2 < l1; ++i2) {
                    EnumFacing enumdirection1 = aenumdirection[i2];

                    if (this.c.a(j + enumdirection1.func_82601_c(), i1 + enumdirection1.func_82599_e()) == 0) {
                        arraylist1.add(enumdirection1);
                    }
                }

                if (arraylist1.isEmpty()) {
                    this.c.a(0, 0, this.c.b, this.c.c, 5);
                    worldgenwoodlandmansionpieces_g.a(((Integer) tuple.func_76341_a()).intValue(), ((Integer) tuple.func_76340_b()).intValue(), i);
                } else {
                    EnumFacing enumdirection2 = (EnumFacing) arraylist1.get(this.a.nextInt(arraylist1.size()));

                    this.a(this.c, j + enumdirection2.func_82601_c(), i1 + enumdirection2.func_82599_e(), enumdirection2, 4);

                    while (this.a(this.c)) {
                        ;
                    }

                }
            }
        }

        private void a(WoodlandMansionPieces.g worldgenwoodlandmansionpieces_g, WoodlandMansionPieces.g worldgenwoodlandmansionpieces_g1) {
            ArrayList arraylist = Lists.newArrayList();

            int i;

            for (i = 0; i < worldgenwoodlandmansionpieces_g.c; ++i) {
                for (int j = 0; j < worldgenwoodlandmansionpieces_g.b; ++j) {
                    if (worldgenwoodlandmansionpieces_g.a(j, i) == 2) {
                        arraylist.add(new Tuple(Integer.valueOf(j), Integer.valueOf(i)));
                    }
                }
            }

            Collections.shuffle(arraylist, this.a);
            i = 10;
            Iterator iterator = arraylist.iterator();

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                int k = ((Integer) tuple.func_76341_a()).intValue();
                int l = ((Integer) tuple.func_76340_b()).intValue();

                if (worldgenwoodlandmansionpieces_g1.a(k, l) == 0) {
                    int i1 = k;
                    int j1 = k;
                    int k1 = l;
                    int l1 = l;
                    int i2 = 65536;

                    if (worldgenwoodlandmansionpieces_g1.a(k + 1, l) == 0 && worldgenwoodlandmansionpieces_g1.a(k, l + 1) == 0 && worldgenwoodlandmansionpieces_g1.a(k + 1, l + 1) == 0 && worldgenwoodlandmansionpieces_g.a(k + 1, l) == 2 && worldgenwoodlandmansionpieces_g.a(k, l + 1) == 2 && worldgenwoodlandmansionpieces_g.a(k + 1, l + 1) == 2) {
                        j1 = k + 1;
                        l1 = l + 1;
                        i2 = 262144;
                    } else if (worldgenwoodlandmansionpieces_g1.a(k - 1, l) == 0 && worldgenwoodlandmansionpieces_g1.a(k, l + 1) == 0 && worldgenwoodlandmansionpieces_g1.a(k - 1, l + 1) == 0 && worldgenwoodlandmansionpieces_g.a(k - 1, l) == 2 && worldgenwoodlandmansionpieces_g.a(k, l + 1) == 2 && worldgenwoodlandmansionpieces_g.a(k - 1, l + 1) == 2) {
                        i1 = k - 1;
                        l1 = l + 1;
                        i2 = 262144;
                    } else if (worldgenwoodlandmansionpieces_g1.a(k - 1, l) == 0 && worldgenwoodlandmansionpieces_g1.a(k, l - 1) == 0 && worldgenwoodlandmansionpieces_g1.a(k - 1, l - 1) == 0 && worldgenwoodlandmansionpieces_g.a(k - 1, l) == 2 && worldgenwoodlandmansionpieces_g.a(k, l - 1) == 2 && worldgenwoodlandmansionpieces_g.a(k - 1, l - 1) == 2) {
                        i1 = k - 1;
                        k1 = l - 1;
                        i2 = 262144;
                    } else if (worldgenwoodlandmansionpieces_g1.a(k + 1, l) == 0 && worldgenwoodlandmansionpieces_g.a(k + 1, l) == 2) {
                        j1 = k + 1;
                        i2 = 131072;
                    } else if (worldgenwoodlandmansionpieces_g1.a(k, l + 1) == 0 && worldgenwoodlandmansionpieces_g.a(k, l + 1) == 2) {
                        l1 = l + 1;
                        i2 = 131072;
                    } else if (worldgenwoodlandmansionpieces_g1.a(k - 1, l) == 0 && worldgenwoodlandmansionpieces_g.a(k - 1, l) == 2) {
                        i1 = k - 1;
                        i2 = 131072;
                    } else if (worldgenwoodlandmansionpieces_g1.a(k, l - 1) == 0 && worldgenwoodlandmansionpieces_g.a(k, l - 1) == 2) {
                        k1 = l - 1;
                        i2 = 131072;
                    }

                    int j2 = this.a.nextBoolean() ? i1 : j1;
                    int k2 = this.a.nextBoolean() ? k1 : l1;
                    int l2 = 2097152;

                    if (!worldgenwoodlandmansionpieces_g.b(j2, k2, 1)) {
                        j2 = j2 == i1 ? j1 : i1;
                        k2 = k2 == k1 ? l1 : k1;
                        if (!worldgenwoodlandmansionpieces_g.b(j2, k2, 1)) {
                            k2 = k2 == k1 ? l1 : k1;
                            if (!worldgenwoodlandmansionpieces_g.b(j2, k2, 1)) {
                                j2 = j2 == i1 ? j1 : i1;
                                k2 = k2 == k1 ? l1 : k1;
                                if (!worldgenwoodlandmansionpieces_g.b(j2, k2, 1)) {
                                    l2 = 0;
                                    j2 = i1;
                                    k2 = k1;
                                }
                            }
                        }
                    }

                    for (int i3 = k1; i3 <= l1; ++i3) {
                        for (int j3 = i1; j3 <= j1; ++j3) {
                            if (j3 == j2 && i3 == k2) {
                                worldgenwoodlandmansionpieces_g1.a(j3, i3, 1048576 | l2 | i2 | i);
                            } else {
                                worldgenwoodlandmansionpieces_g1.a(j3, i3, i2 | i);
                            }
                        }
                    }

                    ++i;
                }
            }

        }
    }

    static class d {

        private final TemplateManager a;
        private final Random b;
        private int c;
        private int d;

        public d(TemplateManager definedstructuremanager, Random random) {
            this.a = definedstructuremanager;
            this.b = random;
        }

        public void a(BlockPos blockposition, Rotation enumblockrotation, List<WoodlandMansionPieces.i> list, WoodlandMansionPieces.c worldgenwoodlandmansionpieces_c) {
            WoodlandMansionPieces.e worldgenwoodlandmansionpieces_e = new WoodlandMansionPieces.e(null);

            worldgenwoodlandmansionpieces_e.b = blockposition;
            worldgenwoodlandmansionpieces_e.a = enumblockrotation;
            worldgenwoodlandmansionpieces_e.c = "wall_flat";
            WoodlandMansionPieces.e worldgenwoodlandmansionpieces_e1 = new WoodlandMansionPieces.e(null);

            this.a(list, worldgenwoodlandmansionpieces_e);
            worldgenwoodlandmansionpieces_e1.b = worldgenwoodlandmansionpieces_e.b.func_177981_b(8);
            worldgenwoodlandmansionpieces_e1.a = worldgenwoodlandmansionpieces_e.a;
            worldgenwoodlandmansionpieces_e1.c = "wall_window";
            if (!list.isEmpty()) {
                ;
            }

            WoodlandMansionPieces.g worldgenwoodlandmansionpieces_g = worldgenwoodlandmansionpieces_c.b;
            WoodlandMansionPieces.g worldgenwoodlandmansionpieces_g1 = worldgenwoodlandmansionpieces_c.c;

            this.c = worldgenwoodlandmansionpieces_c.e + 1;
            this.d = worldgenwoodlandmansionpieces_c.f + 1;
            int i = worldgenwoodlandmansionpieces_c.e + 1;
            int j = worldgenwoodlandmansionpieces_c.f;

            this.a(list, worldgenwoodlandmansionpieces_e, worldgenwoodlandmansionpieces_g, EnumFacing.SOUTH, this.c, this.d, i, j);
            this.a(list, worldgenwoodlandmansionpieces_e1, worldgenwoodlandmansionpieces_g, EnumFacing.SOUTH, this.c, this.d, i, j);
            WoodlandMansionPieces.e worldgenwoodlandmansionpieces_e2 = new WoodlandMansionPieces.e(null);

            worldgenwoodlandmansionpieces_e2.b = worldgenwoodlandmansionpieces_e.b.func_177981_b(19);
            worldgenwoodlandmansionpieces_e2.a = worldgenwoodlandmansionpieces_e.a;
            worldgenwoodlandmansionpieces_e2.c = "wall_window";
            boolean flag = false;

            int k;

            for (int l = 0; l < worldgenwoodlandmansionpieces_g1.c && !flag; ++l) {
                for (k = worldgenwoodlandmansionpieces_g1.b - 1; k >= 0 && !flag; --k) {
                    if (WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g1, k, l)) {
                        worldgenwoodlandmansionpieces_e2.b = worldgenwoodlandmansionpieces_e2.b.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 8 + (l - this.d) * 8);
                        worldgenwoodlandmansionpieces_e2.b = worldgenwoodlandmansionpieces_e2.b.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), (k - this.c) * 8);
                        this.b(list, worldgenwoodlandmansionpieces_e2);
                        this.a(list, worldgenwoodlandmansionpieces_e2, worldgenwoodlandmansionpieces_g1, EnumFacing.SOUTH, k, l, k, l);
                        flag = true;
                    }
                }
            }

            this.a(list, blockposition.func_177981_b(16), enumblockrotation, worldgenwoodlandmansionpieces_g, worldgenwoodlandmansionpieces_g1);
            this.a(list, blockposition.func_177981_b(27), enumblockrotation, worldgenwoodlandmansionpieces_g1, (WoodlandMansionPieces.g) null);
            if (!list.isEmpty()) {
                ;
            }

            WoodlandMansionPieces.b[] aworldgenwoodlandmansionpieces_b = new WoodlandMansionPieces.b[] { new WoodlandMansionPieces.a(null), new WoodlandMansionPieces.f(null), new WoodlandMansionPieces.h(null)};

            for (k = 0; k < 3; ++k) {
                BlockPos blockposition1 = blockposition.func_177981_b(8 * k + (k == 2 ? 3 : 0));
                WoodlandMansionPieces.g worldgenwoodlandmansionpieces_g2 = worldgenwoodlandmansionpieces_c.d[k];
                WoodlandMansionPieces.g worldgenwoodlandmansionpieces_g3 = k == 2 ? worldgenwoodlandmansionpieces_g1 : worldgenwoodlandmansionpieces_g;
                String s = k == 0 ? "carpet_south" : "carpet_south_2";
                String s1 = k == 0 ? "carpet_west" : "carpet_west_2";

                for (int i1 = 0; i1 < worldgenwoodlandmansionpieces_g3.c; ++i1) {
                    for (int j1 = 0; j1 < worldgenwoodlandmansionpieces_g3.b; ++j1) {
                        if (worldgenwoodlandmansionpieces_g3.a(j1, i1) == 1) {
                            BlockPos blockposition2 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 8 + (i1 - this.d) * 8);

                            blockposition2 = blockposition2.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), (j1 - this.c) * 8);
                            list.add(new WoodlandMansionPieces.i(this.a, "corridor_floor", blockposition2, enumblockrotation));
                            if (worldgenwoodlandmansionpieces_g3.a(j1, i1 - 1) == 1 || (worldgenwoodlandmansionpieces_g2.a(j1, i1 - 1) & 8388608) == 8388608) {
                                list.add(new WoodlandMansionPieces.i(this.a, "carpet_north", blockposition2.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 1).func_177984_a(), enumblockrotation));
                            }

                            if (worldgenwoodlandmansionpieces_g3.a(j1 + 1, i1) == 1 || (worldgenwoodlandmansionpieces_g2.a(j1 + 1, i1) & 8388608) == 8388608) {
                                list.add(new WoodlandMansionPieces.i(this.a, "carpet_east", blockposition2.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 1).func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 5).func_177984_a(), enumblockrotation));
                            }

                            if (worldgenwoodlandmansionpieces_g3.a(j1, i1 + 1) == 1 || (worldgenwoodlandmansionpieces_g2.a(j1, i1 + 1) & 8388608) == 8388608) {
                                list.add(new WoodlandMansionPieces.i(this.a, s, blockposition2.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 5).func_177967_a(enumblockrotation.func_185831_a(EnumFacing.WEST), 1), enumblockrotation));
                            }

                            if (worldgenwoodlandmansionpieces_g3.a(j1 - 1, i1) == 1 || (worldgenwoodlandmansionpieces_g2.a(j1 - 1, i1) & 8388608) == 8388608) {
                                list.add(new WoodlandMansionPieces.i(this.a, s1, blockposition2.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.WEST), 1).func_177967_a(enumblockrotation.func_185831_a(EnumFacing.NORTH), 1), enumblockrotation));
                            }
                        }
                    }
                }

                String s2 = k == 0 ? "indoors_wall" : "indoors_wall_2";
                String s3 = k == 0 ? "indoors_door" : "indoors_door_2";
                ArrayList arraylist = Lists.newArrayList();

                for (int k1 = 0; k1 < worldgenwoodlandmansionpieces_g3.c; ++k1) {
                    for (int l1 = 0; l1 < worldgenwoodlandmansionpieces_g3.b; ++l1) {
                        boolean flag1 = k == 2 && worldgenwoodlandmansionpieces_g3.a(l1, k1) == 3;

                        if (worldgenwoodlandmansionpieces_g3.a(l1, k1) == 2 || flag1) {
                            int i2 = worldgenwoodlandmansionpieces_g2.a(l1, k1);
                            int j2 = i2 & 983040;
                            int k2 = i2 & '\uffff';

                            flag1 = flag1 && (i2 & 8388608) == 8388608;
                            arraylist.clear();
                            if ((i2 & 2097152) == 2097152) {
                                EnumFacing[] aenumdirection = EnumFacing.Plane.HORIZONTAL.func_179516_a();
                                int l2 = aenumdirection.length;

                                for (int i3 = 0; i3 < l2; ++i3) {
                                    EnumFacing enumdirection = aenumdirection[i3];

                                    if (worldgenwoodlandmansionpieces_g3.a(l1 + enumdirection.func_82601_c(), k1 + enumdirection.func_82599_e()) == 1) {
                                        arraylist.add(enumdirection);
                                    }
                                }
                            }

                            EnumFacing enumdirection1 = null;

                            if (!arraylist.isEmpty()) {
                                enumdirection1 = (EnumFacing) arraylist.get(this.b.nextInt(arraylist.size()));
                            } else if ((i2 & 1048576) == 1048576) {
                                enumdirection1 = EnumFacing.UP;
                            }

                            BlockPos blockposition3 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 8 + (k1 - this.d) * 8);

                            blockposition3 = blockposition3.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), -1 + (l1 - this.c) * 8);
                            if (WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g3, l1 - 1, k1) && !worldgenwoodlandmansionpieces_c.a(worldgenwoodlandmansionpieces_g3, l1 - 1, k1, k, k2)) {
                                list.add(new WoodlandMansionPieces.i(this.a, enumdirection1 == EnumFacing.WEST ? s3 : s2, blockposition3, enumblockrotation));
                            }

                            BlockPos blockposition4;

                            if (worldgenwoodlandmansionpieces_g3.a(l1 + 1, k1) == 1 && !flag1) {
                                blockposition4 = blockposition3.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 8);
                                list.add(new WoodlandMansionPieces.i(this.a, enumdirection1 == EnumFacing.EAST ? s3 : s2, blockposition4, enumblockrotation));
                            }

                            if (WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g3, l1, k1 + 1) && !worldgenwoodlandmansionpieces_c.a(worldgenwoodlandmansionpieces_g3, l1, k1 + 1, k, k2)) {
                                blockposition4 = blockposition3.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 7);
                                blockposition4 = blockposition4.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 7);
                                list.add(new WoodlandMansionPieces.i(this.a, enumdirection1 == EnumFacing.SOUTH ? s3 : s2, blockposition4, enumblockrotation.func_185830_a(Rotation.CLOCKWISE_90)));
                            }

                            if (worldgenwoodlandmansionpieces_g3.a(l1, k1 - 1) == 1 && !flag1) {
                                blockposition4 = blockposition3.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.NORTH), 1);
                                blockposition4 = blockposition4.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 7);
                                list.add(new WoodlandMansionPieces.i(this.a, enumdirection1 == EnumFacing.NORTH ? s3 : s2, blockposition4, enumblockrotation.func_185830_a(Rotation.CLOCKWISE_90)));
                            }

                            if (j2 == 65536) {
                                this.a(list, blockposition3, enumblockrotation, enumdirection1, aworldgenwoodlandmansionpieces_b[k]);
                            } else {
                                EnumFacing enumdirection2;

                                if (j2 == 131072 && enumdirection1 != null) {
                                    enumdirection2 = worldgenwoodlandmansionpieces_c.b(worldgenwoodlandmansionpieces_g3, l1, k1, k, k2);
                                    boolean flag2 = (i2 & 4194304) == 4194304;

                                    this.a(list, blockposition3, enumblockrotation, enumdirection2, enumdirection1, aworldgenwoodlandmansionpieces_b[k], flag2);
                                } else if (j2 == 262144 && enumdirection1 != null && enumdirection1 != EnumFacing.UP) {
                                    enumdirection2 = enumdirection1.func_176746_e();
                                    if (!worldgenwoodlandmansionpieces_c.a(worldgenwoodlandmansionpieces_g3, l1 + enumdirection2.func_82601_c(), k1 + enumdirection2.func_82599_e(), k, k2)) {
                                        enumdirection2 = enumdirection2.func_176734_d();
                                    }

                                    this.a(list, blockposition3, enumblockrotation, enumdirection2, enumdirection1, aworldgenwoodlandmansionpieces_b[k]);
                                } else if (j2 == 262144 && enumdirection1 == EnumFacing.UP) {
                                    this.a(list, blockposition3, enumblockrotation, aworldgenwoodlandmansionpieces_b[k]);
                                }
                            }
                        }
                    }
                }
            }

        }

        private void a(List<WoodlandMansionPieces.i> list, WoodlandMansionPieces.e worldgenwoodlandmansionpieces_e, WoodlandMansionPieces.g worldgenwoodlandmansionpieces_g, EnumFacing enumdirection, int i, int j, int k, int l) {
            int i1 = i;
            int j1 = j;
            EnumFacing enumdirection1 = enumdirection;

            do {
                if (!WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, i1 + enumdirection.func_82601_c(), j1 + enumdirection.func_82599_e())) {
                    this.c(list, worldgenwoodlandmansionpieces_e);
                    enumdirection = enumdirection.func_176746_e();
                    if (i1 != k || j1 != l || enumdirection1 != enumdirection) {
                        this.b(list, worldgenwoodlandmansionpieces_e);
                    }
                } else if (WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, i1 + enumdirection.func_82601_c(), j1 + enumdirection.func_82599_e()) && WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, i1 + enumdirection.func_82601_c() + enumdirection.func_176735_f().func_82601_c(), j1 + enumdirection.func_82599_e() + enumdirection.func_176735_f().func_82599_e())) {
                    this.d(list, worldgenwoodlandmansionpieces_e);
                    i1 += enumdirection.func_82601_c();
                    j1 += enumdirection.func_82599_e();
                    enumdirection = enumdirection.func_176735_f();
                } else {
                    i1 += enumdirection.func_82601_c();
                    j1 += enumdirection.func_82599_e();
                    if (i1 != k || j1 != l || enumdirection1 != enumdirection) {
                        this.b(list, worldgenwoodlandmansionpieces_e);
                    }
                }
            } while (i1 != k || j1 != l || enumdirection1 != enumdirection);

        }

        private void a(List<WoodlandMansionPieces.i> list, BlockPos blockposition, Rotation enumblockrotation, WoodlandMansionPieces.g worldgenwoodlandmansionpieces_g, @Nullable WoodlandMansionPieces.g worldgenwoodlandmansionpieces_g1) {
            int i;
            int j;
            BlockPos blockposition1;
            boolean flag;
            BlockPos blockposition2;

            for (i = 0; i < worldgenwoodlandmansionpieces_g.c; ++i) {
                for (j = 0; j < worldgenwoodlandmansionpieces_g.b; ++j) {
                    blockposition1 = blockposition.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 8 + (i - this.d) * 8);
                    blockposition1 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), (j - this.c) * 8);
                    flag = worldgenwoodlandmansionpieces_g1 != null && WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g1, j, i);
                    if (WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j, i) && !flag) {
                        list.add(new WoodlandMansionPieces.i(this.a, "roof", blockposition1.func_177981_b(3), enumblockrotation));
                        if (!WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j + 1, i)) {
                            blockposition2 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 6);
                            list.add(new WoodlandMansionPieces.i(this.a, "roof_front", blockposition2, enumblockrotation));
                        }

                        if (!WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j - 1, i)) {
                            blockposition2 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 0);
                            blockposition2 = blockposition2.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 7);
                            list.add(new WoodlandMansionPieces.i(this.a, "roof_front", blockposition2, enumblockrotation.func_185830_a(Rotation.CLOCKWISE_180)));
                        }

                        if (!WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j, i - 1)) {
                            blockposition2 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.WEST), 1);
                            list.add(new WoodlandMansionPieces.i(this.a, "roof_front", blockposition2, enumblockrotation.func_185830_a(Rotation.COUNTERCLOCKWISE_90)));
                        }

                        if (!WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j, i + 1)) {
                            blockposition2 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 6);
                            blockposition2 = blockposition2.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 6);
                            list.add(new WoodlandMansionPieces.i(this.a, "roof_front", blockposition2, enumblockrotation.func_185830_a(Rotation.CLOCKWISE_90)));
                        }
                    }
                }
            }

            if (worldgenwoodlandmansionpieces_g1 != null) {
                for (i = 0; i < worldgenwoodlandmansionpieces_g.c; ++i) {
                    for (j = 0; j < worldgenwoodlandmansionpieces_g.b; ++j) {
                        blockposition1 = blockposition.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 8 + (i - this.d) * 8);
                        blockposition1 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), (j - this.c) * 8);
                        flag = WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g1, j, i);
                        if (WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j, i) && flag) {
                            if (!WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j + 1, i)) {
                                blockposition2 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 7);
                                list.add(new WoodlandMansionPieces.i(this.a, "small_wall", blockposition2, enumblockrotation));
                            }

                            if (!WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j - 1, i)) {
                                blockposition2 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.WEST), 1);
                                blockposition2 = blockposition2.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 6);
                                list.add(new WoodlandMansionPieces.i(this.a, "small_wall", blockposition2, enumblockrotation.func_185830_a(Rotation.CLOCKWISE_180)));
                            }

                            if (!WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j, i - 1)) {
                                blockposition2 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.WEST), 0);
                                blockposition2 = blockposition2.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.NORTH), 1);
                                list.add(new WoodlandMansionPieces.i(this.a, "small_wall", blockposition2, enumblockrotation.func_185830_a(Rotation.COUNTERCLOCKWISE_90)));
                            }

                            if (!WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j, i + 1)) {
                                blockposition2 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 6);
                                blockposition2 = blockposition2.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 7);
                                list.add(new WoodlandMansionPieces.i(this.a, "small_wall", blockposition2, enumblockrotation.func_185830_a(Rotation.CLOCKWISE_90)));
                            }

                            if (!WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j + 1, i)) {
                                if (!WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j, i - 1)) {
                                    blockposition2 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 7);
                                    blockposition2 = blockposition2.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.NORTH), 2);
                                    list.add(new WoodlandMansionPieces.i(this.a, "small_wall_corner", blockposition2, enumblockrotation));
                                }

                                if (!WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j, i + 1)) {
                                    blockposition2 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 8);
                                    blockposition2 = blockposition2.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 7);
                                    list.add(new WoodlandMansionPieces.i(this.a, "small_wall_corner", blockposition2, enumblockrotation.func_185830_a(Rotation.CLOCKWISE_90)));
                                }
                            }

                            if (!WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j - 1, i)) {
                                if (!WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j, i - 1)) {
                                    blockposition2 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.WEST), 2);
                                    blockposition2 = blockposition2.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.NORTH), 1);
                                    list.add(new WoodlandMansionPieces.i(this.a, "small_wall_corner", blockposition2, enumblockrotation.func_185830_a(Rotation.COUNTERCLOCKWISE_90)));
                                }

                                if (!WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j, i + 1)) {
                                    blockposition2 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.WEST), 1);
                                    blockposition2 = blockposition2.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 8);
                                    list.add(new WoodlandMansionPieces.i(this.a, "small_wall_corner", blockposition2, enumblockrotation.func_185830_a(Rotation.CLOCKWISE_180)));
                                }
                            }
                        }
                    }
                }
            }

            for (i = 0; i < worldgenwoodlandmansionpieces_g.c; ++i) {
                for (j = 0; j < worldgenwoodlandmansionpieces_g.b; ++j) {
                    blockposition1 = blockposition.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 8 + (i - this.d) * 8);
                    blockposition1 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), (j - this.c) * 8);
                    flag = worldgenwoodlandmansionpieces_g1 != null && WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g1, j, i);
                    if (WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j, i) && !flag) {
                        BlockPos blockposition3;

                        if (!WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j + 1, i)) {
                            blockposition2 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 6);
                            if (!WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j, i + 1)) {
                                blockposition3 = blockposition2.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 6);
                                list.add(new WoodlandMansionPieces.i(this.a, "roof_corner", blockposition3, enumblockrotation));
                            } else if (WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j + 1, i + 1)) {
                                blockposition3 = blockposition2.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 5);
                                list.add(new WoodlandMansionPieces.i(this.a, "roof_inner_corner", blockposition3, enumblockrotation));
                            }

                            if (!WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j, i - 1)) {
                                list.add(new WoodlandMansionPieces.i(this.a, "roof_corner", blockposition2, enumblockrotation.func_185830_a(Rotation.COUNTERCLOCKWISE_90)));
                            } else if (WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j + 1, i - 1)) {
                                blockposition3 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 9);
                                blockposition3 = blockposition3.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.NORTH), 2);
                                list.add(new WoodlandMansionPieces.i(this.a, "roof_inner_corner", blockposition3, enumblockrotation.func_185830_a(Rotation.CLOCKWISE_90)));
                            }
                        }

                        if (!WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j - 1, i)) {
                            blockposition2 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 0);
                            blockposition2 = blockposition2.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 0);
                            if (!WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j, i + 1)) {
                                blockposition3 = blockposition2.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 6);
                                list.add(new WoodlandMansionPieces.i(this.a, "roof_corner", blockposition3, enumblockrotation.func_185830_a(Rotation.CLOCKWISE_90)));
                            } else if (WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j - 1, i + 1)) {
                                blockposition3 = blockposition2.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 8);
                                blockposition3 = blockposition3.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.WEST), 3);
                                list.add(new WoodlandMansionPieces.i(this.a, "roof_inner_corner", blockposition3, enumblockrotation.func_185830_a(Rotation.COUNTERCLOCKWISE_90)));
                            }

                            if (!WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j, i - 1)) {
                                list.add(new WoodlandMansionPieces.i(this.a, "roof_corner", blockposition2, enumblockrotation.func_185830_a(Rotation.CLOCKWISE_180)));
                            } else if (WoodlandMansionPieces.c.a(worldgenwoodlandmansionpieces_g, j - 1, i - 1)) {
                                blockposition3 = blockposition2.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 1);
                                list.add(new WoodlandMansionPieces.i(this.a, "roof_inner_corner", blockposition3, enumblockrotation.func_185830_a(Rotation.CLOCKWISE_180)));
                            }
                        }
                    }
                }
            }

        }

        private void a(List<WoodlandMansionPieces.i> list, WoodlandMansionPieces.e worldgenwoodlandmansionpieces_e) {
            EnumFacing enumdirection = worldgenwoodlandmansionpieces_e.a.func_185831_a(EnumFacing.WEST);

            list.add(new WoodlandMansionPieces.i(this.a, "entrance", worldgenwoodlandmansionpieces_e.b.func_177967_a(enumdirection, 9), worldgenwoodlandmansionpieces_e.a));
            worldgenwoodlandmansionpieces_e.b = worldgenwoodlandmansionpieces_e.b.func_177967_a(worldgenwoodlandmansionpieces_e.a.func_185831_a(EnumFacing.SOUTH), 16);
        }

        private void b(List<WoodlandMansionPieces.i> list, WoodlandMansionPieces.e worldgenwoodlandmansionpieces_e) {
            list.add(new WoodlandMansionPieces.i(this.a, worldgenwoodlandmansionpieces_e.c, worldgenwoodlandmansionpieces_e.b.func_177967_a(worldgenwoodlandmansionpieces_e.a.func_185831_a(EnumFacing.EAST), 7), worldgenwoodlandmansionpieces_e.a));
            worldgenwoodlandmansionpieces_e.b = worldgenwoodlandmansionpieces_e.b.func_177967_a(worldgenwoodlandmansionpieces_e.a.func_185831_a(EnumFacing.SOUTH), 8);
        }

        private void c(List<WoodlandMansionPieces.i> list, WoodlandMansionPieces.e worldgenwoodlandmansionpieces_e) {
            worldgenwoodlandmansionpieces_e.b = worldgenwoodlandmansionpieces_e.b.func_177967_a(worldgenwoodlandmansionpieces_e.a.func_185831_a(EnumFacing.SOUTH), -1);
            list.add(new WoodlandMansionPieces.i(this.a, "wall_corner", worldgenwoodlandmansionpieces_e.b, worldgenwoodlandmansionpieces_e.a));
            worldgenwoodlandmansionpieces_e.b = worldgenwoodlandmansionpieces_e.b.func_177967_a(worldgenwoodlandmansionpieces_e.a.func_185831_a(EnumFacing.SOUTH), -7);
            worldgenwoodlandmansionpieces_e.b = worldgenwoodlandmansionpieces_e.b.func_177967_a(worldgenwoodlandmansionpieces_e.a.func_185831_a(EnumFacing.WEST), -6);
            worldgenwoodlandmansionpieces_e.a = worldgenwoodlandmansionpieces_e.a.func_185830_a(Rotation.CLOCKWISE_90);
        }

        private void d(List<WoodlandMansionPieces.i> list, WoodlandMansionPieces.e worldgenwoodlandmansionpieces_e) {
            worldgenwoodlandmansionpieces_e.b = worldgenwoodlandmansionpieces_e.b.func_177967_a(worldgenwoodlandmansionpieces_e.a.func_185831_a(EnumFacing.SOUTH), 6);
            worldgenwoodlandmansionpieces_e.b = worldgenwoodlandmansionpieces_e.b.func_177967_a(worldgenwoodlandmansionpieces_e.a.func_185831_a(EnumFacing.EAST), 8);
            worldgenwoodlandmansionpieces_e.a = worldgenwoodlandmansionpieces_e.a.func_185830_a(Rotation.COUNTERCLOCKWISE_90);
        }

        private void a(List<WoodlandMansionPieces.i> list, BlockPos blockposition, Rotation enumblockrotation, EnumFacing enumdirection, WoodlandMansionPieces.b worldgenwoodlandmansionpieces_b) {
            Rotation enumblockrotation1 = Rotation.NONE;
            String s = worldgenwoodlandmansionpieces_b.a(this.b);

            if (enumdirection != EnumFacing.EAST) {
                if (enumdirection == EnumFacing.NORTH) {
                    enumblockrotation1 = enumblockrotation1.func_185830_a(Rotation.COUNTERCLOCKWISE_90);
                } else if (enumdirection == EnumFacing.WEST) {
                    enumblockrotation1 = enumblockrotation1.func_185830_a(Rotation.CLOCKWISE_180);
                } else if (enumdirection == EnumFacing.SOUTH) {
                    enumblockrotation1 = enumblockrotation1.func_185830_a(Rotation.CLOCKWISE_90);
                } else {
                    s = worldgenwoodlandmansionpieces_b.b(this.b);
                }
            }

            BlockPos blockposition1 = Template.func_191157_a(new BlockPos(1, 0, 0), Mirror.NONE, enumblockrotation1, 7, 7);

            enumblockrotation1 = enumblockrotation1.func_185830_a(enumblockrotation);
            blockposition1 = blockposition1.func_190942_a(enumblockrotation);
            BlockPos blockposition2 = blockposition.func_177982_a(blockposition1.func_177958_n(), 0, blockposition1.func_177952_p());

            list.add(new WoodlandMansionPieces.i(this.a, s, blockposition2, enumblockrotation1));
        }

        private void a(List<WoodlandMansionPieces.i> list, BlockPos blockposition, Rotation enumblockrotation, EnumFacing enumdirection, EnumFacing enumdirection1, WoodlandMansionPieces.b worldgenwoodlandmansionpieces_b, boolean flag) {
            BlockPos blockposition1;

            if (enumdirection1 == EnumFacing.EAST && enumdirection == EnumFacing.SOUTH) {
                blockposition1 = blockposition.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 1);
                list.add(new WoodlandMansionPieces.i(this.a, worldgenwoodlandmansionpieces_b.a(this.b, flag), blockposition1, enumblockrotation));
            } else if (enumdirection1 == EnumFacing.EAST && enumdirection == EnumFacing.NORTH) {
                blockposition1 = blockposition.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 1);
                blockposition1 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 6);
                list.add(new WoodlandMansionPieces.i(this.a, worldgenwoodlandmansionpieces_b.a(this.b, flag), blockposition1, enumblockrotation, Mirror.LEFT_RIGHT));
            } else if (enumdirection1 == EnumFacing.WEST && enumdirection == EnumFacing.NORTH) {
                blockposition1 = blockposition.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 7);
                blockposition1 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 6);
                list.add(new WoodlandMansionPieces.i(this.a, worldgenwoodlandmansionpieces_b.a(this.b, flag), blockposition1, enumblockrotation.func_185830_a(Rotation.CLOCKWISE_180)));
            } else if (enumdirection1 == EnumFacing.WEST && enumdirection == EnumFacing.SOUTH) {
                blockposition1 = blockposition.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 7);
                list.add(new WoodlandMansionPieces.i(this.a, worldgenwoodlandmansionpieces_b.a(this.b, flag), blockposition1, enumblockrotation, Mirror.FRONT_BACK));
            } else if (enumdirection1 == EnumFacing.SOUTH && enumdirection == EnumFacing.EAST) {
                blockposition1 = blockposition.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 1);
                list.add(new WoodlandMansionPieces.i(this.a, worldgenwoodlandmansionpieces_b.a(this.b, flag), blockposition1, enumblockrotation.func_185830_a(Rotation.CLOCKWISE_90), Mirror.LEFT_RIGHT));
            } else if (enumdirection1 == EnumFacing.SOUTH && enumdirection == EnumFacing.WEST) {
                blockposition1 = blockposition.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 7);
                list.add(new WoodlandMansionPieces.i(this.a, worldgenwoodlandmansionpieces_b.a(this.b, flag), blockposition1, enumblockrotation.func_185830_a(Rotation.CLOCKWISE_90)));
            } else if (enumdirection1 == EnumFacing.NORTH && enumdirection == EnumFacing.WEST) {
                blockposition1 = blockposition.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 7);
                blockposition1 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 6);
                list.add(new WoodlandMansionPieces.i(this.a, worldgenwoodlandmansionpieces_b.a(this.b, flag), blockposition1, enumblockrotation.func_185830_a(Rotation.CLOCKWISE_90), Mirror.FRONT_BACK));
            } else if (enumdirection1 == EnumFacing.NORTH && enumdirection == EnumFacing.EAST) {
                blockposition1 = blockposition.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 1);
                blockposition1 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 6);
                list.add(new WoodlandMansionPieces.i(this.a, worldgenwoodlandmansionpieces_b.a(this.b, flag), blockposition1, enumblockrotation.func_185830_a(Rotation.COUNTERCLOCKWISE_90)));
            } else if (enumdirection1 == EnumFacing.SOUTH && enumdirection == EnumFacing.NORTH) {
                blockposition1 = blockposition.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 1);
                blockposition1 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.NORTH), 8);
                list.add(new WoodlandMansionPieces.i(this.a, worldgenwoodlandmansionpieces_b.b(this.b, flag), blockposition1, enumblockrotation));
            } else if (enumdirection1 == EnumFacing.NORTH && enumdirection == EnumFacing.SOUTH) {
                blockposition1 = blockposition.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 7);
                blockposition1 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 14);
                list.add(new WoodlandMansionPieces.i(this.a, worldgenwoodlandmansionpieces_b.b(this.b, flag), blockposition1, enumblockrotation.func_185830_a(Rotation.CLOCKWISE_180)));
            } else if (enumdirection1 == EnumFacing.WEST && enumdirection == EnumFacing.EAST) {
                blockposition1 = blockposition.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 15);
                list.add(new WoodlandMansionPieces.i(this.a, worldgenwoodlandmansionpieces_b.b(this.b, flag), blockposition1, enumblockrotation.func_185830_a(Rotation.CLOCKWISE_90)));
            } else if (enumdirection1 == EnumFacing.EAST && enumdirection == EnumFacing.WEST) {
                blockposition1 = blockposition.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.WEST), 7);
                blockposition1 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), 6);
                list.add(new WoodlandMansionPieces.i(this.a, worldgenwoodlandmansionpieces_b.b(this.b, flag), blockposition1, enumblockrotation.func_185830_a(Rotation.COUNTERCLOCKWISE_90)));
            } else if (enumdirection1 == EnumFacing.UP && enumdirection == EnumFacing.EAST) {
                blockposition1 = blockposition.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 15);
                list.add(new WoodlandMansionPieces.i(this.a, worldgenwoodlandmansionpieces_b.c(this.b), blockposition1, enumblockrotation.func_185830_a(Rotation.CLOCKWISE_90)));
            } else if (enumdirection1 == EnumFacing.UP && enumdirection == EnumFacing.SOUTH) {
                blockposition1 = blockposition.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 1);
                blockposition1 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.NORTH), 0);
                list.add(new WoodlandMansionPieces.i(this.a, worldgenwoodlandmansionpieces_b.c(this.b), blockposition1, enumblockrotation));
            }

        }

        private void a(List<WoodlandMansionPieces.i> list, BlockPos blockposition, Rotation enumblockrotation, EnumFacing enumdirection, EnumFacing enumdirection1, WoodlandMansionPieces.b worldgenwoodlandmansionpieces_b) {
            byte b0 = 0;
            byte b1 = 0;
            Rotation enumblockrotation1 = enumblockrotation;
            Mirror enumblockmirror = Mirror.NONE;

            if (enumdirection1 == EnumFacing.EAST && enumdirection == EnumFacing.SOUTH) {
                b0 = -7;
            } else if (enumdirection1 == EnumFacing.EAST && enumdirection == EnumFacing.NORTH) {
                b0 = -7;
                b1 = 6;
                enumblockmirror = Mirror.LEFT_RIGHT;
            } else if (enumdirection1 == EnumFacing.NORTH && enumdirection == EnumFacing.EAST) {
                b0 = 1;
                b1 = 14;
                enumblockrotation1 = enumblockrotation.func_185830_a(Rotation.COUNTERCLOCKWISE_90);
            } else if (enumdirection1 == EnumFacing.NORTH && enumdirection == EnumFacing.WEST) {
                b0 = 7;
                b1 = 14;
                enumblockrotation1 = enumblockrotation.func_185830_a(Rotation.COUNTERCLOCKWISE_90);
                enumblockmirror = Mirror.LEFT_RIGHT;
            } else if (enumdirection1 == EnumFacing.SOUTH && enumdirection == EnumFacing.WEST) {
                b0 = 7;
                b1 = -8;
                enumblockrotation1 = enumblockrotation.func_185830_a(Rotation.CLOCKWISE_90);
            } else if (enumdirection1 == EnumFacing.SOUTH && enumdirection == EnumFacing.EAST) {
                b0 = 1;
                b1 = -8;
                enumblockrotation1 = enumblockrotation.func_185830_a(Rotation.CLOCKWISE_90);
                enumblockmirror = Mirror.LEFT_RIGHT;
            } else if (enumdirection1 == EnumFacing.WEST && enumdirection == EnumFacing.NORTH) {
                b0 = 15;
                b1 = 6;
                enumblockrotation1 = enumblockrotation.func_185830_a(Rotation.CLOCKWISE_180);
            } else if (enumdirection1 == EnumFacing.WEST && enumdirection == EnumFacing.SOUTH) {
                b0 = 15;
                enumblockmirror = Mirror.FRONT_BACK;
            }

            BlockPos blockposition1 = blockposition.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), b0);

            blockposition1 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.SOUTH), b1);
            list.add(new WoodlandMansionPieces.i(this.a, worldgenwoodlandmansionpieces_b.d(this.b), blockposition1, enumblockrotation1, enumblockmirror));
        }

        private void a(List<WoodlandMansionPieces.i> list, BlockPos blockposition, Rotation enumblockrotation, WoodlandMansionPieces.b worldgenwoodlandmansionpieces_b) {
            BlockPos blockposition1 = blockposition.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.EAST), 1);

            list.add(new WoodlandMansionPieces.i(this.a, worldgenwoodlandmansionpieces_b.e(this.b), blockposition1, enumblockrotation, Mirror.NONE));
        }
    }

    static class e {

        public Rotation a;
        public BlockPos b;
        public String c;

        private e() {}

        e(Object object) {
            this();
        }
    }

    public static class i extends StructureComponentTemplate {

        private String d;
        private Rotation e;
        private Mirror f;

        public i() {}

        public i(TemplateManager definedstructuremanager, String s, BlockPos blockposition, Rotation enumblockrotation) {
            this(definedstructuremanager, s, blockposition, enumblockrotation, Mirror.NONE);
        }

        public i(TemplateManager definedstructuremanager, String s, BlockPos blockposition, Rotation enumblockrotation, Mirror enumblockmirror) {
            super(0);
            this.d = s;
            this.field_186178_c = blockposition;
            this.e = enumblockrotation;
            this.f = enumblockmirror;
            this.a(definedstructuremanager);
        }

        private void a(TemplateManager definedstructuremanager) {
            Template definedstructure = definedstructuremanager.func_186237_a((MinecraftServer) null, new ResourceLocation("mansion/" + this.d));
            PlacementSettings definedstructureinfo = (new PlacementSettings()).func_186222_a(true).func_186220_a(this.e).func_186214_a(this.f);

            this.func_186173_a(definedstructure, this.field_186178_c, definedstructureinfo);
        }

        @Override
        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            super.func_143012_a(nbttagcompound);
            nbttagcompound.func_74778_a("Template", this.d);
            nbttagcompound.func_74778_a("Rot", this.field_186177_b.func_186215_c().name());
            nbttagcompound.func_74778_a("Mi", this.field_186177_b.func_186212_b().name());
        }

        @Override
        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.func_143011_b(nbttagcompound, definedstructuremanager);
            this.d = nbttagcompound.func_74779_i("Template");
            this.e = Rotation.valueOf(nbttagcompound.func_74779_i("Rot"));
            this.f = Mirror.valueOf(nbttagcompound.func_74779_i("Mi"));
            this.a(definedstructuremanager);
        }

        @Override
        protected void func_186175_a(String s, BlockPos blockposition, World world, Random random, StructureBoundingBox structureboundingbox) {
            if (s.startsWith("Chest")) {
                Rotation enumblockrotation = this.field_186177_b.func_186215_c();
                IBlockState iblockdata = Blocks.field_150486_ae.func_176223_P();

                if ("ChestWest".equals(s)) {
                    iblockdata = iblockdata.func_177226_a(BlockChest.field_176459_a, enumblockrotation.func_185831_a(EnumFacing.WEST));
                } else if ("ChestEast".equals(s)) {
                    iblockdata = iblockdata.func_177226_a(BlockChest.field_176459_a, enumblockrotation.func_185831_a(EnumFacing.EAST));
                } else if ("ChestSouth".equals(s)) {
                    iblockdata = iblockdata.func_177226_a(BlockChest.field_176459_a, enumblockrotation.func_185831_a(EnumFacing.SOUTH));
                } else if ("ChestNorth".equals(s)) {
                    iblockdata = iblockdata.func_177226_a(BlockChest.field_176459_a, enumblockrotation.func_185831_a(EnumFacing.NORTH));
                }

                this.func_191080_a(world, structureboundingbox, random, blockposition, LootTableList.field_191192_o, iblockdata);
            } else if ("Mage".equals(s)) {
                EntityEvoker entityevoker = new EntityEvoker(world);

                entityevoker.func_110163_bv();
                entityevoker.func_174828_a(blockposition, 0.0F, 0.0F);
                world.func_72838_d(entityevoker);
                world.func_180501_a(blockposition, Blocks.field_150350_a.func_176223_P(), 2);
            } else if ("Warrior".equals(s)) {
                EntityVindicator entityvindicator = new EntityVindicator(world);

                entityvindicator.func_110163_bv();
                entityvindicator.func_174828_a(blockposition, 0.0F, 0.0F);
                entityvindicator.func_180482_a(world.func_175649_E(new BlockPos(entityvindicator)), (IEntityLivingData) null);
                world.func_72838_d(entityvindicator);
                world.func_180501_a(blockposition, Blocks.field_150350_a.func_176223_P(), 2);
            }

        }
    }
}
