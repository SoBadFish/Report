package org.sobadfish.report.form;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.utils.TextFormat;
import org.sobadfish.report.ReportMainClass;
import org.sobadfish.report.config.Report;
import org.sobadfish.report.tools.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Sobadfish
 * @date 2023/11/14
 */
public class DisplayHistoryForm {

    private ArrayList<BaseClickButton> clickButtons = new ArrayList<>();

    public static LinkedHashMap<Player, DisplayHistoryForm> DISPLAY_FROM = new LinkedHashMap<>();

    private final int id;

    public static int getRid(){
        return Utils.rand(31000,41000);
    }

    public ArrayList<BaseClickButton> getClickButtons() {
        return clickButtons;
    }

    public String target;

    public DisplayHistoryForm(int id){
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public void disPlay(Player player,String target) {
        FormWindowSimple simple = new FormWindowSimple(TextFormat.colorize('&', "&b举报系统 &7—— &e记录"), "");

        int reportsSize = ReportMainClass.getDataManager().getHistoryPlayers(player.getName(),target).size();

        List<String> reportsList =  ReportMainClass.getDataManager().getHistoryPlayers(null,target);

        if(reportsList.size() == 0){
            ReportMainClass.sendMessageToObject("&c暂无处理记录",player);
            return;
        }
        ArrayList<BaseClickButton> buttons = new ArrayList<>();
        String str = "服务器累计处理 "+reportsList.size()+" 条举报! 您已处理 "+reportsSize+" 条";
        simple.setContent(str);
        for(String s:reportsList){
            List<Report> reps = ReportMainClass.getDataManager().getHistoryReports(s);
            Report rp = reps.get(0);
            String s2 = s+"\n&c[New]"+rp.getManagerTime()+" &r"+reps.size()+" &2条举报记录";
           buttons.add(new BaseClickButton(new ElementButton(TextFormat.colorize('&',s2),new ElementButtonImageData("path"
                   ,"textures/ui/Friend2")),s) {
               @Override
               public void onClick(Player player) {
                   FormWindowSimple simple1 = new FormWindowSimple(TextFormat.colorize('&',"&b举报系统 &7—— &e记录 &7—— &2"+getTarget())
                           ,"");
                   String target = getTarget();
                   StringBuilder stringBuilder = new StringBuilder();
                   List<Report> reports = ReportMainClass.getDataManager().getHistoryReports(target);
                   stringBuilder.append("&l&r被举报玩家: &r&a").append(target).append("&r\n\n");
                   stringBuilder.append("&r&l举报原因:&r \n");
                   for(Report report: reports){
                       String[] rel = Utils.splitMsg(report.getReportMessage());

                       stringBuilder.append(" &7[").append(report.getTime()).append("]&r &e")
                               .append(report.getTarget()).append(" &7:>>&r\n").append("&7(&l")
                               .append(rel[0]).append("&r&7)&r: ").append(rel[1]).append("&r\n");
                   }
                   stringBuilder.append("\n&r&l处理记录: ").append("\n");
                   StringBuilder mg = new StringBuilder();
                   for(Report report: reports){
                       if(!"".equalsIgnoreCase(report.getManager())){
                           mg.append(" &l&7[&e").append(report.getManagerTime()).append("&7] &2")
                                   .append(report.getManager()).append("&r: ").append(report.getManagerMsg()).append("&r\n");
                       }

                   }
                   stringBuilder.append(mg);
                   simple1.setContent(TextFormat.colorize('&',stringBuilder.toString()));
                   player.showFormWindow(simple1,getRid());
               }
           });

        }
        for(BaseClickButton button: buttons){
            simple.addButton(button.getButton());
        }

        setClickButtons(buttons);
        player.showFormWindow(simple,getId());
        DISPLAY_FROM.put(player,this);


    }

    public void setClickButtons(ArrayList<BaseClickButton> clickButtons) {
        this.clickButtons = clickButtons;
    }

}
