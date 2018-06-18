package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

public class StructureEndCityPieces {

    private static final PlacementSettings OVERWRITE = (new PlacementSettings()).setIgnoreEntities(true);
    private static final PlacementSettings INSERT = (new PlacementSettings()).setIgnoreEntities(true).setReplacedBlock(Blocks.AIR);
    private static final StructureEndCityPieces.IGenerator HOUSE_TOWER_GENERATOR = new StructureEndCityPieces.IGenerator() {
        public void init() {}

        public boolean generate(TemplateManager definedstructuremanager, int i, StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece, BlockPos blockposition, List<StructureComponent> list, Random random) {
            if (i > 8) {
                return false;
            } else {
                Rotation enumblockrotation = worldgenendcitypieces_piece.placeSettings.getRotation();
                StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece1 = StructureEndCityPieces.addHelper(list, StructureEndCityPieces.addPiece(definedstructuremanager, worldgenendcitypieces_piece, blockposition, "base_floor", enumblockrotation, true));
                int j = random.nextInt(3);

                if (j == 0) {
                    StructureEndCityPieces.addHelper(list, StructureEndCityPieces.addPiece(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(-1, 4, -1), "base_roof", enumblockrotation, true));
                } else if (j == 1) {
                    worldgenendcitypieces_piece1 = StructureEndCityPieces.addHelper(list, StructureEndCityPieces.addPiece(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(-1, 0, -1), "second_floor_2", enumblockrotation, false));
                    worldgenendcitypieces_piece1 = StructureEndCityPieces.addHelper(list, StructureEndCityPieces.addPiece(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(-1, 8, -1), "second_roof", enumblockrotation, false));
                    StructureEndCityPieces.recursiveChildren(definedstructuremanager, StructureEndCityPieces.TOWER_GENERATOR, i + 1, worldgenendcitypieces_piece1, (BlockPos) null, list, random);
                } else if (j == 2) {
                    worldgenendcitypieces_piece1 = StructureEndCityPieces.addHelper(list, StructureEndCityPieces.addPiece(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(-1, 0, -1), "second_floor_2", enumblockrotation, false));
                    worldgenendcitypieces_piece1 = StructureEndCityPieces.addHelper(list, StructureEndCityPieces.addPiece(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(-1, 4, -1), "third_floor_c", enumblockrotation, false));
                    worldgenendcitypieces_piece1 = StructureEndCityPieces.addHelper(list, StructureEndCityPieces.addPiece(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(-1, 8, -1), "third_roof", enumblockrotation, true));
                    StructureEndCityPieces.recursiveChildren(definedstructuremanager, StructureEndCityPieces.TOWER_GENERATOR, i + 1, worldgenendcitypieces_piece1, (BlockPos) null, list, random);
                }

                return true;
            }
        }
    };
    private static final List<Tuple<Rotation, BlockPos>> TOWER_BRIDGES = Lists.newArrayList(new Tuple[] { new Tuple(Rotation.NONE, new BlockPos(1, -1, 0)), new Tuple(Rotation.CLOCKWISE_90, new BlockPos(6, -1, 1)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, -1, 5)), new Tuple(Rotation.CLOCKWISE_180, new BlockPos(5, -1, 6))});
    private static final StructureEndCityPieces.IGenerator TOWER_GENERATOR = new StructureEndCityPieces.IGenerator() {
        public void init() {}

        public boolean generate(TemplateManager definedstructuremanager, int i, StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece, BlockPos blockposition, List<StructureComponent> list, Random random) {
            Rotation enumblockrotation = worldgenendcitypieces_piece.placeSettings.getRotation();
            StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece1 = StructureEndCityPieces.addHelper(list, StructureEndCityPieces.addPiece(definedstructuremanager, worldgenendcitypieces_piece, new BlockPos(3 + random.nextInt(2), -3, 3 + random.nextInt(2)), "tower_base", enumblockrotation, true));

            worldgenendcitypieces_piece1 = StructureEndCityPieces.addHelper(list, StructureEndCityPieces.addPiece(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(0, 7, 0), "tower_piece", enumblockrotation, true));
            StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece2 = random.nextInt(3) == 0 ? worldgenendcitypieces_piece1 : null;
            int j = 1 + random.nextInt(3);

            for (int k = 0; k < j; ++k) {
                worldgenendcitypieces_piece1 = StructureEndCityPieces.addHelper(list, StructureEndCityPieces.addPiece(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(0, 4, 0), "tower_piece", enumblockrotation, true));
                if (k < j - 1 && random.nextBoolean()) {
                    worldgenendcitypieces_piece2 = worldgenendcitypieces_piece1;
                }
            }

            if (worldgenendcitypieces_piece2 != null) {
                Iterator iterator = StructureEndCityPieces.TOWER_BRIDGES.iterator();

                while (iterator.hasNext()) {
                    Tuple tuple = (Tuple) iterator.next();

                    if (random.nextBoolean()) {
                        StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece3 = StructureEndCityPieces.addHelper(list, StructureEndCityPieces.addPiece(definedstructuremanager, worldgenendcitypieces_piece2, (BlockPos) tuple.getSecond(), "bridge_end", enumblockrotation.add((Rotation) tuple.getFirst()), true));

                        StructureEndCityPieces.recursiveChildren(definedstructuremanager, StructureEndCityPieces.TOWER_BRIDGE_GENERATOR, i + 1, worldgenendcitypieces_piece3, (BlockPos) null, list, random);
                    }
                }

                StructureEndCityPieces.addHelper(list, StructureEndCityPieces.addPiece(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(-1, 4, -1), "tower_top", enumblockrotation, true));
            } else {
                if (i != 7) {
                    return StructureEndCityPieces.recursiveChildren(definedstructuremanager, StructureEndCityPieces.FAT_TOWER_GENERATOR, i + 1, worldgenendcitypieces_piece1, (BlockPos) null, list, random);
                }

                StructureEndCityPieces.addHelper(list, StructureEndCityPieces.addPiece(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(-1, 4, -1), "tower_top", enumblockrotation, true));
            }

            return true;
        }
    };
    private static final StructureEndCityPieces.IGenerator TOWER_BRIDGE_GENERATOR = new StructureEndCityPieces.IGenerator() {
        public boolean a;

        public void init() {
            this.a = false;
        }

        public boolean generate(TemplateManager definedstructuremanager, int i, StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece, BlockPos blockposition, List<StructureComponent> list, Random random) {
            Rotation enumblockrotation = worldgenendcitypieces_piece.placeSettings.getRotation();
            int j = random.nextInt(4) + 1;
            StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece1 = StructureEndCityPieces.addHelper(list, StructureEndCityPieces.addPiece(definedstructuremanager, worldgenendcitypieces_piece, new BlockPos(0, 0, -4), "bridge_piece", enumblockrotation, true));

            worldgenendcitypieces_piece1.componentType = -1;
            byte b0 = 0;

            for (int k = 0; k < j; ++k) {
                if (random.nextBoolean()) {
                    worldgenendcitypieces_piece1 = StructureEndCityPieces.addHelper(list, StructureEndCityPieces.addPiece(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(0, b0, -4), "bridge_piece", enumblockrotation, true));
                    b0 = 0;
                } else {
                    if (random.nextBoolean()) {
                        worldgenendcitypieces_piece1 = StructureEndCityPieces.addHelper(list, StructureEndCityPieces.addPiece(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(0, b0, -4), "bridge_steep_stairs", enumblockrotation, true));
                    } else {
                        worldgenendcitypieces_piece1 = StructureEndCityPieces.addHelper(list, StructureEndCityPieces.addPiece(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(0, b0, -8), "bridge_gentle_stairs", enumblockrotation, true));
                    }

                    b0 = 4;
                }
            }

            if (!this.a && random.nextInt(10 - i) == 0) {
                StructureEndCityPieces.addHelper(list, StructureEndCityPieces.addPiece(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(-8 + random.nextInt(8), b0, -70 + random.nextInt(10)), "ship", enumblockrotation, true));
                this.a = true;
            } else if (!StructureEndCityPieces.recursiveChildren(definedstructuremanager, StructureEndCityPieces.HOUSE_TOWER_GENERATOR, i + 1, worldgenendcitypieces_piece1, new BlockPos(-3, b0 + 1, -11), list, random)) {
                return false;
            }

            worldgenendcitypieces_piece1 = StructureEndCityPieces.addHelper(list, StructureEndCityPieces.addPiece(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(4, b0, 0), "bridge_end", enumblockrotation.add(Rotation.CLOCKWISE_180), true));
            worldgenendcitypieces_piece1.componentType = -1;
            return true;
        }
    };
    private static final List<Tuple<Rotation, BlockPos>> FAT_TOWER_BRIDGES = Lists.newArrayList(new Tuple[] { new Tuple(Rotation.NONE, new BlockPos(4, -1, 0)), new Tuple(Rotation.CLOCKWISE_90, new BlockPos(12, -1, 4)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, -1, 8)), new Tuple(Rotation.CLOCKWISE_180, new BlockPos(8, -1, 12))});
    private static final StructureEndCityPieces.IGenerator FAT_TOWER_GENERATOR = new StructureEndCityPieces.IGenerator() {
        public void init() {}

        public boolean generate(TemplateManager definedstructuremanager, int i, StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece, BlockPos blockposition, List<StructureComponent> list, Random random) {
            Rotation enumblockrotation = worldgenendcitypieces_piece.placeSettings.getRotation();
            StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece1 = StructureEndCityPieces.addHelper(list, StructureEndCityPieces.addPiece(definedstructuremanager, worldgenendcitypieces_piece, new BlockPos(-3, 4, -3), "fat_tower_base", enumblockrotation, true));

            worldgenendcitypieces_piece1 = StructureEndCityPieces.addHelper(list, StructureEndCityPieces.addPiece(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(0, 4, 0), "fat_tower_middle", enumblockrotation, true));

            for (int j = 0; j < 2 && random.nextInt(3) != 0; ++j) {
                worldgenendcitypieces_piece1 = StructureEndCityPieces.addHelper(list, StructureEndCityPieces.addPiece(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(0, 8, 0), "fat_tower_middle", enumblockrotation, true));
                Iterator iterator = StructureEndCityPieces.FAT_TOWER_BRIDGES.iterator();

                while (iterator.hasNext()) {
                    Tuple tuple = (Tuple) iterator.next();

                    if (random.nextBoolean()) {
                        StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece2 = StructureEndCityPieces.addHelper(list, StructureEndCityPieces.addPiece(definedstructuremanager, worldgenendcitypieces_piece1, (BlockPos) tuple.getSecond(), "bridge_end", enumblockrotation.add((Rotation) tuple.getFirst()), true));

                        StructureEndCityPieces.recursiveChildren(definedstructuremanager, StructureEndCityPieces.TOWER_BRIDGE_GENERATOR, i + 1, worldgenendcitypieces_piece2, (BlockPos) null, list, random);
                    }
                }
            }

            StructureEndCityPieces.addHelper(list, StructureEndCityPieces.addPiece(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(-2, 8, -2), "fat_tower_top", enumblockrotation, true));
            return true;
        }
    };

    public static void registerPieces() {
        MapGenStructureIO.registerStructureComponent(StructureEndCityPieces.CityTemplate.class, "ECP");
    }

    private static StructureEndCityPieces.CityTemplate addPiece(TemplateManager definedstructuremanager, StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece, BlockPos blockposition, String s, Rotation enumblockrotation, boolean flag) {
        StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece1 = new StructureEndCityPieces.CityTemplate(definedstructuremanager, s, worldgenendcitypieces_piece.templatePosition, enumblockrotation, flag);
        BlockPos blockposition1 = worldgenendcitypieces_piece.template.calculateConnectedPos(worldgenendcitypieces_piece.placeSettings, blockposition, worldgenendcitypieces_piece1.placeSettings, BlockPos.ORIGIN);

        worldgenendcitypieces_piece1.offset(blockposition1.getX(), blockposition1.getY(), blockposition1.getZ());
        return worldgenendcitypieces_piece1;
    }

    public static void startHouseTower(TemplateManager definedstructuremanager, BlockPos blockposition, Rotation enumblockrotation, List<StructureComponent> list, Random random) {
        StructureEndCityPieces.FAT_TOWER_GENERATOR.init();
        StructureEndCityPieces.HOUSE_TOWER_GENERATOR.init();
        StructureEndCityPieces.TOWER_BRIDGE_GENERATOR.init();
        StructureEndCityPieces.TOWER_GENERATOR.init();
        StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece = addHelper(list, new StructureEndCityPieces.CityTemplate(definedstructuremanager, "base_floor", blockposition, enumblockrotation, true));

        worldgenendcitypieces_piece = addHelper(list, addPiece(definedstructuremanager, worldgenendcitypieces_piece, new BlockPos(-1, 0, -1), "second_floor", enumblockrotation, false));
        worldgenendcitypieces_piece = addHelper(list, addPiece(definedstructuremanager, worldgenendcitypieces_piece, new BlockPos(-1, 4, -1), "third_floor", enumblockrotation, false));
        worldgenendcitypieces_piece = addHelper(list, addPiece(definedstructuremanager, worldgenendcitypieces_piece, new BlockPos(-1, 8, -1), "third_roof", enumblockrotation, true));
        recursiveChildren(definedstructuremanager, StructureEndCityPieces.TOWER_GENERATOR, 1, worldgenendcitypieces_piece, (BlockPos) null, list, random);
    }

    private static StructureEndCityPieces.CityTemplate addHelper(List<StructureComponent> list, StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece) {
        list.add(worldgenendcitypieces_piece);
        return worldgenendcitypieces_piece;
    }

    private static boolean recursiveChildren(TemplateManager definedstructuremanager, StructureEndCityPieces.IGenerator worldgenendcitypieces_piecegenerator, int i, StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece, BlockPos blockposition, List<StructureComponent> list, Random random) {
        if (i > 8) {
            return false;
        } else {
            ArrayList arraylist = Lists.newArrayList();

            if (worldgenendcitypieces_piecegenerator.generate(definedstructuremanager, i, worldgenendcitypieces_piece, blockposition, arraylist, random)) {
                boolean flag = false;
                int j = random.nextInt();
                Iterator iterator = arraylist.iterator();

                while (iterator.hasNext()) {
                    StructureComponent structurepiece = (StructureComponent) iterator.next();

                    structurepiece.componentType = j;
                    StructureComponent structurepiece1 = StructureComponent.findIntersecting(list, structurepiece.getBoundingBox());

                    if (structurepiece1 != null && structurepiece1.componentType != worldgenendcitypieces_piece.componentType) {
                        flag = true;
                        break;
                    }
                }

                if (!flag) {
                    list.addAll(arraylist);
                    return true;
                }
            }

            return false;
        }
    }

    interface IGenerator {

        void init();

        boolean generate(TemplateManager definedstructuremanager, int i, StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece, BlockPos blockposition, List<StructureComponent> list, Random random);
    }

    public static class CityTemplate extends StructureComponentTemplate {

        private String pieceName;
        private Rotation rotation;
        private boolean overwrite;

        public CityTemplate() {}

        public CityTemplate(TemplateManager definedstructuremanager, String s, BlockPos blockposition, Rotation enumblockrotation, boolean flag) {
            super(0);
            this.pieceName = s;
            this.templatePosition = blockposition;
            this.rotation = enumblockrotation;
            this.overwrite = flag;
            this.loadTemplate(definedstructuremanager);
        }

        private void loadTemplate(TemplateManager definedstructuremanager) {
            Template definedstructure = definedstructuremanager.getTemplate((MinecraftServer) null, new ResourceLocation("endcity/" + this.pieceName));
            PlacementSettings definedstructureinfo = (this.overwrite ? StructureEndCityPieces.OVERWRITE : StructureEndCityPieces.INSERT).copy().setRotation(this.rotation);

            this.setup(definedstructure, this.templatePosition, definedstructureinfo);
        }

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
            super.writeStructureToNBT(nbttagcompound);
            nbttagcompound.setString("Template", this.pieceName);
            nbttagcompound.setString("Rot", this.rotation.name());
            nbttagcompound.setBoolean("OW", this.overwrite);
        }

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.readStructureFromNBT(nbttagcompound, definedstructuremanager);
            this.pieceName = nbttagcompound.getString("Template");
            this.rotation = Rotation.valueOf(nbttagcompound.getString("Rot"));
            this.overwrite = nbttagcompound.getBoolean("OW");
            this.loadTemplate(definedstructuremanager);
        }

        protected void handleDataMarker(String s, BlockPos blockposition, World world, Random random, StructureBoundingBox structureboundingbox) {
            if (s.startsWith("Chest")) {
                BlockPos blockposition1 = blockposition.down();

                if (structureboundingbox.isVecInside((Vec3i) blockposition1)) {
                    TileEntity tileentity = world.getTileEntity(blockposition1);

                    if (tileentity instanceof TileEntityChest) {
                        ((TileEntityChest) tileentity).setLootTable(LootTableList.CHESTS_END_CITY_TREASURE, random.nextLong());
                    }
                }
            } else if (s.startsWith("Sentry")) {
                EntityShulker entityshulker = new EntityShulker(world);

                entityshulker.setPosition((double) blockposition.getX() + 0.5D, (double) blockposition.getY() + 0.5D, (double) blockposition.getZ() + 0.5D);
                entityshulker.setAttachmentPos(blockposition);
                world.spawnEntity(entityshulker);
            } else if (s.startsWith("Elytra")) {
                EntityItemFrame entityitemframe = new EntityItemFrame(world, blockposition, this.rotation.rotate(EnumFacing.SOUTH));

                entityitemframe.setDisplayedItem(new ItemStack(Items.ELYTRA));
                world.spawnEntity(entityitemframe);
            }

        }
    }
}
