package org.sobadfish.report.manager;

import org.sobadfish.report.config.Report;
import org.sobadfish.report.entity.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sobadfish
 * @date 2023/11/14
 */
public interface IDataManager {

    /**
     * 提交举报玩家数据
     * @param player 被举报玩家
     * @param msg 举报信息
     *
     * @param target 举报玩家
     * */
    void pullReport(String player,
                           String msg,
                           String target);

    /**
     * 获取被举报的玩家
     * @return 被举报的玩家名
     * */
    Page<String> getPlayers(int page);

    /**
     * 获取被举报的玩家
     * @return 被举报的玩家名
     * */
    Page<String> getHistoryPlayers(String player,String target,int page);

    /**
     * 获取举报数据
     * @param player 举报玩家(可不填)
     * @return 举报的数据 {@link org.sobadfish.report.config.Report}
     * */
    Page<Report> getReports(String player,int page);

    /**
     * 删除 玩家的举报记录
     * @param player 举报玩家(null为删除全部记录)
     * */
    void delTarget(int id,String player,String admin,String msg);

    /**
     * 获取举报记录
     * @param player 举报玩家(可不填)
     * @return 举报的数据 {@link org.sobadfish.report.config.Report}
     * */
    Page<Report> getHistoryReports(String player,int page);
}
