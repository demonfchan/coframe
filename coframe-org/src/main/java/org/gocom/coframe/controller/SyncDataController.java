package org.gocom.coframe.controller;

import static org.gocom.coframe.sdk.CofConstants.API_PATH_PREFIX;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import org.gocom.coframe.sync.service.SyncDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = API_PATH_PREFIX + "/sync", consumes = { APPLICATION_JSON_UTF8_VALUE }, produces = {APPLICATION_JSON_UTF8_VALUE })
@ConditionalOnProperty(prefix = "security.iam.sso", name = "enabled", havingValue = "true")
public class SyncDataController {

	@Autowired
	private SyncDataService syncDataService;

	@ApiOperation("同步组织机构数据")
	@GetMapping
	public void syncData(@RequestParam(required = false) String startDate, //
			@RequestParam(required = false) String endDate, //
			@RequestParam(required = false, defaultValue = "ALL") String syncMode) {
		syncDataService.syncData(startDate, endDate, syncMode);
	}
}
