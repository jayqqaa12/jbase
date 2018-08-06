package com.jayqqaa12.jbase.spring.boot.properties;

import com.jayqqaa12.jbase.spring.boot.base.ConfigOrdered;
import com.jayqqaa12.jbase.spring.boot.properties.internal.ZookeeperPropertySource;
import com.jayqqaa12.jbase.spring.boot.zookeeper.ZookeeperConfiguration;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.util.StringUtils;

@Configuration
@Import(ZookeeperConfiguration.class)
@Order(ConfigOrdered.PROP)
@ConditionalOnBean(CuratorFramework.class)
public class PropertiesConfiguration implements ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger(PropertiesConfiguration.class);

    private ZookeeperPropertySource zkSources;
    private ConfigurableApplicationContext context;


    @Bean
    @Scope("singleton")
    public SpringProperties springProperties() {
        flushZookeeperProperties();
        return SpringProperties.instance();
    }


    public void flushZookeeperProperties() {
        logger.info("start flush zookeeper properties ");

        ConfigurableEnvironment environment = context.getEnvironment();

        MutablePropertySources sources = environment.getPropertySources();

        String zkRoot = environment.getProperty("config.center.zkRoot");
        String inZk = environment.getProperty("config.center.inzookeeper");

        if (!StringUtils.isEmpty(inZk)) {
            zkSources = new ZookeeperPropertySource(context.getBeanFactory(), zkRoot);
            sources.addLast(zkSources);
            zkSources.flushProperties();
        }

        logger.info("end flush zookeeper properties ");
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = (ConfigurableApplicationContext) applicationContext;
    }


}