package net.minecraft.advancements;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class AdvancementTreeNode {

    private final Advancement field_192328_a;
    private final AdvancementTreeNode field_192329_b;
    private final AdvancementTreeNode field_192330_c;
    private final int field_192331_d;
    private final List<AdvancementTreeNode> field_192332_e = Lists.newArrayList();
    private AdvancementTreeNode field_192333_f;
    private AdvancementTreeNode field_192334_g;
    private int field_192335_h;
    private float field_192336_i;
    private float field_192337_j;
    private float field_192338_k;
    private float field_192339_l;

    public AdvancementTreeNode(Advancement advancement, @Nullable AdvancementTreeNode advancementtree, @Nullable AdvancementTreeNode advancementtree1, int i, int j) {
        if (advancement.func_192068_c() == null) {
            throw new IllegalArgumentException("Can\'t position an invisible advancement!");
        } else {
            this.field_192328_a = advancement;
            this.field_192329_b = advancementtree;
            this.field_192330_c = advancementtree1;
            this.field_192331_d = i;
            this.field_192333_f = this;
            this.field_192335_h = j;
            this.field_192336_i = -1.0F;
            AdvancementTreeNode advancementtree2 = null;

            Advancement advancement1;

            for (Iterator iterator = advancement.func_192069_e().iterator(); iterator.hasNext(); advancementtree2 = this.func_192322_a(advancement1, advancementtree2)) {
                advancement1 = (Advancement) iterator.next();
            }

        }
    }

    @Nullable
    private AdvancementTreeNode func_192322_a(Advancement advancement, @Nullable AdvancementTreeNode advancementtree) {
        Advancement advancement1;

        if (advancement.func_192068_c() != null) {
            advancementtree = new AdvancementTreeNode(advancement, this, advancementtree, this.field_192332_e.size() + 1, this.field_192335_h + 1);
            this.field_192332_e.add(advancementtree);
        } else {
            for (Iterator iterator = advancement.func_192069_e().iterator(); iterator.hasNext(); advancementtree = this.func_192322_a(advancement1, advancementtree)) {
                advancement1 = (Advancement) iterator.next();
            }
        }

        return advancementtree;
    }

    private void func_192320_a() {
        if (this.field_192332_e.isEmpty()) {
            if (this.field_192330_c != null) {
                this.field_192336_i = this.field_192330_c.field_192336_i + 1.0F;
            } else {
                this.field_192336_i = 0.0F;
            }

        } else {
            AdvancementTreeNode advancementtree = null;

            AdvancementTreeNode advancementtree1;

            for (Iterator iterator = this.field_192332_e.iterator(); iterator.hasNext(); advancementtree = advancementtree1.func_192324_a(advancementtree == null ? advancementtree1 : advancementtree)) {
                advancementtree1 = (AdvancementTreeNode) iterator.next();
                advancementtree1.func_192320_a();
            }

            this.func_192325_b();
            float f = (((AdvancementTreeNode) this.field_192332_e.get(0)).field_192336_i + ((AdvancementTreeNode) this.field_192332_e.get(this.field_192332_e.size() - 1)).field_192336_i) / 2.0F;

            if (this.field_192330_c != null) {
                this.field_192336_i = this.field_192330_c.field_192336_i + 1.0F;
                this.field_192337_j = this.field_192336_i - f;
            } else {
                this.field_192336_i = f;
            }

        }
    }

    private float func_192319_a(float f, int i, float f1) {
        this.field_192336_i += f;
        this.field_192335_h = i;
        if (this.field_192336_i < f1) {
            f1 = this.field_192336_i;
        }

        AdvancementTreeNode advancementtree;

        for (Iterator iterator = this.field_192332_e.iterator(); iterator.hasNext(); f1 = advancementtree.func_192319_a(f + this.field_192337_j, i + 1, f1)) {
            advancementtree = (AdvancementTreeNode) iterator.next();
        }

        return f1;
    }

    private void func_192318_a(float f) {
        this.field_192336_i += f;
        Iterator iterator = this.field_192332_e.iterator();

        while (iterator.hasNext()) {
            AdvancementTreeNode advancementtree = (AdvancementTreeNode) iterator.next();

            advancementtree.func_192318_a(f);
        }

    }

    private void func_192325_b() {
        float f = 0.0F;
        float f1 = 0.0F;

        for (int i = this.field_192332_e.size() - 1; i >= 0; --i) {
            AdvancementTreeNode advancementtree = (AdvancementTreeNode) this.field_192332_e.get(i);

            advancementtree.field_192336_i += f;
            advancementtree.field_192337_j += f;
            f1 += advancementtree.field_192338_k;
            f += advancementtree.field_192339_l + f1;
        }

    }

    @Nullable
    private AdvancementTreeNode func_192321_c() {
        return this.field_192334_g != null ? this.field_192334_g : (!this.field_192332_e.isEmpty() ? (AdvancementTreeNode) this.field_192332_e.get(0) : null);
    }

    @Nullable
    private AdvancementTreeNode func_192317_d() {
        return this.field_192334_g != null ? this.field_192334_g : (!this.field_192332_e.isEmpty() ? (AdvancementTreeNode) this.field_192332_e.get(this.field_192332_e.size() - 1) : null);
    }

    private AdvancementTreeNode func_192324_a(AdvancementTreeNode advancementtree) {
        if (this.field_192330_c == null) {
            return advancementtree;
        } else {
            AdvancementTreeNode advancementtree1 = this;
            AdvancementTreeNode advancementtree2 = this;
            AdvancementTreeNode advancementtree3 = this.field_192330_c;
            AdvancementTreeNode advancementtree4 = (AdvancementTreeNode) this.field_192329_b.field_192332_e.get(0);
            float f = this.field_192337_j;
            float f1 = this.field_192337_j;
            float f2 = advancementtree3.field_192337_j;

            float f3;

            for (f3 = advancementtree4.field_192337_j; advancementtree3.func_192317_d() != null && advancementtree1.func_192321_c() != null; f1 += advancementtree2.field_192337_j) {
                advancementtree3 = advancementtree3.func_192317_d();
                advancementtree1 = advancementtree1.func_192321_c();
                advancementtree4 = advancementtree4.func_192321_c();
                advancementtree2 = advancementtree2.func_192317_d();
                advancementtree2.field_192333_f = this;
                float f4 = advancementtree3.field_192336_i + f2 - (advancementtree1.field_192336_i + f) + 1.0F;

                if (f4 > 0.0F) {
                    advancementtree3.func_192326_a(this, advancementtree).func_192316_a(this, f4);
                    f += f4;
                    f1 += f4;
                }

                f2 += advancementtree3.field_192337_j;
                f += advancementtree1.field_192337_j;
                f3 += advancementtree4.field_192337_j;
            }

            if (advancementtree3.func_192317_d() != null && advancementtree2.func_192317_d() == null) {
                advancementtree2.field_192334_g = advancementtree3.func_192317_d();
                advancementtree2.field_192337_j += f2 - f1;
            } else {
                if (advancementtree1.func_192321_c() != null && advancementtree4.func_192321_c() == null) {
                    advancementtree4.field_192334_g = advancementtree1.func_192321_c();
                    advancementtree4.field_192337_j += f - f3;
                }

                advancementtree = this;
            }

            return advancementtree;
        }
    }

    private void func_192316_a(AdvancementTreeNode advancementtree, float f) {
        float f1 = (float) (advancementtree.field_192331_d - this.field_192331_d);

        if (f1 != 0.0F) {
            advancementtree.field_192338_k -= f / f1;
            this.field_192338_k += f / f1;
        }

        advancementtree.field_192339_l += f;
        advancementtree.field_192336_i += f;
        advancementtree.field_192337_j += f;
    }

    private AdvancementTreeNode func_192326_a(AdvancementTreeNode advancementtree, AdvancementTreeNode advancementtree1) {
        return this.field_192333_f != null && advancementtree.field_192329_b.field_192332_e.contains(this.field_192333_f) ? this.field_192333_f : advancementtree1;
    }

    private void func_192327_e() {
        if (this.field_192328_a.func_192068_c() != null) {
            this.field_192328_a.func_192068_c().func_192292_a((float) this.field_192335_h, this.field_192336_i);
        }

        if (!this.field_192332_e.isEmpty()) {
            Iterator iterator = this.field_192332_e.iterator();

            while (iterator.hasNext()) {
                AdvancementTreeNode advancementtree = (AdvancementTreeNode) iterator.next();

                advancementtree.func_192327_e();
            }
        }

    }

    public static void func_192323_a(Advancement advancement) {
        if (advancement.func_192068_c() == null) {
            throw new IllegalArgumentException("Can\'t position children of an invisible root!");
        } else {
            AdvancementTreeNode advancementtree = new AdvancementTreeNode(advancement, (AdvancementTreeNode) null, (AdvancementTreeNode) null, 1, 0);

            advancementtree.func_192320_a();
            float f = advancementtree.func_192319_a(0.0F, 0, advancementtree.field_192336_i);

            if (f < 0.0F) {
                advancementtree.func_192318_a(-f);
            }

            advancementtree.func_192327_e();
        }
    }
}
