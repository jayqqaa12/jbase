/**
 * Copyright (c) 2011-2013, jayqqaa12 12shu (476335667@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jayqqaa12.jbase.jfinal.auto;

import java.util.List;

import javax.sql.DataSource;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.generator.BaseModelGenerator;
import com.jfinal.plugin.activerecord.generator.ColumnMeta;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.activerecord.generator.ModelGenerator;
import com.jfinal.plugin.activerecord.generator.TableMeta;

public class JbaseGenerator extends Generator {

	protected JbaseServiceGenerator serviceGenerator;

	public static String getPk(TableMeta tableMeta, String packageName) {
		String pre = tableMeta.name.toLowerCase().replace("_", "").replace(tableMeta.modelName.toLowerCase(), "");

		String pk = packageName;
		if (StrKit.notBlank(pre)) {
			pk = packageName + "." + pre;
		}
		return pk;
	}
	
	public static String getRemark(ColumnMeta columnMeta){
		
		String remark=columnMeta.remarks;
		if(StrKit.notBlank(remark)){

			remark = "\t/**\n" + "\t* "+remark+" \n" + "\t*/\n";
		}
		
		return remark;
	}

	public JbaseGenerator(DataSource dataSource, BaseModelGenerator baseModelGenerator) {
		super(dataSource, baseModelGenerator);
		// TODO Auto-generated constructor stub
	}

	public JbaseGenerator(DataSource dataSource, String baseModelPackageName, String baseModelOutputDir,
			String modelPackageName, String modelOutputDir) {

		super(dataSource, new JbaseBaseModelGenerator(baseModelPackageName, baseModelOutputDir),
				new JbaseModelGenerator(modelPackageName, baseModelPackageName, modelOutputDir));

	}

	public void setModelGenerate(ModelGenerator modelGenerator) {
		this.modelGenerator = modelGenerator;
	}

	public void setServiceGenerate(JbaseServiceGenerator serviceGenerator) {
		this.serviceGenerator = serviceGenerator;
	}

	@Override
	public void generate() {
		
		List<TableMeta> tableMetas = metaBuilder.build();
		
		
		if (tableMetas.size() == 0) {
			System.out.println("TableMeta 数量为 0，不生成任何文件");
			return;
		}

		baseModelGenerator.generate(tableMetas);

		if (modelGenerator != null) modelGenerator.generate(tableMetas);

		if (mappingKitGenerator != null) mappingKitGenerator.generate(tableMetas);

		if (dataDictionaryGenerator != null && generateDataDictionary) dataDictionaryGenerator.generate(tableMetas);

		if (serviceGenerator != null) serviceGenerator.generate(tableMetas);

		System.out.println("Generate complete.");
	}
	
	
	

}
