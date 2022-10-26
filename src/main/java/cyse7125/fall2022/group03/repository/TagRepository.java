package cyse7125.fall2022.group03.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cyse7125.fall2022.group03.model.Tag;

//public interface TagRepository extends JpaRepository<Tag, TagIdentity> {
public interface TagRepository extends JpaRepository<Tag, Integer> {
    
    @Query("Select tag from Tag tag where tag.tagname=?1 and tag.useri=?2" )
    Tag findTagByTagnameAndUserId(String tagname, String userId);
}
