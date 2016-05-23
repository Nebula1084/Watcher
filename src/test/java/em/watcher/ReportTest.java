
package em.watcher;

import em.watcher.report.ReportService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ReportTest extends PacketTest {

    @Autowired
    ReportService reportService;

    @Test
    @Rollback
    @Transactional
    public void testReport() throws Exception {
        MultiValueMap<String, String> mvm = this.getMvm(device, device, reportDef);
        mvm.add("f1", "12");
        mvm.add("f2", "sdfasdf");
        for (int i = 0; i < 20; i++)
            this.mockMvc.perform(post("/api/report").params(mvm)).andDo(print()).andExpect(status().isOk());
        assertThat(reportService.getLatest(reportDef) != null, is(true));

        mvm.add("f3", "sdfasdf");
        this.mockMvc.perform(post("/api/report").params(mvm)).andDo(print()).andExpect(status().isBadRequest());
        mvm = this.getMvm(device, device, reportDef);
        mvm.add("f3", "12");
        mvm.add("f2", "sdfasdf");
        this.mockMvc.perform(post("/api/report").params(mvm)).andDo(print()).andExpect(status().isBadRequest());
        device.setId(100L);
        mvm = this.getMvm(device, device, reportDef);
        mvm.add("f1", "12");
        mvm.add("f2", "sdfasdf");
        this.mockMvc.perform(post("/api/report").params(mvm)).andDo(print()).andExpect(status().isForbidden());

    }


}
