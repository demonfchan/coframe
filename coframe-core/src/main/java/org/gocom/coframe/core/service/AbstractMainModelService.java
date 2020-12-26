/**
 * 
 */
package org.gocom.coframe.core.service;

import static org.gocom.coframe.core.exception.CoframeErrorCode.FOUND_DUPLICATED_MODEL_WITH_SAME_CODE;
import static org.gocom.coframe.core.exception.CoframeErrorCode.NOT_FOUND_MODEL_BY_ID;

import java.util.Optional;

import org.gocom.coframe.core.model.AbstractMainModel;
import org.gocom.coframe.core.model.AbstractMainModel_;

/**
 * TODO 此处填写 class 信息
 *
 * @author wangwb (mailto:wangwb@primeton.com)
 */
public abstract class AbstractMainModelService<T extends AbstractMainModel> extends AbstractPersistentModelService<T> {
	@Override
	public void preCreate(T model) {
		if (getRepository().count((root, query, builder) -> {
			return builder.equal(root.get(AbstractMainModel_.code), model.getCode());
		}) >= 1L) {
			throw FOUND_DUPLICATED_MODEL_WITH_SAME_CODE.runtimeException(getRepository().getDomainClass().getName(), model.getCode());
		}
	}

	@Override
	public void preUpdate(final T model) {
		// 根据id找到对象
		Optional<T> inDbModel = getRepository().findOne((root, query, builder) -> {
			return builder.and(builder.equal(root.get(AbstractMainModel_.id), model.getId()));
		});
		// 找着了
		if (inDbModel.isPresent()) {
			T dbModel = inDbModel.get();
			// 检测一下code是否有变化
			if (!dbModel.getCode().equals(model.getCode())) {
				// 如果code变了, 进一步检测新的code是否可用(租户内此表中没有相同的code)
				if (getRepository().count((root, query, builder) -> {
					return builder.equal(root.get(AbstractMainModel_.code), model.getCode());
				}) >= 1L) {
					throw FOUND_DUPLICATED_MODEL_WITH_SAME_CODE.runtimeException(getRepository().getDomainClass().getName(), model.getCode());
				}
			}
		} else { // 找不着, 无法更新
			throw NOT_FOUND_MODEL_BY_ID.runtimeException(getRepository().getDomainClass().getName(), model.getId());
		}
	}
}
