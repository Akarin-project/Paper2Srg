package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;

public class CooldownTracker {

    public final Map<Item, CooldownTracker.Cooldown> field_185147_a = Maps.newHashMap();
    public int field_185148_b;

    public CooldownTracker() {}

    public boolean func_185141_a(Item item) {
        return this.func_185143_a(item, 0.0F) > 0.0F;
    }

    public float func_185143_a(Item item, float f) {
        CooldownTracker.Cooldown itemcooldown_info = (CooldownTracker.Cooldown) this.field_185147_a.get(item);

        if (itemcooldown_info != null) {
            float f1 = (float) (itemcooldown_info.field_185138_b - itemcooldown_info.field_185137_a);
            float f2 = (float) itemcooldown_info.field_185138_b - ((float) this.field_185148_b + f);

            return MathHelper.func_76131_a(f2 / f1, 0.0F, 1.0F);
        } else {
            return 0.0F;
        }
    }

    public void func_185144_a() {
        ++this.field_185148_b;
        if (!this.field_185147_a.isEmpty()) {
            Iterator iterator = this.field_185147_a.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();

                if (((CooldownTracker.Cooldown) entry.getValue()).field_185138_b <= this.field_185148_b) {
                    iterator.remove();
                    this.func_185146_c((Item) entry.getKey());
                }
            }
        }

    }

    public void func_185145_a(Item item, int i) {
        this.field_185147_a.put(item, new CooldownTracker.Cooldown(this.field_185148_b, this.field_185148_b + i, null));
        this.func_185140_b(item, i);
    }

    protected void func_185140_b(Item item, int i) {}

    protected void func_185146_c(Item item) {}

    public class Cooldown {

        final int field_185137_a;
        public final int field_185138_b;

        private Cooldown(int i, int j) {
            this.field_185137_a = i;
            this.field_185138_b = j;
        }

        Cooldown(int i, int j, Object object) {
            this(i, j);
        }
    }
}
