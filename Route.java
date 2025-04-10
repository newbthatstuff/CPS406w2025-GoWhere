import java.util.ArrayList;
import java.util.List;

public class Route {
    private List<String> path;

    public Route() {
        path = new ArrayList<>();
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public List<String> getPath() {
        return path;
    }
}