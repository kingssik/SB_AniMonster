package com.khs.exam.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.khs.exam.demo.repository.MemberRepository;
import com.khs.exam.demo.util.Ut;
import com.khs.exam.demo.vo.Member;
import com.khs.exam.demo.vo.ResultData;

@Service
public class MemberService {
	private MemberRepository memberRepository;
	private AttrService attrService;

	public MemberService(AttrService attrService, MemberRepository memberRepository) {
		this.attrService = attrService;
		this.memberRepository = memberRepository;
	}

	public ResultData<Integer> join(String loginId, String loginPw, String name, String nickname, String cellphoneNum,
			String email) {
// 로그인아이디 중복체크
		Member existsMember = getMemberByLoginId(loginId);

		if (existsMember != null) {
			return ResultData.from("F-7", Ut.f("이미 사용중인 아이디(%s)입니다", loginId));
		}

// 이름 + 이메일 중복체크
		existsMember = getMemberByNameAndEmail(name, email);

		if (existsMember != null) {
			return ResultData.from("F-8", Ut.f("이미 사용중인 이름(%s)과 이메일(%s)입니다", name, email));
		}

		loginPw = Ut.sha256(loginPw);

		memberRepository.join(loginId, loginPw, name, nickname, cellphoneNum, email);

		int id = memberRepository.getLastInsertId();

		return ResultData.from("S-1", "회원가입 신청이 완료되었습니다", "id", id);
	}

	public Member getMemberByNameAndEmail(String name, String email) {
		return memberRepository.getMemberByNameAndEmail(name, email);

	}

	public Member getMemberByLoginId(String loginId) {
		return memberRepository.getMemberByLoginId(loginId);

	}

	public Member getMemberById(int id) {
		return memberRepository.getMemberById(id);
	}

	public ResultData modify(int id, String loginPw, String name, String nickname, String cellphoneNum, String email) {
		loginPw = Ut.sha256(loginPw);
		memberRepository.modify(id, loginPw, name, nickname, cellphoneNum, email);
		return ResultData.from("S-1", "회원정보가 수정되었습니다");
	}

	public String genMemberModifyAuthKey(int actorId) {
		String memberModifyAuthKey = Ut.getTempPassword(10);

		attrService.setValue("member", actorId, "extra", "memberModifyAuthKey", memberModifyAuthKey,
				Ut.getDateStrLater(60 * 5));

		return memberModifyAuthKey;
	}

	public ResultData checkMemberModifyAuthKey(int actorId, String memberModifyAuthKey) {
		String saved = attrService.getValue("member", actorId, "extra", "memberModifyAuthKey");

		if (!saved.equals(memberModifyAuthKey)) {
			return ResultData.from("F-1", "일치하지 않거나 만료되었습니다");
		}

		return ResultData.from("S-1", "정상 코드입니다");
	}

	public int idCheck(String id) {
		int cnt = memberRepository.idCheck(id);
		System.out.println("cnt: " + cnt);
		return cnt;
	}

	public void withdrawMember(int id) {
		memberRepository.withdrawMember(id);

	}

	public int getMembersCount(String authLevel, String searchKeywordTypeCode, String searchKeyword) {
		return memberRepository.getMembersCount(authLevel, searchKeywordTypeCode, searchKeyword);
	}

	public List<Member> getForPrintMembers(String authLevel, int itemsInAPage, int page, String searchKeywordTypeCode,
			String searchKeyword) {

		int limitStart = (page - 1) * itemsInAPage;
		int limitTake = itemsInAPage;
		List<Member> members = memberRepository.getForPrintMembers(authLevel, searchKeywordTypeCode, searchKeyword,
				limitStart, limitTake);

		return members;
	}

	public List<Member> getWithrawMemberByStatus(String authLevel, int itemsInAPage, int page,
			String searchKeywordTypeCode, String searchKeyword) {
		int limitStart = (page - 1) * itemsInAPage;
		int limitTake = itemsInAPage;
		List<Member> members = memberRepository.getWithrawMemberByStatus(limitStart, limitTake);

		return members;
	}

	public List<Member> getBrokenMemberByStatus(String authLevel, int itemsInAPage, int page,
			String searchKeywordTypeCode, String searchKeyword) {
		int limitStart = (page - 1) * itemsInAPage;
		int limitTake = itemsInAPage;
		List<Member> members = memberRepository.getBrokenMemberByStatus(limitStart, limitTake);

		return members;
	}

	public void deleteMember(List<Integer> memberIds) {
		for (int memberId : memberIds) {
			Member member = getMemberById(memberId);

			if (member != null) {
				deleteChosenMember(member);
			}
		}
	}

	private void deleteChosenMember(Member member) {
		memberRepository.deleteChosenMember(member.getId());
	}

	public Member getMemberByDelstatus(int id) {
		return memberRepository.getMemberByDelstatus(id);
	}

	public void recoverMember(List<Integer> memberIds) {
		for (int memberId : memberIds) {
			Member member = getMemberById(memberId);

			if (member != null) {
				recoverChosenMember(member);
			}
		}
	}

	private void recoverChosenMember(Member member) {
		memberRepository.recoverMember(member.getId());
	}

	public void breakMember(List<Integer> memberIds) {
		for (int memberId : memberIds) {
			Member member = getMemberById(memberId);

			if (member != null) {
				breakChosenMember(member);
			}
		}
	}

	private void breakChosenMember(Member member) {
		memberRepository.breakMember(member.getId());
	}

	public void breakCancelMember(List<Integer> memberIds) {
		for (int memberId : memberIds) {
			Member member = getMemberById(memberId);

			if (member != null) {
				breakCancelChosenMember(member);
			}
		}
	}

	private void breakCancelChosenMember(Member member) {
		memberRepository.breakCancelMember(member.getId());

	}

	public int getMembersCountByStatus(String authLevel, String status, String searchKeywordTypeCode,
			String searchKeyword) {
		return memberRepository.getMembersCountByStatus(authLevel, status, searchKeywordTypeCode, searchKeyword);
	}

}
