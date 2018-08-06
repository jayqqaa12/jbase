package com.jayqqaa12.jbase.spring.boot.properties.internal;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

/**
 * Created by yhj on 17/3/24.
 */
public class ZookeeperLoader {

    private static String zk_cfg_root = null;

    private final static Logger logger = LoggerFactory.getLogger(ZookeeperLoader.class);

    private CuratorFramework curatorClient;


    private ZookeeperPropertySource source;


    public ZookeeperLoader(CuratorFramework curatorClient, ZookeeperPropertySource source, String zkRoot) {
        this.curatorClient = curatorClient;
        this.source = source;
        this.zk_cfg_root = zkRoot;
    }

    public void addProperty(String key, String value) {
        source.addProperty(key, value);
    }

    public void loadProperties() {
        reloadConf(null);
        /**
         * 系统在完成配置文件的加载之后要检测一下zookeeper中的根节点是否已经存在，如果不存在则添加此节点为根节点
         * 这样为admin启动时将数据库中的配置检测或加载配置提供根节点基础， <strong></strong>
         */
        if (zk_cfg_root != null) {
            try {
                curatorClient.getData().forPath(zk_cfg_root);
            } catch (Exception e) {
                try {
                    curatorClient.create().forPath(zk_cfg_root);
                } catch (Exception e1) {
                    logger.error("创建系统配置根节点失败", e1);
                    ;
                }
            }
        }
    }

    private void reloadConf(String path) {
        try {

            if (path == null) {// 遍历所有子节点来绑定属性
                List<String> confs = curatorClient.getChildren().usingWatcher(notify).forPath(zk_cfg_root);
                for (Iterator iterator = confs.iterator(); iterator.hasNext(); ) {
                    String conf = (String) iterator.next();

                    // 原来是不能覆盖, 修改为,如果不是系统开始的配置,都是能直接覆盖的!
                    // if( ! props.containsKey(conf) ){
                    String data = new String(
                            curatorClient.getData().usingWatcher(notify).forPath(zk_cfg_root + "/" + conf));
                    addProperty(conf, data);
                    logger.info("load conf {} value {} from zk!", conf, data);
                    // }
                }
            } else {// 访问变化的子节点来改变属性
                String data = new String(curatorClient.getData().usingWatcher(notify).forPath(path));
                String conf = path.substring(path.lastIndexOf("/") + "/".length() );
                addProperty(conf, data);
                logger.info("load conf {} value {} from zk!", conf, data);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }


    private void deleteConf(String path) {
        logger.info("node {} has been deleted! remove from spring props.", path);
        try {
            source.removeProperty(path.substring(path.lastIndexOf("/") + 1));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private Watcher notify = new Watcher() {

        @Override
        public void process(WatchedEvent event) {
            if (event.getType() == Event.EventType.NodeChildrenChanged) {
                reloadConf(null);
            } else if (event.getType() == Event.EventType.NodeDataChanged) {
                reloadConf(event.getPath());
            } else if (event.getType() == Event.EventType.NodeDeleted) {
                deleteConf(event.getPath());
            }
        }
    };


}
