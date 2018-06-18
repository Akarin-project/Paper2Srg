package net.minecraft.entity.item;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class EntityMinecartMobSpawner extends EntityMinecart {

    private final MobSpawnerBaseLogic mobSpawnerLogic = new MobSpawnerBaseLogic() {
        public void broadcastEvent(int i) {
            EntityMinecartMobSpawner.this.world.setEntityState(EntityMinecartMobSpawner.this, (byte) i);
        }

        public World getSpawnerWorld() {
            return EntityMinecartMobSpawner.this.world;
        }

        public BlockPos getSpawnerPosition() {
            return new BlockPos(EntityMinecartMobSpawner.this);
        }
    };

    public EntityMinecartMobSpawner(World world) {
        super(world);
    }

    public EntityMinecartMobSpawner(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    public static void registerFixesMinecartMobSpawner(DataFixer dataconvertermanager) {
        registerFixesMinecart(dataconvertermanager, EntityMinecartMobSpawner.class);
        dataconvertermanager.registerWalker(FixTypes.ENTITY, new IDataWalker() {
            public NBTTagCompound process(IDataFixer dataconverter, NBTTagCompound nbttagcompound, int i) {
                String s = nbttagcompound.getString("id");

                if (EntityList.getKey(EntityMinecartMobSpawner.class).equals(new ResourceLocation(s))) {
                    nbttagcompound.setString("id", TileEntity.getKey(TileEntityMobSpawner.class).toString());
                    dataconverter.process(FixTypes.BLOCK_ENTITY, nbttagcompound, i);
                    nbttagcompound.setString("id", s);
                }

                return nbttagcompound;
            }
        });
    }

    public EntityMinecart.Type getType() {
        return EntityMinecart.Type.SPAWNER;
    }

    public IBlockState getDefaultDisplayTile() {
        return Blocks.MOB_SPAWNER.getDefaultState();
    }

    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.mobSpawnerLogic.readFromNBT(nbttagcompound);
    }

    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        this.mobSpawnerLogic.writeToNBT(nbttagcompound);
    }

    public void onUpdate() {
        super.onUpdate();
        this.mobSpawnerLogic.updateSpawner();
    }
}
