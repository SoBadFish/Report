package org.sobadfish.report.manager;

import com.smallaswater.easysql.mysql.data.SqlData;
import com.smallaswater.easysql.mysql.data.SqlDataList;
import com.smallaswater.easysql.mysql.utils.ChunkSqlType;
import com.smallaswater.easysql.mysql.utils.TableType;
import com.smallaswater.easysql.mysql.utils.Types;
import com.smallaswater.easysql.v3.mysql.manager.SqlManager;
import com.smallaswater.easysql.v3.mysql.utils.SelectType;
import org.sobadfish.report.config.Report;
import org.sobadfish.report.tools.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author SoBadFish
 * 2022/1/21
 */
public class ReportSqlManager implements IDataManager{

    public static final String SQL_TABLE = "report";

    public static final String SQL_COLUMN_ID = "id";

    public static final String SQL_COLUMN_PLAYER_NAME = "playerName";

    public static final String SQL_COLUMN_TIME = "time";

    public static final String SQL_COLUMN_REPORT_MESSAGE = "reportMessage";

    public static final String SQL_COLUMN_REPORT_PLAYER = "reportPlayer";

    public static final String SQL_COLUMN_REPORT_DELETE_FLAG = "deleteFlag";

    public static final String SQL_COLUMN_REPORT_ADMIN_PLAYER = "manager";

    public static final String SQL_COLUMN_REPORT_ADMIN_PLAYER_TIME = "managerTime";
    public static final String SQL_COLUMN_REPORT_ADMIN_MSG = "managerMsg";


    private final SqlManager sqlManager;

    public ReportSqlManager(SqlManager sqlManager){
        this.sqlManager = sqlManager;

    }

    private boolean tableDelete = false;



    public SqlManager getSqlManager() {
        return sqlManager;
    }

    public boolean createTable(){
        if(!sqlManager.isExistTable(ReportSqlManager.SQL_TABLE)){
            Types types = Types.CHAR;
            types.setValue("");

            TableType[] tableType = new TableType[]{
                    new TableType(ReportSqlManager.SQL_COLUMN_ID, Types.ID),
                    new TableType(ReportSqlManager.SQL_COLUMN_PLAYER_NAME, types),
                    new TableType(ReportSqlManager.SQL_COLUMN_TIME, types),
                    new TableType(ReportSqlManager.SQL_COLUMN_REPORT_MESSAGE,types),
                    new TableType(ReportSqlManager.SQL_COLUMN_REPORT_PLAYER,types),
                    new TableType(ReportSqlManager.SQL_COLUMN_REPORT_DELETE_FLAG,Types.INT),
                    new TableType(ReportSqlManager.SQL_COLUMN_REPORT_ADMIN_PLAYER,types),
                    new TableType(ReportSqlManager.SQL_COLUMN_REPORT_ADMIN_MSG,types),
                    new TableType(ReportSqlManager.SQL_COLUMN_REPORT_ADMIN_PLAYER_TIME, types),
            };
            return sqlManager.createTable(ReportSqlManager.SQL_TABLE,tableType);
        }
        return true;
    }

    /**
     * 提交举报玩家数据
     * @param player 被举报玩家
     * @param msg 举报信息
     *
     * @param target 举报玩家
     * */
    @Override
    public void pullReport(String player,
                           String msg,
                           String target){
        SqlData data = new SqlData();
        data.put(SQL_COLUMN_PLAYER_NAME,player);
        data.put(SQL_COLUMN_REPORT_MESSAGE,msg);
        data.put(SQL_COLUMN_TIME, Utils.getDateToString(new Date()));
        data.put(SQL_COLUMN_REPORT_PLAYER, target);
        data.put(SQL_COLUMN_REPORT_DELETE_FLAG,0);
        if(tableDelete){
            return;
        }
        sqlManager.insertData(SQL_TABLE,data);
    }
    /**
     * 获取被举报的玩家
     * @return 被举报的玩家名
     * */
    @Override
    public List<String> getPlayers(){
        if(tableDelete){
            return new ArrayList<>();
        }
        SqlDataList<SqlData> data = sqlManager.getData("SELECT DISTINCT "+SQL_COLUMN_PLAYER_NAME+","+SQL_COLUMN_TIME+" FROM "
                +SQL_TABLE+" WHERE "+SQL_COLUMN_REPORT_DELETE_FLAG+" = ? order by "+SQL_COLUMN_TIME+" desc",new ChunkSqlType(1,"0"));
        ArrayList<String> strings = new ArrayList<>();
        for(SqlData data1: data){
            String n = data1.getString(SQL_COLUMN_PLAYER_NAME);
            if("".equalsIgnoreCase(n)){
                continue;
            }
            strings.add(n);
        }
        return strings;
    }

