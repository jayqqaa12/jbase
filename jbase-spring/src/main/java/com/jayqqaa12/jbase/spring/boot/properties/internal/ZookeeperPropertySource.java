package com.jayqqaa12.jbase.spring.boot.properties.internal;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;

public class ZookeeperPropertySource extends PropertiesPropertySource {
    private static final String CONFIG_PROPERTIES_NAME = "zkProperties";
    BeanFactory beanFactory;
    ZookeeperLoader loader = null;
    String zkRoot;

    public ZookeeperPropertySource(BeanFactory beanFactory, String zkRoot) {
        super(CONFIG_PROPERTIES_NAME, new Properties());
        this.beanFactory = beanFactory;
        this.zkRoot = zkRoot;
    }


    public void flushProperties() {

        try {
            CuratorFramework curatorClient = beanFactory.getBean(CuratorFramework.class);

            if (curatorClient != null) {
                loader = new ZookeeperLoader(curatorClient, this, zkRoot);
            }

            if (loader != null) {
                loader.loadProperties();
            }
        } catch (Exception e) {
            logger.warn("load properties error and throw new exception :" + e.getMessage(), e);
        }
    }

    public void addProperty(String key, String value) {
        source.put(key, value);
    }

    public void removeProperty(String key) {
        source.remove(key);
    }
}
