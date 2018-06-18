package net.minecraft.network.play;
import net.minecraft.network.INetHandler;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketEnchantItem;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlaceRecipe;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketRecipeInfo;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.client.CPacketSeenAdvancements;
import net.minecraft.network.play.client.CPacketSpectate;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketVehicleMove;


public interface INetHandlerPlayServer extends INetHandler {

    void handleAnimation(CPacketAnimation packetplayinarmanimation);

    void processChatMessage(CPacketChatMessage packetplayinchat);

    void processTabComplete(CPacketTabComplete packetplayintabcomplete);

    void processClientStatus(CPacketClientStatus packetplayinclientcommand);

    void processClientSettings(CPacketClientSettings packetplayinsettings);

    void processConfirmTransaction(CPacketConfirmTransaction packetplayintransaction);

    void processEnchantItem(CPacketEnchantItem packetplayinenchantitem);

    void processClickWindow(CPacketClickWindow packetplayinwindowclick);

    void func_194308_a(CPacketPlaceRecipe packetplayinautorecipe);

    void processCloseWindow(CPacketCloseWindow packetplayinclosewindow);

    void processCustomPayload(CPacketCustomPayload packetplayincustompayload);

    void processUseEntity(CPacketUseEntity packetplayinuseentity);

    void processKeepAlive(CPacketKeepAlive packetplayinkeepalive);

    void processPlayer(CPacketPlayer packetplayinflying);

    void processPlayerAbilities(CPacketPlayerAbilities packetplayinabilities);

    void processPlayerDigging(CPacketPlayerDigging packetplayinblockdig);

    void processEntityAction(CPacketEntityAction packetplayinentityaction);

    void processInput(CPacketInput packetplayinsteervehicle);

    void processHeldItemChange(CPacketHeldItemChange packetplayinhelditemslot);

    void processCreativeInventoryAction(CPacketCreativeInventoryAction packetplayinsetcreativeslot);

    void processUpdateSign(CPacketUpdateSign packetplayinupdatesign);

    void processTryUseItemOnBlock(CPacketPlayerTryUseItemOnBlock packetplayinuseitem);

    void processTryUseItem(CPacketPlayerTryUseItem packetplayinblockplace);

    void handleSpectate(CPacketSpectate packetplayinspectate);

    void handleResourcePackStatus(CPacketResourcePackStatus packetplayinresourcepackstatus);

    void processSteerBoat(CPacketSteerBoat packetplayinboatmove);

    void processVehicleMove(CPacketVehicleMove packetplayinvehiclemove);

    void processConfirmTeleport(CPacketConfirmTeleport packetplayinteleportaccept);

    void handleRecipeBookUpdate(CPacketRecipeInfo packetplayinrecipedisplayed);

    void handleSeenAdvancements(CPacketSeenAdvancements packetplayinadvancements);
}
