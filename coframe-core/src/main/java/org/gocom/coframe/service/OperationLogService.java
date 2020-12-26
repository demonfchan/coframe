package org.gocom.coframe.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.core.support.CoframeIdUtil;
import org.gocom.coframe.model.LogModel;
import org.gocom.coframe.model.OperationLog;
import org.gocom.coframe.model.OperationLogDetail;
import org.gocom.coframe.model.OperationLog_;
import org.gocom.coframe.repository.OperationLogDetailRepository;
import org.gocom.coframe.repository.OperationLogRepository;
import org.gocom.coframe.sdk.CofContext;
import org.gocom.coframe.sdk.model.CofUser;
import org.gocom.coframe.sdk.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * TODO 此处填写 class 信息
 *
 * @author wangwb (mailto:wangwb@primeton.com)
 */
@Service
public class OperationLogService {
	@Autowired
	private OperationLogRepository repo;

	@Autowired
	private OperationLogDetailRepository detailRepo;

	@Autowired
	private IUserService userService;

	private Sort defaultSort = Sort.by(Sort.Direction.DESC, OperationLog_.operateDate.getName());

	@Transactional(rollbackOn = Throwable.class)
	public OperationLog recordModelCreate(String message, LogModel logModel) {
		return record(OperationLog.Type.CREATE, message, logModel);
	}

	@Transactional(rollbackOn = Throwable.class)
	public OperationLog recordModelUpdate(String message, LogModel oldModel, LogModel newModel) {
		return record(OperationLog.Type.UPDATE, message, oldModel, newModel);
	}

	@Transactional(rollbackOn = Throwable.class)
	public OperationLog recordModelDelete(String message, LogModel logModel) {
		return record(OperationLog.Type.DELETE, message, logModel);
	}

	@Transactional(rollbackOn = Throwable.class)
	public OperationLog recordOther(String message) {
		return record(OperationLog.Type.OTHER, message);
	}

	private OperationLog record(OperationLog.Type operateType, String message, LogModel... models) {
		OperationLog log = new OperationLog();
		CofUser user = null;
		String logId = CoframeIdUtil.generateId(OperationLog.class);
		log.setId(logId);
		log.setOperateType(operateType);
		log.setOperateDate(new Date());
		log.setMessage(message);
		log.setTenantId((String) CofContext.getContext().get(CoframeConstants.CONTEXT_TENANT_ID));
		
		if(StringUtils.isNotBlank((String)CofContext.getContext().get("junit_test"))) {
			user = new CofUser();
			user.setId("testJunitUserId");
			user.setName("testJunitUser");
		} else {
			user = userService.getCurrentLoginedUser();
		}
		
		log.setOperatorId(user.getId());
		log.setOperatorName(user.getName());
		if (models != null && models.length > 0) {
			log.setTargetModelName(models[0].getModelClassName());
			log.setTargetModelId(models[0].getModelId());
			OperationLogDetail detail = new OperationLogDetail();
			detail.setId(logId);
			if (operateType != OperationLog.Type.OTHER) {
				detail.setOldDataJson(JsonUtil.toJson(models[0].getModel()));
			}
			if (operateType == OperationLog.Type.UPDATE) {
				detail.setNewDataJson(JsonUtil.toJson(models[1].getModel()));
			}
			detailRepo.save(detail);
		}
		return repo.save(log);
	}

	public OperationLogDetail getDetailById(String id) {
		return detailRepo.findById(id).orElse(null);
	}

	public List<OperationLog> findByCriteria(OperationLog.Criteria criteria) {
		return repo.findAll(toSpecification(criteria), defaultSort);
	}

	public Page<OperationLog> findByCriteria(OperationLog.Criteria criteria, Pageable pageable) {
		if (pageable == null) {
			pageable = PageRequest.of(CoframeConstants.DEFAULT_BEGIN_PAGE, CoframeConstants.DEFAULT_PAGE_SIZE, defaultSort);
		} else if (pageable.getSort() == null || pageable.getSort() == Sort.unsorted()) {
			pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
		}
		return repo.findAll(toSpecification(criteria), pageable);
	}

	private Specification<OperationLog> toSpecification(OperationLog.Criteria criteria) {
		return (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(builder.equal(root.get(OperationLog_.tenantId), CofContext.getContext().get(CoframeConstants.CONTEXT_TENANT_ID)));
			Optional.ofNullable(criteria.getOperateType()).ifPresent((value) -> predicates.add(builder.equal(root.get(OperationLog_.operateType), value)));
			Optional.ofNullable(criteria.getOperatorId()).ifPresent((value) -> predicates.add(builder.equal(root.get(OperationLog_.operatorId), value)));
			Optional.ofNullable(criteria.getDateBegin()).ifPresent((value) -> predicates.add(builder.greaterThanOrEqualTo(root.get(OperationLog_.operateDate), value)));
			Optional.ofNullable(criteria.getDateEnd()).ifPresent((value) -> predicates.add(builder.lessThanOrEqualTo(root.get(OperationLog_.operateDate), value)));

			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}
}
