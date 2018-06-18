package net.minecraft.entity.item;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;


public class EntityMinecartCommandBlock extends EntityMinecart {

    public static final DataParameter<String> COMMAND = EntityDataManager.createKey(EntityMinecartCommandBlock.class, DataSerializers.STRING);
    private static final DataParameter<ITextComponent> LAST_OUTPUT = EntityDataManager.createKey(EntityMinecartCommandBlock.class, DataSerializers.TEXT_COMPONENT);
    private final CommandBlockBaseLogic commandBlockLogic = new CommandBlockBaseLogic() {
        {
            this.sender = (org.bukkit.craftbukkit.entity.CraftMinecartCommand) EntityMinecartCommandBlock.this.getBukkitEntity(); // CraftBukkit - Set the sender
        }
        public void updateCommand() {
            EntityMinecartCommandBlock.this.getDataManager().set(EntityMinecartCommandBlock.COMMAND, this.getCommand());
            EntityMinecartCommandBlock.this.getDataManager().set(EntityMinecartCommandBlock.LAST_OUTPUT, this.getLastOutput());
        }

        public BlockPos getPosition() {
            return new BlockPos(EntityMinecartCommandBlock.this.posX, EntityMinecartCommandBlock.this.posY + 0.5D, EntityMinecartCommandBlock.this.posZ);
        }

        public Vec3d getPositionVector() {
            return new Vec3d(EntityMinecartCommandBlock.this.posX, EntityMinecartCommandBlock.this.posY, EntityMinecartCommandBlock.this.posZ);
        }

        public World getEntityWorld() {
            return EntityMinecartCommandBlock.this.world;
        }

        public Entity getCommandSenderEntity() {
            return EntityMinecartCommandBlock.this;
        }

        public MinecraftServer getServer() {
            return EntityMinecartCommandBlock.this.world.getMinecraftServer();
        }
    };
    private int activatorRailCooldown;

    public EntityMinecartCommandBlock(World world) {
        super(world);
    }

    public EntityMinecartCommandBlock(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    public static void registerFixesMinecartCommand(DataFixer dataconvertermanager) {
        EntityMinecart.registerFixesMinecart(dataconvertermanager, EntityMinecartCommandBlock.class);
        dataconvertermanager.registerWalker(FixTypes.ENTITY, new IDataWalker() {
            public NBTTagCompound process(IDataFixer dataconverter, NBTTagCompound nbttagcompound, int i) {
                if (TileEntity.getKey(TileEntityCommandBlock.class).equals(new ResourceLocation(nbttagcompound.getString("id")))) {
                    nbttagcompound.setString("id", "Control");
                    dataconverter.process(FixTypes.BLOCK_ENTITY, nbttagcompound, i);
                    nbttagcompound.setString("id", "MinecartCommandBlock");
                }

                return nbttagcompound;
            }
        });
    }

    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(EntityMinecartCommandBlock.COMMAND, "");
        this.getDataManager().register(EntityMinecartCommandBlock.LAST_OUTPUT, new TextComponentString(""));
    }

    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.commandBlockLogic.readDataFromNBT(nbttagcompound);
        this.getDataManager().set(EntityMinecartCommandBlock.COMMAND, this.getCommandBlockLogic().getCommand());
        this.getDataManager().set(EntityMinecartCommandBlock.LAST_OUTPUT, this.getCommandBlockLogic().getLastOutput());
    }

    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        this.commandBlockLogic.writeToNBT(nbttagcompound);
    }

    public EntityMinecart.Type getType() {
        return EntityMinecart.Type.COMMAND_BLOCK;
    }

    public IBlockState getDefaultDisplayTile() {
        return Blocks.COMMAND_BLOCK.getDefaultState();
    }

    public CommandBlockBaseLogic getCommandBlockLogic() {
        return this.commandBlockLogic;
    }

    public void onActivatorRailPass(int i, int j, int k, boolean flag) {
        if (flag && this.ticksExisted - this.activatorRailCooldown >= 4) {
            this.getCommandBlockLogic().trigger(this.world);
            this.activatorRailCooldown = this.ticksExisted;
        }

    }

    public boolean processInitialInteract(EntityPlayer entityhuman, EnumHand enumhand) {
        this.commandBlockLogic.tryOpenEditCommandBlock(entityhuman);
        return false;
    }

    public void notifyDataManagerChange(DataParameter<?> datawatcherobject) {
        super.notifyDataManagerChange(datawatcherobject);
        if (EntityMinecartCommandBlock.LAST_OUTPUT.equals(datawatcherobject)) {
            try {
                this.commandBlockLogic.setLastOutput((ITextComponent) this.getDataManager().get(EntityMinecartCommandBlock.LAST_OUTPUT));
            } catch (Throwable throwable) {
                ;
            }
        } else if (EntityMinecartCommandBlock.COMMAND.equals(datawatcherobject)) {
            this.commandBlockLogic.setCommand((String) this.getDataManager().get(EntityMinecartCommandBlock.COMMAND));
        }

    }

    public boolean ignoreItemEntityData() {
        return true;
    }
}
