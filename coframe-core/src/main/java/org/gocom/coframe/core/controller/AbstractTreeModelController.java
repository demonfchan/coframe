/**
 * 
 */
package org.gocom.coframe.core.controller;

import static org.gocom.coframe.CoframeConstants.ACTION_PAGING_BY_CRITERIA;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import javax.validation.constraints.NotNull;

import org.gocom.coframe.core.model.AbstractTreeModel;
import org.gocom.coframe.core.model.AbstractTreeModelCriteria;
import org.gocom.coframe.core.model.CoframePageRequest;
import org.gocom.coframe.core.service.AbstractTreeModelService;
import org.gocom.coframe.core.support.CoframeValidationGroups;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiOperation;

/**
 * 
 *
 * @author qinsc (mailto:qinsc@primeton.com)
 */
public abstract class AbstractTreeModelController<T extends AbstractTreeModel<T>, K extends AbstractTreeModelCriteria> {
	protected abstract AbstractTreeModelService<T, K> getService();

	@ApiOperation("新增")
	@RequestMapping(method = POST)
	public T create(@Validated({ CoframeValidationGroups.Create.class }) @RequestBody T model) {
		return getService().create(model);
	}

	@ApiOperation("更新")
	@RequestMapping(method = PUT)
	public T update(@Validated({ CoframeValidationGroups.Update.class }) @RequestBody T model) {
		return getService().update(model);
	}

	@ApiOperation("按主键集合(以','分割)删除")
	@RequestMapping(value = "/{ids}", method = DELETE, consumes = ALL_VALUE)
	public void deleteByIds(@PathVariable(name = "ids") String... ids) {
		getService().deleteByIds(ids);
	}

	@ApiOperation("按主键查询")
	@RequestMapping(value = "/{id}", method = GET, consumes = ALL_VALUE)
	public T findById(@PathVariable(name = "id") String id, //
			@RequestParam(name = "loadChildren", required = false) boolean loadChildren, //
			@RequestParam(name = "loadLevel", required = false, defaultValue = "1") int loadLevel, //
			@RequestParam(name = "loadOthers", required = false) boolean loadOthers //
			) {
		return getService().loadById(id, loadChildren, loadLevel, loadOthers);
	}

	@ApiOperation("分页查询")
	@RequestMapping(value = "/" + ACTION_PAGING_BY_CRITERIA, method = GET, consumes = ALL_VALUE)
	public Page<T> pagingByCriteria(//
			K criteria, //
			@NotNull CoframePageRequest pageRequest) {
		return getService().pagingByCriteria(criteria, pageRequest.createPageable());
	}
}
