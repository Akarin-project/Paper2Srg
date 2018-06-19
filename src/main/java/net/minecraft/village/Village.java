package net.minecraft.village;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class Village {

    private World field_75586_a;
    private final List<VillageDoorInfo> field_75584_b = Lists.newArrayList();
    private BlockPos field_75585_c;
    private BlockPos field_75582_d;
    private int field_75583_e;
    private int field_75580_f;
    private int field_75581_g;
    private int field_75588_h;
    private int field_82694_i;
    private final Map<String, Integer> field_82693_j;
    private final List<Village.VillageAggressor> field_75589_i;
    private int field_75587_j;

    private Village() { // Paper - Nothing should call this - world needs to be set.
        this.field_75585_c = BlockPos.field_177992_a;
        this.field_75582_d = BlockPos.field_177992_a;
        this.field_82693_j = Maps.newHashMap();
        this.field_75589_i = Lists.newArrayList();
    }

    public Village(World world) {
        this.field_75585_c = BlockPos.field_177992_a;
        this.field_75582_d = BlockPos.field_177992_a;
        this.field_82693_j = Maps.newHashMap();
        this.field_75589_i = Lists.newArrayList();
        this.field_75586_a = world;
    }

    public void func_82691_a(World world) {
        this.field_75586_a = world;
    }

    public void func_75560_a(int i) {
        this.field_75581_g = i;
        this.func_75557_k();
        this.func_75565_j();
        if (i % 20 == 0) {
            this.func_75572_i();
        }

        if (i % 30 == 0) {
            this.func_75579_h();
        }

        int j = this.field_75588_h / 10;

        if (this.field_75587_j < j && this.field_75584_b.size() > 20 && this.field_75586_a.field_73012_v.nextInt(7000) == 0) {
            Vec3d vec3d = this.func_179862_a(this.field_75582_d, 2, 4, 2);

            if (vec3d != null) {
                EntityIronGolem entityirongolem = new EntityIronGolem(this.field_75586_a);

                entityirongolem.func_70107_b(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c);
                this.field_75586_a.addEntity(entityirongolem, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.VILLAGE_DEFENSE); // CraftBukkit
                ++this.field_75587_j;
            }
        }

    }

    private Vec3d func_179862_a(BlockPos blockposition, int i, int j, int k) {
        for (int l = 0; l < 10; ++l) {
            BlockPos blockposition1 = blockposition.func_177982_a(this.field_75586_a.field_73012_v.nextInt(16) - 8, this.field_75586_a.field_73012_v.nextInt(6) - 3, this.field_75586_a.field_73012_v.nextInt(16) - 8);

            if (this.func_179866_a(blockposition1) && this.func_179861_a(new BlockPos(i, j, k), blockposition1)) {
                return new Vec3d((double) blockposition1.func_177958_n(), (double) blockposition1.func_177956_o(), (double) blockposition1.func_177952_p());
            }
        }

        return null;
    }

    private boolean func_179861_a(BlockPos blockposition, BlockPos blockposition1) {
        if (!this.field_75586_a.func_180495_p(blockposition1.func_177977_b()).func_185896_q()) {
            return false;
        } else {
            int i = blockposition1.func_177958_n() - blockposition.func_177958_n() / 2;
            int j = blockposition1.func_177952_p() - blockposition.func_177952_p() / 2;

            for (int k = i; k < i + blockposition.func_177958_n(); ++k) {
                for (int l = blockposition1.func_177956_o(); l < blockposition1.func_177956_o() + blockposition.func_177956_o(); ++l) {
                    for (int i1 = j; i1 < j + blockposition.func_177952_p(); ++i1) {
                        if (this.field_75586_a.func_180495_p(new BlockPos(k, l, i1)).func_185915_l()) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    private void func_75579_h() {
        List list = this.field_75586_a.func_72872_a(EntityIronGolem.class, new AxisAlignedBB((double) (this.field_75582_d.func_177958_n() - this.field_75583_e), (double) (this.field_75582_d.func_177956_o() - 4), (double) (this.field_75582_d.func_177952_p() - this.field_75583_e), (double) (this.field_75582_d.func_177958_n() + this.field_75583_e), (double) (this.field_75582_d.func_177956_o() + 4), (double) (this.field_75582_d.func_177952_p() + this.field_75583_e)));

        this.field_75587_j = list.size();
    }

    private void func_75572_i() {
        List list = this.field_75586_a.func_72872_a(EntityVillager.class, new AxisAlignedBB((double) (this.field_75582_d.func_177958_n() - this.field_75583_e), (double) (this.field_75582_d.func_177956_o() - 4), (double) (this.field_75582_d.func_177952_p() - this.field_75583_e), (double) (this.field_75582_d.func_177958_n() + this.field_75583_e), (double) (this.field_75582_d.func_177956_o() + 4), (double) (this.field_75582_d.func_177952_p() + this.field_75583_e)));

        this.field_75588_h = list.size();
        if (this.field_75588_h == 0) {
            this.field_82693_j.clear();
        }

    }

    public BlockPos func_180608_a() {
        return this.field_75582_d;
    }

    public int func_75568_b() {
        return this.field_75583_e;
    }

    public int func_75567_c() {
        return this.field_75584_b.size();
    }

    public int func_75561_d() {
        return this.field_75581_g - this.field_75580_f;
    }

    public int func_75562_e() {
        return this.field_75588_h;
    }

    public boolean func_179866_a(BlockPos blockposition) {
        return this.field_75582_d.func_177951_i(blockposition) < (double) (this.field_75583_e * this.field_75583_e);
    }

    public List<VillageDoorInfo> func_75558_f() {
        return this.field_75584_b;
    }

    public VillageDoorInfo func_179865_b(BlockPos blockposition) {
        VillageDoorInfo villagedoor = null;
        int i = Integer.MAX_VALUE;
        Iterator iterator = this.field_75584_b.iterator();

        while (iterator.hasNext()) {
            VillageDoorInfo villagedoor1 = (VillageDoorInfo) iterator.next();
            int j = villagedoor1.func_179848_a(blockposition);

            if (j < i) {
                villagedoor = villagedoor1;
                i = j;
            }
        }

        return villagedoor;
    }

    public VillageDoorInfo func_179863_c(BlockPos blockposition) {
        VillageDoorInfo villagedoor = null;
        int i = Integer.MAX_VALUE;
        Iterator iterator = this.field_75584_b.iterator();

        while (iterator.hasNext()) {
            VillageDoorInfo villagedoor1 = (VillageDoorInfo) iterator.next();
            int j = villagedoor1.func_179848_a(blockposition);

            if (j > 256) {
                j *= 1000;
            } else {
                j = villagedoor1.func_75468_f();
            }

            if (j < i) {
                BlockPos blockposition1 = villagedoor1.func_179852_d();
                EnumFacing enumdirection = villagedoor1.func_188567_j();

                if (this.field_75586_a.func_180495_p(blockposition1.func_177967_a(enumdirection, 1)).func_177230_c().func_176205_b(this.field_75586_a, blockposition1.func_177967_a(enumdirection, 1)) && this.field_75586_a.func_180495_p(blockposition1.func_177967_a(enumdirection, -1)).func_177230_c().func_176205_b(this.field_75586_a, blockposition1.func_177967_a(enumdirection, -1)) && this.field_75586_a.func_180495_p(blockposition1.func_177984_a().func_177967_a(enumdirection, 1)).func_177230_c().func_176205_b(this.field_75586_a, blockposition1.func_177984_a().func_177967_a(enumdirection, 1)) && this.field_75586_a.func_180495_p(blockposition1.func_177984_a().func_177967_a(enumdirection, -1)).func_177230_c().func_176205_b(this.field_75586_a, blockposition1.func_177984_a().func_177967_a(enumdirection, -1))) {
                    villagedoor = villagedoor1;
                    i = j;
                }
            }
        }

        return villagedoor;
    }

    @Nullable
    public VillageDoorInfo func_179864_e(BlockPos blockposition) {
        if (this.field_75582_d.func_177951_i(blockposition) > (double) (this.field_75583_e * this.field_75583_e)) {
            return null;
        } else {
            Iterator iterator = this.field_75584_b.iterator();

            VillageDoorInfo villagedoor;

            do {
                if (!iterator.hasNext()) {
                    return null;
                }

                villagedoor = (VillageDoorInfo) iterator.next();
            } while (villagedoor.func_179852_d().func_177958_n() != blockposition.func_177958_n() || villagedoor.func_179852_d().func_177952_p() != blockposition.func_177952_p() || Math.abs(villagedoor.func_179852_d().func_177956_o() - blockposition.func_177956_o()) > 1);

            return villagedoor;
        }
    }

    public void func_75576_a(VillageDoorInfo villagedoor) {
        this.field_75584_b.add(villagedoor);
        this.field_75585_c = this.field_75585_c.func_177971_a((Vec3i) villagedoor.func_179852_d());
        this.func_75573_l();
        this.field_75580_f = villagedoor.func_75473_b();
    }

    public boolean func_75566_g() {
        return this.field_75584_b.isEmpty();
    }

    public void func_75575_a(EntityLivingBase entityliving) {
        Iterator iterator = this.field_75589_i.iterator();

        Village.VillageAggressor village_aggressor;

        do {
            if (!iterator.hasNext()) {
                this.field_75589_i.add(new Village.VillageAggressor(entityliving, this.field_75581_g));
                return;
            }

            village_aggressor = (Village.VillageAggressor) iterator.next();
        } while (village_aggressor.field_75592_a != entityliving);

        village_aggressor.field_75590_b = this.field_75581_g;
    }

    @Nullable
    public EntityLivingBase func_75571_b(EntityLivingBase entityliving) {
        double d0 = Double.MAX_VALUE;
        Village.VillageAggressor village_aggressor = null;

        for (int i = 0; i < this.field_75589_i.size(); ++i) {
            Village.VillageAggressor village_aggressor1 = (Village.VillageAggressor) this.field_75589_i.get(i);
            double d1 = village_aggressor1.field_75592_a.func_70068_e(entityliving);

            if (d1 <= d0) {
                village_aggressor = village_aggressor1;
                d0 = d1;
            }
        }

        return village_aggressor == null ? null : village_aggressor.field_75592_a;
    }

    public EntityPlayer func_82685_c(EntityLivingBase entityliving) {
        double d0 = Double.MAX_VALUE;
        EntityPlayer entityhuman = null;
        Iterator iterator = this.field_82693_j.keySet().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            if (this.func_82687_d(s)) {
                EntityPlayer entityhuman1 = this.field_75586_a.func_72924_a(s);

                if (entityhuman1 != null) {
                    double d1 = entityhuman1.func_70068_e(entityliving);

                    if (d1 <= d0) {
                        entityhuman = entityhuman1;
                        d0 = d1;
                    }
                }
            }
        }

        return entityhuman;
    }

    private void func_75565_j() {
        Iterator iterator = this.field_75589_i.iterator();

        while (iterator.hasNext()) {
            Village.VillageAggressor village_aggressor = (Village.VillageAggressor) iterator.next();

            if (!village_aggressor.field_75592_a.func_70089_S() || Math.abs(this.field_75581_g - village_aggressor.field_75590_b) > 300) {
                iterator.remove();
            }
        }

    }

    private void func_75557_k() {
        boolean flag = false;
        boolean flag1 = this.field_75586_a.field_73012_v.nextInt(50) == 0;
        Iterator iterator = this.field_75584_b.iterator();

        while (iterator.hasNext()) {
            VillageDoorInfo villagedoor = (VillageDoorInfo) iterator.next();

            if (flag1) {
                villagedoor.func_75466_d();
            }

            if (!this.func_179860_f(villagedoor.func_179852_d()) || Math.abs(this.field_75581_g - villagedoor.func_75473_b()) > 1200) {
                this.field_75585_c = this.field_75585_c.func_177973_b(villagedoor.func_179852_d());
                flag = true;
                villagedoor.func_179853_a(true);
                iterator.remove();
            }
        }

        if (flag) {
            this.func_75573_l();
        }

    }

    private boolean func_179860_f(BlockPos blockposition) {
        IBlockState iblockdata = this.field_75586_a.func_180495_p(blockposition);
        Block block = iblockdata.func_177230_c();

        return block instanceof BlockDoor ? iblockdata.func_185904_a() == Material.field_151575_d : false;
    }

    private void func_75573_l() {
        int i = this.field_75584_b.size();

        if (i == 0) {
            this.field_75582_d = BlockPos.field_177992_a;
            this.field_75583_e = 0;
        } else {
            this.field_75582_d = new BlockPos(this.field_75585_c.func_177958_n() / i, this.field_75585_c.func_177956_o() / i, this.field_75585_c.func_177952_p() / i);
            int j = 0;

            VillageDoorInfo villagedoor;

            for (Iterator iterator = this.field_75584_b.iterator(); iterator.hasNext(); j = Math.max(villagedoor.func_179848_a(this.field_75582_d), j)) {
                villagedoor = (VillageDoorInfo) iterator.next();
            }

            this.field_75583_e = Math.max(32, (int) Math.sqrt((double) j) + 1);
        }
    }

    public int func_82684_a(String s) {
        Integer integer = (Integer) this.field_82693_j.get(s);

        return integer == null ? 0 : integer.intValue();
    }

    public int func_82688_a(String s, int i) {
        int j = this.func_82684_a(s);
        int k = MathHelper.func_76125_a(j + i, -30, 10);

        this.field_82693_j.put(s, Integer.valueOf(k));
        return k;
    }

    public boolean func_82687_d(String s) {
        return this.func_82684_a(s) <= -15;
    }

    public void func_82690_a(NBTTagCompound nbttagcompound) {
        this.field_75588_h = nbttagcompound.func_74762_e("PopSize");
        this.field_75583_e = nbttagcompound.func_74762_e("Radius");
        this.field_75587_j = nbttagcompound.func_74762_e("Golems");
        this.field_75580_f = nbttagcompound.func_74762_e("Stable");
        this.field_75581_g = nbttagcompound.func_74762_e("Tick");
        this.field_82694_i = nbttagcompound.func_74762_e("MTick");
        this.field_75582_d = new BlockPos(nbttagcompound.func_74762_e("CX"), nbttagcompound.func_74762_e("CY"), nbttagcompound.func_74762_e("CZ"));
        this.field_75585_c = new BlockPos(nbttagcompound.func_74762_e("ACX"), nbttagcompound.func_74762_e("ACY"), nbttagcompound.func_74762_e("ACZ"));
        NBTTagList nbttaglist = nbttagcompound.func_150295_c("Doors", 10);

        for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
            VillageDoorInfo villagedoor = new VillageDoorInfo(new BlockPos(nbttagcompound1.func_74762_e("X"), nbttagcompound1.func_74762_e("Y"), nbttagcompound1.func_74762_e("Z")), nbttagcompound1.func_74762_e("IDX"), nbttagcompound1.func_74762_e("IDZ"), nbttagcompound1.func_74762_e("TS"));

            this.field_75584_b.add(villagedoor);
        }

        NBTTagList nbttaglist1 = nbttagcompound.func_150295_c("Players", 10);

        for (int j = 0; j < nbttaglist1.func_74745_c(); ++j) {
            NBTTagCompound nbttagcompound2 = nbttaglist1.func_150305_b(j);

            if (nbttagcompound2.func_74764_b("UUID") && this.field_75586_a != null && this.field_75586_a.func_73046_m() != null) {
                PlayerProfileCache usercache = this.field_75586_a.func_73046_m().func_152358_ax();
                GameProfile gameprofile = usercache.func_152652_a(UUID.fromString(nbttagcompound2.func_74779_i("UUID")));

                if (gameprofile != null) {
                    this.field_82693_j.put(gameprofile.getName(), Integer.valueOf(nbttagcompound2.func_74762_e("S")));
                }
            } else {
                this.field_82693_j.put(nbttagcompound2.func_74779_i("Name"), Integer.valueOf(nbttagcompound2.func_74762_e("S")));
            }
        }

    }

    public void func_82689_b(NBTTagCompound nbttagcompound) {
        nbttagcompound.func_74768_a("PopSize", this.field_75588_h);
        nbttagcompound.func_74768_a("Radius", this.field_75583_e);
        nbttagcompound.func_74768_a("Golems", this.field_75587_j);
        nbttagcompound.func_74768_a("Stable", this.field_75580_f);
        nbttagcompound.func_74768_a("Tick", this.field_75581_g);
        nbttagcompound.func_74768_a("MTick", this.field_82694_i);
        nbttagcompound.func_74768_a("CX", this.field_75582_d.func_177958_n());
        nbttagcompound.func_74768_a("CY", this.field_75582_d.func_177956_o());
        nbttagcompound.func_74768_a("CZ", this.field_75582_d.func_177952_p());
        nbttagcompound.func_74768_a("ACX", this.field_75585_c.func_177958_n());
        nbttagcompound.func_74768_a("ACY", this.field_75585_c.func_177956_o());
        nbttagcompound.func_74768_a("ACZ", this.field_75585_c.func_177952_p());
        NBTTagList nbttaglist = new NBTTagList();
        Iterator iterator = this.field_75584_b.iterator();

        while (iterator.hasNext()) {
            VillageDoorInfo villagedoor = (VillageDoorInfo) iterator.next();
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            nbttagcompound1.func_74768_a("X", villagedoor.func_179852_d().func_177958_n());
            nbttagcompound1.func_74768_a("Y", villagedoor.func_179852_d().func_177956_o());
            nbttagcompound1.func_74768_a("Z", villagedoor.func_179852_d().func_177952_p());
            nbttagcompound1.func_74768_a("IDX", villagedoor.func_179847_f());
            nbttagcompound1.func_74768_a("IDZ", villagedoor.func_179855_g());
            nbttagcompound1.func_74768_a("TS", villagedoor.func_75473_b());
            nbttaglist.func_74742_a(nbttagcompound1);
        }

        nbttagcompound.func_74782_a("Doors", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();
        Iterator iterator1 = this.field_82693_j.keySet().iterator();

        while (iterator1.hasNext()) {
            String s = (String) iterator1.next();
            NBTTagCompound nbttagcompound2 = new NBTTagCompound();
            PlayerProfileCache usercache = this.field_75586_a.func_73046_m().func_152358_ax();

            try {
                GameProfile gameprofile = usercache.func_152655_a(s);

                if (gameprofile != null) {
                    nbttagcompound2.func_74778_a("UUID", gameprofile.getId().toString());
                    nbttagcompound2.func_74768_a("S", ((Integer) this.field_82693_j.get(s)).intValue());
                    nbttaglist1.func_74742_a(nbttagcompound2);
                }
            } catch (RuntimeException runtimeexception) {
                ;
            }
        }

        nbttagcompound.func_74782_a("Players", nbttaglist1);
    }

    public void func_82692_h() {
        this.field_82694_i = this.field_75581_g;
    }

    public boolean func_82686_i() {
        return this.field_82694_i == 0 || this.field_75581_g - this.field_82694_i >= 3600;
    }

    public void func_82683_b(int i) {
        Iterator iterator = this.field_82693_j.keySet().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            this.func_82688_a(s, i);
        }

    }

    class VillageAggressor {

        public EntityLivingBase field_75592_a;
        public int field_75590_b;

        VillageAggressor(EntityLivingBase entityliving, int i) {
            this.field_75592_a = entityliving;
            this.field_75590_b = i;
        }
    }
}
