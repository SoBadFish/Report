package org.sobadfish.report.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import org.sobadfish.report.ReportMainClass;
import org.sobadfish.report.form.DisplayCustomForm;
import org.sobadfish.report.form.DisplayForm;
import org.sobadfish.report.form.DisplayHistoryForm;

import java.util.List;


/**
 * @author SoBadFish
 * 2022/1/21
 */
public class ReportCommand extends Command {

    public ReportCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if(strings.length == 0) {
            if (commandSender instanceof Player) {
                DisplayCustomForm displayCustomForm = new DisplayCustomForm(DisplayCustomForm.getRid());
                displayCustomForm.disPlay((Player) commandSender,null);
            }else{
                ReportMainClass.sendMessageToObject("&c请不要在控制台执行", commandSender);
            }
            return true;
        }
        String cmd = strings[0];
        if("r".equalsIgnoreCase(cmd)){
            if(strings.length > 1){
                DisplayCustomForm displayCustomForm = new DisplayCustomForm(DisplayCustomForm.getRid());
                displayCustomForm.disPlay((Player) commandSender,strings[1]);
                return true;
            }
        }
        if(commandSender.isOp() || ReportMainClass.getMainClass().getAdminPlayers().contains(commandSender.getName())){

            switch (cmd){
                case "a":
                    if(commandSender instanceof Player){
                        DisplayForm displayFrom = new DisplayForm(DisplayForm.getRid());
                        displayFrom.disPlay((Player) commandSender);
                    }else{
                        ReportMainClass.sendMessageToObject("&c请不要在控制台执行", commandSender);
                    }
                    break;
                case "p":
                    if(commandSender.isOp()){
                        if(strings.length > 1){
                            String player = strings[1];
                            Player online = Server.getInstance().getPlayer(player);
                            if(online != null){
                                player = online.getName();
                            }
                            List<String> players = ReportMainClass.getMainClass().getAdminPlayers();
                            if(players.contains(player)){
                                players.remove(player);
                                if(online != null){
                                    ReportMainClass.sendMessageToObject("&c你已不是协管！",online);
                                }
                                ReportMainClass.sendMessageToObject("&a成功取消玩家 "+player+" 的协管身份", commandSender);
                            }else{
                                players.add(player);
                                ReportMainClass.getMainClass().saveAdminPlayers();
                                if(online != null){
                                    ReportMainClass.sendMessageToObject("&a你已成为协管！",online);
                                    ReportMainClass.sendMessageToObject("&a成功增加玩家 "+player+" 的协管身份", commandSender);
                                }
                            }

                        }
                    }

                    break;
                case "m":
                    if(strings.length > 2){
                        String player = strings[1];
                        String msg = strings[2];
                        Player online = Server.getInstance().getPlayer(player);
                        if(online != null){
                            ReportMainClass.sendMessageToObject("&c您已被警告！&7 (&r"+msg+"&r&7)",online);
                        }

                    }
                    break;
                case "h":
                    if(commandSender instanceof Player){
                        String player = null;
                        if(strings.length > 1){
                            player = strings[1];
                        }
                        DisplayHistoryForm displayHistoryForm = new DisplayHistoryForm(DisplayHistoryForm.getRid());
                        displayHistoryForm.disPlay((Player) commandSender, player);
                    }

                    break;
                default:
                    ReportMainClass.sendMessageToObject("&a帮助: &7/rp a &7管理举报面板", commandSender);
                    if(commandSender.isOp()){
                        ReportMainClass.sendMessageToObject("&a帮助: &7/rp p &e<player> &7设置玩家为协管", commandSender);
                    }
                    ReportMainClass.sendMessageToObject("&a帮助: &7/rp m &e<player> <信息> &7向玩家发送警告信息", commandSender);
                    ReportMainClass.sendMessageToObject("&a帮助: &7/rp h <player> &7查看处理记录", commandSender);
                    break;
            }
        }
        return true;
    }
}
