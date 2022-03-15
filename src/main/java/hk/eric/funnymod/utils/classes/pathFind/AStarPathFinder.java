package hk.eric.funnymod.utils.classes.pathFind;

import hk.eric.funnymod.modules.combat.KillAuraModule;
import hk.eric.funnymod.utils.classes.caches.TriCache;
import hk.eric.funnymod.utils.classes.lamdba.TriFunction;

import java.util.*;
import java.util.function.BiFunction;

public class AStarPathFinder {

    private static final TriCache<Node, Node, Boolean, Comparator<Node>> cache = new TriCache<>(
            (start, end, diagonal) -> KillAuraModule.infiniteAuraDistanceCalculationAccuracy.getValue().getComparatorGenerator().apply(start,end,diagonal),
            20
    );

    //TODO: Add vertical clipping
    public static Node search(Node start, Node end, int maxSteps, boolean diagonal, BiFunction<Node, Node, Boolean> ended, boolean dontReturnFail) {
        Queue<Node> queue = new PriorityQueue<>(cache.get(start,end,diagonal));
        Set<Node> explored = new HashSet<>();
        Node current = start;
        queue.add(start);
        explored.add(start);
        while (!queue.isEmpty() && explored.size() <= maxSteps) {
            current = queue.poll();
            if (ended.apply(current, end)) {
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

    public enum distanceAccuracy {
        INTEGER((start, end, diag) -> {
            return Comparator.comparingDouble(node -> node.distanceInt(start, diag) + node.distanceInt(end, diag) * 2);
        }, "Calculate distance in integer, 100% accurate with no diagonal movement, may cause large inaccuracies with diagonal movement"),
        FLOAT((start, end, diag) -> {
            return Comparator.comparingDouble(node -> node.distanceFloat(start, diag) + node.distanceFloat(end, diag) * 2);
        }, "Calculate distance in float, slower calculation but more accurate with diagonal movement"),
        DOUBLE((start, end, diag) -> {
            return Comparator.comparingDouble(node -> node.distanceDouble(start, diag) + node.distanceDouble(end, diag) * 2);
        }, "Calculate distance in double, slower calculation but most accurate with diagonal movement");

        private final TriFunction<Node, Node, Boolean, Comparator<Node>> comparatorGenerator;
        private final String description;

        distanceAccuracy(TriFunction<Node, Node, Boolean, Comparator<Node>> comparatorGenerator, String description) {
            this.comparatorGenerator = comparatorGenerator;
            this.description = description;
        }

        public TriFunction<Node, Node, Boolean, Comparator<Node>> getComparatorGenerator() {
            return comparatorGenerator;
        }

        public String getDescription() {
            return description;
        }
    }
}