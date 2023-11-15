package org.sobadfish.report.form;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import org.sobadfish.report.ReportMainClass;
import org.sobadfish.report.config.PlayerInfo;
import org.sobadfish.report.tools.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Sobadfish
 * @date 2023/11/14
 */
public class DisplayCustomForm {

    private final int id;

    public static int getRid(){
        return Utils.rand(11900,21000);
    }

    public DisplayCustomForm(int id){
        this.id = id;
    }



    public int getId() {
        return id;
    }

    public static LinkedHashMap<Player, DisplayCustomForm> DISPLAY_FROM = new LinkedHashMap<>();

    public void disPlay(Player player, String target){

        FormWindowCustom formWindowCustom = new FormWindowCustom(TextFormat.colorize('&',"&b举报系统"));
        Config msg = ReportMainClass.getMainClass().getMessageConfig();
        formWindowCustom.addElement(new ElementLabel(msg.getString("report-form-msg")));
        List<String> onLinePlayers = new ArrayList<>();
        for(Player online: Server.getInstance().getOnlinePlayers().values()){
            if(online.equals(player)){
                continue;
            }
            onLinePlayers.add(online.getName());
        }
        if(target != null){
            onLinePlayers.add(target);
        }

        if(onLinePlayers.size() == 0){
            ReportMainClass.sendMessageToObject("&c没有可以举报的玩家",player);
            return;
        }

        formWindowCustom.addElement(new ElementDropdown("请选择举报的玩家",onLinePlayers,onLinePlayers.size()-1));
        formWindowCustom.addElement(new ElementDropdown("请选择举报原因",msg.getStringList("report-list")));
        formWindowCustom.addElement(new ElementInput("请输入举报理由"));

        player.showFormWindow(formWindowCustom,getId());
        DISPLAY_FROM.put(player,this);

    }

    public void onListener(Player player,FormResponseCustom responseCustom){
        PlayerInfo info = ReportMainClass.getMainClass().getPlayerInfoManager().getInfo(player.getName());
        String playerName = responseCustom.getDropdownResponse(1).getElementContent();
        String msg =  responseCustom.getDropdownResponse(2).getElementContent()+"&"+responseCustom.getInputResponse(3).replace("&","");
        if(info != null){
            switch (info.addReport(playerName)){
                case SUCCESS:
                    ReportMainClass.getDataManager().pullReport(playerName,msg,player.getName());
                    ReportMainClass.sendMessageToObject("&b举报成功！ 感谢你对服务器建设的支持 &7(等待管理员处理)",player);
                    break;
                case COLD:
                    ReportMainClass.sendMessageToObject("&c举报太频繁了，请在 &r "+info.getCold()+" &c秒后重试",player);
                    break;
                case DAY_MAX:
                    ReportMainClass.sendMessageToObject("&c今日举报次数达到上限，请明天再进行举报吧",player);
                    break;
                case HAS_TARGET:
                    ReportMainClass.sendMessageToObject("&c你今日已经举报过他了",player);
                    break;
                case MY:
                    ReportMainClass.sendMessageToObject("&c你不能举报自己",player);
                    break;
                default:break;
            }
        }

    }



}
