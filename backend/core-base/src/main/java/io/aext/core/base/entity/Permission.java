package io.aext.core.base.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import io.aext.core.base.constant.ResourceType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author rojar
 *
 * @date 2021-06-15
 */
@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode
@Table(name = "permission")
public class Permission {
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "mid")
	@GenericGenerator(name = "mid", strategy = "io.aext.core.base.dao.generator.ManulInsertGenerator")
	@Id
	Long id;

	String path;

	String name;

	@Enumerated(EnumType.STRING)
	ResourceType type;
}
