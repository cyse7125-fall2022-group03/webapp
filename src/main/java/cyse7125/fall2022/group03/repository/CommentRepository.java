package cyse7125.fall2022.group03.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cyse7125.fall2022.group03.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

	//void deleteByCommentId(String commentId);
    
}
