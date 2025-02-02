package com.github.tacowasa059.voicechatteamlimiter;

import com.github.tacowasa059.voicechatteamlimiter.Accessor.PlayerTeamAccessor;
import de.maxhenkel.voicechat.api.*;
import de.maxhenkel.voicechat.api.events.EntitySoundPacketEvent;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;


@ForgeVoicechatPlugin
public class VoiceChatPlugin implements VoicechatPlugin {

    public static VoicechatApi voicechatApi;

    public static VoicechatServerApi voicechatServerApi;


    /**
     * @return the unique ID for this voice chat plugin
     */
    @Override
    public String getPluginId() {
        return VoiceChatTeamLimiter.MODID;
    }

    /**
     * Called when the voice chat initializes the plugin.
     *
     * @param api the voice chat API
     */
    @Override
    public void initialize(VoicechatApi api) {
        voicechatApi = api;
    }

    /**
     * Called once by the voice chat to register all events.
     *
     * @param registration the event registration
     */
    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(MicrophonePacketEvent.class, this::onMicrophonePacket,1);
        registration.registerEvent(EntitySoundPacketEvent.class, this::onEntitySoundPacket,2);
    }


    public void onEntitySoundPacket(EntitySoundPacketEvent event){
        // sender
        VoicechatConnection senderConnection = event.getSenderConnection();

        if (senderConnection == null) {
            return;
        }
        if (!(senderConnection.getPlayer().getPlayer() instanceof ServerPlayer player)) {
            return;
        }

        Team team = player.getTeam();
        if(!(team instanceof PlayerTeam playerTeam)){
            return;
        }


        PlayerTeamAccessor playerTeamAccessor = (PlayerTeamAccessor) playerTeam;
        if(!playerTeamAccessor.isOnlySameTeamVC()){
            return;
        }

        // receiver
        VoicechatConnection receiverConnection = event.getReceiverConnection();

        if (receiverConnection == null){
            return;
        }

        if (!(receiverConnection.getPlayer().getPlayer() instanceof ServerPlayer receiver)) {
            return;
        }

        Team receiveTeam = receiver.getTeam();
        if(receiveTeam == null){
//            player.sendSystemMessage(Component.literal(ChatFormatting.RED + "(無所属)レシーバーにはとどきませんでした"));
            event.cancel();
            return;
        }
        if (!team.equals(receiveTeam)) {
//            player.sendSystemMessage(Component.literal(ChatFormatting.RED + "(チームあり)レシーバーが同じチームにいませんでした"));
            event.cancel();
            return;
        }
    }
    /**
     *
     * @param event MicrophonePacketEvent
     */
    public void onMicrophonePacket(MicrophonePacketEvent event){
        // sender
        VoicechatConnection senderConnection = event.getSenderConnection();

        if (senderConnection == null) {
            return;
        }
        if (!(senderConnection.getPlayer().getPlayer() instanceof ServerPlayer player)) {
            return;
        }

        // チームに入っている and VC許可
        Team team = player.getTeam();
        if(team instanceof PlayerTeam playerTeam){
            PlayerTeamAccessor playerTeamAccessor = (PlayerTeamAccessor) playerTeam;
            if(!playerTeamAccessor.isAllowVoiceChat()) { //ボイスチャットが許可されてないときはダメ
                player.sendSystemMessage(Component.literal(ChatFormatting.RED + "(チームルール)ボイスチャットが許可されていません"));
                event.cancel();
                return;
            }
        }else{
            player.sendSystemMessage(Component.literal(ChatFormatting.RED + "(無所属)ボイスチャットが許可されていません"));
            event.cancel();
            return;
        }

    }
}
