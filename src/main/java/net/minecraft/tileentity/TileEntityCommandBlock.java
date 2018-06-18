package net.minecraft.tileentity;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandResultStats;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TileEntityCommandBlock extends TileEntity {

    private boolean powered;
    private boolean auto;
    private boolean conditionMet;
    private boolean sendToClient;
    private final CommandBlockBaseLogic commandBlockLogic = new CommandBlockBaseLogic() {
        {
            sender = new org.bukkit.craftbukkit.command.CraftBlockCommandSender(this); // CraftBukkit - add sender
        }
        public BlockPos getPosition() {
            return TileEntityCommandBlock.this.pos;
        }

        public Vec3d getPositionVector() {
            return new Vec3d((double) TileEntityCommandBlock.this.pos.getX() + 0.5D, (double) TileEntityCommandBlock.this.pos.getY() + 0.5D, (double) TileEntityCommandBlock.this.pos.getZ() + 0.5D);
        }

        public World getEntityWorld() {
            return TileEntityCommandBlock.this.getWorld();
        }

        public void setCommand(String s) {
            super.setCommand(s);
            TileEntityCommandBlock.this.markDirty();
        }

        public void updateCommand() {
            IBlockState iblockdata = TileEntityCommandBlock.this.world.getBlockState(TileEntityCommandBlock.this.pos);

            TileEntityCommandBlock.this.getWorld().notifyBlockUpdate(TileEntityCommandBlock.this.pos, iblockdata, iblockdata, 3);
        }

        public MinecraftServer getServer() {
            return TileEntityCommandBlock.this.world.getMinecraftServer();
        }
    };

    public TileEntityCommandBlock() {}

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        this.commandBlockLogic.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("powered", this.isPowered());
        nbttagcompound.setBoolean("conditionMet", this.isConditionMet());
        nbttagcompound.setBoolean("auto", this.isAuto());
        return nbttagcompound;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.commandBlockLogic.readDataFromNBT(nbttagcompound);
        this.powered = nbttagcompound.getBoolean("powered");
        this.conditionMet = nbttagcompound.getBoolean("conditionMet");
        this.setAuto(nbttagcompound.getBoolean("auto"));
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        if (this.isSendToClient()) {
            this.setSendToClient(false);
            NBTTagCompound nbttagcompound = this.writeToNBT(new NBTTagCompound());

            return new SPacketUpdateTileEntity(this.pos, 2, nbttagcompound);
        } else {
            return null;
        }
    }

    public boolean onlyOpsCanSetNbt() {
        return true;
    }

    public CommandBlockBaseLogic getCommandBlockLogic() {
        return this.commandBlockLogic;
    }

    public CommandResultStats getCommandResultStats() {
        return this.commandBlockLogic.getCommandResultStats();
    }

    public void setPowered(boolean flag) {
        this.powered = flag;
    }

    public boolean isPowered() {
        return this.powered;
    }

    public boolean isAuto() {
        return this.auto;
    }

    public void setAuto(boolean flag) {
        boolean flag1 = this.auto;

        this.auto = flag;
        if (!flag1 && flag && !this.powered && this.world != null && this.getMode() != TileEntityCommandBlock.Mode.SEQUENCE) {
            Block block = this.getBlockType();

            if (block instanceof BlockCommandBlock) {
                this.setConditionMet();
                this.world.scheduleUpdate(this.pos, block, block.tickRate(this.world));
            }
        }

    }

    public boolean isConditionMet() {
        return this.conditionMet;
    }

    public boolean setConditionMet() {
        this.conditionMet = true;
        if (this.isConditional()) {
            BlockPos blockposition = this.pos.offset(((EnumFacing) this.world.getBlockState(this.pos).getValue(BlockCommandBlock.FACING)).getOpposite());

            if (this.world.getBlockState(blockposition).getBlock() instanceof BlockCommandBlock) {
                TileEntity tileentity = this.world.getTileEntity(blockposition);

                this.conditionMet = tileentity instanceof TileEntityCommandBlock && ((TileEntityCommandBlock) tileentity).getCommandBlockLogic().getSuccessCount() > 0;
            } else {
                this.conditionMet = false;
            }
        }

        return this.conditionMet;
    }

    public boolean isSendToClient() {
        return this.sendToClient;
    }

    public void setSendToClient(boolean flag) {
        this.sendToClient = flag;
    }

    public TileEntityCommandBlock.Mode getMode() {
        Block block = this.getBlockType();

        return block == Blocks.COMMAND_BLOCK ? TileEntityCommandBlock.Mode.REDSTONE : (block == Blocks.REPEATING_COMMAND_BLOCK ? TileEntityCommandBlock.Mode.AUTO : (block == Blocks.CHAIN_COMMAND_BLOCK ? TileEntityCommandBlock.Mode.SEQUENCE : TileEntityCommandBlock.Mode.REDSTONE));
    }

    public boolean isConditional() {
        IBlockState iblockdata = this.world.getBlockState(this.getPos());

        return iblockdata.getBlock() instanceof BlockCommandBlock ? ((Boolean) iblockdata.getValue(BlockCommandBlock.CONDITIONAL)).booleanValue() : false;
    }

    public void validate() {
        this.blockType = null;
        super.validate();
    }

    public static enum Mode {

        SEQUENCE, AUTO, REDSTONE;

        private Mode() {}
    }
}
