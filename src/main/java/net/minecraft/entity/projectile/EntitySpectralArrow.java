package net.minecraft.entity.projectile;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;


public class EntitySpectralArrow extends EntityArrow {

    public int duration = 200;

    public EntitySpectralArrow(World world) {
        super(world);
    }

    public EntitySpectralArrow(World world, EntityLivingBase entityliving) {
        super(world, entityliving);
    }

    public EntitySpectralArrow(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote && !this.inGround) {
            this.world.spawnParticle(EnumParticleTypes.SPELL_INSTANT, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
        }

    }

    protected ItemStack getArrowStack() {
        return new ItemStack(Items.SPECTRAL_ARROW);
    }

    protected void arrowHit(EntityLivingBase entityliving) {
        super.arrowHit(entityliving);
        PotionEffect mobeffect = new PotionEffect(MobEffects.GLOWING, this.duration, 0);

        entityliving.addPotionEffect(mobeffect);
    }

    public static void registerFixesSpectralArrow(DataFixer dataconvertermanager) {
        EntityArrow.registerFixesArrow(dataconvertermanager, "SpectralArrow");
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        if (nbttagcompound.hasKey("Duration")) {
            this.duration = nbttagcompound.getInteger("Duration");
        }

    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("Duration", this.duration);
    }
}
