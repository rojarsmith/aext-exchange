package io.aext.core.service.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author rojar
 *
 * @date 2021-06-14
 */
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class MemberDetails implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2954475768648193332L;

	Long userid;
	String username;
	String email;
	String password;
	boolean enable;
	Collection<? extends GrantedAuthority> authorities;
	String jwtHash;

	public MemberDetails(Long userid, String username, String email, String password, boolean enable,
			Collection<? extends GrantedAuthority> authorities) {
		this.userid = userid;
		this.username = username;
		this.email = email;
		this.password = password;
		this.enable = enable;
		this.authorities = authorities;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.enable;
	}

	public void setJwtHash(String jwtHash) {
		this.jwtHash = jwtHash;
	}
}
