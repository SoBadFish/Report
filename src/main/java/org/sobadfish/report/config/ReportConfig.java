package org.sobadfish.report.config;

import cn.nukkit.utils.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author SoBadFish
 * 2022/1/21
 */
public class ReportConfig {

    private final int dayMax;

    private final int cold;

    private final List<ReportBtn> reportBtns = new ArrayList<>();


    public ReportConfig(Config config){
        this.dayMax = config.getInt("day-max",20);
        this.cold = config.getInt("cold",2);
        List<Map> c =  config.getMapList("report-btn");
        for(Map<?,?> e: c){
            reportBtns.add(new ReportBtn(e.get("name").toString(),e.get("cmd").toString()));
        }
    }

    public List<ReportBtn> getReportBtns() {
        return reportBtns;
    }

    public int getCold() {
        return cold;
    }

    public int getDayMax() {
        return dayMax;
    }

    public static class ReportBtn{
        public String name;

        public String cmd;

        public ReportBtn(String name,String cmd){
            this.name = name;
            this.cmd = cmd;
        }
    }
}
