package org.sobadfish.report;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseSimple;
import org.sobadfish.report.config.PlayerInfo;
import org.sobadfish.report.form.DisplayCustomForm;
import org.sobadfish.report.form.DisplayForm;
import org.sobadfish.report.form.DisplayHistoryForm;
import org.sobadfish.report.form.DisplayManageForm;

import java.util.List;

/**
 * @author SoBadFish
 * 2022/1/21
 */
public class ReportListener implements Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        PlayerInfo playerInfo = new PlayerInfo(event.getPlayer().getName());
        ReportMainClass.getMainClass().getPlayerInfoManager().addPlayerInfo(playerInfo);
        if(event.getPlayer().isOp()){
            List<String> strings = ReportMainClass.getDataManager().getPlayers();
            if(strings.size() > 0) {
                ReportMainClass.sendMessageToObject("&l欢迎管理员来到服务器 当前有&a " + strings.size() + "&r 条举报记录 &e请注意处理", event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onFormListener(PlayerFormRespondedEvent event){
        if(event.wasClosed()){
            return;
        }
        if(DisplayHistoryForm.DISPLAY_FROM.containsKey(event.getPlayer())){
            DisplayHistoryForm form = DisplayHistoryForm.DISPLAY_FROM.get(event.getPlayer());
            if(form.getId() == event.getFormID()){
                if(event.getResponse() instanceof FormResponseSimple){
                    form.getClickButtons().get(((FormResponseSimple) event.getResponse())
                            .getClickedButtonId()).onClick(event.getPlayer());
                }
            }
        }
        if(DisplayForm.DISPLAY_FROM.containsKey(event.getPlayer())){

            DisplayForm form = DisplayForm.DISPLAY_FROM.get(event.getPlayer());
            if(form.getId() == event.getFormID()){
                if(event.getResponse() instanceof FormResponseSimple){
                    form.getClickButtons().get(((FormResponseSimple) event.getResponse())
                            .getClickedButtonId()).onClick(event.getPlayer());
                }
                DisplayForm.DISPLAY_FROM.remove(event.getPlayer());
            }else{
                DisplayForm.DISPLAY_FROM.remove(event.getPlayer());
            }

            return;
        }
        if(DisplayCustomForm.DISPLAY_FROM.containsKey(event.getPlayer())){
            DisplayCustomForm form = DisplayCustomForm.DISPLAY_FROM.get(event.getPlayer());
            if(form.getId() == event.getFormID()){
                if(event.getResponse() instanceof FormResponseCustom){
                    form.onListener(event.getPlayer(), (FormResponseCustom) event.getResponse());

                }
                DisplayCustomForm.DISPLAY_FROM.remove(event.getPlayer());
            }else{
                DisplayCustomForm.DISPLAY_FROM.remove(event.getPlayer());
            }
        }
        if(DisplayManageForm.DISPLAY_FROM.containsKey(event.getPlayer())){
            DisplayManageForm form = DisplayManageForm.DISPLAY_FROM.get(event.getPlayer());
            if(form.getId() == event.getFormID()){
                if(event.getResponse() instanceof FormResponseCustom){
                    form.onListener(event.getPlayer(), (FormResponseCustom) event.getResponse());

                }
                DisplayManageForm.DISPLAY_FROM.remove(event.getPlayer());
            }else{
                DisplayManageForm.DISPLAY_FROM.remove(event.getPlayer());
            }
        }
    }


}
