package io.aext.core.base.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.aext.core.base.dao.MemberDao;
import io.aext.core.base.entity.Member;

/**
 * @author rojar
 *
 * @date 2021-06-06
 */
@Service
public class MemberService extends BaseService<MemberService> {
	@Autowired
	MemberDao memberDao;

	public Optional<Member> findByUsername(String username) {
		return memberDao.findByUsername(username);
	}

	public Optional<Member> findByEmail(String email) {
		return memberDao.findMemberByEmail(email);
	}

	public boolean isEmailExist(String email) {
		return memberDao.getAllByEmailEquals(email).size() > 0 ? true : false;
	}

	public boolean isUsernameExist(String username) {
		return memberDao.getAllByUsernameEquals(username).size() > 0 ? true : false;
	}

	public Member save(Member member) {
		return memberDao.save(member);
	}
}
