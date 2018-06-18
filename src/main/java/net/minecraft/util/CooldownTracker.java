package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;

public class CooldownTracker {

    public final Map<Item, CooldownTracker.Cooldown> cooldowns = Maps.newHashMap();
    public int ticks;

    public CooldownTracker() {}

    public boolean hasCooldown(Item item) {
        return this.getCooldown(item, 0.0F) > 0.0F;
    }

    public float getCooldown(Item item, float f) {
        CooldownTracker.Cooldown itemcooldown_info = (CooldownTracker.Cooldown) this.cooldowns.get(item);

        if (itemcooldown_info != null) {
            float f1 = (float) (itemcooldown_info.expireTicks - itemcooldown_info.createTicks);
            float f2 = (float) itemcooldown_info.expireTicks - ((float) this.ticks + f);

            return MathHelper.clamp(f2 / f1, 0.0F, 1.0F);
        } else {
            return 0.0F;
        }
    }

    public void tick() {
        ++this.ticks;
        if (!this.cooldowns.isEmpty()) {
            Iterator iterator = this.cooldowns.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();

                if (((CooldownTracker.Cooldown) entry.getValue()).expireTicks <= this.ticks) {
                    iterator.remove();
                    this.notifyOnRemove((Item) entry.getKey());
                }
            }
        }

    }

    public void setCooldown(Item item, int i) {
        this.cooldowns.put(item, new CooldownTracker.Cooldown(this.ticks, this.ticks + i, null));
        this.notifyOnSet(item, i);
    }

    protected void notifyOnSet(Item item, int i) {}

    protected void notifyOnRemove(Item item) {}

    public class Cooldown {

        final int createTicks;
        public final int expireTicks;

        private Cooldown(int i, int j) {
            this.createTicks = i;
            this.expireTicks = j;
        }

        Cooldown(int i, int j, Object object) {
            this(i, j);
        }
    }
}
