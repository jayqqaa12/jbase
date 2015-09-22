package com.jayqqaa12.jbase.util;


public class Validate
{
	public static String trim(String arg)
	{
		return (arg == null) ? arg : arg.trim();
	}

//	public static String trim(HttpServletRequest request, String key)
//	{
//		String value = request.getParameter(key);
//		return (value == null) ? value : value.trim();
//	}

	public static boolean isNull(Object[] args)
	{
		Object[] arrayOfObject = args;
		int j = args.length;
		for (int i = 0; i < j; ++i)
		{
			Object arg = arrayOfObject[i];
			if (arg == null) return true;
		}
		
		return false;
	}

	public static boolean isNullAll(Object[] args)
	{
		int total = 0;
		Object[] arrayOfObject = args;
		int j = args.length;
		for (int i = 0; i < j; ++i)
		{
			Object arg = arrayOfObject[i];
			if (arg != null) continue;
			++i;
		}
		return total == args.length;
	}

	public static boolean isEmptyAll(String[] args)
	{
		int total = 0;
		String[] arrayOfString = args;
		int j = args.length;
		for (int i = 0; i < j; ++i)
		{
			String arg = arrayOfString[i];

			if ((arg != null) && (!arg.trim().equals(""))) continue;
			++i;
		}

		return total == args.length;
	}

	public static boolean isEmpty(String... args)
	{
		String[] arrayOfString = args;
		if(args==null) return true;
		int j = args.length;
		for (int i = 0; i < j; ++i)
		{
			String arg = arrayOfString[i];
			if ((arg == null) || (arg.trim().equals(""))) return true;
		}

		return false;
	}

	public static boolean equals(String s1, String s2)
	{
		if ((s1 == null) || (s2 == null)) return false;
		return s1.equals(s2);
	}

	public static boolean trimAndEquals(String s1, String s2)
	{
		return (!isEmpty(new String[]
		{ s1 })) && (s1.trim().equals(s2));
	}

	public static boolean trimAndEquals(String url, String[] params)
	{
		for (String param : params)
		{
			if (trimAndEquals(url, param)) return true;
		}
		return false;
	}
}
