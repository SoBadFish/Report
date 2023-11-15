package org.sobadfish.report.manager;

import org.sobadfish.report.config.PlayerInfo;

import java.util.ArrayList;

/**
 * @author SoBadFish
 * 2022/1/21
 */
public class PlayerInfoManager implements Runnable{

    private final ArrayList<PlayerInfo> playerInfos = new ArrayList<>();

    public void addPlayerInfo(PlayerInfo playerInfo){
        if(!playerInfos.contains(playerInfo)) {
            playerInfos.add(playerInfo);
        }
    }

    public PlayerInfo getInfo(String player){
        if(playerInfos.contains(new PlayerInfo(player))){
            return playerInfos.get(playerInfos.indexOf(new PlayerInfo(player)));
        }
        return null;
    }

    @Override
    public void run() {
        for(PlayerInfo info : playerInfos){
            info.reduceCold();
        }
    }
}
