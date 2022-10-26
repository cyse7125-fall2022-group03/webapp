package cyse7125.fall2022.group03.model;

import java.io.Serializable;
import java.util.Objects;

public class TagIdentity implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private String userId;
    private String tagId;
    
    public TagIdentity() {
        
    }

    public TagIdentity(String userId, String tagId) {
        super();
        this.userId = userId;
        this.tagId = tagId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId, userId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TagIdentity other = (TagIdentity) obj;
        return Objects.equals(tagId, other.tagId) && Objects.equals(userId, other.userId);
    }
    
}
