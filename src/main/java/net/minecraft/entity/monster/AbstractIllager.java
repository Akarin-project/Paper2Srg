package net.minecraft.entity.monster;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;


public abstract class AbstractIllager extends EntityMob {

    protected static final DataParameter<Byte> field_193080_a = EntityDataManager.func_187226_a(AbstractIllager.class, DataSerializers.field_187191_a);

    public AbstractIllager(World world) {
        super(world);
    }

    protected void func_70088_a() {
        super.func_70088_a();
        this.field_70180_af.func_187214_a(AbstractIllager.field_193080_a, Byte.valueOf((byte) 0));
    }

    protected void func_193079_a(int i, boolean flag) {
        byte b0 = ((Byte) this.field_70180_af.func_187225_a(AbstractIllager.field_193080_a)).byteValue();
        int j;

        if (flag) {
            j = b0 | i;
        } else {
            j = b0 & ~i;
        }

        this.field_70180_af.func_187227_b(AbstractIllager.field_193080_a, Byte.valueOf((byte) (j & 255)));
    }

    public EnumCreatureAttribute func_70668_bt() {
        return EnumCreatureAttribute.ILLAGER;
    }
}
