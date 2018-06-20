package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class MapGenScatteredFeature extends MapGenStructure {

    private static final List<Biome> field_75061_e = Arrays.asList(new Biome[] { Biomes.field_76769_d, Biomes.field_76786_s, Biomes.field_76782_w, Biomes.field_76792_x, Biomes.field_76780_h, Biomes.field_76774_n, Biomes.field_150584_S});
    private final List<Biome.SpawnListEntry> field_82668_f;
    private int field_82669_g;
    private final int field_82670_h;

    public MapGenScatteredFeature() {
        this.field_82668_f = Lists.newArrayList();
        this.field_82669_g = 32;
        this.field_82670_h = 8;
        this.field_82668_f.add(new Biome.SpawnListEntry(EntityWitch.class, 1, 1, 1));
    }

    public MapGenScatteredFeature(Map<String, String> map) {
        this();
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (((String) entry.getKey()).equals("distance")) {
                this.field_82669_g = MathHelper.func_82714_a((String) entry.getValue(), this.field_82669_g, 9);
            }
        }

    }

    @Override
    public String func_143025_a() {
        return "Temple";
    }

    @Override
    protected boolean func_75047_a(int i, int j) {
        int k = i;
        int l = j;

        if (i < 0) {
            i -= this.field_82669_g - 1;
        }

        if (j < 0) {
            j -= this.field_82669_g - 1;
        }

        int i1 = i / this.field_82669_g;
        int j1 = j / this.field_82669_g;
        Random random = this.field_75039_c.func_72843_D(i1, j1, this.field_75039_c.spigotConfig.largeFeatureSeed); // Spigot

        i1 *= this.field_82669_g;
        j1 *= this.field_82669_g;
        i1 += random.nextInt(this.field_82669_g - 8);
        j1 += random.nextInt(this.field_82669_g - 8);
        if (k == i1 && l == j1) {
            Biome biomebase = this.field_75039_c.func_72959_q().func_180631_a(new BlockPos(k * 16 + 8, 0, l * 16 + 8));

            if (biomebase == null) {
                return false;
            }

            Iterator iterator = MapGenScatteredFeature.field_75061_e.iterator();

            while (iterator.hasNext()) {
                Biome biomebase1 = (Biome) iterator.next();

                if (biomebase == biomebase1) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public BlockPos func_180706_b(World world, BlockPos blockposition, boolean flag) {
        this.field_75039_c = world;
        return func_191069_a(world, this, blockposition, this.field_82669_g, 8, this.field_75039_c.spigotConfig.largeFeatureSeed, false, 100, flag); // Spigot
    }

    @Override
    protected StructureStart func_75049_b(int i, int j) {
        return new MapGenScatteredFeature.Start(this.field_75039_c, this.field_75038_b, i, j);
    }

    public boolean func_175798_a(BlockPos blockposition) {
        StructureStart structurestart = this.func_175797_c(blockposition);

        if (structurestart != null && structurestart instanceof MapGenScatteredFeature.Start && !structurestart.field_75075_a.isEmpty()) {
            StructureComponent structurepiece = structurestart.field_75075_a.get(0);

            return structurepiece instanceof ComponentScatteredFeaturePieces.SwampHut;
        } else {
            return false;
        }
    }

    public List<Biome.SpawnListEntry> func_82667_a() {
        return this.field_82668_f;
    }

    public static class Start extends StructureStart {

        public Start() {}

        public Start(World world, Random random, int i, int j) {
            this(world, random, i, j, world.func_180494_b(new BlockPos(i * 16 + 8, 0, j * 16 + 8)));
        }

        public Start(World world, Random random, int i, int j, Biome biomebase) {
            super(i, j);
            if (biomebase != Biomes.field_76782_w && biomebase != Biomes.field_76792_x) {
                if (biomebase == Biomes.field_76780_h) {
                    ComponentScatteredFeaturePieces.SwampHut worldgenregistration_worldgenwitchhut = new ComponentScatteredFeaturePieces.SwampHut(random, i * 16, j * 16);

                    this.field_75075_a.add(worldgenregistration_worldgenwitchhut);
                } else if (biomebase != Biomes.field_76769_d && biomebase != Biomes.field_76786_s) {
                    if (biomebase == Biomes.field_76774_n || biomebase == Biomes.field_150584_S) {
                        ComponentScatteredFeaturePieces.b worldgenregistration_b = new ComponentScatteredFeaturePieces.b(random, i * 16, j * 16);

                        this.field_75075_a.add(worldgenregistration_b);
                    }
                } else {
                    ComponentScatteredFeaturePieces.DesertPyramid worldgenregistration_worldgenpyramidpiece = new ComponentScatteredFeaturePieces.DesertPyramid(random, i * 16, j * 16);

                    this.field_75075_a.add(worldgenregistration_worldgenpyramidpiece);
                }
            } else {
                ComponentScatteredFeaturePieces.JunglePyramid worldgenregistration_worldgenjungletemple = new ComponentScatteredFeaturePieces.JunglePyramid(random, i * 16, j * 16);

                this.field_75075_a.add(worldgenregistration_worldgenjungletemple);
            }

            this.func_75072_c();
        }
    }
}
