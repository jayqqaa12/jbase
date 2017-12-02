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
package com.jayqqaa12.jbase.jfinal.ext.auto;

import java.util.List;

import com.jfinal.plugin.activerecord.generator.MappingKitGenerator;
import com.jfinal.plugin.activerecord.generator.TableMeta;

public class JbaseMappingKitGenerator extends MappingKitGenerator {

	public JbaseMappingKitGenerator(String mappingKitPackageName, String mappingKitOutputDir) {
		super(mappingKitPackageName, mappingKitOutputDir);
	}

	
	
	
	protected void genMappingMethod(List<TableMeta> tableMetas, StringBuilder ret) {
		ret.append(String.format(mappingMethodDefineTemplate));
		for (TableMeta tableMeta : tableMetas) {
			boolean isCompositPrimaryKey = tableMeta.primaryKey.contains(",");
			if (isCompositPrimaryKey)
				ret.append(String.format(compositeKeyTemplate, tableMeta.primaryKey));
			
			String pk = JbaseGenerator.getPk(tableMeta, mappingKitPackageName);
			
			
			String add = String.format(mappingMethodContentTemplate, tableMeta.name,  tableMeta.primaryKey, pk+"."+ tableMeta.modelName);
			ret.append(add);
		}
		ret.append(String.format("\t}%n"));
	}
	
	
}
