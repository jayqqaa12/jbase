/*    */ package com.sinaapp.msdxblog.apkUtil.entity;
/*    */ 
/*    */ public class ImpliedFeature
/*    */ {
/*    */   private String feature;
/*    */   private String implied;
/*    */ 
/*    */   public ImpliedFeature()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ImpliedFeature(String feature, String implied)
/*    */   {
/* 44 */     this.feature = feature;
/* 45 */     this.implied = implied;
/*    */   }
/*    */ 
/*    */   public String getFeature() {
/* 49 */     return this.feature;
/*    */   }
/*    */ 
/*    */   public void setFeature(String feature) {
/* 53 */     this.feature = feature;
/*    */   }
/*    */ 
/*    */   public String getImplied() {
/* 57 */     return this.implied;
/*    */   }
/*    */ 
/*    */   public void setImplied(String implied) {
/* 61 */     this.implied = implied;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 66 */     return "Feature [feature=" + this.feature + ", implied=" + this.implied + "]";
/*    */   }
/*    */ }

/* Location:           C:\Users\12\Desktop\apkutil-1.0.jar
 * Qualified Name:     com.sinaapp.msdxblog.apkUtil.entity.ImpliedFeature
 * JD-Core Version:    0.5.4
 */