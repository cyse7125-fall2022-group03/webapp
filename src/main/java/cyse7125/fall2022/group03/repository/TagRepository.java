package cyse7125.fall2022.group03.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cyse7125.fall2022.group03.model.Tag;
import cyse7125.fall2022.group03.model.TagIdentity;

//public interface TagRepository extends JpaRepository<Tag, Integer> {
@Repository
public interface TagRepository extends JpaRepository<Tag, String> {
    
    @Query("Select tag from Tag tag where tag.tagname=?1 and tag.useri=?2" )
    List<Tag> findTagByTagnameAndUserId(String tagname, String userId);		// will give list
    
    
    @Modifying
    @Query("update Tag tag set tag.tagUpdated = ?1 where tag.tagname=?2 and tag.useri=?3")
    int updateTagUpdated(String time, String tagname, String userId);
    
    //@Query("Select tag from Tag tag where tag.tagname=?1 and tag.useri=?2" )
    //Tag findTagByTagnameAndUserId(String tagname, String userId);
}
