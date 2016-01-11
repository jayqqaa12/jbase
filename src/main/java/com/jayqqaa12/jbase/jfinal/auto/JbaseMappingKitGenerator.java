package com.jayqqaa12.jbase.jfinal.auto;

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
