package com.github.tacowasa059.voicechatteamlimiter.mixin;

import com.github.tacowasa059.voicechatteamlimiter.Accessor.PlayerTeamAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.ScoreboardSaveData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;



@Mixin(ScoreboardSaveData.class)
public class ScoreboardSaveDataMixin {
    @Shadow
    private Scoreboard scoreboard;

    @Inject(method = "loadTeams",
            at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/scores/ScoreboardSaveData;loadTeamPlayers(Lnet/minecraft/world/scores/PlayerTeam;Lnet/minecraft/nbt/ListTag;)V"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void injectLoadTeams(ListTag p_83525_, CallbackInfo ci, int i, CompoundTag compoundtag, String s, PlayerTeam playerteam, Component component) {
        if (compoundtag.contains("allowVoiceChat", 99)) {

            boolean flag = compoundtag.getBoolean("allowVoiceChat");

            PlayerTeamAccessor playerTeamAccessor = (PlayerTeamAccessor) playerteam;
            playerTeamAccessor.setAllowVoiceChat(flag);
        }

        if(compoundtag.contains("onlySameTeamVC", 99)){
            boolean flag = compoundtag.getBoolean("onlySameTeamVC");
            PlayerTeamAccessor playerTeamAccessor = (PlayerTeamAccessor) playerteam;
            playerTeamAccessor.setOnlySameTeamVC(flag);
        }

    }

    @Inject(method = "saveTeams",
            at = @At("HEAD"),cancellable = true)
    private void injectSaveTeams(CallbackInfoReturnable<ListTag> cir) {

        ListTag listtag = new ListTag();

        for(PlayerTeam playerteam : this.scoreboard.getPlayerTeams()) {
            CompoundTag compoundtag = new CompoundTag();
            compoundtag.putString("Name", playerteam.getName());
            compoundtag.putString("DisplayName", Component.Serializer.toJson(playerteam.getDisplayName()));
            if (playerteam.getColor().getId() >= 0) {
                compoundtag.putString("TeamColor", playerteam.getColor().getName());
            }

            compoundtag.putBoolean("AllowFriendlyFire", playerteam.isAllowFriendlyFire());
            compoundtag.putBoolean("SeeFriendlyInvisibles", playerteam.canSeeFriendlyInvisibles());
            compoundtag.putString("MemberNamePrefix", Component.Serializer.toJson(playerteam.getPlayerPrefix()));
            compoundtag.putString("MemberNameSuffix", Component.Serializer.toJson(playerteam.getPlayerSuffix()));
            compoundtag.putString("NameTagVisibility", playerteam.getNameTagVisibility().name);
            compoundtag.putString("DeathMessageVisibility", playerteam.getDeathMessageVisibility().name);
            compoundtag.putString("CollisionRule", playerteam.getCollisionRule().name);

            PlayerTeamAccessor playerTeamAccessor = (PlayerTeamAccessor) playerteam;
            compoundtag.putBoolean("allowVoiceChat", playerTeamAccessor.isAllowVoiceChat());
            compoundtag.putBoolean("onlySameTeamVC", playerTeamAccessor.isOnlySameTeamVC());

            ListTag listtag1 = new ListTag();

            for(String s : playerteam.getPlayers()) {
                listtag1.add(StringTag.valueOf(s));
            }

            compoundtag.put("Players", listtag1);
            listtag.add(compoundtag);
        }

        cir.setReturnValue(listtag);
        cir.cancel();

    }


}
