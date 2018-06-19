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

    private static final Pattern field_185921_a = Pattern.compile("^[a-z0-9_]+$");
    private static final Function<IProperty<?>, String> field_177626_b = new Function() {
        @Nullable
        public String a(@Nullable IProperty<?> iblockstate) {
            return iblockstate == null ? "<NULL>" : iblockstate.func_177701_a();
        }

        @Nullable
        public Object apply(@Nullable Object object) {
            return this.a((IProperty) object);
        }
    };
    private final Block field_177627_c;
    private final ImmutableSortedMap<String, IProperty<?>> field_177624_d;
    private final ImmutableList<IBlockState> field_177625_e;

    public BlockStateContainer(Block block, IProperty<?>... aiblockstate) {
        this.field_177627_c = block;
        HashMap hashmap = Maps.newHashMap();
        IProperty[] aiblockstate1 = aiblockstate;
        int i = aiblockstate.length;

        for (int j = 0; j < i; ++j) {
            IProperty iblockstate = aiblockstate1[j];

            func_185919_a(block, iblockstate);
            hashmap.put(iblockstate.func_177701_a(), iblockstate);
        }

        this.field_177624_d = ImmutableSortedMap.copyOf(hashmap);
        LinkedHashMap linkedhashmap = Maps.newLinkedHashMap();
        ArrayList arraylist = Lists.newArrayList();
        Iterable iterable = Cartesian.func_179321_a(this.func_177620_e());
        Iterator iterator = iterable.iterator();

        while (iterator.hasNext()) {
            List list = (List) iterator.next();
            Map map = MapPopulator.func_179400_b(this.field_177624_d.values(), list);
            BlockStateContainer.StateImplementation blockstatelist_blockdata = new BlockStateContainer.StateImplementation(block, ImmutableMap.copyOf(map), null);

            linkedhashmap.put(map, blockstatelist_blockdata);
            arraylist.add(blockstatelist_blockdata);
        }

        iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            BlockStateContainer.StateImplementation blockstatelist_blockdata1 = (BlockStateContainer.StateImplementation) iterator.next();

            blockstatelist_blockdata1.func_177235_a((Map) linkedhashmap);
        }

        this.field_177625_e = ImmutableList.copyOf(arraylist);
    }

    public static <T extends Comparable<T>> String func_185919_a(Block block, IProperty<T> iblockstate) {
        String s = iblockstate.func_177701_a();

        if (!BlockStateContainer.field_185921_a.matcher(s).matches()) {
            throw new IllegalArgumentException("Block: " + block.getClass() + " has invalidly named property: " + s);
        } else {
            Iterator iterator = iblockstate.func_177700_c().iterator();

            String s1;

            do {
                if (!iterator.hasNext()) {
                    return s;
                }

                Comparable comparable = (Comparable) iterator.next();

                s1 = iblockstate.func_177702_a(comparable);
            } while (BlockStateContainer.field_185921_a.matcher(s1).matches());

            throw new IllegalArgumentException("Block: " + block.getClass() + " has property: " + s + " with invalidly named value: " + s1);
        }
    }

    public ImmutableList<IBlockState> func_177619_a() {
        return this.field_177625_e;
    }

    private List<Iterable<Comparable<?>>> func_177620_e() {
        ArrayList arraylist = Lists.newArrayList();
        ImmutableCollection immutablecollection = this.field_177624_d.values();
        UnmodifiableIterator unmodifiableiterator = immutablecollection.iterator();

        while (unmodifiableiterator.hasNext()) {
            IProperty iblockstate = (IProperty) unmodifiableiterator.next();

            arraylist.add(iblockstate.func_177700_c());
        }

        return arraylist;
    }

    public IBlockState func_177621_b() {
        return (IBlockState) this.field_177625_e.get(0);
    }

    public Block func_177622_c() {
        return this.field_177627_c;
    }

    public Collection<IProperty<?>> func_177623_d() {
        return this.field_177624_d.values();
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("block", Block.field_149771_c.func_177774_c(this.field_177627_c)).add("properties", Iterables.transform(this.field_177624_d.values(), BlockStateContainer.field_177626_b)).toString();
    }

    @Nullable
    public IProperty<?> func_185920_a(String s) {
        return (IProperty) this.field_177624_d.get(s);
    }

    static class StateImplementation extends BlockStateBase {

        private final Block field_177239_a;
        private final ImmutableMap<IProperty<?>, Comparable<?>> field_177237_b;
        private ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> field_177238_c;

        private StateImplementation(Block block, ImmutableMap<IProperty<?>, Comparable<?>> immutablemap) {
            this.field_177239_a = block;
            this.field_177237_b = immutablemap;
        }

        public Collection<IProperty<?>> func_177227_a() {
            return Collections.unmodifiableCollection(this.field_177237_b.keySet());
        }

        public <T extends Comparable<T>> T func_177229_b(IProperty<T> iblockstate) {
            Comparable comparable = (Comparable) this.field_177237_b.get(iblockstate);

            if (comparable == null) {
                throw new IllegalArgumentException("Cannot get property " + iblockstate + " as it does not exist in " + this.field_177239_a.func_176194_O());
            } else {
                return (Comparable) iblockstate.func_177699_b().cast(comparable);
            }
        }

        public <T extends Comparable<T>, V extends T> IBlockState func_177226_a(IProperty<T> iblockstate, V v0) {
            Comparable comparable = (Comparable) this.field_177237_b.get(iblockstate);

            if (comparable == null) {
                throw new IllegalArgumentException("Cannot set property " + iblockstate + " as it does not exist in " + this.field_177239_a.func_176194_O());
            } else if (comparable == v0) {
                return this;
            } else {
                IBlockState iblockdata = (IBlockState) this.field_177238_c.get(iblockstate, v0);

                if (iblockdata == null) {
                    throw new IllegalArgumentException("Cannot set property " + iblockstate + " to " + v0 + " on block " + Block.field_149771_c.func_177774_c(this.field_177239_a) + ", it is not an allowed value");
                } else {
                    return iblockdata;
                }
            }
        }

        public ImmutableMap<IProperty<?>, Comparable<?>> func_177228_b() {
            return this.field_177237_b;
        }

        public Block func_177230_c() {
            return this.field_177239_a;
        }

        public boolean equals(Object object) {
            return this == object;
        }

        public int hashCode() {
            return this.field_177237_b.hashCode();
        }

        public void func_177235_a(Map<Map<IProperty<?>, Comparable<?>>, BlockStateContainer.StateImplementation> map) {
            if (this.field_177238_c != null) {
                throw new IllegalStateException();
            } else {
                HashBasedTable hashbasedtable = HashBasedTable.create();
                UnmodifiableIterator unmodifiableiterator = this.field_177237_b.entrySet().iterator();

                while (unmodifiableiterator.hasNext()) {
                    Entry entry = (Entry) unmodifiableiterator.next();
                    IProperty iblockstate = (IProperty) entry.getKey();
                    Iterator iterator = iblockstate.func_177700_c().iterator();

                    while (iterator.hasNext()) {
                        Comparable comparable = (Comparable) iterator.next();

                        if (comparable != entry.getValue()) {
                            hashbasedtable.put(iblockstate, comparable, map.get(this.func_177236_b(iblockstate, comparable)));
                        }
                    }
                }

                this.field_177238_c = ImmutableTable.copyOf(hashbasedtable);
            }
        }

        private Map<IProperty<?>, Comparable<?>> func_177236_b(IProperty<?> iblockstate, Comparable<?> comparable) {
            HashMap hashmap = Maps.newHashMap(this.field_177237_b);

            hashmap.put(iblockstate, comparable);
            return hashmap;
        }

        public Material func_185904_a() {
            return this.field_177239_a.func_149688_o(this);
        }

        public boolean func_185913_b() {
            return this.field_177239_a.func_149730_j(this);
        }

        public boolean func_189884_a(Entity entity) {
            return this.field_177239_a.func_189872_a((IBlockState) this, entity);
        }

        public int func_185891_c() {
            return this.field_177239_a.func_149717_k(this);
        }

        public int func_185906_d() {
            return this.field_177239_a.func_149750_m(this);
        }

        public boolean func_185916_f() {
            return this.field_177239_a.func_149710_n(this);
        }

        public MapColor func_185909_g(IBlockAccess iblockaccess, BlockPos blockposition) {
            return this.field_177239_a.func_180659_g(this, iblockaccess, blockposition);
        }

        public IBlockState func_185907_a(Rotation enumblockrotation) {
            return this.field_177239_a.func_185499_a((IBlockState) this, enumblockrotation);
        }

        public IBlockState func_185902_a(Mirror enumblockmirror) {
            return this.field_177239_a.func_185471_a((IBlockState) this, enumblockmirror);
        }

        public boolean func_185917_h() {
            return this.field_177239_a.func_149686_d((IBlockState) this);
        }

        public EnumBlockRenderType func_185901_i() {
            return this.field_177239_a.func_149645_b((IBlockState) this);
        }

        public boolean func_185898_k() {
            return this.field_177239_a.func_149637_q(this);
        }

        public boolean func_185915_l() {
            return this.field_177239_a.func_149721_r(this);
        }

        public boolean func_185897_m() {
            return this.field_177239_a.func_149744_f(this);
        }

        public int func_185911_a(IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
            return this.field_177239_a.func_180656_a((IBlockState) this, iblockaccess, blockposition, enumdirection);
        }

        public boolean func_185912_n() {
            return this.field_177239_a.func_149740_M(this);
        }

        public int func_185888_a(World world, BlockPos blockposition) {
            return this.field_177239_a.func_180641_l(this, world, blockposition);
        }

        public float func_185887_b(World world, BlockPos blockposition) {
            return this.field_177239_a.func_176195_g((IBlockState) this, world, blockposition);
        }

        public float func_185903_a(EntityPlayer entityhuman, World world, BlockPos blockposition) {
            return this.field_177239_a.func_180647_a(this, entityhuman, world, blockposition);
        }

        public int func_185893_b(IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
            return this.field_177239_a.func_176211_b(this, iblockaccess, blockposition, enumdirection);
        }

        public EnumPushReaction func_185905_o() {
            return this.field_177239_a.func_149656_h(this);
        }

        public IBlockState func_185899_b(IBlockAccess iblockaccess, BlockPos blockposition) {
            return this.field_177239_a.func_176221_a(this, iblockaccess, blockposition);
        }

        public boolean func_185914_p() {
            return this.field_177239_a.func_149662_c((IBlockState) this);
        }

        @Nullable
        public AxisAlignedBB func_185890_d(IBlockAccess iblockaccess, BlockPos blockposition) {
            return this.field_177239_a.func_180646_a((IBlockState) this, iblockaccess, blockposition);
        }

        public void func_185908_a(World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
            this.field_177239_a.func_185477_a(this, world, blockposition, axisalignedbb, list, entity, flag);
        }

        public AxisAlignedBB func_185900_c(IBlockAccess iblockaccess, BlockPos blockposition) {
            return this.field_177239_a.func_185496_a(this, iblockaccess, blockposition);
        }

        public RayTraceResult func_185910_a(World world, BlockPos blockposition, Vec3d vec3d, Vec3d vec3d1) {
            return this.field_177239_a.func_180636_a(this, world, blockposition, vec3d, vec3d1);
        }

        public boolean func_185896_q() {
            return this.field_177239_a.func_185481_k(this);
        }

        public Vec3d func_191059_e(IBlockAccess iblockaccess, BlockPos blockposition) {
            return this.field_177239_a.func_190949_e(this, iblockaccess, blockposition);
        }

        public boolean func_189547_a(World world, BlockPos blockposition, int i, int j) {
            return this.field_177239_a.func_189539_a(this, world, blockposition, i, j);
        }

        public void func_189546_a(World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
            this.field_177239_a.func_189540_a(this, world, blockposition, block, blockposition1);
        }

        public boolean func_191058_s() {
            return this.field_177239_a.func_176214_u(this);
        }

        public BlockFaceShape func_193401_d(IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
            return this.field_177239_a.func_193383_a(iblockaccess, (IBlockState) this, blockposition, enumdirection);
        }

        StateImplementation(Block block, ImmutableMap immutablemap, Object object) {
            this(block, immutablemap);
        }
    }
}
