package org.sobadfish.report.config;

/**
 * @author SoBadFish
 * 2022/1/21
 */
public class Report {

    private final int id;

    /**
     * 被举报玩家
     * */
    private final String player;

    private final String reportMessage;

    private final String time;

    /**
     * 举报玩家
     * */
    private final String target;

    private String manager;

    public int delete = 0;

    private String managerMsg;

    private String managerTime = "";

    public Report(int id,
                  String player,
                  String time,
                  String reportMessage,
                  String target,
                  String manager,
                  String managerMsg){
        this.id = id;
        this.player = player;
        this.reportMessage = reportMessage;
        this.time = time;
        this.target = target;
        this.manager = manager;
        this.managerMsg = managerMsg;
    }

    public int getId() {
        return id;
    }

    public String getManager() {
        return manager;
    }

    public void setManagerTime(String managerTime) {
        this.managerTime = managerTime;
    }

    public String getManagerTime() {
        return managerTime;
    }

    public String getManagerMsg() {
        return managerMsg;
    }

    public void setManagerMsg(String managerMsg) {
        this.managerMsg = managerMsg;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }


    public String getTime() {
        return time;
    }

    public String getPlayer() {
        return player;
    }

    public String getReportMessage() {
        return reportMessage;
    }

    public String getTarget() {
        return target;
    }


}
