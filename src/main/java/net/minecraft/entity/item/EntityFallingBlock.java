package net.minecraft.entity.item;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;


import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public class EntityFallingBlock extends Entity {

    private IBlockState field_175132_d;
    public int field_145812_b;
    public boolean field_145813_c = true;
    private boolean field_145808_f;
    public boolean field_145809_g;
    private int field_145815_h = 40;
    private float field_145816_i = 2.0F;
    public NBTTagCompound field_145810_d;
    protected static final DataParameter<BlockPos> field_184532_d = EntityDataManager.func_187226_a(EntityFallingBlock.class, DataSerializers.field_187200_j);

    public EntityFallingBlock(World world) {
        super(world);
    }

    public EntityFallingBlock(World world, double d0, double d1, double d2, IBlockState iblockdata) {
        super(world);
        this.field_175132_d = iblockdata;
        this.field_70156_m = true;
        this.func_70105_a(0.98F, 0.98F);
        this.func_70107_b(d0, d1 + (double) ((1.0F - this.field_70131_O) / 2.0F), d2);
        this.field_70159_w = 0.0D;
        this.field_70181_x = 0.0D;
        this.field_70179_y = 0.0D;
        this.field_70169_q = d0;
        this.field_70167_r = d1;
        this.field_70166_s = d2;
        this.func_184530_a(new BlockPos(this));
    }

    public boolean func_70075_an() {
        return false;
    }

    public void func_184530_a(BlockPos blockposition) {
        this.field_70180_af.func_187227_b(EntityFallingBlock.field_184532_d, blockposition);
    }

    protected boolean func_70041_e_() {
        return false;
    }

    protected void func_70088_a() {
        this.field_70180_af.func_187214_a(EntityFallingBlock.field_184532_d, BlockPos.field_177992_a);
    }

    public boolean func_70067_L() {
        return !this.field_70128_L;
    }

    public void func_70071_h_() {
        Block block = this.field_175132_d.func_177230_c();

        if (this.field_175132_d.func_185904_a() == Material.field_151579_a) {
            this.func_70106_y();
        } else {
            this.field_70169_q = this.field_70165_t;
            this.field_70167_r = this.field_70163_u;
            this.field_70166_s = this.field_70161_v;
            BlockPos blockposition;

            if (this.field_145812_b++ == 0) {
                blockposition = new BlockPos(this);
                if (this.field_70170_p.func_180495_p(blockposition).func_177230_c() == block && !CraftEventFactory.callEntityChangeBlockEvent(this, blockposition, Blocks.field_150350_a, 0).isCancelled()) {
                    this.field_70170_p.func_175698_g(blockposition);
                    this.field_70170_p.chunkPacketBlockController.updateNearbyBlocks(this.field_70170_p, blockposition); // Paper - Anti-Xray
                } else if (!this.field_70170_p.field_72995_K) {
                    this.func_70106_y();
                    return;
                }
            }

            if (!this.func_189652_ae()) {
                this.field_70181_x -= 0.03999999910593033D;
            }

            this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);

            // Paper start - Configurable EntityFallingBlock height nerf
            if (this.field_70170_p.paperConfig.fallingBlockHeightNerf != 0 && this.field_70163_u > this.field_70170_p.paperConfig.fallingBlockHeightNerf) {
                if (this.field_145813_c && this.field_70170_p.func_82736_K().func_82766_b("doEntityDrops")) {
                    this.dropItem(new ItemStack(block, 1, block.func_180651_a(this.field_175132_d)), 0.0F);
                }

                this.func_70106_y();
            }
            // Paper end

            if (!this.field_70170_p.field_72995_K) {
                blockposition = new BlockPos(this);
                boolean flag = this.field_175132_d.func_177230_c() == Blocks.field_192444_dS;
                boolean flag1 = flag && this.field_70170_p.func_180495_p(blockposition).func_185904_a() == Material.field_151586_h;
                double d0 = this.field_70159_w * this.field_70159_w + this.field_70181_x * this.field_70181_x + this.field_70179_y * this.field_70179_y;

                if (flag && d0 > 1.0D) {
                    RayTraceResult movingobjectposition = this.field_70170_p.func_72901_a(new Vec3d(this.field_70169_q, this.field_70167_r, this.field_70166_s), new Vec3d(this.field_70165_t, this.field_70163_u, this.field_70161_v), true);

                    if (movingobjectposition != null && this.field_70170_p.func_180495_p(movingobjectposition.func_178782_a()).func_185904_a() == Material.field_151586_h) {
                        blockposition = movingobjectposition.func_178782_a();
                        flag1 = true;
                    }
                }

                if (!this.field_70122_E && !flag1) {
                    if (this.field_145812_b > 100 && !this.field_70170_p.field_72995_K && (blockposition.func_177956_o() < 1 || blockposition.func_177956_o() > 256) || this.field_145812_b > 600) {
                        if (this.field_145813_c && this.field_70170_p.func_82736_K().func_82766_b("doEntityDrops")) {
                            this.func_70099_a(new ItemStack(block, 1, block.func_180651_a(this.field_175132_d)), 0.0F);
                        }

                        this.func_70106_y();
                    }
                } else {
                    IBlockState iblockdata = this.field_70170_p.func_180495_p(blockposition);
                    
                    if (!isOnGround()) {
                        this.field_70122_E = false;
                        if (this.field_70170_p.paperConfig.altFallingBlockOnGround) return; // Paper
                    }

                    this.field_70159_w *= 0.699999988079071D;
                    this.field_70179_y *= 0.699999988079071D;
                    this.field_70181_x *= -0.5D;
                    if (iblockdata.func_177230_c() != Blocks.field_180384_M) {
                        this.func_70106_y();
                        if (!this.field_145808_f) {
                            // CraftBukkit start
                            if (this.field_70170_p.func_190527_a(block, blockposition, true, EnumFacing.UP, (Entity) null) && (flag1 || !BlockFalling.func_185759_i(this.field_70170_p.func_180495_p(blockposition.func_177977_b())))) {
                                if (CraftEventFactory.callEntityChangeBlockEvent(this, blockposition, this.field_175132_d.func_177230_c(), this.field_175132_d.func_177230_c().func_176201_c(this.field_175132_d)).isCancelled()) {
                                    return;
                                }
                                this.field_70170_p.func_180501_a(blockposition, this.field_175132_d, 3);
                                this.field_70170_p.chunkPacketBlockController.updateNearbyBlocks(this.field_70170_p, blockposition); // Paper - Anti-Xray
                                // CraftBukkit end
                                if (block instanceof BlockFalling) {
                                    ((BlockFalling) block).func_176502_a_(this.field_70170_p, blockposition, this.field_175132_d, iblockdata);
                                }

                                if (this.field_145810_d != null && block instanceof ITileEntityProvider) {
                                    TileEntity tileentity = this.field_70170_p.func_175625_s(blockposition);

                                    if (tileentity != null) {
                                        NBTTagCompound nbttagcompound = tileentity.func_189515_b(new NBTTagCompound());
                                        Iterator iterator = this.field_145810_d.func_150296_c().iterator();

                                        while (iterator.hasNext()) {
                                            String s = (String) iterator.next();
                                            NBTBase nbtbase = this.field_145810_d.func_74781_a(s);

                                            if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s)) {
                                                nbttagcompound.func_74782_a(s, nbtbase.func_74737_b());
                                            }
                                        }

                                        tileentity.func_145839_a(nbttagcompound);
                                        tileentity.func_70296_d();
                                    }
                                }
                            } else if (this.field_145813_c && this.field_70170_p.func_82736_K().func_82766_b("doEntityDrops")) {
                                this.func_70099_a(new ItemStack(block, 1, block.func_180651_a(this.field_175132_d)), 0.0F);
                            }
                        } else if (block instanceof BlockFalling) {
                            ((BlockFalling) block).func_190974_b(this.field_70170_p, blockposition);
                        }
                    }
                }
            }

            this.field_70159_w *= 0.9800000190734863D;
            this.field_70181_x *= 0.9800000190734863D;
            this.field_70179_y *= 0.9800000190734863D;
        }
    }

    // Paper start
    private boolean isOnGround() {
        BlockPos where = new BlockPos(this.field_70165_t, this.field_70163_u - 0.009999999776482582D, this.field_70161_v);
        boolean cannotMoveThrough = !BlockFalling.canMoveThrough(this.field_70170_p.func_180495_p(where));
        if (!this.field_70170_p.paperConfig.altFallingBlockOnGround) return cannotMoveThrough;

        if (cannotMoveThrough) {
            return true;
        }

        IBlockState blockData = this.field_70170_p.func_180495_p(where.func_177977_b());
        if (BlockFalling.canMoveThrough(blockData)) {
            return false;
        }

        List<AxisAlignedBB> list = new ArrayList<>();
        addCollisions(blockData, func_130014_f_(), where, this.func_174813_aQ(), list, this);
        return list.size() > 0;
    }

    // OBFHELPER
    private void addCollisions(IBlockState blockData, World world, BlockPos where, AxisAlignedBB collider, List<AxisAlignedBB> list, Entity entity) {
        blockData.func_185908_a(world, where, collider, list, entity, false);
    }
    // Paper end

    public void func_180430_e(float f, float f1) {
        Block block = this.field_175132_d.func_177230_c();

        if (this.field_145809_g) {
            int i = MathHelper.func_76123_f(f - 1.0F);

            if (i > 0) {
                ArrayList arraylist = Lists.newArrayList(this.field_70170_p.func_72839_b(this, this.func_174813_aQ()));
                boolean flag = block == Blocks.field_150467_bQ;
                DamageSource damagesource = flag ? DamageSource.field_82728_o : DamageSource.field_82729_p;
                Iterator iterator = arraylist.iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();

                    CraftEventFactory.entityDamage = this; // CraftBukkit
                    entity.func_70097_a(damagesource, (float) Math.min(MathHelper.func_76141_d((float) i * this.field_145816_i), this.field_145815_h));
                    CraftEventFactory.entityDamage = null; // CraftBukkit
                }

                if (flag && (double) this.field_70146_Z.nextFloat() < 0.05000000074505806D + (double) i * 0.05D) {
                    int j = ((Integer) this.field_175132_d.func_177229_b(BlockAnvil.field_176505_b)).intValue();

                    ++j;
                    if (j > 2) {
                        this.field_145808_f = true;
                    } else {
                        this.field_175132_d = this.field_175132_d.func_177226_a(BlockAnvil.field_176505_b, Integer.valueOf(j));
                    }
                }
            }
        }

    }

    public static void func_189741_a(DataFixer dataconvertermanager) {}

    protected void func_70014_b(NBTTagCompound nbttagcompound) {
        Block block = this.field_175132_d != null ? this.field_175132_d.func_177230_c() : Blocks.field_150350_a;
        ResourceLocation minecraftkey = (ResourceLocation) Block.field_149771_c.func_177774_c(block);

        nbttagcompound.func_74778_a("Block", minecraftkey == null ? "" : minecraftkey.toString());
        nbttagcompound.func_74774_a("Data", (byte) block.func_176201_c(this.field_175132_d));
        nbttagcompound.func_74768_a("Time", this.field_145812_b);
        nbttagcompound.func_74757_a("DropItem", this.field_145813_c);
        nbttagcompound.func_74757_a("HurtEntities", this.field_145809_g);
        nbttagcompound.func_74776_a("FallHurtAmount", this.field_145816_i);
        nbttagcompound.func_74768_a("FallHurtMax", this.field_145815_h);
        if (this.field_145810_d != null) {
            nbttagcompound.func_74782_a("TileEntityData", this.field_145810_d);
        }

    }

    protected void func_70037_a(NBTTagCompound nbttagcompound) {
        int i = nbttagcompound.func_74771_c("Data") & 255;

        if (nbttagcompound.func_150297_b("Block", 8)) {
            this.field_175132_d = Block.func_149684_b(nbttagcompound.func_74779_i("Block")).func_176203_a(i);
        } else if (nbttagcompound.func_150297_b("TileID", 99)) {
            this.field_175132_d = Block.func_149729_e(nbttagcompound.func_74762_e("TileID")).func_176203_a(i);
        } else {
            this.field_175132_d = Block.func_149729_e(nbttagcompound.func_74771_c("Tile") & 255).func_176203_a(i);
        }

        // Paper start - Block FallingBlocks with Command Blocks
        // Check mappings on update - dc = "repeating_command_block" - dd = "chain_command_block"
        final Block b = this.field_175132_d.func_177230_c();
        if (this.field_70170_p.paperConfig.filterNBTFromSpawnEgg && (b == Blocks.field_150483_bI || b == Blocks.field_185776_dc || b == Blocks.field_185777_dd)) {
            this.field_175132_d = Blocks.field_150348_b.func_176223_P();
        }
        // Paper end

        this.field_145812_b = nbttagcompound.func_74762_e("Time");
        Block block = this.field_175132_d.func_177230_c();

        if (nbttagcompound.func_150297_b("HurtEntities", 99)) {
            this.field_145809_g = nbttagcompound.func_74767_n("HurtEntities");
            this.field_145816_i = nbttagcompound.func_74760_g("FallHurtAmount");
            this.field_145815_h = nbttagcompound.func_74762_e("FallHurtMax");
        } else if (block == Blocks.field_150467_bQ) {
            this.field_145809_g = true;
        }

        if (nbttagcompound.func_150297_b("DropItem", 99)) {
            this.field_145813_c = nbttagcompound.func_74767_n("DropItem");
        }

        if (nbttagcompound.func_150297_b("TileEntityData", 10)) {
            this.field_145810_d = nbttagcompound.func_74775_l("TileEntityData");
        }

        if (block == null || block.func_176223_P().func_185904_a() == Material.field_151579_a) {
            this.field_175132_d = Blocks.field_150354_m.func_176223_P();
        }

        // Paper start - Try and load origin location from the old NBT tags for backwards compatibility
        if (nbttagcompound.func_74764_b("SourceLoc_x")) {
            int srcX = nbttagcompound.func_74762_e("SourceLoc_x");
            int srcY = nbttagcompound.func_74762_e("SourceLoc_y");
            int srcZ = nbttagcompound.func_74762_e("SourceLoc_z");
            origin = new org.bukkit.Location(field_70170_p.getWorld(), srcX, srcY, srcZ);
        }
        // Paper end
    }

    public void func_145806_a(boolean flag) {
        this.field_145809_g = flag;
    }

    public void func_85029_a(CrashReportCategory crashreportsystemdetails) {
        super.func_85029_a(crashreportsystemdetails);
        if (this.field_175132_d != null) {
            Block block = this.field_175132_d.func_177230_c();

            crashreportsystemdetails.func_71507_a("Immitating block ID", (Object) Integer.valueOf(Block.func_149682_b(block)));
            crashreportsystemdetails.func_71507_a("Immitating block data", (Object) Integer.valueOf(block.func_176201_c(this.field_175132_d)));
        }

    }

    @Nullable
    public IBlockState func_175131_l() {
        return this.field_175132_d;
    }

    public boolean func_184213_bq() {
        return true;
    }
}
