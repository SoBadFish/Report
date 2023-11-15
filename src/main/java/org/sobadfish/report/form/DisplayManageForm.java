package org.sobadfish.report.form;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.utils.TextFormat;
import org.sobadfish.report.ReportMainClass;
import org.sobadfish.report.config.Report;
import org.sobadfish.report.config.ReportConfig;
import org.sobadfish.report.tools.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Sobadfish
 * @date 2023/11/14
 */
public class DisplayManageForm {

    public static LinkedHashMap<Player, DisplayManageForm> DISPLAY_FROM = new LinkedHashMap<>();

    private final int id;

    private int reportId;

    public static int getRid(){
        return Utils.rand(31000,41000);
    }

    public String target;

    public DisplayManageForm(int id){
        this.id = id;
    }


    public int getId() {
        return id;
    }


    public void disPlay(Player player,String target){
        ArrayList<Report> reports = ReportMainClass.getDataManager().getReports(target);
        if(reports.size() == 0){
            return;
        }
        this.target = target;
        reportId = reports.get(0).getId();
        FormWindowCustom formWindowCustom = new FormWindowCustom(TextFormat.colorize('&',"&b举报管理 &7—— &2"+target));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("&l&r被举报玩家: &r&a").append(target).append("&r\n\n");
        stringBuilder.append("&r&l举报原因:&r \n");
        for(Report report: reports){
            String[] rel = report.getReportMessage().split("&");
            stringBuilder.append(" &7[").append(report.getTime()).append("]&r &e")
                    .append(report.getTarget()).append(" &7:>>&r\n").append(" &7(&l")
                    .append(rel[0]).append("&r&7)&r: ").append(rel[1]).append("&r\n");
        }
        formWindowCustom.addElement(new ElementLabel(TextFormat.colorize('&',stringBuilder.toString())));
        List<String> names = new ArrayList<>();
        for(ReportConfig.ReportBtn reportBtn: ReportMainClass.getMainClass().getReportConfig().getReportBtns()){
            names.add(reportBtn.name);
        }
        formWindowCustom.addElement(new ElementDropdown("请选择处理类型",names));
        formWindowCustom.addElement(new ElementInput("请输入处理说明"));
        player.showFormWindow(formWindowCustom,getId());
        DISPLAY_FROM.put(player,this);

    }

    public void onListener(Player player, FormResponseCustom responseCustom){
        if(target == null){
            return;
        }
        String q = responseCustom.getInputResponse(2);
        if("".equalsIgnoreCase(q.trim())){
            q = "无";
        }
        ReportConfig.ReportBtn btn = ReportMainClass.getMainClass().getReportConfig().getReportBtns().get(responseCustom.getDropdownResponse(1).getElementID());
        if(!"".equalsIgnoreCase(btn.cmd)){
            Server.getInstance().getCommandMap().dispatch(new ConsoleCommandSender(),btn.cmd.replace("@msg",q).replace("@target",target));
        }
        ReportMainClass.getDataManager().delTarget(reportId,target, player.getName(), "&7[&e"+btn.name+"&7]&c "+q);
        ReportMainClass.sendMessageToAdmin("&e管理员 &r"+player.getName()+" &e已处理 &r"+target);
    }
}
