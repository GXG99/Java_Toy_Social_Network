package socialnetwork.graph;

import java.util.Objects;

public class Vertex<Long> {
    public Long value;

    public Vertex(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex<?> vertex = (Vertex<?>) o;
        return Objects.equals(value, vertex.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
