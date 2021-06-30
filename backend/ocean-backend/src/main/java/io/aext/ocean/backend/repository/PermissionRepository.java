package io.aext.ocean.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.aext.ocean.backend.enums.ResourceType;
import io.aext.ocean.backend.model.entity.Permission;

/**
 * @author Rojar Smith
 *
 * @date 2021-06-30
 */
@Repository
public interface PermissionRepository extends BaseRepository<Permission> {
	Optional<Permission> findById(Long id);

	List<Permission> findByIdIn(List<String> ids);

	List<Permission> findByIdGreaterThanEqualAndIdLessThanEqual(Long startId, Long endId);

	// The same as findByIdGreaterThanEqualAndIdLessThanEqual.
	@Query(nativeQuery = true, value = "SELECT * FROM Permission as e WHERE e.id between (:begin) and (:end)")
	List<Permission> findById(@Param("begin") Long begin, @Param("end") Long end);

	List<Permission> getAllByTypeEquals(ResourceType type);

}
