package com.github.tacowasa059.voicechatteamlimiter.mixin;

import com.github.tacowasa059.voicechatteamlimiter.Accessor.PlayerTeamAccessor;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.TeamArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.TeamCommand;
import net.minecraft.world.scores.PlayerTeam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TeamCommand.class)
public class TeamCommandMixin {
    @Inject(method = "register", at = @At("RETURN"))
    private static void injectRegister(CommandDispatcher<CommandSourceStack> dispatcher, CallbackInfo ci) {
        dispatcher.register(Commands.literal("team")
                .requires(src -> src.hasPermission(2))
                .then(Commands.literal("modify")
                        .then(Commands.argument("team", TeamArgument.team())
                                .then(Commands.literal("allowVoiceChat")
                                        .then(Commands.argument("allowed", BoolArgumentType.bool())
                                                .executes(ctx -> {
                                                    PlayerTeam team = TeamArgument.getTeam(ctx, "team");
                                                    if (team instanceof PlayerTeamAccessor accessor) {
                                                        boolean value = BoolArgumentType.getBool(ctx, "allowed");
                                                        boolean value2 = accessor.isAllowVoiceChat();
                                                        if(value == value2){
                                                            ctx.getSource().sendFailure(Component.translatable("commands.team.option.allowVoiceChat.alreadySet"));
                                                        }
                                                        else{
                                                            accessor.setAllowVoiceChat(value);
                                                            ctx.getSource().sendSuccess(() ->
                                                                    Component.translatable("commands.team.option.allowVoiceChat.success", team.getFormattedDisplayName(), value), true);
                                                        }
                                                    }
                                                    return 1;
                                                })
                                        )
                                )
                                .then(Commands.literal("onlySameTeamVC")
                                        .then(Commands.argument("only", BoolArgumentType.bool())
                                                .executes(ctx -> {
                                                    PlayerTeam team = TeamArgument.getTeam(ctx, "team");
                                                    if (team instanceof PlayerTeamAccessor accessor) {
                                                        boolean value = BoolArgumentType.getBool(ctx, "only");
                                                        boolean value2 = accessor.isOnlySameTeamVC();

                                                        if(value == value2){
                                                            ctx.getSource().sendFailure(Component.translatable("commands.team.option.onlySameTeamVC.alreadySet"));
                                                        }
                                                        else{
                                                            accessor.setOnlySameTeamVC(value);
                                                            ctx.getSource().sendSuccess(() ->
                                                                    Component.translatable("commands.team.option.onlySameTeamVC.success", team.getFormattedDisplayName(), value), true);

                                                        }
                                                    }
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )
        );
    }
}
