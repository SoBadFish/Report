package org.sobadfish.report.manager;

import cn.nukkit.utils.Config;
import org.sobadfish.report.ReportMainClass;
import org.sobadfish.report.config.Report;
import org.sobadfish.report.tools.Utils;

import java.util.*;

/**
 * @author Sobadfish
 * @date 2023/11/14
 */
public class ReportYamlManager implements IDataManager{

    public int sid;

    public ReportMainClass reportMainClass;

    public Config config;

    public List<Report> reports = new ArrayList<>();

    public List<Report> historyReports = new ArrayList<>();


    public ReportYamlManager(ReportMainClass mainClass){
        this.reportMainClass = mainClass;
        mainClass.saveResource("report.yml",false);
        config = new Config(mainClass.getDataFolder()+"/report.yml",Config.YAML);
        init();
    }

    public void init(){
        int id = -1;
        for(Map<?,?> map: config.getMapList("report")){

            String player = "";
            String time = "";
            String reportMessage = "";
            String target = "";
            String manager = "";
            String managerMsg = "";
            String managerTime = "";
            if(map.containsKey("id")){
                id = Integer.parseInt(map.get("id").toString());
            }else{
                id++;
            }
            sid = id;
            if(map.containsKey("player")){
                player = map.get("player").toString();
            }
            if(map.containsKey("time")){
                time = map.get("time").toString();
            }
            if(map.containsKey("reportMessage")){
                reportMessage = map.get("reportMessage").toString();
            }
            if(map.containsKey("target")){
                target = map.get("target").toString();
            }
            if(map.containsKey("manager")){
                manager = map.get("manager").toString();
            }
            if(map.containsKey("managerMsg")){
                managerMsg = map.get("managerMsg").toString();
            }
            if(map.containsKey("managerTime")){
                managerTime = map.get("managerTime").toString();
            }
            Report report;
            if(map.containsKey("delete") && "1".equalsIgnoreCase(map.get("delete").toString())){
                report = new Report(id,player,time,reportMessage, target,manager,managerMsg);
                report.setManagerTime(managerTime);
                historyReports.add(report);
            }else{
                report = new Report(id,player,time,reportMessage, target,manager,managerMsg);
                report.setManagerTime(managerTime);
                reports.add(report);
            }

        }

    }

    @Override
    public void pullReport(String player, String msg, String target) {
        reports.add(new Report(sid+1,player,Utils.getDateToString(new Date()), msg, target,"",""));
        save();
    }

    @Override
    public List<String> getPlayers() {
        List<String> players = new ArrayList<>();
        for(Report report: reports){
            if(!players.contains(report.getPlayer())){
                players.add(report.getPlayer());
            }
        }
        return players;

    }

    @Override
    public List<String> getHistoryPlayers(String player,String target) {
        List<String> players = new ArrayList<>();
        for(Report report: historyReports){

            if(!players.contains(report.getPlayer())){
                if(player != null){
                    if(report.getManager().equalsIgnoreCase(player)){
                        if(target != null){
                            if(report.getPlayer().equalsIgnoreCase(target)){
                                players.add(report.getPlayer());
                            }
                        }else{
                            players.add(report.getPlayer());
                        }
                    }
                }else{
                    if(target != null){
                        if(report.getPlayer().equalsIgnoreCase(target)){
                            players.add(report.getPlayer());
                        }
                    }else{
                        players.add(report.getPlayer());
                    }
                }




            }
        }
        return players;
    }

    @Override
    public ArrayList<Report> getReports(String player) {
        ArrayList<Report> delegates = new ArrayList<>();
        for (Report report : reports){
            if(player != null){
                if(report.delete == 0 && report.getPlayer().equalsIgnoreCase(player)){
                    delegates.add(report);
                }
            }else{
                if(report.delete == 0){
                    delegates.add(report);
                }
            }

        }
        return delegates;
    }

    @Override
    public void delTarget(int id,String player,String admin,String msg) {

        if(player == null){
            reports.clear();
        }
        for(Report report: reports){
            if(report.getPlayer().equalsIgnoreCase(player) && report.getId() == id){
                report.setManager(admin);
                report.setManagerMsg(msg);
                report.setManagerTime(Utils.getDateToString(new Date()));
                report.delete = 1;
                historyReports.add(report);
            }
        }
        save();

    }

    @Override
    public ArrayList<Report> getHistoryReports(String player) {
        ArrayList<Report> reports = new ArrayList<>();
        for(Report report : new ArrayList<>(historyReports)){
            if(report.getPlayer().equalsIgnoreCase(player)){
                reports.add(report);
            }
        }
        return reports;
    }

    private void save(){
        List<Map<String, Object>> cMap = new ArrayList<>();
        for(Report report: reports){
            Map<String, Object> smap = new LinkedHashMap<>();
            smap.put("id",report.getId());
            smap.put("player",report.getPlayer());
            smap.put("time",report.getTime());
            smap.put("reportMessage",report.getReportMessage());
            smap.put("target",report.getTarget());
            smap.put("manager",report.getManager());
            smap.put("managerMsg",report.getManagerMsg());
            smap.put("delete",report.delete);
            smap.put("managerTime",report.getManagerTime());
            cMap.add(smap);
        }

        this.config.set("report",cMap);
        this.config.save();
    }
}
