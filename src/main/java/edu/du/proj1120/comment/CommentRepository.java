package edu.du.proj1120.comment;

import edu.du.proj1120.comment.Comment;
import edu.du.proj1120.fileuploadboard.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    List<Comment> findByBoardBoardIdx(Integer board_boardIdx);
    // 특정 게시물에 대한 댓글 조회

    // 기본적인 JPA 메소드 제공
    void deleteById(Long commentId);
//    List<Comment> findByBoard(Board board);
}
