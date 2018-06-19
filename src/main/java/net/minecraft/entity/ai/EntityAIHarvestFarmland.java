package net.minecraft.entity.ai;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class EntityAIHarvestFarmland extends EntityAIMoveToBlock {

    private final EntityVillager field_179504_c;
    private boolean field_179502_d;
    private boolean field_179503_e;
    private int field_179501_f;

    public EntityAIHarvestFarmland(EntityVillager entityvillager, double d0) {
        super(entityvillager, d0, 16);
        this.field_179504_c = entityvillager;
    }

    public boolean func_75250_a() {
        if (this.field_179496_a <= 0) {
            if (!this.field_179504_c.field_70170_p.func_82736_K().func_82766_b("mobGriefing")) {
                return false;
            }

            this.field_179501_f = -1;
            this.field_179502_d = this.field_179504_c.func_175556_cs();
            this.field_179503_e = this.field_179504_c.func_175557_cr();
        }

        return super.func_75250_a();
    }

    public boolean func_75253_b() {
        return this.field_179501_f >= 0 && super.func_75253_b();
    }

    public void func_75246_d() {
        super.func_75246_d();
        this.field_179504_c.func_70671_ap().func_75650_a((double) this.field_179494_b.func_177958_n() + 0.5D, (double) (this.field_179494_b.func_177956_o() + 1), (double) this.field_179494_b.func_177952_p() + 0.5D, 10.0F, (float) this.field_179504_c.func_70646_bf());
        if (this.func_179487_f()) {
            World world = this.field_179504_c.field_70170_p;
            BlockPos blockposition = this.field_179494_b.func_177984_a();
            IBlockState iblockdata = world.func_180495_p(blockposition);
            Block block = iblockdata.func_177230_c();

            if (this.field_179501_f == 0 && block instanceof BlockCrops && ((BlockCrops) block).func_185525_y(iblockdata)) {
                // CraftBukkit start
                if (!org.bukkit.craftbukkit.event.CraftEventFactory.callEntityChangeBlockEvent(this.field_179504_c, blockposition, Blocks.field_150350_a, 0).isCancelled()) {
                    world.func_175655_b(blockposition, true);
                }
                // CraftBukkit end
            } else if (this.field_179501_f == 1 && iblockdata.func_185904_a() == Material.field_151579_a) {
                InventoryBasic inventorysubcontainer = this.field_179504_c.func_175551_co();

                for (int i = 0; i < inventorysubcontainer.func_70302_i_(); ++i) {
                    ItemStack itemstack = inventorysubcontainer.func_70301_a(i);
                    boolean flag = false;

                    if (!itemstack.func_190926_b()) {
                        // CraftBukkit start
                        Block planted = null;
                        if (itemstack.func_77973_b() == Items.field_151014_N) {
                            planted = Blocks.field_150464_aj;
                            flag = true;
                        } else if (itemstack.func_77973_b() == Items.field_151174_bG) {
                            planted = Blocks.field_150469_bN;
                            flag = true;
                        } else if (itemstack.func_77973_b() == Items.field_151172_bF) {
                            planted = Blocks.field_150459_bM;
                            flag = true;
                        } else if (itemstack.func_77973_b() == Items.field_185163_cU) {
                            planted = Blocks.field_185773_cZ;
                            flag = true;
                        }

                        if (planted != null && !org.bukkit.craftbukkit.event.CraftEventFactory.callEntityChangeBlockEvent(this.field_179504_c, blockposition, planted, 0).isCancelled()) {
                            world.func_180501_a(blockposition, planted.func_176223_P(), 3);
                        } else {
                            flag = false;
                        }
                        // CraftBukkit end
                    }

                    if (flag) {
                        itemstack.func_190918_g(1);
                        if (itemstack.func_190926_b()) {
                            inventorysubcontainer.func_70299_a(i, ItemStack.field_190927_a);
                        }
                        break;
                    }
                }
            }

            this.field_179501_f = -1;
            this.field_179496_a = 10;
        }

    }

    protected boolean func_179488_a(World world, BlockPos blockposition) {
        Block block = world.func_180495_p(blockposition).func_177230_c();

        if (block == Blocks.field_150458_ak) {
            blockposition = blockposition.func_177984_a();
            IBlockState iblockdata = world.func_180495_p(blockposition);

            block = iblockdata.func_177230_c();
            if (block instanceof BlockCrops && ((BlockCrops) block).func_185525_y(iblockdata) && this.field_179503_e && (this.field_179501_f == 0 || this.field_179501_f < 0)) {
                this.field_179501_f = 0;
                return true;
            }

            if (iblockdata.func_185904_a() == Material.field_151579_a && this.field_179502_d && (this.field_179501_f == 1 || this.field_179501_f < 0)) {
                this.field_179501_f = 1;
                return true;
            }
        }

        return false;
    }
}
