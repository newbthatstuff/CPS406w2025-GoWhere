import java.util.*;
import java.awt.Point;

public class Graph {
    private Map<String, Map<String, Double>> adjList;
    private Map<String, Point> coordinates;

    public Graph() {
        adjList = new HashMap<>();
        coordinates = new HashMap<>();
    }

    public void addNode(String node, int x, int y) {
        adjList.putIfAbsent(node, new HashMap<>());
        coordinates.put(node, new Point(x, y));
    }

    public void addEdge(String from, String to) {
        Point p1 = coordinates.get(from);
        Point p2 = coordinates.get(to);
        double distance = Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
        adjList.get(from).put(to, distance);
        adjList.get(to).put(from, distance);
    }

    public List<String> findPath(String start, String end) {
        Map<String, Double> distances = new HashMap<>();
        Map<String, String> parent = new HashMap<>();
        class Node implements Comparable<Node> {
            String name;
            double distance;

            Node(String name, double distance) {
                this.name = name;
                this.distance = distance;
            }

            @Override
            public int compareTo(Node other) {
                return Double.compare(this.distance, other.distance);
            }
        }

        PriorityQueue<Node> pq = new PriorityQueue<>();
        Set<String> visited = new HashSet<>();

        for (String node : adjList.keySet()) {
            distances.put(node, Double.MAX_VALUE);
        }
        distances.put(start, 0.0);
        pq.add(new Node(start, 0.0));

        while (!pq.isEmpty()) {
            Node currentNode = pq.poll();
            String node = currentNode.name;
            if (visited.contains(node)) continue;
            visited.add(node);

            if (node.equals(end)) break;

            for (Map.Entry<String, Double> neighbor : adjList.get(node).entrySet()) {
                String nextNode = neighbor.getKey();
                if (visited.contains(nextNode)) continue;

                double weight = neighbor.getValue();
                double newDist = distances.get(node) + weight;

                if (newDist < distances.get(nextNode)) {
                    distances.put(nextNode, newDist);
                    parent.put(nextNode, node);
                    pq.add(new Node(nextNode, newDist));
                }
            }
        }

        List<String> path = new ArrayList<>();
        String current = end;
        while (current != null) {
            path.add(current);
            current = parent.getOrDefault(current, null);
        }
        Collections.reverse(path);

        List<String> correctedPath = new ArrayList<>();
        for (String node : path) {
            correctedPath.add(node);
            if (node.equals(end)) break;
        }

        System.out.println("Path from " + start + " to " + end + ": " + correctedPath);
        return correctedPath;
    }

    public double getDistance(String from, String to) {
        return adjList.get(from).getOrDefault(to, Double.MAX_VALUE);
    }

    public Point getCoordinates(String node) {
        return coordinates.get(node);
    }

    public Map<String, Point> getCoordinates() {
        return coordinates;
    }
}