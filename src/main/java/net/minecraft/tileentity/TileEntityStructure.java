package net.minecraft.tileentity;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStructure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class TileEntityStructure extends TileEntity {

    private String field_184420_a = ""; // PAIL: rename name
    public String field_184421_f = ""; // PAIL: private -> public
    public String field_184422_g = ""; // PAIL: private -> public
    public BlockPos field_184423_h = new BlockPos(0, 1, 0); // PAIL: private -> public
    public BlockPos field_184424_i; // PAIL: private -> public
    public Mirror field_184425_j; // PAIL: private -> public
    public Rotation field_184426_k; // PAIL: private -> public
    private TileEntityStructure.Mode field_184427_l; // PAIL: rename
    public boolean field_184428_m; // PAIL: private -> public
    private boolean field_189727_n;
    public boolean field_189728_o; // PAIL: private -> public
    public boolean field_189729_p; // PAIL: private -> public
    public float field_189730_q; // PAIL: private -> public
    public long field_189731_r; // PAIL: private -> public

    public TileEntityStructure() {
        this.field_184424_i = BlockPos.field_177992_a;
        this.field_184425_j = Mirror.NONE;
        this.field_184426_k = Rotation.NONE;
        this.field_184427_l = TileEntityStructure.Mode.DATA;
        this.field_184428_m = true;
        this.field_189729_p = true;
        this.field_189730_q = 1.0F;
    }

    public NBTTagCompound func_189515_b(NBTTagCompound nbttagcompound) {
        super.func_189515_b(nbttagcompound);
        nbttagcompound.func_74778_a("name", this.field_184420_a);
        nbttagcompound.func_74778_a("author", this.field_184421_f);
        nbttagcompound.func_74778_a("metadata", this.field_184422_g);
        nbttagcompound.func_74768_a("posX", this.field_184423_h.func_177958_n());
        nbttagcompound.func_74768_a("posY", this.field_184423_h.func_177956_o());
        nbttagcompound.func_74768_a("posZ", this.field_184423_h.func_177952_p());
        nbttagcompound.func_74768_a("sizeX", this.field_184424_i.func_177958_n());
        nbttagcompound.func_74768_a("sizeY", this.field_184424_i.func_177956_o());
        nbttagcompound.func_74768_a("sizeZ", this.field_184424_i.func_177952_p());
        nbttagcompound.func_74778_a("rotation", this.field_184426_k.toString());
        nbttagcompound.func_74778_a("mirror", this.field_184425_j.toString());
        nbttagcompound.func_74778_a("mode", this.field_184427_l.toString());
        nbttagcompound.func_74757_a("ignoreEntities", this.field_184428_m);
        nbttagcompound.func_74757_a("powered", this.field_189727_n);
        nbttagcompound.func_74757_a("showair", this.field_189728_o);
        nbttagcompound.func_74757_a("showboundingbox", this.field_189729_p);
        nbttagcompound.func_74776_a("integrity", this.field_189730_q);
        nbttagcompound.func_74772_a("seed", this.field_189731_r);
        return nbttagcompound;
    }

    public void func_145839_a(NBTTagCompound nbttagcompound) {
        super.func_145839_a(nbttagcompound);
        this.func_184404_a(nbttagcompound.func_74779_i("name"));
        this.field_184421_f = nbttagcompound.func_74779_i("author");
        this.field_184422_g = nbttagcompound.func_74779_i("metadata");
        int i = MathHelper.func_76125_a(nbttagcompound.func_74762_e("posX"), -32, 32);
        int j = MathHelper.func_76125_a(nbttagcompound.func_74762_e("posY"), -32, 32);
        int k = MathHelper.func_76125_a(nbttagcompound.func_74762_e("posZ"), -32, 32);

        this.field_184423_h = new BlockPos(i, j, k);
        int l = MathHelper.func_76125_a(nbttagcompound.func_74762_e("sizeX"), 0, 32);
        int i1 = MathHelper.func_76125_a(nbttagcompound.func_74762_e("sizeY"), 0, 32);
        int j1 = MathHelper.func_76125_a(nbttagcompound.func_74762_e("sizeZ"), 0, 32);

        this.field_184424_i = new BlockPos(l, i1, j1);

        try {
            this.field_184426_k = Rotation.valueOf(nbttagcompound.func_74779_i("rotation"));
        } catch (IllegalArgumentException illegalargumentexception) {
            this.field_184426_k = Rotation.NONE;
        }

        try {
            this.field_184425_j = Mirror.valueOf(nbttagcompound.func_74779_i("mirror"));
        } catch (IllegalArgumentException illegalargumentexception1) {
            this.field_184425_j = Mirror.NONE;
        }

        try {
            this.field_184427_l = TileEntityStructure.Mode.valueOf(nbttagcompound.func_74779_i("mode"));
        } catch (IllegalArgumentException illegalargumentexception2) {
            this.field_184427_l = TileEntityStructure.Mode.DATA;
        }

        this.field_184428_m = nbttagcompound.func_74767_n("ignoreEntities");
        this.field_189727_n = nbttagcompound.func_74767_n("powered");
        this.field_189728_o = nbttagcompound.func_74767_n("showair");
        this.field_189729_p = nbttagcompound.func_74767_n("showboundingbox");
        if (nbttagcompound.func_74764_b("integrity")) {
            this.field_189730_q = nbttagcompound.func_74760_g("integrity");
        } else {
            this.field_189730_q = 1.0F;
        }

        this.field_189731_r = nbttagcompound.func_74763_f("seed");
        this.func_189704_J();
    }

    private void func_189704_J() {
        if (this.field_145850_b != null) {
            BlockPos blockposition = this.func_174877_v();
            IBlockState iblockdata = this.field_145850_b.func_180495_p(blockposition);

            if (iblockdata.func_177230_c() == Blocks.field_185779_df) {
                this.field_145850_b.func_180501_a(blockposition, iblockdata.func_177226_a(BlockStructure.field_185587_a, this.field_184427_l), 2);
            }

        }
    }

    @Nullable
    public SPacketUpdateTileEntity func_189518_D_() {
        return new SPacketUpdateTileEntity(this.field_174879_c, 7, this.func_189517_E_());
    }

    public NBTTagCompound func_189517_E_() {
        return this.func_189515_b(new NBTTagCompound());
    }

    public boolean func_189701_a(EntityPlayer entityhuman) {
        if (!entityhuman.func_189808_dh()) {
            return false;
        } else {
            if (entityhuman.func_130014_f_().field_72995_K) {
                entityhuman.func_189807_a(this);
            }

            return true;
        }
    }

    public String func_189715_d() {
        return this.field_184420_a;
    }

    public void func_184404_a(String s) {
        String s1 = s;
        char[] achar = ChatAllowedCharacters.field_189861_b;
        int i = achar.length;

        for (int j = 0; j < i; ++j) {
            char c0 = achar[j];

            s1 = s1.replace(c0, '_');
        }

        this.field_184420_a = s1;
    }

    public void func_189720_a(EntityLivingBase entityliving) {
        if (!StringUtils.func_151246_b(entityliving.func_70005_c_())) {
            this.field_184421_f = entityliving.func_70005_c_();
        }

    }

    public void func_184414_b(BlockPos blockposition) {
        this.field_184423_h = blockposition;
    }

    public void func_184409_c(BlockPos blockposition) {
        this.field_184424_i = blockposition;
    }

    public void func_184411_a(Mirror enumblockmirror) {
        this.field_184425_j = enumblockmirror;
    }

    public void func_184408_a(Rotation enumblockrotation) {
        this.field_184426_k = enumblockrotation;
    }

    public void func_184410_b(String s) {
        this.field_184422_g = s;
    }

    public TileEntityStructure.Mode func_189700_k() {
        return this.field_184427_l;
    }

    public void func_184405_a(TileEntityStructure.Mode tileentitystructure_usagemode) {
        this.field_184427_l = tileentitystructure_usagemode;
        IBlockState iblockdata = this.field_145850_b.func_180495_p(this.func_174877_v());

        if (iblockdata.func_177230_c() == Blocks.field_185779_df) {
            this.field_145850_b.func_180501_a(this.func_174877_v(), iblockdata.func_177226_a(BlockStructure.field_185587_a, tileentitystructure_usagemode), 2);
        }

    }

    public void func_184406_a(boolean flag) {
        this.field_184428_m = flag;
    }

    public void func_189718_a(float f) {
        this.field_189730_q = f;
    }

    public void func_189725_a(long i) {
        this.field_189731_r = i;
    }

    public boolean func_184417_l() {
        if (this.field_184427_l != TileEntityStructure.Mode.SAVE) {
            return false;
        } else {
            BlockPos blockposition = this.func_174877_v();
            boolean flag = true;
            BlockPos blockposition1 = new BlockPos(blockposition.func_177958_n() - 80, 0, blockposition.func_177952_p() - 80);
            BlockPos blockposition2 = new BlockPos(blockposition.func_177958_n() + 80, 255, blockposition.func_177952_p() + 80);
            List list = this.func_184418_a(blockposition1, blockposition2);
            List list1 = this.func_184415_a(list);

            if (list1.size() < 1) {
                return false;
            } else {
                StructureBoundingBox structureboundingbox = this.func_184416_a(blockposition, list1);

                if (structureboundingbox.field_78893_d - structureboundingbox.field_78897_a > 1 && structureboundingbox.field_78894_e - structureboundingbox.field_78895_b > 1 && structureboundingbox.field_78892_f - structureboundingbox.field_78896_c > 1) {
                    this.field_184423_h = new BlockPos(structureboundingbox.field_78897_a - blockposition.func_177958_n() + 1, structureboundingbox.field_78895_b - blockposition.func_177956_o() + 1, structureboundingbox.field_78896_c - blockposition.func_177952_p() + 1);
                    this.field_184424_i = new BlockPos(structureboundingbox.field_78893_d - structureboundingbox.field_78897_a - 1, structureboundingbox.field_78894_e - structureboundingbox.field_78895_b - 1, structureboundingbox.field_78892_f - structureboundingbox.field_78896_c - 1);
                    this.func_70296_d();
                    IBlockState iblockdata = this.field_145850_b.func_180495_p(blockposition);

                    this.field_145850_b.func_184138_a(blockposition, iblockdata, iblockdata, 3);
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    private List<TileEntityStructure> func_184415_a(List<TileEntityStructure> list) {
        Iterable iterable = Iterables.filter(list, new Predicate() {
            public boolean a(@Nullable TileEntityStructure tileentitystructure) {
                return tileentitystructure.field_184427_l == TileEntityStructure.Mode.CORNER && TileEntityStructure.this.field_184420_a.equals(tileentitystructure.field_184420_a);
            }

            public boolean apply(@Nullable Object object) {
                return this.a((TileEntityStructure) object);
            }
        });

        return Lists.newArrayList(iterable);
    }

    private List<TileEntityStructure> func_184418_a(BlockPos blockposition, BlockPos blockposition1) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = BlockPos.func_177975_b(blockposition, blockposition1).iterator();

        while (iterator.hasNext()) {
            BlockPos.MutableBlockPos blockposition_mutableblockposition = (BlockPos.MutableBlockPos) iterator.next();
            IBlockState iblockdata = this.field_145850_b.func_180495_p(blockposition_mutableblockposition);

            if (iblockdata.func_177230_c() == Blocks.field_185779_df) {
                TileEntity tileentity = this.field_145850_b.func_175625_s(blockposition_mutableblockposition);

                if (tileentity != null && tileentity instanceof TileEntityStructure) {
                    arraylist.add((TileEntityStructure) tileentity);
                }
            }
        }

        return arraylist;
    }

    private StructureBoundingBox func_184416_a(BlockPos blockposition, List<TileEntityStructure> list) {
        StructureBoundingBox structureboundingbox;

        if (list.size() > 1) {
            BlockPos blockposition1 = ((TileEntityStructure) list.get(0)).func_174877_v();

            structureboundingbox = new StructureBoundingBox(blockposition1, blockposition1);
        } else {
            structureboundingbox = new StructureBoundingBox(blockposition, blockposition);
        }

        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            TileEntityStructure tileentitystructure = (TileEntityStructure) iterator.next();
            BlockPos blockposition2 = tileentitystructure.func_174877_v();

            if (blockposition2.func_177958_n() < structureboundingbox.field_78897_a) {
                structureboundingbox.field_78897_a = blockposition2.func_177958_n();
            } else if (blockposition2.func_177958_n() > structureboundingbox.field_78893_d) {
                structureboundingbox.field_78893_d = blockposition2.func_177958_n();
            }

            if (blockposition2.func_177956_o() < structureboundingbox.field_78895_b) {
                structureboundingbox.field_78895_b = blockposition2.func_177956_o();
            } else if (blockposition2.func_177956_o() > structureboundingbox.field_78894_e) {
                structureboundingbox.field_78894_e = blockposition2.func_177956_o();
            }

            if (blockposition2.func_177952_p() < structureboundingbox.field_78896_c) {
                structureboundingbox.field_78896_c = blockposition2.func_177952_p();
            } else if (blockposition2.func_177952_p() > structureboundingbox.field_78892_f) {
                structureboundingbox.field_78892_f = blockposition2.func_177952_p();
            }
        }

        return structureboundingbox;
    }

    public boolean func_184419_m() {
        return this.func_189712_b(true);
    }

    public boolean func_189712_b(boolean flag) {
        if (this.field_184427_l == TileEntityStructure.Mode.SAVE && !this.field_145850_b.field_72995_K && !StringUtils.func_151246_b(this.field_184420_a)) {
            BlockPos blockposition = this.func_174877_v().func_177971_a((Vec3i) this.field_184423_h);
            WorldServer worldserver = (WorldServer) this.field_145850_b;
            MinecraftServer minecraftserver = this.field_145850_b.func_73046_m();
            TemplateManager definedstructuremanager = worldserver.func_184163_y();
            Template definedstructure = definedstructuremanager.func_186237_a(minecraftserver, new ResourceLocation(this.field_184420_a));

            definedstructure.func_186254_a(this.field_145850_b, blockposition, this.field_184424_i, !this.field_184428_m, Blocks.field_189881_dj);
            definedstructure.func_186252_a(this.field_184421_f);
            return !flag || definedstructuremanager.func_186238_c(minecraftserver, new ResourceLocation(this.field_184420_a));
        } else {
            return false;
        }
    }

    public boolean func_184412_n() {
        return this.func_189714_c(true);
    }

    public boolean func_189714_c(boolean flag) {
        if (this.field_184427_l == TileEntityStructure.Mode.LOAD && !this.field_145850_b.field_72995_K && !StringUtils.func_151246_b(this.field_184420_a)) {
            BlockPos blockposition = this.func_174877_v();
            BlockPos blockposition1 = blockposition.func_177971_a((Vec3i) this.field_184423_h);
            WorldServer worldserver = (WorldServer) this.field_145850_b;
            MinecraftServer minecraftserver = this.field_145850_b.func_73046_m();
            TemplateManager definedstructuremanager = worldserver.func_184163_y();
            Template definedstructure = definedstructuremanager.func_189942_b(minecraftserver, new ResourceLocation(this.field_184420_a));

            if (definedstructure == null) {
                return false;
            } else {
                if (!StringUtils.func_151246_b(definedstructure.func_186261_b())) {
                    this.field_184421_f = definedstructure.func_186261_b();
                }

                BlockPos blockposition2 = definedstructure.func_186259_a();
                boolean flag1 = this.field_184424_i.equals(blockposition2);

                if (!flag1) {
                    this.field_184424_i = blockposition2;
                    this.func_70296_d();
                    IBlockState iblockdata = this.field_145850_b.func_180495_p(blockposition);

                    this.field_145850_b.func_184138_a(blockposition, iblockdata, iblockdata, 3);
                }

                if (flag && !flag1) {
                    return false;
                } else {
                    PlacementSettings definedstructureinfo = (new PlacementSettings()).func_186214_a(this.field_184425_j).func_186220_a(this.field_184426_k).func_186222_a(this.field_184428_m).func_186218_a((ChunkPos) null).func_186225_a((Block) null).func_186226_b(false);

                    if (this.field_189730_q < 1.0F) {
                        definedstructureinfo.func_189946_a(MathHelper.func_76131_a(this.field_189730_q, 0.0F, 1.0F)).func_189949_a(Long.valueOf(this.field_189731_r));
                    }

                    definedstructure.func_186260_a(this.field_145850_b, blockposition1, definedstructureinfo);
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    public void func_189706_E() {
        WorldServer worldserver = (WorldServer) this.field_145850_b;
        TemplateManager definedstructuremanager = worldserver.func_184163_y();

        definedstructuremanager.func_189941_a(new ResourceLocation(this.field_184420_a));
    }

    public boolean func_189709_F() {
        if (this.field_184427_l == TileEntityStructure.Mode.LOAD && !this.field_145850_b.field_72995_K) {
            WorldServer worldserver = (WorldServer) this.field_145850_b;
            MinecraftServer minecraftserver = this.field_145850_b.func_73046_m();
            TemplateManager definedstructuremanager = worldserver.func_184163_y();

            return definedstructuremanager.func_189942_b(minecraftserver, new ResourceLocation(this.field_184420_a)) != null;
        } else {
            return false;
        }
    }

    public boolean func_189722_G() {
        return this.field_189727_n;
    }

    public void func_189723_d(boolean flag) {
        this.field_189727_n = flag;
    }

    public void func_189703_e(boolean flag) {
        this.field_189728_o = flag;
    }

    public void func_189710_f(boolean flag) {
        this.field_189729_p = flag;
    }

    @Nullable
    public ITextComponent func_145748_c_() {
        return new TextComponentTranslation("structure_block.hover." + this.field_184427_l.field_185116_f, new Object[] { this.field_184427_l == TileEntityStructure.Mode.DATA ? this.field_184422_g : this.field_184420_a});
    }

    public static enum Mode implements IStringSerializable {

        SAVE("save", 0), LOAD("load", 1), CORNER("corner", 2), DATA("data", 3);

        private static final TileEntityStructure.Mode[] field_185115_e = new TileEntityStructure.Mode[values().length];
        private final String field_185116_f;
        private final int field_185117_g;

        private Mode(String s, int i) {
            this.field_185116_f = s;
            this.field_185117_g = i;
        }

        public String func_176610_l() {
            return this.field_185116_f;
        }

        public int func_185110_a() {
            return this.field_185117_g;
        }

        public static TileEntityStructure.Mode func_185108_a(int i) {
            return i >= 0 && i < TileEntityStructure.Mode.field_185115_e.length ? TileEntityStructure.Mode.field_185115_e[i] : TileEntityStructure.Mode.field_185115_e[0];
        }

        static {
            TileEntityStructure.Mode[] atileentitystructure_usagemode = values();
            int i = atileentitystructure_usagemode.length;

            for (int j = 0; j < i; ++j) {
                TileEntityStructure.Mode tileentitystructure_usagemode = atileentitystructure_usagemode[j];

                TileEntityStructure.Mode.field_185115_e[tileentitystructure_usagemode.func_185110_a()] = tileentitystructure_usagemode;
            }

        }
    }
}
