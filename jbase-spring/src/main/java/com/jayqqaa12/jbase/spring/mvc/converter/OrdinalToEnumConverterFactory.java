package com.jayqqaa12.jbase.spring.mvc.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;


/**
 * 把 ordinal 转为Enum 的方法
 */
public class OrdinalToEnumConverterFactory implements ConverterFactory<String , Enum> {



	@Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
		return new IntegerToEnum( getEnumType(targetType));
	}

	private class IntegerToEnum<T extends Enum> implements Converter<String, T> {

		private final Class<T> enumType;

		public IntegerToEnum(Class<T> enumType) {
			this.enumType = enumType;
		}

		@Override
		public T convert(String  source) {
			return this.enumType.getEnumConstants()[Integer.parseInt(source)];
		}
	}


	public static Class<?> getEnumType(Class<?> targetType) {
		Class<?> enumType = targetType;
		while (enumType != null && !enumType.isEnum()) {
			enumType = enumType.getSuperclass();
		}
		if (enumType == null) {
			throw new IllegalArgumentException(
					"The target type " + targetType.getName() + " does not refer to an enum");
		}
		return enumType;
	}

}