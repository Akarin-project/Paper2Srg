package net.minecraft.entity.item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;


public class EntityMinecartEmpty extends EntityMinecart {

    public EntityMinecartEmpty(World world) {
        super(world);
    }

    public EntityMinecartEmpty(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    public static void func_189673_a(DataFixer dataconvertermanager) {
        EntityMinecart.func_189669_a(dataconvertermanager, EntityMinecartEmpty.class);
    }

    public boolean func_184230_a(EntityPlayer entityhuman, EnumHand enumhand) {
        if (entityhuman.func_70093_af()) {
            return false;
        } else if (this.func_184207_aI()) {
            return true;
        } else {
            if (!this.field_70170_p.field_72995_K) {
                entityhuman.func_184220_m(this);
            }

            return true;
        }
    }

    public void func_96095_a(int i, int j, int k, boolean flag) {
        if (flag) {
            if (this.func_184207_aI()) {
                this.func_184226_ay();
            }

            if (this.func_70496_j() == 0) {
                this.func_70494_i(-this.func_70493_k());
                this.func_70497_h(10);
                this.func_70492_c(50.0F);
                this.func_70018_K();
            }
        }

    }

    public EntityMinecart.Type func_184264_v() {
        return EntityMinecart.Type.RIDEABLE;
    }
}
