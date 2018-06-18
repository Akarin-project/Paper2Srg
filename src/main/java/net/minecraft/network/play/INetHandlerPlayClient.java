package net.minecraft.network.play;
import net.minecraft.network.INetHandler;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketCamera;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.network.play.server.SPacketCombatEvent;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketCooldown;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.network.play.server.SPacketDisplayObjective;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.network.play.server.SPacketEntityHeadLook;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketKeepAlive;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketPlaceGhostRecipe;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRecipeBook;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.network.play.server.SPacketResourcePackSend;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketScoreboardObjective;
import net.minecraft.network.play.server.SPacketSelectAdvancementsTab;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketSignEditorOpen;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.network.play.server.SPacketStatistics;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.network.play.server.SPacketUnloadChunk;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.network.play.server.SPacketUpdateScore;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.network.play.server.SPacketWindowProperty;
import net.minecraft.network.play.server.SPacketWorldBorder;


public interface INetHandlerPlayClient extends INetHandler {

    void handleSpawnObject(SPacketSpawnObject packetplayoutspawnentity);

    void handleSpawnExperienceOrb(SPacketSpawnExperienceOrb packetplayoutspawnentityexperienceorb);

    void handleSpawnGlobalEntity(SPacketSpawnGlobalEntity packetplayoutspawnentityweather);

    void handleSpawnMob(SPacketSpawnMob packetplayoutspawnentityliving);

    void handleScoreboardObjective(SPacketScoreboardObjective packetplayoutscoreboardobjective);

    void handleSpawnPainting(SPacketSpawnPainting packetplayoutspawnentitypainting);

    void handleSpawnPlayer(SPacketSpawnPlayer packetplayoutnamedentityspawn);

    void handleAnimation(SPacketAnimation packetplayoutanimation);

    void handleStatistics(SPacketStatistics packetplayoutstatistic);

    void handleRecipeBook(SPacketRecipeBook packetplayoutrecipes);

    void handleBlockBreakAnim(SPacketBlockBreakAnim packetplayoutblockbreakanimation);

    void handleSignEditorOpen(SPacketSignEditorOpen packetplayoutopensigneditor);

    void handleUpdateTileEntity(SPacketUpdateTileEntity packetplayouttileentitydata);

    void handleBlockAction(SPacketBlockAction packetplayoutblockaction);

    void handleBlockChange(SPacketBlockChange packetplayoutblockchange);

    void handleChat(SPacketChat packetplayoutchat);

    void handleTabComplete(SPacketTabComplete packetplayouttabcomplete);

    void handleMultiBlockChange(SPacketMultiBlockChange packetplayoutmultiblockchange);

    void handleMaps(SPacketMaps packetplayoutmap);

    void handleConfirmTransaction(SPacketConfirmTransaction packetplayouttransaction);

    void handleCloseWindow(SPacketCloseWindow packetplayoutclosewindow);

    void handleWindowItems(SPacketWindowItems packetplayoutwindowitems);

    void handleOpenWindow(SPacketOpenWindow packetplayoutopenwindow);

    void handleWindowProperty(SPacketWindowProperty packetplayoutwindowdata);

    void handleSetSlot(SPacketSetSlot packetplayoutsetslot);

    void handleCustomPayload(SPacketCustomPayload packetplayoutcustompayload);

    void handleDisconnect(SPacketDisconnect packetplayoutkickdisconnect);

    void handleUseBed(SPacketUseBed packetplayoutbed);

    void handleEntityStatus(SPacketEntityStatus packetplayoutentitystatus);

    void handleEntityAttach(SPacketEntityAttach packetplayoutattachentity);

    void handleSetPassengers(SPacketSetPassengers packetplayoutmount);

    void handleExplosion(SPacketExplosion packetplayoutexplosion);

    void handleChangeGameState(SPacketChangeGameState packetplayoutgamestatechange);

