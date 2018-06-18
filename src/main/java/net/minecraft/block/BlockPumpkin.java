package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.Iterator;
import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMaterialMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.util.BlockStateListPopulator;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

// CraftBukkit start
import org.bukkit.craftbukkit.util.BlockStateListPopulator;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
// CraftBukkit end

public class BlockPumpkin extends BlockHorizontal {

    private BlockPattern snowmanBasePattern;
    private BlockPattern snowmanPattern;
    private BlockPattern golemBasePattern;
    private BlockPattern golemPattern;
    private static final Predicate<IBlockState> IS_PUMPKIN = new Predicate() {
        public boolean a(@Nullable IBlockState iblockdata) {
            return iblockdata != null && (iblockdata.getBlock() == Blocks.PUMPKIN || iblockdata.getBlock() == Blocks.LIT_PUMPKIN);
        }

        public boolean apply(@Nullable Object object) {
            return this.a((IBlockState) object);
        }
    };

    protected BlockPumpkin() {
        super(Material.GOURD, MapColor.ADOBE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockPumpkin.FACING, EnumFacing.NORTH));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public void onBlockAdded(World world, BlockPos blockposition, IBlockState iblockdata) {
        super.onBlockAdded(world, blockposition, iblockdata);
        this.trySpawnGolem(world, blockposition);
    }

    public boolean canDispenserPlace(World world, BlockPos blockposition) {
        return this.getSnowmanBasePattern().match(world, blockposition) != null || this.getGolemBasePattern().match(world, blockposition) != null;
    }

