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
 * @author SoBadFish
 * 2022/1/21
 */
public class DisplayForm {


    public static LinkedHashMap<Player, DisplayForm> DISPLAY_FROM = new LinkedHashMap<>();

    private ArrayList<BaseClickButton> clickButtons = new ArrayList<>();

    private Page<String> players = new Page<>(0,new ArrayList<>());

    private int page = 0;

    private int id;

    public DisplayForm(int id){
        this.id = id;
    }

    public void setClickButtons(ArrayList<BaseClickButton> clickButtons) {
        this.clickButtons = clickButtons;
    }

    public ArrayList<BaseClickButton> getClickButtons() {
        return clickButtons;
    }

    public int getId() {
        return id;
    }

    public static int getRid(){
        return Utils.rand(1890,118025);
    }

    public void disPlay(Player player){
        FormWindowSimple simple = new FormWindowSimple(TextFormat.colorize('&',"&b举报系统"),"");
        ArrayList<BaseClickButton> buttons = new ArrayList<>();
        if(players.total == 0) {
            players = ReportMainClass.getDataManager().getPlayers(page);
        }
        if(players.total > 0){
            for(String s: players.data){
                Page<Report> reports = ReportMainClass.getDataManager().getReports(s,0);
                if(reports.total == 0){
                    continue;
                }
                Report nRepot = reports.data.get(0);
                String str = s.trim();
                str += "\n&4[New] &c"+nRepot.getTime()+" &r "+reports.total+" &2条相关记录";
                buttons.add(new BaseClickButton(new ElementButton(TextFormat.colorize('&',str),new ElementButtonImageData("path"
                        ,"textures/ui/Friend2")),s) {
                    @Override
                    public void onClick(Player player) {
                        DisplayManageForm fromButton = new DisplayManageForm(DisplayManageForm.getRid());
                        fromButton.disPlay(player,getTarget());

                    }
                });
            }
        }else{
            ReportMainClass.sendMessageToObject("&c无举报记录",player);
            return;
        }
        if(players.total > ReportMainClass.PAGE_SIZE){
            if(page == 0){
                addNext(buttons);
            }else if(page * ReportMainClass.PAGE_SIZE < players.total){
                addLast(buttons);
                addNext(buttons);
            }else{
                addLast(buttons);
            }
        }


        for(BaseClickButton button: buttons){
            simple.addButton(button.getButton());
        }
        player.showFormWindow(simple,getId());
        setClickButtons(buttons);
        DISPLAY_FROM.put(player,this);

    }

    private void addLast(ArrayList<BaseClickButton> buttons){
        buttons.add(new BaseClickButton(new ElementButton("上一页", new ElementButtonImageData("path", "textures/ui/arrow_dark_left_stretch")), null) {
            @Override
            public void onClick(Player player) {
                DisplayForm from = DISPLAY_FROM.get(player);
                from.setId(getRid());
                from.page--;
                from.disPlay(player);


            }
        });
    }

    private void addNext(ArrayList<BaseClickButton> buttons){
        buttons.add(new BaseClickButton(new ElementButton("下一页", new ElementButtonImageData("path", "textures/ui/arrow_dark_right_stretch")), null) {
            @Override
            public void onClick(Player player) {
                DisplayForm from = DISPLAY_FROM.get(player);
                from.setId(getRid());
                from.page++;
                from.disPlay(player);
            }
        });
    }

    public void setId(int id) {
        this.id = id;
    }

//    public <T> ArrayList<T> getArrayListByPage(int page, List<T> list){
//        ArrayList<T> button = new ArrayList<>();
//        for(int i = (page - 1) * ITEM_SIZE; i < ITEM_SIZE + ((page - 1) * ITEM_SIZE);i++){
//            if(list.size() > i){
//                button.add(list.get(i));
//            }
//        }
//        return button;
//    }
//
//    public int mathPage(ArrayList<?> button){
//        if(button.size() == 0){
//            return 1;
//        }
//        return (int) Math.ceil(button.size() / (double)ITEM_SIZE);
//    }



}
