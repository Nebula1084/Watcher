package em.watcher;

import em.watcher.control.ControlPacket;
import em.watcher.control.ControlPacketPool;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BlockingPacketTest extends PacketTest {

    @Autowired
    ControlPacketPool packetPool;

    @Test
    public void test() throws InterruptedException {
        packetPool.offer(device, new ControlPacket());
        packetPool.blockingPoll(device);
    }

//    @Test(expected = TestTimedOutException.class, timeout = 1000)
//    public void testBlocking() throws InterruptedException {
//        packetPool.offer(device, new ControlPacket());
//        packetPool.blockingPoll(device);
//        packetPool.blockingPoll(device);
//    }
}
