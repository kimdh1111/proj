package edu.du.proj1120.fileuploadboard.board.service;

import edu.du.proj1120.comment.Comment;
import edu.du.proj1120.fileuploadboard.board.dto.BoardDto;
import edu.du.proj1120.fileuploadboard.board.dto.BoardFileDto;
import edu.du.proj1120.fileuploadboard.entity.Board;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

public interface BoardService {

	List<BoardDto> selectBoardList() throws Exception;
	
	void insertBoard(BoardDto board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception;

	Board selectBoardDetail(int boardIdx) throws Exception;

	void updateBoard(BoardDto board) throws Exception;

	void deleteBoard(int boardIdx) throws Exception;

	BoardFileDto selectBoardFileInformation(int idx, int boardIdx) throws Exception;


	Board getBoardById(Long boardIdx);

	public void saveBoardAndComment(Board board, Comment comment);

	public List<Board> searchBoard(String searchQuery);
}
