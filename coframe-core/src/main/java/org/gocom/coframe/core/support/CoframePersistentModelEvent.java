/**
 *
 */
package org.gocom.coframe.core.support;

import java.lang.reflect.Array;

import org.gocom.coframe.core.model.AbstractPersistentModel;
import org.springframework.context.ApplicationEvent;

/**
 * TODO 此处填写 class 信息
 *
 * @author wangwb (mailto:wangwb@primeton.com)
 */
public class CoframePersistentModelEvent<T extends AbstractPersistentModel> extends ApplicationEvent {
	private static final long serialVersionUID = 5542443911974434071L;

	private Type type;

	public static enum Type {
		PRE_CREATE, POST_CREATE, PRE_UPDATE, POST_UPDATE, PRE_DELETE, POST_DELETE;
	}

	@SuppressWarnings("unchecked")
	public CoframePersistentModelEvent(Type type, T... models) {
		super(Array.newInstance(models[0].getClass(), models.length));
		System.arraycopy(models, 0, getSource(), 0, models.length);
		this.type = type;
	}

	public T getModel() {
		return getModels()[0];
	}

	@SuppressWarnings("unchecked")
	public T[] getModels() {
		return (T[]) getSource();
	}

	public Type getType() {
		return type;
	}
}
