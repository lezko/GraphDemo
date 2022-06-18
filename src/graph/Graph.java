package graph;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Graph<T> {
    public class Neighbour {
        public Integer weight;
        public Node node;

        public Neighbour(Node node, Integer weight) {
            this.weight = weight;
            this.node = node;
        }

        public Neighbour(Node node) {
            this(node,null);
        }
    }

    public class Node {
        public T value;
        private final Color DEFAULT_COLOR = Color.lightGray;
        public boolean visited = false;

        public List<Neighbour> neighbours = new LinkedList<>();

        private Color color = DEFAULT_COLOR;

        public Node(T value) {
            this.value = value;
        }

        public void connect(Node node) {
            connectNodes(this, node);
            onChangeAction.run();
        }

        public void resetColor() {
            color = DEFAULT_COLOR;
        }

        public void remove() {
            removeNode(this);
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }
    }

    private final List<Node> nodes = new LinkedList<>();
    private final Runnable onChangeAction;

    public Graph(Runnable runnable) {
        onChangeAction = runnable;
    }

    public Node addNode(T value) {
        Node newNode = new Node(value);
        nodes.add(newNode);
        onChangeAction.run();
        return newNode;
    }

    public void removeNode(int index) {
        Node removeNode = nodes.get(index);
        for (Neighbour neighbour: removeNode.neighbours) {
            neighbour.node.neighbours.remove(removeNode);
        }
        nodes.remove(removeNode);
        onChangeAction.run();
    }

    public void removeNode(Node removeNode) {
        for (Neighbour neighbour: removeNode.neighbours) {
            neighbour.node.neighbours = neighbour.node.neighbours.stream().filter(n -> !n.node.equals(removeNode)).collect(Collectors.toList());
        }
        nodes.remove(removeNode);
        onChangeAction.run();
    }

    public void connectNodes(int index1, int index2) {
        nodes.get(index1).neighbours.add(new Neighbour(nodes.get(index2)));
        nodes.get(index2).neighbours.add(new Neighbour(nodes.get(index1)));
        onChangeAction.run();
    }

    public void connectNodes(int index1, int index2, Integer weight) {
        nodes.get(index1).neighbours.add(new Neighbour(nodes.get(index2), weight));
        nodes.get(index2).neighbours.add(new Neighbour(nodes.get(index1), weight));
        onChangeAction.run();
    }

    public void connectNodes(Node node1, Node node2) {
        node1.neighbours.add(new Neighbour(node2));
        node2.neighbours.add(new Neighbour(node1));
        onChangeAction.run();
    }

    public void connectNodes(Node node1, Node node2, int weight) {
        node1.neighbours.add(new Neighbour(node2, weight));
        node2.neighbours.add(new Neighbour(node1, weight));
        onChangeAction.run();
    }

    public void setWeight(Node n1, Node n2, int weight) throws Exception {
        if (
                !n1.neighbours.stream().anyMatch(n -> n.node.equals(n2)) ||
                !n2.neighbours.stream().anyMatch(n -> n.node.equals(n1))
        ) {
            throw new Exception("Trying to set weight to not connected nodes");
        }
        Objects.requireNonNull(n1.neighbours.stream().filter(n -> n.node.equals(n2)).findFirst().orElse(null)).weight = weight;
        Objects.requireNonNull(n2.neighbours.stream().filter(n -> n.node.equals(n1)).findFirst().orElse(null)).weight = weight;
    }

    public void disconnect(Node n1, Node n2) {
        n1.neighbours.remove(n1.neighbours.stream().filter(n -> n.node.equals(n2)).findFirst().orElse(null));
        n2.neighbours.remove(n2.neighbours.stream().filter(n -> n.node.equals(n1)).findFirst().orElse(null));
        onChangeAction.run();
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public int size() {
        return nodes.size();
    }
}
