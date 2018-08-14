import com.jayqqaa12.j2cache.J2Cache;
import com.jayqqaa12.j2cache.redis.queue.DelayQueueProcessListener;
import com.jayqqaa12.j2cache.redis.queue.Message;
import com.jayqqaa12.j2cache.redis.queue.RedisDelayQueue;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class
TestRedisDelayQueue {
	RedisDelayQueue queue = null;

	@Before
	public void init() {

		J2Cache.get("aaa");

		queue = new RedisDelayQueue("test",
				new DelayQueueProcessListener() {
					public void pushCallback(Message message) {
					}
					public void peekCallback(Message message) {
						System.out.println("message----->" + message);

					}
					public void ackCallback(Message message) {
					}
				});

		queue.clear();


	}

	@Test
	public void testCreate() throws InterruptedException {
		Message message = new Message();
		for (int i = 0; i < 10; i++) {
			message.setId(i + "");
			message.setPriority(0);
			message.setTimeout(3000+i*1000);
			message.setCreateTime(System.currentTimeMillis());

			queue.push(message);
		}
		queue.listen();

		TimeUnit.SECONDS.sleep(111);
		queue.close();
	}

}
