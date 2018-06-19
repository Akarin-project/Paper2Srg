package net.minecraft.block.state;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface IBlockProperties {

    Material func_185904_a();

    boolean func_185913_b();

    boolean func_189884_a(Entity entity);

    int func_185891_c();

    int func_185906_d();

    boolean func_185916_f();

    MapColor func_185909_g(IBlockAccess iblockaccess, BlockPos blockposition);

    IBlockState func_185907_a(Rotation enumblockrotation);

    IBlockState func_185902_a(Mirror enumblockmirror);

    boolean func_185917_h();

    EnumBlockRenderType func_185901_i();

    boolean func_185898_k();

    boolean func_185915_l();

    boolean func_185897_m();

    int func_185911_a(IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection);

    boolean func_185912_n();

    int func_185888_a(World world, BlockPos blockposition);

    float func_185887_b(World world, BlockPos blockposition);

    float func_185903_a(EntityPlayer entityhuman, World world, BlockPos blockposition);

    int func_185893_b(IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection);

    EnumPushReaction func_185905_o();

    IBlockState func_185899_b(IBlockAccess iblockaccess, BlockPos blockposition);

    boolean func_185914_p();

    @Nullable
    AxisAlignedBB func_185890_d(IBlockAccess iblockaccess, BlockPos blockposition);

    void func_185908_a(World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag);

    AxisAlignedBB func_185900_c(IBlockAccess iblockaccess, BlockPos blockposition);

    RayTraceResult func_185910_a(World world, BlockPos blockposition, Vec3d vec3d, Vec3d vec3d1);

    boolean func_185896_q();

    Vec3d func_191059_e(IBlockAccess iblockaccess, BlockPos blockposition);

    boolean func_191058_s();

    BlockFaceShape func_193401_d(IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection);
}
