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

    private final List<Template.BlockInfo> field_186270_a = Lists.newArrayList();
    private final List<Template.EntityInfo> field_186271_b = Lists.newArrayList();
    private BlockPos field_186272_c;
    private String field_186273_d;

    public Template() {
        this.field_186272_c = BlockPos.field_177992_a;
        this.field_186273_d = "?";
    }

    public BlockPos func_186259_a() {
        return this.field_186272_c;
    }

    public void func_186252_a(String s) {
        this.field_186273_d = s;
    }

    public String func_186261_b() {
        return this.field_186273_d;
    }

    public void func_186254_a(World world, BlockPos blockposition, BlockPos blockposition1, boolean flag, @Nullable Block block) {
        if (blockposition1.func_177958_n() >= 1 && blockposition1.func_177956_o() >= 1 && blockposition1.func_177952_p() >= 1) {
            BlockPos blockposition2 = blockposition.func_177971_a(blockposition1).func_177982_a(-1, -1, -1);
            ArrayList arraylist = Lists.newArrayList();
            ArrayList arraylist1 = Lists.newArrayList();
            ArrayList arraylist2 = Lists.newArrayList();
            BlockPos blockposition3 = new BlockPos(Math.min(blockposition.func_177958_n(), blockposition2.func_177958_n()), Math.min(blockposition.func_177956_o(), blockposition2.func_177956_o()), Math.min(blockposition.func_177952_p(), blockposition2.func_177952_p()));
            BlockPos blockposition4 = new BlockPos(Math.max(blockposition.func_177958_n(), blockposition2.func_177958_n()), Math.max(blockposition.func_177956_o(), blockposition2.func_177956_o()), Math.max(blockposition.func_177952_p(), blockposition2.func_177952_p()));

            this.field_186272_c = blockposition1;
            Iterator iterator = BlockPos.func_177975_b(blockposition3, blockposition4).iterator();

            while (iterator.hasNext()) {
                BlockPos.MutableBlockPos blockposition_mutableblockposition = (BlockPos.MutableBlockPos) iterator.next();
                BlockPos blockposition5 = blockposition_mutableblockposition.func_177973_b(blockposition3);
                IBlockState iblockdata = world.func_180495_p(blockposition_mutableblockposition);

                if (block == null || block != iblockdata.func_177230_c()) {
                    TileEntity tileentity = world.func_175625_s(blockposition_mutableblockposition);

                    if (tileentity != null) {
                        NBTTagCompound nbttagcompound = tileentity.func_189515_b(new NBTTagCompound());

                        nbttagcompound.func_82580_o("x");
                        nbttagcompound.func_82580_o("y");
                        nbttagcompound.func_82580_o("z");
                        arraylist1.add(new Template.BlockInfo(blockposition5, iblockdata, nbttagcompound));
                    } else if (!iblockdata.func_185913_b() && !iblockdata.func_185917_h()) {
                        arraylist2.add(new Template.BlockInfo(blockposition5, iblockdata, (NBTTagCompound) null));
                    } else {
                        arraylist.add(new Template.BlockInfo(blockposition5, iblockdata, (NBTTagCompound) null));
                    }
                }
            }

            this.field_186270_a.clear();
            this.field_186270_a.addAll(arraylist);
            this.field_186270_a.addAll(arraylist1);
            this.field_186270_a.addAll(arraylist2);
            if (flag) {
                this.func_186255_a(world, blockposition3, blockposition4.func_177982_a(1, 1, 1));
            } else {
                this.field_186271_b.clear();
            }

        }
    }

    private void func_186255_a(World world, BlockPos blockposition, BlockPos blockposition1) {
        List list = world.func_175647_a(Entity.class, new AxisAlignedBB(blockposition, blockposition1), new Predicate() {
            public boolean a(@Nullable Entity entity) {
                return !(entity instanceof EntityPlayer);
            }

            @Override
            public boolean apply(@Nullable Object object) {
                return this.a((Entity) object);
            }
        });

        this.field_186271_b.clear();

        Vec3d vec3d;
        NBTTagCompound nbttagcompound;
        BlockPos blockposition2;

        for (Iterator iterator = list.iterator(); iterator.hasNext(); this.field_186271_b.add(new Template.EntityInfo(vec3d, blockposition2, nbttagcompound))) {
            Entity entity = (Entity) iterator.next();

            vec3d = new Vec3d(entity.field_70165_t - blockposition.func_177958_n(), entity.field_70163_u - blockposition.func_177956_o(), entity.field_70161_v - blockposition.func_177952_p());
            nbttagcompound = new NBTTagCompound();
            entity.func_70039_c(nbttagcompound);
            if (entity instanceof EntityPainting) {
                blockposition2 = ((EntityPainting) entity).func_174857_n().func_177973_b(blockposition);
            } else {
                blockposition2 = new BlockPos(vec3d);
            }
        }

    }

    public Map<BlockPos, String> func_186258_a(BlockPos blockposition, PlacementSettings definedstructureinfo) {
        HashMap hashmap = Maps.newHashMap();
        StructureBoundingBox structureboundingbox = definedstructureinfo.func_186213_g();
        Iterator iterator = this.field_186270_a.iterator();

        while (iterator.hasNext()) {
            Template.BlockInfo definedstructure_blockinfo = (Template.BlockInfo) iterator.next();
            BlockPos blockposition1 = func_186266_a(definedstructureinfo, definedstructure_blockinfo.field_186242_a).func_177971_a(blockposition);

            if (structureboundingbox == null || structureboundingbox.func_175898_b(blockposition1)) {
                IBlockState iblockdata = definedstructure_blockinfo.field_186243_b;

                if (iblockdata.func_177230_c() == Blocks.field_185779_df && definedstructure_blockinfo.field_186244_c != null) {
                    TileEntityStructure.Mode tileentitystructure_usagemode = TileEntityStructure.Mode.valueOf(definedstructure_blockinfo.field_186244_c.func_74779_i("mode"));

                    if (tileentitystructure_usagemode == TileEntityStructure.Mode.DATA) {
                        hashmap.put(blockposition1, definedstructure_blockinfo.field_186244_c.func_74779_i("metadata"));
                    }
                }
            }
        }

        return hashmap;
    }

    public BlockPos func_186262_a(PlacementSettings definedstructureinfo, BlockPos blockposition, PlacementSettings definedstructureinfo1, BlockPos blockposition1) {
        BlockPos blockposition2 = func_186266_a(definedstructureinfo, blockposition);
        BlockPos blockposition3 = func_186266_a(definedstructureinfo1, blockposition1);

        return blockposition2.func_177973_b(blockposition3);
    }

    public static BlockPos func_186266_a(PlacementSettings definedstructureinfo, BlockPos blockposition) {
        return func_186268_a(blockposition, definedstructureinfo.func_186212_b(), definedstructureinfo.func_186215_c());
    }

    public void func_186260_a(World world, BlockPos blockposition, PlacementSettings definedstructureinfo) {
        definedstructureinfo.func_186224_i();
        this.func_186253_b(world, blockposition, definedstructureinfo);
    }

    public void func_186253_b(World world, BlockPos blockposition, PlacementSettings definedstructureinfo) {
        this.func_189960_a(world, blockposition, new BlockRotationProcessor(blockposition, definedstructureinfo), definedstructureinfo, 2);
    }

    public void func_189962_a(World world, BlockPos blockposition, PlacementSettings definedstructureinfo, int i) {
        this.func_189960_a(world, blockposition, new BlockRotationProcessor(blockposition, definedstructureinfo), definedstructureinfo, i);
    }

    public void func_189960_a(World world, BlockPos blockposition, @Nullable ITemplateProcessor definedstructureprocessor, PlacementSettings definedstructureinfo, int i) {
        if ((!this.field_186270_a.isEmpty() || !definedstructureinfo.func_186221_e() && !this.field_186271_b.isEmpty()) && this.field_186272_c.func_177958_n() >= 1 && this.field_186272_c.func_177956_o() >= 1 && this.field_186272_c.func_177952_p() >= 1) {
            Block block = definedstructureinfo.func_186219_f();
            StructureBoundingBox structureboundingbox = definedstructureinfo.func_186213_g();
            Iterator iterator = this.field_186270_a.iterator();

            Template.BlockInfo definedstructure_blockinfo;
            BlockPos blockposition1;

            while (iterator.hasNext()) {
                definedstructure_blockinfo = (Template.BlockInfo) iterator.next();
                blockposition1 = func_186266_a(definedstructureinfo, definedstructure_blockinfo.field_186242_a).func_177971_a(blockposition);
                Template.BlockInfo definedstructure_blockinfo1 = definedstructureprocessor != null ? definedstructureprocessor.func_189943_a(world, blockposition1, definedstructure_blockinfo) : definedstructure_blockinfo;

                if (definedstructure_blockinfo1 != null) {
                    Block block1 = definedstructure_blockinfo1.field_186243_b.func_177230_c();

                    if ((block == null || block != block1) && (!definedstructureinfo.func_186227_h() || block1 != Blocks.field_185779_df) && (structureboundingbox == null || structureboundingbox.func_175898_b(blockposition1))) {
                        IBlockState iblockdata = definedstructure_blockinfo1.field_186243_b.func_185902_a(definedstructureinfo.func_186212_b());
                        IBlockState iblockdata1 = iblockdata.func_185907_a(definedstructureinfo.func_186215_c());
                        TileEntity tileentity;

                        if (definedstructure_blockinfo1.field_186244_c != null) {
                            tileentity = world.func_175625_s(blockposition1);
                            if (tileentity != null) {
                                if (tileentity instanceof IInventory) {
                                    ((IInventory) tileentity).func_174888_l();
                                }

                                world.func_180501_a(blockposition1, Blocks.field_180401_cv.func_176223_P(), 4);
                            }
                        }

                        if (world.func_180501_a(blockposition1, iblockdata1, i) && definedstructure_blockinfo1.field_186244_c != null) {
                            tileentity = world.func_175625_s(blockposition1);
                            if (tileentity != null) {
                                definedstructure_blockinfo1.field_186244_c.func_74768_a("x", blockposition1.func_177958_n());
                                definedstructure_blockinfo1.field_186244_c.func_74768_a("y", blockposition1.func_177956_o());
                                definedstructure_blockinfo1.field_186244_c.func_74768_a("z", blockposition1.func_177952_p());
                                tileentity.isLoadingStructure = true; // Paper
                                tileentity.func_145839_a(definedstructure_blockinfo1.field_186244_c);
                                tileentity.func_189668_a(definedstructureinfo.func_186212_b());
                                tileentity.func_189667_a(definedstructureinfo.func_186215_c());
                                tileentity.isLoadingStructure = false; // Paper
                            }
                        }
                    }
                }
            }

            iterator = this.field_186270_a.iterator();

            while (iterator.hasNext()) {
                definedstructure_blockinfo = (Template.BlockInfo) iterator.next();
                if (block == null || block != definedstructure_blockinfo.field_186243_b.func_177230_c()) {
                    blockposition1 = func_186266_a(definedstructureinfo, definedstructure_blockinfo.field_186242_a).func_177971_a(blockposition);
                    if (structureboundingbox == null || structureboundingbox.func_175898_b(blockposition1)) {
                        world.func_175722_b(blockposition1, definedstructure_blockinfo.field_186243_b.func_177230_c(), false);
                        if (definedstructure_blockinfo.field_186244_c != null) {
                            TileEntity tileentity1 = world.func_175625_s(blockposition1);

                            if (tileentity1 != null) {
                                tileentity1.func_70296_d();
                            }
                        }
                    }
                }
            }

            if (!definedstructureinfo.func_186221_e()) {
                this.func_186263_a(world, blockposition, definedstructureinfo.func_186212_b(), definedstructureinfo.func_186215_c(), structureboundingbox);
            }

        }
    }

    private void func_186263_a(World world, BlockPos blockposition, Mirror enumblockmirror, Rotation enumblockrotation, @Nullable StructureBoundingBox structureboundingbox) {
        Iterator iterator = this.field_186271_b.iterator();

        while (iterator.hasNext()) {
            Template.EntityInfo definedstructure_entityinfo = (Template.EntityInfo) iterator.next();
            BlockPos blockposition1 = func_186268_a(definedstructure_entityinfo.field_186248_b, enumblockmirror, enumblockrotation).func_177971_a(blockposition);

            if (structureboundingbox == null || structureboundingbox.func_175898_b(blockposition1)) {
                NBTTagCompound nbttagcompound = definedstructure_entityinfo.field_186249_c;
                Vec3d vec3d = func_186269_a(definedstructure_entityinfo.field_186247_a, enumblockmirror, enumblockrotation);
                Vec3d vec3d1 = vec3d.func_72441_c(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
                NBTTagList nbttaglist = new NBTTagList();

                nbttaglist.func_74742_a(new NBTTagDouble(vec3d1.field_72450_a));
                nbttaglist.func_74742_a(new NBTTagDouble(vec3d1.field_72448_b));
                nbttaglist.func_74742_a(new NBTTagDouble(vec3d1.field_72449_c));
                nbttagcompound.func_74782_a("Pos", nbttaglist);
                nbttagcompound.func_186854_a("UUID", UUID.randomUUID());

                Entity entity;

                try {
                    entity = EntityList.func_75615_a(nbttagcompound, world);
                } catch (Exception exception) {
                    entity = null;
                }

                if (entity != null) {
                    float f = entity.func_184217_a(enumblockmirror);

                    f += entity.field_70177_z - entity.func_184229_a(enumblockrotation);
                    entity.func_70012_b(vec3d1.field_72450_a, vec3d1.field_72448_b, vec3d1.field_72449_c, f, entity.field_70125_A);
                    world.func_72838_d(entity);
                }
            }
        }

    }

    public BlockPos func_186257_a(Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case COUNTERCLOCKWISE_90:
        case CLOCKWISE_90:
            return new BlockPos(this.field_186272_c.func_177952_p(), this.field_186272_c.func_177956_o(), this.field_186272_c.func_177958_n());

        default:
            return this.field_186272_c;
        }
    }

    private static BlockPos func_186268_a(BlockPos blockposition, Mirror enumblockmirror, Rotation enumblockrotation) {
        int i = blockposition.func_177958_n();
        int j = blockposition.func_177956_o();
        int k = blockposition.func_177952_p();
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

    private static Vec3d func_186269_a(Vec3d vec3d, Mirror enumblockmirror, Rotation enumblockrotation) {
        double d0 = vec3d.field_72450_a;
        double d1 = vec3d.field_72448_b;
        double d2 = vec3d.field_72449_c;
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

    public BlockPos func_189961_a(BlockPos blockposition, Mirror enumblockmirror, Rotation enumblockrotation) {
        return func_191157_a(blockposition, enumblockmirror, enumblockrotation, this.func_186259_a().func_177958_n(), this.func_186259_a().func_177952_p());
    }

    public static BlockPos func_191157_a(BlockPos blockposition, Mirror enumblockmirror, Rotation enumblockrotation, int i, int j) {
        --i;
        --j;
        int k = enumblockmirror == Mirror.FRONT_BACK ? i : 0;
        int l = enumblockmirror == Mirror.LEFT_RIGHT ? j : 0;
        BlockPos blockposition1 = blockposition;

        switch (enumblockrotation) {
        case COUNTERCLOCKWISE_90:
            blockposition1 = blockposition.func_177982_a(l, 0, i - k);
            break;

        case CLOCKWISE_90:
            blockposition1 = blockposition.func_177982_a(j - l, 0, k);
            break;

        case CLOCKWISE_180:
            blockposition1 = blockposition.func_177982_a(i - k, 0, j - l);
            break;

        case NONE:
            blockposition1 = blockposition.func_177982_a(k, 0, l);
        }

        return blockposition1;
    }

    public static void func_191158_a(DataFixer dataconvertermanager) {
        dataconvertermanager.func_188258_a(FixTypes.STRUCTURE, new IDataWalker() {
            @Override
            public NBTTagCompound func_188266_a(IDataFixer dataconverter, NBTTagCompound nbttagcompound, int i) {
                NBTTagList nbttaglist;
                int j;
                NBTTagCompound nbttagcompound1;

                if (nbttagcompound.func_150297_b("entities", 9)) {
                    nbttaglist = nbttagcompound.func_150295_c("entities", 10);

                    for (j = 0; j < nbttaglist.func_74745_c(); ++j) {
                        nbttagcompound1 = (NBTTagCompound) nbttaglist.func_179238_g(j);
                        if (nbttagcompound1.func_150297_b("nbt", 10)) {
                            nbttagcompound1.func_74782_a("nbt", dataconverter.func_188251_a(FixTypes.ENTITY, nbttagcompound1.func_74775_l("nbt"), i));
                        }
                    }
                }

                if (nbttagcompound.func_150297_b("blocks", 9)) {
                    nbttaglist = nbttagcompound.func_150295_c("blocks", 10);

                    for (j = 0; j < nbttaglist.func_74745_c(); ++j) {
                        nbttagcompound1 = (NBTTagCompound) nbttaglist.func_179238_g(j);
                        if (nbttagcompound1.func_150297_b("nbt", 10)) {
                            nbttagcompound1.func_74782_a("nbt", dataconverter.func_188251_a(FixTypes.BLOCK_ENTITY, nbttagcompound1.func_74775_l("nbt"), i));
                        }
                    }
                }

                return nbttagcompound;
            }
        });
    }

    public NBTTagCompound func_189552_a(NBTTagCompound nbttagcompound) {
        Template.a definedstructure_a = new Template.a(null);
        NBTTagList nbttaglist = new NBTTagList();

        NBTTagCompound nbttagcompound1;

        for (Iterator iterator = this.field_186270_a.iterator(); iterator.hasNext(); nbttaglist.func_74742_a(nbttagcompound1)) {
            Template.BlockInfo definedstructure_blockinfo = (Template.BlockInfo) iterator.next();

            nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.func_74782_a("pos", this.func_186267_a(new int[] { definedstructure_blockinfo.field_186242_a.func_177958_n(), definedstructure_blockinfo.field_186242_a.func_177956_o(), definedstructure_blockinfo.field_186242_a.func_177952_p()}));
            nbttagcompound1.func_74768_a("state", definedstructure_a.a(definedstructure_blockinfo.field_186243_b));
            if (definedstructure_blockinfo.field_186244_c != null) {
                nbttagcompound1.func_74782_a("nbt", definedstructure_blockinfo.field_186244_c);
            }
        }

        NBTTagList nbttaglist1 = new NBTTagList();

        NBTTagCompound nbttagcompound2;

        for (Iterator iterator1 = this.field_186271_b.iterator(); iterator1.hasNext(); nbttaglist1.func_74742_a(nbttagcompound2)) {
            Template.EntityInfo definedstructure_entityinfo = (Template.EntityInfo) iterator1.next();

            nbttagcompound2 = new NBTTagCompound();
            nbttagcompound2.func_74782_a("pos", this.func_186264_a(new double[] { definedstructure_entityinfo.field_186247_a.field_72450_a, definedstructure_entityinfo.field_186247_a.field_72448_b, definedstructure_entityinfo.field_186247_a.field_72449_c}));
            nbttagcompound2.func_74782_a("blockPos", this.func_186267_a(new int[] { definedstructure_entityinfo.field_186248_b.func_177958_n(), definedstructure_entityinfo.field_186248_b.func_177956_o(), definedstructure_entityinfo.field_186248_b.func_177952_p()}));
            if (definedstructure_entityinfo.field_186249_c != null) {
                nbttagcompound2.func_74782_a("nbt", definedstructure_entityinfo.field_186249_c);
            }
        }

        NBTTagList nbttaglist2 = new NBTTagList();
        Iterator iterator2 = definedstructure_a.iterator();

        while (iterator2.hasNext()) {
            IBlockState iblockdata = (IBlockState) iterator2.next();

            nbttaglist2.func_74742_a(NBTUtil.func_190009_a(new NBTTagCompound(), iblockdata));
        }

        nbttagcompound.func_74782_a("palette", nbttaglist2);
        nbttagcompound.func_74782_a("blocks", nbttaglist);
        nbttagcompound.func_74782_a("entities", nbttaglist1);
        nbttagcompound.func_74782_a("size", this.func_186267_a(new int[] { this.field_186272_c.func_177958_n(), this.field_186272_c.func_177956_o(), this.field_186272_c.func_177952_p()}));
        nbttagcompound.func_74778_a("author", this.field_186273_d);
        nbttagcompound.func_74768_a("DataVersion", 1343);
        return nbttagcompound;
    }

    public void func_186256_b(NBTTagCompound nbttagcompound) {
        this.field_186270_a.clear();
        this.field_186271_b.clear();
        NBTTagList nbttaglist = nbttagcompound.func_150295_c("size", 3);

        this.field_186272_c = new BlockPos(nbttaglist.func_186858_c(0), nbttaglist.func_186858_c(1), nbttaglist.func_186858_c(2));
        this.field_186273_d = nbttagcompound.func_74779_i("author");
        Template.a definedstructure_a = new Template.a(null);
        NBTTagList nbttaglist1 = nbttagcompound.func_150295_c("palette", 10);

        for (int i = 0; i < nbttaglist1.func_74745_c(); ++i) {
            definedstructure_a.a(NBTUtil.func_190008_d(nbttaglist1.func_150305_b(i)), i);
        }

        NBTTagList nbttaglist2 = nbttagcompound.func_150295_c("blocks", 10);

        for (int j = 0; j < nbttaglist2.func_74745_c(); ++j) {
            NBTTagCompound nbttagcompound1 = nbttaglist2.func_150305_b(j);
            NBTTagList nbttaglist3 = nbttagcompound1.func_150295_c("pos", 3);
            BlockPos blockposition = new BlockPos(nbttaglist3.func_186858_c(0), nbttaglist3.func_186858_c(1), nbttaglist3.func_186858_c(2));
            IBlockState iblockdata = definedstructure_a.a(nbttagcompound1.func_74762_e("state"));
            NBTTagCompound nbttagcompound2;

            if (nbttagcompound1.func_74764_b("nbt")) {
                nbttagcompound2 = nbttagcompound1.func_74775_l("nbt");
            } else {
                nbttagcompound2 = null;
            }

            this.field_186270_a.add(new Template.BlockInfo(blockposition, iblockdata, nbttagcompound2));
        }

        NBTTagList nbttaglist4 = nbttagcompound.func_150295_c("entities", 10);

        for (int k = 0; k < nbttaglist4.func_74745_c(); ++k) {
            NBTTagCompound nbttagcompound3 = nbttaglist4.func_150305_b(k);
            NBTTagList nbttaglist5 = nbttagcompound3.func_150295_c("pos", 6);
            Vec3d vec3d = new Vec3d(nbttaglist5.func_150309_d(0), nbttaglist5.func_150309_d(1), nbttaglist5.func_150309_d(2));
            NBTTagList nbttaglist6 = nbttagcompound3.func_150295_c("blockPos", 3);
            BlockPos blockposition1 = new BlockPos(nbttaglist6.func_186858_c(0), nbttaglist6.func_186858_c(1), nbttaglist6.func_186858_c(2));

            if (nbttagcompound3.func_74764_b("nbt")) {
                NBTTagCompound nbttagcompound4 = nbttagcompound3.func_74775_l("nbt");

                this.field_186271_b.add(new Template.EntityInfo(vec3d, blockposition1, nbttagcompound4));
            }
        }

    }

    private NBTTagList func_186267_a(int... aint) {
        NBTTagList nbttaglist = new NBTTagList();
        int[] aint1 = aint;
        int i = aint.length;

        for (int j = 0; j < i; ++j) {
            int k = aint1[j];

            nbttaglist.func_74742_a(new NBTTagInt(k));
        }

        return nbttaglist;
    }

    private NBTTagList func_186264_a(double... adouble) {
        NBTTagList nbttaglist = new NBTTagList();
        double[] adouble1 = adouble;
        int i = adouble.length;

        for (int j = 0; j < i; ++j) {
            double d0 = adouble1[j];

            nbttaglist.func_74742_a(new NBTTagDouble(d0));
        }

        return nbttaglist;
    }

    public static class EntityInfo {

        public final Vec3d field_186247_a;
        public final BlockPos field_186248_b;
        public final NBTTagCompound field_186249_c;

        public EntityInfo(Vec3d vec3d, BlockPos blockposition, NBTTagCompound nbttagcompound) {
            this.field_186247_a = vec3d;
            this.field_186248_b = blockposition;
            this.field_186249_c = nbttagcompound;
        }
    }

    public static class BlockInfo {

        public final BlockPos field_186242_a;
        public final IBlockState field_186243_b;
        public final NBTTagCompound field_186244_c;

        public BlockInfo(BlockPos blockposition, IBlockState iblockdata, @Nullable NBTTagCompound nbttagcompound) {
            this.field_186242_a = blockposition;
            this.field_186243_b = iblockdata;
            this.field_186244_c = nbttagcompound;
        }
    }

    static class a implements Iterable<IBlockState> {

        public static final IBlockState a = Blocks.field_150350_a.func_176223_P();
        final ObjectIntIdentityMap<IBlockState> b;
        private int c;

        private a() {
            this.b = new ObjectIntIdentityMap(16);
        }

        public int a(IBlockState iblockdata) {
            int i = this.b.func_148747_b(iblockdata);

            if (i == -1) {
                i = this.c++;
                this.b.func_148746_a(iblockdata, i);
            }

            return i;
        }

        @Nullable
        public IBlockState a(int i) {
            IBlockState iblockdata = this.b.func_148745_a(i);

            return iblockdata == null ? a : iblockdata; // Paper - decompile error - Blocks.AIR.getBlockData()
        }

        @Override
        public Iterator<IBlockState> iterator() {
            return this.b.iterator();
        }

        public void a(IBlockState iblockdata, int i) {
            this.b.func_148746_a(iblockdata, i);
        }

        a(Object object) {
            this();
        }
    }
}
