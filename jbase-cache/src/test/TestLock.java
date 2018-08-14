import com.jayqqaa12.j2cache.J2Cache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by 12 on 2017/7/14.
 */
public class TestLock {

    public static void main(String[] args) throws InterruptedException {


        ExecutorService executorService = Executors.newFixedThreadPool(10);

        //可重入锁
//        for (int i = 0; i < 500; i++) {
//            executorService.execute(() -> {
//                boolean succ = J2Cache.lock().spinLock("sb", 1);
//                System.out.println(System.currentTimeMillis() + " :>>" + Thread.currentThread().getId() + ":" + succ);
//            });
//        }

        // 正常的锁
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                boolean succ = J2Cache.lock().spinLock("sb", 1);
                System.out.println(System.currentTimeMillis() + " :>>" + Thread.currentThread().getId() + ":" + succ);
            });
        }


        TimeUnit.MINUTES.sleep(111);
    }


}
