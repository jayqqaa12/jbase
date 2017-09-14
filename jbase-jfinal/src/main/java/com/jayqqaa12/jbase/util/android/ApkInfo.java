package com.jayqqaa12.jbase.util.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApkInfo {
    public static final String APPLICATION_ICON_120 = "application-icon-120";
    public static final String APPLICATION_ICON_160 = "application-icon-160";
    public static final String APPLICATION_ICON_240 = "application-icon-240";
    public static final String APPLICATION_ICON_320 = "application-icon-320";
    private String versionCode = null;

    private String versionName = null;

    private String packageName = null;

    private String minSdkVersion = null;

    private List<String> usesPermissions = null;
    private String sdkVersion;
    private String targetSdkVersion;
    private String applicationLable;
    private Map<String, String> applicationIcons;
    private String applicationIcon;
    private List<ImpliedFeature> impliedFeatures;
    private List<String> features;
    private String launchableActivity;

    public ApkInfo() {
        this.usesPermissions = new ArrayList();
        this.applicationIcons = new HashMap();
        this.impliedFeatures = new ArrayList();
        this.features = new ArrayList();
    }

    public String getVersionCode() {

        return this.versionCode;
    }

    public void setVersionCode(String versionCode) {

        this.versionCode = versionCode;
    }

    public String getVersionName() {

        return this.versionName;
    }

    public void setVersionName(String versionName) {

        this.versionName = versionName;
    }

    public String getMinSdkVersion() {

        return this.minSdkVersion;
    }

    public void setMinSdkVersion(String minSdkVersion) {

        this.minSdkVersion = minSdkVersion;
    }

    public String getPackageName() {

        return this.packageName;
    }

    public void setPackageName(String packageName) {

        this.packageName = packageName;
    }

    public String getSdkVersion() {

        return this.sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {

        this.sdkVersion = sdkVersion;
    }

    public String getTargetSdkVersion() {

        return this.targetSdkVersion;
    }

    public void setTargetSdkVersion(String targetSdkVersion) {

        this.targetSdkVersion = targetSdkVersion;
    }

    public List<String> getUsesPermissions() {

        return this.usesPermissions;
    }

    public void setUsesPermissions(List<String> usesPermission) {

        this.usesPermissions = usesPermission;
    }

    public void addToUsesPermissions(String usesPermission) {

        this.usesPermissions.add(usesPermission);
    }

    public String getApplicationLable() {

        return this.applicationLable;
    }

    public void setApplicationLable(String applicationLable) {

        this.applicationLable = applicationLable;
    }

    public String getApplicationIcon() {

        return this.applicationIcon;
    }

    public void setApplicationIcon(String applicationIcon) {

        this.applicationIcon = applicationIcon;
    }

    public Map<String, String> getApplicationIcons() {

        return this.applicationIcons;
    }

    public void setApplicationIcons(Map<String, String> applicationIcons) {

        this.applicationIcons = applicationIcons;
    }

    public void addToApplicationIcons(String key, String value) {

        this.applicationIcons.put(key, value);
    }

    public void addToImpliedFeatures(ImpliedFeature impliedFeature) {

        this.impliedFeatures.add(impliedFeature);
    }

    public List<ImpliedFeature> getImpliedFeatures() {

        return this.impliedFeatures;
    }

    public void setImpliedFeatures(List<ImpliedFeature> impliedFeatures) {

        this.impliedFeatures = impliedFeatures;
    }

    public List<String> getFeatures() {

        return this.features;
    }

    public void setFeatures(List<String> features) {

        this.features = features;
    }

    public void addToFeatures(String feature) {

        this.features.add(feature);
    }

    public String toString() {

        return "ApkInfo [versionCode=" + this.versionCode + ",\n versionName=" +
       this.versionName + ",\n packageName=" + this.packageName +
       ",\n minSdkVersion=" + this.minSdkVersion + ",\n usesPermissions=" +
       this.usesPermissions + ",\n sdkVersion=" + this.sdkVersion +
       ",\n targetSdkVersion=" + this.targetSdkVersion +
       ",\n applicationLable=" + this.applicationLable +
       ",\n applicationIcons=" + this.applicationIcons +
       ",\n applicationIcon=" + this.applicationIcon +
       ",\n impliedFeatures=" + this.impliedFeatures + ",\n features=" +
       this.features + ",\n launchableActivity=" + this.launchableActivity + "\n]";
    }

    public String getLaunchableActivity() {

        return this.launchableActivity;
    }

    public void setLaunchableActivity(String launchableActivity) {

        this.launchableActivity = launchableActivity;
    }
}