    private void trySpawnGolem(World world, BlockPos blockposition) {
        BlockPattern.PatternHelper shapedetector_shapedetectorcollection = this.getSnowmanPattern().match(world, blockposition);
        int i;
        Iterator iterator;
        EntityPlayerMP entityplayer;
        int j;

        BlockStateListPopulator blockList = new BlockStateListPopulator(world.getWorld()); // CraftBukkit - Use BlockStateListPopulator
        if (shapedetector_shapedetectorcollection != null) {
            for (i = 0; i < this.getSnowmanPattern().getThumbLength(); ++i) {
                BlockWorldState shapedetectorblock = shapedetector_shapedetectorcollection.translateOffset(0, i, 0);

                // CraftBukkit start
                // world.setTypeAndData(shapedetectorblock.d(), Blocks.AIR.getBlockData(), 2);
                BlockPos pos = shapedetectorblock.getPos();
                blockList.setTypeId(pos.getX(), pos.getY(), pos.getZ(), 0);
                // CraftBukkit end
            }

            EntitySnowman entitysnowman = new EntitySnowman(world);
            BlockPos blockposition1 = shapedetector_shapedetectorcollection.translateOffset(0, 2, 0).getPos();

            entitysnowman.setLocationAndAngles((double) blockposition1.getX() + 0.5D, (double) blockposition1.getY() + 0.05D, (double) blockposition1.getZ() + 0.5D, 0.0F, 0.0F);
            // CraftBukkit start
            if (world.addEntity(entitysnowman, SpawnReason.BUILD_SNOWMAN)) {
                blockList.updateList();
            iterator = world.getEntitiesWithinAABB(EntityPlayerMP.class, entitysnowman.getEntityBoundingBox().grow(5.0D)).iterator();

            while (iterator.hasNext()) {
                entityplayer = (EntityPlayerMP) iterator.next();
                CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayer, (Entity) entitysnowman);
            }

            for (j = 0; j < 120; ++j) {
                world.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, (double) blockposition1.getX() + world.rand.nextDouble(), (double) blockposition1.getY() + world.rand.nextDouble() * 2.5D, (double) blockposition1.getZ() + world.rand.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
            }

            for (j = 0; j < this.getSnowmanPattern().getThumbLength(); ++j) {
                BlockWorldState shapedetectorblock1 = shapedetector_shapedetectorcollection.translateOffset(0, j, 0);

                world.notifyNeighborsRespectDebug(shapedetectorblock1.getPos(), Blocks.AIR, false);
            }
            } // CraftBukkit end
        } else {
            shapedetector_shapedetectorcollection = this.getGolemPattern().match(world, blockposition);
            if (shapedetector_shapedetectorcollection != null) {
                for (i = 0; i < this.getGolemPattern().getPalmLength(); ++i) {
                    for (int k = 0; k < this.getGolemPattern().getThumbLength(); ++k) {
                        // CraftBukkit start
                        // world.setTypeAndData(shapedetector_shapedetectorcollection.a(i, k, 0).getPosition(), Blocks.AIR.getBlockData(), 2);
                        BlockPos pos = shapedetector_shapedetectorcollection.translateOffset(i, k, 0).getPos();
                        blockList.setTypeId(pos.getX(), pos.getY(), pos.getZ(), 0);
                        // CraftBukkit end
                    }
                }

                BlockPos blockposition2 = shapedetector_shapedetectorcollection.translateOffset(1, 2, 0).getPos();
                EntityIronGolem entityirongolem = new EntityIronGolem(world);

                entityirongolem.setPlayerCreated(true);
                entityirongolem.setLocationAndAngles((double) blockposition2.getX() + 0.5D, (double) blockposition2.getY() + 0.05D, (double) blockposition2.getZ() + 0.5D, 0.0F, 0.0F);
            // CraftBukkit start
            if (world.addEntity(entityirongolem, SpawnReason.BUILD_IRONGOLEM)) {
                blockList.updateList();
                iterator = world.getEntitiesWithinAABB(EntityPlayerMP.class, entityirongolem.getEntityBoundingBox().grow(5.0D)).iterator();

                while (iterator.hasNext()) {
                    entityplayer = (EntityPlayerMP) iterator.next();
                    CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayer, (Entity) entityirongolem);
                }

                for (j = 0; j < 120; ++j) {
                    world.spawnParticle(EnumParticleTypes.SNOWBALL, (double) blockposition2.getX() + world.rand.nextDouble(), (double) blockposition2.getY() + world.rand.nextDouble() * 3.9D, (double) blockposition2.getZ() + world.rand.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
                }

                for (j = 0; j < this.getGolemPattern().getPalmLength(); ++j) {
                    for (int l = 0; l < this.getGolemPattern().getThumbLength(); ++l) {
                        BlockWorldState shapedetectorblock2 = shapedetector_shapedetectorcollection.translateOffset(j, l, 0);

                        world.notifyNeighborsRespectDebug(shapedetectorblock2.getPos(), Blocks.AIR, false);
                    }
                }
            }
            } // CraftBukkit end
        }

    }

    public boolean canPlaceBlockAt(World world, BlockPos blockposition) {
        return world.getBlockState(blockposition).getBlock().blockMaterial.isReplaceable() && world.getBlockState(blockposition.down()).isTopSolid();
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockPumpkin.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockPumpkin.FACING)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withRotation(enumblockmirror.toRotation((EnumFacing) iblockdata.getValue(BlockPumpkin.FACING)));
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.getDefaultState().withProperty(BlockPumpkin.FACING, entityliving.getHorizontalFacing().getOpposite());
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockPumpkin.FACING, EnumFacing.getHorizontal(i));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((EnumFacing) iblockdata.getValue(BlockPumpkin.FACING)).getHorizontalIndex();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockPumpkin.FACING});
    }

    protected BlockPattern getSnowmanBasePattern() {
        if (this.snowmanBasePattern == null) {
            this.snowmanBasePattern = FactoryBlockPattern.start().aisle(new String[] { " ", "#", "#"}).where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.SNOW))).build();
        }

        return this.snowmanBasePattern;
    }

    protected BlockPattern getSnowmanPattern() {
        if (this.snowmanPattern == null) {
            this.snowmanPattern = FactoryBlockPattern.start().aisle(new String[] { "^", "#", "#"}).where('^', BlockWorldState.hasState(BlockPumpkin.IS_PUMPKIN)).where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.SNOW))).build();
        }

        return this.snowmanPattern;
    }

    protected BlockPattern getGolemBasePattern() {
        if (this.golemBasePattern == null) {
            this.golemBasePattern = FactoryBlockPattern.start().aisle(new String[] { "~ ~", "###", "~#~"}).where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.IRON_BLOCK))).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }

        return this.golemBasePattern;
    }

    protected BlockPattern getGolemPattern() {
        if (this.golemPattern == null) {
            this.golemPattern = FactoryBlockPattern.start().aisle(new String[] { "~^~", "###", "~#~"}).where('^', BlockWorldState.hasState(BlockPumpkin.IS_PUMPKIN)).where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.IRON_BLOCK))).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }

        return this.golemPattern;
    }
}
