
package em.watcher;

import org.junit.Test;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ReportTest extends PacketTest {


    @Test
    public void testReport() throws Exception {
        MultiValueMap<String, String> mvm = this.getMvm(this.device.getId(), reportDef);
        mvm.add("f1", "12");
        mvm.add("f2", "sdfasdf");
        this.mockMvc.perform(post("/api/report").params(mvm)).andDo(print()).andExpect(status().isOk());
        mvm.add("f3", "sdfasdf");
        this.mockMvc.perform(post("/api/report").params(mvm)).andDo(print()).andExpect(status().isBadRequest());
        mvm = this.getMvm(this.device.getId(), reportDef);
        mvm.add("f3", "12");
        mvm.add("f2", "sdfasdf");
        this.mockMvc.perform(post("/api/report").params(mvm)).andDo(print()).andExpect(status().isBadRequest());
        mvm = this.getMvm(100L, reportDef);
        mvm.add("f1", "12");
        mvm.add("f2", "sdfasdf");
        this.mockMvc.perform(post("/api/report").params(mvm)).andDo(print()).andExpect(status().isForbidden());
    }


}
