package cyse7125.fall2022.group03.model;

import java.io.Serializable;
import java.util.Objects;




public class ListsIdentity implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private String userId;
    private String listId;
    
    public ListsIdentity() {
        
    }

    public ListsIdentity(String userId, String listId) {
        super();
        this.userId = userId;
        this.listId = listId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(listId, userId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ListsIdentity other = (ListsIdentity) obj;
        return Objects.equals(listId, other.listId) && Objects.equals(userId, other.userId);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    
    
    
}
