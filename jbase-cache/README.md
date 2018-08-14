
参考j2cache实现的j2cache v1.0



### 两级缓存结构 

1. L1： 进程内缓存(ehcache)  ,也可1级使用Redis，大多数项目的用法
2. L2： 集中式缓存，支持多种集中式缓存服务器，如 Redis
3. 由于大量的缓存读取会导致 L2 的网络带宽成为整个系统的瓶颈，因此 L1 的目标是降低对 L2 的读取次数

L1过期时不通知L2删除，L2手动删除时，通知其他L1进行删除操作。


### key存储方式 
以下两个方法为例：

0. setn(region,key,value) //不通知其他节点清空消息
1. set(region,key,value)
2. set(key,value)


. 为了一个redis服务器,多个项目公用，key前缀添加namespace
. region:为模块或者特定缓存，key:键，value:值
. region不设置时key=>"namespace:key"
. redis,region不为空时存HASH： （“namespace:region”,“key”）,region空时存String，key=>"namespace:key"



### 数据读取方法 

1. 读取顺序  -> L1 -> L2 -> DB
2. 数据更新
    1 从数据库中读取最新数据，依次更新 L1 -> L2 ，广播（定时）清除某个缓存信息
3.可指定读取的级别L1 or L2
4.可指定设定值的级别 L1 OR L2


### 使用注解


@Cache

对应的属性

region  
key
level   缓存级别 默认全部 可选值 1 一级 2二级
expire  过期时间  默认永久
notifyOther  是否通知 默认true

@CacheClear

对应的属性

region
key


@Lock
分布式锁注解（基于redis）

对应的属性

key
lockExpire 过期时间
spainWaitMillSec 自旋等待时间 根据方法的执行时间来设置 默认20ms
spain  是否自旋等待  默认true  如果false的话就会直接返回 否则就会等待


### 什么时候可以用

1.需要内存缓存和redis缓存同时使用的时候
2.需要使用redis的时候
3.需要设置定时过期的缓存的时候
4.多个类公用的内存缓存的时候（大多数情况用map就足够了）

### 注意事项 

1.注意 因为redis hash 不能设置超时所以 有设置超时的自动设置为普通类型 !!!

2.不传region默认使用配置的默认缓存时间 (ehcache redis)

3.不想让其他节点清空消息的一定要使用setn方法

4.spring使用如果在bean初始化的时候就使用了  可能因为依赖redis没有初始化而失败   可以使用 depends-on="xxx" 解决  也可以使用@autowrte 来加载配置j2cache

5. 做为KEY的对象 一定要实现 序列化接口 和 equal hashcode方法 否则无法判断是否为相同的key 导致无法存取对象



### redis 集群设置


### 集成spring 

 

### 集成Spring Boot

先配置好spring boot的redis 配置 

然后 添加注解 @EnableJ2Cache 即可 



TODO
 
1.解决高并发的 cache 可能重复查询问题  （自动续租的方式）

2. cache 管理页面


