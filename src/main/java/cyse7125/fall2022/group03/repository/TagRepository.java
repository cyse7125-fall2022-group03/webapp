package cyse7125.fall2022.group03.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cyse7125.fall2022.group03.model.Tag;
import cyse7125.fall2022.group03.model.TagIdentity;
import cyse7125.fall2022.group03.model.Task;

public interface TagRepository extends JpaRepository<Tag, TagIdentity> {
    

}
