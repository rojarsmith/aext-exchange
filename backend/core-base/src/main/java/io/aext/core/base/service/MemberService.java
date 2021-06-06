package io.aext.core.base.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.aext.core.base.dao.MemberDao;
import io.aext.core.base.entity.Member;

/**
 * @author rojar
 *
 * @date 2021-06-05
 */
@Service
public class MemberService extends BaseService<MemberService> {
	@Autowired
	private MemberDao memberDao;

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
