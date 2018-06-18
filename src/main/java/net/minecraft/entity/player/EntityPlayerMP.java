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

    private static final Logger LOGGER = LogManager.getLogger();
    public String language = null; // PAIL: private -> public // Paper - default to null
    public long lastSave = MinecraftServer.currentTick; // Paper
    public NetHandlerPlayServer connection;
    public final MinecraftServer mcServer;
    public final PlayerInteractionManager interactionManager;
    public double managedPosX;
    public double managedPosZ;
    public final Deque<Integer> entityRemoveQueue = new ArrayDeque<>(); // Paper
    private final PlayerAdvancements advancements;
    private final StatisticsManagerServer statsFile;
    private float lastHealthScore = Float.MIN_VALUE;
    private int lastFoodScore = Integer.MIN_VALUE;
    private int lastAirScore = Integer.MIN_VALUE;
    private int lastArmorScore = Integer.MIN_VALUE;
    private int lastLevelScore = Integer.MIN_VALUE;
    private int lastExperienceScore = Integer.MIN_VALUE;
    private float lastHealth = -1.0E8F;
    private int lastFoodLevel = -99999999;
    private boolean wasHungry = true;
    public int lastExperience = -99999999;
    public int respawnInvulnerabilityTicks = 60;
    private EntityPlayer.EnumChatVisibility chatVisibility;
    private boolean chatColours = true;
    private long playerLastActiveTime = System.currentTimeMillis();
    private Entity spectatingEntity;
    public boolean invulnerableDimensionChange;
    private boolean seenCredits; private void setHasSeenCredits(boolean has) { this.seenCredits = has; } // Paper - OBFHELPER
    private final RecipeBookServer recipeBook = new RecipeBookServer();
    private Vec3d levitationStartPos;
    private int levitatingSince;
    private boolean disconnected;
    private Vec3d enteredNetherPosition;
    private int currentWindowId;
    public boolean isChangingQuantityOnly;
    public int ping;
    public boolean queuedEndExit;
    // Paper start - Player view distance API
    private int viewDistance = -1;
    public int getViewDistance() {
        return viewDistance == -1 ? ((WorldServer) world).getPlayerChunkMap().getViewDistance() : viewDistance;
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
        playerinteractmanager.player = this;
        this.interactionManager = playerinteractmanager;
        // CraftBukkit start
        BlockPos blockposition = getSpawnPoint(minecraftserver, worldserver);

        this.mcServer = minecraftserver;
        this.statsFile = minecraftserver.getPlayerList().getStatisticManager(this);
        this.advancements = minecraftserver.getPlayerList().getPlayerAdvancements(this);
        this.stepHeight = 1.0F;
        this.moveToBlockPosAndAngles(blockposition, 0.0F, 0.0F);
        // CraftBukkit end

        while (!worldserver.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && this.posY < 255.0D) {
            this.setPosition(this.posX, this.posY + 1.0D, this.posZ);
        }

        // CraftBukkit start
        this.displayName = this.getName();
        this.canPickUpLoot = true;
        this.maxHealthCache = this.getMaxHealth();
        // CraftBukkit end
    }

    public final BlockPos getSpawnPoint(MinecraftServer minecraftserver, WorldServer worldserver) {
        BlockPos blockposition = worldserver.getSpawnPoint();

        if (worldserver.provider.hasSkyLight() && worldserver.getWorldInfo().getGameType() != GameType.ADVENTURE) {
            int i = Math.max(0, minecraftserver.getSpawnRadius(worldserver));
            int j = MathHelper.floor(worldserver.getWorldBorder().getClosestDistance((double) blockposition.getX(), (double) blockposition.getZ()));

            if (j < i) {
                i = j;
            }

            if (j <= 1) {
                i = 1;
            }

            blockposition = worldserver.getTopSolidOrLiquidBlock(blockposition.add(this.rand.nextInt(i * 2 + 1) - i, 0, this.rand.nextInt(i * 2 + 1) - i));
        }

        return blockposition;
    }
    // CraftBukkit end

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        if (this.posY > 300) this.posY = 257; // Paper - bring down to a saner Y level if out of world
        if (nbttagcompound.hasKey("playerGameType", 99)) {
            if (this.getServer().getForceGamemode()) {
                this.interactionManager.setGameType(this.getServer().getGameType());
            } else {
                this.interactionManager.setGameType(GameType.getByID(nbttagcompound.getInteger("playerGameType")));
            }
        }

        if (nbttagcompound.hasKey("enteredNetherPosition", 10)) {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("enteredNetherPosition");

            this.enteredNetherPosition = new Vec3d(nbttagcompound1.getDouble("x"), nbttagcompound1.getDouble("y"), nbttagcompound1.getDouble("z"));
        }

        this.seenCredits = nbttagcompound.getBoolean("seenCredits");
        if (nbttagcompound.hasKey("recipeBook", 10)) {
            this.recipeBook.read(nbttagcompound.getCompoundTag("recipeBook"));
        }
        this.getBukkitEntity().readExtraData(nbttagcompound); // CraftBukkit

    }

    public static void registerFixesPlayerMP(DataFixer dataconvertermanager) {
        dataconvertermanager.registerWalker(FixTypes.PLAYER, new IDataWalker() {
            public NBTTagCompound process(IDataFixer dataconverter, NBTTagCompound nbttagcompound, int i) {
                if (nbttagcompound.hasKey("RootVehicle", 10)) {
                    NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("RootVehicle");

                    if (nbttagcompound1.hasKey("Entity", 10)) {
                        nbttagcompound1.setTag("Entity", dataconverter.process(FixTypes.ENTITY, nbttagcompound1.getCompoundTag("Entity"), i));
                    }
                }

                return nbttagcompound;
            }
        });
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("playerGameType", this.interactionManager.getGameType().getID());
        nbttagcompound.setBoolean("seenCredits", this.seenCredits);
        if (this.enteredNetherPosition != null) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            nbttagcompound1.setDouble("x", this.enteredNetherPosition.x);
            nbttagcompound1.setDouble("y", this.enteredNetherPosition.y);
            nbttagcompound1.setDouble("z", this.enteredNetherPosition.z);
            nbttagcompound.setTag("enteredNetherPosition", nbttagcompound1);
        }

        Entity entity = this.getLowestRidingEntity();
        Entity entity1 = this.getRidingEntity();

        if (entity1 != null && entity != this && entity.getRecursivePassengersByType(EntityPlayerMP.class).size() == 1) {
            NBTTagCompound nbttagcompound2 = new NBTTagCompound();
            NBTTagCompound nbttagcompound3 = new NBTTagCompound();

            entity.writeToNBTOptional(nbttagcompound3);
            nbttagcompound2.setUniqueId("Attach", entity1.getUniqueID());
            nbttagcompound2.setTag("Entity", nbttagcompound3);
            nbttagcompound.setTag("RootVehicle", nbttagcompound2);
        }

        nbttagcompound.setTag("recipeBook", this.recipeBook.write());
        this.getBukkitEntity().setExtraData(nbttagcompound); // CraftBukkit
    }

    // CraftBukkit start - World fallback code, either respawn location or global spawn
    public void setWorld(World world) {
        super.setWorld(world);
        if (world == null) {
            this.isDead = false;
            BlockPos position = null;
            if (this.spawnWorld != null && !this.spawnWorld.equals("")) {
                CraftWorld cworld = (CraftWorld) Bukkit.getServer().getWorld(this.spawnWorld);
                if (cworld != null && this.getBedLocation() != null) {
                    world = cworld.getHandle();
                    position = EntityPlayer.getBedSpawnLocation(cworld.getHandle(), this.getBedLocation(), false);
                }
            }
            if (world == null || position == null) {
                world = ((CraftWorld) Bukkit.getServer().getWorlds().get(0)).getHandle();
                position = world.getSpawnPoint();
            }
            this.world = world;
            this.setPosition(position.getX() + 0.5, position.getY(), position.getZ() + 0.5);
        }
        this.dimension = ((WorldServer) this.world).dimension;
        this.interactionManager.setWorld((WorldServer) world);
    }
    // CraftBukkit end

    public void addExperienceLevel(int i) {
        super.addExperienceLevel(i);
        this.lastExperience = -1;
    }

    public void onEnchant(ItemStack itemstack, int i) {
        super.onEnchant(itemstack, i);
        this.lastExperience = -1;
    }

    public void addSelfToInternalCraftingInventory() {
        this.openContainer.addListener(this);
    }

    public void sendEnterCombat() {
        super.sendEnterCombat();
        this.connection.sendPacket(new SPacketCombatEvent(this.getCombatTracker(), SPacketCombatEvent.Event.ENTER_COMBAT));
    }

    public void sendEndCombat() {
        super.sendEndCombat();
        this.connection.sendPacket(new SPacketCombatEvent(this.getCombatTracker(), SPacketCombatEvent.Event.END_COMBAT));
    }

    protected void onInsideBlock(IBlockState iblockdata) {
        CriteriaTriggers.ENTER_BLOCK.trigger(this, iblockdata);
    }

    protected CooldownTracker createCooldownTracker() {
        return new CooldownTrackerServer(this);
    }

    public void onUpdate() {
        // CraftBukkit start
        if (this.joining) {
            this.joining = false;
        }
        // CraftBukkit end
        this.interactionManager.updateBlockRemoving();
        --this.respawnInvulnerabilityTicks;
        if (this.hurtResistantTime > 0) {
            --this.hurtResistantTime;
        }

        // Paper start - Configurable container update tick rate
        if (--containerUpdateDelay <= 0) {
            this.openContainer.detectAndSendChanges();
            containerUpdateDelay = world.paperConfig.containerUpdateTickRate;
        }
        // Paper end
        if (!this.world.isRemote && !this.openContainer.canInteractWith(this)) {
            this.closeScreen();
            this.openContainer = this.inventoryContainer;
        }

        while (!this.entityRemoveQueue.isEmpty()) {
            int i = Math.min(this.entityRemoveQueue.size(), Integer.MAX_VALUE);
            int[] aint = new int[i];
            Iterator iterator = this.entityRemoveQueue.iterator();
            int j = 0;

            // Paper start
            /* while (iterator.hasNext() && j < i) {
                aint[j++] = ((Integer) iterator.next()).intValue();
                iterator.remove();
            } */

            Integer integer;
            while (j < i && (integer = this.entityRemoveQueue.poll()) != null) {
                aint[j++] = integer.intValue();
            }
            // Paper end

            this.connection.sendPacket(new SPacketDestroyEntities(aint));
        }

        Entity entity = this.getSpectatingEntity();

        if (entity != this) {
            if (entity.isEntityAlive()) {
                this.setPositionAndRotation(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
                this.mcServer.getPlayerList().serverUpdateMovingPlayer(this);
                if (this.isSneaking()) {
                    this.setSpectatingEntity(this);
                }
            } else {
                this.setSpectatingEntity(this);
            }
        }

        CriteriaTriggers.TICK.trigger(this);
        if (this.levitationStartPos != null) {
            CriteriaTriggers.LEVITATION.trigger(this, this.levitationStartPos, this.ticksExisted - this.levitatingSince);
        }

        this.advancements.flushDirty(this);
    }

    public void onUpdateEntity() {
        try {
            super.onUpdate();

            for (int i = 0; i < this.inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = this.inventory.getStackInSlot(i);

                if (!itemstack.isEmpty() && itemstack.getItem().isMap()) {
                    Packet packet = ((ItemMapBase) itemstack.getItem()).createMapDataPacket(itemstack, this.world, (EntityPlayer) this);

                    if (packet != null) {
                        this.connection.sendPacket(packet);
                    }
                }
            }

            if (this.getHealth() != this.lastHealth || this.lastFoodLevel != this.foodStats.getFoodLevel() || this.foodStats.getSaturationLevel() == 0.0F != this.wasHungry) {
                this.connection.sendPacket(new SPacketUpdateHealth(this.getBukkitEntity().getScaledHealth(), this.foodStats.getFoodLevel(), this.foodStats.getSaturationLevel())); // CraftBukkit
                this.lastHealth = this.getHealth();
                this.lastFoodLevel = this.foodStats.getFoodLevel();
                this.wasHungry = this.foodStats.getSaturationLevel() == 0.0F;
            }

            if (this.getHealth() + this.getAbsorptionAmount() != this.lastHealthScore) {
                this.lastHealthScore = this.getHealth() + this.getAbsorptionAmount();
                this.updateScorePoints(IScoreCriteria.HEALTH, MathHelper.ceil(this.lastHealthScore));
            }

            if (this.foodStats.getFoodLevel() != this.lastFoodScore) {
                this.lastFoodScore = this.foodStats.getFoodLevel();
                this.updateScorePoints(IScoreCriteria.FOOD, MathHelper.ceil((float) this.lastFoodScore));
            }

            if (this.getAir() != this.lastAirScore) {
                this.lastAirScore = this.getAir();
                this.updateScorePoints(IScoreCriteria.AIR, MathHelper.ceil((float) this.lastAirScore));
            }

            // CraftBukkit start - Force max health updates
            if (this.maxHealthCache != this.getMaxHealth()) {
                this.getBukkitEntity().updateScaledHealth();
            }
            // CraftBukkit end

            if (this.getTotalArmorValue() != this.lastArmorScore) {
                this.lastArmorScore = this.getTotalArmorValue();
                this.updateScorePoints(IScoreCriteria.ARMOR, MathHelper.ceil((float) this.lastArmorScore));
            }

            if (this.experienceTotal != this.lastExperienceScore) {
                this.lastExperienceScore = this.experienceTotal;
                this.updateScorePoints(IScoreCriteria.XP, MathHelper.ceil((float) this.lastExperienceScore));
            }

            if (this.experienceLevel != this.lastLevelScore) {
                this.lastLevelScore = this.experienceLevel;
                this.updateScorePoints(IScoreCriteria.LEVEL, MathHelper.ceil((float) this.lastLevelScore));
            }

            if (this.experienceTotal != this.lastExperience) {
                this.lastExperience = this.experienceTotal;
                this.connection.sendPacket(new SPacketSetExperience(this.experience, this.experienceTotal, this.experienceLevel));
            }

            if (this.ticksExisted % 20 == 0) {
                CriteriaTriggers.LOCATION.trigger(this);
            }

            // CraftBukkit start - initialize oldLevel and fire PlayerLevelChangeEvent
            if (this.oldLevel == -1) {
                this.oldLevel = this.experienceLevel;
            }

            if (this.oldLevel != this.experienceLevel) {
                CraftEventFactory.callPlayerLevelChangeEvent(this.world.getServer().getPlayer((EntityPlayerMP) this), this.oldLevel, this.experienceLevel);
                this.oldLevel = this.experienceLevel;
            }
            // CraftBukkit end
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking player");
            CrashReportCategory crashreportsystemdetails = crashreport.makeCategory("Player being ticked");

            this.addEntityCrashInfo(crashreportsystemdetails);
            throw new ReportedException(crashreport);
        }
    }

    private void updateScorePoints(IScoreCriteria iscoreboardcriteria, int i) {
        Collection collection = this.world.getServer().getScoreboardManager().getScoreboardScores(iscoreboardcriteria, this.getName(), new java.util.ArrayList<Score>()); // CraftBukkit - Use our scores instead
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            Score scoreboardscore = (Score) iterator.next(); // CraftBukkit - Use our scores instead

            scoreboardscore.setScorePoints(i);
        }

    }

    public void onDeath(DamageSource damagesource) {
        boolean flag = this.world.getGameRules().getBoolean("showDeathMessages");

        this.connection.sendPacket(new SPacketCombatEvent(this.getCombatTracker(), SPacketCombatEvent.Event.ENTITY_DIED, flag));
        // CraftBukkit start - fire PlayerDeathEvent
        if (this.isDead) {
            return;
        }
        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>(this.inventory.getSizeInventory());
        boolean keepInventory = this.world.getGameRules().getBoolean("keepInventory") || this.isSpectator();

        if (!keepInventory) {
            for (ItemStack item : this.inventory.getContents()) {
                if (!item.isEmpty() && !EnchantmentHelper.hasVanishingCurse(item)) {
                    loot.add(CraftItemStack.asCraftMirror(item));
                }
            }
        }

        ITextComponent chatmessage = this.getCombatTracker().getDeathMessage();

        String deathmessage = chatmessage.getUnformattedText();
        org.bukkit.event.entity.PlayerDeathEvent event = CraftEventFactory.callPlayerDeathEvent(this, loot, deathmessage, keepInventory);

        String deathMessage = event.getDeathMessage();

        if (deathMessage != null && deathMessage.length() > 0 && flag) { // TODO: allow plugins to override?
            if (deathMessage.equals(deathmessage)) {
                Team scoreboardteambase = this.getTeam();

                if (scoreboardteambase != null && scoreboardteambase.getDeathMessageVisibility() != Team.EnumVisible.ALWAYS) {
                    if (scoreboardteambase.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OTHER_TEAMS) {
                        this.mcServer.getPlayerList().sendMessageToAllTeamMembers((EntityPlayer) this, chatmessage);
                    } else if (scoreboardteambase.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OWN_TEAM) {
                        this.mcServer.getPlayerList().sendMessageToTeamOrAllPlayers((EntityPlayer) this, chatmessage);
                    }
                } else {
                    this.mcServer.getPlayerList().sendMessage(chatmessage);
                }
            } else {
                this.mcServer.getPlayerList().sendMessage(org.bukkit.craftbukkit.util.CraftChatMessage.fromString(deathMessage));
            }
        }

        this.spawnShoulderEntities();
        // we clean the player's inventory after the EntityDeathEvent is called so plugins can get the exact state of the inventory.
        if (!event.getKeepInventory()) {
            this.inventory.clear();
        }

        this.closeScreen();
        this.setSpectatingEntity(this); // Remove spectated target
        // CraftBukkit end

        // CraftBukkit - Get our scores instead
        Collection collection = this.world.getServer().getScoreboardManager().getScoreboardScores(IScoreCriteria.DEATH_COUNT, this.getName(), new java.util.ArrayList<Score>());
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            Score scoreboardscore = (Score) iterator.next(); // CraftBukkit - Use our scores instead

            scoreboardscore.incrementScore();
        }

        EntityLivingBase entityliving = this.getAttackingEntity();

        if (entityliving != null) {
            EntityList.EntityEggInfo entitytypes_monsteregginfo = (EntityList.EntityEggInfo) EntityList.ENTITY_EGGS.get(EntityList.getKey((Entity) entityliving));

            if (entitytypes_monsteregginfo != null) {
                this.addStat(entitytypes_monsteregginfo.entityKilledByStat);
            }

            entityliving.awardKillScore(this, this.scoreValue, damagesource);
        }

        this.addStat(StatList.DEATHS);
        this.takeStat(StatList.TIME_SINCE_DEATH);
        this.extinguish();
        this.setFlag(0, false);
        this.getCombatTracker().reset();
    }

    public void awardKillScore(Entity entity, int i, DamageSource damagesource) {
        if (entity != this) {
            super.awardKillScore(entity, i, damagesource);
            this.addScore(i);
            // CraftBukkit - Get our scores instead
            Collection<Score> collection = this.world.getServer().getScoreboardManager().getScoreboardScores(IScoreCriteria.TOTAL_KILL_COUNT, this.getName(), new java.util.ArrayList<Score>());

            if (entity instanceof EntityPlayer) {
                this.addStat(StatList.PLAYER_KILLS);
                // CraftBukkit - Get our scores instead
                this.world.getServer().getScoreboardManager().getScoreboardScores(IScoreCriteria.PLAYER_KILL_COUNT, this.getName(), collection);
                // collection.addAll(this.getScoreboard().getObjectivesForCriteria(IScoreboardCriteria.e));
                // CraftBukkit end
            } else {
                this.addStat(StatList.MOB_KILLS);
            }

            collection.addAll(this.awardTeamKillScores(entity));
            Iterator<Score> iterator = collection.iterator(); // CraftBukkit

            while (iterator.hasNext()) {
                // CraftBukkit start
                // ScoreboardObjective scoreboardobjective = (ScoreboardObjective) iterator.next();

                // this.getScoreboard().getPlayerScoreForObjective(this.getName(), scoreboardobjective).incrementScore();
                iterator.next().incrementScore();
                // CraftBukkit end
            }

            CriteriaTriggers.PLAYER_KILLED_ENTITY.trigger(this, entity, damagesource);
        }
    }

    private Collection<Score> awardTeamKillScores(Entity entity) { // CraftBukkit
        String s = entity instanceof EntityPlayer ? entity.getName() : entity.getCachedUniqueIdString();
        ScorePlayerTeam scoreboardteam = this.getWorldScoreboard().getPlayersTeam(this.getName());

        if (scoreboardteam != null) {
            int i = scoreboardteam.getColor().getColorIndex();

            if (i >= 0 && i < IScoreCriteria.KILLED_BY_TEAM.length) {
                Iterator iterator = this.getWorldScoreboard().getObjectivesFromCriteria(IScoreCriteria.KILLED_BY_TEAM[i]).iterator();

                while (iterator.hasNext()) {
                    ScoreObjective scoreboardobjective = (ScoreObjective) iterator.next();
                    Score scoreboardscore = this.getWorldScoreboard().getOrCreateScore(s, scoreboardobjective);

                    scoreboardscore.incrementScore();
                }
            }
        }

        ScorePlayerTeam scoreboardteam1 = this.getWorldScoreboard().getPlayersTeam(s);

        if (scoreboardteam1 != null) {
            int j = scoreboardteam1.getColor().getColorIndex();

            if (j >= 0 && j < IScoreCriteria.TEAM_KILL.length) {
                // CraftBukkit - Get our scores instead
                return this.world.getServer().getScoreboardManager().getScoreboardScores(IScoreCriteria.TEAM_KILL[j], this.getName(), new java.util.ArrayList<Score>());
                // return this.getScoreboard().getObjectivesForCriteria(IScoreboardCriteria.m[j]);
                // CraftBukkit end
            }
        }

        return Lists.newArrayList();
    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        if (this.isEntityInvulnerable(damagesource)) {
            return false;
        } else {
            boolean flag = this.mcServer.isDedicatedServer() && this.canPlayersAttack() && "fall".equals(damagesource.damageType);

            if (!flag && this.respawnInvulnerabilityTicks > 0 && damagesource != DamageSource.OUT_OF_WORLD) {
                return false;
            } else {
                if (damagesource instanceof EntityDamageSource) {
                    Entity entity = damagesource.getTrueSource();

                    if (entity instanceof EntityPlayer && !this.canAttackPlayer((EntityPlayer) entity)) {
                        return false;
                    }

                    if (entity instanceof EntityArrow) {
                        EntityArrow entityarrow = (EntityArrow) entity;

                        if (entityarrow.shootingEntity instanceof EntityPlayer && !this.canAttackPlayer((EntityPlayer) entityarrow.shootingEntity)) {
                            return false;
                        }
                    }
                }

                return super.attackEntityFrom(damagesource, f);
            }
        }
    }

    public boolean canAttackPlayer(EntityPlayer entityhuman) {
        return !this.canPlayersAttack() ? false : super.canAttackPlayer(entityhuman);
    }

    private boolean canPlayersAttack() {
        // CraftBukkit - this.server.getPvP() -> this.world.pvpMode
        return this.world.pvpMode;
    }

    @Nullable
    public Entity changeDimension(int i) {
        if (this.isPlayerSleeping()) return this; // CraftBukkit - SPIGOT-3154
        // this.worldChangeInvuln = true; // CraftBukkit - Moved down and into PlayerList#changeDimension
        if (this.dimension == 0 && i == -1) {
            this.enteredNetherPosition = new Vec3d(this.posX, this.posY, this.posZ);
        } else if (this.dimension != -1 && i != 0) {
            this.enteredNetherPosition = null;
        }

        if (this.dimension == 1 && i == 1) {
            this.invulnerableDimensionChange = true; // CraftBukkit - Moved down from above
            this.world.removeEntity(this);
            if (!this.queuedEndExit) {
                this.queuedEndExit = true;
                if (world.paperConfig.disableEndCredits) this.setHasSeenCredits(true); // Paper - Toggle to always disable end credits
                this.connection.sendPacket(new SPacketChangeGameState(4, this.seenCredits ? 0.0F : 1.0F));
                this.seenCredits = true;
            }

            return this;
        } else {
            if (this.dimension == 0 && i == 1) {
                i = 1;
            }

            // CraftBukkit start
            TeleportCause cause = (this.dimension == 1 || i == 1) ? TeleportCause.END_PORTAL : TeleportCause.NETHER_PORTAL;
            this.mcServer.getPlayerList().changeDimension(this, i, cause); // PAIL: check all this
            // CraftBukkit end
            this.connection.sendPacket(new SPacketEffect(1032, BlockPos.ORIGIN, 0, false));
            this.lastExperience = -1;
            this.lastHealth = -1.0F;
            this.lastFoodLevel = -1;
            return this;
        }
    }

    public boolean isSpectatedByPlayer(EntityPlayerMP entityplayer) {
        return entityplayer.isSpectator() ? this.getSpectatingEntity() == this : (this.isSpectator() ? false : super.isSpectatedByPlayer(entityplayer));
    }

    private void sendTileEntityUpdate(TileEntity tileentity) {
        if (tileentity != null) {
            SPacketUpdateTileEntity packetplayouttileentitydata = tileentity.getUpdatePacket();

            if (packetplayouttileentitydata != null) {
                this.connection.sendPacket(packetplayouttileentitydata);
            }
        }

    }

    public void onItemPickup(Entity entity, int i) {
        super.onItemPickup(entity, i);
        this.openContainer.detectAndSendChanges();
    }

    public EntityPlayer.SleepResult trySleep(BlockPos blockposition) {
        EntityPlayer.SleepResult entityhuman_enumbedresult = super.trySleep(blockposition);

        if (entityhuman_enumbedresult == EntityPlayer.SleepResult.OK) {
            this.addStat(StatList.SLEEP_IN_BED);
            SPacketUseBed packetplayoutbed = new SPacketUseBed(this, blockposition);

            this.getServerWorld().getEntityTracker().sendToTracking((Entity) this, (Packet) packetplayoutbed);
            this.connection.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.connection.sendPacket(packetplayoutbed);
            CriteriaTriggers.SLEPT_IN_BED.trigger(this);
        }

        return entityhuman_enumbedresult;
    }

    public void wakeUpPlayer(boolean flag, boolean flag1, boolean flag2) {
        if (!this.sleeping) return; // CraftBukkit - Can't leave bed if not in one!
        if (this.isPlayerSleeping()) {
            this.getServerWorld().getEntityTracker().sendToTrackingAndSelf(this, new SPacketAnimation(this, 2));
        }

        super.wakeUpPlayer(flag, flag1, flag2);
        if (this.connection != null) {
            this.connection.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }

    }

    public boolean startRiding(Entity entity, boolean flag) {
        Entity entity1 = this.getRidingEntity();

        if (!super.startRiding(entity, flag)) {
            return false;
        } else {
            Entity entity2 = this.getRidingEntity();

            if (entity2 != entity1 && this.connection != null) {
                this.connection.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            }

            return true;
        }
    }

    public void dismountRidingEntity() {
        Entity entity = this.getRidingEntity();

        super.dismountRidingEntity();
        Entity entity1 = this.getRidingEntity();

        if (entity1 != entity && this.connection != null) {
            this.connection.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }
        // Paper start - "Fixes" an issue in which the vehicle player would not be notified that the passenger dismounted
        if (entity instanceof EntityPlayerMP) {
            WorldServer worldServer = (WorldServer) entity.getEntityWorld();
            worldServer.entityTracker.untrack(this);
            worldServer.entityTracker.track(this);
        }
        // Paper end

    }

    public boolean isEntityInvulnerable(DamageSource damagesource) {
        return super.isEntityInvulnerable(damagesource) || this.isInvulnerableDimensionChange();
    }

    protected void updateFallState(double d0, boolean flag, IBlockState iblockdata, BlockPos blockposition) {}

    protected void frostWalk(BlockPos blockposition) {
        if (!this.isSpectator()) {
            super.frostWalk(blockposition);
        }

    }

    public void handleFalling(double d0, boolean flag) {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.posY - 0.20000000298023224D);
        int k = MathHelper.floor(this.posZ);
        BlockPos blockposition = new BlockPos(i, j, k);
        IBlockState iblockdata = this.world.getBlockState(blockposition);

        if (iblockdata.getMaterial() == Material.AIR) {
            BlockPos blockposition1 = blockposition.down();
            IBlockState iblockdata1 = this.world.getBlockState(blockposition1);
            Block block = iblockdata1.getBlock();

            if (block instanceof BlockFence || block instanceof BlockWall || block instanceof BlockFenceGate) {
                blockposition = blockposition1;
                iblockdata = iblockdata1;
            }
        }

        super.updateFallState(d0, flag, iblockdata, blockposition);
    }

    public void openEditSign(TileEntitySign tileentitysign) {
        tileentitysign.setPlayer((EntityPlayer) this);
        this.connection.sendPacket(new SPacketSignEditorOpen(tileentitysign.getPos()));
    }

    public int nextContainerCounter() { // CraftBukkit - void -> int
        this.currentWindowId = this.currentWindowId % 100 + 1;
        return currentWindowId; // CraftBukkit
    }

    public void displayGui(IInteractionObject itileentitycontainer) {
        // CraftBukkit start - Inventory open hook
        if (false && itileentitycontainer instanceof ILootContainer && ((ILootContainer) itileentitycontainer).getLootTable() != null && this.isSpectator()) {
            this.sendStatusMessage((new TextComponentTranslation("container.spectatorCantOpen", new Object[0])).setStyle((new Style()).setColor(TextFormatting.RED)), true);
        } else {
            boolean cancelled = itileentitycontainer instanceof ILootContainer && ((ILootContainer) itileentitycontainer).getLootTable() != null && this.isSpectator();
            Container container = CraftEventFactory.callInventoryOpenEvent(this, itileentitycontainer.createContainer(this.inventory, this), cancelled);
            if (container == null) {
                return;
            }
            this.nextContainerCounter();
            this.openContainer = container;
            this.connection.sendPacket(new SPacketOpenWindow(this.currentWindowId, itileentitycontainer.getGuiID(), itileentitycontainer.getDisplayName()));
            // CraftBukkit end
            this.openContainer.windowId = this.currentWindowId;
            this.openContainer.addListener(this);
        }
    }

    public void displayGUIChest(IInventory iinventory) {
        // CraftBukkit start - Inventory open hook
        // Copied from below
        boolean cancelled = false;
        if (iinventory instanceof ILockableContainer) {
            ILockableContainer itileinventory = (ILockableContainer) iinventory;
            cancelled = itileinventory.isLocked() && !this.canOpen(itileinventory.getLockCode()) && !this.isSpectator();
        }

        Container container;
        if (iinventory instanceof IInteractionObject) {
            if (iinventory instanceof TileEntity) {
                Preconditions.checkArgument(((TileEntity) iinventory).getWorld() != null, "Container must have world to be opened");
            }
            container = ((IInteractionObject) iinventory).createContainer(this.inventory, this);
        } else {
            container = new ContainerChest(this.inventory, iinventory, this);
        }
        container = CraftEventFactory.callInventoryOpenEvent(this, container, cancelled);
        if (container == null && !cancelled) { // Let pre-cancelled events fall through
            iinventory.closeInventory(this);
            return;
        }
        // CraftBukkit end

        if (iinventory instanceof ILootContainer && ((ILootContainer) iinventory).getLootTable() != null && this.isSpectator()) {
            this.sendStatusMessage((new TextComponentTranslation("container.spectatorCantOpen", new Object[0])).setStyle((new Style()).setColor(TextFormatting.RED)), true);
        } else {
            if (this.openContainer != this.inventoryContainer) {
                this.closeScreen();
            }

            if (iinventory instanceof ILockableContainer) {
                ILockableContainer itileinventory = (ILockableContainer) iinventory;

                if (itileinventory.isLocked() && !this.canOpen(itileinventory.getLockCode()) && !this.isSpectator()) {
                    this.connection.sendPacket(new SPacketChat(new TextComponentTranslation("container.isLocked", new Object[] { iinventory.getDisplayName()}), ChatType.GAME_INFO));
                    this.connection.sendPacket(new SPacketSoundEffect(SoundEvents.BLOCK_CHEST_LOCKED, SoundCategory.BLOCKS, this.posX, this.posY, this.posZ, 1.0F, 1.0F));
                    iinventory.closeInventory(this); // CraftBukkit
                    return;
                }
            }

            this.nextContainerCounter();
            // CraftBukkit start
            if (iinventory instanceof IInteractionObject) {
                this.openContainer = container;
                this.connection.sendPacket(new SPacketOpenWindow(this.currentWindowId, ((IInteractionObject) iinventory).getGuiID(), iinventory.getDisplayName(), iinventory.getSizeInventory()));
            } else {
                this.openContainer = container;
                this.connection.sendPacket(new SPacketOpenWindow(this.currentWindowId, "minecraft:container", iinventory.getDisplayName(), iinventory.getSizeInventory()));
            }
            // CraftBukkit end

            this.openContainer.windowId = this.currentWindowId;
            this.openContainer.addListener(this);
        }
    }

    public void displayVillagerTradeGui(IMerchant imerchant) {
        // CraftBukkit start - Inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerMerchant(this.inventory, imerchant, this.world));
        if (container == null) {
            return;
        }
        // CraftBukkit end
        this.nextContainerCounter();
        this.openContainer = container; // CraftBukkit
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.addListener(this);
        InventoryMerchant inventorymerchant = ((ContainerMerchant) this.openContainer).getMerchantInventory();
        ITextComponent ichatbasecomponent = imerchant.getDisplayName();

        this.connection.sendPacket(new SPacketOpenWindow(this.currentWindowId, "minecraft:villager", ichatbasecomponent, inventorymerchant.getSizeInventory()));
        MerchantRecipeList merchantrecipelist = imerchant.getRecipes(this);

        if (merchantrecipelist != null) {
            PacketBuffer packetdataserializer = new PacketBuffer(Unpooled.buffer());

            packetdataserializer.writeInt(this.currentWindowId);
            merchantrecipelist.writeToBuf(packetdataserializer);
            this.connection.sendPacket(new SPacketCustomPayload("MC|TrList", packetdataserializer));
        }

    }

    public void openGuiHorseInventory(AbstractHorse entityhorseabstract, IInventory iinventory) {
        // CraftBukkit start - Inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerHorseInventory(this.inventory, iinventory, entityhorseabstract, this));
        if (container == null) {
            iinventory.closeInventory(this);
            return;
        }
        // CraftBukkit end
        if (this.openContainer != this.inventoryContainer) {
            this.closeScreen();
        }

        this.nextContainerCounter();
        this.connection.sendPacket(new SPacketOpenWindow(this.currentWindowId, "EntityHorse", iinventory.getDisplayName(), iinventory.getSizeInventory(), entityhorseabstract.getEntityId()));
        this.openContainer = container; // CraftBukkit
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.addListener(this);
    }

    public void openBook(ItemStack itemstack, EnumHand enumhand) {
        Item item = itemstack.getItem();

        if (item == Items.WRITTEN_BOOK) {
            PacketBuffer packetdataserializer = new PacketBuffer(Unpooled.buffer());

            packetdataserializer.writeEnumValue((Enum) enumhand);
            this.connection.sendPacket(new SPacketCustomPayload("MC|BOpen", packetdataserializer));
        }

    }

    public void displayGuiCommandBlock(TileEntityCommandBlock tileentitycommand) {
        tileentitycommand.setSendToClient(true);
        this.sendTileEntityUpdate((TileEntity) tileentitycommand);
    }

    public void sendSlotContents(Container container, int i, ItemStack itemstack) {
        if (!(container.getSlot(i) instanceof SlotCrafting)) {
            if (container == this.inventoryContainer) {
                CriteriaTriggers.INVENTORY_CHANGED.trigger(this, this.inventory);
            }

            if (!this.isChangingQuantityOnly) {
                this.connection.sendPacket(new SPacketSetSlot(container.windowId, i, itemstack));
            }
        }
    }

    public void sendContainerToPlayer(Container container) {
        this.sendAllContents(container, container.getInventory());
    }

    public void sendAllContents(Container container, NonNullList<ItemStack> nonnulllist) {
        this.connection.sendPacket(new SPacketWindowItems(container.windowId, nonnulllist));
        this.connection.sendPacket(new SPacketSetSlot(-1, -1, this.inventory.getItemStack()));
        // CraftBukkit start - Send a Set Slot to update the crafting result slot
        if (java.util.EnumSet.of(InventoryType.CRAFTING,InventoryType.WORKBENCH).contains(container.getBukkitView().getType())) {
            this.connection.sendPacket(new SPacketSetSlot(container.windowId, 0, container.getSlot(0).getStack()));
        }
        // CraftBukkit end
    }

    public void sendWindowProperty(Container container, int i, int j) {
        this.connection.sendPacket(new SPacketWindowProperty(container.windowId, i, j));
    }

    public void sendAllWindowProperties(Container container, IInventory iinventory) {
        for (int i = 0; i < iinventory.getFieldCount(); ++i) {
            this.connection.sendPacket(new SPacketWindowProperty(container.windowId, i, iinventory.getField(i)));
        }

    }

    public void closeScreen() {
        CraftEventFactory.handleInventoryCloseEvent(this); // CraftBukkit
        this.connection.sendPacket(new SPacketCloseWindow(this.openContainer.windowId));
        this.closeContainer();
    }

    public void updateHeldItem() {
        if (!this.isChangingQuantityOnly) {
            this.connection.sendPacket(new SPacketSetSlot(-1, -1, this.inventory.getItemStack()));
        }
    }

    public void closeContainer() {
        this.openContainer.onContainerClosed((EntityPlayer) this);
        this.openContainer = this.inventoryContainer;
    }

    public void setEntityActionState(float f, float f1, boolean flag, boolean flag1) {
        if (this.isRiding()) {
            if (f >= -1.0F && f <= 1.0F) {
                this.moveStrafing = f;
            }

            if (f1 >= -1.0F && f1 <= 1.0F) {
                this.moveForward = f1;
            }

            this.isJumping = flag;
            this.setSneaking(flag1);
        }

    }

    public void addStat(StatBase statistic, int i) {
        if (statistic != null) {
            this.statsFile.increaseStat(this, statistic, i);
            Iterator iterator = this.getWorldScoreboard().getObjectivesFromCriteria(statistic.getCriteria()).iterator();

            while (iterator.hasNext()) {
                ScoreObjective scoreboardobjective = (ScoreObjective) iterator.next();

                this.getWorldScoreboard().getOrCreateScore(this.getName(), scoreboardobjective).increaseScore(i);
            }

        }
    }

    public void takeStat(StatBase statistic) {
        if (statistic != null) {
            this.statsFile.unlockAchievement(this, statistic, 0);
            Iterator iterator = this.getWorldScoreboard().getObjectivesFromCriteria(statistic.getCriteria()).iterator();

            while (iterator.hasNext()) {
                ScoreObjective scoreboardobjective = (ScoreObjective) iterator.next();

                this.getWorldScoreboard().getOrCreateScore(this.getName(), scoreboardobjective).setScorePoints(0);
            }

        }
    }

    public void unlockRecipes(List<IRecipe> list) {
        this.recipeBook.add(list, this);
    }

    public void unlockRecipes(ResourceLocation[] aminecraftkey) {
        ArrayList arraylist = Lists.newArrayList();
        ResourceLocation[] aminecraftkey1 = aminecraftkey;
        int i = aminecraftkey.length;

        for (int j = 0; j < i; ++j) {
            ResourceLocation minecraftkey = aminecraftkey1[j];

            // CraftBukkit start
            if (CraftingManager.getRecipe(minecraftkey) == null) {
                Bukkit.getLogger().warning("Ignoring grant of non existent recipe " + minecraftkey);
                continue;
            }
            // CraftBukit end
            arraylist.add(CraftingManager.getRecipe(minecraftkey));
        }

        this.unlockRecipes((List<IRecipe>) arraylist); // CraftBukkit - decompile error
    }

    public void resetRecipes(List<IRecipe> list) {
        this.recipeBook.remove(list, this);
    }

    public void mountEntityAndWakeUp() {
        this.disconnected = true;
        this.removePassengers();
        if (this.sleeping) {
            this.wakeUpPlayer(true, false, false);
        }

    }

    public boolean hasDisconnected() {
        return this.disconnected;
    }

    public void setPlayerHealthUpdated() {
        this.lastHealth = -1.0E8F;
        this.lastExperience = -1; // CraftBukkit - Added to reset
    }

    // CraftBukkit start - Support multi-line messages
    public void sendMessage(ITextComponent[] ichatbasecomponent) {
        for (ITextComponent component : ichatbasecomponent) {
            this.sendMessage(component);
        }
    }
    // CraftBukkit end

    public void sendStatusMessage(ITextComponent ichatbasecomponent, boolean flag) {
        this.connection.sendPacket(new SPacketChat(ichatbasecomponent, flag ? ChatType.GAME_INFO : ChatType.CHAT));
    }

    protected void onItemUseFinish() {
        if (!this.activeItemStack.isEmpty() && this.isHandActive()) {
            this.connection.sendPacket(new SPacketEntityStatus(this, (byte) 9));
            super.onItemUseFinish();
        }

    }

    public void copyFrom(EntityPlayerMP entityplayer, boolean flag) {
        if (flag) {
            this.inventory.copyInventory(entityplayer.inventory);
            this.setHealth(entityplayer.getHealth());
            this.foodStats = entityplayer.foodStats;
            this.experienceLevel = entityplayer.experienceLevel;
            this.experienceTotal = entityplayer.experienceTotal;
            this.experience = entityplayer.experience;
            this.setScore(entityplayer.getScore());
            this.lastPortalPos = entityplayer.lastPortalPos;
            this.lastPortalVec = entityplayer.lastPortalVec;
            this.teleportDirection = entityplayer.teleportDirection;
        } else if (this.world.getGameRules().getBoolean("keepInventory") || entityplayer.isSpectator()) {
            this.inventory.copyInventory(entityplayer.inventory);
            this.experienceLevel = entityplayer.experienceLevel;
            this.experienceTotal = entityplayer.experienceTotal;
            this.experience = entityplayer.experience;
            this.setScore(entityplayer.getScore());
        }

        this.xpSeed = entityplayer.xpSeed;
        this.enderChest = entityplayer.enderChest;
        this.getDataManager().set(EntityPlayerMP.PLAYER_MODEL_FLAG, entityplayer.getDataManager().get(EntityPlayerMP.PLAYER_MODEL_FLAG));
        this.lastExperience = -1;
        this.lastHealth = -1.0F;
        this.lastFoodLevel = -1;
        // this.cr.a((RecipeBook) entityplayer.cr); // CraftBukkit
        // Paper start - Optimize remove queue
        //this.removeQueue.addAll(entityplayer.removeQueue);
        if (this.entityRemoveQueue != entityplayer.entityRemoveQueue) {
            this.entityRemoveQueue.addAll(entityplayer.entityRemoveQueue);
        }
        this.seenCredits = entityplayer.seenCredits;
        this.enteredNetherPosition = entityplayer.enteredNetherPosition;
        this.setLeftShoulderEntity(entityplayer.getLeftShoulderEntity());
        this.setRightShoulderEntity(entityplayer.getRightShoulderEntity());
    }

    protected void onNewPotionEffect(PotionEffect mobeffect) {
        super.onNewPotionEffect(mobeffect);
        this.connection.sendPacket(new SPacketEntityEffect(this.getEntityId(), mobeffect));
        if (mobeffect.getPotion() == MobEffects.LEVITATION) {
            this.levitatingSince = this.ticksExisted;
            this.levitationStartPos = new Vec3d(this.posX, this.posY, this.posZ);
        }

        CriteriaTriggers.EFFECTS_CHANGED.trigger(this);
    }

    protected void onChangedPotionEffect(PotionEffect mobeffect, boolean flag) {
        super.onChangedPotionEffect(mobeffect, flag);
        this.connection.sendPacket(new SPacketEntityEffect(this.getEntityId(), mobeffect));
        CriteriaTriggers.EFFECTS_CHANGED.trigger(this);
    }

    protected void onFinishedPotionEffect(PotionEffect mobeffect) {
        super.onFinishedPotionEffect(mobeffect);
        this.connection.sendPacket(new SPacketRemoveEntityEffect(this.getEntityId(), mobeffect.getPotion()));
        if (mobeffect.getPotion() == MobEffects.LEVITATION) {
            this.levitationStartPos = null;
        }

        CriteriaTriggers.EFFECTS_CHANGED.trigger(this);
    }

    public void setPositionAndUpdate(double d0, double d1, double d2) {
        this.connection.setPlayerLocation(d0, d1, d2, this.rotationYaw, this.rotationPitch);
    }

    public void onCriticalHit(Entity entity) {
        this.getServerWorld().getEntityTracker().sendToTrackingAndSelf(this, new SPacketAnimation(entity, 4));
    }

    public void onEnchantmentCritical(Entity entity) {
        this.getServerWorld().getEntityTracker().sendToTrackingAndSelf(this, new SPacketAnimation(entity, 5));
    }

    public void sendPlayerAbilities() {
        if (this.connection != null) {
            this.connection.sendPacket(new SPacketPlayerAbilities(this.capabilities));
            this.updatePotionMetadata();
        }
    }

    public WorldServer getServerWorld() {
        return (WorldServer) this.world;
    }

    public void setGameType(GameType enumgamemode) {
        // CraftBukkit start
        if (enumgamemode == this.interactionManager.getGameType()) {
            return;
        }

        PlayerGameModeChangeEvent event = new PlayerGameModeChangeEvent(getBukkitEntity(), GameMode.getByValue(enumgamemode.getID()));
        world.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        // CraftBukkit end

        this.interactionManager.setGameType(enumgamemode);
        this.connection.sendPacket(new SPacketChangeGameState(3, (float) enumgamemode.getID()));
        if (enumgamemode == GameType.SPECTATOR) {
            this.spawnShoulderEntities();
            this.dismountRidingEntity();
        } else {
            this.setSpectatingEntity(this);
        }

        this.sendPlayerAbilities();
        this.markPotionsDirty();
    }

    public boolean isSpectator() {
        return this.interactionManager.getGameType() == GameType.SPECTATOR;
    }

    public boolean isCreative() {
        return this.interactionManager.getGameType() == GameType.CREATIVE;
    }

    public void sendMessage(ITextComponent ichatbasecomponent) {
        this.connection.sendPacket(new SPacketChat(ichatbasecomponent));
    }

    public boolean canUseCommand(int i, String s) {
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

    public String getPlayerIP() {
        String s = this.connection.netManager.getRemoteAddress().toString();

        s = s.substring(s.indexOf("/") + 1);
        s = s.substring(0, s.indexOf(":"));
        return s;
    }

    public void handleClientSettings(CPacketClientSettings packetplayinsettings) {
        // CraftBukkit start
        if (getPrimaryHand() != packetplayinsettings.getMainHand()) {
            PlayerChangedMainHandEvent event = new PlayerChangedMainHandEvent(getBukkitEntity(), getPrimaryHand() == EnumHandSide.LEFT ? MainHand.LEFT : MainHand.RIGHT);
            this.mcServer.server.getPluginManager().callEvent(event);
        }

        // Paper start - add PlayerLocaleChangeEvent
        // Since the field is initialized to null, this event should always fire the first time the packet is received
        String oldLocale = this.language;
        this.language = packetplayinsettings.getLang();
        if (!this.language.equals(oldLocale)) {
            new com.destroystokyo.paper.event.player.PlayerLocaleChangeEvent(this.getBukkitEntity(), oldLocale, this.language).callEvent();
        }

        // Compat with Bukkit
        oldLocale = oldLocale != null ? oldLocale : "en_us";
        // Paper end

        if (!oldLocale.equals(packetplayinsettings.getLang())) {
            PlayerLocaleChangeEvent event = new PlayerLocaleChangeEvent(getBukkitEntity(), packetplayinsettings.getLang());
            this.mcServer.server.getPluginManager().callEvent(event);
        }
        // CraftBukkit end
        this.chatVisibility = packetplayinsettings.getChatVisibility();
        this.chatColours = packetplayinsettings.isColorsEnabled();
        this.getDataManager().set(EntityPlayerMP.PLAYER_MODEL_FLAG, Byte.valueOf((byte) packetplayinsettings.getModelPartFlags()));
        this.getDataManager().set(EntityPlayerMP.MAIN_HAND, Byte.valueOf((byte) (packetplayinsettings.getMainHand() == EnumHandSide.LEFT ? 0 : 1)));
    }

    public EntityPlayer.EnumChatVisibility getChatVisibility() {
        return this.chatVisibility;
    }

    public void loadResourcePack(String s, String s1) {
        this.connection.sendPacket(new SPacketResourcePackSend(s, s1));
    }

    public BlockPos getPosition() {
        return new BlockPos(this.posX, this.posY + 0.5D, this.posZ);
    }

    public void markPlayerActive() {
        this.playerLastActiveTime = MinecraftServer.getCurrentTimeMillis();
    }

    public StatisticsManagerServer getStatFile() {
        return this.statsFile;
    }

    public RecipeBookServer getRecipeBook() {
        return this.recipeBook;
    }

    public void removeEntity(Entity entity) {
        if (entity instanceof EntityPlayer) {
            this.connection.sendPacket(new SPacketDestroyEntities(new int[] { entity.getEntityId()}));
        } else {
            this.entityRemoveQueue.add(Integer.valueOf(entity.getEntityId()));
        }

    }

    public void addEntity(Entity entity) {
        this.entityRemoveQueue.remove(Integer.valueOf(entity.getEntityId()));
    }

    protected void updatePotionMetadata() {
        if (this.isSpectator()) {
            this.resetPotionEffectMetadata();
            this.setInvisible(true);
        } else {
            super.updatePotionMetadata();
        }

        this.getServerWorld().getEntityTracker().updateVisibility(this);
    }

    public Entity getSpectatingEntity() {
        return (Entity) (this.spectatingEntity == null ? this : this.spectatingEntity);
    }

    public void setSpectatingEntity(Entity entity) {
        Entity entity1 = this.getSpectatingEntity();

        this.spectatingEntity = (Entity) (entity == null ? this : entity);
        if (entity1 != this.spectatingEntity) {
            this.connection.sendPacket(new SPacketCamera(this.spectatingEntity));
            this.connection.a(this.spectatingEntity.posX, this.spectatingEntity.posY, this.spectatingEntity.posZ, this.rotationYaw, this.rotationPitch, TeleportCause.SPECTATE); // CraftBukkit
        }

    }

    protected void decrementTimeUntilPortal() {
        if (this.timeUntilPortal > 0 && !this.invulnerableDimensionChange) {
            --this.timeUntilPortal;
        }

    }

    public void attackTargetEntityWithCurrentItem(Entity entity) {
        if (this.interactionManager.getGameType() == GameType.SPECTATOR) {
            this.setSpectatingEntity(entity);
        } else {
            super.attackTargetEntityWithCurrentItem(entity);
        }

    }

    public long getLastActiveTime() {
        return this.playerLastActiveTime;
    }

    @Nullable
    public ITextComponent getTabListDisplayName() {
        return listName; // CraftBukkit
    }

    public void swingArm(EnumHand enumhand) {
        super.swingArm(enumhand);
        this.resetCooldown();
    }

    public boolean isInvulnerableDimensionChange() {
        return this.invulnerableDimensionChange;
    }

    public void clearInvulnerableDimensionChange() {
        this.invulnerableDimensionChange = false;
    }

    public void setElytraFlying() {
        if (!CraftEventFactory.callToggleGlideEvent(this, true).isCancelled()) // CraftBukkit
        this.setFlag(7, true);
    }

    public void clearElytraFlying() {
        // CraftBukkit start
        if (!CraftEventFactory.callToggleGlideEvent(this, false).isCancelled()) {
        this.setFlag(7, true);
        this.setFlag(7, false);
        }
        // CraftBukkit end
    }

    public PlayerAdvancements getAdvancements() {
        return this.advancements;
    }

    @Nullable
    public Vec3d getEnteredNetherPosition() {
        return this.enteredNetherPosition;
    }

    // CraftBukkit start - Add per-player time and weather.
    public long timeOffset = 0;
    public boolean relativeTime = true;

    public long getPlayerTime() {
        if (this.relativeTime) {
            // Adds timeOffset to the current server time.
            return this.world.getWorldTime() + this.timeOffset;
        } else {
            // Adds timeOffset to the beginning of this day.
            return this.world.getWorldTime() - (this.world.getWorldTime() % 24000) + this.timeOffset;
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
            this.connection.sendPacket(new SPacketChangeGameState(2, 0));
        } else {
            this.connection.sendPacket(new SPacketChangeGameState(1, 0));
        }
    }

    private float pluginRainPosition;
    private float pluginRainPositionPrevious;

    public void updateWeather(float oldRain, float newRain, float oldThunder, float newThunder) {
        if (this.weather == null) {
            // Vanilla
            if (oldRain != newRain) {
                this.connection.sendPacket(new SPacketChangeGameState(7, newRain));
            }
        } else {
            // Plugin
            if (pluginRainPositionPrevious != pluginRainPosition) {
                this.connection.sendPacket(new SPacketChangeGameState(7, pluginRainPosition));
            }
        }

        if (oldThunder != newThunder) {
            if (weather == WeatherType.DOWNFALL || weather == null) {
                this.connection.sendPacket(new SPacketChangeGameState(8, newThunder));
            } else {
                this.connection.sendPacket(new SPacketChangeGameState(8, 0));
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

        pluginRainPosition = MathHelper.clamp(pluginRainPosition, 0.0F, 1.0F);
    }

    public void resetPlayerWeather() {
        this.weather = null;
        this.setPlayerWeather(this.world.getWorldInfo().isRaining() ? WeatherType.DOWNFALL : WeatherType.CLEAR, false);
    }

    @Override
    public String toString() {
        return super.toString() + "(" + this.getName() + " at " + this.posX + "," + this.posY + "," + this.posZ + ")";
    }

    // SPIGOT-1903, MC-98153
    public void forceSetPositionRotation(double x, double y, double z, float yaw, float pitch) {
        this.setLocationAndAngles(x, y, z, yaw, pitch);
        this.connection.captureCurrentPosition();
    }

    @Override
    protected boolean isMovementBlocked() {
        return super.isMovementBlocked() || (this.connection != null && this.connection.isDisconnected()); // Paper
    }

    @Override
    public Scoreboard getWorldScoreboard() {
        return getBukkitEntity().getScoreboard().getHandle();
    }

    public void reset() {
        float exp = 0;
        boolean keepInventory = this.world.getGameRules().getBoolean("keepInventory");

        if (this.keepLevel || keepInventory) {
            exp = this.experience;
            this.newTotalExp = this.experienceTotal;
            this.newLevel = this.experienceLevel;
        }

        this.setHealth(this.getMaxHealth());
        this.fire = 0;
        this.fallDistance = 0;
        this.foodStats = new FoodStats(this);
        this.experienceLevel = this.newLevel;
        this.experienceTotal = this.newTotalExp;
        this.experience = 0;
        this.deathTime = 0;
        this.setArrowCountInEntity(0);
        this.clearActivePotions();
        this.potionsNeedUpdate = true;
        this.openContainer = this.inventoryContainer;
        this.attackingPlayer = null;
        this.revengeTarget = null;
        this._combatTracker = new CombatTracker(this);
        this.lastExperience = -1;
        if (this.keepLevel || keepInventory) {
            this.experience = exp;
        } else {
            this.addExperience(this.newExp);
        }
        this.keepLevel = false;
    }

    @Override
    public CraftPlayer getBukkitEntity() {
        return (CraftPlayer) super.getBukkitEntity();
    }
    // CraftBukkit end
}
