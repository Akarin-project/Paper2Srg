package net.minecraft.entity;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public abstract class EntityAgeable extends EntityCreature {

    private static final DataParameter<Boolean> BABY = EntityDataManager.createKey(EntityAgeable.class, DataSerializers.BOOLEAN);
    protected int growingAge;
    protected int forcedAge;
    protected int forcedAgeTimer;
    private float ageWidth = -1.0F;
    private float ageHeight;
    public boolean ageLocked; // CraftBukkit

    // Spigot start
    @Override
    public void inactiveTick()
    {
        super.inactiveTick();
        if ( this.world.isRemote || this.ageLocked )
        { // CraftBukkit
            this.setScaleForAge( this.isChild() );
        } else
        {
            int i = this.getGrowingAge();

            if ( i < 0 )
            {
                ++i;
                this.setGrowingAge( i );
            } else if ( i > 0 )
            {
                --i;
                this.setGrowingAge( i );
            }
        }
    }
    // Spigot end

    public EntityAgeable(World world) {
        super(world);
    }

    @Nullable
    public abstract EntityAgeable createChild(EntityAgeable entityageable);

    public boolean processInteract(EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (itemstack.getItem() == Items.SPAWN_EGG) {
            if (!this.world.isRemote) {
                Class oclass = (Class) EntityList.REGISTRY.getObject(ItemMonsterPlacer.getNamedIdFrom(itemstack));

                if (oclass != null && this.getClass() == oclass) {
                    EntityAgeable entityageable = this.createChild(this);

                    if (entityageable != null) {
                        entityageable.setGrowingAge(-24000);
                        entityageable.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
                        this.world.addEntity(entityageable, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SPAWNER_EGG); // CraftBukkit
                        if (itemstack.hasDisplayName()) {
                            entityageable.setCustomNameTag(itemstack.getDisplayName());
                        }

                        if (!entityhuman.capabilities.isCreativeMode) {
                            itemstack.shrink(1);
                        }
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    protected boolean holdingSpawnEggOfClass(ItemStack itemstack, Class<? extends Entity> oclass) {
        if (itemstack.getItem() != Items.SPAWN_EGG) {
            return false;
        } else {
            Class oclass1 = (Class) EntityList.REGISTRY.getObject(ItemMonsterPlacer.getNamedIdFrom(itemstack));

            return oclass1 != null && oclass == oclass1;
        }
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityAgeable.BABY, Boolean.valueOf(false));
    }

    public int getGrowingAge() {
        return this.world.isRemote ? (((Boolean) this.dataManager.get(EntityAgeable.BABY)).booleanValue() ? -1 : 1) : this.growingAge;
    }

    public void ageUp(int i, boolean flag) {
        int j = this.getGrowingAge();
        int k = j;

        j += i * 20;
        if (j > 0) {
            j = 0;
            if (k < 0) {
                this.onGrowingAdult();
            }
        }

        int l = j - k;

        this.setGrowingAge(j);
        if (flag) {
            this.forcedAge += l;
            if (this.forcedAgeTimer == 0) {
                this.forcedAgeTimer = 40;
            }
        }

        if (this.getGrowingAge() == 0) {
            this.setGrowingAge(this.forcedAge);
        }

    }

    public void addGrowth(int i) {
        this.ageUp(i, false);
    }

    public void setGrowingAge(int i) {
        this.dataManager.set(EntityAgeable.BABY, Boolean.valueOf(i < 0));
        this.growingAge = i;
        this.setScaleForAge(this.isChild());
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("Age", this.getGrowingAge());
        nbttagcompound.setInteger("ForcedAge", this.forcedAge);
        nbttagcompound.setBoolean("AgeLocked", this.ageLocked); // CraftBukkit
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.setGrowingAge(nbttagcompound.getInteger("Age"));
        this.forcedAge = nbttagcompound.getInteger("ForcedAge");
        this.ageLocked = nbttagcompound.getBoolean("AgeLocked"); // CraftBukkit
    }

    public void notifyDataManagerChange(DataParameter<?> datawatcherobject) {
        if (EntityAgeable.BABY.equals(datawatcherobject)) {
            this.setScaleForAge(this.isChild());
        }

        super.notifyDataManagerChange(datawatcherobject);
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.world.isRemote || ageLocked) { // CraftBukkit
            if (this.forcedAgeTimer > 0) {
                if (this.forcedAgeTimer % 4 == 0) {
                    this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, 0.0D, 0.0D, 0.0D, new int[0]);
                }

                --this.forcedAgeTimer;
            }
        } else {
            int i = this.getGrowingAge();

            if (i < 0) {
                ++i;
                this.setGrowingAge(i);
                if (i == 0) {
                    this.onGrowingAdult();
                }
            } else if (i > 0) {
                --i;
                this.setGrowingAge(i);
            }
        }

    }

    protected void onGrowingAdult() {}

    public boolean isChild() {
        return this.getGrowingAge() < 0;
    }

    public void setScaleForAge(boolean flag) {
        this.setScale(flag ? 0.5F : 1.0F);
    }

    public final void setSize(float f, float f1) {
        boolean flag = this.ageWidth > 0.0F;

        this.ageWidth = f;
        this.ageHeight = f1;
        if (!flag) {
            this.setScale(1.0F);
        }

    }

    protected final void setScale(float f) {
        super.setSize(this.ageWidth * f, this.ageHeight * f);
    }
}
