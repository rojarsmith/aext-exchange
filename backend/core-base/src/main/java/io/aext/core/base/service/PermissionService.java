package io.aext.core.base.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;

import io.aext.core.base.enums.ResourceType;
import io.aext.core.base.model.entity.Permission;

/**
 * @author rojar
 *
 * @date 2021-06-19
 */
public interface PermissionService extends BaseService<Permission> {

	List<Permission> readPermissions(Long userId);

	Optional<Permission> readPermission(Long id);

	List<Permission> readPermissions(ResourceType type);

	List<Permission> readPermissions(ResourceType type, PageRequest page);

	Permission update(Permission resource);

	List<Permission> update(List<Permission> resource);

	int delete(ResourceType type);
}
