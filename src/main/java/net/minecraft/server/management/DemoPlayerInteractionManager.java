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

    private boolean field_73105_c;
    private boolean field_73103_d;
    private int field_73104_e;
    private int field_73102_f;

    public DemoPlayerInteractionManager(World world) {
        super(world);
    }

    public void func_73075_a() {
        super.func_73075_a();
        ++this.field_73102_f;
        long i = this.field_73092_a.func_82737_E();
        long j = i / 24000L + 1L;

        if (!this.field_73105_c && this.field_73102_f > 20) {
            this.field_73105_c = true;
            this.field_73090_b.field_71135_a.func_147359_a(new SPacketChangeGameState(5, 0.0F));
        }

        this.field_73103_d = i > 120500L;
        if (this.field_73103_d) {
            ++this.field_73104_e;
        }

        if (i % 24000L == 500L) {
            if (j <= 6L) {
                this.field_73090_b.func_145747_a(new TextComponentTranslation("demo.day." + j, new Object[0]));
            }
        } else if (j == 1L) {
            if (i == 100L) {
                this.field_73090_b.field_71135_a.func_147359_a(new SPacketChangeGameState(5, 101.0F));
            } else if (i == 175L) {
                this.field_73090_b.field_71135_a.func_147359_a(new SPacketChangeGameState(5, 102.0F));
            } else if (i == 250L) {
                this.field_73090_b.field_71135_a.func_147359_a(new SPacketChangeGameState(5, 103.0F));
            }
        } else if (j == 5L && i % 24000L == 22000L) {
            this.field_73090_b.func_145747_a(new TextComponentTranslation("demo.day.warning", new Object[0]));
        }

    }

    private void func_73101_e() {
        if (this.field_73104_e > 100) {
            this.field_73090_b.func_145747_a(new TextComponentTranslation("demo.reminder", new Object[0]));
            this.field_73104_e = 0;
        }

    }

    public void func_180784_a(BlockPos blockposition, EnumFacing enumdirection) {
        if (this.field_73103_d) {
            this.func_73101_e();
        } else {
            super.func_180784_a(blockposition, enumdirection);
        }
    }

    public void func_180785_a(BlockPos blockposition) {
        if (!this.field_73103_d) {
            super.func_180785_a(blockposition);
        }
    }

    public boolean func_180237_b(BlockPos blockposition) {
        return this.field_73103_d ? false : super.func_180237_b(blockposition);
    }

    public EnumActionResult func_187250_a(EntityPlayer entityhuman, World world, ItemStack itemstack, EnumHand enumhand) {
        if (this.field_73103_d) {
            this.func_73101_e();
            return EnumActionResult.PASS;
        } else {
            return super.func_187250_a(entityhuman, world, itemstack, enumhand);
        }
    }

    public EnumActionResult func_187251_a(EntityPlayer entityhuman, World world, ItemStack itemstack, EnumHand enumhand, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2) {
        if (this.field_73103_d) {
            this.func_73101_e();
            return EnumActionResult.PASS;
        } else {
            return super.func_187251_a(entityhuman, world, itemstack, enumhand, blockposition, enumdirection, f, f1, f2);
        }
    }
}
