package net.minecraft.entity.ai;
import net.minecraft.entity.EntityLiving;


public class EntityJumpHelper {

    private final EntityLiving entity;
    protected boolean isJumping;

    public EntityJumpHelper(EntityLiving entityinsentient) {
        this.entity = entityinsentient;
    }

    public void setJumping() {
        this.isJumping = true;
    }

    public void jumpIfSet() { this.doJump(); } // Paper - OBFHELPER
    public void doJump() {
        this.entity.setJumping(this.isJumping);
        this.isJumping = false;
    }
}
