package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockRail;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.WorldGenMineshaftPieces.c;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

public class StructureMineshaftPieces {

    public static void registerStructurePieces() {
        MapGenStructureIO.registerStructureComponent(StructureMineshaftPieces.Corridor.class, "MSCorridor");
        MapGenStructureIO.registerStructureComponent(StructureMineshaftPieces.Cross.class, "MSCrossing");
        MapGenStructureIO.registerStructureComponent(StructureMineshaftPieces.Room.class, "MSRoom");
        MapGenStructureIO.registerStructureComponent(StructureMineshaftPieces.Stairs.class, "MSStairs");
    }

    private static WorldGenMineshaftPieces.c a(List<StructureComponent> list, Random random, int i, int j, int k, @Nullable EnumFacing enumdirection, int l, MapGenMineshaft.Type worldgenmineshaft_type) {
        int i1 = random.nextInt(100);
        StructureBoundingBox structureboundingbox;

        if (i1 >= 80) {
            structureboundingbox = StructureMineshaftPieces.Cross.findCrossing(list, random, i, j, k, enumdirection);
            if (structureboundingbox != null) {
                return new StructureMineshaftPieces.Cross(l, random, structureboundingbox, enumdirection, worldgenmineshaft_type);
            }
        } else if (i1 >= 70) {
            structureboundingbox = StructureMineshaftPieces.Stairs.findStairs(list, random, i, j, k, enumdirection);
            if (structureboundingbox != null) {
                return new StructureMineshaftPieces.Stairs(l, random, structureboundingbox, enumdirection, worldgenmineshaft_type);
            }
        } else {
            structureboundingbox = StructureMineshaftPieces.Corridor.findCorridorSize(list, random, i, j, k, enumdirection);
            if (structureboundingbox != null) {
                return new StructureMineshaftPieces.Corridor(l, random, structureboundingbox, enumdirection, worldgenmineshaft_type);
            }
        }

        return null;
    }

    private static WorldGenMineshaftPieces.c b(StructureComponent structurepiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
        if (l > 8) {
            return null;
        } else if (Math.abs(i - structurepiece.getBoundingBox().minX) <= 80 && Math.abs(k - structurepiece.getBoundingBox().minZ) <= 80) {
            MapGenMineshaft.Type worldgenmineshaft_type = ((WorldGenMineshaftPieces.c) structurepiece).a;
            WorldGenMineshaftPieces.c worldgenmineshaftpieces_c = a(list, random, i, j, k, enumdirection, l + 1, worldgenmineshaft_type);

            if (worldgenmineshaftpieces_c != null) {
                list.add(worldgenmineshaftpieces_c);
                worldgenmineshaftpieces_c.buildComponent(structurepiece, list, random);
            }

            return worldgenmineshaftpieces_c;
        } else {
            return null;
        }
    }

    public static class Stairs extends WorldGenMineshaftPieces.c {

        public Stairs() {}

        public Stairs(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection, MapGenMineshaft.Type worldgenmineshaft_type) {
            super(i, worldgenmineshaft_type);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
        }

