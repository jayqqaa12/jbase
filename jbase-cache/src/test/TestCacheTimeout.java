import com.jayqqaa12.j2cache.J2Cache;

public class TestCacheTimeout {

    public static void main(String[] args) throws Exception {
        System.setProperty("java.net.preferIPv4Stack", "true"); //Disable IPv6 in JVM


        User user1 = new User();
        user1.setName("12");
        user1.setAge("34");



        int i = 1;
        while (i++ < 100) {
            System.out.println("=====================  " + i);

           User user =  J2Cache.get("test","a2345",()->{
               System.out.println("load");
               return user1;
           },3);

            System.out.println(user);
            Thread.sleep(1000);

        }
    }
}