package net.minecraft.world.gen.layer;

public class GenLayerFuzzyZoom extends GenLayerZoom {

    public GenLayerFuzzyZoom(long i, GenLayer genlayer) {
        super(i, genlayer);
    }

    protected int selectModeOrRandom(int i, int j, int k, int l) {
        return this.selectRandom(new int[] { i, j, k, l});
    }
}
