package net.minecraft.world.gen.structure;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.TemplateManager;

public abstract class StructureComponent {

    protected StructureBoundingBox boundingBox;
    @Nullable
    private EnumFacing coordBaseMode;
    private Mirror mirror;
    private Rotation rotation;
    protected int componentType;

    public StructureComponent() {}

    protected StructureComponent(int i) {
        this.componentType = i;
    }

    public final NBTTagCompound createStructureBaseNBT() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.setString("id", MapGenStructureIO.getStructureComponentName(this));
        nbttagcompound.setTag("BB", this.boundingBox.toNBTTagIntArray());
        EnumFacing enumdirection = this.getCoordBaseMode();

        nbttagcompound.setInteger("O", enumdirection == null ? -1 : enumdirection.getHorizontalIndex());
        nbttagcompound.setInteger("GD", this.componentType);
        this.writeStructureToNBT(nbttagcompound);
        return nbttagcompound;
    }

    protected abstract void writeStructureToNBT(NBTTagCompound nbttagcompound);

    public void readStructureBaseNBT(World world, NBTTagCompound nbttagcompound) {
        if (nbttagcompound.hasKey("BB")) {
            this.boundingBox = new StructureBoundingBox(nbttagcompound.getIntArray("BB"));
        }

        int i = nbttagcompound.getInteger("O");

        this.setCoordBaseMode(i == -1 ? null : EnumFacing.getHorizontal(i));
        this.componentType = nbttagcompound.getInteger("GD");
        this.readStructureFromNBT(nbttagcompound, world.getSaveHandler().getStructureTemplateManager());
    }

    protected abstract void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager);

    public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {}

    public abstract boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox);

    public StructureBoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    public int getComponentType() {
        return this.componentType;
    }

    public static StructureComponent findIntersecting(List<StructureComponent> list, StructureBoundingBox structureboundingbox) {
        Iterator iterator = list.iterator();

        StructureComponent structurepiece;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            structurepiece = (StructureComponent) iterator.next();
        } while (structurepiece.getBoundingBox() == null || !structurepiece.getBoundingBox().intersectsWith(structureboundingbox));

        return structurepiece;
    }

    protected boolean isLiquidInStructureBoundingBox(World world, StructureBoundingBox structureboundingbox) {
        int i = Math.max(this.boundingBox.minX - 1, structureboundingbox.minX);
        int j = Math.max(this.boundingBox.minY - 1, structureboundingbox.minY);
        int k = Math.max(this.boundingBox.minZ - 1, structureboundingbox.minZ);
        int l = Math.min(this.boundingBox.maxX + 1, structureboundingbox.maxX);
        int i1 = Math.min(this.boundingBox.maxY + 1, structureboundingbox.maxY);
        int j1 = Math.min(this.boundingBox.maxZ + 1, structureboundingbox.maxZ);
        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

        int k1;
        int l1;

        for (k1 = i; k1 <= l; ++k1) {
            for (l1 = k; l1 <= j1; ++l1) {
                if (world.getBlockState(blockposition_mutableblockposition.setPos(k1, j, l1)).getMaterial().isLiquid()) {
                    return true;
                }

                if (world.getBlockState(blockposition_mutableblockposition.setPos(k1, i1, l1)).getMaterial().isLiquid()) {
                    return true;
                }
            }
        }

        for (k1 = i; k1 <= l; ++k1) {
            for (l1 = j; l1 <= i1; ++l1) {
                if (world.getBlockState(blockposition_mutableblockposition.setPos(k1, l1, k)).getMaterial().isLiquid()) {
                    return true;
                }

                if (world.getBlockState(blockposition_mutableblockposition.setPos(k1, l1, j1)).getMaterial().isLiquid()) {
                    return true;
                }
            }
        }

        for (k1 = k; k1 <= j1; ++k1) {
            for (l1 = j; l1 <= i1; ++l1) {
                if (world.getBlockState(blockposition_mutableblockposition.setPos(i, l1, k1)).getMaterial().isLiquid()) {
                    return true;
                }

                if (world.getBlockState(blockposition_mutableblockposition.setPos(l, l1, k1)).getMaterial().isLiquid()) {
                    return true;
                }
            }
        }

        return false;
    }

    protected int getXWithOffset(int i, int j) {
        EnumFacing enumdirection = this.getCoordBaseMode();

        if (enumdirection == null) {
            return i;
        } else {
            switch (enumdirection) {
            case NORTH:
            case SOUTH:
                return this.boundingBox.minX + i;

            case WEST:
                return this.boundingBox.maxX - j;

            case EAST:
                return this.boundingBox.minX + j;

            default:
                return i;
            }
        }
    }

    protected int getYWithOffset(int i) {
        return this.getCoordBaseMode() == null ? i : i + this.boundingBox.minY;
    }

    protected int getZWithOffset(int i, int j) {
        EnumFacing enumdirection = this.getCoordBaseMode();

        if (enumdirection == null) {
            return j;
        } else {
            switch (enumdirection) {
            case NORTH:
                return this.boundingBox.maxZ - j;

            case SOUTH:
                return this.boundingBox.minZ + j;

            case WEST:
            case EAST:
                return this.boundingBox.minZ + i;

            default:
                return j;
            }
        }
    }

    protected void setBlockState(World world, IBlockState iblockdata, int i, int j, int k, StructureBoundingBox structureboundingbox) {
        BlockPos blockposition = new BlockPos(this.getXWithOffset(i, k), this.getYWithOffset(j), this.getZWithOffset(i, k));

        if (structureboundingbox.isVecInside(blockposition)) {
            if (this.mirror != Mirror.NONE) {
                iblockdata = iblockdata.withMirror(this.mirror);
            }

            if (this.rotation != Rotation.NONE) {
                iblockdata = iblockdata.withRotation(this.rotation);
            }

            world.setBlockState(blockposition, iblockdata, 2);
        }
    }

    protected IBlockState getBlockStateFromPos(World world, int i, int j, int k, StructureBoundingBox structureboundingbox) {
        int l = this.getXWithOffset(i, k);
        int i1 = this.getYWithOffset(j);
        int j1 = this.getZWithOffset(i, k);
        BlockPos blockposition = new BlockPos(l, i1, j1);

        return !structureboundingbox.isVecInside(blockposition) ? Blocks.AIR.getDefaultState() : world.getBlockState(blockposition);
    }

    protected int getSkyBrightness(World world, int i, int j, int k, StructureBoundingBox structureboundingbox) {
        int l = this.getXWithOffset(i, k);
        int i1 = this.getYWithOffset(j + 1);
        int j1 = this.getZWithOffset(i, k);
        BlockPos blockposition = new BlockPos(l, i1, j1);

        return !structureboundingbox.isVecInside(blockposition) ? EnumSkyBlock.SKY.defaultLightValue : world.getLightFor(EnumSkyBlock.SKY, blockposition);
    }

    protected void fillWithAir(World world, StructureBoundingBox structureboundingbox, int i, int j, int k, int l, int i1, int j1) {
        for (int k1 = j; k1 <= i1; ++k1) {
            for (int l1 = i; l1 <= l; ++l1) {
                for (int i2 = k; i2 <= j1; ++i2) {
                    this.setBlockState(world, Blocks.AIR.getDefaultState(), l1, k1, i2, structureboundingbox);
                }
            }
        }

    }

    protected void fillWithBlocks(World world, StructureBoundingBox structureboundingbox, int i, int j, int k, int l, int i1, int j1, IBlockState iblockdata, IBlockState iblockdata1, boolean flag) {
        for (int k1 = j; k1 <= i1; ++k1) {
            for (int l1 = i; l1 <= l; ++l1) {
                for (int i2 = k; i2 <= j1; ++i2) {
                    if (!flag || this.getBlockStateFromPos(world, l1, k1, i2, structureboundingbox).getMaterial() != Material.AIR) {
                        if (k1 != j && k1 != i1 && l1 != i && l1 != l && i2 != k && i2 != j1) {
                            this.setBlockState(world, iblockdata1, l1, k1, i2, structureboundingbox);
                        } else {
                            this.setBlockState(world, iblockdata, l1, k1, i2, structureboundingbox);
                        }
                    }
                }
            }
        }

    }

    protected void fillWithRandomizedBlocks(World world, StructureBoundingBox structureboundingbox, int i, int j, int k, int l, int i1, int j1, boolean flag, Random random, StructureComponent.BlockSelector structurepiece_structurepieceblockselector) {
        for (int k1 = j; k1 <= i1; ++k1) {
            for (int l1 = i; l1 <= l; ++l1) {
                for (int i2 = k; i2 <= j1; ++i2) {
                    if (!flag || this.getBlockStateFromPos(world, l1, k1, i2, structureboundingbox).getMaterial() != Material.AIR) {
                        structurepiece_structurepieceblockselector.selectBlocks(random, l1, k1, i2, k1 == j || k1 == i1 || l1 == i || l1 == l || i2 == k || i2 == j1);
                        this.setBlockState(world, structurepiece_structurepieceblockselector.getBlockState(), l1, k1, i2, structureboundingbox);
                    }
                }
            }
        }

    }

    protected void generateMaybeBox(World world, StructureBoundingBox structureboundingbox, Random random, float f, int i, int j, int k, int l, int i1, int j1, IBlockState iblockdata, IBlockState iblockdata1, boolean flag, int k1) {
        for (int l1 = j; l1 <= i1; ++l1) {
            for (int i2 = i; i2 <= l; ++i2) {
                for (int j2 = k; j2 <= j1; ++j2) {
                    if (random.nextFloat() <= f && (!flag || this.getBlockStateFromPos(world, i2, l1, j2, structureboundingbox).getMaterial() != Material.AIR) && (k1 <= 0 || this.getSkyBrightness(world, i2, l1, j2, structureboundingbox) < k1)) {
                        if (l1 != j && l1 != i1 && i2 != i && i2 != l && j2 != k && j2 != j1) {
                            this.setBlockState(world, iblockdata1, i2, l1, j2, structureboundingbox);
                        } else {
                            this.setBlockState(world, iblockdata, i2, l1, j2, structureboundingbox);
                        }
                    }
                }
            }
        }

    }

    protected void randomlyPlaceBlock(World world, StructureBoundingBox structureboundingbox, Random random, float f, int i, int j, int k, IBlockState iblockdata) {
        if (random.nextFloat() < f) {
            this.setBlockState(world, iblockdata, i, j, k, structureboundingbox);
        }

    }

    protected void randomlyRareFillWithBlocks(World world, StructureBoundingBox structureboundingbox, int i, int j, int k, int l, int i1, int j1, IBlockState iblockdata, boolean flag) {
        float f = l - i + 1;
        float f1 = i1 - j + 1;
        float f2 = j1 - k + 1;
        float f3 = i + f / 2.0F;
        float f4 = k + f2 / 2.0F;

        for (int k1 = j; k1 <= i1; ++k1) {
            float f5 = (k1 - j) / f1;

            for (int l1 = i; l1 <= l; ++l1) {
                float f6 = (l1 - f3) / (f * 0.5F);

                for (int i2 = k; i2 <= j1; ++i2) {
                    float f7 = (i2 - f4) / (f2 * 0.5F);

                    if (!flag || this.getBlockStateFromPos(world, l1, k1, i2, structureboundingbox).getMaterial() != Material.AIR) {
                        float f8 = f6 * f6 + f5 * f5 + f7 * f7;

                        if (f8 <= 1.05F) {
                            this.setBlockState(world, iblockdata, l1, k1, i2, structureboundingbox);
                        }
                    }
                }
            }
        }

    }

    protected void clearCurrentPositionBlocksUpwards(World world, int i, int j, int k, StructureBoundingBox structureboundingbox) {
        BlockPos blockposition = new BlockPos(this.getXWithOffset(i, k), this.getYWithOffset(j), this.getZWithOffset(i, k));

        if (structureboundingbox.isVecInside(blockposition)) {
            while (!world.isAirBlock(blockposition) && blockposition.getY() < 255) {
                world.setBlockState(blockposition, Blocks.AIR.getDefaultState(), 2);
                blockposition = blockposition.up();
            }

        }
    }

    protected void replaceAirAndLiquidDownwards(World world, IBlockState iblockdata, int i, int j, int k, StructureBoundingBox structureboundingbox) {
        int l = this.getXWithOffset(i, k);
        int i1 = this.getYWithOffset(j);
        int j1 = this.getZWithOffset(i, k);

        if (structureboundingbox.isVecInside((new BlockPos(l, i1, j1)))) {
            while ((world.isAirBlock(new BlockPos(l, i1, j1)) || world.getBlockState(new BlockPos(l, i1, j1)).getMaterial().isLiquid()) && i1 > 1) {
                world.setBlockState(new BlockPos(l, i1, j1), iblockdata, 2);
                --i1;
            }

        }
    }

    protected boolean generateChest(World world, StructureBoundingBox structureboundingbox, Random random, int i, int j, int k, ResourceLocation minecraftkey) {
        BlockPos blockposition = new BlockPos(this.getXWithOffset(i, k), this.getYWithOffset(j), this.getZWithOffset(i, k));

        return this.generateChest(world, structureboundingbox, random, blockposition, minecraftkey, (IBlockState) null);
    }

    protected boolean generateChest(World world, StructureBoundingBox structureboundingbox, Random random, BlockPos blockposition, ResourceLocation minecraftkey, @Nullable IBlockState iblockdata) {
        if (structureboundingbox.isVecInside(blockposition) && world.getBlockState(blockposition).getBlock() != Blocks.CHEST) {
            if (iblockdata == null) {
                iblockdata = Blocks.CHEST.correctFacing(world, blockposition, Blocks.CHEST.getDefaultState());
            }

            world.setBlockState(blockposition, iblockdata, 2);
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityChest) {
                ((TileEntityChest) tileentity).setLootTable(minecraftkey, random.nextLong());
            }

            return true;
        } else {
            return false;
        }
    }

    protected boolean createDispenser(World world, StructureBoundingBox structureboundingbox, Random random, int i, int j, int k, EnumFacing enumdirection, ResourceLocation minecraftkey) {
        BlockPos blockposition = new BlockPos(this.getXWithOffset(i, k), this.getYWithOffset(j), this.getZWithOffset(i, k));

        if (structureboundingbox.isVecInside(blockposition) && world.getBlockState(blockposition).getBlock() != Blocks.DISPENSER) {
            this.setBlockState(world, Blocks.DISPENSER.getDefaultState().withProperty(BlockDispenser.FACING, enumdirection), i, j, k, structureboundingbox);
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityDispenser) {
                ((TileEntityDispenser) tileentity).setLootTable(minecraftkey, random.nextLong());
            }

            return true;
        } else {
            return false;
        }
    }

    protected void generateDoor(World world, StructureBoundingBox structureboundingbox, Random random, int i, int j, int k, EnumFacing enumdirection, BlockDoor blockdoor) {
        this.setBlockState(world, blockdoor.getDefaultState().withProperty(BlockDoor.FACING, enumdirection), i, j, k, structureboundingbox);
        this.setBlockState(world, blockdoor.getDefaultState().withProperty(BlockDoor.FACING, enumdirection).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), i, j + 1, k, structureboundingbox);
    }

    public void offset(int i, int j, int k) {
        this.boundingBox.offset(i, j, k);
    }

    @Nullable
    public EnumFacing getCoordBaseMode() {
        return this.coordBaseMode;
    }

    public void setCoordBaseMode(@Nullable EnumFacing enumdirection) {
        this.coordBaseMode = enumdirection;
        if (enumdirection == null) {
            this.rotation = Rotation.NONE;
            this.mirror = Mirror.NONE;
        } else {
            switch (enumdirection) {
            case SOUTH:
                this.mirror = Mirror.LEFT_RIGHT;
                this.rotation = Rotation.NONE;
                break;

            case WEST:
                this.mirror = Mirror.LEFT_RIGHT;
                this.rotation = Rotation.CLOCKWISE_90;
                break;

            case EAST:
                this.mirror = Mirror.NONE;
                this.rotation = Rotation.CLOCKWISE_90;
                break;

            default:
                this.mirror = Mirror.NONE;
                this.rotation = Rotation.NONE;
            }
        }

    }

    public abstract static class BlockSelector {

        protected IBlockState blockstate;

        protected BlockSelector() {
            this.blockstate = Blocks.AIR.getDefaultState();
        }

        public abstract void selectBlocks(Random random, int i, int j, int k, boolean flag);

        public IBlockState getBlockState() {
            return this.blockstate;
        }
    }
}
