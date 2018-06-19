package net.minecraft.entity.player;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Deque;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMapBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketCamera;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketCombatEvent;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.network.play.server.SPacketResourcePackSend;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketSignEditorOpen;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.network.play.server.SPacketWindowProperty;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.stats.RecipeBookServer;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.CooldownTrackerServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.FoodStats;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.GameType;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.ILootContainer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.WeatherType;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChangedMainHandEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.MainHand;

// CraftBukkit start
import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.WeatherType;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChangedMainHandEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.MainHand;
// CraftBukkit end

public class EntityPlayerMP extends EntityPlayer implements IContainerListener {

    private static final Logger field_147102_bM = LogManager.getLogger();
    public String field_71148_cg = null; // PAIL: private -> public // Paper - default to null
    public long lastSave = MinecraftServer.currentTick; // Paper
    public NetHandlerPlayServer field_71135_a;
    public final MinecraftServer field_71133_b;
    public final PlayerInteractionManager field_71134_c;
    public double field_71131_d;
    public double field_71132_e;
    public final Deque<Integer> field_71130_g = new ArrayDeque<>(); // Paper
    private final PlayerAdvancements field_192042_bX;
    private final StatisticsManagerServer field_147103_bO;
    private float field_130068_bO = Float.MIN_VALUE;
    private int field_184852_bV = Integer.MIN_VALUE;
    private int field_184853_bW = Integer.MIN_VALUE;
    private int field_184854_bX = Integer.MIN_VALUE;
    private int field_184855_bY = Integer.MIN_VALUE;
    private int field_184856_bZ = Integer.MIN_VALUE;
    private float field_71149_ch = -1.0E8F;
    private int field_71146_ci = -99999999;
    private boolean field_71147_cj = true;
    public int field_71144_ck = -99999999;
    public int field_147101_bU = 60;
    private EntityPlayer.EnumChatVisibility field_71143_cn;
    private boolean field_71140_co = true;
    private long field_143005_bX = System.currentTimeMillis();
    private Entity field_175401_bS;
    public boolean field_184851_cj;
    private boolean field_192040_cp; private void setHasSeenCredits(boolean has) { this.field_192040_cp = has; } // Paper - OBFHELPER
    private final RecipeBookServer field_192041_cq = new RecipeBookServer();
    private Vec3d field_193107_ct;
    private int field_193108_cu;
    private boolean field_193109_cv;
    private Vec3d field_193110_cw;
    private int field_71139_cq;
    public boolean field_71137_h;
    public int field_71138_i;
    public boolean field_71136_j;
    // Paper start - Player view distance API
    private int viewDistance = -1;
    public int getViewDistance() {
        return viewDistance == -1 ? ((WorldServer) field_70170_p).func_184164_w().getViewDistance() : viewDistance;
    }
    public void setViewDistance(int viewDistance) {
        this.viewDistance = viewDistance;
    }
    // Paper end
    private int containerUpdateDelay; // Paper

    // CraftBukkit start
    public String displayName;
    public ITextComponent listName;
    public org.bukkit.Location compassTarget;
    public int newExp = 0;
    public int newLevel = 0;
    public int newTotalExp = 0;
    public boolean keepLevel = false;
    public double maxHealthCache;
    public boolean joining = true;
    public boolean sentListPacket = false;
    // CraftBukkit end

    public EntityPlayerMP(MinecraftServer minecraftserver, WorldServer worldserver, GameProfile gameprofile, PlayerInteractionManager playerinteractmanager) {
        super(worldserver, gameprofile);
        playerinteractmanager.field_73090_b = this;
        this.field_71134_c = playerinteractmanager;
        // CraftBukkit start
        BlockPos blockposition = getSpawnPoint(minecraftserver, worldserver);

        this.field_71133_b = minecraftserver;
        this.field_147103_bO = minecraftserver.func_184103_al().getStatisticManager(this);
        this.field_192042_bX = minecraftserver.func_184103_al().func_192054_h(this);
        this.field_70138_W = 1.0F;
        this.func_174828_a(blockposition, 0.0F, 0.0F);
        // CraftBukkit end

        while (!worldserver.func_184144_a(this, this.func_174813_aQ()).isEmpty() && this.field_70163_u < 255.0D) {
            this.func_70107_b(this.field_70165_t, this.field_70163_u + 1.0D, this.field_70161_v);
        }

        // CraftBukkit start
        this.displayName = this.func_70005_c_();
        this.canPickUpLoot = true;
        this.maxHealthCache = this.func_110138_aP();
        // CraftBukkit end
    }

    public final BlockPos getSpawnPoint(MinecraftServer minecraftserver, WorldServer worldserver) {
        BlockPos blockposition = worldserver.func_175694_M();

        if (worldserver.field_73011_w.func_191066_m() && worldserver.func_72912_H().func_76077_q() != GameType.ADVENTURE) {
            int i = Math.max(0, minecraftserver.func_184108_a(worldserver));
            int j = MathHelper.func_76128_c(worldserver.func_175723_af().func_177729_b((double) blockposition.func_177958_n(), (double) blockposition.func_177952_p()));

            if (j < i) {
                i = j;
            }

            if (j <= 1) {
                i = 1;
            }

            blockposition = worldserver.func_175672_r(blockposition.func_177982_a(this.field_70146_Z.nextInt(i * 2 + 1) - i, 0, this.field_70146_Z.nextInt(i * 2 + 1) - i));
        }

        return blockposition;
    }
    // CraftBukkit end

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        if (this.field_70163_u > 300) this.field_70163_u = 257; // Paper - bring down to a saner Y level if out of world
        if (nbttagcompound.func_150297_b("playerGameType", 99)) {
            if (this.func_184102_h().func_104056_am()) {
                this.field_71134_c.func_73076_a(this.func_184102_h().func_71265_f());
            } else {
                this.field_71134_c.func_73076_a(GameType.func_77146_a(nbttagcompound.func_74762_e("playerGameType")));
            }
        }

        if (nbttagcompound.func_150297_b("enteredNetherPosition", 10)) {
            NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("enteredNetherPosition");

            this.field_193110_cw = new Vec3d(nbttagcompound1.func_74769_h("x"), nbttagcompound1.func_74769_h("y"), nbttagcompound1.func_74769_h("z"));
        }

