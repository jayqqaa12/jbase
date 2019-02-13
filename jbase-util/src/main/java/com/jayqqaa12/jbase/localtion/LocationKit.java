package com.jayqqaa12.jbase.localtion;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通过lang 获取 全球城市数据
 * <p>
 * 全部缓存在内存 可能占用较高
 *
 * @author: 12
 * @create: 2019-01-08 11:26
 **/
public class LocationKit {
    private static final Logger LOG = LoggerFactory.getLogger(LocationKit.class);

    private static final String PATH = "location/";

    private static final Map<String, Location> cache = new HashMap<>();

    public static final String SPLIT = "-";


    /**
     * 通过lang 获取 全球城市数据
     *
     * @param lang
     */
    public static Location loadData(String lang) {
        if(lang==null)throw new NullPointerException("load location file lang is null");
        
        if (cache.containsKey(lang)) return cache.get(lang);
        try {

            if(lang.equals("zh"))lang="zh_cn";

           InputStream inputStream =  Location.class.getClassLoader()
                    .getResourceAsStream (PATH + lang.toLowerCase() + ".xml");

            LOG.info("load {} location file ",lang);
            JAXBContext jc = JAXBContext.newInstance(Location.class);
            Unmarshaller uma = jc.createUnmarshaller();
            Location location = (Location) uma.unmarshal(inputStream);

            location.buildMap(SPLIT);

            cache.putIfAbsent(lang, location);
            return location;
        } catch (Exception e) {
            LOG.error("load {} location file error ",lang,e);
            return null;
        }
    }

   
    public static String getName(String county, String state, String city, String region, String lang) {

        String code ="";

        if(!StringUtils.isEmpty(county))code=county;
        if(!StringUtils.isEmpty(state))code+= SPLIT+ state;
        if(!StringUtils.isEmpty(city))code+=SPLIT+city;
        if(!StringUtils.isEmpty(region))code+=SPLIT+region;

        return getName(code, lang);
    }


    /**
     * 通过编码转换为地址
     *
     * @param code 格式以 SPLIT 分割  county-state-city-region
     * @param lang
     * @return
     */
    public static String getName(String code, String lang) {
        if (StringUtils.isEmpty(code)) return null;

        Location location = loadData(lang);
        if (location != null) {
            if (location.regionNameMap.containsKey(code))
                return location.regionNameMap.get(code);
            if (location.cityNameMap.containsKey(code))
                return location.cityNameMap.get(code);
            if (location.stateNameMap.containsKey(code))
                return location.stateNameMap.get(code);
            if (location.countryNameMap.containsKey(code))
                return location.countryNameMap.get(code);
        }
        return null;
    }


    /**
     * 通过 上级code 查询下级 并不包含下下级 级联查询使用
     *
     * @param code
     * @param lang
     * @return
     */
    public static List selectChildren(String code, String lang) {
        if (StringUtils.isEmpty(lang)) return null;

        Location location = loadData(lang);
        if (location != null) {
            if (location.regionMap.containsKey(code))
                return location.regionMap.get(code);
            if (location.cityMap.containsKey(code))
                return location.cityMap.get(code);
            if (location.stateMap.containsKey(code))
                return location.stateMap.get(code);
            else if (StringUtils.isEmpty(code))
                return location.countryList;
        }
        return null;
    }

 
}
