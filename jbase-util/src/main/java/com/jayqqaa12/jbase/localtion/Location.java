package com.jayqqaa12.jbase.localtion;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;

/**
 * @author: 12
 * @create: 2019-01-08 11:30
 **/
@ToString
@Getter
@XmlRootElement(name = "Location")
@Slf4j
public class Location {

    @XmlElement(name = "CountryRegion")
    public List<Country> countryList = Lists.newArrayList();

    protected Map<String, String> countryNameMap = Maps.newHashMap();
    protected Map<String, String> stateNameMap = Maps.newHashMap();
    protected Map<String, String> cityNameMap = Maps.newHashMap();
    protected Map<String, String> regionNameMap = Maps.newHashMap();

    protected Map<String, List<State>> stateMap = Maps.newHashMap();
    protected Map<String, List<City>> cityMap = Maps.newHashMap();
    protected Map<String, List<Region>> regionMap = Maps.newHashMap();


    protected void buildMap(String split) {

        if(countryList.isEmpty())
            log.error("location load file is null ");

        for (Country country : countryList) {
            countryNameMap.putIfAbsent(country.code, country.name);

            for (State state : country.children) {

                String stateCode = "";
                String stateName="";
                if(state.code!=null) stateCode=split+state.code;
                if(state.name!=null) stateName=split+state.name;



                stateNameMap.putIfAbsent(country.code +stateCode, country.name +stateName);
                stateMap.put(country.code, country.children);

                for (City city : state.children) {
                    String cityCode = "";
                    String cityName="";

                    if(city.code!=null) cityCode=split+city.code;
                    if(city.name!=null) cityName=split+city.name;

                    cityNameMap.putIfAbsent(country.code +stateCode + cityCode, country.name + stateName +cityName);
                    cityMap.put(country.code + stateCode, state.children);

                    for (Region region : city.children) {
                        String regionCode = "";
                        String regionName="";
                        if(region.code!=null) regionCode=split+region.code;
                        if(region.name!=null) regionName=split+region.name;

                        regionNameMap.putIfAbsent(country.code +stateCode + cityCode+regionCode, country.name + stateName +cityName+ regionName);
                        regionMap.put(country.code + stateCode+cityCode, city.children);
                    }
                    city.children = null;
                }
                state.children = null;
            }
            country.children = null;
        }


    }


    @ToString
    @Getter
    static class Country {

        @XmlAttribute(name = "Name")
        public String name;
        @XmlAttribute(name = "Code")
        public String code;

        @XmlElement(name = "State")
        public List<State> children = Lists.newArrayList();

    }

    @ToString
    @Getter
    static class State {
        @XmlAttribute(name = "Name")
        public String name;
        @XmlAttribute(name = "Code")
        public String code;
        @XmlElement(name = "City")
        public List<City> children = Lists.newArrayList();
    }

    @ToString
    @Getter
    static class City {
        @XmlAttribute(name = "Name")
        public String name;
        @XmlAttribute(name = "Code")
        public String code;

        @XmlElement(name = "Region")
        public List<Region> children = Lists.newArrayList();
    }

    @ToString
    @Getter
    static class Region {
        @XmlAttribute(name = "Name")
        public String name;
        @XmlAttribute(name = "Code")
        public String code;
    }


}


