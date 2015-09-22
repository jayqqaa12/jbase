/*    */ package com.sinaapp.msdxblog.apkUtil;
/*    */ 
/*    */ public class Version
/*    */ {
/*    */   public static String getVersion()
/*    */   {
/* 34 */     return String.format("%d.%d.%d", new Object[] { Integer.valueOf(getMajorVersion()), Integer.valueOf(getMinorVersion()), Integer.valueOf(getRevisionNumber()) });
/*    */   }
/*    */ 
/*    */   public static int getMajorVersion()
/*    */   {
/* 43 */     return 1;
/*    */   }
/*    */ 
/*    */   public static int getMinorVersion()
/*    */   {
/* 52 */     return 1;
/*    */   }
/*    */ 
/*    */   public static int getRevisionNumber()
/*    */   {
/* 61 */     return 0;
/*    */   }
/*    */ }

/* Location:           C:\Users\12\Desktop\apkutil-1.0.jar
 * Qualified Name:     com.sinaapp.msdxblog.apkUtil.Version
 * JD-Core Version:    0.5.4
 */