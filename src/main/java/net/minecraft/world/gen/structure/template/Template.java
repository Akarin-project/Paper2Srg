package net.minecraft.world.gen.structure.template;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.DefinedStructure.a;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.Mirror;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.Rotation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class Template {

    private final List<Template.BlockInfo> blocks = Lists.newArrayList();
    private final List<Template.EntityInfo> entities = Lists.newArrayList();
    private BlockPos size;
    private String author;

    public Template() {
        this.size = BlockPos.ORIGIN;
        this.author = "?";
    }

    public BlockPos getSize() {
        return this.size;
    }

    public void setAuthor(String s) {
        this.author = s;
    }

    public String getAuthor() {
        return this.author;
    }

    public void takeBlocksFromWorld(World world, BlockPos blockposition, BlockPos blockposition1, boolean flag, @Nullable Block block) {
        if (blockposition1.getX() >= 1 && blockposition1.getY() >= 1 && blockposition1.getZ() >= 1) {
            BlockPos blockposition2 = blockposition.add((Vec3i) blockposition1).add(-1, -1, -1);
            ArrayList arraylist = Lists.newArrayList();
            ArrayList arraylist1 = Lists.newArrayList();
            ArrayList arraylist2 = Lists.newArrayList();
            BlockPos blockposition3 = new BlockPos(Math.min(blockposition.getX(), blockposition2.getX()), Math.min(blockposition.getY(), blockposition2.getY()), Math.min(blockposition.getZ(), blockposition2.getZ()));
            BlockPos blockposition4 = new BlockPos(Math.max(blockposition.getX(), blockposition2.getX()), Math.max(blockposition.getY(), blockposition2.getY()), Math.max(blockposition.getZ(), blockposition2.getZ()));

            this.size = blockposition1;
            Iterator iterator = BlockPos.getAllInBoxMutable(blockposition3, blockposition4).iterator();

            while (iterator.hasNext()) {
                BlockPos.MutableBlockPos blockposition_mutableblockposition = (BlockPos.MutableBlockPos) iterator.next();
                BlockPos blockposition5 = blockposition_mutableblockposition.subtract(blockposition3);
                IBlockState iblockdata = world.getBlockState(blockposition_mutableblockposition);

                if (block == null || block != iblockdata.getBlock()) {
                    TileEntity tileentity = world.getTileEntity(blockposition_mutableblockposition);

                    if (tileentity != null) {
                        NBTTagCompound nbttagcompound = tileentity.writeToNBT(new NBTTagCompound());

                        nbttagcompound.removeTag("x");
                        nbttagcompound.removeTag("y");
                        nbttagcompound.removeTag("z");
                        arraylist1.add(new Template.BlockInfo(blockposition5, iblockdata, nbttagcompound));
                    } else if (!iblockdata.isFullBlock() && !iblockdata.isFullCube()) {
                        arraylist2.add(new Template.BlockInfo(blockposition5, iblockdata, (NBTTagCompound) null));
                    } else {
                        arraylist.add(new Template.BlockInfo(blockposition5, iblockdata, (NBTTagCompound) null));
                    }
                }
            }

            this.blocks.clear();
            this.blocks.addAll(arraylist);
            this.blocks.addAll(arraylist1);
            this.blocks.addAll(arraylist2);
            if (flag) {
                this.takeEntitiesFromWorld(world, blockposition3, blockposition4.add(1, 1, 1));
            } else {
                this.entities.clear();
            }

        }
    }

    private void takeEntitiesFromWorld(World world, BlockPos blockposition, BlockPos blockposition1) {
        List list = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockposition, blockposition1), new Predicate() {
            public boolean a(@Nullable Entity entity) {
                return !(entity instanceof EntityPlayer);
            }

            public boolean apply(@Nullable Object object) {
                return this.a((Entity) object);
            }
        });

        this.entities.clear();

        Vec3d vec3d;
        NBTTagCompound nbttagcompound;
        BlockPos blockposition2;

        for (Iterator iterator = list.iterator(); iterator.hasNext(); this.entities.add(new Template.EntityInfo(vec3d, blockposition2, nbttagcompound))) {
            Entity entity = (Entity) iterator.next();

            vec3d = new Vec3d(entity.posX - (double) blockposition.getX(), entity.posY - (double) blockposition.getY(), entity.posZ - (double) blockposition.getZ());
            nbttagcompound = new NBTTagCompound();
            entity.writeToNBTOptional(nbttagcompound);
            if (entity instanceof EntityPainting) {
                blockposition2 = ((EntityPainting) entity).getHangingPosition().subtract(blockposition);
            } else {
                blockposition2 = new BlockPos(vec3d);
            }
        }

    }

    public Map<BlockPos, String> getDataBlocks(BlockPos blockposition, PlacementSettings definedstructureinfo) {
        HashMap hashmap = Maps.newHashMap();
        StructureBoundingBox structureboundingbox = definedstructureinfo.getBoundingBox();
        Iterator iterator = this.blocks.iterator();

        while (iterator.hasNext()) {
            Template.BlockInfo definedstructure_blockinfo = (Template.BlockInfo) iterator.next();
            BlockPos blockposition1 = transformedBlockPos(definedstructureinfo, definedstructure_blockinfo.pos).add((Vec3i) blockposition);

            if (structureboundingbox == null || structureboundingbox.isVecInside((Vec3i) blockposition1)) {
                IBlockState iblockdata = definedstructure_blockinfo.blockState;

                if (iblockdata.getBlock() == Blocks.STRUCTURE_BLOCK && definedstructure_blockinfo.tileentityData != null) {
                    TileEntityStructure.Mode tileentitystructure_usagemode = TileEntityStructure.Mode.valueOf(definedstructure_blockinfo.tileentityData.getString("mode"));

                    if (tileentitystructure_usagemode == TileEntityStructure.Mode.DATA) {
                        hashmap.put(blockposition1, definedstructure_blockinfo.tileentityData.getString("metadata"));
                    }
                }
            }
        }

        return hashmap;
    }

    public BlockPos calculateConnectedPos(PlacementSettings definedstructureinfo, BlockPos blockposition, PlacementSettings definedstructureinfo1, BlockPos blockposition1) {
        BlockPos blockposition2 = transformedBlockPos(definedstructureinfo, blockposition);
        BlockPos blockposition3 = transformedBlockPos(definedstructureinfo1, blockposition1);

        return blockposition2.subtract(blockposition3);
    }

    public static BlockPos transformedBlockPos(PlacementSettings definedstructureinfo, BlockPos blockposition) {
        return transformedBlockPos(blockposition, definedstructureinfo.getMirror(), definedstructureinfo.getRotation());
    }

    public void addBlocksToWorldChunk(World world, BlockPos blockposition, PlacementSettings definedstructureinfo) {
        definedstructureinfo.setBoundingBoxFromChunk();
        this.addBlocksToWorld(world, blockposition, definedstructureinfo);
    }

    public void addBlocksToWorld(World world, BlockPos blockposition, PlacementSettings definedstructureinfo) {
        this.addBlocksToWorld(world, blockposition, new BlockRotationProcessor(blockposition, definedstructureinfo), definedstructureinfo, 2);
    }

    public void addBlocksToWorld(World world, BlockPos blockposition, PlacementSettings definedstructureinfo, int i) {
        this.addBlocksToWorld(world, blockposition, new BlockRotationProcessor(blockposition, definedstructureinfo), definedstructureinfo, i);
    }

    public void addBlocksToWorld(World world, BlockPos blockposition, @Nullable ITemplateProcessor definedstructureprocessor, PlacementSettings definedstructureinfo, int i) {
        if ((!this.blocks.isEmpty() || !definedstructureinfo.getIgnoreEntities() && !this.entities.isEmpty()) && this.size.getX() >= 1 && this.size.getY() >= 1 && this.size.getZ() >= 1) {
            Block block = definedstructureinfo.getReplacedBlock();
            StructureBoundingBox structureboundingbox = definedstructureinfo.getBoundingBox();
            Iterator iterator = this.blocks.iterator();

            Template.BlockInfo definedstructure_blockinfo;
            BlockPos blockposition1;

            while (iterator.hasNext()) {
                definedstructure_blockinfo = (Template.BlockInfo) iterator.next();
                blockposition1 = transformedBlockPos(definedstructureinfo, definedstructure_blockinfo.pos).add((Vec3i) blockposition);
                Template.BlockInfo definedstructure_blockinfo1 = definedstructureprocessor != null ? definedstructureprocessor.processBlock(world, blockposition1, definedstructure_blockinfo) : definedstructure_blockinfo;

                if (definedstructure_blockinfo1 != null) {
                    Block block1 = definedstructure_blockinfo1.blockState.getBlock();

                    if ((block == null || block != block1) && (!definedstructureinfo.getIgnoreStructureBlock() || block1 != Blocks.STRUCTURE_BLOCK) && (structureboundingbox == null || structureboundingbox.isVecInside((Vec3i) blockposition1))) {
                        IBlockState iblockdata = definedstructure_blockinfo1.blockState.withMirror(definedstructureinfo.getMirror());
                        IBlockState iblockdata1 = iblockdata.withRotation(definedstructureinfo.getRotation());
                        TileEntity tileentity;

                        if (definedstructure_blockinfo1.tileentityData != null) {
                            tileentity = world.getTileEntity(blockposition1);
                            if (tileentity != null) {
                                if (tileentity instanceof IInventory) {
                                    ((IInventory) tileentity).clear();
                                }

                                world.setBlockState(blockposition1, Blocks.BARRIER.getDefaultState(), 4);
                            }
                        }

                        if (world.setBlockState(blockposition1, iblockdata1, i) && definedstructure_blockinfo1.tileentityData != null) {
                            tileentity = world.getTileEntity(blockposition1);
                            if (tileentity != null) {
                                definedstructure_blockinfo1.tileentityData.setInteger("x", blockposition1.getX());
                                definedstructure_blockinfo1.tileentityData.setInteger("y", blockposition1.getY());
                                definedstructure_blockinfo1.tileentityData.setInteger("z", blockposition1.getZ());
                                tileentity.isLoadingStructure = true; // Paper
                                tileentity.readFromNBT(definedstructure_blockinfo1.tileentityData);
                                tileentity.mirror(definedstructureinfo.getMirror());
                                tileentity.rotate(definedstructureinfo.getRotation());
                                tileentity.isLoadingStructure = false; // Paper
                            }
                        }
                    }
                }
            }

            iterator = this.blocks.iterator();

            while (iterator.hasNext()) {
                definedstructure_blockinfo = (Template.BlockInfo) iterator.next();
                if (block == null || block != definedstructure_blockinfo.blockState.getBlock()) {
                    blockposition1 = transformedBlockPos(definedstructureinfo, definedstructure_blockinfo.pos).add((Vec3i) blockposition);
                    if (structureboundingbox == null || structureboundingbox.isVecInside((Vec3i) blockposition1)) {
                        world.notifyNeighborsRespectDebug(blockposition1, definedstructure_blockinfo.blockState.getBlock(), false);
                        if (definedstructure_blockinfo.tileentityData != null) {
                            TileEntity tileentity1 = world.getTileEntity(blockposition1);

                            if (tileentity1 != null) {
                                tileentity1.markDirty();
                            }
                        }
                    }
                }
            }

            if (!definedstructureinfo.getIgnoreEntities()) {
                this.addEntitiesToWorld(world, blockposition, definedstructureinfo.getMirror(), definedstructureinfo.getRotation(), structureboundingbox);
            }

        }
    }

    private void addEntitiesToWorld(World world, BlockPos blockposition, Mirror enumblockmirror, Rotation enumblockrotation, @Nullable StructureBoundingBox structureboundingbox) {
        Iterator iterator = this.entities.iterator();

        while (iterator.hasNext()) {
            Template.EntityInfo definedstructure_entityinfo = (Template.EntityInfo) iterator.next();
            BlockPos blockposition1 = transformedBlockPos(definedstructure_entityinfo.blockPos, enumblockmirror, enumblockrotation).add((Vec3i) blockposition);

            if (structureboundingbox == null || structureboundingbox.isVecInside((Vec3i) blockposition1)) {
                NBTTagCompound nbttagcompound = definedstructure_entityinfo.entityData;
                Vec3d vec3d = transformedVec3d(definedstructure_entityinfo.pos, enumblockmirror, enumblockrotation);
                Vec3d vec3d1 = vec3d.addVector((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ());
                NBTTagList nbttaglist = new NBTTagList();

                nbttaglist.appendTag(new NBTTagDouble(vec3d1.x));
                nbttaglist.appendTag(new NBTTagDouble(vec3d1.y));
                nbttaglist.appendTag(new NBTTagDouble(vec3d1.z));
                nbttagcompound.setTag("Pos", nbttaglist);
                nbttagcompound.setUniqueId("UUID", UUID.randomUUID());

                Entity entity;

                try {
                    entity = EntityList.createEntityFromNBT(nbttagcompound, world);
                } catch (Exception exception) {
                    entity = null;
                }

                if (entity != null) {
                    float f = entity.getMirroredYaw(enumblockmirror);

                    f += entity.rotationYaw - entity.getRotatedYaw(enumblockrotation);
                    entity.setLocationAndAngles(vec3d1.x, vec3d1.y, vec3d1.z, f, entity.rotationPitch);
                    world.spawnEntity(entity);
                }
            }
        }

    }

    public BlockPos transformedSize(Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case COUNTERCLOCKWISE_90:
        case CLOCKWISE_90:
            return new BlockPos(this.size.getZ(), this.size.getY(), this.size.getX());

        default:
            return this.size;
        }
    }

    private static BlockPos transformedBlockPos(BlockPos blockposition, Mirror enumblockmirror, Rotation enumblockrotation) {
        int i = blockposition.getX();
        int j = blockposition.getY();
        int k = blockposition.getZ();
        boolean flag = true;

        switch (enumblockmirror) {
        case LEFT_RIGHT:
            k = -k;
            break;

        case FRONT_BACK:
            i = -i;
            break;

        default:
            flag = false;
        }

        switch (enumblockrotation) {
        case COUNTERCLOCKWISE_90:
            return new BlockPos(k, j, -i);

        case CLOCKWISE_90:
            return new BlockPos(-k, j, i);

        case CLOCKWISE_180:
            return new BlockPos(-i, j, -k);

        default:
            return flag ? new BlockPos(i, j, k) : blockposition;
        }
    }

    private static Vec3d transformedVec3d(Vec3d vec3d, Mirror enumblockmirror, Rotation enumblockrotation) {
        double d0 = vec3d.x;
        double d1 = vec3d.y;
        double d2 = vec3d.z;
        boolean flag = true;

        switch (enumblockmirror) {
        case LEFT_RIGHT:
            d2 = 1.0D - d2;
            break;

        case FRONT_BACK:
            d0 = 1.0D - d0;
            break;

        default:
            flag = false;
        }

        switch (enumblockrotation) {
        case COUNTERCLOCKWISE_90:
            return new Vec3d(d2, d1, 1.0D - d0);

        case CLOCKWISE_90:
            return new Vec3d(1.0D - d2, d1, d0);

        case CLOCKWISE_180:
            return new Vec3d(1.0D - d0, d1, 1.0D - d2);

        default:
            return flag ? new Vec3d(d0, d1, d2) : vec3d;
        }
    }

    public BlockPos getZeroPositionWithTransform(BlockPos blockposition, Mirror enumblockmirror, Rotation enumblockrotation) {
        return getZeroPositionWithTransform(blockposition, enumblockmirror, enumblockrotation, this.getSize().getX(), this.getSize().getZ());
    }

    public static BlockPos getZeroPositionWithTransform(BlockPos blockposition, Mirror enumblockmirror, Rotation enumblockrotation, int i, int j) {
        --i;
        --j;
        int k = enumblockmirror == Mirror.FRONT_BACK ? i : 0;
        int l = enumblockmirror == Mirror.LEFT_RIGHT ? j : 0;
        BlockPos blockposition1 = blockposition;

        switch (enumblockrotation) {
        case COUNTERCLOCKWISE_90:
            blockposition1 = blockposition.add(l, 0, i - k);
            break;

        case CLOCKWISE_90:
            blockposition1 = blockposition.add(j - l, 0, k);
            break;

        case CLOCKWISE_180:
            blockposition1 = blockposition.add(i - k, 0, j - l);
            break;

        case NONE:
            blockposition1 = blockposition.add(k, 0, l);
        }

        return blockposition1;
    }

    public static void registerFixes(DataFixer dataconvertermanager) {
        dataconvertermanager.registerWalker(FixTypes.STRUCTURE, new IDataWalker() {
            public NBTTagCompound process(IDataFixer dataconverter, NBTTagCompound nbttagcompound, int i) {
                NBTTagList nbttaglist;
                int j;
                NBTTagCompound nbttagcompound1;

                if (nbttagcompound.hasKey("entities", 9)) {
                    nbttaglist = nbttagcompound.getTagList("entities", 10);

                    for (j = 0; j < nbttaglist.tagCount(); ++j) {
                        nbttagcompound1 = (NBTTagCompound) nbttaglist.get(j);
                        if (nbttagcompound1.hasKey("nbt", 10)) {
                            nbttagcompound1.setTag("nbt", dataconverter.process(FixTypes.ENTITY, nbttagcompound1.getCompoundTag("nbt"), i));
                        }
                    }
                }

                if (nbttagcompound.hasKey("blocks", 9)) {
                    nbttaglist = nbttagcompound.getTagList("blocks", 10);

                    for (j = 0; j < nbttaglist.tagCount(); ++j) {
                        nbttagcompound1 = (NBTTagCompound) nbttaglist.get(j);
                        if (nbttagcompound1.hasKey("nbt", 10)) {
                            nbttagcompound1.setTag("nbt", dataconverter.process(FixTypes.BLOCK_ENTITY, nbttagcompound1.getCompoundTag("nbt"), i));
                        }
                    }
                }

                return nbttagcompound;
            }
        });
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        DefinedStructure.a definedstructure_a = new DefinedStructure.a(null);
        NBTTagList nbttaglist = new NBTTagList();

        NBTTagCompound nbttagcompound1;

        for (Iterator iterator = this.blocks.iterator(); iterator.hasNext(); nbttaglist.appendTag(nbttagcompound1)) {
            Template.BlockInfo definedstructure_blockinfo = (Template.BlockInfo) iterator.next();

            nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setTag("pos", this.writeInts(new int[] { definedstructure_blockinfo.pos.getX(), definedstructure_blockinfo.pos.getY(), definedstructure_blockinfo.pos.getZ()}));
            nbttagcompound1.setInteger("state", definedstructure_a.a(definedstructure_blockinfo.blockState));
            if (definedstructure_blockinfo.tileentityData != null) {
                nbttagcompound1.setTag("nbt", definedstructure_blockinfo.tileentityData);
            }
        }

        NBTTagList nbttaglist1 = new NBTTagList();

        NBTTagCompound nbttagcompound2;

        for (Iterator iterator1 = this.entities.iterator(); iterator1.hasNext(); nbttaglist1.appendTag(nbttagcompound2)) {
            Template.EntityInfo definedstructure_entityinfo = (Template.EntityInfo) iterator1.next();

            nbttagcompound2 = new NBTTagCompound();
            nbttagcompound2.setTag("pos", this.writeDoubles(new double[] { definedstructure_entityinfo.pos.x, definedstructure_entityinfo.pos.y, definedstructure_entityinfo.pos.z}));
            nbttagcompound2.setTag("blockPos", this.writeInts(new int[] { definedstructure_entityinfo.blockPos.getX(), definedstructure_entityinfo.blockPos.getY(), definedstructure_entityinfo.blockPos.getZ()}));
            if (definedstructure_entityinfo.entityData != null) {
                nbttagcompound2.setTag("nbt", definedstructure_entityinfo.entityData);
            }
        }

        NBTTagList nbttaglist2 = new NBTTagList();
        Iterator iterator2 = definedstructure_a.iterator();

        while (iterator2.hasNext()) {
            IBlockState iblockdata = (IBlockState) iterator2.next();

            nbttaglist2.appendTag(NBTUtil.writeBlockState(new NBTTagCompound(), iblockdata));
        }

        nbttagcompound.setTag("palette", nbttaglist2);
        nbttagcompound.setTag("blocks", nbttaglist);
        nbttagcompound.setTag("entities", nbttaglist1);
        nbttagcompound.setTag("size", this.writeInts(new int[] { this.size.getX(), this.size.getY(), this.size.getZ()}));
        nbttagcompound.setString("author", this.author);
        nbttagcompound.setInteger("DataVersion", 1343);
        return nbttagcompound;
    }

    public void read(NBTTagCompound nbttagcompound) {
        this.blocks.clear();
        this.entities.clear();
        NBTTagList nbttaglist = nbttagcompound.getTagList("size", 3);

        this.size = new BlockPos(nbttaglist.getIntAt(0), nbttaglist.getIntAt(1), nbttaglist.getIntAt(2));
        this.author = nbttagcompound.getString("author");
        DefinedStructure.a definedstructure_a = new DefinedStructure.a(null);
        NBTTagList nbttaglist1 = nbttagcompound.getTagList("palette", 10);

        for (int i = 0; i < nbttaglist1.tagCount(); ++i) {
            definedstructure_a.a(NBTUtil.readBlockState(nbttaglist1.getCompoundTagAt(i)), i);
        }

        NBTTagList nbttaglist2 = nbttagcompound.getTagList("blocks", 10);

        for (int j = 0; j < nbttaglist2.tagCount(); ++j) {
            NBTTagCompound nbttagcompound1 = nbttaglist2.getCompoundTagAt(j);
            NBTTagList nbttaglist3 = nbttagcompound1.getTagList("pos", 3);
            BlockPos blockposition = new BlockPos(nbttaglist3.getIntAt(0), nbttaglist3.getIntAt(1), nbttaglist3.getIntAt(2));
            IBlockState iblockdata = definedstructure_a.a(nbttagcompound1.getInteger("state"));
            NBTTagCompound nbttagcompound2;

            if (nbttagcompound1.hasKey("nbt")) {
                nbttagcompound2 = nbttagcompound1.getCompoundTag("nbt");
            } else {
                nbttagcompound2 = null;
            }

            this.blocks.add(new Template.BlockInfo(blockposition, iblockdata, nbttagcompound2));
        }

        NBTTagList nbttaglist4 = nbttagcompound.getTagList("entities", 10);

        for (int k = 0; k < nbttaglist4.tagCount(); ++k) {
            NBTTagCompound nbttagcompound3 = nbttaglist4.getCompoundTagAt(k);
            NBTTagList nbttaglist5 = nbttagcompound3.getTagList("pos", 6);
            Vec3d vec3d = new Vec3d(nbttaglist5.getDoubleAt(0), nbttaglist5.getDoubleAt(1), nbttaglist5.getDoubleAt(2));
            NBTTagList nbttaglist6 = nbttagcompound3.getTagList("blockPos", 3);
            BlockPos blockposition1 = new BlockPos(nbttaglist6.getIntAt(0), nbttaglist6.getIntAt(1), nbttaglist6.getIntAt(2));

            if (nbttagcompound3.hasKey("nbt")) {
                NBTTagCompound nbttagcompound4 = nbttagcompound3.getCompoundTag("nbt");

                this.entities.add(new Template.EntityInfo(vec3d, blockposition1, nbttagcompound4));
            }
        }

    }

    private NBTTagList writeInts(int... aint) {
        NBTTagList nbttaglist = new NBTTagList();
        int[] aint1 = aint;
        int i = aint.length;

        for (int j = 0; j < i; ++j) {
            int k = aint1[j];

            nbttaglist.appendTag(new NBTTagInt(k));
        }

        return nbttaglist;
    }

    private NBTTagList writeDoubles(double... adouble) {
        NBTTagList nbttaglist = new NBTTagList();
        double[] adouble1 = adouble;
        int i = adouble.length;

        for (int j = 0; j < i; ++j) {
            double d0 = adouble1[j];

            nbttaglist.appendTag(new NBTTagDouble(d0));
        }

        return nbttaglist;
    }

    public static class EntityInfo {

        public final Vec3d pos;
        public final BlockPos blockPos;
        public final NBTTagCompound entityData;

        public EntityInfo(Vec3d vec3d, BlockPos blockposition, NBTTagCompound nbttagcompound) {
            this.pos = vec3d;
            this.blockPos = blockposition;
            this.entityData = nbttagcompound;
        }
    }

    public static class BlockInfo {

        public final BlockPos pos;
        public final IBlockState blockState;
        public final NBTTagCompound tileentityData;

        public BlockInfo(BlockPos blockposition, IBlockState iblockdata, @Nullable NBTTagCompound nbttagcompound) {
            this.pos = blockposition;
            this.blockState = iblockdata;
            this.tileentityData = nbttagcompound;
        }
    }

    static class a implements Iterable<IBlockState> {

        public static final IBlockState a = Blocks.AIR.getDefaultState();
        final ObjectIntIdentityMap<IBlockState> b;
        private int c;

        private a() {
            this.b = new ObjectIntIdentityMap(16);
        }

        public int a(IBlockState iblockdata) {
            int i = this.b.get(iblockdata);

            if (i == -1) {
                i = this.c++;
                this.b.put(iblockdata, i);
            }

            return i;
        }

        @Nullable
        public IBlockState a(int i) {
            IBlockState iblockdata = (IBlockState) this.b.getByValue(i);

            return iblockdata == null ? a : iblockdata; // Paper - decompile error - Blocks.AIR.getBlockData()
        }

        public Iterator<IBlockState> iterator() {
            return this.b.iterator();
        }

        public void a(IBlockState iblockdata, int i) {
            this.b.put(iblockdata, i);
        }

        a(Object object) {
            this();
        }
    }
}
