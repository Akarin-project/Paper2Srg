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

    void func_147235_a(SPacketSpawnObject packetplayoutspawnentity);

    void func_147286_a(SPacketSpawnExperienceOrb packetplayoutspawnentityexperienceorb);

    void func_147292_a(SPacketSpawnGlobalEntity packetplayoutspawnentityweather);

    void func_147281_a(SPacketSpawnMob packetplayoutspawnentityliving);

    void func_147291_a(SPacketScoreboardObjective packetplayoutscoreboardobjective);

    void func_147288_a(SPacketSpawnPainting packetplayoutspawnentitypainting);

    void func_147237_a(SPacketSpawnPlayer packetplayoutnamedentityspawn);

    void func_147279_a(SPacketAnimation packetplayoutanimation);

    void func_147293_a(SPacketStatistics packetplayoutstatistic);

    void func_191980_a(SPacketRecipeBook packetplayoutrecipes);

    void func_147294_a(SPacketBlockBreakAnim packetplayoutblockbreakanimation);

    void func_147268_a(SPacketSignEditorOpen packetplayoutopensigneditor);

    void func_147273_a(SPacketUpdateTileEntity packetplayouttileentitydata);

    void func_147261_a(SPacketBlockAction packetplayoutblockaction);

    void func_147234_a(SPacketBlockChange packetplayoutblockchange);

    void func_147251_a(SPacketChat packetplayoutchat);

    void func_147274_a(SPacketTabComplete packetplayouttabcomplete);

    void func_147287_a(SPacketMultiBlockChange packetplayoutmultiblockchange);

    void func_147264_a(SPacketMaps packetplayoutmap);

    void func_147239_a(SPacketConfirmTransaction packetplayouttransaction);

    void func_147276_a(SPacketCloseWindow packetplayoutclosewindow);

    void func_147241_a(SPacketWindowItems packetplayoutwindowitems);

    void func_147265_a(SPacketOpenWindow packetplayoutopenwindow);

    void func_147245_a(SPacketWindowProperty packetplayoutwindowdata);

    void func_147266_a(SPacketSetSlot packetplayoutsetslot);

    void func_147240_a(SPacketCustomPayload packetplayoutcustompayload);

    void func_147253_a(SPacketDisconnect packetplayoutkickdisconnect);

    void func_147278_a(SPacketUseBed packetplayoutbed);

    void func_147236_a(SPacketEntityStatus packetplayoutentitystatus);

    void func_147243_a(SPacketEntityAttach packetplayoutattachentity);

    void func_184328_a(SPacketSetPassengers packetplayoutmount);

    void func_147283_a(SPacketExplosion packetplayoutexplosion);

    void func_147252_a(SPacketChangeGameState packetplayoutgamestatechange);

    void func_147272_a(SPacketKeepAlive packetplayoutkeepalive);

    void func_147263_a(SPacketChunkData packetplayoutmapchunk);

    void func_184326_a(SPacketUnloadChunk packetplayoutunloadchunk);

    void func_147277_a(SPacketEffect packetplayoutworldevent);

    void func_147282_a(SPacketJoinGame packetplayoutlogin);

    void func_147259_a(SPacketEntity packetplayoutentity);

    void func_184330_a(SPacketPlayerPosLook packetplayoutposition);

    void func_147289_a(SPacketParticles packetplayoutworldparticles);

    void func_147270_a(SPacketPlayerAbilities packetplayoutabilities);

    void func_147256_a(SPacketPlayerListItem packetplayoutplayerinfo);

    void func_147238_a(SPacketDestroyEntities packetplayoutentitydestroy);

    void func_147262_a(SPacketRemoveEntityEffect packetplayoutremoveentityeffect);

    void func_147280_a(SPacketRespawn packetplayoutrespawn);

    void func_147267_a(SPacketEntityHeadLook packetplayoutentityheadrotation);

    void func_147257_a(SPacketHeldItemChange packetplayouthelditemslot);

    void func_147254_a(SPacketDisplayObjective packetplayoutscoreboarddisplayobjective);

    void func_147284_a(SPacketEntityMetadata packetplayoutentitymetadata);

    void func_147244_a(SPacketEntityVelocity packetplayoutentityvelocity);

    void func_147242_a(SPacketEntityEquipment packetplayoutentityequipment);

    void func_147295_a(SPacketSetExperience packetplayoutexperience);

    void func_147249_a(SPacketUpdateHealth packetplayoutupdatehealth);

    void func_147247_a(SPacketTeams packetplayoutscoreboardteam);

    void func_147250_a(SPacketUpdateScore packetplayoutscoreboardscore);

    void func_147271_a(SPacketSpawnPosition packetplayoutspawnposition);

    void func_147285_a(SPacketTimeUpdate packetplayoutupdatetime);

    void func_184327_a(SPacketSoundEffect packetplayoutnamedsoundeffect);

    void func_184329_a(SPacketCustomSound packetplayoutcustomsoundeffect);

    void func_147246_a(SPacketCollectItem packetplayoutcollect);

    void func_147275_a(SPacketEntityTeleport packetplayoutentityteleport);

    void func_147290_a(SPacketEntityProperties packetplayoutupdateattributes);

    void func_147260_a(SPacketEntityEffect packetplayoutentityeffect);

    void func_175098_a(SPacketCombatEvent packetplayoutcombatevent);

    void func_175101_a(SPacketServerDifficulty packetplayoutserverdifficulty);

    void func_175094_a(SPacketCamera packetplayoutcamera);

    void func_175093_a(SPacketWorldBorder packetplayoutworldborder);

    void func_175099_a(SPacketTitle packetplayouttitle);

    void func_175096_a(SPacketPlayerListHeaderFooter packetplayoutplayerlistheaderfooter);

    void func_175095_a(SPacketResourcePackSend packetplayoutresourcepacksend);

    void func_184325_a(SPacketUpdateBossInfo packetplayoutboss);

    void func_184324_a(SPacketCooldown packetplayoutsetcooldown);

    void func_184323_a(SPacketMoveVehicle packetplayoutvehiclemove);

    void func_191981_a(SPacketAdvancementInfo packetplayoutadvancements);

    void func_194022_a(SPacketSelectAdvancementsTab packetplayoutselectadvancementtab);

    void func_194307_a(SPacketPlaceGhostRecipe packetplayoutautorecipe);
}
