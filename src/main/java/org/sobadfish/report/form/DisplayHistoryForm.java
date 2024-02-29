package org.sobadfish.report.form;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.utils.TextFormat;
import org.sobadfish.report.ReportMainClass;
import org.sobadfish.report.config.Report;
import org.sobadfish.report.entity.Page;
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

    private int id;

    private int page = 0;

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


        Page<String> reportsSize = ReportMainClass.getDataManager().getHistoryPlayers(player.getName(),target,0);

        Page<String> reportsList =  ReportMainClass.getDataManager().getHistoryPlayers(null,target,page);

        if(reportsList.total == 0){
            ReportMainClass.sendMessageToObject("&c暂无处理记录",player);
            return;
        }
        ArrayList<BaseClickButton> buttons = new ArrayList<>();
        String str = "服务器累计处理 "+reportsList.total+" 条举报! 您已处理 "+reportsSize.total+" 条";
        simple.setContent(str);
        for(String s:reportsList.data){
            Page<Report> reps = ReportMainClass.getDataManager().getHistoryReports(s,0);
            Report rp = reps.data.get(0);
            String s2 = s+"\n&c[New]"+rp.getManagerTime()+" &r"+reps.total+" &2条举报记录";
           buttons.add(new BaseClickButton(new ElementButton(TextFormat.colorize('&',s2),new ElementButtonImageData("path"
                   ,"textures/ui/Friend2")),s) {
               @Override
               public void onClick(Player player) {
                   FormWindowSimple simple1 = new FormWindowSimple(TextFormat.colorize('&',"&b举报系统 &7—— &e记录 &7—— &2"+getTarget())
                           ,"");
                   String target = getTarget();
                   StringBuilder stringBuilder = new StringBuilder();
                   Page<Report> reports = ReportMainClass.getDataManager().getHistoryReports(target,0);
                   stringBuilder.append("&l&r被举报玩家: &r&a").append(target).append("&r\n\n");
                   stringBuilder.append("&r&l举报原因:&r \n");
                   for(Report report: reports.data){
                       String[] rel = Utils.splitMsg(report.getReportMessage());

                       stringBuilder.append(" &7[").append(report.getTime()).append("]&r &e")
                               .append(report.getTarget()).append(" &7:>>&r\n").append("&7(&l")
                               .append(rel[0]).append("&r&7)&r: ").append(rel[1]).append("&r\n");
                   }
                   stringBuilder.append("\n&r&l处理记录: ").append("\n");
                   StringBuilder mg = new StringBuilder();
                   for(Report report: reports.data){
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

        if(reportsList.total > ReportMainClass.PAGE_SIZE){
            if(page == 0){
                addNext(buttons,target);
            }else if(page * ReportMainClass.PAGE_SIZE < reportsList.total){
                addLast(buttons,target);
                addNext(buttons,target);
            }else{
                addLast(buttons,target);
            }
        }


        for(BaseClickButton button: buttons){
            simple.addButton(button.getButton());
        }


        setClickButtons(buttons);
        player.showFormWindow(simple,getId());
        DISPLAY_FROM.put(player,this);


    }

    private void addLast(ArrayList<BaseClickButton> buttons,String target){
        buttons.add(new BaseClickButton(new ElementButton("上一页", new ElementButtonImageData("path", "textures/ui/arrow_dark_left_stretch")), null) {
            @Override
            public void onClick(Player player) {
                DisplayHistoryForm from = DISPLAY_FROM.get(player);
                from.setId(getRid());
                from.page--;
                from.disPlay(player,target);


            }
        });
    }

    public void setId(int id) {
        this.id = id;
    }

    private void addNext(ArrayList<BaseClickButton> buttons,String target){
        buttons.add(new BaseClickButton(new ElementButton("下一页", new ElementButtonImageData("path", "textures/ui/arrow_dark_right_stretch")), null) {
            @Override
            public void onClick(Player player) {
                DisplayHistoryForm from = DISPLAY_FROM.get(player);
                from.setId(getRid());
                from.page++;
                from.disPlay(player,target);
            }
        });
    }

    public void setClickButtons(ArrayList<BaseClickButton> clickButtons) {
        this.clickButtons = clickButtons;
    }

}
