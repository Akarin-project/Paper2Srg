package net.minecraft.server.management;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;


public class DemoPlayerInteractionManager extends PlayerInteractionManager {

    private boolean displayedIntro;
    private boolean demoTimeExpired;
    private int demoEndedReminder;
    private int gameModeTicks;

    public DemoPlayerInteractionManager(World world) {
        super(world);
    }

    public void updateBlockRemoving() {
        super.updateBlockRemoving();
        ++this.gameModeTicks;
        long i = this.world.getTotalWorldTime();
        long j = i / 24000L + 1L;

        if (!this.displayedIntro && this.gameModeTicks > 20) {
            this.displayedIntro = true;
            this.player.connection.sendPacket(new SPacketChangeGameState(5, 0.0F));
        }

        this.demoTimeExpired = i > 120500L;
        if (this.demoTimeExpired) {
            ++this.demoEndedReminder;
        }

        if (i % 24000L == 500L) {
            if (j <= 6L) {
                this.player.sendMessage(new TextComponentTranslation("demo.day." + j, new Object[0]));
            }
        } else if (j == 1L) {
            if (i == 100L) {
                this.player.connection.sendPacket(new SPacketChangeGameState(5, 101.0F));
            } else if (i == 175L) {
                this.player.connection.sendPacket(new SPacketChangeGameState(5, 102.0F));
            } else if (i == 250L) {
                this.player.connection.sendPacket(new SPacketChangeGameState(5, 103.0F));
            }
        } else if (j == 5L && i % 24000L == 22000L) {
            this.player.sendMessage(new TextComponentTranslation("demo.day.warning", new Object[0]));
        }

    }

    private void sendDemoReminder() {
        if (this.demoEndedReminder > 100) {
            this.player.sendMessage(new TextComponentTranslation("demo.reminder", new Object[0]));
            this.demoEndedReminder = 0;
        }

    }

    public void onBlockClicked(BlockPos blockposition, EnumFacing enumdirection) {
        if (this.demoTimeExpired) {
            this.sendDemoReminder();
        } else {
            super.onBlockClicked(blockposition, enumdirection);
        }
    }

    public void blockRemoving(BlockPos blockposition) {
        if (!this.demoTimeExpired) {
            super.blockRemoving(blockposition);
        }
    }

    public boolean tryHarvestBlock(BlockPos blockposition) {
        return this.demoTimeExpired ? false : super.tryHarvestBlock(blockposition);
    }

    public EnumActionResult processRightClick(EntityPlayer entityhuman, World world, ItemStack itemstack, EnumHand enumhand) {
        if (this.demoTimeExpired) {
            this.sendDemoReminder();
            return EnumActionResult.PASS;
        } else {
            return super.processRightClick(entityhuman, world, itemstack, enumhand);
        }
    }

    public EnumActionResult processRightClickBlock(EntityPlayer entityhuman, World world, ItemStack itemstack, EnumHand enumhand, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2) {
        if (this.demoTimeExpired) {
            this.sendDemoReminder();
            return EnumActionResult.PASS;
        } else {
            return super.processRightClickBlock(entityhuman, world, itemstack, enumhand, blockposition, enumdirection, f, f1, f2);
        }
    }
}
