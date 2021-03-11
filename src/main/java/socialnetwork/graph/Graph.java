package socialnetwork.graph;

import socialnetwork.domain.Tuple;

import java.util.*;

public class Graph {
    public Map<Long, List<Long>> adjacency;
    public Map<Long, List<Long>> componentMap;
    public Map<Long, Long> visited;

    public Graph() {
        adjacency = new HashMap<>();
        visited = new HashMap<>();
        componentMap = new HashMap<>();
    }

    /**
     * Method that creates a vertex for a user with specified ID.
     *
     * @param vertex - User ID
     */
    public void addVertex(Long vertex) {
        adjacency.put(vertex, new LinkedList<>());
    }


    /**
     * Method that creates an edge of two friend users from the network.
     *
     * @param source      - ID of first user
     * @param destination - ID of second user
     */
    public void addEdge(Long source, Long destination) {
        if (!adjacency.containsKey(source))
            addVertex(source);
        if (!adjacency.containsKey(destination))
            addVertex(destination);
        adjacency.get(source).add(destination);
        adjacency.get(destination).add(source);
        visited.put(source, 0L);
        visited.put(destination, 0L);
    }

    /**
     * Method that returns the list of the strongest connected vertexes
     *
     * @return - list with indexes of vertexes in strongest connected components
     */
    public List<Long> findStrongestConnectedComponent() {
        Long component = 0L;
        Tuple<Long, Long> longestComponent = new Tuple<>(-1L, -1L);

        adjacency.forEach((K, V) -> {
            visited.replace(K, 0L);
        });

        componentMap.put(component, new LinkedList<>());
        for (Long vertex : visited.keySet()) {
            if (visited.get(vertex) == 0L) {
                DFS(vertex, component);
                component += 1L;
                componentMap.put(component, new LinkedList<>());
            }
        }

        componentMap.forEach((K, V) -> {
            if (!V.isEmpty()) {
                Long length = longestPathLength(V.get(0), K);
                if (length > longestComponent.getRight()) {
                    longestComponent.setLeft(K);
                    longestComponent.setRight(length);
                }
            }
        });

        return componentMap.get(longestComponent.getLeft());
    }


    /**
     * Method that computes the diameter of a component
     *
     * @param start - starting index
     * @return longest path in component
     */
    private Long longestPathLength(Long start, Long component) {
        Tuple<Long, Long> t1, t2;

        // first bfs to find one end point of
        // longest path
        t1 = BFS(start, component);

        // second bfs to find actual longest path
        t2 = BFS(t1.getLeft(), component);

        return t2.getRight();
    }

    /**
     * Method that finds the farthest node and its distance
     *
     * @param start - starting vertex
     * @return Tuple of farthest node index and distance
     */
    private Tuple<Long, Long> BFS(Long start, Long component) {
        Long[] dis = new Long[adjacency.size() + 1];
        Arrays.fill(dis, -1L);
        Queue<Long> q = new LinkedList<>();

        q.add(start);

        // Set distance from start to 0
        dis[Math.toIntExact(start)] = 0L;
        while (!q.isEmpty()) {
            Long t = q.poll();

            // Verificam toti vecinii
            for (int i = 0; i < adjacency.get(t).size(); ++i) {
                Long v = adjacency.get(t).get(i);
                if (dis[Math.toIntExact(v)] == -1) {
                    q.add(v);
                    dis[Math.toIntExact(v)] = dis[Math.toIntExact(t)] + 1;
                }
            }
        }
        Long maxDis = 0L;
        Long nodeIdx = 0L;

        // Cel mai departat nod si distanta lui
        for (Long i = 0L; i < componentMap.get(component).size(); ++i) {
            if (dis[Math.toIntExact(i)] > maxDis) {
                maxDis = dis[Math.toIntExact(i)];
                nodeIdx = i;
            }
        }
        return new Tuple<>(nodeIdx, maxDis);
    }

    /**
     * Method that runs DFS through the network and adds vertexes to their component
     *
     * @param vertex    - starting vertex for DFS
     * @param component - starting component number
     */
    private void DFS(Long vertex, Long component) {
        visited.put(vertex, 1L);
        componentMap.get(component).add(vertex);
        for (Long child : adjacency.get(vertex)) {
            if (visited.get(child) == 0L) {
                DFS(child, component);
            }
        }
    }

    /**
     * Method that computes and returns number of connected components in the network graph
     *
     * @return - number of connected components in the network graph
     */
    public int getNumberOfConnectedComponents() {
        adjacency.forEach((K, V) -> {
            visited.replace(K, 0L);
        });

        int count = 0;
        for (Long vertex : visited.keySet()) {
            if (visited.get(vertex) == 0L) {
                findDFS(vertex);
                count++;
            }
        }
        return count;
    }

    /**
     * Method that computes DFS from a given vertex (User ID) and marks all other reachable vertexes as visited.
     *
     * @param vertex Starting ID for the DFS
     */
    private void findDFS(Long vertex) {
        visited.put(vertex, 1L);
        for (Long child : adjacency.get(vertex)) {
            if (visited.get(child) == 0L) {
                findDFS(child);
            }
        }
    }


    /**
     * Method that returns the adjacency list of the network.
     *
     * @return - adjacency list
     */
    public Map<Long, List<Long>> getAdjacency() {
        return adjacency;
    }

    /**
     * Method that sets the adjacency list of the network
     *
     * @param adjacency - adjacency list
     */
    public void setAdjacency(Map<Long, List<Long>> adjacency) {
        this.adjacency = adjacency;
    }
}
