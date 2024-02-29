package org.sobadfish.report.entity;

import com.smallaswater.easysql.mysql.data.SqlData;
import com.smallaswater.easysql.mysql.data.SqlDataList;
import com.smallaswater.easysql.mysql.utils.MySqlFunctions;
import com.smallaswater.easysql.v3.mysql.manager.SqlManager;

import java.util.List;

/**
 * @author Sobadfish
 * @date 2024/2/29
 */
public class Page<T> {

    //总数
    public int total;

    public List<T> data;

    public Page(int total,List<T> data) {
        this.total = total;
        this.data = data;
    }


    /**
     * 分页查询数据
     * @param sqlManager mysql对象
     * @param sqlCommand SQL指令
     * @param limit 页码
     * @param offset 条数
     * */
    public static Page<SqlData> selectPage(SqlManager sqlManager,SqlCommand sqlCommand,int limit,int offset){

        int count = 0;
        SqlDataList<SqlData> sq = sqlManager.getData("SELECT COUNT(*) as TB  FROM ("+sqlCommand.sql.trim()+") as COUNT_TABLE",sqlCommand.types);
        if(sq.size() > 0){
            count = sq.get(0).getInt("TB");
        }
        SqlDataList<SqlData> sqlData;
        if(limit == -1){
            sqlData = sqlManager.getData(sqlCommand.sql.trim(),sqlCommand.types);
        }else{
            sqlData = sqlManager.getData(sqlCommand.sql.trim()+" LIMIT "+limit * offset+","+offset,sqlCommand.types);
        }

        return new Page<>(count,sqlData);
    }

}
