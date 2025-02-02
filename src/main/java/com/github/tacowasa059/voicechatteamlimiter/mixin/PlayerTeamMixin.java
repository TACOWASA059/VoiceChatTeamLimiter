package com.github.tacowasa059.voicechatteamlimiter.mixin;


import com.github.tacowasa059.voicechatteamlimiter.Accessor.PlayerTeamAccessor;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerTeam.class)
public class PlayerTeamMixin implements PlayerTeamAccessor {
    @Shadow
    private Scoreboard scoreboard;

    private boolean allowVoiceChat = false;

    private boolean onlySameTeamVC = false;
    @Override
    public void setAllowVoiceChat(boolean flag) {
        PlayerTeam playerTeam = (PlayerTeam)(Object)this;
        allowVoiceChat = flag;
        this.scoreboard.onTeamChanged(playerTeam);
    }

    @Override
    public boolean isAllowVoiceChat() {
        return allowVoiceChat;
    }

    @Override
    public void setOnlySameTeamVC(boolean flag) {
        PlayerTeam playerTeam = (PlayerTeam)(Object)this;
        onlySameTeamVC = flag;
        this.scoreboard.onTeamChanged(playerTeam);
    }

    @Override
    public boolean isOnlySameTeamVC() {
        return onlySameTeamVC;
    }
}
