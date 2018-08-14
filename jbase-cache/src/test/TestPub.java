import com.jayqqaa12.j2cache.J2Cache;

public class TestPub {
    public static void main(String[] args) throws Exception {
//        Jedis jedis = getJedis();
//        System.out.println(jedis.keys("*"));
        User user = new User();
        user.setName("123");

        //发布
        long loop = 1;
        while (loop++ < 555555) {

            J2Cache.set("tttt", 123,loop);
//            Command cmd = new Command(Command.OPT_DELETE_KEY, "12", loop);
//            jedis.publish(SafeEncoder.encode(CacheConstans.REDIS_CHANNEL), cmd.toBuffers());
            Thread.sleep(1111);
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
