package cyse7125.fall2022.group03.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cyse7125.fall2022.group03.model.Tag;

//public interface TagRepository extends JpaRepository<Tag, Integer> {
@Repository
public interface TagRepository extends JpaRepository<Tag, String> {
    
    //@Query("Select tag from Tag tag where tag.tagname=?1 and tag.taskObject.userId=?2" )		//was useri initially ?
	@Query("Select tag from Tag tag where tag.tagname=?1 and tag.useri=?2" )		//was useri initially ?
    List<Tag> findTagByTagnameAndUserId(String tagname, String userId);		// will give list
    
    //@Query("Select tag from Tag tag where tag.tagname=?1 and tag.taskObject.taskId=?2" )
	//List<Tag> findTagByTagnameAndTaskId(String tagname, String taskId);		// will give list
    
    
    @Modifying
    @Transactional
    //@Query("update Tag tag set tag.tagUpdated = ?1 where tag.tagname=?2 and tag.userId=?3")
    @Query("update Tag tag set tag.tagUpdated = ?1 where tag.tagname=?2 and tag.useri=?3")
    int updateTagUpdated(LocalDateTime time, String tagname, String userId);
    
    //@Query("Select tag from Tag tag where tag.tagname=?1 and tag.useri=?2" )
    //Tag findTagByTagnameAndUserId(String tagname, String userId);
    
    @Modifying
    @Transactional
    @Query("update Tag tag set tag.tagUpdated = ?1 where tag.tagname=?2 and tag.useri=?3")
    int updateAllTOnUpdate(LocalDateTime time, String tagname, String userId);
    
}
