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

    public static void registerFixesMinecartEmpty(DataFixer dataconvertermanager) {
        EntityMinecart.registerFixesMinecart(dataconvertermanager, EntityMinecartEmpty.class);
    }

    public boolean processInitialInteract(EntityPlayer entityhuman, EnumHand enumhand) {
        if (entityhuman.isSneaking()) {
            return false;
        } else if (this.isBeingRidden()) {
            return true;
        } else {
            if (!this.world.isRemote) {
                entityhuman.startRiding(this);
            }

            return true;
        }
    }

    public void onActivatorRailPass(int i, int j, int k, boolean flag) {
        if (flag) {
            if (this.isBeingRidden()) {
                this.removePassengers();
            }

            if (this.getRollingAmplitude() == 0) {
                this.setRollingDirection(-this.getRollingDirection());
                this.setRollingAmplitude(10);
                this.setDamage(50.0F);
                this.markVelocityChanged();
            }
        }

    }

    public EntityMinecart.Type getType() {
        return EntityMinecart.Type.RIDEABLE;
    }
}
