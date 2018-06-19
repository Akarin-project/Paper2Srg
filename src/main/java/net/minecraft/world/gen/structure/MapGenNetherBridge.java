package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class MapGenNetherBridge extends MapGenStructure {

    private final List<Biome.SpawnListEntry> field_75060_e = Lists.newArrayList();

    public MapGenNetherBridge() {
        this.field_75060_e.add(new Biome.SpawnListEntry(EntityBlaze.class, 10, 2, 3));
        this.field_75060_e.add(new Biome.SpawnListEntry(EntityPigZombie.class, 5, 4, 4));
        this.field_75060_e.add(new Biome.SpawnListEntry(EntityWitherSkeleton.class, 8, 5, 5));
        this.field_75060_e.add(new Biome.SpawnListEntry(EntitySkeleton.class, 2, 5, 5));
        this.field_75060_e.add(new Biome.SpawnListEntry(EntityMagmaCube.class, 3, 4, 4));
    }

    public String func_143025_a() {
        return "Fortress";
    }

    public List<Biome.SpawnListEntry> func_75059_a() {
        return this.field_75060_e;
    }

    protected boolean func_75047_a(int i, int j) {
        int k = i >> 4;
        int l = j >> 4;

        this.field_75038_b.setSeed((long) (k ^ l << 4) ^ this.field_75039_c.func_72905_C());
        this.field_75038_b.nextInt();
        return this.field_75038_b.nextInt(3) != 0 ? false : (i != (k << 4) + 4 + this.field_75038_b.nextInt(8) ? false : j == (l << 4) + 4 + this.field_75038_b.nextInt(8));
    }

    protected StructureStart func_75049_b(int i, int j) {
        return new MapGenNetherBridge.Start(this.field_75039_c, this.field_75038_b, i, j);
    }

    public BlockPos func_180706_b(World world, BlockPos blockposition, boolean flag) {
        boolean flag1 = true;
        int i = blockposition.func_177958_n() >> 4;
        int j = blockposition.func_177952_p() >> 4;

        for (int k = 0; k <= 1000; ++k) {
            for (int l = -k; l <= k; ++l) {
                boolean flag2 = l == -k || l == k;

                for (int i1 = -k; i1 <= k; ++i1) {
                    boolean flag3 = i1 == -k || i1 == k;

                    if (flag2 || flag3) {
                        int j1 = i + l;
                        int k1 = j + i1;

                        if (this.func_75047_a(j1, k1) && (!flag || !world.func_190526_b(j1, k1))) {
                            return new BlockPos((j1 << 4) + 8, 64, (k1 << 4) + 8);
                        }
                    }
                }
            }
        }

        return null;
    }

    public static class Start extends StructureStart {

        public Start() {}

        public Start(World world, Random random, int i, int j) {
            super(i, j);
            StructureNetherBridgePieces.Start worldgennetherpieces_worldgennetherpiece15 = new StructureNetherBridgePieces.Start(random, (i << 4) + 2, (j << 4) + 2);

            this.field_75075_a.add(worldgennetherpieces_worldgennetherpiece15);
            worldgennetherpieces_worldgennetherpiece15.func_74861_a((StructureComponent) worldgennetherpieces_worldgennetherpiece15, this.field_75075_a, random);
            List list = worldgennetherpieces_worldgennetherpiece15.field_74967_d;

            while (!list.isEmpty()) {
                int k = random.nextInt(list.size());
                StructureComponent structurepiece = (StructureComponent) list.remove(k);

                structurepiece.func_74861_a((StructureComponent) worldgennetherpieces_worldgennetherpiece15, this.field_75075_a, random);
            }

            this.func_75072_c();
            this.func_75070_a(world, random, 48, 70);
        }
    }
}
