 package com.jayqqaa12.jbase.util;
 
  
 public class L
 {
   private static final int LOG = 0;
   private static final int DEBUG = 3;
   private static final int WANER = 2;
   private static final int ERROR = 1;
   private static final int INFO = 4;
   private static final int OUT = 5;
   
   
   
   
   
   
   
 
   public static void i(Object s)
   {
     System.out.println(s);
   }
 
   public static void i( Object... obj){
	   int i =1;
	  for(Object o:obj){
		  
		  i(i++ +" ="+ o );
		  
	  }
   }
   
   
   public static void err(Object s)
   {
     System.err.println(s);
   }
 }

