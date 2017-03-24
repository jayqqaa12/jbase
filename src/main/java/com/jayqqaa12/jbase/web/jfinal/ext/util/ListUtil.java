package com.jayqqaa12.jbase.web.jfinal.ext.util;

import com.jayqqaa12.jbase.web.jfinal.ext.model.Model;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.List;

/***
 * for jfinal
 *
 * 改用 jdk8 就够了
 * 
 * @author 12
 * @deprecated
 * 
 */
@Deprecated
public class ListUtil
{
	
	public static int sum(List<Integer> list){
		
		int max =0;
		
		for(Integer i :list){
			
			max +=i;
		}
		return max;
	}
	
	

	/**
	 * 
	 * 通过 key 把value 转为 list
	 * 
	 * @param key
	 * @param data
	 * @return
	 */
	public static List<Integer> getKeyToList(String key, List data)
	{

		List<Integer> list = new ArrayList<Integer>();
		for (Object obj : data)
		{
			if (obj instanceof Record) list.add(((Record) obj).getInt(key));
			else if(obj instanceof Model) list.add(((Model) obj).getInt(key));
		}
		return list;
	}

	public static String listToString(List list, String param)
	{

		String str = "";

		for (Object m : list)
		{

			if (m instanceof Model)
			{
				str += ((Model) m).get(param) + ",";
			}

		}

		if (str.length() > 1) return str.substring(0, str.length() - 1);
		else return str;

	}

	public static Object[][] stringToArray(Object obj, String objArray)
	{
		String[] array = objArray.split(",");
		Object[][] objs = new Object[array.length][2];

		for (int i = 0; i < array.length; i++)
		{

			objs[i] = new Object[] { obj, Integer.parseInt(array[i]) };
		}

		return objs;
	}

	public static Object[][] ArrayToArray(Integer obj, Integer[] objArray)
	{
		Object[][] objs = new Object[objArray.length][2];

		for (int i = 0; i < objArray.length; i++)
		{
			objs[i] = new Object[] { obj, objArray[i] };
		}

		return objs;

	}

	public static Object[][] ArrayToArray(String ids, Integer[] objArray)
	{
		String[] idArray = ids.split(",");

		Object[][] objs = new Object[objArray.length * idArray.length][2];

		int z = 0;
		for (int j = 0; j < idArray.length; j++)
		{
			for (int i = 0; i < objArray.length; i++)
			{
				objs[z++] = new Object[] { idArray[j], objArray[i] };
			}
		}

		return objs;
	}
}
