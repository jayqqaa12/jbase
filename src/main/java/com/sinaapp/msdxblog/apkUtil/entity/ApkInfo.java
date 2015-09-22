/*     */ package com.sinaapp.msdxblog.apkUtil.entity;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class ApkInfo
/*     */ {
/*     */   public static final String APPLICATION_ICON_120 = "application-icon-120";
/*     */   public static final String APPLICATION_ICON_160 = "application-icon-160";
/*     */   public static final String APPLICATION_ICON_240 = "application-icon-240";
/*     */   public static final String APPLICATION_ICON_320 = "application-icon-320";
/*  32 */   private String versionCode = null;
/*     */ 
/*  36 */   private String versionName = null;
/*     */ 
/*  40 */   private String packageName = null;
/*     */ 
/*  44 */   private String minSdkVersion = null;
/*     */ 
/*  48 */   private List<String> usesPermissions = null;
/*     */   private String sdkVersion;
/*     */   private String targetSdkVersion;
/*     */   private String applicationLable;
/*     */   private Map<String, String> applicationIcons;
/*     */   private String applicationIcon;
/*     */   private List<ImpliedFeature> impliedFeatures;
/*     */   private List<String> features;
/*     */   private String launchableActivity;
/*     */ 
/*     */   public ApkInfo()
/*     */   {
/*  87 */     this.usesPermissions = new ArrayList();
/*  88 */     this.applicationIcons = new HashMap();
/*  89 */     this.impliedFeatures = new ArrayList();
/*  90 */     this.features = new ArrayList();
/*     */   }
/*     */ 
/*     */   public String getVersionCode()
/*     */   {
/*  99 */     return this.versionCode;
/*     */   }
/*     */ 
/*     */   public void setVersionCode(String versionCode)
/*     */   {
/* 107 */     this.versionCode = versionCode;
/*     */   }
/*     */ 
/*     */   public String getVersionName()
/*     */   {
/* 116 */     return this.versionName;
/*     */   }
/*     */ 
/*     */   public void setVersionName(String versionName)
/*     */   {
/* 124 */     this.versionName = versionName;
/*     */   }
/*     */ 
/*     */   public String getMinSdkVersion()
/*     */   {
/* 133 */     return this.minSdkVersion;
/*     */   }
/*     */ 
/*     */   public void setMinSdkVersion(String minSdkVersion)
/*     */   {
/* 141 */     this.minSdkVersion = minSdkVersion;
/*     */   }
/*     */ 
/*     */   public String getPackageName()
/*     */   {
/* 150 */     return this.packageName;
/*     */   }
/*     */ 
/*     */   public void setPackageName(String packageName) {
/* 154 */     this.packageName = packageName;
/*     */   }
/*     */ 
/*     */   public String getSdkVersion()
/*     */   {
/* 163 */     return this.sdkVersion;
/*     */   }
/*     */ 
/*     */   public void setSdkVersion(String sdkVersion) {
/* 167 */     this.sdkVersion = sdkVersion;
/*     */   }
/*     */ 
/*     */   public String getTargetSdkVersion()
/*     */   {
/* 176 */     return this.targetSdkVersion;
/*     */   }
/*     */ 
/*     */   public void setTargetSdkVersion(String targetSdkVersion) {
/* 180 */     this.targetSdkVersion = targetSdkVersion;
/*     */   }
/*     */ 
/*     */   public List<String> getUsesPermissions()
/*     */   {
/* 189 */     return this.usesPermissions;
/*     */   }
/*     */ 
/*     */   public void setUsesPermissions(List<String> usesPermission) {
/* 193 */     this.usesPermissions = usesPermission;
/*     */   }
/*     */ 
/*     */   public void addToUsesPermissions(String usesPermission) {
/* 197 */     this.usesPermissions.add(usesPermission);
/*     */   }
/*     */ 
/*     */   public String getApplicationLable()
/*     */   {
/* 206 */     return this.applicationLable;
/*     */   }
/*     */ 
/*     */   public void setApplicationLable(String applicationLable) {
/* 210 */     this.applicationLable = applicationLable;
/*     */   }
/*     */ 
/*     */   public String getApplicationIcon()
/*     */   {
/* 219 */     return this.applicationIcon;
/*     */   }
/*     */ 
/*     */   public void setApplicationIcon(String applicationIcon) {
/* 223 */     this.applicationIcon = applicationIcon;
/*     */   }
/*     */ 
/*     */   public Map<String, String> getApplicationIcons()
/*     */   {
/* 232 */     return this.applicationIcons;
/*     */   }
/*     */ 
/*     */   public void setApplicationIcons(Map<String, String> applicationIcons) {
/* 236 */     this.applicationIcons = applicationIcons;
/*     */   }
/*     */ 
/*     */   public void addToApplicationIcons(String key, String value) {
/* 240 */     this.applicationIcons.put(key, value);
/*     */   }
/*     */ 
/*     */   public void addToImpliedFeatures(ImpliedFeature impliedFeature) {
/* 244 */     this.impliedFeatures.add(impliedFeature);
/*     */   }
/*     */ 
/*     */   public List<ImpliedFeature> getImpliedFeatures()
/*     */   {
/* 253 */     return this.impliedFeatures;
/*     */   }
/*     */ 
/*     */   public void setImpliedFeatures(List<ImpliedFeature> impliedFeatures) {
/* 257 */     this.impliedFeatures = impliedFeatures;
/*     */   }
/*     */ 
/*     */   public List<String> getFeatures()
/*     */   {
/* 266 */     return this.features;
/*     */   }
/*     */ 
/*     */   public void setFeatures(List<String> features) {
/* 270 */     this.features = features;
/*     */   }
/*     */ 
/*     */   public void addToFeatures(String feature) {
/* 274 */     this.features.add(feature);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 279 */     return "ApkInfo [versionCode=" + this.versionCode + ",\n versionName=" + 
/* 280 */       this.versionName + ",\n packageName=" + this.packageName + 
/* 281 */       ",\n minSdkVersion=" + this.minSdkVersion + ",\n usesPermissions=" + 
/* 282 */       this.usesPermissions + ",\n sdkVersion=" + this.sdkVersion + 
/* 283 */       ",\n targetSdkVersion=" + this.targetSdkVersion + 
/* 284 */       ",\n applicationLable=" + this.applicationLable + 
/* 285 */       ",\n applicationIcons=" + this.applicationIcons + 
/* 286 */       ",\n applicationIcon=" + this.applicationIcon + 
/* 287 */       ",\n impliedFeatures=" + this.impliedFeatures + ",\n features=" + 
/* 288 */       this.features + ",\n launchableActivity=" + this.launchableActivity + "\n]";
/*     */   }
/*     */ 
/*     */   public String getLaunchableActivity() {
/* 292 */     return this.launchableActivity;
/*     */   }
/*     */ 
/*     */   public void setLaunchableActivity(String launchableActivity) {
/* 296 */     this.launchableActivity = launchableActivity;
/*     */   }
/*     */ }

/* Location:           C:\Users\12\Desktop\apkutil-1.0.jar
 * Qualified Name:     com.sinaapp.msdxblog.apkUtil.entity.ApkInfo
 * JD-Core Version:    0.5.4
 */