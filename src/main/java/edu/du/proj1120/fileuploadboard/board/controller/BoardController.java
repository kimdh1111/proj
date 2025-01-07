package edu.du.proj1120.fileuploadboard.board.controller;

import edu.du.proj1120.comment.Comment;
import edu.du.proj1120.comment.CommentService;
import edu.du.proj1120.fileuploadboard.board.dto.BoardDto;
import edu.du.proj1120.fileuploadboard.board.dto.BoardFileDto;
import edu.du.proj1120.fileuploadboard.board.service.BoardService;
import edu.du.proj1120.fileuploadboard.entity.Board;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j // 로그 출력을 위한 어노테이션
public class BoardController {

	@Autowired
	private BoardService boardService; // BoardService를 주입받아 사용
	@Autowired
	private CommentService commentService;

	@GetMapping("/")
	public String home(Model model) throws Exception {
		List<BoardDto> list = boardService.selectBoardList();  // 게시글 목록을 서비스에서 조회
		model.addAttribute("list", list);  // 템플릿에 전달
		return "main"; // main.html 템플릿을 반환
	}

	@GetMapping("/main")
	public String home2(Model model) throws Exception {
		List<BoardDto> list = boardService.selectBoardList();  // 게시글 목록을 서비스에서 조회
		model.addAttribute("list", list);  // 템플릿에 전달
		return "main"; // main.html 템플릿을 반환
	}




//	// 게시판 목록을 조회하는 메소드
//	@RequestMapping("/board/openBoardList.do")
//	public ModelAndView openBoardList() throws Exception{
//		log.info("====> openBoardList {}", "게시판 목록 조회"); // 로그 출력
//		ModelAndView mv = new ModelAndView("/board/boardList"); // 게시판 목록 페이지로 이동
//
//		// 게시판 목록을 서비스에서 조회
//		List<BoardDto> list = boardService.selectBoardList();
//		System.out.println(list); // 콘솔에 출력 (디버깅용)
//		mv.addObject("list", list); // 조회된 게시글 목록을 뷰에 전달
//
//		return mv; // 게시글 목록 페이지 반환
//	}

	@RequestMapping("/board/openBoardList.do")
	public ModelAndView openBoardList(@RequestParam(value = "search", required = false) String search) throws Exception {
		log.info("====> openBoardList {}", "게시판 목록 조회");

		ModelAndView mv = new ModelAndView("/board/boardList");

		// 게시판 목록을 서비스에서 조회
		List<BoardDto> list = boardService.selectBoardList();

		// 검색어가 있으면, 검색어와 일치하는 게시글만 필터링
		if (search != null && !search.trim().isEmpty()) {
			list = list.stream()
					.filter(board ->
							(board.getCreatorId() != null && board.getCreatorId().contains(search)) ||
									(board.getTitle() != null && board.getTitle().contains(search)) ||
									(board.getContents() != null && board.getContents().contains(search))
					)
					.collect(Collectors.toList());
		}

		mv.addObject("list", list); // 필터링된 게시글 목록을 뷰에 전달
		return mv;
	}

	// 게시글 작성 페이지를 열기 위한 메소드
	@RequestMapping("/board/openBoardWrite.do")
	public String openBoardWrite() throws Exception{
		// 게시글 작성 페이지로 이동
		return "board/boardWrite";
	}

