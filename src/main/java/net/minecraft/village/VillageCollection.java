package net.minecraft.village;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.WorldSavedData;

public class VillageCollection extends WorldSavedData {

    private World field_75556_a;
    private final List<BlockPos> field_75554_b = Lists.newArrayList();
    private final List<VillageDoorInfo> field_75555_c = Lists.newArrayList();
    private final List<Village> field_75552_d = Lists.newArrayList();
    private int field_75553_e;

    public VillageCollection(String s) {
        super(s);
    }

    public VillageCollection(World world) {
        super(func_176062_a(world.field_73011_w));
        this.field_75556_a = world;
        this.func_76185_a();
    }

    public void func_82566_a(World world) {
        this.field_75556_a = world;
        Iterator iterator = this.field_75552_d.iterator();

        while (iterator.hasNext()) {
            Village village = (Village) iterator.next();

            village.func_82691_a(world);
        }

    }

    public void func_176060_a(BlockPos blockposition) {
        if (this.field_75554_b.size() <= 64) {
            if (!this.func_176057_e(blockposition)) {
                this.field_75554_b.add(blockposition);
            }

        }
    }

    public void func_75544_a() {
        ++this.field_75553_e;
        Iterator iterator = this.field_75552_d.iterator();

        while (iterator.hasNext()) {
            Village village = (Village) iterator.next();

            village.func_75560_a(this.field_75553_e);
        }

        this.func_75549_c();
        this.func_75543_d();
        this.func_75545_e();
        if (this.field_75553_e % 400 == 0) {
            this.func_76185_a();
        }

    }

    private void func_75549_c() {
        Iterator iterator = this.field_75552_d.iterator();

        while (iterator.hasNext()) {
            Village village = (Village) iterator.next();

            if (village.func_75566_g()) {
                iterator.remove();
                this.func_76185_a();
            }
        }

    }

    public List<Village> func_75540_b() {
        return this.field_75552_d;
    }

    public Village func_176056_a(BlockPos blockposition, int i) {
        Village village = null;
        double d0 = 3.4028234663852886E38D;
        Iterator iterator = this.field_75552_d.iterator();

        while (iterator.hasNext()) {
            Village village1 = (Village) iterator.next();
            double d1 = village1.func_180608_a().func_177951_i(blockposition);

            if (d1 < d0) {
                float f = (float) (i + village1.func_75568_b());

                if (d1 <= (double) (f * f)) {
                    village = village1;
                    d0 = d1;
                }
            }
        }

        return village;
    }

    private void func_75543_d() {
        if (!this.field_75554_b.isEmpty()) {
            this.func_180609_b((BlockPos) this.field_75554_b.remove(0));
        }
    }

    private void func_75545_e() {
        for (int i = 0; i < this.field_75555_c.size(); ++i) {
            VillageDoorInfo villagedoor = (VillageDoorInfo) this.field_75555_c.get(i);
            Village village = this.func_176056_a(villagedoor.func_179852_d(), 32);

            if (village == null) {
                village = new Village(this.field_75556_a);
                this.field_75552_d.add(village);
                this.func_76185_a();
            }

            village.func_75576_a(villagedoor);
        }

        this.field_75555_c.clear();
    }

    private void func_180609_b(BlockPos blockposition) {
        boolean flag = true;
        boolean flag1 = true;
        boolean flag2 = true;

        for (int i = -16; i < 16; ++i) {
            for (int j = -4; j < 4; ++j) {
                for (int k = -16; k < 16; ++k) {
                    BlockPos blockposition1 = blockposition.func_177982_a(i, j, k);

                    if (this.func_176058_f(blockposition1)) {
                        VillageDoorInfo villagedoor = this.func_176055_c(blockposition1);

                        if (villagedoor == null) {
                            this.func_176059_d(blockposition1);
                        } else {
                            villagedoor.func_179849_a(this.field_75553_e);
                        }
                    }
                }
            }
        }

    }

    @Nullable
    private VillageDoorInfo func_176055_c(BlockPos blockposition) {
        Iterator iterator = this.field_75555_c.iterator();

        VillageDoorInfo villagedoor;

        do {
            if (!iterator.hasNext()) {
                iterator = this.field_75552_d.iterator();

                VillageDoorInfo villagedoor1;

                do {
                    if (!iterator.hasNext()) {
                        return null;
                    }

                    Village village = (Village) iterator.next();

                    villagedoor1 = village.func_179864_e(blockposition);
                } while (villagedoor1 == null);

                return villagedoor1;
            }

            villagedoor = (VillageDoorInfo) iterator.next();
        } while (villagedoor.func_179852_d().func_177958_n() != blockposition.func_177958_n() || villagedoor.func_179852_d().func_177952_p() != blockposition.func_177952_p() || Math.abs(villagedoor.func_179852_d().func_177956_o() - blockposition.func_177956_o()) > 1);

        return villagedoor;
    }

    private void func_176059_d(BlockPos blockposition) {
        EnumFacing enumdirection = BlockDoor.func_176517_h(this.field_75556_a, blockposition);
        EnumFacing enumdirection1 = enumdirection.func_176734_d();
        int i = this.func_176061_a(blockposition, enumdirection, 5);
        int j = this.func_176061_a(blockposition, enumdirection1, i + 1);

        if (i != j) {
            this.field_75555_c.add(new VillageDoorInfo(blockposition, i < j ? enumdirection : enumdirection1, this.field_75553_e));
        }

    }

    private int func_176061_a(BlockPos blockposition, EnumFacing enumdirection, int i) {
        int j = 0;

        for (int k = 1; k <= 5; ++k) {
            if (this.field_75556_a.func_175678_i(blockposition.func_177967_a(enumdirection, k))) {
                ++j;
                if (j >= i) {
                    return j;
                }
            }
        }

        return j;
    }

    private boolean func_176057_e(BlockPos blockposition) {
        Iterator iterator = this.field_75554_b.iterator();

        BlockPos blockposition1;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            blockposition1 = (BlockPos) iterator.next();
        } while (!blockposition1.equals(blockposition));

        return true;
    }

    private boolean func_176058_f(BlockPos blockposition) {
        // Paper start
        IBlockState iblockdata = this.field_75556_a.getTypeIfLoaded(blockposition);
        if (iblockdata == null) {
            return false;
        }
        // Paper end
        Block block = iblockdata.func_177230_c();

        return block instanceof BlockDoor ? iblockdata.func_185904_a() == Material.field_151575_d : false;
    }

    public void func_76184_a(NBTTagCompound nbttagcompound) {
        this.field_75553_e = nbttagcompound.func_74762_e("Tick");
        NBTTagList nbttaglist = nbttagcompound.func_150295_c("Villages", 10);

        for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
            Village village = new Village(field_75556_a); // Paper

            village.func_82690_a(nbttagcompound1);
            this.field_75552_d.add(village);
        }

    }

    public NBTTagCompound func_189551_b(NBTTagCompound nbttagcompound) {
        nbttagcompound.func_74768_a("Tick", this.field_75553_e);
        NBTTagList nbttaglist = new NBTTagList();
        Iterator iterator = this.field_75552_d.iterator();

        while (iterator.hasNext()) {
            Village village = (Village) iterator.next();
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            village.func_82689_b(nbttagcompound1);
            nbttaglist.func_74742_a(nbttagcompound1);
        }

        nbttagcompound.func_74782_a("Villages", nbttaglist);
        return nbttagcompound;
    }

    public static String func_176062_a(WorldProvider worldprovider) {
        return "villages" + worldprovider.func_186058_p().func_186067_c();
    }
}
