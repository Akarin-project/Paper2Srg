package net.minecraft.advancements;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class AdvancementTreeNode {

    private final Advancement advancement;
    private final AdvancementTreeNode parent;
    private final AdvancementTreeNode sibling;
    private final int index;
    private final List<AdvancementTreeNode> children = Lists.newArrayList();
    private AdvancementTreeNode ancestor;
    private AdvancementTreeNode thread;
    private int x;
    private float y;
    private float mod;
    private float change;
    private float shift;

    public AdvancementTreeNode(Advancement advancement, @Nullable AdvancementTreeNode advancementtree, @Nullable AdvancementTreeNode advancementtree1, int i, int j) {
        if (advancement.getDisplay() == null) {
            throw new IllegalArgumentException("Can\'t position an invisible advancement!");
        } else {
            this.advancement = advancement;
            this.parent = advancementtree;
            this.sibling = advancementtree1;
            this.index = i;
            this.ancestor = this;
            this.x = j;
            this.y = -1.0F;
            AdvancementTreeNode advancementtree2 = null;

            Advancement advancement1;

            for (Iterator iterator = advancement.getChildren().iterator(); iterator.hasNext(); advancementtree2 = this.buildSubTree(advancement1, advancementtree2)) {
                advancement1 = (Advancement) iterator.next();
            }

        }
    }

    @Nullable
    private AdvancementTreeNode buildSubTree(Advancement advancement, @Nullable AdvancementTreeNode advancementtree) {
        Advancement advancement1;

        if (advancement.getDisplay() != null) {
            advancementtree = new AdvancementTreeNode(advancement, this, advancementtree, this.children.size() + 1, this.x + 1);
            this.children.add(advancementtree);
        } else {
            for (Iterator iterator = advancement.getChildren().iterator(); iterator.hasNext(); advancementtree = this.buildSubTree(advancement1, advancementtree)) {
                advancement1 = (Advancement) iterator.next();
            }
        }

        return advancementtree;
    }

    private void firstWalk() {
        if (this.children.isEmpty()) {
            if (this.sibling != null) {
                this.y = this.sibling.y + 1.0F;
            } else {
                this.y = 0.0F;
            }

        } else {
            AdvancementTreeNode advancementtree = null;

            AdvancementTreeNode advancementtree1;

            for (Iterator iterator = this.children.iterator(); iterator.hasNext(); advancementtree = advancementtree1.apportion(advancementtree == null ? advancementtree1 : advancementtree)) {
                advancementtree1 = (AdvancementTreeNode) iterator.next();
                advancementtree1.firstWalk();
            }

            this.executeShifts();
            float f = (((AdvancementTreeNode) this.children.get(0)).y + ((AdvancementTreeNode) this.children.get(this.children.size() - 1)).y) / 2.0F;

            if (this.sibling != null) {
                this.y = this.sibling.y + 1.0F;
                this.mod = this.y - f;
            } else {
                this.y = f;
            }

        }
    }

    private float secondWalk(float f, int i, float f1) {
        this.y += f;
        this.x = i;
        if (this.y < f1) {
            f1 = this.y;
        }

        AdvancementTreeNode advancementtree;

        for (Iterator iterator = this.children.iterator(); iterator.hasNext(); f1 = advancementtree.secondWalk(f + this.mod, i + 1, f1)) {
            advancementtree = (AdvancementTreeNode) iterator.next();
        }

        return f1;
    }

    private void thirdWalk(float f) {
        this.y += f;
        Iterator iterator = this.children.iterator();

        while (iterator.hasNext()) {
            AdvancementTreeNode advancementtree = (AdvancementTreeNode) iterator.next();

            advancementtree.thirdWalk(f);
        }

    }

    private void executeShifts() {
        float f = 0.0F;
        float f1 = 0.0F;

        for (int i = this.children.size() - 1; i >= 0; --i) {
            AdvancementTreeNode advancementtree = (AdvancementTreeNode) this.children.get(i);

            advancementtree.y += f;
            advancementtree.mod += f;
            f1 += advancementtree.change;
            f += advancementtree.shift + f1;
        }

    }

    @Nullable
    private AdvancementTreeNode getFirstChild() {
        return this.thread != null ? this.thread : (!this.children.isEmpty() ? (AdvancementTreeNode) this.children.get(0) : null);
    }

    @Nullable
    private AdvancementTreeNode getLastChild() {
        return this.thread != null ? this.thread : (!this.children.isEmpty() ? (AdvancementTreeNode) this.children.get(this.children.size() - 1) : null);
    }

    private AdvancementTreeNode apportion(AdvancementTreeNode advancementtree) {
        if (this.sibling == null) {
            return advancementtree;
        } else {
            AdvancementTreeNode advancementtree1 = this;
            AdvancementTreeNode advancementtree2 = this;
            AdvancementTreeNode advancementtree3 = this.sibling;
            AdvancementTreeNode advancementtree4 = (AdvancementTreeNode) this.parent.children.get(0);
            float f = this.mod;
            float f1 = this.mod;
            float f2 = advancementtree3.mod;

            float f3;

            for (f3 = advancementtree4.mod; advancementtree3.getLastChild() != null && advancementtree1.getFirstChild() != null; f1 += advancementtree2.mod) {
                advancementtree3 = advancementtree3.getLastChild();
                advancementtree1 = advancementtree1.getFirstChild();
                advancementtree4 = advancementtree4.getFirstChild();
                advancementtree2 = advancementtree2.getLastChild();
                advancementtree2.ancestor = this;
                float f4 = advancementtree3.y + f2 - (advancementtree1.y + f) + 1.0F;

                if (f4 > 0.0F) {
                    advancementtree3.getAncestor(this, advancementtree).moveSubtree(this, f4);
                    f += f4;
                    f1 += f4;
                }

                f2 += advancementtree3.mod;
                f += advancementtree1.mod;
                f3 += advancementtree4.mod;
            }

            if (advancementtree3.getLastChild() != null && advancementtree2.getLastChild() == null) {
                advancementtree2.thread = advancementtree3.getLastChild();
                advancementtree2.mod += f2 - f1;
            } else {
                if (advancementtree1.getFirstChild() != null && advancementtree4.getFirstChild() == null) {
                    advancementtree4.thread = advancementtree1.getFirstChild();
                    advancementtree4.mod += f - f3;
                }

                advancementtree = this;
            }

            return advancementtree;
        }
    }

    private void moveSubtree(AdvancementTreeNode advancementtree, float f) {
        float f1 = (float) (advancementtree.index - this.index);

        if (f1 != 0.0F) {
            advancementtree.change -= f / f1;
            this.change += f / f1;
        }

        advancementtree.shift += f;
        advancementtree.y += f;
        advancementtree.mod += f;
    }

    private AdvancementTreeNode getAncestor(AdvancementTreeNode advancementtree, AdvancementTreeNode advancementtree1) {
        return this.ancestor != null && advancementtree.parent.children.contains(this.ancestor) ? this.ancestor : advancementtree1;
    }

    private void updatePosition() {
        if (this.advancement.getDisplay() != null) {
            this.advancement.getDisplay().setPosition((float) this.x, this.y);
        }

        if (!this.children.isEmpty()) {
            Iterator iterator = this.children.iterator();

            while (iterator.hasNext()) {
                AdvancementTreeNode advancementtree = (AdvancementTreeNode) iterator.next();

                advancementtree.updatePosition();
            }
        }

    }

    public static void layout(Advancement advancement) {
        if (advancement.getDisplay() == null) {
            throw new IllegalArgumentException("Can\'t position children of an invisible root!");
        } else {
            AdvancementTreeNode advancementtree = new AdvancementTreeNode(advancement, (AdvancementTreeNode) null, (AdvancementTreeNode) null, 1, 0);

            advancementtree.firstWalk();
            float f = advancementtree.secondWalk(0.0F, 0, advancementtree.y);

            if (f < 0.0F) {
                advancementtree.thirdWalk(-f);
            }

            advancementtree.updatePosition();
        }
    }
}
