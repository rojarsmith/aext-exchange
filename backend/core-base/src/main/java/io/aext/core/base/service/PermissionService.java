package io.aext.core.base.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.aext.core.base.enums.ResourceType;
import io.aext.core.base.model.entity.Permission;
import io.aext.core.base.repository.PermissionRepository;

/**
 * @author rojar
 *
 * @date 2021-06-15
 */
@Service
public class PermissionService extends BaseService<Permission> {
	@Autowired
	PermissionRepository permissionRepository;

	public List<Permission> getAllByTypeEquals(ResourceType type) {
		return permissionRepository.getAllByTypeEquals(type);
	}

	public Optional<Permission> findById(Long id) {
		return permissionRepository.findById(id);
	}
	
//	public List<Resource> getIdsByUserId(Long id) {
//		return resourceDao.findById(id);
//	}

	public int deleteByType(ResourceType type) {
		List<Permission> data = permissionRepository.getAllByTypeEquals(type);
		permissionRepository.deleteAll(data);
		return data.size();
	}

	public List<Permission> save(List<Permission> resource) {
		return permissionRepository.saveAll(resource);
	}

	public Permission save(Permission resource) {
		return permissionRepository.save(resource);
	}
}
