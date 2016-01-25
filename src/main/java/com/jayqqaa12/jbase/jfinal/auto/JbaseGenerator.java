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
