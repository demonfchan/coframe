/**
 * 
 */
package org.gocom.coframe.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.gocom.coframe.core.exception.CoframeErrorCode;
import org.gocom.coframe.core.model.AbstractTreeModel;
import org.gocom.coframe.core.model.AbstractTreeModelCriteria;
import org.gocom.coframe.core.model.AbstractTreeModel_;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO 此处填写 class 信息
 *
 * @author wangwb (mailto:wangwb@primeton.com)
 */
public abstract class AbstractTreeModelService<T extends AbstractTreeModel<T>, K extends AbstractTreeModelCriteria> extends AbstractMainModelService<T> {
	/**
	 * 按 id 查询某个节点，并把其子节点加载出来
	 * 
	 * @param id
	 * @return
	 */
	public T loadById(String id) {
		return loadById(id, true, 0, false);
	}

	/**
	 * 按 id 查询某个节点，并把其子节点加载出来，最多向下加载 level 层
	 * 
	 * @param id
	 * @param loadChildren
	 * @param level
	 * @return
	 */
	public T loadById(String id, boolean loadChildren, int level) {
		return loadById(id, loadChildren, level, false);
	}

	/**
	 * 按 id 查询某个节点，并把其子节点加载出来，最多向下加载 level 层, 可同时将节点的其它资源一并加载
	 * 
	 * @param id           节点的 id
	 * @param loadChildren 是否加载子节点
	 * @param level        从当前节点开始，向下加载子节点的层级
	 * @param loadOthers   是否同时加载节点的其它资源
	 * @return
	 */
	public T loadById(String id, boolean loadChildren, int level, boolean loadOthers) {
		if (loadChildren) {
			return loadTreeModel(findById(id), level, loadOthers);
		} else {
			return findById(id, true);
		}
	}

	@Override
	public void preCreate(T model) {
		super.preCreate(model);
		model.setLeaf(true);
		if (model.getParentId() == null) {
			generateRootSeq(model);
			model.setLevel(1);
		} else {
			getRepository().findById(model.getParentId()).ifPresent(parent -> {
				parent.setLeaf(false);
				getRepository().save(parent);
				generateSeq(parent, model);
				model.setLevel(parent.getLevel() + 1);
			});
		}
	}

