package cyse7125.fall2022.group03.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cyse7125.fall2022.group03.model.Lists;
import cyse7125.fall2022.group03.model.ListsIdentity;

@Repository
public interface ListsRepository extends JpaRepository<Lists, ListsIdentity> {

	List<Lists> findByUserId(String userId);

	@Query("Select lists from Lists lists where lists.listId=?1 and lists.userId=?2" )
	Lists findTaskByListIdAndUserId(String listId, String userId);

}
