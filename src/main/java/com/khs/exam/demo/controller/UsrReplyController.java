package com.khs.exam.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.khs.exam.demo.service.ArticleService;
import com.khs.exam.demo.service.ReplyService;
import com.khs.exam.demo.util.Ut;
import com.khs.exam.demo.vo.Article;
import com.khs.exam.demo.vo.Reply;
import com.khs.exam.demo.vo.ResultData;
import com.khs.exam.demo.vo.Rq;

@Controller
public class UsrReplyController {

	@Autowired
	private ReplyService replyService;
	@Autowired
	private Rq rq;
	@Autowired
	private ArticleService articleService;

	@RequestMapping("/usr/reply/doWrite")
	@ResponseBody
	public String doWrite(String relTypeCode, int relId, String body, String replaceUri) {

		if (Ut.empty(relTypeCode)) {
			return rq.jsHistoryBack("relTypeCode을(를) 입력하세요");
		}

		if (Ut.empty(relId)) {
			return rq.jsHistoryBack("relId을(를) 입력하세요");
		}

		if (Ut.empty(body)) {
			return rq.jsHistoryBack("body을(를) 입력하세요");
		}

		ResultData<Integer> writeReplyRd = replyService.writeReply(rq.getLoginedMemberId(), relTypeCode, relId, body);

		int id = writeReplyRd.getData1();

		if (Ut.empty(replaceUri)) {
			switch (relTypeCode) {
			case "article":
				replaceUri = Ut.f("../article/detail?id=%d", relId);
				break;
			}

		}

		return rq.jsReplace(writeReplyRd.getMsg(), replaceUri);
	}

	@RequestMapping("/usr/reply/modify")
	public String showModify(int id, String replaceUri, Model model) {

		if (Ut.empty(id)) {
			return rq.jsHistoryBack("id을(를) 입력하세요");
		}

		Reply reply = replyService.getForPrintReply(rq.getLoginedMember(), id);

		if (reply == null) {
			return rq.jsHistoryBack(Ut.f("%d번 댓글은 없습니다", id));
		}

		if (reply.isExtra__actorCanModify() == false) {
			return rq.jsHistoryBack("해당 권한이 없습니다");
		}

		String relDataTitle = null;

		switch (reply.getRelTypeCode()) {
		case "article":
			Article article = articleService.getArticle(reply.getRelId());
			relDataTitle = article.getTitle();
			break;
		}

		model.addAttribute("reply", reply);
		model.addAttribute("relDataTitle", relDataTitle);

		return "/usr/reply/modify";
	}

	@RequestMapping("/usr/reply/doModify")
	@ResponseBody
	public String doModify(int id, String body, String replaceUri) {

		if (Ut.empty(id)) {
			return rq.jsHistoryBack("id을(를) 입력하세요");
		}

		Reply reply = replyService.getForPrintReply(rq.getLoginedMember(), id);
		
		if (reply == null) {
			return rq.jsHistoryBack(Ut.f("%d번 댓글은 없습니다", id));
		}
		
		if (reply.isExtra__actorCanModify() == false) {
			return rq.jsHistoryBack("해당 권한이 없습니다");
		}
		
		if (Ut.empty(body)) {
			return rq.jsHistoryBack("내용을 입력하세요");
		}

		ResultData modifyReplyRd = replyService.modifyReply(id, body);

		if (Ut.empty(replaceUri)) {
			switch (reply.getRelTypeCode()) {
			case "article":
				replaceUri = Ut.f("../article/detail?id=%d", reply.getRelId());
				break;
			}

		}

		return rq.jsReplace(modifyReplyRd.getMsg(), replaceUri);
	}

	@RequestMapping("/usr/reply/doDelete")
	@ResponseBody
	public String doDelete(int id, String replaceUri) {

		if (Ut.empty(id)) {
			return rq.jsHistoryBack("id을(를) 입력하세요");
		}

		Reply reply = replyService.getForPrintReply(rq.getLoginedMember(), id);

		ResultData deleteReplyRd = replyService.deleteReply(id);

		if (Ut.empty(replaceUri)) {
			switch (reply.getRelTypeCode()) {
			case "article":
				replaceUri = Ut.f("../article/detail?id=%d", reply.getRelId());
				break;
			}

		}

		return rq.jsReplace(deleteReplyRd.getMsg(), replaceUri);
	}
}