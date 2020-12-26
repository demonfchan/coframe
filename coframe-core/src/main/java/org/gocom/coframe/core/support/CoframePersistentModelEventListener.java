/**
 * 
 */
package org.gocom.coframe.core.support;

import org.apache.commons.lang3.StringUtils;
import org.gocom.coframe.core.model.AbstractMainModel;
import org.gocom.coframe.core.model.AbstractPersistentModel;
import org.gocom.coframe.core.support.CoframePersistentModelEvent.Type;
import org.gocom.coframe.model.LogModel;
import org.gocom.coframe.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * TODO 此处填写 class 信息
 *
 * @author wangwb (mailto:wangwb@primeton.com)
 */
@Component
public class CoframePersistentModelEventListener {
	@Autowired
	private OperationLogService operLogSvc;

	@EventListener
	public void onEvent(CoframePersistentModelEvent<?> event) {
		Type type = event.getType();
		AbstractPersistentModel[] models = event.getModels();

		setModelFields(type, models);
		logOperation(type, models);
	}

	private void setModelFields(Type type, AbstractPersistentModel... models) {
		// 需要在记录操作日志前, 设置主键
		if (type == Type.PRE_CREATE) {
			AbstractPersistentModel model = models[0];
			if (StringUtils.isBlank(model.getId())) {
				model.setId(CoframeIdUtil.generateId(models[0].getClass()));
			}
		}
	}

	/**
	 * 记录操作日志
	 * 
	 * @param type
	 * @param models
	 */
	private void logOperation(Type type, AbstractPersistentModel... models) {
		if (type == Type.POST_CREATE) {
			AbstractPersistentModel model = models[0];
			String message = "Create " + models[0].getClass().getSimpleName() + ": " + model.getId();
			operLogSvc.recordModelCreate(message, new LogModel(model.getId(), model.getClass().getSimpleName(), model));
		} else if (type == Type.POST_DELETE) {
			AbstractPersistentModel model = models[0];
			String message = "Delete " + models[0].getClass().getSimpleName() + ": " + model.getId();
			operLogSvc.recordModelDelete(message, new LogModel(model.getId(), model.getClass().getSimpleName(), model));
		} else if (type == Type.POST_UPDATE) {
			// 更新的话是2个实体, 一个是要更新的model, 一个是数据库中存在的model, 这里只需要存一条操作日志
			String message = "Update " + models[0].getClass().getSimpleName() + ": " + digest(models[0]);
			LogModel oldModel = new LogModel(models[0].getId(), models[0].getClass().getSimpleName(), models[0]);
			LogModel newModel = new LogModel(models[1].getId(), models[1].getClass().getSimpleName(), models[1]);
			operLogSvc.recordModelUpdate(message, oldModel, newModel);
		}
	}

	private String digest(AbstractPersistentModel model) {
		String str = "{";
		str += model.getId();
		if (model instanceof AbstractMainModel) {
			str += ", " + ((AbstractMainModel) model).getName();
			str += ", " + ((AbstractMainModel) model).getCode();
		}
		return str + "}";
	}
}