        public static StructureBoundingBox findStairs(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection) {
            StructureBoundingBox structureboundingbox = new StructureBoundingBox(i, j - 5, k, i, j + 2, k);

            switch (enumdirection) {
            case NORTH:
            default:
                structureboundingbox.maxX = i + 2;
                structureboundingbox.minZ = k - 8;
                break;

            case SOUTH:
                structureboundingbox.maxX = i + 2;
                structureboundingbox.maxZ = k + 8;
                break;

            case WEST:
                structureboundingbox.minX = i - 8;
                structureboundingbox.maxZ = k + 2;
                break;

            case EAST:
                structureboundingbox.maxX = i + 8;
                structureboundingbox.maxZ = k + 2;
            }

            return StructureComponent.findIntersecting(list, structureboundingbox) != null ? null : structureboundingbox;
        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            int i = this.getComponentType();
            EnumFacing enumdirection = this.getCoordBaseMode();

            if (enumdirection != null) {
                switch (enumdirection) {
                case NORTH:
                default:
                    StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                    break;

                case SOUTH:
                    StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                    break;

                case WEST:
                    StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST, i);
                    break;

                case EAST:
                    StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, i);
                }
            }

        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
                return false;
            } else {
                this.fillWithBlocks(world, structureboundingbox, 0, 5, 0, 2, 7, 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                this.fillWithBlocks(world, structureboundingbox, 0, 0, 7, 2, 2, 8, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);

                for (int i = 0; i < 5; ++i) {
                    this.fillWithBlocks(world, structureboundingbox, 0, 5 - i - (i < 4 ? 1 : 0), 2 + i, 2, 7 - i, 2 + i, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                }

                return true;
            }
        }
    }

    public static class Cross extends WorldGenMineshaftPieces.c {

        private EnumFacing corridorDirection;
        private boolean isMultipleFloors;

        public Cross() {}

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
            super.writeStructureToNBT(nbttagcompound);
            nbttagcompound.setBoolean("tf", this.isMultipleFloors);
            nbttagcompound.setInteger("D", this.corridorDirection.getHorizontalIndex());
        }

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.readStructureFromNBT(nbttagcompound, definedstructuremanager);
            this.isMultipleFloors = nbttagcompound.getBoolean("tf");
            this.corridorDirection = EnumFacing.getHorizontal(nbttagcompound.getInteger("D"));
        }

        public Cross(int i, Random random, StructureBoundingBox structureboundingbox, @Nullable EnumFacing enumdirection, MapGenMineshaft.Type worldgenmineshaft_type) {
            super(i, worldgenmineshaft_type);
            this.corridorDirection = enumdirection;
            this.boundingBox = structureboundingbox;
            this.isMultipleFloors = structureboundingbox.getYSize() > 3;
        }

        public static StructureBoundingBox findCrossing(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection) {
            StructureBoundingBox structureboundingbox = new StructureBoundingBox(i, j, k, i, j + 2, k);

            if (random.nextInt(4) == 0) {
                structureboundingbox.maxY += 4;
            }

            switch (enumdirection) {
            case NORTH:
            default:
                structureboundingbox.minX = i - 1;
                structureboundingbox.maxX = i + 3;
                structureboundingbox.minZ = k - 4;
                break;

            case SOUTH:
                structureboundingbox.minX = i - 1;
                structureboundingbox.maxX = i + 3;
                structureboundingbox.maxZ = k + 3 + 1;
                break;

            case WEST:
                structureboundingbox.minX = i - 4;
                structureboundingbox.minZ = k - 1;
                structureboundingbox.maxZ = k + 3;
                break;

            case EAST:
                structureboundingbox.maxX = i + 3 + 1;
                structureboundingbox.minZ = k - 1;
                structureboundingbox.maxZ = k + 3;
            }

            return StructureComponent.findIntersecting(list, structureboundingbox) != null ? null : structureboundingbox;
        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            int i = this.getComponentType();

            switch (this.corridorDirection) {
            case NORTH:
            default:
                StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
                StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
                break;

            case SOUTH:
                StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
                StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
                break;

            case WEST:
                StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
                break;

            case EAST:
                StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
            }

            if (this.isMultipleFloors) {
                if (random.nextBoolean()) {
                    StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.minX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                }

                if (random.nextBoolean()) {
                    StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
                }

                if (random.nextBoolean()) {
                    StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
                }

                if (random.nextBoolean()) {
                    StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.minX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                }
            }

        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
                return false;
            } else {
                IBlockState iblockdata = this.G_();

                if (this.isMultipleFloors) {
                    this.fillWithBlocks(world, structureboundingbox, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                    this.fillWithBlocks(world, structureboundingbox, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ - 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                    this.fillWithBlocks(world, structureboundingbox, this.boundingBox.minX + 1, this.boundingBox.maxY - 2, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                    this.fillWithBlocks(world, structureboundingbox, this.boundingBox.minX, this.boundingBox.maxY - 2, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                    this.fillWithBlocks(world, structureboundingbox, this.boundingBox.minX + 1, this.boundingBox.minY + 3, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 3, this.boundingBox.maxZ - 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                } else {
                    this.fillWithBlocks(world, structureboundingbox, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                    this.fillWithBlocks(world, structureboundingbox, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                }

                this.placeSupportPillar(world, structureboundingbox, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxY);
                this.placeSupportPillar(world, structureboundingbox, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.maxY);
                this.placeSupportPillar(world, structureboundingbox, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxY);
                this.placeSupportPillar(world, structureboundingbox, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.maxY);

                for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i) {
                    for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j) {
                        if (this.getBlockStateFromPos(world, i, this.boundingBox.minY - 1, j, structureboundingbox).getMaterial() == Material.AIR && this.getSkyBrightness(world, i, this.boundingBox.minY - 1, j, structureboundingbox) < 8) {
                            this.setBlockState(world, iblockdata, i, this.boundingBox.minY - 1, j, structureboundingbox);
                        }
                    }
                }

                return true;
            }
        }

        private void placeSupportPillar(World world, StructureBoundingBox structureboundingbox, int i, int j, int k, int l) {
            if (this.getBlockStateFromPos(world, i, l + 1, k, structureboundingbox).getMaterial() != Material.AIR) {
                this.fillWithBlocks(world, structureboundingbox, i, j, k, i, l, k, this.G_(), Blocks.AIR.getDefaultState(), false);
            }

        }
    }

    public static class Corridor extends WorldGenMineshaftPieces.c {

        private boolean hasRails;
        private boolean hasSpiders;
        private boolean spawnerPlaced;
        private int sectionCount;

        public Corridor() {}

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
            super.writeStructureToNBT(nbttagcompound);
            nbttagcompound.setBoolean("hr", this.hasRails);
            nbttagcompound.setBoolean("sc", this.hasSpiders);
            nbttagcompound.setBoolean("hps", this.spawnerPlaced);
            nbttagcompound.setInteger("Num", this.sectionCount);
        }

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.readStructureFromNBT(nbttagcompound, definedstructuremanager);
            this.hasRails = nbttagcompound.getBoolean("hr");
            this.hasSpiders = nbttagcompound.getBoolean("sc");
            this.spawnerPlaced = nbttagcompound.getBoolean("hps");
            this.sectionCount = nbttagcompound.getInteger("Num");
        }

        public Corridor(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection, MapGenMineshaft.Type worldgenmineshaft_type) {
            super(i, worldgenmineshaft_type);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
            this.hasRails = random.nextInt(3) == 0;
            this.hasSpiders = !this.hasRails && random.nextInt(23) == 0;
            if (this.getCoordBaseMode().getAxis() == EnumFacing.Axis.Z) {
                this.sectionCount = structureboundingbox.getZSize() / 5;
            } else {
                this.sectionCount = structureboundingbox.getXSize() / 5;
            }

        }

        public static StructureBoundingBox findCorridorSize(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection) {
            StructureBoundingBox structureboundingbox = new StructureBoundingBox(i, j, k, i, j + 2, k);

            int l;

            for (l = random.nextInt(3) + 2; l > 0; --l) {
                int i1 = l * 5;

                switch (enumdirection) {
                case NORTH:
                default:
                    structureboundingbox.maxX = i + 2;
                    structureboundingbox.minZ = k - (i1 - 1);
                    break;

                case SOUTH:
                    structureboundingbox.maxX = i + 2;
                    structureboundingbox.maxZ = k + (i1 - 1);
                    break;

                case WEST:
                    structureboundingbox.minX = i - (i1 - 1);
                    structureboundingbox.maxZ = k + 2;
                    break;

                case EAST:
                    structureboundingbox.maxX = i + (i1 - 1);
                    structureboundingbox.maxZ = k + 2;
                }

                if (StructureComponent.findIntersecting(list, structureboundingbox) == null) {
                    break;
                }
            }

            return l > 0 ? structureboundingbox : null;
        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            int i = this.getComponentType();
            int j = random.nextInt(4);
            EnumFacing enumdirection = this.getCoordBaseMode();

            if (enumdirection != null) {
                switch (enumdirection) {
                case NORTH:
                default:
                    if (j <= 1) {
                        StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.minX, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ - 1, enumdirection, i);
                    } else if (j == 2) {
                        StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ, EnumFacing.WEST, i);
                    } else {
                        StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ, EnumFacing.EAST, i);
                    }
                    break;

                case SOUTH:
                    if (j <= 1) {
                        StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.minX, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.maxZ + 1, enumdirection, i);
                    } else if (j == 2) {
                        StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.maxZ - 3, EnumFacing.WEST, i);
                    } else {
                        StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.maxZ - 3, EnumFacing.EAST, i);
                    }
                    break;

                case WEST:
                    if (j <= 1) {
                        StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ, enumdirection, i);
                    } else if (j == 2) {
                        StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.minX, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                    } else {
                        StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.minX, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                    }
                    break;

                case EAST:
                    if (j <= 1) {
                        StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ, enumdirection, i);
                    } else if (j == 2) {
                        StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                    } else {
                        StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                    }
                }
            }

            if (i < 8) {
                int k;
                int l;

                if (enumdirection != EnumFacing.NORTH && enumdirection != EnumFacing.SOUTH) {
                    for (k = this.boundingBox.minX + 3; k + 3 <= this.boundingBox.maxX; k += 5) {
                        l = random.nextInt(5);
                        if (l == 0) {
                            StructureMineshaftPieces.b(structurepiece, list, random, k, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i + 1);
                        } else if (l == 1) {
                            StructureMineshaftPieces.b(structurepiece, list, random, k, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i + 1);
                        }
                    }
                } else {
                    for (k = this.boundingBox.minZ + 3; k + 3 <= this.boundingBox.maxZ; k += 5) {
                        l = random.nextInt(5);
                        if (l == 0) {
                            StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, k, EnumFacing.WEST, i + 1);
                        } else if (l == 1) {
                            StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, k, EnumFacing.EAST, i + 1);
                        }
                    }
                }
            }

        }

        protected boolean generateChest(World world, StructureBoundingBox structureboundingbox, Random random, int i, int j, int k, ResourceLocation minecraftkey) {
            BlockPos blockposition = new BlockPos(this.getXWithOffset(i, k), this.getYWithOffset(j), this.getZWithOffset(i, k));

            if (structureboundingbox.isVecInside((Vec3i) blockposition) && world.getBlockState(blockposition).getMaterial() == Material.AIR && world.getBlockState(blockposition.down()).getMaterial() != Material.AIR) {
                IBlockState iblockdata = Blocks.RAIL.getDefaultState().withProperty(BlockRail.SHAPE, random.nextBoolean() ? BlockRailBase.EnumRailDirection.NORTH_SOUTH : BlockRailBase.EnumRailDirection.EAST_WEST);

                this.setBlockState(world, iblockdata, i, j, k, structureboundingbox);
                EntityMinecartChest entityminecartchest = new EntityMinecartChest(world, (double) ((float) blockposition.getX() + 0.5F), (double) ((float) blockposition.getY() + 0.5F), (double) ((float) blockposition.getZ() + 0.5F));

                entityminecartchest.setLootTable(minecraftkey, random.nextLong());
                world.spawnEntity(entityminecartchest);
                return true;
            } else {
                return false;
            }
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
                return false;
            } else {
                boolean flag = false;
                boolean flag1 = true;
                boolean flag2 = false;
                boolean flag3 = true;
                int i = this.sectionCount * 5 - 1;
                IBlockState iblockdata = this.G_();

                this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 2, 1, i, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                this.generateMaybeBox(world, structureboundingbox, random, 0.8F, 0, 2, 0, 2, 2, i, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false, 0);
                if (this.hasSpiders) {
                    this.generateMaybeBox(world, structureboundingbox, random, 0.6F, 0, 0, 0, 2, 1, i, Blocks.WEB.getDefaultState(), Blocks.AIR.getDefaultState(), false, 8);
                }

                int j;
                int k;

                for (j = 0; j < this.sectionCount; ++j) {
                    k = 2 + j * 5;
                    this.placeSupport(world, structureboundingbox, 0, 0, k, 2, 2, random);
                    this.placeCobWeb(world, structureboundingbox, random, 0.1F, 0, 2, k - 1);
                    this.placeCobWeb(world, structureboundingbox, random, 0.1F, 2, 2, k - 1);
                    this.placeCobWeb(world, structureboundingbox, random, 0.1F, 0, 2, k + 1);
                    this.placeCobWeb(world, structureboundingbox, random, 0.1F, 2, 2, k + 1);
                    this.placeCobWeb(world, structureboundingbox, random, 0.05F, 0, 2, k - 2);
                    this.placeCobWeb(world, structureboundingbox, random, 0.05F, 2, 2, k - 2);
                    this.placeCobWeb(world, structureboundingbox, random, 0.05F, 0, 2, k + 2);
                    this.placeCobWeb(world, structureboundingbox, random, 0.05F, 2, 2, k + 2);
                    if (random.nextInt(100) == 0) {
                        this.generateChest(world, structureboundingbox, random, 2, 0, k - 1, LootTableList.CHESTS_ABANDONED_MINESHAFT);
                    }

                    if (random.nextInt(100) == 0) {
                        this.generateChest(world, structureboundingbox, random, 0, 0, k + 1, LootTableList.CHESTS_ABANDONED_MINESHAFT);
                    }

                    if (this.hasSpiders && !this.spawnerPlaced) {
                        int l = this.getYWithOffset(0);
                        int i1 = k - 1 + random.nextInt(3);
                        int j1 = this.getXWithOffset(1, i1);
                        int k1 = this.getZWithOffset(1, i1);
                        BlockPos blockposition = new BlockPos(j1, l, k1);

                        if (structureboundingbox.isVecInside((Vec3i) blockposition) && this.getSkyBrightness(world, 1, 0, i1, structureboundingbox) < 8) {
                            this.spawnerPlaced = true;
                            world.setBlockState(blockposition, Blocks.MOB_SPAWNER.getDefaultState(), 2);
                            TileEntity tileentity = world.getTileEntity(blockposition);

                            if (tileentity instanceof TileEntityMobSpawner) {
                                ((TileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setEntityId(EntityList.getKey(EntityCaveSpider.class));
                            }
                        }
                    }
                }

                for (j = 0; j <= 2; ++j) {
                    for (k = 0; k <= i; ++k) {
                        boolean flag4 = true;
                        IBlockState iblockdata1 = this.getBlockStateFromPos(world, j, -1, k, structureboundingbox);

                        if (iblockdata1.getMaterial() == Material.AIR && this.getSkyBrightness(world, j, -1, k, structureboundingbox) < 8) {
                            boolean flag5 = true;

                            this.setBlockState(world, iblockdata, j, -1, k, structureboundingbox);
                        }
                    }
                }

                if (this.hasRails) {
                    IBlockState iblockdata2 = Blocks.RAIL.getDefaultState().withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH);

                    for (k = 0; k <= i; ++k) {
                        IBlockState iblockdata3 = this.getBlockStateFromPos(world, 1, -1, k, structureboundingbox);

                        if (iblockdata3.getMaterial() != Material.AIR && iblockdata3.isFullBlock()) {
                            float f = this.getSkyBrightness(world, 1, 0, k, structureboundingbox) > 8 ? 0.9F : 0.7F;

                            this.randomlyPlaceBlock(world, structureboundingbox, random, f, 1, 0, k, iblockdata2);
                        }
                    }
                }

                return true;
            }
        }

        private void placeSupport(World world, StructureBoundingBox structureboundingbox, int i, int j, int k, int l, int i1, Random random) {
            if (this.a(world, structureboundingbox, i, i1, l, k)) {
                IBlockState iblockdata = this.G_();
                IBlockState iblockdata1 = this.b();
                IBlockState iblockdata2 = Blocks.AIR.getDefaultState();

                this.fillWithBlocks(world, structureboundingbox, i, j, k, i, l - 1, k, iblockdata1, iblockdata2, false);
                this.fillWithBlocks(world, structureboundingbox, i1, j, k, i1, l - 1, k, iblockdata1, iblockdata2, false);
                if (random.nextInt(4) == 0) {
                    this.fillWithBlocks(world, structureboundingbox, i, l, k, i, l, k, iblockdata, iblockdata2, false);
                    this.fillWithBlocks(world, structureboundingbox, i1, l, k, i1, l, k, iblockdata, iblockdata2, false);
                } else {
                    this.fillWithBlocks(world, structureboundingbox, i, l, k, i1, l, k, iblockdata, iblockdata2, false);
                    this.randomlyPlaceBlock(world, structureboundingbox, random, 0.05F, i + 1, l, k - 1, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.NORTH));
                    this.randomlyPlaceBlock(world, structureboundingbox, random, 0.05F, i + 1, l, k + 1, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.SOUTH));
                }

            }
        }

        private void placeCobWeb(World world, StructureBoundingBox structureboundingbox, Random random, float f, int i, int j, int k) {
            if (this.getSkyBrightness(world, i, j, k, structureboundingbox) < 8) {
                this.randomlyPlaceBlock(world, structureboundingbox, random, f, i, j, k, Blocks.WEB.getDefaultState());
            }

        }
    }

    public static class Room extends WorldGenMineshaftPieces.c {

        private final List<StructureBoundingBox> connectedRooms = Lists.newLinkedList();

        public Room() {}

        public Room(int i, Random random, int j, int k, MapGenMineshaft.Type worldgenmineshaft_type) {
            super(i, worldgenmineshaft_type);
            this.a = worldgenmineshaft_type;
            this.boundingBox = new StructureBoundingBox(j, 50, k, j + 7 + random.nextInt(6), 54 + random.nextInt(6), k + 7 + random.nextInt(6));
        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            int i = this.getComponentType();
            int j = this.boundingBox.getYSize() - 3 - 1;

            if (j <= 0) {
                j = 1;
            }

            int k;
            WorldGenMineshaftPieces.c worldgenmineshaftpieces_c;
            StructureBoundingBox structureboundingbox;

            for (k = 0; k < this.boundingBox.getXSize(); k += 4) {
                k += random.nextInt(this.boundingBox.getXSize());
                if (k + 3 > this.boundingBox.getXSize()) {
                    break;
                }

                worldgenmineshaftpieces_c = StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.minX + k, this.boundingBox.minY + random.nextInt(j) + 1, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
                if (worldgenmineshaftpieces_c != null) {
                    structureboundingbox = worldgenmineshaftpieces_c.getBoundingBox();
                    this.connectedRooms.add(new StructureBoundingBox(structureboundingbox.minX, structureboundingbox.minY, this.boundingBox.minZ, structureboundingbox.maxX, structureboundingbox.maxY, this.boundingBox.minZ + 1));
                }
            }

            for (k = 0; k < this.boundingBox.getXSize(); k += 4) {
                k += random.nextInt(this.boundingBox.getXSize());
                if (k + 3 > this.boundingBox.getXSize()) {
                    break;
                }

                worldgenmineshaftpieces_c = StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.minX + k, this.boundingBox.minY + random.nextInt(j) + 1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
                if (worldgenmineshaftpieces_c != null) {
                    structureboundingbox = worldgenmineshaftpieces_c.getBoundingBox();
                    this.connectedRooms.add(new StructureBoundingBox(structureboundingbox.minX, structureboundingbox.minY, this.boundingBox.maxZ - 1, structureboundingbox.maxX, structureboundingbox.maxY, this.boundingBox.maxZ));
                }
            }

            for (k = 0; k < this.boundingBox.getZSize(); k += 4) {
                k += random.nextInt(this.boundingBox.getZSize());
                if (k + 3 > this.boundingBox.getZSize()) {
                    break;
                }

                worldgenmineshaftpieces_c = StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + random.nextInt(j) + 1, this.boundingBox.minZ + k, EnumFacing.WEST, i);
                if (worldgenmineshaftpieces_c != null) {
                    structureboundingbox = worldgenmineshaftpieces_c.getBoundingBox();
                    this.connectedRooms.add(new StructureBoundingBox(this.boundingBox.minX, structureboundingbox.minY, structureboundingbox.minZ, this.boundingBox.minX + 1, structureboundingbox.maxY, structureboundingbox.maxZ));
                }
            }

            for (k = 0; k < this.boundingBox.getZSize(); k += 4) {
                k += random.nextInt(this.boundingBox.getZSize());
                if (k + 3 > this.boundingBox.getZSize()) {
                    break;
                }

                worldgenmineshaftpieces_c = StructureMineshaftPieces.b(structurepiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + random.nextInt(j) + 1, this.boundingBox.minZ + k, EnumFacing.EAST, i);
                if (worldgenmineshaftpieces_c != null) {
                    structureboundingbox = worldgenmineshaftpieces_c.getBoundingBox();
                    this.connectedRooms.add(new StructureBoundingBox(this.boundingBox.maxX - 1, structureboundingbox.minY, structureboundingbox.minZ, this.boundingBox.maxX, structureboundingbox.maxY, structureboundingbox.maxZ));
                }
            }

        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
                return false;
            } else {
                this.fillWithBlocks(world, structureboundingbox, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.maxZ, Blocks.DIRT.getDefaultState(), Blocks.AIR.getDefaultState(), true);
                this.fillWithBlocks(world, structureboundingbox, this.boundingBox.minX, this.boundingBox.minY + 1, this.boundingBox.minZ, this.boundingBox.maxX, Math.min(this.boundingBox.minY + 3, this.boundingBox.maxY), this.boundingBox.maxZ, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                Iterator iterator = this.connectedRooms.iterator();

                while (iterator.hasNext()) {
                    StructureBoundingBox structureboundingbox1 = (StructureBoundingBox) iterator.next();

                    this.fillWithBlocks(world, structureboundingbox, structureboundingbox1.minX, structureboundingbox1.maxY - 2, structureboundingbox1.minZ, structureboundingbox1.maxX, structureboundingbox1.maxY, structureboundingbox1.maxZ, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                }

                this.randomlyRareFillWithBlocks(world, structureboundingbox, this.boundingBox.minX, this.boundingBox.minY + 4, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.AIR.getDefaultState(), false);
                return true;
            }
        }

        public void offset(int i, int j, int k) {
            super.offset(i, j, k);
            Iterator iterator = this.connectedRooms.iterator();

            while (iterator.hasNext()) {
                StructureBoundingBox structureboundingbox = (StructureBoundingBox) iterator.next();

                structureboundingbox.offset(i, j, k);
            }

        }

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
            super.writeStructureToNBT(nbttagcompound);
            NBTTagList nbttaglist = new NBTTagList();
            Iterator iterator = this.connectedRooms.iterator();

            while (iterator.hasNext()) {
                StructureBoundingBox structureboundingbox = (StructureBoundingBox) iterator.next();

                nbttaglist.appendTag(structureboundingbox.toNBTTagIntArray());
            }

            nbttagcompound.setTag("Entrances", nbttaglist);
        }

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.readStructureFromNBT(nbttagcompound, definedstructuremanager);
            NBTTagList nbttaglist = nbttagcompound.getTagList("Entrances", 11);

            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                this.connectedRooms.add(new StructureBoundingBox(nbttaglist.getIntArrayAt(i)));
            }

        }
    }

    abstract static class c extends StructureComponent {

        protected MapGenMineshaft.Type a;

        public c() {}

        public c(int i, MapGenMineshaft.Type worldgenmineshaft_type) {
            super(i);
            this.a = worldgenmineshaft_type;
        }

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
            nbttagcompound.setInteger("MST", this.a.ordinal());
        }

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            this.a = MapGenMineshaft.Type.byId(nbttagcompound.getInteger("MST"));
        }

        protected IBlockState G_() {
            switch (this.a) {
            case NORMAL:
            default:
                return Blocks.PLANKS.getDefaultState();

            case MESA:
                return Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK);
            }
        }

        protected IBlockState b() {
            switch (this.a) {
            case NORMAL:
            default:
                return Blocks.OAK_FENCE.getDefaultState();

            case MESA:
                return Blocks.DARK_OAK_FENCE.getDefaultState();
            }
        }

        protected boolean a(World world, StructureBoundingBox structureboundingbox, int i, int j, int k, int l) {
            for (int i1 = i; i1 <= j; ++i1) {
                if (this.getBlockStateFromPos(world, i1, k + 1, l, structureboundingbox).getMaterial() == Material.AIR) {
                    return false;
                }
            }

            return true;
        }
    }
}
