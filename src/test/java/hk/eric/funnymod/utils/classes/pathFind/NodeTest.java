package hk.eric.funnymod.utils.classes.pathFind;

import hk.eric.funnymod.utils.classes.minecraftPlus.BetterBlockPos;
import org.junit.jupiter.api.Test;

class NodeTest {
    @Test
    public void testNode() {
        Node node = new Node(new BetterBlockPos(0, 0, 0));
        Node node1 = new Node(new BetterBlockPos(1, 1, 1));
        Node node2 = new Node(new BetterBlockPos(2, 2, 2));
        Node node3 = new Node(new BetterBlockPos(3, 3, 3));
        node3.setParent(node2);
        node2.setParent(node1);
        node1.setParent(node);
        node3.asStream().forEach(lambdaNode -> System.out.println(lambdaNode.getPos().getX()));
    }
}