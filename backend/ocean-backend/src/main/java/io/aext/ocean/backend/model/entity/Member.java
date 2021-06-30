package io.aext.ocean.backend.model.entity;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.aext.ocean.backend.enums.MemberStatus;
import lombok.Data;

/**
 * @author Rojar Smith
 *
 * @date 2021-06-30
 */
@Data
@Entity
public class Member {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	Long id;

	@NotBlank
	@Column(unique = true)
	String username;

	/**
	 * login password
	 */
	@JsonIgnore
	@NotBlank
	String password;

	/**
	 * fund password
	 */
	@JsonIgnore
	String fundPassword;

	@Column(unique = true)
	String email;

	@Column(unique = true)
	String emailRescue;

//	@CreationTimestamp
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Instant registTime;

	@Column(name = "status")
	EnumSet<MemberStatus> memberLevel = EnumSet.noneOf(MemberStatus.class);

	@ManyToMany(targetEntity = Role.class, fetch = FetchType.LAZY)
	List<Role> roleList;

	String jwtHash;
}
