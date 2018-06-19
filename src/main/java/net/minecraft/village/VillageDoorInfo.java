package net.minecraft.village;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;


public class VillageDoorInfo {

    private final BlockPos field_179859_a;
    private final BlockPos field_179857_b;
    private final EnumFacing field_179858_c;
    private int field_75475_f;
    private boolean field_75476_g;
    private int field_75482_h;

    public VillageDoorInfo(BlockPos blockposition, int i, int j, int k) {
        this(blockposition, func_179854_a(i, j), k);
    }

    private static EnumFacing func_179854_a(int i, int j) {
        return i < 0 ? EnumFacing.WEST : (i > 0 ? EnumFacing.EAST : (j < 0 ? EnumFacing.NORTH : EnumFacing.SOUTH));
    }

    public VillageDoorInfo(BlockPos blockposition, EnumFacing enumdirection, int i) {
        this.field_179859_a = blockposition;
        this.field_179858_c = enumdirection;
        this.field_179857_b = blockposition.func_177967_a(enumdirection, 2);
        this.field_75475_f = i;
    }

    public int func_75474_b(int i, int j, int k) {
        return (int) this.field_179859_a.func_177954_c((double) i, (double) j, (double) k);
    }

    public int func_179848_a(BlockPos blockposition) {
        return (int) blockposition.func_177951_i(this.func_179852_d());
    }

    public int func_179846_b(BlockPos blockposition) {
        return (int) this.field_179857_b.func_177951_i(blockposition);
    }

    public boolean func_179850_c(BlockPos blockposition) {
        int i = blockposition.func_177958_n() - this.field_179859_a.func_177958_n();
        int j = blockposition.func_177952_p() - this.field_179859_a.func_177956_o();

        return i * this.field_179858_c.func_82601_c() + j * this.field_179858_c.func_82599_e() >= 0;
    }

    public void func_75466_d() {
        this.field_75482_h = 0;
    }

    public void func_75470_e() {
        ++this.field_75482_h;
    }

    public int func_75468_f() {
        return this.field_75482_h;
    }

    public BlockPos func_179852_d() {
        return this.field_179859_a;
    }

    public BlockPos func_179856_e() {
        return this.field_179857_b;
    }

    public int func_179847_f() {
        return this.field_179858_c.func_82601_c() * 2;
    }

    public int func_179855_g() {
        return this.field_179858_c.func_82599_e() * 2;
    }

    public int func_75473_b() {
        return this.field_75475_f;
    }

    public void func_179849_a(int i) {
        this.field_75475_f = i;
    }

    public boolean func_179851_i() {
        return this.field_75476_g;
    }

    public void func_179853_a(boolean flag) {
        this.field_75476_g = flag;
    }

    public EnumFacing func_188567_j() {
        return this.field_179858_c;
    }
}
