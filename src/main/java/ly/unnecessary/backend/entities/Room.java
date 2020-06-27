package ly.unnecessary.backend.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Room {
    @Id
    int id;

    String title;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}