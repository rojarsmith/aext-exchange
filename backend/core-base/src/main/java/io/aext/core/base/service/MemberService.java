package io.aext.core.base.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.aext.core.base.model.entity.Member;
import io.aext.core.base.repository.MemberRepository;

/**
 * @author rojar
 *
 * @date 2021-06-19
 */
@Service
public class MemberService  {
	@Autowired
	MemberRepository memberRepository;

	public Optional<Member> findByUsername(String username) {
		return memberRepository.findByUsername(username);
	}

	public Optional<Member> findByEmail(String email) {
		return memberRepository.findMemberByEmail(email);
	}

	public boolean isEmailExist(String email) {
		return memberRepository.getAllByEmailEquals(email).size() > 0 ? true : false;
	}

	public boolean isUsernameExist(String username) {
		return memberRepository.getAllByUsernameEquals(username).size() > 0 ? true : false;
	}

	public Member save(Member member) {
		return memberRepository.save(member);
	}
}
