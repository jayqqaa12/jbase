//package com.jayqqaa12.jbase.spring.mvc;
//
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.google.common.collect.Lists;
//import com.jayqqaa12.j2cache.J2Cache;
//import com.jayqqaa12.jbase.spring.boot.base.Query;
//import com.jayqqaa12.jbase.util.Ret;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * 配置管理（简化版）
// * boot 临时过渡的版本 功能比较简单
// * 基于j2cache
// *
// * @author: 12
// * @create: 2019-01-02 15:16
// **/
//@Component
//@ConditionalOnClass(J2Cache.class)
//public class ConfigHelper {
//
//    public static final String KEY_PRE = "config";
//
//    @Autowired
//    J2Cache j2Cache;
//
//    /**
//     * 获取配置
//     *
//     * @param key
//     * @return
//     */
//    private String getValStr(String key) {
//        return j2Cache.get(KEY_PRE, key);
//    }
//
//    private Boolean getValBool(String key) {
//        String v = getValStr(key);
//        return v == null ? null : Boolean.valueOf(v);
//    }
//
//    private Double getValDouble(String key) {
//        String v = getValStr(key);
//        return v == null ? null : Double.parseDouble(v);
//    }
//
//    private Integer getValInt(String key) {
//        String v = getValStr(key);
//        return v == null ? null : Integer.parseInt(v);
//    }
//
//
//    public String getValStr(String key, String defult) {
//        return getValStr(key) != null ? getValStr(key) : defult;
//    }
//
//    public Boolean getValBool(String key, Boolean defult) {
//        return getValBool(key) != null ? getValBool(key) : defult;
//    }
//
//    public Double getValDouble(String key, Double defult) {
//        return getValDouble(key) != null ? getValDouble(key) : defult;
//    }
//
//    public Integer getValInt(String key, Integer defult) {
//        return getValInt(key) != null ? getValInt(key) : defult;
//    }
//
//
//    /**
//     * 更新配置
//     *
//     * @param key
//     * @param v
//     */
//    public void setVal(String key, String v) {
//        j2Cache.set(KEY_PRE, key, v);
//    }
//
//
//    /**
//     * 获取所有配置信息
//     *
//     * @return
//     */
//    public IPage all(Query query) {
//        List<Ret> list = Lists.newArrayList();
//
//        String key = (String) query.condition().get("key");
//        if (!StringUtils.isEmpty(key)) {
//            Ret ret = Ret.of("key", key);
//            Object v = j2Cache.get(KEY_PRE, key);
//            ret.put("value", v);
//            if (v != null) list.add(ret);
//        } else {
//            for (Object k : j2Cache.keys2(KEY_PRE)) {
//                Ret ret = Ret.of("key", k);
//                Object v = j2Cache.get(KEY_PRE, k);
//                ret.put("value", v);
//                if (v != null) list.add(ret);
//            }
//        }
//        return query.setRecords(list.stream().skip((query.getCurrent() - 1) * query.getSize()).limit(query.getSize())
//                .collect(Collectors.toList()));
//    }
//}
