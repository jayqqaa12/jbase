package com.jayqqaa12.jbase.jfinal.auto;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.jfinal.kit.LogKit;
import com.jfinal.plugin.activerecord.generator.ColumnMeta;
import com.jfinal.plugin.activerecord.generator.MetaBuilder;
import com.jfinal.plugin.activerecord.generator.TableMeta;
import com.jfinal.plugin.activerecord.generator.TypeMapping;

public class JbaseMetaBuilder extends MetaBuilder {

	

	public JbaseMetaBuilder(DataSource dataSource) {
		super(dataSource);
	}
	
	
	@Override
	public List<TableMeta> build() {
		 List<TableMeta>  rst= super.build();
		 
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
					ColumnMeta columnMeta = new ColumnMeta();
					columnMeta.name = rs.getString("COLUMN_NAME");			// 名称
					
					columnMeta.defaultValue = rs.getString("COLUMN_DEF");	// 默认值
					if (columnMeta.defaultValue == null)
						columnMeta.defaultValue = "";
					
					columnMeta.remarks = rs.getString("REMARKS");			// 备注
					if (columnMeta.remarks == null)
						columnMeta.remarks = "";
					
					for(ColumnMeta c :tableMeta.columnMetas){
						if(c.name.equals(columnMeta.name)){
							c.remarks = columnMeta.remarks;
							c.defaultValue = columnMeta.defaultValue;
						}
					}
					
				}
				rs.close();
			}
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
		finally {
			if (conn != null)
				try {conn.close();} catch (SQLException e) {LogKit.error(e.getMessage(), e);}
		}
	}
	
}
