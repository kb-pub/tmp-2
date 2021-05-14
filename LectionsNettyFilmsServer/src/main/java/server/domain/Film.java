package server.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import server.db.NotLoadedCollection;

import java.util.Collection;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Film {
    private long id;
    private String title;
    private short duration;
    private Collection<Actor> actors = new NotLoadedCollection<>();

    public Film(long id, String title, short duration) {
        this.id = id;
        this.title = title;
        this.duration = duration;
    }
}
