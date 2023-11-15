package org.sobadfish.report.form;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;

/**
 * @author SoBadFish
 * 2022/1/21
 */
public abstract class BaseClickButton {

    private ElementButton button;

    private String target;

    public BaseClickButton(ElementButton button,String target){
        this.button = button;
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public ElementButton getButton() {
        return button;
    }

    /**
     * 点击的玩家
     * @param player 玩家
     * */
    abstract public void onClick(Player player);
}
