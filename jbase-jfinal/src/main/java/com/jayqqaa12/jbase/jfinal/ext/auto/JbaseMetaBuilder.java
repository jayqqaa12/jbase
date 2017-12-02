/**
 * Copyright (c) 2011-2013, jayqqaa12 12shu (476335667@qq.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jayqqaa12.jbase.jfinal.ext.auto;

import com.jfinal.kit.LogKit;
import com.jfinal.plugin.activerecord.generator.ColumnMeta;
import com.jfinal.plugin.activerecord.generator.MetaBuilder;
import com.jfinal.plugin.activerecord.generator.TableMeta;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JbaseMetaBuilder extends MetaBuilder {


    public JbaseMetaBuilder(DataSource dataSource) {
        super(dataSource);
    }


    @Override
    public List<TableMeta> build() {
        List<TableMeta> rst = super.build();

        rebuildColumnMetas(rst);

        return rst;
    }


    protected void rebuildColumnMetas(List<TableMeta> tableMetas) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            DatabaseMetaData dbMeta = conn.getMetaData();
            for (TableMeta tableMeta : tableMetas) {
                // 重建整个 TableMeta.columnMetas
                ResultSet rs = dbMeta.getColumns(conn.getCatalog(), null, tableMeta.name, null);
                while (rs.next()) {
                    readRemark(tableMeta, rs);
                }
                rs.close();
            }
        } catch (SQLException e) {
            LogKit.error(e.getMessage(), e);
        } finally {
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e) {
                    LogKit.error(e.getMessage(), e);
                }
        }
    }



    private void readRemark(TableMeta tableMeta, ResultSet rs) throws SQLException {
        ColumnMeta columnMeta = new ColumnMeta();
        columnMeta.name = rs.getString("COLUMN_NAME");            // 名称

        columnMeta.defaultValue = rs.getString("COLUMN_DEF");    // 默认值
        if (columnMeta.defaultValue == null)
            columnMeta.defaultValue = "";
        columnMeta.remarks = rs.getString("REMARKS");            // 备注
        if (columnMeta.remarks == null)
            columnMeta.remarks = "";

        for (ColumnMeta c : tableMeta.columnMetas) {
            if (c.name.equals(columnMeta.name)) {
                c.remarks = columnMeta.remarks;
                c.defaultValue = columnMeta.defaultValue;
            }
        }
    }

}
