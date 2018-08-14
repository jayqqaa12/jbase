package com.jayqqaa12.j2cache;

import java.io.IOException;


public interface ClusterPolicy {

    /**
     * 连接到集群
     */
    void connect();

    /**
     * 发送清除缓存的命令
     *
     * @param region 区域名称
     * @param keys   缓存键值
     */
    void sendEvictCmd(String region, Object... keys);

    /**
     * 发送清除整个缓存区域的命令
     *
     * @param region 区域名称
     */
    void sendClearCmd(String region);

    /**
     * 断开集群连接
     */
    void disconnect();

    /**
     * 删除本地某个缓存条目
     *
     * @param region 区域名称
     * @throws IOException io exception
     */
    default void evict(String region, Object keys) throws IOException {
        CacheProviderHolder.remove(CacheConstans.LEVEL1, region, keys);
    }

    /**
     * 清除本地整个缓存区域
     * @param region 区域名称
     * @throws IOException io exception
     */
    default void clear(String region) throws IOException {
        CacheProviderHolder.clear(CacheConstans.LEVEL1, region);
    }


}
