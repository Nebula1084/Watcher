package em.watcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import em.watcher.control.ControlDef;
import em.watcher.control.ControlPacket;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static em.watcher.conroller.PacketController.SR;
import static em.watcher.conroller.PacketController.TARGET_ID;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ControlTest4 extends PacketTest {
    ObjectMapper objectMapper = new ObjectMapper();
    final int[] count = new int[1];
    Map<Long, ControlPacket> packetMap = Collections.synchronizedMap(new HashMap<>());

    class TestThread extends Thread {
        MultiValueMap<String, String> sendForm;
        MultiValueMap<String, String> recvForm;

        public TestThread(MultiValueMap<String, String> sendForm, MultiValueMap<String, String> recvForm) {
            this.sendForm = sendForm;
            this.recvForm = recvForm;
        }

        @Override
        public void run() {
            final ControlPacket[] sendResult = new ControlPacket[1];
            ControlPacket recvResult = new ControlPacket();
            Thread sender = new Thread() {
                public void run() {
                    try {
                        byte[] sendBytes = ControlTest4.this.mockMvc.perform(post("/api/control").params(sendForm))
                                .andDo(print()).andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsByteArray();
                        sendResult[0] = ControlTest4.this.objectMapper.readValue(sendBytes, ControlPacket.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            sender.start();
            try {
                sleep(1000);
                byte[] recvBytes = ControlTest4.this.mockMvc.perform(post("/api/control").params(recvForm))
                        .andDo(print()).andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsByteArray();
                recvResult = ControlTest4.this.objectMapper.readValue(recvBytes, ControlPacket.class);
                sender.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            packetMap.put(sendResult[0].getId(), sendResult[0]);
            packetMap.put(recvResult.getId(), recvResult);
        }
    }

    @Test
    public void testControl() throws Exception {
        ControlDef def = controlService.getControlDef(controlDef.getId());
        assertThat(def != null, is(true));
        System.out.println("------------------------------" + (def != null ? def.getId() : null) + "-----------------------------------");
        logger.info(def);
        MultiValueMap<String, String> sendForm = this.getMvm(this.device.getId(), controlDef);
        sendForm.add(TARGET_ID, String.valueOf(target.getId()));
        sendForm.add("f1", "12");
        sendForm.add("f2", "abfc");
        sendForm.add("f3", "1");
        sendForm.add(SR, ControlPacket.Send);

        MultiValueMap<String, String> recvForm = this.getMvm(this.target.getId(), controlDef);
        recvForm.add(SR, ControlPacket.Recv);
        TestThread[] threads = new TestThread[10];
        for (int i = 0; i < 5; i++) {
            threads[i] = new TestThread(sendForm, recvForm);
            threads[i].start();
        }
        for (int i = 0; i < 5; i++) {
            threads[i].join();
        }
        for (Long key : packetMap.keySet()) {
            ControlPacket packet = packetMap.get(key);
            ControlPacket fellow = packetMap.get(packet.getFellowPacketId());
            assertThat(fellow.getFellowPacketId(), is(packet.getId()));
        }
        assertThat(packetMap.size(), is(10));
    }
}
