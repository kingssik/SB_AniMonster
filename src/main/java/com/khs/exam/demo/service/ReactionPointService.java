package com.khs.exam.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khs.exam.demo.repository.ArticleRepository;
import com.khs.exam.demo.repository.ReactionPointRepository;
import com.khs.exam.demo.vo.ResultData;

@Service
public class ReactionPointService {

	@Autowired
	private ReactionPointRepository reactionPointRepository;
	@Autowired
	private ArticleService articleService;

	public ResultData actorCanMakeReaction(int actorId, String relTypeCode, int relId) {
		if (actorId == 0) {
			return ResultData.from("F-1", "로그인 후 이용해주세요");
		}
		int sumReactionPointByMemberId = reactionPointRepository.getSumReactionPointByMemberId(actorId, relTypeCode,
				relId);
		
		if(sumReactionPointByMemberId != 0) {
			return ResultData.from("F-2", "리액션을 할 수 없습니다", "sumReactionPointByMemberId", sumReactionPointByMemberId);
		}

		return ResultData.from("S-1", "리액션을 할 수 있습니다", "sumReactionPointByMemberId", sumReactionPointByMemberId);
	}
	
	// 좋아요 처리
	public ResultData addGoodReactionPoint(int actorId, String relTypeCode, int relId) {
		reactionPointRepository.addGoodReactionPoint(actorId, relTypeCode, relId);

		switch (relTypeCode) {
		case "article":
			articleService.increaseGoodReactionPoint(relId);
			break;
		}

		return ResultData.from("S-1", "좋아요 처리 되었습니다");
	}
	
	// 싫어요 처리
	public ResultData addBadReactionPoint(int actorId, String relTypeCode, int relId) {
		reactionPointRepository.addBadReactionPoint(actorId, relTypeCode, relId);

		switch (relTypeCode) {
		case "article":
			articleService.increaseBadReactionPoint(relId);
			break;
		}

		return ResultData.from("S-1", "싫어요 처리 되었습니다");
	}
	
	// 좋아요 취소
	public ResultData deleteGoodReactionPoint(int actorId, String relTypeCode, int relId) {
		reactionPointRepository.deleteGoodReactionPoint(actorId, relTypeCode, relId);

		switch (relTypeCode) {
		case "article":
			articleService.decreaseGoodReactionPoint(relId);
			break;
		}

		return ResultData.from("S-1", "좋아요를 취소했습니다");
		
	}
	
	// 싫어요 취소
	public ResultData deleteBadReactionPoint(int actorId, String relTypeCode, int relId) {
		reactionPointRepository.deleteBadReactionPoint(actorId, relTypeCode, relId);

		switch (relTypeCode) {
		case "article":
			articleService.decreaseBadReactionPoint(relId);
			break;
		}

		return ResultData.from("S-1", "싫어요를 취소했습니다");
		
	}

}
