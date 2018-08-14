package com.jayqqaa12.j2cache.util;

import com.jayqqaa12.j2cache.J2Cache;

import java.io.InputStream;
import java.util.Properties;

import static com.jayqqaa12.j2cache.CacheConstans.CONFIG_FILE;

/**
 * Created by 12 on 2018/1/15.
 */
public class ConfigKit {
//    private static final Logger LOG = LoggerFactory.getLogger(ConfigKit.class);

    private static Properties props;


    /**
     * @param props
     */
    public static void setProps(Properties props) {
        ConfigKit.props = props;
    }

    public static Properties initFromConfig() {
        if (props == null) props = new Properties();

        try (InputStream configStream = getConfigStream()) {
            props.load(configStream);
            return props;
        } catch (Exception e) {
            throw new CacheException("Cannot find " + CONFIG_FILE + " !!!");
        }
    }



    /**
     * get j2cache properties stream
     *
     * @return
     */
    private static InputStream getConfigStream() {
//        LOG.info("Load J2Cache Config File : [{}].", CONFIG_FILE);
        InputStream configStream = J2Cache.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
        if (configStream == null)
            configStream = J2Cache.class.getClassLoader().getParent().getResourceAsStream(CONFIG_FILE);
        if (configStream == null)
            throw new CacheException("Cannot find " + CONFIG_FILE + " !!!");
        return configStream;
    }

}
