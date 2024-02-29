package org.sobadfish.report.entity;

import com.smallaswater.easysql.mysql.utils.ChunkSqlType;

/**
 * @author Sobadfish
 * @date 2024/2/29
 */

public class SqlCommand {

    public String sql;

    public ChunkSqlType[] types = new ChunkSqlType[0];

    public SqlCommand(String sql, ChunkSqlType... types) {
        this.sql = sql;
        if(types.length > 0) {
            this.types = types;
        }
    }
}
