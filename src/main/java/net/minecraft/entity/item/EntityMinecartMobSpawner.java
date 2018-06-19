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

    private final MobSpawnerBaseLogic field_98040_a = new MobSpawnerBaseLogic() {
        public void func_98267_a(int i) {
            EntityMinecartMobSpawner.this.field_70170_p.func_72960_a(EntityMinecartMobSpawner.this, (byte) i);
        }

        public World func_98271_a() {
            return EntityMinecartMobSpawner.this.field_70170_p;
        }

        public BlockPos func_177221_b() {
            return new BlockPos(EntityMinecartMobSpawner.this);
        }
    };

    public EntityMinecartMobSpawner(World world) {
        super(world);
    }

    public EntityMinecartMobSpawner(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    public static void func_189672_a(DataFixer dataconvertermanager) {
        func_189669_a(dataconvertermanager, EntityMinecartMobSpawner.class);
        dataconvertermanager.func_188258_a(FixTypes.ENTITY, new IDataWalker() {
            public NBTTagCompound func_188266_a(IDataFixer dataconverter, NBTTagCompound nbttagcompound, int i) {
                String s = nbttagcompound.func_74779_i("id");

                if (EntityList.func_191306_a(EntityMinecartMobSpawner.class).equals(new ResourceLocation(s))) {
                    nbttagcompound.func_74778_a("id", TileEntity.func_190559_a(TileEntityMobSpawner.class).toString());
                    dataconverter.func_188251_a(FixTypes.BLOCK_ENTITY, nbttagcompound, i);
                    nbttagcompound.func_74778_a("id", s);
                }

                return nbttagcompound;
            }
        });
    }

    public EntityMinecart.Type func_184264_v() {
        return EntityMinecart.Type.SPAWNER;
    }

    public IBlockState func_180457_u() {
        return Blocks.field_150474_ac.func_176223_P();
    }

    protected void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        this.field_98040_a.func_98270_a(nbttagcompound);
    }

    protected void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        this.field_98040_a.func_189530_b(nbttagcompound);
    }

    public void func_70071_h_() {
        super.func_70071_h_();
        this.field_98040_a.func_98278_g();
    }
}
