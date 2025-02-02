# VoiceChatTeamLimiter
## 動作確認環境
```
forge 1.20.1
[FORGE][1.20.1] Simple Voice Chat 1.20.1-2.5.17
```

## 機能
- 無所属のプレイヤーがVCチャットを不可にする
- チームごとに`allowVoiceChat`と`onlySameTeamVC`を設定可能

| チーム変数 | 意味                            |
|-------|-------------------------------|
| allowVoiceChat    | trueのときにチームメンバーがVCを送信することができる |
| onlySameTeamVC    | trueのときにチームメンバー以外にVCが送信されない   |

## コマンド
バニラのコマンドに追加
### allowVoiceChatの変更
```
/team modify <team> allowVoiceChat <true/false>
```
### onlySameTeamVCの変更
```
/team modify <team> onlySameTeamVC <true/false>
```