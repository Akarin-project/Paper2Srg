package net.minecraft.block.state;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MapPopulator;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Cartesian;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStateContainer {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-z0-9_]+$");
    private static final Function<IProperty<?>, String> GET_NAME_FUNC = new Function() {
        @Nullable
        public String a(@Nullable IProperty<?> iblockstate) {
            return iblockstate == null ? "<NULL>" : iblockstate.getName();
        }

        @Nullable
        public Object apply(@Nullable Object object) {
            return this.a((IProperty) object);
        }
    };
    private final Block block;
    private final ImmutableSortedMap<String, IProperty<?>> properties;
    private final ImmutableList<IBlockState> validStates;

    public BlockStateContainer(Block block, IProperty<?>... aiblockstate) {
        this.block = block;
        HashMap hashmap = Maps.newHashMap();
        IProperty[] aiblockstate1 = aiblockstate;
        int i = aiblockstate.length;

        for (int j = 0; j < i; ++j) {
            IProperty iblockstate = aiblockstate1[j];

            validateProperty(block, iblockstate);
            hashmap.put(iblockstate.getName(), iblockstate);
        }

        this.properties = ImmutableSortedMap.copyOf(hashmap);
        LinkedHashMap linkedhashmap = Maps.newLinkedHashMap();
        ArrayList arraylist = Lists.newArrayList();
        Iterable iterable = Cartesian.cartesianProduct(this.getAllowedValues());
        Iterator iterator = iterable.iterator();

        while (iterator.hasNext()) {
            List list = (List) iterator.next();
            Map map = MapPopulator.createMap(this.properties.values(), list);
            BlockStateContainer.StateImplementation blockstatelist_blockdata = new BlockStateContainer.StateImplementation(block, ImmutableMap.copyOf(map), null);

            linkedhashmap.put(map, blockstatelist_blockdata);
            arraylist.add(blockstatelist_blockdata);
        }

        iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            BlockStateContainer.StateImplementation blockstatelist_blockdata1 = (BlockStateContainer.StateImplementation) iterator.next();

            blockstatelist_blockdata1.buildPropertyValueTable((Map) linkedhashmap);
        }

        this.validStates = ImmutableList.copyOf(arraylist);
    }

    public static <T extends Comparable<T>> String validateProperty(Block block, IProperty<T> iblockstate) {
        String s = iblockstate.getName();

        if (!BlockStateContainer.NAME_PATTERN.matcher(s).matches()) {
            throw new IllegalArgumentException("Block: " + block.getClass() + " has invalidly named property: " + s);
        } else {
            Iterator iterator = iblockstate.getAllowedValues().iterator();

            String s1;

            do {
                if (!iterator.hasNext()) {
                    return s;
                }

                Comparable comparable = (Comparable) iterator.next();

                s1 = iblockstate.getName(comparable);
            } while (BlockStateContainer.NAME_PATTERN.matcher(s1).matches());

            throw new IllegalArgumentException("Block: " + block.getClass() + " has property: " + s + " with invalidly named value: " + s1);
        }
    }

    public ImmutableList<IBlockState> getValidStates() {
        return this.validStates;
    }

    private List<Iterable<Comparable<?>>> getAllowedValues() {
        ArrayList arraylist = Lists.newArrayList();
        ImmutableCollection immutablecollection = this.properties.values();
        UnmodifiableIterator unmodifiableiterator = immutablecollection.iterator();

        while (unmodifiableiterator.hasNext()) {
            IProperty iblockstate = (IProperty) unmodifiableiterator.next();

            arraylist.add(iblockstate.getAllowedValues());
        }

        return arraylist;
    }

    public IBlockState getBaseState() {
        return (IBlockState) this.validStates.get(0);
    }

    public Block getBlock() {
        return this.block;
    }

    public Collection<IProperty<?>> getProperties() {
        return this.properties.values();
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("block", Block.REGISTRY.getNameForObject(this.block)).add("properties", Iterables.transform(this.properties.values(), BlockStateContainer.GET_NAME_FUNC)).toString();
    }

    @Nullable
    public IProperty<?> getProperty(String s) {
        return (IProperty) this.properties.get(s);
    }

    static class StateImplementation extends BlockStateBase {

        private final Block block;
        private final ImmutableMap<IProperty<?>, Comparable<?>> properties;
        private ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> propertyValueTable;

        private StateImplementation(Block block, ImmutableMap<IProperty<?>, Comparable<?>> immutablemap) {
            this.block = block;
            this.properties = immutablemap;
        }

        public Collection<IProperty<?>> getPropertyKeys() {
            return Collections.unmodifiableCollection(this.properties.keySet());
        }

        public <T extends Comparable<T>> T getValue(IProperty<T> iblockstate) {
            Comparable comparable = (Comparable) this.properties.get(iblockstate);

            if (comparable == null) {
                throw new IllegalArgumentException("Cannot get property " + iblockstate + " as it does not exist in " + this.block.getBlockState());
            } else {
                return (Comparable) iblockstate.getValueClass().cast(comparable);
            }
        }

        public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> iblockstate, V v0) {
            Comparable comparable = (Comparable) this.properties.get(iblockstate);

            if (comparable == null) {
                throw new IllegalArgumentException("Cannot set property " + iblockstate + " as it does not exist in " + this.block.getBlockState());
            } else if (comparable == v0) {
                return this;
            } else {
                IBlockState iblockdata = (IBlockState) this.propertyValueTable.get(iblockstate, v0);

                if (iblockdata == null) {
                    throw new IllegalArgumentException("Cannot set property " + iblockstate + " to " + v0 + " on block " + Block.REGISTRY.getNameForObject(this.block) + ", it is not an allowed value");
                } else {
                    return iblockdata;
                }
            }
        }

        public ImmutableMap<IProperty<?>, Comparable<?>> getProperties() {
            return this.properties;
        }

        public Block getBlock() {
            return this.block;
        }

        public boolean equals(Object object) {
            return this == object;
        }

        public int hashCode() {
            return this.properties.hashCode();
        }

        public void buildPropertyValueTable(Map<Map<IProperty<?>, Comparable<?>>, BlockStateContainer.StateImplementation> map) {
            if (this.propertyValueTable != null) {
                throw new IllegalStateException();
            } else {
                HashBasedTable hashbasedtable = HashBasedTable.create();
                UnmodifiableIterator unmodifiableiterator = this.properties.entrySet().iterator();

                while (unmodifiableiterator.hasNext()) {
                    Entry entry = (Entry) unmodifiableiterator.next();
                    IProperty iblockstate = (IProperty) entry.getKey();
                    Iterator iterator = iblockstate.getAllowedValues().iterator();

                    while (iterator.hasNext()) {
                        Comparable comparable = (Comparable) iterator.next();

                        if (comparable != entry.getValue()) {
                            hashbasedtable.put(iblockstate, comparable, map.get(this.getPropertiesWithValue(iblockstate, comparable)));
                        }
                    }
                }

                this.propertyValueTable = ImmutableTable.copyOf(hashbasedtable);
            }
        }

        private Map<IProperty<?>, Comparable<?>> getPropertiesWithValue(IProperty<?> iblockstate, Comparable<?> comparable) {
            HashMap hashmap = Maps.newHashMap(this.properties);

            hashmap.put(iblockstate, comparable);
            return hashmap;
        }

        public Material getMaterial() {
            return this.block.getMaterial(this);
        }

        public boolean isFullBlock() {
            return this.block.isFullBlock(this);
        }

        public boolean canEntitySpawn(Entity entity) {
            return this.block.canEntitySpawn((IBlockState) this, entity);
        }

        public int getLightOpacity() {
            return this.block.getLightOpacity(this);
        }

        public int getLightValue() {
            return this.block.getLightValue(this);
        }

        public boolean useNeighborBrightness() {
            return this.block.getUseNeighborBrightness(this);
        }

        public MapColor getMapColor(IBlockAccess iblockaccess, BlockPos blockposition) {
            return this.block.getMapColor(this, iblockaccess, blockposition);
        }

        public IBlockState withRotation(Rotation enumblockrotation) {
            return this.block.withRotation((IBlockState) this, enumblockrotation);
        }

        public IBlockState withMirror(Mirror enumblockmirror) {
            return this.block.withMirror((IBlockState) this, enumblockmirror);
        }

        public boolean isFullCube() {
            return this.block.isFullCube((IBlockState) this);
        }

        public EnumBlockRenderType getRenderType() {
            return this.block.getRenderType((IBlockState) this);
        }

        public boolean isBlockNormalCube() {
            return this.block.isBlockNormalCube(this);
        }

        public boolean isNormalCube() {
            return this.block.isNormalCube(this);
        }

        public boolean canProvidePower() {
            return this.block.canProvidePower(this);
        }

        public int getWeakPower(IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
            return this.block.getWeakPower((IBlockState) this, iblockaccess, blockposition, enumdirection);
        }

        public boolean hasComparatorInputOverride() {
            return this.block.hasComparatorInputOverride(this);
        }

        public int getComparatorInputOverride(World world, BlockPos blockposition) {
            return this.block.getComparatorInputOverride(this, world, blockposition);
        }

        public float getBlockHardness(World world, BlockPos blockposition) {
            return this.block.getBlockHardness((IBlockState) this, world, blockposition);
        }

        public float getPlayerRelativeBlockHardness(EntityPlayer entityhuman, World world, BlockPos blockposition) {
            return this.block.getPlayerRelativeBlockHardness(this, entityhuman, world, blockposition);
        }

        public int getStrongPower(IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
            return this.block.getStrongPower(this, iblockaccess, blockposition, enumdirection);
        }

        public EnumPushReaction getMobilityFlag() {
            return this.block.getMobilityFlag(this);
        }

        public IBlockState getActualState(IBlockAccess iblockaccess, BlockPos blockposition) {
            return this.block.getActualState(this, iblockaccess, blockposition);
        }

        public boolean isOpaqueCube() {
            return this.block.isOpaqueCube((IBlockState) this);
        }

        @Nullable
        public AxisAlignedBB getCollisionBoundingBox(IBlockAccess iblockaccess, BlockPos blockposition) {
            return this.block.getCollisionBoundingBox((IBlockState) this, iblockaccess, blockposition);
        }

        public void addCollisionBoxToList(World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
            this.block.addCollisionBoxToList(this, world, blockposition, axisalignedbb, list, entity, flag);
        }

        public AxisAlignedBB getBoundingBox(IBlockAccess iblockaccess, BlockPos blockposition) {
            return this.block.getBoundingBox(this, iblockaccess, blockposition);
        }

        public RayTraceResult collisionRayTrace(World world, BlockPos blockposition, Vec3d vec3d, Vec3d vec3d1) {
            return this.block.collisionRayTrace(this, world, blockposition, vec3d, vec3d1);
        }

        public boolean isTopSolid() {
            return this.block.isTopSolid(this);
        }

        public Vec3d getOffset(IBlockAccess iblockaccess, BlockPos blockposition) {
            return this.block.getOffset(this, iblockaccess, blockposition);
        }

        public boolean onBlockEventReceived(World world, BlockPos blockposition, int i, int j) {
            return this.block.eventReceived(this, world, blockposition, i, j);
        }

        public void neighborChanged(World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
            this.block.neighborChanged(this, world, blockposition, block, blockposition1);
        }

        public boolean causesSuffocation() {
            return this.block.causesSuffocation(this);
        }

        public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
            return this.block.getBlockFaceShape(iblockaccess, (IBlockState) this, blockposition, enumdirection);
        }

        StateImplementation(Block block, ImmutableMap immutablemap, Object object) {
            this(block, immutablemap);
        }
    }
}
