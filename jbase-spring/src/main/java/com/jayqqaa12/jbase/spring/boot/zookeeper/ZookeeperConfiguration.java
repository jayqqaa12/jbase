package com.jayqqaa12.jbase.spring.boot.zookeeper;

import com.jayqqaa12.jbase.spring.boot.base.ConfigOrdered;
import com.jayqqaa12.jbase.spring.boot.zookeeper.internal.ZookeeperBuilder;
import com.jayqqaa12.jbase.spring.boot.zookeeper.internal.ZookeeperProperties;
import com.jayqqaa12.jbase.spring.boot.zookeeper.lock.DistributeLockFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

/**
 * Created by yhj on 17/3/23.
 */
@Configuration
@ConditionalOnClass({CuratorFramework.class, ZooKeeper.class})
@Order(ConfigOrdered.ZK)
public class ZookeeperConfiguration implements ApplicationContextAware {

    private CuratorFramework client = null;
    private ApplicationContext applicationContext;

    @Bean
    public ZookeeperProperties zookeeperProperties() {
        ZookeeperProperties properties = new ZookeeperProperties();

        Environment environment = applicationContext.getEnvironment();

        properties.setUri(environment.getProperty("spring.zk.uri"));
        properties.setSessionTimeoutMs(environment.getProperty("spring.zk.sessionTimeoutMs",Integer.class));
        properties.setConnectionTimeoutMs(environment.getProperty("spring.zk.connectionTimeoutMs",Integer.class));
        properties.setBaseSleepTimeMs(environment.getProperty("spring.zk.baseSleepTimeMs",Integer.class));
        properties.setMaxRetries(environment.getProperty("spring.zk.maxRetries",Integer.class));

        return properties;
    }

    @Bean
    public CuratorFramework curatorFramework() throws Exception {
        if (client == null) {
            client = ZookeeperBuilder.buildCuratorFramework(zookeeperProperties());
        }
        return client;
    }


    @Bean
    public DistributeLockFactory distributeLockFactory(CuratorFramework client) {
        DistributeLockFactory factory = new DistributeLockFactory();
        factory.setCuratorFramework(client);

        return factory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
