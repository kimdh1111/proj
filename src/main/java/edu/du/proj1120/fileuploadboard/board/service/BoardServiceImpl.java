package edu.du.proj1120.fileuploadboard.board.service;

import edu.du.proj1120.comment.Comment;
import edu.du.proj1120.comment.CommentRepository;
import edu.du.proj1120.fileuploadboard.board.dto.BoardDto;
import edu.du.proj1120.fileuploadboard.board.dto.BoardFileDto;
import edu.du.proj1120.fileuploadboard.board.mapper.BoardMapper;
import edu.du.proj1120.fileuploadboard.board.repository.BoardRepository;
import edu.du.proj1120.fileuploadboard.common.FileUtils;
import edu.du.proj1120.fileuploadboard.entity.Board;
import edu.du.proj1120.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class BoardServiceImpl implements BoardService{
	
	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private FileUtils fileUtils;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private MemberRepository memberRepository;  // MemberRepository 주입


	@Override
	public List<BoardDto> selectBoardList() throws Exception {
		List<BoardDto> boardList = boardMapper.selectBoardList();  // 게시글 목록 조회
		for (BoardDto board : boardList) {
			// 각 게시글에 첨부된 파일 목록을 추가
			List<BoardFileDto> fileList = boardMapper.selectBoardFileList(board.getBoardIdx());
			board.setFileList(fileList); // 파일 리스트 설정
		}
		return boardList;
	}


	@Override
	public void insertBoard(BoardDto board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
		boardMapper.insertBoard(board);
		List<BoardFileDto> list = fileUtils.parseFileInfo(board.getBoardIdx(), multipartHttpServletRequest);
		if(CollectionUtils.isEmpty(list) == false){
			boardMapper.insertBoardFileList(list);
		}
	}

	@Override
	public Board selectBoardDetail(int boardIdx) throws Exception{
		BoardDto board = boardMapper.selectBoardDetail(boardIdx);
		List<BoardFileDto> fileList = boardMapper.selectBoardFileList(boardIdx);
		board.setFileList(fileList);

		boardMapper.updateHitCount(boardIdx);

		return board;
	}

	@Override
	public void updateBoard(BoardDto board) throws Exception {
		boardMapper.updateBoard(board);
	}

	@Override
	public void deleteBoard(int boardIdx) throws Exception {
		boardMapper.deleteBoard(boardIdx);
	}

	@Override
	public BoardFileDto selectBoardFileInformation(int idx, int boardIdx) throws Exception {
		return boardMapper.selectBoardFileInformation(idx, boardIdx);
	}

	@Override
	public Board getBoardById(Long boardIdx) {
		return boardMapper.getBoardById(boardIdx);
	}

	public void saveBoardAndComment(Board board, Comment comment) {

		// 1. 먼저 게시판을 저장합니다.
		entityManager.persist(board);  // Board 객체 저장

		// 2. 바로 flush하여 데이터베이스에 반영합니다.
		entityManager.flush();  // Board가 DB에 저장되었는지 강제로 반영

		// 3. 댓글을 게시판과 연결
		comment.setBoard(board);  // Comment와 Board를 연결
		entityManager.persist(comment); // Comment 엔티티를 저장
	}
	@Transactional
	public void saveBoardWithComments(Board board, List<Comment> comments) {
		// 댓글 먼저 저장
        commentRepository.saveAll(comments);

		// 게시글 저장
		boardRepository.save(board);
	}


	// 검색어로 게시글을 조회하는 메소드
	public List<Board> searchBoard(String searchQuery) {
		return boardRepository.findByTitleContainingOrContentsContainingOrCreatorIdContaining(searchQuery, searchQuery, searchQuery);
	}



}


