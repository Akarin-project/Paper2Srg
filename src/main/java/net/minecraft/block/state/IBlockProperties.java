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

    Material getMaterial();

    boolean isFullBlock();

    boolean canEntitySpawn(Entity entity);

    int getLightOpacity();

    int getLightValue();

    boolean useNeighborBrightness();

    MapColor getMapColor(IBlockAccess iblockaccess, BlockPos blockposition);

    IBlockState withRotation(Rotation enumblockrotation);

    IBlockState withMirror(Mirror enumblockmirror);

    boolean isFullCube();

    EnumBlockRenderType getRenderType();

    boolean isBlockNormalCube();

    boolean isNormalCube();

    boolean canProvidePower();

    int getWeakPower(IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection);

    boolean hasComparatorInputOverride();

    int getComparatorInputOverride(World world, BlockPos blockposition);

    float getBlockHardness(World world, BlockPos blockposition);

    float getPlayerRelativeBlockHardness(EntityPlayer entityhuman, World world, BlockPos blockposition);

    int getStrongPower(IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection);

    EnumPushReaction getMobilityFlag();

    IBlockState getActualState(IBlockAccess iblockaccess, BlockPos blockposition);

    boolean isOpaqueCube();

    @Nullable
    AxisAlignedBB getCollisionBoundingBox(IBlockAccess iblockaccess, BlockPos blockposition);

    void addCollisionBoxToList(World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag);

    AxisAlignedBB getBoundingBox(IBlockAccess iblockaccess, BlockPos blockposition);

    RayTraceResult collisionRayTrace(World world, BlockPos blockposition, Vec3d vec3d, Vec3d vec3d1);

    boolean isTopSolid();

    Vec3d getOffset(IBlockAccess iblockaccess, BlockPos blockposition);

    boolean causesSuffocation();

    BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection);
}
