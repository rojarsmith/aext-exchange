package io.aext.core.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.aext.core.base.model.entity.Role;
import io.aext.core.base.repository.RoleRepository;
import io.aext.core.base.service.RoleService;

/**
 * @author rojar
 *
 * @date 2021-06-25
 */
@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	RoleRepository roleRepository;

	@Override
	public Role update(Role role) {
		return roleRepository.save(role);
	}

	@Override
	public List<Role> update(List<Role> roles) {
		return roleRepository.saveAll(roles);
	}
}
