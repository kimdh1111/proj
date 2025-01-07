package edu.du.proj1120.fileuploadboard.entity;

import edu.du.proj1120.comment.Comment;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ToString
@Data
@Entity
@Table(name= "t_board")
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer boardIdx;
	
	private String title;
	
	private String contents;

	@ColumnDefault("0") //default 0
	private Integer hitCnt;
	
	private String creatorId;
	
	private String createdDatetime;
	
	private String updaterId;
	
	private String updatedDatetime;

	@Column(columnDefinition = "varchar(2) default 'N'")
	private String deletedYn;
	// Board와 Comment 간의 관계 설정
	@OneToMany(mappedBy = "board") //cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comment = new ArrayList<>();
}
