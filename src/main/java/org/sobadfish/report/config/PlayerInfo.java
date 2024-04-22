package org.sobadfish.report.config;

import org.sobadfish.report.ReportMainClass;
import org.sobadfish.report.tools.Utils;


import java.util.ArrayList;
import java.util.Date;


/**
 * @author SoBadFish
 * 2022/1/21
 */
public class PlayerInfo {

    private String playerName;

    private int cold = 0;

    private int reportCount = 0;

    private Date createData;

    private ArrayList<String> targetName = new ArrayList<>();

    public PlayerInfo(String playerName){
        this.playerName = playerName;
        this.createData = new Date();
    }

    public void reduceCold(){
        if(cold > 0){
            cold--;
        }
        if(Utils.getDifferDay(createData) >= 1){
            reset();
        }
    }



    public Date getCreateData() {
        return createData;
    }

    public ReportType addReport(String target){
        if(target.equalsIgnoreCase(playerName)){
            return ReportType.MY;
        }
        if(targetName.contains(target)){
            return ReportType.HAS_TARGET;
        }
        if(reportCount ==  ReportMainClass.getMainClass().getReportConfig().getDayMax()){
            return ReportType.DAY_MAX;
        }
        if(cold != 0){
            return ReportType.COLD;
        }
        targetName.add(target);
        reportCount++;
        cold = ReportMainClass.getMainClass().getReportConfig().getCold();
//        ReportMainClass.sendMessageToAdmin("&r玩家 &2"+playerName+" &r 举报了 &e"+target+" ");
        return ReportType.SUCCESS;
    }

    public int getCold() {
        return cold;
    }

    public int getReportCount() {
        return reportCount;
    }

    public ArrayList<String> getTargetName() {
        return targetName;
    }

    private void reset(){
        createData = new Date();
        cold = 0;
        reportCount = 0;
        targetName.clear();
    }

    public String getPlayerName() {
        return playerName;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PlayerInfo){
            return ((PlayerInfo) obj).playerName.equalsIgnoreCase(playerName);
        }
        return false;
    }

    public enum ReportType{
        /**
         * 举报成功
         * */
        SUCCESS,
        /**
         * 存在举报的玩家
         * */
        HAS_TARGET,
        /**
         * 到达今日上限
         * */
        DAY_MAX,
        /**
         * 冷却中
         * */
        COLD,
        /**
         * 自己举报自己
         * */
        MY

    }
}
