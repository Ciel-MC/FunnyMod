package hk.eric.funnymod.utils.classes.pathFind;

import java.util.*;

public class AStarPathFinder {

    public static Node search(Node start, Node end, int maxSteps, boolean diagonal, boolean dontReturnFail) {
        Queue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.distance(start) + node.distance(end) * 2));
        Set<Node> explored = new HashSet<>();
        Node current = start;
        queue.add(start);
        explored.add(start);
        while (!queue.isEmpty() && explored.size() <= maxSteps) {
            current = queue.poll();
            if (current.equals(end)) {
                return current;
            }
            for (Node neighbour : current.getNeighbours(diagonal)) {
                if (!explored.contains(neighbour)) {
                    neighbour.setParent(current);
                    explored.add(neighbour);
                    queue.add(neighbour);
                }
            }
        }
        return dontReturnFail ? null : current;
    }
}