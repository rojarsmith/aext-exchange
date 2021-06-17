package io.aext.core.base.dao;

import java.util.List;
import java.util.Optional;

import io.aext.core.base.constant.ResourceType;
import io.aext.core.base.entity.Permission;

/**
 * @author rojar
 *
 * @date 2021-06-15
 */
public interface PermissionDao extends BaseDao<Permission> {
	List<Permission> getAllByTypeEquals(ResourceType type);

	Optional<Permission> findById(Long id);
}
