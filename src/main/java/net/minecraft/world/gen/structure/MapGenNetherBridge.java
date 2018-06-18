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

    private final List<Biome.SpawnListEntry> spawnList = Lists.newArrayList();

    public MapGenNetherBridge() {
        this.spawnList.add(new Biome.SpawnListEntry(EntityBlaze.class, 10, 2, 3));
        this.spawnList.add(new Biome.SpawnListEntry(EntityPigZombie.class, 5, 4, 4));
        this.spawnList.add(new Biome.SpawnListEntry(EntityWitherSkeleton.class, 8, 5, 5));
        this.spawnList.add(new Biome.SpawnListEntry(EntitySkeleton.class, 2, 5, 5));
        this.spawnList.add(new Biome.SpawnListEntry(EntityMagmaCube.class, 3, 4, 4));
    }

    public String getStructureName() {
        return "Fortress";
    }

    public List<Biome.SpawnListEntry> getSpawnList() {
        return this.spawnList;
    }

    protected boolean canSpawnStructureAtCoords(int i, int j) {
        int k = i >> 4;
        int l = j >> 4;

        this.rand.setSeed((long) (k ^ l << 4) ^ this.world.getSeed());
        this.rand.nextInt();
        return this.rand.nextInt(3) != 0 ? false : (i != (k << 4) + 4 + this.rand.nextInt(8) ? false : j == (l << 4) + 4 + this.rand.nextInt(8));
    }

    protected StructureStart getStructureStart(int i, int j) {
        return new MapGenNetherBridge.Start(this.world, this.rand, i, j);
    }

    public BlockPos getNearestStructurePos(World world, BlockPos blockposition, boolean flag) {
        boolean flag1 = true;
        int i = blockposition.getX() >> 4;
        int j = blockposition.getZ() >> 4;

        for (int k = 0; k <= 1000; ++k) {
            for (int l = -k; l <= k; ++l) {
                boolean flag2 = l == -k || l == k;

                for (int i1 = -k; i1 <= k; ++i1) {
                    boolean flag3 = i1 == -k || i1 == k;

                    if (flag2 || flag3) {
                        int j1 = i + l;
                        int k1 = j + i1;

                        if (this.canSpawnStructureAtCoords(j1, k1) && (!flag || !world.isChunkGeneratedAt(j1, k1))) {
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

            this.components.add(worldgennetherpieces_worldgennetherpiece15);
            worldgennetherpieces_worldgennetherpiece15.buildComponent((StructureComponent) worldgennetherpieces_worldgennetherpiece15, this.components, random);
            List list = worldgennetherpieces_worldgennetherpiece15.pendingChildren;

            while (!list.isEmpty()) {
                int k = random.nextInt(list.size());
                StructureComponent structurepiece = (StructureComponent) list.remove(k);

                structurepiece.buildComponent((StructureComponent) worldgennetherpieces_worldgennetherpiece15, this.components, random);
            }

            this.updateBoundingBox();
            this.setRandomHeight(world, random, 48, 70);
        }
    }
}
