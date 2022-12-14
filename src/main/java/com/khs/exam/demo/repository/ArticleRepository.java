package com.khs.exam.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.khs.exam.demo.vo.Article;

@Mapper
public interface ArticleRepository {

	public void writeArticle(int memberId, int boardId, String title, String body);

	@Select("""
			<script>
				SELECT A.*,
				M.nickname AS extra__writerName
				FROM article AS A
				LEFT JOIN `member` AS M
				ON A.memberId = M.id
				WHERE 1
				AND A.id = #{id}
			</script>
											""")
	public Article getForPrintArticle(int id);

	@Select("""
			<script>
				SELECT A.*,
				M.nickname AS extra__writerName
				FROM article AS A
				LEFT JOIN `member` AS M
				ON A.memberId = M.id
				WHERE 1
				<if test="boardId != 0">
					AND A.boardId = #{boardId}
				</if>
				<if test="searchKeyword != ''">
					<choose>
						<when test="searchKeywordTypeCode == 'title'">
							AND A.title LIKE CONCAT('%', #{searchKeyword}, '%')
						</when>
						<when test="searchKeywordTypeCode == 'body'">
							AND A.body LIKE CONCAT('%', #{searchKeyword}, '%')
						</when>
						<otherwise>
							AND (
							A.title LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							A.body LIKE CONCAT('%', #{searchKeyword}, '%')
							)
						</otherwise>
					</choose>
				</if>
				ORDER BY A.id DESC
				<if test="limitTake != -1">
					LIMIT #{limitStart}, #{limitTake}
				</if>
			</script>
								""")
	public List<Article> getForPrintArticles(int boardId, String searchKeywordTypeCode, String searchKeyword,
			int limitStart, int limitTake);

	@Select("""
			<script>
				SELECT A.*,
				M.nickname AS extra__writerName
				FROM article AS A
				LEFT JOIN `member` AS M
				ON A.memberId = M.id
				WHERE 1
				<if test="boardId != 0">
					AND A.boardId = #{boardId}
				</if>
				ORDER BY A.hitCount DESC
				<if test="limitTake != -1">
					LIMIT #{limitStart}, #{limitTake}
				</if>
			</script>
								""")
	public List<Article> getForPrintArticlesByHitCount(int boardId, int limitStart, int limitTake);

	@Select("""
			<script>
				SELECT A.*,
				M.nickname AS extra__writerName
				FROM article AS A
				LEFT JOIN `member` AS M
				ON A.memberId = M.id
				WHERE 1
				<if test="boardId != 0">
					AND A.boardId = #{boardId}
				</if>
				ORDER BY A.regDate DESC
				<if test="limitTake != -1">
					LIMIT #{limitStart}, #{limitTake}
				</if>
			</script>
								""")
	public List<Article> getForPrintArticlesByRegDate(int boardId, int limitStart, int limitTake);

	@Select("""
			<script>
				SELECT A.*,
				M.nickname AS extra__writerName
				FROM article AS A
				LEFT JOIN `member` AS M
				ON A.memberId = M.id
				WHERE 1
				<if test="boardId != 0">
					AND A.boardId = #{boardId}
				</if>
				ORDER BY A.goodReactionPoint DESC
				<if test="limitTake != -1">
					LIMIT #{limitStart}, #{limitTake}
				</if>
			</script>
								""")
	public List<Article> getForPrintArticlesByGoodReactionPoint(int boardId, int limitStart, int limitTake);

	public void deleteArticle(int id);

	public void modifyArticle(int id, String title, String body);

	public int getLastInsertId();

	@Select("""
			<script>
			SELECT COUNT(*) AS cnt
			FROM article AS A
			WHERE 1
			<if test="boardId != 0">
				AND A.boardId = #{boardId}
			</if>
			<if test="searchKeyword != ''">
				<choose>
					<when test="searchKeywordTypeCode == 'title'">
						AND A.title LIKE CONCAT('%', #{searchKeyword}, '%')
					</when>
					<when test="searchKeywordTypeCode == 'body'">
						AND A.body LIKE CONCAT('%', #{searchKeyword}, '%')
					</when>
					<otherwise>
						AND (
							A.title LIKE CONCAT('%', #{searchKeyword}, '%')
							OR A.body LIKE CONCAT('%', #{searchKeyword}, '%')
							)
					</otherwise>
				</choose>
			</if>
			</script>
							""")
	public int getArticlesCount(int boardId, String searchKeywordTypeCode, String searchKeyword);

	@Update("""
			<script>
			UPDATE article
			SET hitCount = hitCount + 1
			WHERE id = #{id}
			</script>
				""")
	public int increaseHitCount(int id);

	@Select("""
			<script>
			SELECT hitCount
			FROM article
			WHERE id = #{id}
			</script>
				""")
	public int getArticleHitCount(int id);

	@Select("""
			<script>
				SELECT IFNULL(SUM(RP.point),0) AS s
				FROM reactionPoint AS RP
				WHERE RP.relTypeCode = 'article'
				AND RP.relId = #{id}
				AND RP.memberId = #{memberId}
			</script>
						""")
	public int getSumReactionPointByMemberId(int memberId, int id);

	@Update("""
			<script>
			UPDATE article
			SET goodReactionPoint = goodReactionPoint + 1
			WHERE id = #{relId}
			</script>
				""")
	public int increaseGoodReactionPoint(int relId);

	@Update("""
			<script>
			UPDATE article
			SET badReactionPoint = badReactionPoint + 1
			WHERE id = #{relId}
			</script>
				""")
	public int increaseBadReactionPoint(int relId);

	@Update("""
			<script>
			UPDATE article
			SET goodReactionPoint = goodReactionPoint - 1
			WHERE id = #{relId}
			</script>
				""")
	public int decreaseGoodReactionPoint(int relId);

	@Update("""
			<script>
			UPDATE article
			SET badReactionPoint = badReactionPoint - 1
			WHERE id = #{relId}
			</script>
				""")
	public int decreaseBadReactionPoint(int relId);

	@Select("""
			<script>
				SELECT *
				FROM article
				WHERE id = #{id}
			</script>
			""")
	public Article getArticle(int id);

}