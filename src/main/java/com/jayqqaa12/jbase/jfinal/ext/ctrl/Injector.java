package com.jayqqaa12.jbase.jfinal.ext.ctrl;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;

/**
 * Injector.
 */
public class Injector {
	
	private static <T> T createInstance(Class<T> objClass) {
		try {
			return objClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static <T> T injectModel(Class<T> modelClass, HttpServletRequest request, boolean skipConvertError) {
		String modelName = modelClass.getSimpleName();
		return (T)injectModel(modelClass, StrKit.firstCharToLowerCase(modelName), request, skipConvertError);
	}
 
	
	@SuppressWarnings("unchecked")
	public static final <T> T injectModel(Class<T> modelClass, String modelName, HttpServletRequest request, boolean skipConvertError) {
		Object temp = createInstance(modelClass);
		if (temp instanceof Model == false) {
			throw new IllegalArgumentException("getModel only support class of Model, using getBean for other class.");
		}
		
		Model<?> model = (Model<?>)temp;
		String modelNameAndDot = StrKit.notBlank(modelName) ? modelName + "." : null;
		
		Table table = TableMapping.me().getTable(model.getClass());
		Map<String, String[]> parasMap = request.getParameterMap();
		for (Entry<String, Class<?>> entry : table.getColumnTypeMapEntrySet()) {
			String attrName = entry.getKey();
			String paraName = modelNameAndDot != null ? modelNameAndDot + attrName : attrName;
			if (parasMap.containsKey(paraName)) {
				try {
					Class<?> colType = entry.getValue();
					String paraValue = request.getParameter(paraName);
					Object value = paraValue != null ? TypeConverter.convert(colType, paraValue) : null;
					model.set(attrName, value);
				} catch (Exception e) {
					if (skipConvertError == false) {
						throw new RuntimeException("Can not convert parameter: " + paraName, e);
					}
				}
			}
		}
		
		return (T)model;
	}
}

