package com.github.tacowasa059.voicechatteamlimiter.Accessor;

public interface PlayerTeamAccessor {
    void setAllowVoiceChat(boolean flag);

    boolean isAllowVoiceChat();

    void setOnlySameTeamVC(boolean flag);

    boolean isOnlySameTeamVC();
}
