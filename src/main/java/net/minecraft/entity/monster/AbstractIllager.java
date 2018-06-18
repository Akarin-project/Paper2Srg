package net.minecraft.entity.monster;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;


public abstract class AbstractIllager extends EntityMob {

    protected static final DataParameter<Byte> AGGRESSIVE = EntityDataManager.createKey(AbstractIllager.class, DataSerializers.BYTE);

    public AbstractIllager(World world) {
        super(world);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(AbstractIllager.AGGRESSIVE, Byte.valueOf((byte) 0));
    }

    protected void setAggressive(int i, boolean flag) {
        byte b0 = ((Byte) this.dataManager.get(AbstractIllager.AGGRESSIVE)).byteValue();
        int j;

        if (flag) {
            j = b0 | i;
        } else {
            j = b0 & ~i;
        }

        this.dataManager.set(AbstractIllager.AGGRESSIVE, Byte.valueOf((byte) (j & 255)));
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ILLAGER;
    }
}
