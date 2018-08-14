import com.jayqqaa12.j2cache.J2Cache;

public class TestSub {
    public static void main(String[] args) throws Exception {
//        Jedis jedis = getJedis();
//        System.out.println(jedis.keys("*"));
//        RedisPubSubListener redisPubSubListener = new RedisPubSubListener();
        //订阅
//        jedis.subscribe(redisPubSubListener, SafeEncoder.encode(CacheConstans.REDIS_CHANNEL));
        User user = new User();
        user.setName("123");

        long loop = 0;
        while (loop++ < 1000) {

            Long  data= J2Cache.get("tttt", 123);


            System.out.println("read test ="+data);
            Thread.sleep(1000);
        }
    }

//    private static JedisPool pool;
//
//    static {
//        // 创建jedis池配置实例
//        JedisPoolConfig config = new JedisPoolConfig();
//        // 设置池配置项值
//        config.setMaxTotal(100);
//        config.setMaxIdle(20);
//        config.setMaxWaitMillis(3000);
//        config.setTestOnReturn(true);
//        config.setTestOnBorrow(true);
//        //need password
//        pool = new JedisPool(config, "106.14.19.47", 6379, 2000, "Cypress203",1);
//    }
//
//    /**
//     * 单个jedis*
//     *
//     * @return
//     */
//    public static Jedis getJedis() {
//        Jedis Jedis = getPool().getResource();
//        return Jedis;
//    }
//
//    public static JedisPool getPool() {
//        return pool;
//    }

}
