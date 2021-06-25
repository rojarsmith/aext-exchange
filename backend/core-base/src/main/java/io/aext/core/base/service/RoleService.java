package io.aext.core.base.service;

import java.util.List;

import io.aext.core.base.model.entity.Role;

/**
 * @author rojar
 *
 * @date 2021-06-25
 */
public interface RoleService extends BaseService<RoleService> {
	Role update(Role role);

	List<Role> update(List<Role> roles);
}
