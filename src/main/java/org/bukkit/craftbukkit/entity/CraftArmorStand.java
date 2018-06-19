package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.math.Rotations;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public class CraftArmorStand extends CraftLivingEntity implements ArmorStand {

    public CraftArmorStand(CraftServer server, EntityArmorStand entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftArmorStand";
    }

    @Override
    public EntityType getType() {
        return EntityType.ARMOR_STAND;
    }

    @Override
    public EntityArmorStand getHandle() {
        return (EntityArmorStand) super.getHandle();
    }

    @Override
    public ItemStack getItemInHand() {
        return getEquipment().getItemInHand();
    }

    @Override
    public void setItemInHand(ItemStack item) {
        getEquipment().setItemInHand(item);
    }

    @Override
    public ItemStack getBoots() {
        return getEquipment().getBoots();
    }

    @Override
    public void setBoots(ItemStack item) {
        getEquipment().setBoots(item);
    }

    @Override
    public ItemStack getLeggings() {
        return getEquipment().getLeggings();
    }

    @Override
    public void setLeggings(ItemStack item) {
        getEquipment().setLeggings(item);
    }

    @Override
    public ItemStack getChestplate() {
        return getEquipment().getChestplate();
    }

    @Override
    public void setChestplate(ItemStack item) {
        getEquipment().setChestplate(item);
    }

    @Override
    public ItemStack getHelmet() {
        return getEquipment().getHelmet();
    }

    @Override
    public void setHelmet(ItemStack item) {
        getEquipment().setHelmet(item);
    }

    @Override
    public EulerAngle getBodyPose() {
        return fromNMS(getHandle().field_175444_bi);
    }

    @Override
    public void setBodyPose(EulerAngle pose) {
        getHandle().func_175424_b(toNMS(pose));
    }

    @Override
    public EulerAngle getLeftArmPose() {
        return fromNMS(getHandle().field_175438_bj);
    }

    @Override
    public void setLeftArmPose(EulerAngle pose) {
        getHandle().func_175405_c(toNMS(pose));
    }

    @Override
    public EulerAngle getRightArmPose() {
        return fromNMS(getHandle().field_175439_bk);
    }

    @Override
    public void setRightArmPose(EulerAngle pose) {
        getHandle().func_175428_d(toNMS(pose));
    }

    @Override
    public EulerAngle getLeftLegPose() {
        return fromNMS(getHandle().field_175440_bl);
    }

    @Override
    public void setLeftLegPose(EulerAngle pose) {
        getHandle().func_175417_e(toNMS(pose));
    }

    @Override
    public EulerAngle getRightLegPose() {
        return fromNMS(getHandle().field_175441_bm);
    }

    @Override
    public void setRightLegPose(EulerAngle pose) {
        getHandle().func_175427_f(toNMS(pose));
    }

    @Override
    public EulerAngle getHeadPose() {
        return fromNMS(getHandle().field_175443_bh);
    }

    @Override
    public void setHeadPose(EulerAngle pose) {
        getHandle().func_175415_a(toNMS(pose));
    }

    @Override
    public boolean hasBasePlate() {
        return !getHandle().func_175414_r();
    }

    @Override
    public void setBasePlate(boolean basePlate) {
        getHandle().func_175426_l(!basePlate);
    }

    @Override
    public void setGravity(boolean gravity) {
        super.setGravity(gravity);
        // Armor stands are special
        getHandle().field_70145_X = !gravity;
    }

    @Override
    public boolean isVisible() {
        return !getHandle().func_82150_aj();
    }

    @Override
    public void setVisible(boolean visible) {
        getHandle().func_82142_c(!visible);
    }

    @Override
    public boolean hasArms() {
        return getHandle().func_175402_q();
    }

    @Override
    public void setArms(boolean arms) {
        getHandle().func_175413_k(arms);
    }

    @Override
    public boolean isSmall() {
        return getHandle().func_175410_n();
    }

    @Override
    public void setSmall(boolean small) {
        getHandle().func_175420_a(small);
    }

    private static EulerAngle fromNMS(Rotations old) {
        return new EulerAngle(
            Math.toRadians(old.func_179415_b()),
            Math.toRadians(old.func_179416_c()),
            Math.toRadians(old.func_179413_d())
        );
    }

    private static Rotations toNMS(EulerAngle old) {
        return new Rotations(
            (float) Math.toDegrees(old.getX()),
            (float) Math.toDegrees(old.getY()),
            (float) Math.toDegrees(old.getZ())
        );
    }

    @Override
    public boolean isMarker() {
        return getHandle().func_181026_s();
    }

    @Override
    public void setMarker(boolean marker) {
        getHandle().func_181027_m(marker);
    }

    @Override
    public boolean canMove() {
        return getHandle().canMove;
    }

    @Override
    public void setCanMove(boolean move) {
        getHandle().canMove = move;
    }
}
