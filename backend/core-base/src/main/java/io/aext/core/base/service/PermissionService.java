package io.aext.core.base.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.aext.core.base.enums.ResourceType;
import io.aext.core.base.model.entity.Permission;

/**
 * @author rojar
 *
 * @date 2021-06-19
 */
public interface PermissionService extends BaseService<Permission> {
	List<Permission> getIdsByUserId(Long userId);

	List<Permission> getAllByTypeEquals(ResourceType type);

	public Optional<Permission> findById(Long id);

	public int deleteByType(ResourceType type);

	public List<Permission> save(List<Permission> resource);

	public Permission save(Permission resource);
}
