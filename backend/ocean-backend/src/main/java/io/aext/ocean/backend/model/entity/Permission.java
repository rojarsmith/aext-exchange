package io.aext.ocean.backend.model.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import io.aext.ocean.backend.enums.ResourceType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author Rojar Smith
 *
 * @date 2021-06-30
 */
@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode
@Table(name = "permission")
public class Permission {
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ManulInsertIdentityGenerator")
	@GenericGenerator(name = "ManulInsertIdentityGenerator", strategy = "io.aext.ocean.backend.model.entity.generator.ManulInsertIdentityGenerator")
	@Id
	Long id;

	String path;

	String name;

	@Enumerated(EnumType.STRING)
	ResourceType type;
}
