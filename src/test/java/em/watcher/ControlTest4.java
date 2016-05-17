package em.watcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import em.watcher.control.ControlDef;
import em.watcher.control.ControlPacket;
import org.junit.Test;
import org.springframework.util.MultiValueMap;

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

            try {
                byte[] sendBytes = ControlTest4.this.mockMvc.perform(post("/api/control").params(sendForm))
                        .andDo(print()).andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsByteArray();
                synchronized (count) {
                    count[0]++;
                }
                byte[] recvBytes = ControlTest4.this.mockMvc.perform(post("/api/control").params(recvForm))
                        .andDo(print()).andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsByteArray();
                synchronized (count) {
                    count[0]++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testControl() throws Exception {
        ControlDef def = controlService.getControlDef(controlDef.getId());
        assertThat(def != null, is(true));
        System.out.println("------------------------------" + (def != null ? def.getId() : null) + "-----------------------------------");
        logger.info(def);
        MultiValueMap<String, String> sendForm = this.getMvm(device, device, controlDef);
        sendForm.add(TARGET_ID, String.valueOf(target.getId()));
        sendForm.add("f1", "12");
        sendForm.add("f2", "abfc");
        sendForm.add("f3", "1");
        sendForm.add(SR, ControlPacket.Send);

        MultiValueMap<String, String> recvForm = this.getMvm(target, target, controlDef);
        recvForm.add(SR, ControlPacket.Recv);
        TestThread[] threads = new TestThread[100];
        for (int i = 0; i < 100; i++) {
            threads[i] = new TestThread(sendForm, recvForm);
            threads[i].start();
        }
        for (int i = 0; i < 100; i++) {
            threads[i].join();
        }
        assertThat(count[0], is(200));
    }
}
