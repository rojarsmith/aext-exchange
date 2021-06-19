package io.aext.core.base.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.aext.core.base.model.entity.Role;
import io.aext.core.base.repository.RoleRepository;

/**
 * @author rojar
 *
 * @date 2021-06-16
 */
@Service
public class RoleService extends BaseService<RoleService> {
	@Autowired
	RoleRepository roleDao;

	public Role save(Role role) {
		return roleDao.save(role);
	}
}
