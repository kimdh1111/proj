package edu.du.proj1120.fileuploadboard.board.mapper;

import edu.du.proj1120.fileuploadboard.board.dto.BoardDto;
import edu.du.proj1120.fileuploadboard.board.dto.BoardFileDto;
import edu.du.proj1120.fileuploadboard.entity.Board;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BoardMapper {
	List<BoardDto> selectBoardList() throws Exception;
	
	void insertBoard(BoardDto board) throws Exception;

	BoardDto selectBoardDetail(int boardIdx) throws Exception;

	void updateHitCount(int boardIdx) throws Exception;
	
	void updateBoard(BoardDto board) throws Exception;
	
	void deleteBoard(int boardIdx) throws Exception;

	void insertBoardFileList(List<BoardFileDto> list) throws Exception;

	List<BoardFileDto> selectBoardFileList(int boardIdx) throws Exception;

	BoardFileDto selectBoardFileInformation(@Param("idx") int idx, @Param("boardIdx" )int boardIdx);

	@Select("SELECT * FROM t_board WHERE board_idx = #{boardIdx}")
	Board getBoardById(Long boardIdx);

	BoardDto toDto(Board board);

	Board toEntity(BoardDto boardDto);
}
