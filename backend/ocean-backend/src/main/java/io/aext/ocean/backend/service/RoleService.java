package io.aext.ocean.backend.service;

import java.util.List;

import io.aext.ocean.backend.model.entity.Role;

/**
 * @author Rojar Smith
 *
 * @date 2021-06-30
 */
public interface RoleService extends BaseService<RoleService> {
	Role update(Role role);

	List<Role> update(List<Role> roles);
}
