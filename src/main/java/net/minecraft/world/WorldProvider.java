package net.minecraft.world;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.gen.ChunkGeneratorDebug;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.IChunkGenerator;

public abstract class WorldProvider {

    public static final float[] field_111203_a = new float[] { 1.0F, 0.75F, 0.5F, 0.25F, 0.0F, 0.25F, 0.5F, 0.75F};
    protected World field_76579_a;
    private WorldType field_76577_b;
    private String field_82913_c;
    protected BiomeProvider field_76578_c;
    protected boolean field_76575_d;
    protected boolean field_76576_e;
    protected boolean field_191067_f;
    protected final float[] field_76573_f = new float[16];
    private final float[] field_76580_h = new float[4];

    public WorldProvider() {}

    public final void func_76558_a(World world) {
        this.field_76579_a = world;
        this.field_76577_b = world.func_72912_H().func_76067_t();
        this.field_82913_c = world.func_72912_H().func_82571_y();
        this.func_76572_b();
        this.func_76556_a();
    }

    protected void func_76556_a() {
        float f = 0.0F;

        for (int i = 0; i <= 15; ++i) {
            float f1 = 1.0F - (float) i / 15.0F;

            this.field_76573_f[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * 1.0F + 0.0F;
        }

    }

    protected void func_76572_b() {
        this.field_191067_f = true;
        WorldType worldtype = this.field_76579_a.func_72912_H().func_76067_t();

        if (worldtype == WorldType.field_77138_c) {
            FlatGeneratorInfo worldgenflatinfo = FlatGeneratorInfo.func_82651_a(this.field_76579_a.func_72912_H().func_82571_y());

            this.field_76578_c = new BiomeProviderSingle(Biome.func_180276_a(worldgenflatinfo.func_82648_a(), Biomes.field_180279_ad));
        } else if (worldtype == WorldType.field_180272_g) {
            this.field_76578_c = new BiomeProviderSingle(Biomes.field_76772_c);
        } else {
            this.field_76578_c = new BiomeProvider(this.field_76579_a.func_72912_H());
        }

    }

    public IChunkGenerator func_186060_c() {
        return (IChunkGenerator) (this.field_76577_b == WorldType.field_77138_c ? new ChunkGeneratorFlat(this.field_76579_a, this.field_76579_a.func_72905_C(), this.field_76579_a.func_72912_H().func_76089_r(), this.field_82913_c) : (this.field_76577_b == WorldType.field_180272_g ? new ChunkGeneratorDebug(this.field_76579_a) : (this.field_76577_b == WorldType.field_180271_f ? new ChunkGeneratorOverworld(this.field_76579_a, this.field_76579_a.func_72905_C(), this.field_76579_a.func_72912_H().func_76089_r(), this.field_82913_c) : new ChunkGeneratorOverworld(this.field_76579_a, this.field_76579_a.func_72905_C(), this.field_76579_a.func_72912_H().func_76089_r(), this.field_82913_c))));
    }

    public boolean func_76566_a(int i, int j) {
        BlockPos blockposition = new BlockPos(i, 0, j);

        return this.field_76579_a.func_180494_b(blockposition).func_185352_i() ? true : this.field_76579_a.func_184141_c(blockposition).func_177230_c() == Blocks.field_150349_c;
    }

    public float func_76563_a(long i, float f) {
        int j = (int) (i % 24000L);
        float f1 = ((float) j + f) / 24000.0F - 0.25F;

        if (f1 < 0.0F) {
            ++f1;
        }

        if (f1 > 1.0F) {
            --f1;
        }

        float f2 = f1;

        f1 = 1.0F - (float) ((Math.cos((double) f1 * 3.141592653589793D) + 1.0D) / 2.0D);
        f1 = f2 + (f1 - f2) / 3.0F;
        return f1;
    }

    public int func_76559_b(long i) {
        return (int) (i / 24000L % 8L + 8L) % 8;
    }

    public boolean func_76569_d() {
        return true;
    }

    public boolean func_76567_e() {
        return true;
    }

    @Nullable
    public BlockPos func_177496_h() {
        return null;
    }

    public int func_76557_i() {
        return this.field_76577_b == WorldType.field_77138_c ? 4 : this.field_76579_a.func_181545_F() + 1;
    }

    public BiomeProvider func_177499_m() {
        return this.field_76578_c;
    }

    public boolean func_177500_n() {
        return this.field_76575_d;
    }

    public boolean func_191066_m() {
        return this.field_191067_f;
    }

    public final boolean isSkyMissing() { return this.func_177495_o(); } // Paper - OBFHELPER
    public boolean func_177495_o() {
        return this.field_76576_e;
    }

    public float[] func_177497_p() {
        return this.field_76573_f;
    }

    public WorldBorder func_177501_r() {
        return new WorldBorder();
    }

    public void func_186061_a(EntityPlayerMP entityplayer) {}

    public void func_186062_b(EntityPlayerMP entityplayer) {}

    public abstract DimensionType func_186058_p();

    public void func_186057_q() {}

    public void func_186059_r() {}

    public boolean func_186056_c(int i, int j) {
        return !this.field_76579_a.shouldStayLoaded(i, j); // Paper - Use shouldStayLoaded check for all worlds
    }
}
