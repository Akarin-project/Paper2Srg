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

    private IBlockState fallTile;
    public int fallTime;
    public boolean shouldDropItem = true;
    private boolean dontSetBlock;
    public boolean hurtEntities;
    private int fallHurtMax = 40;
    private float fallHurtAmount = 2.0F;
    public NBTTagCompound tileEntityData;
    protected static final DataParameter<BlockPos> ORIGIN = EntityDataManager.createKey(EntityFallingBlock.class, DataSerializers.BLOCK_POS);

    public EntityFallingBlock(World world) {
        super(world);
    }

    public EntityFallingBlock(World world, double d0, double d1, double d2, IBlockState iblockdata) {
        super(world);
        this.fallTile = iblockdata;
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.98F);
        this.setPosition(d0, d1 + (double) ((1.0F - this.height) / 2.0F), d2);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = d0;
        this.prevPosY = d1;
        this.prevPosZ = d2;
        this.setOrigin(new BlockPos(this));
    }

    public boolean canBeAttackedWithItem() {
        return false;
    }

    public void setOrigin(BlockPos blockposition) {
        this.dataManager.set(EntityFallingBlock.ORIGIN, blockposition);
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    protected void entityInit() {
        this.dataManager.register(EntityFallingBlock.ORIGIN, BlockPos.ORIGIN);
    }

    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    public void onUpdate() {
        Block block = this.fallTile.getBlock();

        if (this.fallTile.getMaterial() == Material.AIR) {
            this.setDead();
        } else {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            BlockPos blockposition;

            if (this.fallTime++ == 0) {
                blockposition = new BlockPos(this);
                if (this.world.getBlockState(blockposition).getBlock() == block && !CraftEventFactory.callEntityChangeBlockEvent(this, blockposition, Blocks.AIR, 0).isCancelled()) {
                    this.world.setBlockToAir(blockposition);
                    this.world.chunkPacketBlockController.updateNearbyBlocks(this.world, blockposition); // Paper - Anti-Xray
                } else if (!this.world.isRemote) {
                    this.setDead();
                    return;
                }
            }

            if (!this.hasNoGravity()) {
                this.motionY -= 0.03999999910593033D;
            }

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

            // Paper start - Configurable EntityFallingBlock height nerf
            if (this.world.paperConfig.fallingBlockHeightNerf != 0 && this.posY > this.world.paperConfig.fallingBlockHeightNerf) {
                if (this.shouldDropItem && this.world.getGameRules().getBoolean("doEntityDrops")) {
                    this.dropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0F);
                }

                this.setDead();
            }
            // Paper end

            if (!this.world.isRemote) {
                blockposition = new BlockPos(this);
                boolean flag = this.fallTile.getBlock() == Blocks.CONCRETE_POWDER;
                boolean flag1 = flag && this.world.getBlockState(blockposition).getMaterial() == Material.WATER;
                double d0 = this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ;

                if (flag && d0 > 1.0D) {
                    RayTraceResult movingobjectposition = this.world.rayTraceBlocks(new Vec3d(this.prevPosX, this.prevPosY, this.prevPosZ), new Vec3d(this.posX, this.posY, this.posZ), true);

                    if (movingobjectposition != null && this.world.getBlockState(movingobjectposition.getBlockPos()).getMaterial() == Material.WATER) {
                        blockposition = movingobjectposition.getBlockPos();
                        flag1 = true;
                    }
                }

                if (!this.onGround && !flag1) {
                    if (this.fallTime > 100 && !this.world.isRemote && (blockposition.getY() < 1 || blockposition.getY() > 256) || this.fallTime > 600) {
                        if (this.shouldDropItem && this.world.getGameRules().getBoolean("doEntityDrops")) {
                            this.entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0F);
                        }

                        this.setDead();
                    }
                } else {
                    IBlockState iblockdata = this.world.getBlockState(blockposition);
                    
                    if (!isOnGround()) {
                        this.onGround = false;
                        if (this.world.paperConfig.altFallingBlockOnGround) return; // Paper
                    }

                    this.motionX *= 0.699999988079071D;
                    this.motionZ *= 0.699999988079071D;
                    this.motionY *= -0.5D;
                    if (iblockdata.getBlock() != Blocks.PISTON_EXTENSION) {
                        this.setDead();
                        if (!this.dontSetBlock) {
                            // CraftBukkit start
                            if (this.world.mayPlace(block, blockposition, true, EnumFacing.UP, (Entity) null) && (flag1 || !BlockFalling.canFallThrough(this.world.getBlockState(blockposition.down())))) {
                                if (CraftEventFactory.callEntityChangeBlockEvent(this, blockposition, this.fallTile.getBlock(), this.fallTile.getBlock().getMetaFromState(this.fallTile)).isCancelled()) {
                                    return;
                                }
                                this.world.setBlockState(blockposition, this.fallTile, 3);
                                this.world.chunkPacketBlockController.updateNearbyBlocks(this.world, blockposition); // Paper - Anti-Xray
                                // CraftBukkit end
                                if (block instanceof BlockFalling) {
                                    ((BlockFalling) block).onEndFalling(this.world, blockposition, this.fallTile, iblockdata);
                                }

                                if (this.tileEntityData != null && block instanceof ITileEntityProvider) {
                                    TileEntity tileentity = this.world.getTileEntity(blockposition);

                                    if (tileentity != null) {
                                        NBTTagCompound nbttagcompound = tileentity.writeToNBT(new NBTTagCompound());
                                        Iterator iterator = this.tileEntityData.getKeySet().iterator();

                                        while (iterator.hasNext()) {
                                            String s = (String) iterator.next();
                                            NBTBase nbtbase = this.tileEntityData.getTag(s);

                                            if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s)) {
                                                nbttagcompound.setTag(s, nbtbase.copy());
                                            }
                                        }

                                        tileentity.readFromNBT(nbttagcompound);
                                        tileentity.markDirty();
                                    }
                                }
                            } else if (this.shouldDropItem && this.world.getGameRules().getBoolean("doEntityDrops")) {
                                this.entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0F);
                            }
                        } else if (block instanceof BlockFalling) {
                            ((BlockFalling) block).onBroken(this.world, blockposition);
                        }
                    }
                }
            }

            this.motionX *= 0.9800000190734863D;
            this.motionY *= 0.9800000190734863D;
            this.motionZ *= 0.9800000190734863D;
        }
    }

    // Paper start
    private boolean isOnGround() {
        BlockPos where = new BlockPos(this.posX, this.posY - 0.009999999776482582D, this.posZ);
        boolean cannotMoveThrough = !BlockFalling.canMoveThrough(this.world.getBlockState(where));
        if (!this.world.paperConfig.altFallingBlockOnGround) return cannotMoveThrough;

        if (cannotMoveThrough) {
            return true;
        }

        IBlockState blockData = this.world.getBlockState(where.down());
        if (BlockFalling.canMoveThrough(blockData)) {
            return false;
        }

        List<AxisAlignedBB> list = new ArrayList<>();
        addCollisions(blockData, getEntityWorld(), where, this.getEntityBoundingBox(), list, this);
        return list.size() > 0;
    }

    // OBFHELPER
    private void addCollisions(IBlockState blockData, World world, BlockPos where, AxisAlignedBB collider, List<AxisAlignedBB> list, Entity entity) {
        blockData.addCollisionBoxToList(world, where, collider, list, entity, false);
    }
    // Paper end

    public void fall(float f, float f1) {
        Block block = this.fallTile.getBlock();

        if (this.hurtEntities) {
            int i = MathHelper.ceil(f - 1.0F);

            if (i > 0) {
                ArrayList arraylist = Lists.newArrayList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()));
                boolean flag = block == Blocks.ANVIL;
                DamageSource damagesource = flag ? DamageSource.ANVIL : DamageSource.FALLING_BLOCK;
                Iterator iterator = arraylist.iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();

                    CraftEventFactory.entityDamage = this; // CraftBukkit
                    entity.attackEntityFrom(damagesource, (float) Math.min(MathHelper.floor((float) i * this.fallHurtAmount), this.fallHurtMax));
                    CraftEventFactory.entityDamage = null; // CraftBukkit
                }

                if (flag && (double) this.rand.nextFloat() < 0.05000000074505806D + (double) i * 0.05D) {
                    int j = ((Integer) this.fallTile.getValue(BlockAnvil.DAMAGE)).intValue();

                    ++j;
                    if (j > 2) {
                        this.dontSetBlock = true;
                    } else {
                        this.fallTile = this.fallTile.withProperty(BlockAnvil.DAMAGE, Integer.valueOf(j));
                    }
                }
            }
        }

    }

    public static void registerFixesFallingBlock(DataFixer dataconvertermanager) {}

    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        Block block = this.fallTile != null ? this.fallTile.getBlock() : Blocks.AIR;
        ResourceLocation minecraftkey = (ResourceLocation) Block.REGISTRY.getNameForObject(block);

        nbttagcompound.setString("Block", minecraftkey == null ? "" : minecraftkey.toString());
        nbttagcompound.setByte("Data", (byte) block.getMetaFromState(this.fallTile));
        nbttagcompound.setInteger("Time", this.fallTime);
        nbttagcompound.setBoolean("DropItem", this.shouldDropItem);
        nbttagcompound.setBoolean("HurtEntities", this.hurtEntities);
        nbttagcompound.setFloat("FallHurtAmount", this.fallHurtAmount);
        nbttagcompound.setInteger("FallHurtMax", this.fallHurtMax);
        if (this.tileEntityData != null) {
            nbttagcompound.setTag("TileEntityData", this.tileEntityData);
        }

    }

    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        int i = nbttagcompound.getByte("Data") & 255;

        if (nbttagcompound.hasKey("Block", 8)) {
            this.fallTile = Block.getBlockFromName(nbttagcompound.getString("Block")).getStateFromMeta(i);
        } else if (nbttagcompound.hasKey("TileID", 99)) {
            this.fallTile = Block.getBlockById(nbttagcompound.getInteger("TileID")).getStateFromMeta(i);
        } else {
            this.fallTile = Block.getBlockById(nbttagcompound.getByte("Tile") & 255).getStateFromMeta(i);
        }

        // Paper start - Block FallingBlocks with Command Blocks
        // Check mappings on update - dc = "repeating_command_block" - dd = "chain_command_block"
        final Block b = this.fallTile.getBlock();
        if (this.world.paperConfig.filterNBTFromSpawnEgg && (b == Blocks.COMMAND_BLOCK || b == Blocks.REPEATING_COMMAND_BLOCK || b == Blocks.CHAIN_COMMAND_BLOCK)) {
            this.fallTile = Blocks.STONE.getDefaultState();
        }
        // Paper end

        this.fallTime = nbttagcompound.getInteger("Time");
        Block block = this.fallTile.getBlock();

        if (nbttagcompound.hasKey("HurtEntities", 99)) {
            this.hurtEntities = nbttagcompound.getBoolean("HurtEntities");
            this.fallHurtAmount = nbttagcompound.getFloat("FallHurtAmount");
            this.fallHurtMax = nbttagcompound.getInteger("FallHurtMax");
        } else if (block == Blocks.ANVIL) {
            this.hurtEntities = true;
        }

        if (nbttagcompound.hasKey("DropItem", 99)) {
            this.shouldDropItem = nbttagcompound.getBoolean("DropItem");
        }

        if (nbttagcompound.hasKey("TileEntityData", 10)) {
            this.tileEntityData = nbttagcompound.getCompoundTag("TileEntityData");
        }

        if (block == null || block.getDefaultState().getMaterial() == Material.AIR) {
            this.fallTile = Blocks.SAND.getDefaultState();
        }

        // Paper start - Try and load origin location from the old NBT tags for backwards compatibility
        if (nbttagcompound.hasKey("SourceLoc_x")) {
            int srcX = nbttagcompound.getInteger("SourceLoc_x");
            int srcY = nbttagcompound.getInteger("SourceLoc_y");
            int srcZ = nbttagcompound.getInteger("SourceLoc_z");
            origin = new org.bukkit.Location(world.getWorld(), srcX, srcY, srcZ);
        }
        // Paper end
    }

    public void setHurtEntities(boolean flag) {
        this.hurtEntities = flag;
    }

    public void addEntityCrashInfo(CrashReportCategory crashreportsystemdetails) {
        super.addEntityCrashInfo(crashreportsystemdetails);
        if (this.fallTile != null) {
            Block block = this.fallTile.getBlock();

            crashreportsystemdetails.addCrashSection("Immitating block ID", (Object) Integer.valueOf(Block.getIdFromBlock(block)));
            crashreportsystemdetails.addCrashSection("Immitating block data", (Object) Integer.valueOf(block.getMetaFromState(this.fallTile)));
        }

    }

    @Nullable
    public IBlockState getBlock() {
        return this.fallTile;
    }

    public boolean ignoreItemEntityData() {
        return true;
    }
}
