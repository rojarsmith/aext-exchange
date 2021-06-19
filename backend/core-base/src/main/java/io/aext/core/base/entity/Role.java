package io.aext.core.base.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import io.aext.core.base.model.entity.Permission;
import lombok.Data;

/**
 * @author rojar
 *
 * @date 2021-06-16
 */
@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "role", uniqueConstraints = { @UniqueConstraint(columnNames = "code") })
public class Role implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Role code
	 */
	@NotNull
	private String code;

	/**
	 * Role name
	 */
	@NotNull
	private String name;

	/**
	 * Role's permission
	 */
	@ManyToMany(targetEntity = Permission.class, fetch = FetchType.EAGER)
	private List<Permission> permissionList;

	public Role() {
	}

	public Role(String code, String name, List<Permission> permissionList) {
		this.code = code;
		this.name = name;
		this.permissionList = permissionList;
	}
}
