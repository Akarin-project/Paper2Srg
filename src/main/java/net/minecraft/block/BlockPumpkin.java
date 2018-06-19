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

    private BlockPattern field_176394_a;
    private BlockPattern field_176393_b;
    private BlockPattern field_176395_M;
    private BlockPattern field_176396_O;
    private static final Predicate<IBlockState> field_181085_Q = new Predicate() {
        public boolean a(@Nullable IBlockState iblockdata) {
            return iblockdata != null && (iblockdata.func_177230_c() == Blocks.field_150423_aK || iblockdata.func_177230_c() == Blocks.field_150428_aP);
        }

        public boolean apply(@Nullable Object object) {
            return this.a((IBlockState) object);
        }
    };

    protected BlockPumpkin() {
        super(Material.field_151572_C, MapColor.field_151676_q);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockPumpkin.field_185512_D, EnumFacing.NORTH));
        this.func_149675_a(true);
        this.func_149647_a(CreativeTabs.field_78030_b);
    }

    public void func_176213_c(World world, BlockPos blockposition, IBlockState iblockdata) {
        super.func_176213_c(world, blockposition, iblockdata);
        this.func_180673_e(world, blockposition);
    }

    public boolean func_176390_d(World world, BlockPos blockposition) {
        return this.func_176392_j().func_177681_a(world, blockposition) != null || this.func_176389_S().func_177681_a(world, blockposition) != null;
    }

    private void func_180673_e(World world, BlockPos blockposition) {
        BlockPattern.PatternHelper shapedetector_shapedetectorcollection = this.func_176391_l().func_177681_a(world, blockposition);
        int i;
        Iterator iterator;
        EntityPlayerMP entityplayer;
        int j;

        BlockStateListPopulator blockList = new BlockStateListPopulator(world.getWorld()); // CraftBukkit - Use BlockStateListPopulator
        if (shapedetector_shapedetectorcollection != null) {
            for (i = 0; i < this.func_176391_l().func_177685_b(); ++i) {
                BlockWorldState shapedetectorblock = shapedetector_shapedetectorcollection.func_177670_a(0, i, 0);

                // CraftBukkit start
                // world.setTypeAndData(shapedetectorblock.d(), Blocks.AIR.getBlockData(), 2);
                BlockPos pos = shapedetectorblock.func_177508_d();
                blockList.setTypeId(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p(), 0);
                // CraftBukkit end
            }

            EntitySnowman entitysnowman = new EntitySnowman(world);
            BlockPos blockposition1 = shapedetector_shapedetectorcollection.func_177670_a(0, 2, 0).func_177508_d();

            entitysnowman.func_70012_b((double) blockposition1.func_177958_n() + 0.5D, (double) blockposition1.func_177956_o() + 0.05D, (double) blockposition1.func_177952_p() + 0.5D, 0.0F, 0.0F);
            // CraftBukkit start
            if (world.addEntity(entitysnowman, SpawnReason.BUILD_SNOWMAN)) {
                blockList.updateList();
            iterator = world.func_72872_a(EntityPlayerMP.class, entitysnowman.func_174813_aQ().func_186662_g(5.0D)).iterator();

            while (iterator.hasNext()) {
                entityplayer = (EntityPlayerMP) iterator.next();
                CriteriaTriggers.field_192133_m.func_192229_a(entityplayer, (Entity) entitysnowman);
            }

            for (j = 0; j < 120; ++j) {
                world.func_175688_a(EnumParticleTypes.SNOW_SHOVEL, (double) blockposition1.func_177958_n() + world.field_73012_v.nextDouble(), (double) blockposition1.func_177956_o() + world.field_73012_v.nextDouble() * 2.5D, (double) blockposition1.func_177952_p() + world.field_73012_v.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
            }

            for (j = 0; j < this.func_176391_l().func_177685_b(); ++j) {
                BlockWorldState shapedetectorblock1 = shapedetector_shapedetectorcollection.func_177670_a(0, j, 0);

                world.func_175722_b(shapedetectorblock1.func_177508_d(), Blocks.field_150350_a, false);
            }
            } // CraftBukkit end
        } else {
            shapedetector_shapedetectorcollection = this.func_176388_T().func_177681_a(world, blockposition);
            if (shapedetector_shapedetectorcollection != null) {
                for (i = 0; i < this.func_176388_T().func_177684_c(); ++i) {
                    for (int k = 0; k < this.func_176388_T().func_177685_b(); ++k) {
                        // CraftBukkit start
                        // world.setTypeAndData(shapedetector_shapedetectorcollection.a(i, k, 0).getPosition(), Blocks.AIR.getBlockData(), 2);
                        BlockPos pos = shapedetector_shapedetectorcollection.func_177670_a(i, k, 0).func_177508_d();
                        blockList.setTypeId(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p(), 0);
                        // CraftBukkit end
                    }
                }

                BlockPos blockposition2 = shapedetector_shapedetectorcollection.func_177670_a(1, 2, 0).func_177508_d();
                EntityIronGolem entityirongolem = new EntityIronGolem(world);

                entityirongolem.func_70849_f(true);
                entityirongolem.func_70012_b((double) blockposition2.func_177958_n() + 0.5D, (double) blockposition2.func_177956_o() + 0.05D, (double) blockposition2.func_177952_p() + 0.5D, 0.0F, 0.0F);
            // CraftBukkit start
            if (world.addEntity(entityirongolem, SpawnReason.BUILD_IRONGOLEM)) {
                blockList.updateList();
                iterator = world.func_72872_a(EntityPlayerMP.class, entityirongolem.func_174813_aQ().func_186662_g(5.0D)).iterator();

                while (iterator.hasNext()) {
                    entityplayer = (EntityPlayerMP) iterator.next();
                    CriteriaTriggers.field_192133_m.func_192229_a(entityplayer, (Entity) entityirongolem);
                }

                for (j = 0; j < 120; ++j) {
                    world.func_175688_a(EnumParticleTypes.SNOWBALL, (double) blockposition2.func_177958_n() + world.field_73012_v.nextDouble(), (double) blockposition2.func_177956_o() + world.field_73012_v.nextDouble() * 3.9D, (double) blockposition2.func_177952_p() + world.field_73012_v.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
                }

                for (j = 0; j < this.func_176388_T().func_177684_c(); ++j) {
                    for (int l = 0; l < this.func_176388_T().func_177685_b(); ++l) {
                        BlockWorldState shapedetectorblock2 = shapedetector_shapedetectorcollection.func_177670_a(j, l, 0);

                        world.func_175722_b(shapedetectorblock2.func_177508_d(), Blocks.field_150350_a, false);
                    }
                }
            }
            } // CraftBukkit end
        }

    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        return world.func_180495_p(blockposition).func_177230_c().field_149764_J.func_76222_j() && world.func_180495_p(blockposition.func_177977_b()).func_185896_q();
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockPumpkin.field_185512_D, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockPumpkin.field_185512_D)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockPumpkin.field_185512_D)));
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.func_176223_P().func_177226_a(BlockPumpkin.field_185512_D, entityliving.func_174811_aO().func_176734_d());
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockPumpkin.field_185512_D, EnumFacing.func_176731_b(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((EnumFacing) iblockdata.func_177229_b(BlockPumpkin.field_185512_D)).func_176736_b();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockPumpkin.field_185512_D});
    }

    protected BlockPattern func_176392_j() {
        if (this.field_176394_a == null) {
            this.field_176394_a = FactoryBlockPattern.func_177660_a().func_177659_a(new String[] { " ", "#", "#"}).func_177662_a('#', BlockWorldState.func_177510_a(BlockStateMatcher.func_177638_a(Blocks.field_150433_aE))).func_177661_b();
        }

        return this.field_176394_a;
    }

    protected BlockPattern func_176391_l() {
        if (this.field_176393_b == null) {
            this.field_176393_b = FactoryBlockPattern.func_177660_a().func_177659_a(new String[] { "^", "#", "#"}).func_177662_a('^', BlockWorldState.func_177510_a(BlockPumpkin.field_181085_Q)).func_177662_a('#', BlockWorldState.func_177510_a(BlockStateMatcher.func_177638_a(Blocks.field_150433_aE))).func_177661_b();
        }

        return this.field_176393_b;
    }

    protected BlockPattern func_176389_S() {
        if (this.field_176395_M == null) {
            this.field_176395_M = FactoryBlockPattern.func_177660_a().func_177659_a(new String[] { "~ ~", "###", "~#~"}).func_177662_a('#', BlockWorldState.func_177510_a(BlockStateMatcher.func_177638_a(Blocks.field_150339_S))).func_177662_a('~', BlockWorldState.func_177510_a(BlockMaterialMatcher.func_189886_a(Material.field_151579_a))).func_177661_b();
        }

        return this.field_176395_M;
    }

    protected BlockPattern func_176388_T() {
        if (this.field_176396_O == null) {
            this.field_176396_O = FactoryBlockPattern.func_177660_a().func_177659_a(new String[] { "~^~", "###", "~#~"}).func_177662_a('^', BlockWorldState.func_177510_a(BlockPumpkin.field_181085_Q)).func_177662_a('#', BlockWorldState.func_177510_a(BlockStateMatcher.func_177638_a(Blocks.field_150339_S))).func_177662_a('~', BlockWorldState.func_177510_a(BlockMaterialMatcher.func_189886_a(Material.field_151579_a))).func_177661_b();
        }

        return this.field_176396_O;
    }
}
