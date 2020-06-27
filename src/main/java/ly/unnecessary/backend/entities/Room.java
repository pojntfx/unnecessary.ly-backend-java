package ly.unnecessary.backend.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Room {
    @Id
    public long id;

    public String title;
}