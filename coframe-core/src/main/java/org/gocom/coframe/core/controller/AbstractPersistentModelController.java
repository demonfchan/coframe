/**
 * 
 */
package org.gocom.coframe.core.controller;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import org.gocom.coframe.core.model.AbstractPersistentModel;
import org.gocom.coframe.core.service.AbstractPersistentModelService;
import org.gocom.coframe.core.support.CoframeValidationGroups;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiOperation;

/**
 * 抽像的 Controller 类
 *
 * @author qinsc (mailto:qinsc@primeton.com)
 */
public abstract class AbstractPersistentModelController<T extends AbstractPersistentModel> {
    protected abstract AbstractPersistentModelService<T> getService();

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

    @ApiOperation("按主键集合删除")
    @RequestMapping(value = "/bulk-delete", method = PUT, consumes = ALL_VALUE)
    public void deleteByIds(@RequestBody String[] ids) {
    	getService().deleteByIds(ids);
    }

    @ApiOperation("按主键查询")
    @RequestMapping(value = "/{id}", method = GET, consumes = ALL_VALUE)
    public T findById(@PathVariable(name = "id") String id) {
        return getService().findById(id);
    }
}