    @Override
    public List<String> getHistoryPlayers(String player,String target) {
        if(tableDelete){
            return new ArrayList<>();
        }
        SqlDataList<SqlData> data;

        if(target != null){
            if(player == null){
                data = sqlManager.getData("SELECT DISTINCT "+SQL_COLUMN_PLAYER_NAME+","+SQL_COLUMN_REPORT_ADMIN_PLAYER_TIME+" FROM "
                        +SQL_TABLE+" WHERE "+SQL_COLUMN_REPORT_DELETE_FLAG+" = ? AND "+SQL_COLUMN_REPORT_PLAYER+" = ? order by "+SQL_COLUMN_REPORT_ADMIN_PLAYER_TIME+" desc",new ChunkSqlType(1,"1"),new ChunkSqlType(2,target));
            }else{
                data = sqlManager.getData("SELECT DISTINCT "+SQL_COLUMN_PLAYER_NAME+","+SQL_COLUMN_REPORT_ADMIN_PLAYER_TIME+" FROM "
                        +SQL_TABLE+" WHERE "+SQL_COLUMN_REPORT_DELETE_FLAG+" = ? AND "+SQL_COLUMN_REPORT_PLAYER+" = ? AND "+SQL_COLUMN_REPORT_ADMIN_PLAYER+" = ? order by "+SQL_COLUMN_REPORT_ADMIN_PLAYER_TIME+" desc",new ChunkSqlType(1,"1"),new ChunkSqlType(2,target),new ChunkSqlType(3,player));
            }
        }else{
            if(player == null){
                data = sqlManager.getData("SELECT DISTINCT "+SQL_COLUMN_PLAYER_NAME+","+SQL_COLUMN_REPORT_ADMIN_PLAYER_TIME+" FROM "
                        +SQL_TABLE+" WHERE "+SQL_COLUMN_REPORT_DELETE_FLAG+" = ? order by "+SQL_COLUMN_REPORT_ADMIN_PLAYER_TIME+" desc",new ChunkSqlType(1,"1"));
            }else{
                data = sqlManager.getData("SELECT DISTINCT "+SQL_COLUMN_PLAYER_NAME+","+SQL_COLUMN_REPORT_ADMIN_PLAYER_TIME+" FROM "
                        +SQL_TABLE+" WHERE "+SQL_COLUMN_REPORT_DELETE_FLAG+" = ? AND "+SQL_COLUMN_REPORT_ADMIN_PLAYER+" = ? order by "+SQL_COLUMN_REPORT_ADMIN_PLAYER_TIME+" desc",new ChunkSqlType(1,"1"),new ChunkSqlType(2,player));
            }

        }

        ArrayList<String> strings = new ArrayList<>();
        for(SqlData data1: data){
            String n = data1.getString(SQL_COLUMN_PLAYER_NAME);
            if("".equalsIgnoreCase(n)){
                continue;
            }
            strings.add(n);
        }
        return strings;
    }

