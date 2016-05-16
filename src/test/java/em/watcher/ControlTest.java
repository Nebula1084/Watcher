package em.watcher;


import em.watcher.control.ControlDef;
import em.watcher.control.ControlPacket;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import static em.watcher.conroller.PacketController.SR;
import static em.watcher.conroller.PacketController.TARGET_ID;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ControlTest extends PacketTest {

    @Test
    @Transactional
    public void testSend() throws Exception {
        MultiValueMap<String, String> mvm = this.getMvm(this.device.getId(), controlDef);
        mvm.add(TARGET_ID, String.valueOf(target.getId()));
        mvm.add("f1", "12");
        mvm.add("f2", "sdfasdf");
        mvm.add("f3", "1");
        this.mockMvc.perform(post("/api/control").params(mvm)).andDo(print()).andExpect(status().isBadRequest());
        mvm.add("sr", "S");
        this.mockMvc.perform(post("/api/control").params(mvm)).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void testRecv() throws Exception {
        MultiValueMap<String, String> mvm = this.getMvm(this.target.getId(), controlDef);
        mvm.add("f1", "12");
        mvm.add("f2", "sdfasdf");
        mvm.add("f3", "1");
        mvm.add("sr", "R");
        this.mockMvc.perform(post("/api/control").params(mvm)).andDo(print()).andExpect(status().isBadRequest());
        mvm = this.getMvm(this.device.getId(), controlDef);
        mvm.add("sr", "R");
        this.mockMvc.perform(post("/api/control").params(mvm)).andDo(print()).andExpect(status().isNotFound());
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
        sendForm.add("f2", "abc");
        sendForm.add("f3", "1");
        sendForm.add(SR, ControlPacket.Send);

        MultiValueMap<String, String> recvForm = this.getMvm(this.target.getId(), controlDef);
        recvForm.add(SR, ControlPacket.Recv);
        Thread sender = new Thread() {
            public void run() {
                try {
                    ControlTest.this.mockMvc.perform(post("/api/control").params(sendForm)).andDo(print()).andExpect(status().isOk());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        sender.start();
        Thread.sleep(1000);
        ControlTest.this.mockMvc.perform(post("/api/control").params(recvForm)).andDo(print()).andExpect(status().isOk());
        sender.join();
    }

}