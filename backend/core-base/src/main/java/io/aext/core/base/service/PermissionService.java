package io.aext.core.base.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.aext.core.base.constant.ResourceType;
import io.aext.core.base.dao.PermissionDao;
import io.aext.core.base.entity.Permission;

/**
 * @author rojar
 *
 * @date 2021-06-15
 */
@Service
public class PermissionService extends BaseService<Permission> {
	@Autowired
	PermissionDao permissionDao;

	public List<Permission> getAllByTypeEquals(ResourceType type) {
		return permissionDao.getAllByTypeEquals(type);
	}

	public Optional<Permission> findById(Long id) {
		return permissionDao.findById(id);
	}
	
//	public List<Resource> getIdsByUserId(Long id) {
//		return resourceDao.findById(id);
//	}

	public int deleteByType(ResourceType type) {
		List<Permission> data = permissionDao.getAllByTypeEquals(type);
		permissionDao.deleteAll(data);
		return data.size();
	}

	public List<Permission> save(List<Permission> resource) {
		return permissionDao.saveAll(resource);
	}

	public Permission save(Permission resource) {
		return permissionDao.save(resource);
	}
}
