package org.gocom.coframe.sync.scheduletask;

import org.gocom.coframe.sync.service.SyncDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import static org.gocom.coframe.CoframeConstants.CRON_TIME;
import org.springframework.stereotype.Component;
@EnableScheduling   // 2.开启定时任务
@Component
@ConditionalOnProperty(prefix = "security.iam.sso", name = "enabled", havingValue = "true")
public class SyncScheduleTask {
	
	@Autowired
	public SyncDataService syncService;
	
	@Scheduled(cron = "${" + CRON_TIME + "}")
    public void configureTasks() {
    	syncService.syncData(null, null, "ALL");
    }
}
