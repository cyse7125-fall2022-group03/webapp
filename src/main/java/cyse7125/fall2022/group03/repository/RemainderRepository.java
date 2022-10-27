package cyse7125.fall2022.group03.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cyse7125.fall2022.group03.model.Remainder;

@Repository
public interface RemainderRepository extends JpaRepository<Remainder, Integer> {

	//void deleteByRemainderId(String remainderId);
	
	//@Modifying
    //@Query("delete from Remainder remainder where remainder.tagname=?2 and remainder.useri=?3")
    //int updateTagUpdated(String time, String tagname, String userId);
}
