package edu.du.proj1120.comment;

import edu.du.proj1120.fileuploadboard.entity.Board;
import lombok.ToString;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 댓글 작성자 이름
    @Column(name = "memo")
    private String memo; // 댓글 내용

    @ManyToOne(cascade = CascadeType.MERGE)  // // CascadeType.ALL 사용 가능
    @JoinColumn(name = "board_idx")
    private Board board; // 댓글이 속한 게시물

    // 기본 생성자, getter, setter, toString
    public Comment() {}

    public Comment(String name, String memo, Board board) {
        this.name = name;
        this.memo = memo;
        this.board = board;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public String toString() {
        return "Comment{id=" + id + ", name='" + name + "', memo='" + memo + "', board=" + board + '}';
    }
}
