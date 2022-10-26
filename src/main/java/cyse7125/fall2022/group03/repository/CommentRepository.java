package cyse7125.fall2022.group03.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cyse7125.fall2022.group03.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    
}
