package edu.du.proj1120.fileuploadboard.board.repository;

import edu.du.proj1120.fileuploadboard.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface BoardRepository extends JpaRepository<Board, Long> {
    // 추가적인 메소드가 필요하면 여기에 작성

//    @Query("SELECT b FROM Board b WHERE " +
//            "(:title IS NULL OR b.title LIKE %:title%) AND " +
//            "(:contents IS NULL OR b.contents LIKE %:contents%) AND " +
//            "(:creatorId IS NULL OR b.creatorId LIKE %:creatorId%)")
//    List<Board> searchBoards(String title, String contents, String creatorId);

    // 제목, 내용, 작성자에 검색어가 포함된 게시글을 조회
    List<Board> findByTitleContainingOrContentsContainingOrCreatorIdContaining(String title, String contents, String creatorId);
}

