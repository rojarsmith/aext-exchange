package io.aext.core.base.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.aext.core.base.dao.RoleDao;
import io.aext.core.base.entity.Role;

/**
 * @author rojar
 *
 * @date 2021-06-16
 */
@Service
public class RoleService extends BaseService<RoleService> {
	@Autowired
	RoleDao roleDao;

	public Role save(Role role) {
		return roleDao.save(role);
	}
}
