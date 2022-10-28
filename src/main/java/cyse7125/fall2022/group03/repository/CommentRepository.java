package cyse7125.fall2022.group03.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cyse7125.fall2022.group03.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

	@Transactional 
	void deleteByCommentId(String commentId);
    
}
