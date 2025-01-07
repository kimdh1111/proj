package edu.du.proj1120.comment;

import edu.du.proj1120.fileuploadboard.board.repository.BoardRepository;
import edu.du.proj1120.fileuploadboard.board.service.BoardService;
import edu.du.proj1120.fileuploadboard.entity.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
@Service
public class CommentService {

    @PersistenceContext
    private EntityManager entityManager;

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    @Autowired
    public CommentService(CommentRepository commentRepository, BoardRepository boardRepository) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
    }

//    // 특정 게시물에 대한 댓글 조회
//    public List<Comment> getCommentByBoard(Board board) {
//        System.out.println("--------------------------------------1");
//        List<Comment> list = commentRepository.findByBoard(board);
//        System.out.println("--------------------------------------2");
//        System.out.println(list);
//        if (list.isEmpty()) {
//            System.out.println("No comments found");
//        }
//        return list;
//    }

    // 댓글 등록
    public void addComment(String memo, Board board, String name) {
        Comment comment = new Comment();
        comment.setMemo(memo);
        comment.setBoard(board);  // 댓글이 특정 게시물에 속하게 설정
        comment.setName(name);  // 댓글 작성자의 ID 설정 (로그인한 사용자)
        commentRepository.save(comment);
        commentRepository.flush(); // DB에 즉시 반영
    }

    // 댓글 삭제
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
//    @Transactional
//    public void saveComment(Board board, Comment comment) {
//        // Board 먼저 저장
//        boardRepository.save(board);  // Board 객체가 먼저 저장되어야 합니다.
//// flush 호출을 통해 DB에 강제 반영
//        boardRepository.flush();
//        // 그 후에 Comment 저장
//        comment.setBoard(board);
//        commentRepository.save(comment);
//    }


    @Transactional
    public Comment saveComment(Comment comment, Long boardIdx) {
        // 해당 board를 찾아서 comment에 연결
        Board board = boardRepository.findById(boardIdx).orElseThrow(() -> new IllegalArgumentException("Invalid board id"));

        boardRepository.save(board);

        comment.setBoard(board);

        // 댓글 저장
        return commentRepository.save(comment); // 저장된 댓글을 반환
    }

//    public List<Comment> getCommentByBoard(Long boardIdx) {
//        Board board = new Board();
//        return commentRepository.findByBoard(board); // 게시글에 해당하는 댓글 조회
//    }

    // 특정 게시글에 달린 댓글 조회
    public List<Comment> getCommentsByBoardIdx(Long boardIdx) {
        return commentRepository.findByBoardBoardIdx(Math.toIntExact(boardIdx));
    }

}