	@Override
	public void preUpdate(T model) {
		super.preUpdate(model);
		checkAndMove(model);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void preDelete(String id) {
		super.preDelete(id);
		checkDelete(id);
	}

	/**
	 * 分页查询
	 * 
	 * @param criteria
	 * @param pageRequest
	 * @return
	 */
	public abstract Page<T> pagingByCriteria(K criteria, Pageable pageable);

	/**
	 * 为非根节点生成 seq
	 * 
	 * @param model
	 */
	@SuppressWarnings("serial")
	public void generateSeq(T parent, T model) {
		Sort sort = Sort.by(Direction.DESC, AbstractTreeModel_.seq.getName());
		List<T> models = getRepository().findAll(toSpecification(parent.getId(), null), sort);
		models.sort(new AbstractTreeModel<T>() {
		});
		if (models == null || models.size() == 0) {
			model.setSeq(parent.getSeq() + "1.");
		} else {
			String lastSeqStr = models.get(0).getSeq();
			lastSeqStr = lastSeqStr.substring(0, lastSeqStr.length() - 1);
			String lastSeqNo = lastSeqStr.substring(lastSeqStr.lastIndexOf(".") + 1);
			model.setSeq(parent.getSeq() + (Integer.parseInt(lastSeqNo) + 1) + ".");
		}
	}

	/**
	 * 为根节点生成 seq
	 * 
	 * @param model
	 */
	@SuppressWarnings("serial")
	public void generateRootSeq(T model) {
		Sort sort = Sort.by(Direction.DESC, AbstractTreeModel_.seq.getName());
		List<T> models = getRepository().findAll(toSpecification(null, null), sort);
		models.sort(new AbstractTreeModel<T>() {
		});
		if (models == null || models.size() == 0) {
			model.setSeq(".1.");
		} else {
			String lastSeqStr = models.get(0).getSeq();
			String lastSeqNo = lastSeqStr.substring(1, lastSeqStr.length() - 1);
			model.setSeq("." + (Integer.parseInt(lastSeqNo) + 1) + ".");
		}
	}

	/**
	 * 从给定节点开始，将指定层的子节点加载出来
	 * 
	 * @param model 需要加载子的节点
	 * @param level 给定节点层次为0，向下加载的最大层数
	 * @return
	 */
	public T loadTreeModel(T model, int level, boolean loadOthers) {
		Sort sort = Sort.by(Direction.ASC, AbstractTreeModel_.seq.getName());
		// 将当前节点下的子节点查出来
		List<T> models = getRepository().findAll(toSpecification(model.getSeq()), sort);
		int modelLevel = StringUtils.countMatches(model.getSeq(), ".");
		if (model.getParentId() != null) {
			model.setParent(getRepository().findById(model.getParentId()).get());
		}
		if (models != null && models.size() > 0) {
			Map<String, T> cache = new HashMap<>();
			cache.put(model.getId(), model);
			models.forEach(m -> {
				if (level > 0 && (StringUtils.countMatches(m.getSeq(), ".") - modelLevel > level)) {
					return;
				}

				T parent = cache.get(m.getParentId());
				if (parent != null) {
					parent.getChildren().add(m);
					cache.put(m.getId(), m);
					if (loadOthers) {
						loadTreeModelOthers(m);
					}
				}
			});
		}
		return model;
	}

	/**
	 * 加载树节点的其它对象. 如果树下还有其它节点需要加载，可复覆写这个方法。比如机构下除了有机构，还有网位，工作组等，
	 * 可覆写此方法，使得在加载机构树的同时，将其它资源一并加载出来。注意控制层级
	 * 
	 * @param model
	 */
	public void loadTreeModelOthers(T model) {
	}

	/**
	 * 检查，如果有需要，移动节点至新的父节点之下
	 * 
	 * @param model
	 */
	public void checkAndMove(T model) {
		getRepository().findById(model.getId()).ifPresent(modelInDB -> {
			String newParentId = model.getParentId();
			String oldParentId = modelInDB.getParentId();

			// 给的新的父节点 id 为空，且原来不为根节点，表示移至根节点
			if (newParentId == null) {
				if (oldParentId != null) {
					move(modelInDB, null);
				}
			} else
			// 给了新的父 id,则需要进行移动
			if (!newParentId.equals(oldParentId)) {
				move(modelInDB, newParentId);
			}
		});
	}

	/**
	 * 检查一下节点能否删除
	 * 
	 * @param id
	 */
	private void checkDelete(String id) {
		if (getRepository().count(toSpecification(id, null)) > 0) {
			throw getDeleteNotAllowedByHasChildrenErrorCode().runtimeException();
		}
	}
	
	/**
	 * 获取因为其下有子节点，不允许删除的错误码
	 * @return
	 */
	public abstract CoframeErrorCode getDeleteNotAllowedByHasChildrenErrorCode();

	/**
	 * 移动节点
	 * 
	 * @param model
	 * @param newParent
	 */
	public void move(T model, String newParentId) {
		final T loadedModel = loadTreeModel(model, 0, false);
		if (model.getParentId() != null) {
			final T oldParent = getRepository().findById(model.getParentId()).get();
			// 如果老的父节点下就这一个子，将父改为叶节点
			if (oldParent != null && getRepository().count(toSpecification(oldParent.getId(), null)) == 1) {
				oldParent.setLeaf(true);
				getRepository().save(oldParent);
			}
		}

		T newParent = null;
		if (newParentId != null) {
			newParent = findById(newParentId);
		}

		// 更新之前，记录老的 seq
		String oldParentSeq = loadedModel.getSeq();

		// 移动至根节点
		if (newParent == null) {
			generateRootSeq(loadedModel);
			loadedModel.setParentId(null);
			loadedModel.setLevel(1);
			getRepository().save(loadedModel);
		} else { // 移动至非根节点
			// 更新新的父节点为非叶节点
			if (newParent.isLeaf()) {
				newParent.setLeaf(false);
				getRepository().save(newParent);
			}

			// 为被移动的节点生成的新的 seq, 新的 level
			generateSeq(newParent, loadedModel);
			loadedModel.setParentId(newParent.getId());
			loadedModel.setLevel((!Optional.ofNullable(newParent.getLevel()).isPresent() ? 0 : newParent.getLevel()) + 1);
			getRepository().save(loadedModel);
		}

		// 递归更新所有的被移动节点的子节点的 seq 与 level
		loadedModel.getChildren().forEach(child -> {
			updateMovedChild(child, loadedModel, oldParentSeq, loadedModel.getSeq());
		});
	}

	/**
	 * 更新移动后的子节点
	 * 
	 * @param model
	 * @param oldParentSeq
	 * @param newParentSeq
	 */
	protected void updateMovedChild(T model, T newParent, String oldParentSeq, String newParentSeq) {
		model.setSeq(newParent.getSeq() + model.getSeq().substring(oldParentSeq.length()));
		model.setLevel(newParent.getLevel() + 1);
		getRepository().save(model);
		model.getChildren().forEach(child -> {
			updateMovedChild(child, model, oldParentSeq, newParentSeq);
		});
	}
	
	/**
	 * 构造查询条件, 只以 seq 为条件查询
	 * 
	 * @param seq
	 * @return
	 */
	protected Specification<T> toSpecification(String seq) {
		return (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			Optional.ofNullable(seq).ifPresent((value) -> predicates.add(builder.like(root.get(AbstractTreeModel_.seq), seq + "%")));
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}

	/**
	 * 构造查询条件。如果 parentId 给值 null, 则查询条件中添加 isNull 的限定
	 * 
	 * @param parentId
	 * @param seq
	 * @return
	 */
	protected Specification<T> toSpecification(String parentId, String seq) {
		return (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (parentId == null) {
				predicates.add(builder.isNull(root.get(AbstractTreeModel_.parentId)));
			} else {
				predicates.add(builder.equal(root.get(AbstractTreeModel_.parentId), parentId));
			}
			Optional.ofNullable(seq).ifPresent((value) -> predicates.add(builder.like(root.get(AbstractTreeModel_.seq), seq + "%")));
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}
}