	// 게시글을 DB에 저장하는 메소드
	@PostMapping("/board/insertBoard.do")
	public String insertBoard(BoardDto board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception{

		// 게시글을 서비스에서 저장하는 메소드 호출
		boardService.insertBoard(board, multipartHttpServletRequest);

		// 저장 후 게시판 목록으로 리디렉션
		return "redirect:/board/openBoardList.do";
	}

//	// 게시글 상세 내용을 조회하는 메소드
//	@RequestMapping("/board/openBoardDetail.do")
//	public ModelAndView openBoardDetail(@RequestParam int boardIdx) throws Exception{
//		ModelAndView mv = new ModelAndView("/board/boardDetail"); // 게시글 상세 페이지로 이동
//
//
//		// 게시글 상세 정보 조회
//		Board board = boardService.selectBoardDetail(boardIdx);
//		mv.addObject("board", board);
////		if (!board.getComment().isEmpty()) {
//////			List<Comment> commentList = commentService.findByBoardBoardIdx(boardidx);
////			List<Comment> comment = commentService.getCommentsByBoardIdx((long) boardIdx);
////			System.out.println("==="+comment);
////			mv.addObject("comment", comment);
////		}
//		List<Comment> comment = commentService.getCommentsByBoardIdx((long) boardIdx);
//
//		mv.addObject("comment", comment);
//
//
//		System.out.println("========"+comment);
//
//		return mv; // 게시글 상세 페이지 반환
//	}


	@RequestMapping("/board/openBoardDetail.do")
	public ModelAndView openBoardDetail(@RequestParam int boardIdx) throws Exception {
		ModelAndView mv = new ModelAndView("/board/boardDetail"); // 게시글 상세 페이지로 이동

		// 게시글 상세 정보 조회
		Board board = boardService.selectBoardDetail(boardIdx);
		mv.addObject("board", board);

		// 댓글 리스트 조회
		List<Comment> comment = commentService.getCommentsByBoardIdx((long) boardIdx);
		mv.addObject("comment", comment);

		return mv; // 게시글 상세 페이지 반환
	}

	// 게시글을 수정하는 메소드
	@RequestMapping("/board/updateBoard.do")
	public String updateBoard(BoardDto board) throws Exception{
		// 수정된 게시글을 서비스에서 처리
		boardService.updateBoard(board);

		// 수정 후 게시판 목록으로 리디렉션
		return "redirect:/board/openBoardList.do";
	}

	// 게시글을 삭제하는 메소드
	@RequestMapping("/board/deleteBoard.do")
	public String deleteBoard(int boardIdx) throws Exception{
		// 게시글 삭제 처리
		boardService.deleteBoard(boardIdx);

		// 삭제 후 게시판 목록으로 리디렉션
		return "redirect:/board/openBoardList.do";
	}



	// 게시글에 첨부된 파일을 다운로드하는 메소드
	@RequestMapping("/board/downloadBoardFile.do")
	public void downloadBoardFile(@RequestParam int idx, @RequestParam int boardIdx, HttpServletResponse response) throws Exception{
		// 현재 작업 디렉토리 경로 출력 (디버깅용)
		String currentPath = Paths.get("").toAbsolutePath().toString();
		System.out.println("---------------------"+currentPath);

		// 파일 정보를 서비스에서 조회
		BoardFileDto boardFile = boardService.selectBoardFileInformation(idx, boardIdx);

		// 파일이 존재하는 경우 다운로드 처리
		if(ObjectUtils.isEmpty(boardFile) == false) {
			String fileName = boardFile.getOriginalFileName(); // 원본 파일명
			// 파일을 바이트 배열로 읽어옴
			byte[] files = FileUtils.readFileToByteArray(new File("./src/main/resources/static"+boardFile.getStoredFilePath()));

			// 응답 헤더 설정 (파일 다운로드)
			response.setContentType("application/octet-stream"); // 파일 바이너리 전송
			response.setContentLength(files.length); // 파일 크기 설정
			response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(fileName,"UTF-8")+"\";"); // 파일명 설정
			response.setHeader("Content-Transfer-Encoding", "binary"); // 바이너리 전송 설정

			// 파일 바이트 데이터를 응답 스트림에 작성
			response.getOutputStream().write(files);
			response.getOutputStream().flush(); // 스트림을 플러시
			response.getOutputStream().close(); // 스트림을 닫음
		}
	}








	@RequestMapping("/board/searchBoard.do")
	public ModelAndView searchBoard(@RequestParam("searchQuery") String searchQuery) throws Exception {
		log.info("====> searchBoard {}", searchQuery); // 검색어 출력

		// 검색어로 게시글 조회
		List<Board> list = boardService.searchBoard(searchQuery);

		ModelAndView mv = new ModelAndView("/board/boardList"); // 게시판 목록 페이지로 이동
		mv.addObject("list", list); // 검색된 게시글 목록을 뷰에 전달

		return mv;
	}

}
