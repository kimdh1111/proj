package edu.du.proj1120.fileuploadboard.board.dto;

import edu.du.proj1120.fileuploadboard.entity.Board;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
public class BoardDto extends Board {
	
	private Integer boardIdx;
	
	private String title;
	
	private String contents;
	
	private Integer hitCnt;
	
	private String creatorId;
	
	private String createdDatetime;
	
	private String updaterId;
	
	private String updatedDatetime;
	
	private List<BoardFileDto> fileList;


}
