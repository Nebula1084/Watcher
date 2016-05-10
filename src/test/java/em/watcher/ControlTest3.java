package em.watcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import em.watcher.control.ControlDef;
import em.watcher.control.ControlPacket;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.MultiValueMap;

import static em.watcher.conroller.PacketController.SR;
import static em.watcher.conroller.PacketController.TARGET_ID;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ControlTest3 extends PacketTest {

    ObjectMapper objectMapper = new ObjectMapper();

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
        ControlTest3.this.mockMvc.perform(post("/api/control").params(sendForm))
                .andDo(print()).andExpect(status().isNotFound());
        ControlTest3.this.mockMvc.perform(post("/api/control").params(recvForm))
                .andDo(print()).andExpect(status().isNotFound());
    }
}
