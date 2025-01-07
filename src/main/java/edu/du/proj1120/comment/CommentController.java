package edu.du.proj1120.comment;

import edu.du.proj1120.fileuploadboard.board.service.BoardService;
import edu.du.proj1120.fileuploadboard.entity.Board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/board/boardDetail")
public class CommentController {

    private final CommentService commentService;
    private final BoardService boardService;


    @Autowired
    public CommentController(CommentService commentService, BoardService boardService, CommentRepository commentRepository) {
        this.commentService = commentService;
        this.boardService = boardService;

    }

//    // 댓글 리스트 조회 (boardIdx가 URL 경로에 있으므로 이를 사용하여 댓글을 가져옴)
//    @GetMapping
//    public String getComment(@PathVariable Long boardIdx, Model model) {
//        Board board = boardService.getBoardById(boardIdx);  // boardIdx로 실제 Board 객체 조회
//        List<Comment> comment = commentService.getCommentByBoard(board);  // 해당 Board에 대한 댓글 리스트 조회
//
//        model.addAttribute("board", board);  // 모델에 게시물 정보 추가
//        model.addAttribute("comment", comment);  // 모델에 댓글 리스트 추가
//        return "board/boardDetail";  // Thymeleaf 뷰를 반환
//    }
    @Transactional
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/{boardIdx}/comment")
    public String addComment(@PathVariable Long boardIdx, @RequestParam String memo, Principal principal,CommentDto commentDto) {
        System.out.println("여기");

        Board board = boardService.getBoardById(boardIdx);  // 게시판 ID로 실제 Board 객체를 조회
        System.out.println(board);
        // 로그인한 사용자 ID를 가져옴 (principal.getName()을 통해 세션에서 사용자 ID를 가져옵니다)
        String name = principal.getName(); // 로그인한 사용자의 ID (로그인된 사용자가 없으면 예외 처리 필요)

        // 댓글 객체 생성 및 설정
        Comment comment = new Comment();
        comment.setMemo(commentDto.getMemo());
        comment.setName(name);  // 댓글 작성자 설정
        comment.setBoard(board);  // 댓글이 달릴 게시글 설정

        commentService.addComment(commentDto.getMemo(), board, name);  // 댓글을 추가하는 서비스 호출
        return "redirect:/board/openBoardDetail.do?boardIdx=" + boardIdx;  // 댓글 등록 후 해당 게시물 페이지로 리다이렉트
    }

    @RequestMapping("/{boardIdx}/comment/{commentId}/delete")
    public String deleteComment(@PathVariable Long boardIdx, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);  // 댓글 삭제
        return "redirect:/board/openBoardDetail.do?boardIdx=" + boardIdx;  // 댓글 삭제 후 해당 게시물 페이지로 리다이렉트
    }

//    @GetMapping("/{boardIdx}")
//    public ResponseEntity<Board> getBoardDetail(@PathVariable Long boardIdx) {
//        Board board = boardService.getBoardById(boardIdx);
//
//        if (board == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//
//        return ResponseEntity.ok(board);
//    }
//
//    @GetMapping("/{boardIdx}")
//    public String getBoardDetail(@PathVariable Long boardIdx, Model model, Principal principal) {
//        // 게시글 정보 조회
//        Board board = boardService.getBoardById(boardIdx);
//        model.addAttribute("board", board);
//        System.out.println("여기가문제다1");
//        // 댓글 리스트 조회
//        List<Comment> comment = commentService.getCommentsByBoardIdx(boardIdx);
//
//        // 로그인한 사용자 정보가 있으면 사용자 이름을 전달 (댓글 작성자 이름을 설정할 때 사용)
//        String name = principal != null ? principal.getName() : null;
//
//        // 모델에 게시글, 댓글, 로그인 사용자 이름 정보 추가
//
//
//        model.addAttribute("boardIdx", boardIdx);
//        model.addAttribute("comment", comment);
//        model.addAttribute("name", name);  // 댓글 작성자 정보를 위해 사용
//
//        // 게시글 상세 페이지로 이동
//        return "board/boardDetail";  // boardDetail.html 템플릿으로 이동
//    }


    @GetMapping("/{boardIdx}")
    public String getBoardDetail(@PathVariable Long boardIdx, Model model, Principal principal) {
        // 게시글 정보 조회
        Board board = boardService.getBoardById(boardIdx);
        model.addAttribute("board", board);

        // 댓글 리스트 조회
        List<Comment> comment = commentService.getCommentsByBoardIdx(boardIdx);
        model.addAttribute("comment", comment);

        // 로그인한 사용자 정보가 있으면 사용자 이름을 전달
        String name = principal != null ? principal.getName() : null;
        model.addAttribute("name", name);  // 댓글 작성자 정보를 위해 사용

        // 게시글 상세 페이지로 이동
        return "board/boardDetail";  // boardDetail.html 템플릿으로 이동
    }
    @PostMapping("/save")
    public ResponseEntity<Comment> saveComment(@RequestBody Comment comment, @RequestParam Long boardIdx, Principal principal) {
        // 댓글을 저장하고, 저장된 댓글을 반환

        Comment savedComment = commentService.saveComment(comment,boardIdx);
        return ResponseEntity.ok(savedComment);
    }
    // 특정 게시글에 달린 댓글 목록 조회
    @GetMapping("/board/{boardIdx}")
    public List<Comment> getCommentsByBoard(@PathVariable Long boardIdx) {
        return commentService.getCommentsByBoardIdx(boardIdx);
    }

}
