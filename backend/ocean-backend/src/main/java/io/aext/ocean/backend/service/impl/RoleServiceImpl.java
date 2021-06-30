package io.aext.ocean.backend.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.aext.ocean.backend.model.entity.Role;
import io.aext.ocean.backend.repository.RoleRepository;
import io.aext.ocean.backend.service.RoleService;

/**
 * @author Rojar Smith
 *
 * @date 2021-06-30
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