        this.field_192040_cp = nbttagcompound.func_74767_n("seenCredits");
        if (nbttagcompound.func_150297_b("recipeBook", 10)) {
            this.field_192041_cq.func_192825_a(nbttagcompound.func_74775_l("recipeBook"));
        }
        this.getBukkitEntity().readExtraData(nbttagcompound); // CraftBukkit

    }

    public static void func_191522_a(DataFixer dataconvertermanager) {
        dataconvertermanager.func_188258_a(FixTypes.PLAYER, new IDataWalker() {
            public NBTTagCompound func_188266_a(IDataFixer dataconverter, NBTTagCompound nbttagcompound, int i) {
                if (nbttagcompound.func_150297_b("RootVehicle", 10)) {
                    NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("RootVehicle");

                    if (nbttagcompound1.func_150297_b("Entity", 10)) {
                        nbttagcompound1.func_74782_a("Entity", dataconverter.func_188251_a(FixTypes.ENTITY, nbttagcompound1.func_74775_l("Entity"), i));
                    }
                }

                return nbttagcompound;
            }
        });
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        nbttagcompound.func_74768_a("playerGameType", this.field_71134_c.func_73081_b().func_77148_a());
        nbttagcompound.func_74757_a("seenCredits", this.field_192040_cp);
        if (this.field_193110_cw != null) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            nbttagcompound1.func_74780_a("x", this.field_193110_cw.field_72450_a);
            nbttagcompound1.func_74780_a("y", this.field_193110_cw.field_72448_b);
            nbttagcompound1.func_74780_a("z", this.field_193110_cw.field_72449_c);
            nbttagcompound.func_74782_a("enteredNetherPosition", nbttagcompound1);
        }

        Entity entity = this.func_184208_bv();
        Entity entity1 = this.func_184187_bx();

        if (entity1 != null && entity != this && entity.func_184180_b(EntityPlayerMP.class).size() == 1) {
            NBTTagCompound nbttagcompound2 = new NBTTagCompound();
            NBTTagCompound nbttagcompound3 = new NBTTagCompound();

            entity.func_70039_c(nbttagcompound3);
            nbttagcompound2.func_186854_a("Attach", entity1.func_110124_au());
            nbttagcompound2.func_74782_a("Entity", nbttagcompound3);
            nbttagcompound.func_74782_a("RootVehicle", nbttagcompound2);
        }

        nbttagcompound.func_74782_a("recipeBook", this.field_192041_cq.func_192824_e());
        this.getBukkitEntity().setExtraData(nbttagcompound); // CraftBukkit
    }

    // CraftBukkit start - World fallback code, either respawn location or global spawn
    public void func_70029_a(World world) {
        super.func_70029_a(world);
        if (world == null) {
            this.field_70128_L = false;
            BlockPos position = null;
            if (this.spawnWorld != null && !this.spawnWorld.equals("")) {
                CraftWorld cworld = (CraftWorld) Bukkit.getServer().getWorld(this.spawnWorld);
                if (cworld != null && this.func_180470_cg() != null) {
                    world = cworld.getHandle();
                    position = EntityPlayer.func_180467_a(cworld.getHandle(), this.func_180470_cg(), false);
                }
            }
            if (world == null || position == null) {
                world = ((CraftWorld) Bukkit.getServer().getWorlds().get(0)).getHandle();
                position = world.func_175694_M();
            }
            this.field_70170_p = world;
            this.func_70107_b(position.func_177958_n() + 0.5, position.func_177956_o(), position.func_177952_p() + 0.5);
        }
        this.field_71093_bK = ((WorldServer) this.field_70170_p).dimension;
        this.field_71134_c.func_73080_a((WorldServer) world);
    }
    // CraftBukkit end

    public void func_82242_a(int i) {
        super.func_82242_a(i);
        this.field_71144_ck = -1;
    }

    public void func_192024_a(ItemStack itemstack, int i) {
        super.func_192024_a(itemstack, i);
        this.field_71144_ck = -1;
    }

    public void func_71116_b() {
        this.field_71070_bA.func_75132_a(this);
    }

    public void func_152111_bt() {
        super.func_152111_bt();
        this.field_71135_a.func_147359_a(new SPacketCombatEvent(this.func_110142_aN(), SPacketCombatEvent.Event.ENTER_COMBAT));
    }

    public void func_152112_bu() {
        super.func_152112_bu();
        this.field_71135_a.func_147359_a(new SPacketCombatEvent(this.func_110142_aN(), SPacketCombatEvent.Event.END_COMBAT));
    }

    protected void func_191955_a(IBlockState iblockdata) {
        CriteriaTriggers.field_192124_d.func_192193_a(this, iblockdata);
    }

    protected CooldownTracker func_184815_l() {
        return new CooldownTrackerServer(this);
    }

    public void func_70071_h_() {
        // CraftBukkit start
        if (this.joining) {
            this.joining = false;
        }
        // CraftBukkit end
        this.field_71134_c.func_73075_a();
        --this.field_147101_bU;
        if (this.field_70172_ad > 0) {
            --this.field_70172_ad;
        }

        // Paper start - Configurable container update tick rate
        if (--containerUpdateDelay <= 0) {
            this.field_71070_bA.func_75142_b();
            containerUpdateDelay = field_70170_p.paperConfig.containerUpdateTickRate;
        }
        // Paper end
        if (!this.field_70170_p.field_72995_K && !this.field_71070_bA.func_75145_c(this)) {
            this.func_71053_j();
            this.field_71070_bA = this.field_71069_bz;
        }

        while (!this.field_71130_g.isEmpty()) {
            int i = Math.min(this.field_71130_g.size(), Integer.MAX_VALUE);
            int[] aint = new int[i];
            Iterator iterator = this.field_71130_g.iterator();
            int j = 0;

            // Paper start
            /* while (iterator.hasNext() && j < i) {
                aint[j++] = ((Integer) iterator.next()).intValue();
                iterator.remove();
            } */

            Integer integer;
            while (j < i && (integer = this.field_71130_g.poll()) != null) {
                aint[j++] = integer.intValue();
            }
            // Paper end

            this.field_71135_a.func_147359_a(new SPacketDestroyEntities(aint));
        }

        Entity entity = this.func_175398_C();

        if (entity != this) {
            if (entity.func_70089_S()) {
                this.func_70080_a(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, entity.field_70177_z, entity.field_70125_A);
                this.field_71133_b.func_184103_al().func_72358_d(this);
                if (this.func_70093_af()) {
                    this.func_175399_e(this);
                }
            } else {
                this.func_175399_e(this);
            }
        }

        CriteriaTriggers.field_193135_v.func_193182_a(this);
        if (this.field_193107_ct != null) {
            CriteriaTriggers.field_193133_t.func_193162_a(this, this.field_193107_ct, this.field_70173_aa - this.field_193108_cu);
        }

        this.field_192042_bX.func_192741_b(this);
    }

    public void func_71127_g() {
        try {
            super.func_70071_h_();

            for (int i = 0; i < this.field_71071_by.func_70302_i_(); ++i) {
                ItemStack itemstack = this.field_71071_by.func_70301_a(i);

                if (!itemstack.func_190926_b() && itemstack.func_77973_b().func_77643_m_()) {
                    Packet packet = ((ItemMapBase) itemstack.func_77973_b()).func_150911_c(itemstack, this.field_70170_p, (EntityPlayer) this);

                    if (packet != null) {
                        this.field_71135_a.func_147359_a(packet);
                    }
                }
            }

            if (this.func_110143_aJ() != this.field_71149_ch || this.field_71146_ci != this.field_71100_bB.func_75116_a() || this.field_71100_bB.func_75115_e() == 0.0F != this.field_71147_cj) {
                this.field_71135_a.func_147359_a(new SPacketUpdateHealth(this.getBukkitEntity().getScaledHealth(), this.field_71100_bB.func_75116_a(), this.field_71100_bB.func_75115_e())); // CraftBukkit
                this.field_71149_ch = this.func_110143_aJ();
                this.field_71146_ci = this.field_71100_bB.func_75116_a();
                this.field_71147_cj = this.field_71100_bB.func_75115_e() == 0.0F;
            }

            if (this.func_110143_aJ() + this.func_110139_bj() != this.field_130068_bO) {
                this.field_130068_bO = this.func_110143_aJ() + this.func_110139_bj();
                this.func_184849_a(IScoreCriteria.field_96638_f, MathHelper.func_76123_f(this.field_130068_bO));
            }

            if (this.field_71100_bB.func_75116_a() != this.field_184852_bV) {
                this.field_184852_bV = this.field_71100_bB.func_75116_a();
                this.func_184849_a(IScoreCriteria.field_186698_h, MathHelper.func_76123_f((float) this.field_184852_bV));
            }

            if (this.func_70086_ai() != this.field_184853_bW) {
                this.field_184853_bW = this.func_70086_ai();
                this.func_184849_a(IScoreCriteria.field_186699_i, MathHelper.func_76123_f((float) this.field_184853_bW));
            }

            // CraftBukkit start - Force max health updates
            if (this.maxHealthCache != this.func_110138_aP()) {
                this.getBukkitEntity().updateScaledHealth();
            }
            // CraftBukkit end

            if (this.func_70658_aO() != this.field_184854_bX) {
                this.field_184854_bX = this.func_70658_aO();
                this.func_184849_a(IScoreCriteria.field_186700_j, MathHelper.func_76123_f((float) this.field_184854_bX));
            }

            if (this.field_71067_cb != this.field_184856_bZ) {
                this.field_184856_bZ = this.field_71067_cb;
                this.func_184849_a(IScoreCriteria.field_186701_k, MathHelper.func_76123_f((float) this.field_184856_bZ));
            }

            if (this.field_71068_ca != this.field_184855_bY) {
                this.field_184855_bY = this.field_71068_ca;
                this.func_184849_a(IScoreCriteria.field_186702_l, MathHelper.func_76123_f((float) this.field_184855_bY));
            }

            if (this.field_71067_cb != this.field_71144_ck) {
                this.field_71144_ck = this.field_71067_cb;
                this.field_71135_a.func_147359_a(new SPacketSetExperience(this.field_71106_cc, this.field_71067_cb, this.field_71068_ca));
            }

            if (this.field_70173_aa % 20 == 0) {
                CriteriaTriggers.field_192135_o.func_192215_a(this);
            }

            // CraftBukkit start - initialize oldLevel and fire PlayerLevelChangeEvent
            if (this.oldLevel == -1) {
                this.oldLevel = this.field_71068_ca;
            }

            if (this.oldLevel != this.field_71068_ca) {
                CraftEventFactory.callPlayerLevelChangeEvent(this.field_70170_p.getServer().getPlayer((EntityPlayerMP) this), this.oldLevel, this.field_71068_ca);
                this.oldLevel = this.field_71068_ca;
            }
            // CraftBukkit end
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.func_85055_a(throwable, "Ticking player");
            CrashReportCategory crashreportsystemdetails = crashreport.func_85058_a("Player being ticked");

            this.func_85029_a(crashreportsystemdetails);
            throw new ReportedException(crashreport);
        }
    }

    private void func_184849_a(IScoreCriteria iscoreboardcriteria, int i) {
        Collection collection = this.field_70170_p.getServer().getScoreboardManager().getScoreboardScores(iscoreboardcriteria, this.func_70005_c_(), new java.util.ArrayList<Score>()); // CraftBukkit - Use our scores instead
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            Score scoreboardscore = (Score) iterator.next(); // CraftBukkit - Use our scores instead

            scoreboardscore.func_96647_c(i);
        }

    }

    public void func_70645_a(DamageSource damagesource) {
        boolean flag = this.field_70170_p.func_82736_K().func_82766_b("showDeathMessages");

        this.field_71135_a.func_147359_a(new SPacketCombatEvent(this.func_110142_aN(), SPacketCombatEvent.Event.ENTITY_DIED, flag));
        // CraftBukkit start - fire PlayerDeathEvent
        if (this.field_70128_L) {
            return;
        }
        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>(this.field_71071_by.func_70302_i_());
        boolean keepInventory = this.field_70170_p.func_82736_K().func_82766_b("keepInventory") || this.func_175149_v();

        if (!keepInventory) {
            for (ItemStack item : this.field_71071_by.getContents()) {
                if (!item.func_190926_b() && !EnchantmentHelper.func_190939_c(item)) {
                    loot.add(CraftItemStack.asCraftMirror(item));
                }
            }
        }

        ITextComponent chatmessage = this.func_110142_aN().func_151521_b();

        String deathmessage = chatmessage.func_150260_c();
        org.bukkit.event.entity.PlayerDeathEvent event = CraftEventFactory.callPlayerDeathEvent(this, loot, deathmessage, keepInventory);

        String deathMessage = event.getDeathMessage();

        if (deathMessage != null && deathMessage.length() > 0 && flag) { // TODO: allow plugins to override?
            if (deathMessage.equals(deathmessage)) {
                Team scoreboardteambase = this.func_96124_cp();

                if (scoreboardteambase != null && scoreboardteambase.func_178771_j() != Team.EnumVisible.ALWAYS) {
                    if (scoreboardteambase.func_178771_j() == Team.EnumVisible.HIDE_FOR_OTHER_TEAMS) {
                        this.field_71133_b.func_184103_al().func_177453_a((EntityPlayer) this, chatmessage);
                    } else if (scoreboardteambase.func_178771_j() == Team.EnumVisible.HIDE_FOR_OWN_TEAM) {
                        this.field_71133_b.func_184103_al().func_177452_b((EntityPlayer) this, chatmessage);
                    }
                } else {
                    this.field_71133_b.func_184103_al().func_148539_a(chatmessage);
                }
            } else {
                this.field_71133_b.func_184103_al().sendMessage(org.bukkit.craftbukkit.util.CraftChatMessage.fromString(deathMessage));
            }
        }

        this.func_192030_dh();
        // we clean the player's inventory after the EntityDeathEvent is called so plugins can get the exact state of the inventory.
        if (!event.getKeepInventory()) {
            this.field_71071_by.func_174888_l();
        }

        this.func_71053_j();
        this.func_175399_e(this); // Remove spectated target
        // CraftBukkit end

        // CraftBukkit - Get our scores instead
        Collection collection = this.field_70170_p.getServer().getScoreboardManager().getScoreboardScores(IScoreCriteria.field_96642_c, this.func_70005_c_(), new java.util.ArrayList<Score>());
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            Score scoreboardscore = (Score) iterator.next(); // CraftBukkit - Use our scores instead

            scoreboardscore.func_96648_a();
        }

        EntityLivingBase entityliving = this.func_94060_bK();

        if (entityliving != null) {
            EntityList.EntityEggInfo entitytypes_monsteregginfo = (EntityList.EntityEggInfo) EntityList.field_75627_a.get(EntityList.func_191301_a((Entity) entityliving));

            if (entitytypes_monsteregginfo != null) {
                this.func_71029_a(entitytypes_monsteregginfo.field_151513_e);
            }

            entityliving.func_191956_a(this, this.field_70744_aE, damagesource);
        }

        this.func_71029_a(StatList.field_188069_A);
        this.func_175145_a(StatList.field_188098_h);
        this.func_70066_B();
        this.func_70052_a(0, false);
        this.func_110142_aN().func_94549_h();
    }

    public void func_191956_a(Entity entity, int i, DamageSource damagesource) {
        if (entity != this) {
            super.func_191956_a(entity, i, damagesource);
            this.func_85039_t(i);
            // CraftBukkit - Get our scores instead
            Collection<Score> collection = this.field_70170_p.getServer().getScoreboardManager().getScoreboardScores(IScoreCriteria.field_96640_e, this.func_70005_c_(), new java.util.ArrayList<Score>());

            if (entity instanceof EntityPlayer) {
                this.func_71029_a(StatList.field_75932_A);
                // CraftBukkit - Get our scores instead
                this.field_70170_p.getServer().getScoreboardManager().getScoreboardScores(IScoreCriteria.field_96639_d, this.func_70005_c_(), collection);
                // collection.addAll(this.getScoreboard().getObjectivesForCriteria(IScoreboardCriteria.e));
                // CraftBukkit end
            } else {
                this.func_71029_a(StatList.field_188070_B);
            }

            collection.addAll(this.func_192038_E(entity));
            Iterator<Score> iterator = collection.iterator(); // CraftBukkit

            while (iterator.hasNext()) {
                // CraftBukkit start
                // ScoreboardObjective scoreboardobjective = (ScoreboardObjective) iterator.next();

                // this.getScoreboard().getPlayerScoreForObjective(this.getName(), scoreboardobjective).incrementScore();
                iterator.next().func_96648_a();
                // CraftBukkit end
            }

            CriteriaTriggers.field_192122_b.func_192211_a(this, entity, damagesource);
        }
    }

    private Collection<Score> func_192038_E(Entity entity) { // CraftBukkit
        String s = entity instanceof EntityPlayer ? entity.func_70005_c_() : entity.func_189512_bd();
        ScorePlayerTeam scoreboardteam = this.func_96123_co().func_96509_i(this.func_70005_c_());

        if (scoreboardteam != null) {
            int i = scoreboardteam.func_178775_l().func_175746_b();

            if (i >= 0 && i < IScoreCriteria.field_178793_i.length) {
                Iterator iterator = this.func_96123_co().func_96520_a(IScoreCriteria.field_178793_i[i]).iterator();

                while (iterator.hasNext()) {
                    ScoreObjective scoreboardobjective = (ScoreObjective) iterator.next();
                    Score scoreboardscore = this.func_96123_co().func_96529_a(s, scoreboardobjective);

                    scoreboardscore.func_96648_a();
                }
            }
        }

        ScorePlayerTeam scoreboardteam1 = this.func_96123_co().func_96509_i(s);

        if (scoreboardteam1 != null) {
            int j = scoreboardteam1.func_178775_l().func_175746_b();

            if (j >= 0 && j < IScoreCriteria.field_178792_h.length) {
                // CraftBukkit - Get our scores instead
                return this.field_70170_p.getServer().getScoreboardManager().getScoreboardScores(IScoreCriteria.field_178792_h[j], this.func_70005_c_(), new java.util.ArrayList<Score>());
                // return this.getScoreboard().getObjectivesForCriteria(IScoreboardCriteria.m[j]);
                // CraftBukkit end
            }
        }

        return Lists.newArrayList();
    }

    public boolean func_70097_a(DamageSource damagesource, float f) {
        if (this.func_180431_b(damagesource)) {
            return false;
        } else {
            boolean flag = this.field_71133_b.func_71262_S() && this.func_175400_cq() && "fall".equals(damagesource.field_76373_n);

            if (!flag && this.field_147101_bU > 0 && damagesource != DamageSource.field_76380_i) {
                return false;
            } else {
                if (damagesource instanceof EntityDamageSource) {
                    Entity entity = damagesource.func_76346_g();

                    if (entity instanceof EntityPlayer && !this.func_96122_a((EntityPlayer) entity)) {
                        return false;
                    }

                    if (entity instanceof EntityArrow) {
                        EntityArrow entityarrow = (EntityArrow) entity;

                        if (entityarrow.field_70250_c instanceof EntityPlayer && !this.func_96122_a((EntityPlayer) entityarrow.field_70250_c)) {
                            return false;
                        }
                    }
                }

                return super.func_70097_a(damagesource, f);
            }
        }
    }

    public boolean func_96122_a(EntityPlayer entityhuman) {
        return !this.func_175400_cq() ? false : super.func_96122_a(entityhuman);
    }

    private boolean func_175400_cq() {
        // CraftBukkit - this.server.getPvP() -> this.world.pvpMode
        return this.field_70170_p.pvpMode;
    }

    @Nullable
    public Entity func_184204_a(int i) {
        if (this.func_70608_bn()) return this; // CraftBukkit - SPIGOT-3154
        // this.worldChangeInvuln = true; // CraftBukkit - Moved down and into PlayerList#changeDimension
        if (this.field_71093_bK == 0 && i == -1) {
            this.field_193110_cw = new Vec3d(this.field_70165_t, this.field_70163_u, this.field_70161_v);
        } else if (this.field_71093_bK != -1 && i != 0) {
            this.field_193110_cw = null;
        }

        if (this.field_71093_bK == 1 && i == 1) {
            this.field_184851_cj = true; // CraftBukkit - Moved down from above
            this.field_70170_p.func_72900_e(this);
            if (!this.field_71136_j) {
                this.field_71136_j = true;
                if (field_70170_p.paperConfig.disableEndCredits) this.setHasSeenCredits(true); // Paper - Toggle to always disable end credits
                this.field_71135_a.func_147359_a(new SPacketChangeGameState(4, this.field_192040_cp ? 0.0F : 1.0F));
                this.field_192040_cp = true;
            }

            return this;
        } else {
            if (this.field_71093_bK == 0 && i == 1) {
                i = 1;
            }

            // CraftBukkit start
            TeleportCause cause = (this.field_71093_bK == 1 || i == 1) ? TeleportCause.END_PORTAL : TeleportCause.NETHER_PORTAL;
            this.field_71133_b.func_184103_al().changeDimension(this, i, cause); // PAIL: check all this
            // CraftBukkit end
            this.field_71135_a.func_147359_a(new SPacketEffect(1032, BlockPos.field_177992_a, 0, false));
            this.field_71144_ck = -1;
            this.field_71149_ch = -1.0F;
            this.field_71146_ci = -1;
            return this;
        }
    }

    public boolean func_174827_a(EntityPlayerMP entityplayer) {
        return entityplayer.func_175149_v() ? this.func_175398_C() == this : (this.func_175149_v() ? false : super.func_174827_a(entityplayer));
    }

    private void func_147097_b(TileEntity tileentity) {
        if (tileentity != null) {
            SPacketUpdateTileEntity packetplayouttileentitydata = tileentity.func_189518_D_();

            if (packetplayouttileentitydata != null) {
                this.field_71135_a.func_147359_a(packetplayouttileentitydata);
            }
        }

    }

    public void func_71001_a(Entity entity, int i) {
        super.func_71001_a(entity, i);
        this.field_71070_bA.func_75142_b();
    }

    public EntityPlayer.SleepResult func_180469_a(BlockPos blockposition) {
        EntityPlayer.SleepResult entityhuman_enumbedresult = super.func_180469_a(blockposition);

        if (entityhuman_enumbedresult == EntityPlayer.SleepResult.OK) {
            this.func_71029_a(StatList.field_188064_ad);
            SPacketUseBed packetplayoutbed = new SPacketUseBed(this, blockposition);

            this.func_71121_q().func_73039_n().func_151247_a((Entity) this, (Packet) packetplayoutbed);
            this.field_71135_a.func_147364_a(this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_70177_z, this.field_70125_A);
            this.field_71135_a.func_147359_a(packetplayoutbed);
            CriteriaTriggers.field_192136_p.func_192215_a(this);
        }

        return entityhuman_enumbedresult;
    }

    public void func_70999_a(boolean flag, boolean flag1, boolean flag2) {
        if (!this.field_71083_bS) return; // CraftBukkit - Can't leave bed if not in one!
        if (this.func_70608_bn()) {
            this.func_71121_q().func_73039_n().func_151248_b(this, new SPacketAnimation(this, 2));
        }

        super.func_70999_a(flag, flag1, flag2);
        if (this.field_71135_a != null) {
            this.field_71135_a.func_147364_a(this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_70177_z, this.field_70125_A);
        }

    }

    public boolean func_184205_a(Entity entity, boolean flag) {
        Entity entity1 = this.func_184187_bx();

        if (!super.func_184205_a(entity, flag)) {
            return false;
        } else {
            Entity entity2 = this.func_184187_bx();

            if (entity2 != entity1 && this.field_71135_a != null) {
                this.field_71135_a.func_147364_a(this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_70177_z, this.field_70125_A);
            }

            return true;
        }
    }

    public void func_184210_p() {
        Entity entity = this.func_184187_bx();

        super.func_184210_p();
        Entity entity1 = this.func_184187_bx();

        if (entity1 != entity && this.field_71135_a != null) {
            this.field_71135_a.func_147364_a(this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_70177_z, this.field_70125_A);
        }
        // Paper start - "Fixes" an issue in which the vehicle player would not be notified that the passenger dismounted
        if (entity instanceof EntityPlayerMP) {
            WorldServer worldServer = (WorldServer) entity.func_130014_f_();
            worldServer.field_73062_L.func_72790_b(this);
            worldServer.field_73062_L.func_72786_a(this);
        }
        // Paper end

    }

    public boolean func_180431_b(DamageSource damagesource) {
        return super.func_180431_b(damagesource) || this.func_184850_K();
    }

    protected void func_184231_a(double d0, boolean flag, IBlockState iblockdata, BlockPos blockposition) {}

    protected void func_184594_b(BlockPos blockposition) {
        if (!this.func_175149_v()) {
            super.func_184594_b(blockposition);
        }

    }

    public void func_71122_b(double d0, boolean flag) {
        int i = MathHelper.func_76128_c(this.field_70165_t);
        int j = MathHelper.func_76128_c(this.field_70163_u - 0.20000000298023224D);
        int k = MathHelper.func_76128_c(this.field_70161_v);
        BlockPos blockposition = new BlockPos(i, j, k);
        IBlockState iblockdata = this.field_70170_p.func_180495_p(blockposition);

        if (iblockdata.func_185904_a() == Material.field_151579_a) {
            BlockPos blockposition1 = blockposition.func_177977_b();
            IBlockState iblockdata1 = this.field_70170_p.func_180495_p(blockposition1);
            Block block = iblockdata1.func_177230_c();

            if (block instanceof BlockFence || block instanceof BlockWall || block instanceof BlockFenceGate) {
                blockposition = blockposition1;
                iblockdata = iblockdata1;
            }
        }

        super.func_184231_a(d0, flag, iblockdata, blockposition);
    }

    public void func_175141_a(TileEntitySign tileentitysign) {
        tileentitysign.func_145912_a((EntityPlayer) this);
        this.field_71135_a.func_147359_a(new SPacketSignEditorOpen(tileentitysign.func_174877_v()));
    }

    public int nextContainerCounter() { // CraftBukkit - void -> int
        this.field_71139_cq = this.field_71139_cq % 100 + 1;
        return field_71139_cq; // CraftBukkit
    }

    public void func_180468_a(IInteractionObject itileentitycontainer) {
        // CraftBukkit start - Inventory open hook
        if (false && itileentitycontainer instanceof ILootContainer && ((ILootContainer) itileentitycontainer).func_184276_b() != null && this.func_175149_v()) {
            this.func_146105_b((new TextComponentTranslation("container.spectatorCantOpen", new Object[0])).func_150255_a((new Style()).func_150238_a(TextFormatting.RED)), true);
        } else {
            boolean cancelled = itileentitycontainer instanceof ILootContainer && ((ILootContainer) itileentitycontainer).func_184276_b() != null && this.func_175149_v();
            Container container = CraftEventFactory.callInventoryOpenEvent(this, itileentitycontainer.func_174876_a(this.field_71071_by, this), cancelled);
            if (container == null) {
                return;
            }
            this.nextContainerCounter();
            this.field_71070_bA = container;
            this.field_71135_a.func_147359_a(new SPacketOpenWindow(this.field_71139_cq, itileentitycontainer.func_174875_k(), itileentitycontainer.func_145748_c_()));
            // CraftBukkit end
            this.field_71070_bA.field_75152_c = this.field_71139_cq;
            this.field_71070_bA.func_75132_a(this);
        }
    }

    public void func_71007_a(IInventory iinventory) {
        // CraftBukkit start - Inventory open hook
        // Copied from below
        boolean cancelled = false;
        if (iinventory instanceof ILockableContainer) {
            ILockableContainer itileinventory = (ILockableContainer) iinventory;
            cancelled = itileinventory.func_174893_q_() && !this.func_175146_a(itileinventory.func_174891_i()) && !this.func_175149_v();
        }

        Container container;
        if (iinventory instanceof IInteractionObject) {
            if (iinventory instanceof TileEntity) {
                Preconditions.checkArgument(((TileEntity) iinventory).func_145831_w() != null, "Container must have world to be opened");
            }
            container = ((IInteractionObject) iinventory).func_174876_a(this.field_71071_by, this);
        } else {
            container = new ContainerChest(this.field_71071_by, iinventory, this);
        }
        container = CraftEventFactory.callInventoryOpenEvent(this, container, cancelled);
        if (container == null && !cancelled) { // Let pre-cancelled events fall through
            iinventory.func_174886_c(this);
            return;
        }
        // CraftBukkit end

        if (iinventory instanceof ILootContainer && ((ILootContainer) iinventory).func_184276_b() != null && this.func_175149_v()) {
            this.func_146105_b((new TextComponentTranslation("container.spectatorCantOpen", new Object[0])).func_150255_a((new Style()).func_150238_a(TextFormatting.RED)), true);
        } else {
            if (this.field_71070_bA != this.field_71069_bz) {
                this.func_71053_j();
            }

            if (iinventory instanceof ILockableContainer) {
                ILockableContainer itileinventory = (ILockableContainer) iinventory;

                if (itileinventory.func_174893_q_() && !this.func_175146_a(itileinventory.func_174891_i()) && !this.func_175149_v()) {
                    this.field_71135_a.func_147359_a(new SPacketChat(new TextComponentTranslation("container.isLocked", new Object[] { iinventory.func_145748_c_()}), ChatType.GAME_INFO));
                    this.field_71135_a.func_147359_a(new SPacketSoundEffect(SoundEvents.field_187654_U, SoundCategory.BLOCKS, this.field_70165_t, this.field_70163_u, this.field_70161_v, 1.0F, 1.0F));
                    iinventory.func_174886_c(this); // CraftBukkit
                    return;
                }
            }

            this.nextContainerCounter();
            // CraftBukkit start
            if (iinventory instanceof IInteractionObject) {
                this.field_71070_bA = container;
                this.field_71135_a.func_147359_a(new SPacketOpenWindow(this.field_71139_cq, ((IInteractionObject) iinventory).func_174875_k(), iinventory.func_145748_c_(), iinventory.func_70302_i_()));
            } else {
                this.field_71070_bA = container;
                this.field_71135_a.func_147359_a(new SPacketOpenWindow(this.field_71139_cq, "minecraft:container", iinventory.func_145748_c_(), iinventory.func_70302_i_()));
            }
            // CraftBukkit end

            this.field_71070_bA.field_75152_c = this.field_71139_cq;
            this.field_71070_bA.func_75132_a(this);
        }
    }

    public void func_180472_a(IMerchant imerchant) {
        // CraftBukkit start - Inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerMerchant(this.field_71071_by, imerchant, this.field_70170_p));
        if (container == null) {
            return;
        }
        // CraftBukkit end
        this.nextContainerCounter();
        this.field_71070_bA = container; // CraftBukkit
        this.field_71070_bA.field_75152_c = this.field_71139_cq;
        this.field_71070_bA.func_75132_a(this);
        InventoryMerchant inventorymerchant = ((ContainerMerchant) this.field_71070_bA).func_75174_d();
        ITextComponent ichatbasecomponent = imerchant.func_145748_c_();

        this.field_71135_a.func_147359_a(new SPacketOpenWindow(this.field_71139_cq, "minecraft:villager", ichatbasecomponent, inventorymerchant.func_70302_i_()));
        MerchantRecipeList merchantrecipelist = imerchant.func_70934_b(this);

        if (merchantrecipelist != null) {
            PacketBuffer packetdataserializer = new PacketBuffer(Unpooled.buffer());

            packetdataserializer.writeInt(this.field_71139_cq);
            merchantrecipelist.func_151391_a(packetdataserializer);
            this.field_71135_a.func_147359_a(new SPacketCustomPayload("MC|TrList", packetdataserializer));
        }

    }

    public void func_184826_a(AbstractHorse entityhorseabstract, IInventory iinventory) {
        // CraftBukkit start - Inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerHorseInventory(this.field_71071_by, iinventory, entityhorseabstract, this));
        if (container == null) {
            iinventory.func_174886_c(this);
            return;
        }
        // CraftBukkit end
        if (this.field_71070_bA != this.field_71069_bz) {
            this.func_71053_j();
        }

        this.nextContainerCounter();
        this.field_71135_a.func_147359_a(new SPacketOpenWindow(this.field_71139_cq, "EntityHorse", iinventory.func_145748_c_(), iinventory.func_70302_i_(), entityhorseabstract.func_145782_y()));
        this.field_71070_bA = container; // CraftBukkit
        this.field_71070_bA.field_75152_c = this.field_71139_cq;
        this.field_71070_bA.func_75132_a(this);
    }

    public void func_184814_a(ItemStack itemstack, EnumHand enumhand) {
        Item item = itemstack.func_77973_b();

        if (item == Items.field_151164_bB) {
            PacketBuffer packetdataserializer = new PacketBuffer(Unpooled.buffer());

            packetdataserializer.func_179249_a((Enum) enumhand);
            this.field_71135_a.func_147359_a(new SPacketCustomPayload("MC|BOpen", packetdataserializer));
        }

    }

    public void func_184824_a(TileEntityCommandBlock tileentitycommand) {
        tileentitycommand.func_184252_d(true);
        this.func_147097_b((TileEntity) tileentitycommand);
    }

    public void func_71111_a(Container container, int i, ItemStack itemstack) {
        if (!(container.func_75139_a(i) instanceof SlotCrafting)) {
            if (container == this.field_71069_bz) {
                CriteriaTriggers.field_192125_e.func_192208_a(this, this.field_71071_by);
            }

            if (!this.field_71137_h) {
                this.field_71135_a.func_147359_a(new SPacketSetSlot(container.field_75152_c, i, itemstack));
            }
        }
    }

    public void func_71120_a(Container container) {
        this.func_71110_a(container, container.func_75138_a());
    }

    public void func_71110_a(Container container, NonNullList<ItemStack> nonnulllist) {
        this.field_71135_a.func_147359_a(new SPacketWindowItems(container.field_75152_c, nonnulllist));
        this.field_71135_a.func_147359_a(new SPacketSetSlot(-1, -1, this.field_71071_by.func_70445_o()));
        // CraftBukkit start - Send a Set Slot to update the crafting result slot
        if (java.util.EnumSet.of(InventoryType.CRAFTING,InventoryType.WORKBENCH).contains(container.getBukkitView().getType())) {
            this.field_71135_a.func_147359_a(new SPacketSetSlot(container.field_75152_c, 0, container.func_75139_a(0).func_75211_c()));
        }
        // CraftBukkit end
    }

    public void func_71112_a(Container container, int i, int j) {
        this.field_71135_a.func_147359_a(new SPacketWindowProperty(container.field_75152_c, i, j));
    }

    public void func_175173_a(Container container, IInventory iinventory) {
        for (int i = 0; i < iinventory.func_174890_g(); ++i) {
            this.field_71135_a.func_147359_a(new SPacketWindowProperty(container.field_75152_c, i, iinventory.func_174887_a_(i)));
        }

    }

    public void func_71053_j() {
        CraftEventFactory.handleInventoryCloseEvent(this); // CraftBukkit
        this.field_71135_a.func_147359_a(new SPacketCloseWindow(this.field_71070_bA.field_75152_c));
        this.func_71128_l();
    }

    public void func_71113_k() {
        if (!this.field_71137_h) {
            this.field_71135_a.func_147359_a(new SPacketSetSlot(-1, -1, this.field_71071_by.func_70445_o()));
        }
    }

    public void func_71128_l() {
        this.field_71070_bA.func_75134_a((EntityPlayer) this);
        this.field_71070_bA = this.field_71069_bz;
    }

    public void func_110430_a(float f, float f1, boolean flag, boolean flag1) {
        if (this.func_184218_aH()) {
            if (f >= -1.0F && f <= 1.0F) {
                this.field_70702_br = f;
            }

            if (f1 >= -1.0F && f1 <= 1.0F) {
                this.field_191988_bg = f1;
            }

            this.field_70703_bu = flag;
            this.func_70095_a(flag1);
        }

    }

    public void func_71064_a(StatBase statistic, int i) {
        if (statistic != null) {
            this.field_147103_bO.func_150871_b(this, statistic, i);
            Iterator iterator = this.func_96123_co().func_96520_a(statistic.func_150952_k()).iterator();

            while (iterator.hasNext()) {
                ScoreObjective scoreboardobjective = (ScoreObjective) iterator.next();

                this.func_96123_co().func_96529_a(this.func_70005_c_(), scoreboardobjective).func_96649_a(i);
            }

        }
    }

    public void func_175145_a(StatBase statistic) {
        if (statistic != null) {
            this.field_147103_bO.func_150873_a(this, statistic, 0);
            Iterator iterator = this.func_96123_co().func_96520_a(statistic.func_150952_k()).iterator();

            while (iterator.hasNext()) {
                ScoreObjective scoreboardobjective = (ScoreObjective) iterator.next();

                this.func_96123_co().func_96529_a(this.func_70005_c_(), scoreboardobjective).func_96647_c(0);
            }

        }
    }

    public void func_192021_a(List<IRecipe> list) {
        this.field_192041_cq.func_193835_a(list, this);
    }

    public void func_193102_a(ResourceLocation[] aminecraftkey) {
        ArrayList arraylist = Lists.newArrayList();
        ResourceLocation[] aminecraftkey1 = aminecraftkey;
        int i = aminecraftkey.length;

        for (int j = 0; j < i; ++j) {
            ResourceLocation minecraftkey = aminecraftkey1[j];

            // CraftBukkit start
            if (CraftingManager.func_193373_a(minecraftkey) == null) {
                Bukkit.getLogger().warning("Ignoring grant of non existent recipe " + minecraftkey);
                continue;
            }
            // CraftBukit end
            arraylist.add(CraftingManager.func_193373_a(minecraftkey));
        }

        this.func_192021_a((List<IRecipe>) arraylist); // CraftBukkit - decompile error
    }

    public void func_192022_b(List<IRecipe> list) {
        this.field_192041_cq.func_193834_b(list, this);
    }

    public void func_71123_m() {
        this.field_193109_cv = true;
        this.func_184226_ay();
        if (this.field_71083_bS) {
            this.func_70999_a(true, false, false);
        }

    }

    public boolean func_193105_t() {
        return this.field_193109_cv;
    }

    public void func_71118_n() {
        this.field_71149_ch = -1.0E8F;
        this.field_71144_ck = -1; // CraftBukkit - Added to reset
    }

    // CraftBukkit start - Support multi-line messages
    public void sendMessage(ITextComponent[] ichatbasecomponent) {
        for (ITextComponent component : ichatbasecomponent) {
            this.func_145747_a(component);
        }
    }
    // CraftBukkit end

    public void func_146105_b(ITextComponent ichatbasecomponent, boolean flag) {
        this.field_71135_a.func_147359_a(new SPacketChat(ichatbasecomponent, flag ? ChatType.GAME_INFO : ChatType.CHAT));
    }

    protected void func_71036_o() {
        if (!this.field_184627_bm.func_190926_b() && this.func_184587_cr()) {
            this.field_71135_a.func_147359_a(new SPacketEntityStatus(this, (byte) 9));
            super.func_71036_o();
        }

    }

    public void func_193104_a(EntityPlayerMP entityplayer, boolean flag) {
        if (flag) {
            this.field_71071_by.func_70455_b(entityplayer.field_71071_by);
            this.func_70606_j(entityplayer.func_110143_aJ());
            this.field_71100_bB = entityplayer.field_71100_bB;
            this.field_71068_ca = entityplayer.field_71068_ca;
            this.field_71067_cb = entityplayer.field_71067_cb;
            this.field_71106_cc = entityplayer.field_71106_cc;
            this.func_85040_s(entityplayer.func_71037_bA());
            this.field_181016_an = entityplayer.field_181016_an;
            this.field_181017_ao = entityplayer.field_181017_ao;
            this.field_181018_ap = entityplayer.field_181018_ap;
        } else if (this.field_70170_p.func_82736_K().func_82766_b("keepInventory") || entityplayer.func_175149_v()) {
            this.field_71071_by.func_70455_b(entityplayer.field_71071_by);
            this.field_71068_ca = entityplayer.field_71068_ca;
            this.field_71067_cb = entityplayer.field_71067_cb;
            this.field_71106_cc = entityplayer.field_71106_cc;
            this.func_85040_s(entityplayer.func_71037_bA());
        }

        this.field_175152_f = entityplayer.field_175152_f;
        this.field_71078_a = entityplayer.field_71078_a;
        this.func_184212_Q().func_187227_b(EntityPlayerMP.field_184827_bp, entityplayer.func_184212_Q().func_187225_a(EntityPlayerMP.field_184827_bp));
        this.field_71144_ck = -1;
        this.field_71149_ch = -1.0F;
        this.field_71146_ci = -1;
        // this.cr.a((RecipeBook) entityplayer.cr); // CraftBukkit
        // Paper start - Optimize remove queue
        //this.removeQueue.addAll(entityplayer.removeQueue);
        if (this.field_71130_g != entityplayer.field_71130_g) {
            this.field_71130_g.addAll(entityplayer.field_71130_g);
        }
        this.field_192040_cp = entityplayer.field_192040_cp;
        this.field_193110_cw = entityplayer.field_193110_cw;
        this.func_192029_h(entityplayer.func_192023_dk());
        this.func_192031_i(entityplayer.func_192025_dl());
    }

    protected void func_70670_a(PotionEffect mobeffect) {
        super.func_70670_a(mobeffect);
        this.field_71135_a.func_147359_a(new SPacketEntityEffect(this.func_145782_y(), mobeffect));
        if (mobeffect.func_188419_a() == MobEffects.field_188424_y) {
            this.field_193108_cu = this.field_70173_aa;
            this.field_193107_ct = new Vec3d(this.field_70165_t, this.field_70163_u, this.field_70161_v);
        }

        CriteriaTriggers.field_193139_z.func_193153_a(this);
    }

    protected void func_70695_b(PotionEffect mobeffect, boolean flag) {
        super.func_70695_b(mobeffect, flag);
        this.field_71135_a.func_147359_a(new SPacketEntityEffect(this.func_145782_y(), mobeffect));
        CriteriaTriggers.field_193139_z.func_193153_a(this);
    }

    protected void func_70688_c(PotionEffect mobeffect) {
        super.func_70688_c(mobeffect);
        this.field_71135_a.func_147359_a(new SPacketRemoveEntityEffect(this.func_145782_y(), mobeffect.func_188419_a()));
        if (mobeffect.func_188419_a() == MobEffects.field_188424_y) {
            this.field_193107_ct = null;
        }

        CriteriaTriggers.field_193139_z.func_193153_a(this);
    }

    public void func_70634_a(double d0, double d1, double d2) {
        this.field_71135_a.func_147364_a(d0, d1, d2, this.field_70177_z, this.field_70125_A);
    }

    public void func_71009_b(Entity entity) {
        this.func_71121_q().func_73039_n().func_151248_b(this, new SPacketAnimation(entity, 4));
    }

    public void func_71047_c(Entity entity) {
        this.func_71121_q().func_73039_n().func_151248_b(this, new SPacketAnimation(entity, 5));
    }

    public void func_71016_p() {
        if (this.field_71135_a != null) {
            this.field_71135_a.func_147359_a(new SPacketPlayerAbilities(this.field_71075_bZ));
            this.func_175135_B();
        }
    }

    public WorldServer func_71121_q() {
        return (WorldServer) this.field_70170_p;
    }

    public void func_71033_a(GameType enumgamemode) {
        // CraftBukkit start
        if (enumgamemode == this.field_71134_c.func_73081_b()) {
            return;
        }

        PlayerGameModeChangeEvent event = new PlayerGameModeChangeEvent(getBukkitEntity(), GameMode.getByValue(enumgamemode.func_77148_a()));
        field_70170_p.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        // CraftBukkit end

        this.field_71134_c.func_73076_a(enumgamemode);
        this.field_71135_a.func_147359_a(new SPacketChangeGameState(3, (float) enumgamemode.func_77148_a()));
        if (enumgamemode == GameType.SPECTATOR) {
            this.func_192030_dh();
            this.func_184210_p();
        } else {
            this.func_175399_e(this);
        }

        this.func_71016_p();
        this.func_175136_bO();
    }

    public boolean func_175149_v() {
        return this.field_71134_c.func_73081_b() == GameType.SPECTATOR;
    }

    public boolean func_184812_l_() {
        return this.field_71134_c.func_73081_b() == GameType.CREATIVE;
    }

    public void func_145747_a(ITextComponent ichatbasecomponent) {
        this.field_71135_a.func_147359_a(new SPacketChat(ichatbasecomponent));
    }

    public boolean func_70003_b(int i, String s) {
        /* CraftBukkit start
        if ("seed".equals(s) && !this.server.aa()) {
            return true;
        } else if (!"tell".equals(s) && !"help".equals(s) && !"me".equals(s) && !"trigger".equals(s)) {
            if (this.server.getPlayerList().isOp(this.getProfile())) {
                OpListEntry oplistentry = (OpListEntry) this.server.getPlayerList().getOPs().get(this.getProfile());

                return oplistentry != null ? oplistentry.a() >= i : this.server.q() >= i;
            } else {
                return false;
            }
        } else {
            return true;
        }
        */
        if ("@".equals(s)) {
            return getBukkitEntity().hasPermission("minecraft.command.selector");
        }
        if ("".equals(s)) {
            return getBukkitEntity().isOp();
        }
        return getBukkitEntity().hasPermission("minecraft.command." + s);
        // CraftBukkit end
    }

    public String func_71114_r() {
        String s = this.field_71135_a.field_147371_a.func_74430_c().toString();

        s = s.substring(s.indexOf("/") + 1);
        s = s.substring(0, s.indexOf(":"));
        return s;
    }

    public void func_147100_a(CPacketClientSettings packetplayinsettings) {
        // CraftBukkit start
        if (func_184591_cq() != packetplayinsettings.func_186991_f()) {
            PlayerChangedMainHandEvent event = new PlayerChangedMainHandEvent(getBukkitEntity(), func_184591_cq() == EnumHandSide.LEFT ? MainHand.LEFT : MainHand.RIGHT);
            this.field_71133_b.server.getPluginManager().callEvent(event);
        }

        // Paper start - add PlayerLocaleChangeEvent
        // Since the field is initialized to null, this event should always fire the first time the packet is received
        String oldLocale = this.field_71148_cg;
        this.field_71148_cg = packetplayinsettings.func_149524_c();
        if (!this.field_71148_cg.equals(oldLocale)) {
            new com.destroystokyo.paper.event.player.PlayerLocaleChangeEvent(this.getBukkitEntity(), oldLocale, this.field_71148_cg).callEvent();
        }

        // Compat with Bukkit
        oldLocale = oldLocale != null ? oldLocale : "en_us";
        // Paper end

        if (!oldLocale.equals(packetplayinsettings.func_149524_c())) {
            PlayerLocaleChangeEvent event = new PlayerLocaleChangeEvent(getBukkitEntity(), packetplayinsettings.func_149524_c());
            this.field_71133_b.server.getPluginManager().callEvent(event);
        }
        // CraftBukkit end
        this.field_71143_cn = packetplayinsettings.func_149523_e();
        this.field_71140_co = packetplayinsettings.func_149520_f();
        this.func_184212_Q().func_187227_b(EntityPlayerMP.field_184827_bp, Byte.valueOf((byte) packetplayinsettings.func_149521_d()));
        this.func_184212_Q().func_187227_b(EntityPlayerMP.field_184828_bq, Byte.valueOf((byte) (packetplayinsettings.func_186991_f() == EnumHandSide.LEFT ? 0 : 1)));
    }

    public EntityPlayer.EnumChatVisibility func_147096_v() {
        return this.field_71143_cn;
    }

    public void func_175397_a(String s, String s1) {
        this.field_71135_a.func_147359_a(new SPacketResourcePackSend(s, s1));
    }

    public BlockPos func_180425_c() {
        return new BlockPos(this.field_70165_t, this.field_70163_u + 0.5D, this.field_70161_v);
    }

    public void func_143004_u() {
        this.field_143005_bX = MinecraftServer.func_130071_aq();
    }

    public StatisticsManagerServer func_147099_x() {
        return this.field_147103_bO;
    }

    public RecipeBookServer func_192037_E() {
        return this.field_192041_cq;
    }

    public void func_152339_d(Entity entity) {
        if (entity instanceof EntityPlayer) {
            this.field_71135_a.func_147359_a(new SPacketDestroyEntities(new int[] { entity.func_145782_y()}));
        } else {
            this.field_71130_g.add(Integer.valueOf(entity.func_145782_y()));
        }

    }

    public void func_184848_d(Entity entity) {
        this.field_71130_g.remove(Integer.valueOf(entity.func_145782_y()));
    }

    protected void func_175135_B() {
        if (this.func_175149_v()) {
            this.func_175133_bi();
            this.func_82142_c(true);
        } else {
            super.func_175135_B();
        }

        this.func_71121_q().func_73039_n().func_180245_a(this);
    }

    public Entity func_175398_C() {
        return (Entity) (this.field_175401_bS == null ? this : this.field_175401_bS);
    }

    public void func_175399_e(Entity entity) {
        Entity entity1 = this.func_175398_C();

        this.field_175401_bS = (Entity) (entity == null ? this : entity);
        if (entity1 != this.field_175401_bS) {
            this.field_71135_a.func_147359_a(new SPacketCamera(this.field_175401_bS));
            this.field_71135_a.a(this.field_175401_bS.field_70165_t, this.field_175401_bS.field_70163_u, this.field_175401_bS.field_70161_v, this.field_70177_z, this.field_70125_A, TeleportCause.SPECTATE); // CraftBukkit
        }

    }

    protected void func_184173_H() {
        if (this.field_71088_bW > 0 && !this.field_184851_cj) {
            --this.field_71088_bW;
        }

    }

    public void func_71059_n(Entity entity) {
        if (this.field_71134_c.func_73081_b() == GameType.SPECTATOR) {
            this.func_175399_e(entity);
        } else {
            super.func_71059_n(entity);
        }

    }

    public long func_154331_x() {
        return this.field_143005_bX;
    }

    @Nullable
    public ITextComponent func_175396_E() {
        return listName; // CraftBukkit
    }

    public void func_184609_a(EnumHand enumhand) {
        super.func_184609_a(enumhand);
        this.func_184821_cY();
    }

    public boolean func_184850_K() {
        return this.field_184851_cj;
    }

    public void func_184846_L() {
        this.field_184851_cj = false;
    }

    public void func_184847_M() {
        if (!CraftEventFactory.callToggleGlideEvent(this, true).isCancelled()) // CraftBukkit
        this.func_70052_a(7, true);
    }

    public void func_189103_N() {
        // CraftBukkit start
        if (!CraftEventFactory.callToggleGlideEvent(this, false).isCancelled()) {
        this.func_70052_a(7, true);
        this.func_70052_a(7, false);
        }
        // CraftBukkit end
    }

    public PlayerAdvancements func_192039_O() {
        return this.field_192042_bX;
    }

    @Nullable
    public Vec3d func_193106_Q() {
        return this.field_193110_cw;
    }

    // CraftBukkit start - Add per-player time and weather.
    public long timeOffset = 0;
    public boolean relativeTime = true;

    public long getPlayerTime() {
        if (this.relativeTime) {
            // Adds timeOffset to the current server time.
            return this.field_70170_p.func_72820_D() + this.timeOffset;
        } else {
            // Adds timeOffset to the beginning of this day.
            return this.field_70170_p.func_72820_D() - (this.field_70170_p.func_72820_D() % 24000) + this.timeOffset;
        }
    }

    public WeatherType weather = null;

    public WeatherType getPlayerWeather() {
        return this.weather;
    }

    public void setPlayerWeather(WeatherType type, boolean plugin) {
        if (!plugin && this.weather != null) {
            return;
        }

        if (plugin) {
            this.weather = type;
        }

        if (type == WeatherType.DOWNFALL) {
            this.field_71135_a.func_147359_a(new SPacketChangeGameState(2, 0));
        } else {
            this.field_71135_a.func_147359_a(new SPacketChangeGameState(1, 0));
        }
    }

    private float pluginRainPosition;
    private float pluginRainPositionPrevious;

    public void updateWeather(float oldRain, float newRain, float oldThunder, float newThunder) {
        if (this.weather == null) {
            // Vanilla
            if (oldRain != newRain) {
                this.field_71135_a.func_147359_a(new SPacketChangeGameState(7, newRain));
            }
        } else {
            // Plugin
            if (pluginRainPositionPrevious != pluginRainPosition) {
                this.field_71135_a.func_147359_a(new SPacketChangeGameState(7, pluginRainPosition));
            }
        }

        if (oldThunder != newThunder) {
            if (weather == WeatherType.DOWNFALL || weather == null) {
                this.field_71135_a.func_147359_a(new SPacketChangeGameState(8, newThunder));
            } else {
                this.field_71135_a.func_147359_a(new SPacketChangeGameState(8, 0));
            }
        }
    }

    public void tickWeather() {
        if (this.weather == null) return;

        pluginRainPositionPrevious = pluginRainPosition;
        if (weather == WeatherType.DOWNFALL) {
            pluginRainPosition += 0.01;
        } else {
            pluginRainPosition -= 0.01;
        }

        pluginRainPosition = MathHelper.func_76131_a(pluginRainPosition, 0.0F, 1.0F);
    }

    public void resetPlayerWeather() {
        this.weather = null;
        this.setPlayerWeather(this.field_70170_p.func_72912_H().func_76059_o() ? WeatherType.DOWNFALL : WeatherType.CLEAR, false);
    }

    @Override
    public String toString() {
        return super.toString() + "(" + this.func_70005_c_() + " at " + this.field_70165_t + "," + this.field_70163_u + "," + this.field_70161_v + ")";
    }

    // SPIGOT-1903, MC-98153
    public void forceSetPositionRotation(double x, double y, double z, float yaw, float pitch) {
        this.func_70012_b(x, y, z, yaw, pitch);
        this.field_71135_a.func_184342_d();
    }

    @Override
    protected boolean func_70610_aX() {
        return super.func_70610_aX() || (this.field_71135_a != null && this.field_71135_a.isDisconnected()); // Paper
    }

    @Override
    public Scoreboard func_96123_co() {
        return getBukkitEntity().getScoreboard().getHandle();
    }

    public void reset() {
        float exp = 0;
        boolean keepInventory = this.field_70170_p.func_82736_K().func_82766_b("keepInventory");

        if (this.keepLevel || keepInventory) {
            exp = this.field_71106_cc;
            this.newTotalExp = this.field_71067_cb;
            this.newLevel = this.field_71068_ca;
        }

        this.func_70606_j(this.func_110138_aP());
        this.field_190534_ay = 0;
        this.field_70143_R = 0;
        this.field_71100_bB = new FoodStats(this);
        this.field_71068_ca = this.newLevel;
        this.field_71067_cb = this.newTotalExp;
        this.field_71106_cc = 0;
        this.field_70725_aQ = 0;
        this.func_85034_r(0);
        this.func_70674_bp();
        this.field_70752_e = true;
        this.field_71070_bA = this.field_71069_bz;
        this.field_70717_bb = null;
        this.field_70755_b = null;
        this.field_94063_bt = new CombatTracker(this);
        this.field_71144_ck = -1;
        if (this.keepLevel || keepInventory) {
            this.field_71106_cc = exp;
        } else {
            this.func_71023_q(this.newExp);
        }
        this.keepLevel = false;
    }

    @Override
    public CraftPlayer getBukkitEntity() {
        return (CraftPlayer) super.getBukkitEntity();
    }
    // CraftBukkit end
}
