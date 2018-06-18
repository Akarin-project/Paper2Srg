package net.minecraft.tileentity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;


public class TileEntityEnderChest extends TileEntity { // Paper - Remove ITickable

    public float lidAngle; // Paper - lid angle
    public float prevLidAngle;
    public int numPlayersUsing; // Paper - Number of viewers
    private int ticksSinceSync;

    public TileEntityEnderChest() {}

    public void update() {
        // Paper start - Disable all of this, just in case this does get ticked
        /*
        if (++this.h % 20 * 4 == 0) {
            this.world.playBlockAction(this.position, Blocks.ENDER_CHEST, 1, this.g);
        }

        this.f = this.a;
        int i = this.position.getX();
        int j = this.position.getY();
        int k = this.position.getZ();
        float f = 0.1F;
        double d0;

        if (this.g > 0 && this.a == 0.0F) {
            double d1 = (double) i + 0.5D;

            d0 = (double) k + 0.5D;
            this.world.a((EntityHuman) null, d1, (double) j + 0.5D, d0, SoundEffects.aT, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
        }

        if (this.g == 0 && this.a > 0.0F || this.g > 0 && this.a < 1.0F) {
            float f1 = this.a;

            if (this.g > 0) {
                this.a += 0.1F;
            } else {
                this.a -= 0.1F;
            }

            if (this.a > 1.0F) {
                this.a = 1.0F;
            }

            float f2 = 0.5F;

            if (this.a < 0.5F && f1 >= 0.5F) {
                d0 = (double) i + 0.5D;
                double d2 = (double) k + 0.5D;

                this.world.a((EntityHuman) null, d0, (double) j + 0.5D, d2, SoundEffects.aS, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
            }

            if (this.a < 0.0F) {
                this.a = 0.0F;
            }
        }
        */
        // Paper end

    }

    public boolean receiveClientEvent(int i, int j) {
        if (i == 1) {
            this.numPlayersUsing = j;
            return true;
        } else {
            return super.receiveClientEvent(i, j);
        }
    }

    public void invalidate() {
        this.updateContainingBlockInfo();
        super.invalidate();
    }

    public void openChest() {
        ++this.numPlayersUsing;

        // Paper start - Move enderchest open sounds out of the tick loop
        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
            this.lidAngle = 0.7F;

            double d1 = (double) this.getPos().getX() + 0.5D;
            double d0 = (double) this.getPos().getZ() + 0.5D;

            this.world.playSound((EntityPlayer) null, d1, (double) this.getPos().getY() + 0.5D, d0, SoundEvents.BLOCK_ENDERCHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
        }
        // Paper end

        this.world.addBlockEvent(this.pos, Blocks.ENDER_CHEST, 1, this.numPlayersUsing);
    }

    public void closeChest() {
        --this.numPlayersUsing;

        // Paper start - Move enderchest close sounds out of the tick loop
        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
            double d0 = (double) this.getPos().getX() + 0.5D;
            double d2 = (double) this.getPos().getZ() + 0.5D;

            this.world.playSound((EntityPlayer) null, d0, (double) this.getPos().getY() + 0.5D, d2, SoundEvents.BLOCK_ENDERCHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            this.lidAngle = 0.0F;
        }
        // Paper end

        this.world.addBlockEvent(this.pos, Blocks.ENDER_CHEST, 1, this.numPlayersUsing);
    }

    public boolean canBeUsed(EntityPlayer entityhuman) {
        return this.world.getTileEntity(this.pos) != this ? false : entityhuman.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }
}
