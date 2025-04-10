import java.util.List;

public class RouteEngine {
    private Graph graph;

    public RouteEngine(Graph graph) {
        this.graph = graph;
    }

    public Route calculateRoute(Location start, Location end) {
        Route route = new Route();
        List<String> path = graph.findPath(start.getLabel(), end.getLabel());
        route.setPath(path);
        return route;
    }
}