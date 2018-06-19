package org.bukkit.craftbukkit.projectiles;

import java.util.Random;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.WitherSkull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.util.Vector;

import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.tileentity.TileEntityDispenser;

import net;

public class CraftBlockProjectileSource implements BlockProjectileSource {
    private final TileEntityDispenser dispenserBlock;

    public CraftBlockProjectileSource(TileEntityDispenser dispenserBlock) {
        this.dispenserBlock = dispenserBlock;
    }

    @Override
    public Block getBlock() {
        return dispenserBlock.func_145831_w().getWorld().getBlockAt(dispenserBlock.func_174877_v().func_177958_n(), dispenserBlock.func_174877_v().func_177956_o(), dispenserBlock.func_174877_v().func_177952_p());
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return launchProjectile(projectile, null);
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        Validate.isTrue(getBlock().getType() == Material.DISPENSER, "Block is no longer dispenser");
        // Copied from BlockDispenser.dispense()
        BlockSourceImpl isourceblock = new BlockSourceImpl(dispenserBlock.func_145831_w(), dispenserBlock.func_174877_v());
        // Copied from DispenseBehaviorProjectile
        IPosition iposition = BlockDispenser.func_149939_a(isourceblock);
        EnumFacing enumdirection = (EnumFacing) isourceblock.func_189992_e().func_177229_b(BlockDispenser.field_176441_a);
        net.minecraft.world.World world = dispenserBlock.func_145831_w();
        net.minecraft.entity.Entity launch = null;

        if (Snowball.class.isAssignableFrom(projectile)) {
            launch = new EntitySnowball(world, iposition.func_82615_a(), iposition.func_82617_b(), iposition.func_82616_c());
        } else if (Egg.class.isAssignableFrom(projectile)) {
            launch = new EntityEgg(world, iposition.func_82615_a(), iposition.func_82617_b(), iposition.func_82616_c());
        } else if (EnderPearl.class.isAssignableFrom(projectile)) {
            launch = new EntityEnderPearl(world, null);
            launch.func_70107_b(iposition.func_82615_a(), iposition.func_82617_b(), iposition.func_82616_c());
        } else if (ThrownExpBottle.class.isAssignableFrom(projectile)) {
            launch = new EntityExpBottle(world, iposition.func_82615_a(), iposition.func_82617_b(), iposition.func_82616_c());
        } else if (ThrownPotion.class.isAssignableFrom(projectile)) {
            if (LingeringPotion.class.isAssignableFrom(projectile)) {
                launch = new EntityPotion(world, iposition.func_82615_a(), iposition.func_82617_b(), iposition.func_82616_c(), CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.LINGERING_POTION, 1)));
            } else {
                launch = new EntityPotion(world, iposition.func_82615_a(), iposition.func_82617_b(), iposition.func_82616_c(), CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.SPLASH_POTION, 1)));
            }
        } else if (Arrow.class.isAssignableFrom(projectile)) {
            if (TippedArrow.class.isAssignableFrom(projectile)) {
                launch = new EntityTippedArrow(world, iposition.func_82615_a(), iposition.func_82617_b(), iposition.func_82616_c());
                ((EntityTippedArrow) launch).setType(CraftPotionUtil.fromBukkit(new PotionData(PotionType.WATER, false, false)));
            } else if (SpectralArrow.class.isAssignableFrom(projectile)) {
                launch = new EntitySpectralArrow(world, iposition.func_82615_a(), iposition.func_82617_b(), iposition.func_82616_c());
            } else {
                launch = new EntityTippedArrow(world, iposition.func_82615_a(), iposition.func_82617_b(), iposition.func_82616_c());
            }
            ((EntityArrow) launch).field_70251_a = EntityArrow.PickupStatus.ALLOWED;
            ((EntityArrow) launch).projectileSource = this;
        } else if (Fireball.class.isAssignableFrom(projectile)) {
            double d0 = iposition.func_82615_a() + (double) ((float) enumdirection.func_82601_c() * 0.3F);
            double d1 = iposition.func_82617_b() + (double) ((float) enumdirection.func_96559_d() * 0.3F);
            double d2 = iposition.func_82616_c() + (double) ((float) enumdirection.func_82599_e() * 0.3F);
            Random random = world.field_73012_v;
            double d3 = random.nextGaussian() * 0.05D + (double) enumdirection.func_82601_c();
            double d4 = random.nextGaussian() * 0.05D + (double) enumdirection.func_96559_d();
            double d5 = random.nextGaussian() * 0.05D + (double) enumdirection.func_82599_e();

            if (SmallFireball.class.isAssignableFrom(projectile)) {
                launch = new EntitySmallFireball(world, null, d0, d1, d2);
            } else if (WitherSkull.class.isAssignableFrom(projectile)) {
                launch = new EntityWitherSkull(world);
                launch.func_70107_b(d0, d1, d2);
                double d6 = (double) MathHelper.func_76133_a(d3 * d3 + d4 * d4 + d5 * d5);

                ((EntityFireball) launch).field_70232_b = d3 / d6 * 0.1D;
                ((EntityFireball) launch).field_70233_c = d4 / d6 * 0.1D;
                ((EntityFireball) launch).field_70230_d = d5 / d6 * 0.1D;
            } else {
                launch = new EntityLargeFireball(world);
                launch.func_70107_b(d0, d1, d2);
                double d6 = (double) MathHelper.func_76133_a(d3 * d3 + d4 * d4 + d5 * d5);

                ((EntityFireball) launch).field_70232_b = d3 / d6 * 0.1D;
                ((EntityFireball) launch).field_70233_c = d4 / d6 * 0.1D;
                ((EntityFireball) launch).field_70230_d = d5 / d6 * 0.1D;
            }

            ((EntityFireball) launch).projectileSource = this;
        }

        Validate.notNull(launch, "Projectile not supported");

        if (launch instanceof IProjectile) {
            if (launch instanceof EntityThrowable) {
                ((EntityThrowable) launch).projectileSource = this;
            }
            // Values from DispenseBehaviorProjectile
            float a = 6.0F;
            float b = 1.1F;
            if (launch instanceof EntityPotion || launch instanceof ThrownExpBottle) {
                // Values from respective DispenseBehavior classes
                a *= 0.5F;
                b *= 1.25F;
            }
            // Copied from DispenseBehaviorProjectile
            ((IProjectile) launch).func_70186_c((double) enumdirection.func_82601_c(), (double) ((float) enumdirection.func_96559_d() + 0.1F), (double) enumdirection.func_82599_e(), b, a);
        }

        if (velocity != null) {
            ((T) launch.getBukkitEntity()).setVelocity(velocity);
        }

        world.func_72838_d(launch);
        return (T) launch.getBukkitEntity();
    }
}
