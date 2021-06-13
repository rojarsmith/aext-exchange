package io.aext.core.base.entity;

import java.time.Instant;
import java.util.EnumSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.aext.core.base.constant.CommonStatus;
import io.aext.core.base.constant.MemberLevel;
import lombok.Data;

/**
 * @author rojar
 *
 * @date 2021-06-05
 */
@Data
@Entity
public class Member {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@NotBlank
	@Column(unique = true)
	private String username;

	/**
	 * login password
	 */
	@JsonIgnore
	@NotBlank
	private String password;

	/**
	 * fund password
	 */
	@JsonIgnore
	private String fundPassword;

	@Column(unique = true)
	private String email;

	@Column(nullable = false)
	private Boolean emailVerified = false;

	@Column(unique = true)
	private String emailRescue;

	@Column(nullable = false)
	private Boolean emailRescueVerified = false;

	@CreationTimestamp
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Instant registTime;

	private EnumSet<MemberLevel> memberLevel = EnumSet.of(MemberLevel.GENERAL);

	private EnumSet<CommonStatus> commonStatus = EnumSet.of(CommonStatus.NORMAL);
}
