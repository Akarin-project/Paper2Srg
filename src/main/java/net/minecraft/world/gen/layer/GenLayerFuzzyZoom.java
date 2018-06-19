package net.minecraft.world.gen.layer;

public class GenLayerFuzzyZoom extends GenLayerZoom {

    public GenLayerFuzzyZoom(long i, GenLayer genlayer) {
        super(i, genlayer);
    }

    protected int func_151617_b(int i, int j, int k, int l) {
        return this.func_151619_a(new int[] { i, j, k, l});
    }
}