    void handleKeepAlive(SPacketKeepAlive packetplayoutkeepalive);

    void handleChunkData(SPacketChunkData packetplayoutmapchunk);

    void processChunkUnload(SPacketUnloadChunk packetplayoutunloadchunk);

    void handleEffect(SPacketEffect packetplayoutworldevent);

    void handleJoinGame(SPacketJoinGame packetplayoutlogin);

    void handleEntityMovement(SPacketEntity packetplayoutentity);

    void handlePlayerPosLook(SPacketPlayerPosLook packetplayoutposition);

    void handleParticles(SPacketParticles packetplayoutworldparticles);

    void handlePlayerAbilities(SPacketPlayerAbilities packetplayoutabilities);

    void handlePlayerListItem(SPacketPlayerListItem packetplayoutplayerinfo);

    void handleDestroyEntities(SPacketDestroyEntities packetplayoutentitydestroy);

    void handleRemoveEntityEffect(SPacketRemoveEntityEffect packetplayoutremoveentityeffect);

    void handleRespawn(SPacketRespawn packetplayoutrespawn);

    void handleEntityHeadLook(SPacketEntityHeadLook packetplayoutentityheadrotation);

    void handleHeldItemChange(SPacketHeldItemChange packetplayouthelditemslot);

    void handleDisplayObjective(SPacketDisplayObjective packetplayoutscoreboarddisplayobjective);

    void handleEntityMetadata(SPacketEntityMetadata packetplayoutentitymetadata);

    void handleEntityVelocity(SPacketEntityVelocity packetplayoutentityvelocity);

    void handleEntityEquipment(SPacketEntityEquipment packetplayoutentityequipment);

    void handleSetExperience(SPacketSetExperience packetplayoutexperience);

    void handleUpdateHealth(SPacketUpdateHealth packetplayoutupdatehealth);

    void handleTeams(SPacketTeams packetplayoutscoreboardteam);

    void handleUpdateScore(SPacketUpdateScore packetplayoutscoreboardscore);

    void handleSpawnPosition(SPacketSpawnPosition packetplayoutspawnposition);

    void handleTimeUpdate(SPacketTimeUpdate packetplayoutupdatetime);

    void handleSoundEffect(SPacketSoundEffect packetplayoutnamedsoundeffect);

    void handleCustomSound(SPacketCustomSound packetplayoutcustomsoundeffect);

    void handleCollectItem(SPacketCollectItem packetplayoutcollect);

    void handleEntityTeleport(SPacketEntityTeleport packetplayoutentityteleport);

    void handleEntityProperties(SPacketEntityProperties packetplayoutupdateattributes);

    void handleEntityEffect(SPacketEntityEffect packetplayoutentityeffect);

    void handleCombatEvent(SPacketCombatEvent packetplayoutcombatevent);

    void handleServerDifficulty(SPacketServerDifficulty packetplayoutserverdifficulty);

    void handleCamera(SPacketCamera packetplayoutcamera);

    void handleWorldBorder(SPacketWorldBorder packetplayoutworldborder);

    void handleTitle(SPacketTitle packetplayouttitle);

    void handlePlayerListHeaderFooter(SPacketPlayerListHeaderFooter packetplayoutplayerlistheaderfooter);

    void handleResourcePack(SPacketResourcePackSend packetplayoutresourcepacksend);

    void handleUpdateBossInfo(SPacketUpdateBossInfo packetplayoutboss);

    void handleCooldown(SPacketCooldown packetplayoutsetcooldown);

    void handleMoveVehicle(SPacketMoveVehicle packetplayoutvehiclemove);

    void handleAdvancementInfo(SPacketAdvancementInfo packetplayoutadvancements);

    void handleSelectAdvancementsTab(SPacketSelectAdvancementsTab packetplayoutselectadvancementtab);

    void func_194307_a(SPacketPlaceGhostRecipe packetplayoutautorecipe);
}
