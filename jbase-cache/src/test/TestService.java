
import com.jayqqaa12.j2cache.spring.annotation.Cache;
import com.jayqqaa12.j2cache.spring.annotation.CacheClear;
import com.jayqqaa12.j2cache.spring.annotation.Lock;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created by 12 on 2017/7/6.
 */
@Service
public class TestService {


    @Cache(key = "test")
    public int test1() {
        System.out.println("test1");
        return 1;
    }

    @Cache(key = "test11", level = 1)
    @CacheClear(key = "test")
    public int test11() {
        System.out.println("test11");
        return 1;
    }

    @CacheClear(key = "test")
    @Cache(key = "test111", level = 2)
    public int test111() {
        System.out.println("test111");
        return 1;
    }

    @CacheClear(key = "test111")
    @Cache(key = "test2#id")
    public int test2(Integer id) {
        System.out.println("test2");
        return 1;
    }


    @Cache(region = "region", key = "test4")
    public int test4() {
        System.out.println("test4");
        return 1;
    }

    @Cache(key = "test5", expire = 100)
    public int test5() {
        System.out.println("test5");
        return 1;
    }

    @Cache(region = "region", key = "test6", expire = 111)
    public int test6() {
        System.out.println("test6");
        return 1;
    }


    @Lock(key = "lock#key",lockExpire = 10)
    public void testLock(int key){
        System.out.println("get lock succ");

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Lock(key = "lock2",lockExpire = 10,spain = false)
    public  void testLock2(){

        System.out.println("get lock2 succ");

    }


//    @Cache(key = "#list.![id]")
//    public int test7(List<User> list) {
//        System.out.println("test7");
//
//        return 1;
//    }
//
//
//    @Cache(key = "test3#user.id")
//    public int test3(User user) {
//        System.out.println("test3");
//        return 1;
//    }
//
//    @Cache(region = "test8", key = "#user.id")
//    public int test8(User user) {
//        System.out.println("test8");
//        return 1;
//    }

}
