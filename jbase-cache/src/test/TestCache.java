import com.jayqqaa12.j2cache.J2Cache;
import org.junit.Assert;

public class TestCache {

    private static final String REGION = "REGION";

    private static final String KEY = "TEST";

    public static void main(String[] args) {


        System.setProperty("java.net.preferIPv4Stack", "true"); //Disable IPv6 in JVM


        User user1 = new User();
        user1.setName("12");
        user1.setAge("34");
//

        J2Cache.set(KEY, user1);

        User cache = J2Cache.get(KEY);
        Assert.assertEquals(cache.getName(), user1.getName());
        user1.setName("13");
        J2Cache.set1(KEY, user1);

        cache = J2Cache.get1(KEY);
        Assert.assertEquals(cache.getName(), user1.getName());

        cache= J2Cache.get(REGION,KEY,()->user1,100);

        Assert.assertEquals(cache.getName(), user1.getName());



        cache=J2Cache.get(REGION,KEY);

        Assert.assertEquals(cache.getName(), user1.getName());


        cache=J2Cache.get1(REGION,KEY);

        Assert.assertEquals(cache.getName(), user1.getName());

        cache=J2Cache.get2(REGION,KEY);


        Assert.assertEquals(cache,null);


        cache = J2Cache.get1(KEY);
        J2Cache.set(cache,cache);

        User data=J2Cache.get(cache) ;

        System.out.println(data);
        Assert.assertEquals(cache,data);

        J2Cache.set(12,123);
        int a=J2Cache.get2(12);

        Assert.assertEquals(a,123);


        J2Cache.close();

    }
}