    /**
     * 获取举报数据
     * @param player 举报玩家(可不填)
     * @return 举报的数据 {@link org.sobadfish.report.config.Report}
     * */
    @Override
    public ArrayList<Report> getReports(String player){
        if(tableDelete){
            return new ArrayList<>();
        }
        ArrayList<Report> reports = new ArrayList<>();
        SqlDataList<SqlData> data;
        if(player != null){
            data = sqlManager.getData("SELECT * FROM "+SQL_TABLE+" WHERE "+SQL_COLUMN_PLAYER_NAME+" = ? AND "+SQL_COLUMN_REPORT_DELETE_FLAG+" = ? order by "+SQL_COLUMN_TIME+" desc"
                    ,new ChunkSqlType(1,player),new ChunkSqlType(2,"0"));
        }else{
            data = sqlManager.getData("SELECT * FROM "+SQL_TABLE+" WHERE "+SQL_COLUMN_REPORT_DELETE_FLAG+" = ? order by "+SQL_COLUMN_TIME+" desc",new ChunkSqlType(1,"0"));
        }
        for(SqlData data1: data){
            Report report = new Report(
                    data1.getInt(SQL_COLUMN_ID),
                    data1.getString(SQL_COLUMN_PLAYER_NAME),
                    data1.getString(SQL_COLUMN_TIME),
                    data1.getString(SQL_COLUMN_REPORT_MESSAGE),
                    data1.getString(SQL_COLUMN_REPORT_PLAYER),
                    data1.getString(SQL_COLUMN_REPORT_ADMIN_PLAYER),
                    data1.getString(SQL_COLUMN_REPORT_ADMIN_MSG));
            report.setManagerTime( data1.getString(SQL_COLUMN_REPORT_ADMIN_PLAYER_TIME));
            reports.add(report);
        }
        return reports;
    }

    /**
     * 获取举报历史记录
     * @param player 举报玩家(可不填)
     * @return 举报的数据 {@link org.sobadfish.report.config.Report}
     * */
    @Override
    public ArrayList<Report> getHistoryReports(String player){
        if(tableDelete){
            return new ArrayList<>();
        }
        ArrayList<Report> reports = new ArrayList<>();
        SqlDataList<SqlData> data;
        if(player != null){
            data = sqlManager.getData("SELECT * FROM "+SQL_TABLE+" WHERE "+SQL_COLUMN_PLAYER_NAME+" = ? AND "+SQL_COLUMN_REPORT_DELETE_FLAG+" = ? order by "+SQL_COLUMN_REPORT_ADMIN_PLAYER_TIME+" desc"
                    ,new ChunkSqlType(1,player),new ChunkSqlType(2,"1"));
        }else{
            data = sqlManager.getData("SELECT * FROM "+SQL_TABLE+" WHERE "+SQL_COLUMN_REPORT_DELETE_FLAG+" = ? order by "+SQL_COLUMN_REPORT_ADMIN_PLAYER_TIME+" desc",new ChunkSqlType(1,"1"));
        }
        for(SqlData data1: data){
            Report report = new Report(
                    data1.getInt(SQL_COLUMN_ID),
                    data1.getString(SQL_COLUMN_PLAYER_NAME),
                    data1.getString(SQL_COLUMN_TIME),
                    data1.getString(SQL_COLUMN_REPORT_MESSAGE),
                    data1.getString(SQL_COLUMN_REPORT_PLAYER),
                    data1.getString(SQL_COLUMN_REPORT_ADMIN_PLAYER),
                    data1.getString(SQL_COLUMN_REPORT_ADMIN_MSG));
            report.setManagerTime( data1.getString(SQL_COLUMN_REPORT_ADMIN_PLAYER_TIME));
            reports.add(report);
        }
        return reports;
    }

    /**
     * 删除 玩家的举报记录
     * @param player 举报玩家(null为删除全部记录)
     * */
    @Override
    public void delTarget(int id,String player, String admin,String msg){
        if(player == null){
            tableDelete = true;
            sqlManager.deleteTable(SQL_TABLE);
            if(createTable()){
                tableDelete = false;
            }
        }else{
            sqlManager.setData(SQL_TABLE,new SqlData(SQL_COLUMN_REPORT_DELETE_FLAG,1)
                            .put(SQL_COLUMN_REPORT_ADMIN_PLAYER,admin)
                            .put(SQL_COLUMN_REPORT_ADMIN_MSG,msg)
                            .put(SQL_COLUMN_REPORT_ADMIN_PLAYER_TIME,Utils.getDateToString(new Date()))
                    ,new SqlData(SQL_COLUMN_ID,id));
        }

    }


}
