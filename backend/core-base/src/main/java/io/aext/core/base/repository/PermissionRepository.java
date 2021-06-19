package io.aext.core.base.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import io.aext.core.base.constant.ResourceType;
import io.aext.core.base.model.entity.Permission;

/**
 * @author rojar
 *
 * @date 2021-06-15
 */
@Repository
public interface PermissionRepository extends BaseRepository<Permission> {
	Optional<Permission> findById(Long id);

	List<Permission> findByIdIn(List<String> ids);

	List<Permission> getAllByTypeEquals(ResourceType type);
}
