package gui;

import graph.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class GraphPanel extends JPanel implements MouseListener {

    private Graph<String> graph;
    private final int NODE_SIZE = 50;

    private final List<NodeComponent> connectNodes = new LinkedList<>();

    public GraphPanel() {
        setLayout(null);
        addMouseListener(this);
    }

    public void initGraph(Graph<String> graph) {
        this.graph = graph;
    }

    @Override
    public void paint(Graphics graphics) {
        if (graph == null) {
            return;
        }

        Graphics2D g = (Graphics2D) graphics;
        super.paint(g);
        List<NodeComponent> nodeComponents = new LinkedList<>();
        for (Component comp : getComponents()) {
            if (comp instanceof NodeComponent) {
                nodeComponents.add((NodeComponent) comp);
            }
        }

        for (NodeComponent nodeComponent : nodeComponents) {
            for (Graph<String>.Neighbour neighbour : nodeComponent.node.neighbours) {
                NodeComponent neighbourComponent = nodeComponents.stream().filter(comp -> comp.node.equals(neighbour.node)).findFirst().orElse(null);
                if (neighbourComponent == null) {
                    continue;
                }
                drawEdge(g, nodeComponent, neighbourComponent, neighbour.weight);
            }
        }
    }

    public void performConnection(NodeComponent node) {
        if (connectNodes.size() > 1) {
            System.err.println("Connection queue overflow");
            return;
        }
        connectNodes.add(node);
        if (connectNodes.size() == 2) {
            NodeComponent nc1 = connectNodes.get(0);
            NodeComponent nc2 = connectNodes.get(1);
            if (
                    nc1.node.neighbours.stream().anyMatch(n -> n.node.equals(nc2.node))
                            || nc2.node.neighbours.stream().anyMatch(n -> n.node.equals(nc1.node))

            ) {
                graph.disconnect(nc1.node, nc2.node);
            } else {
                JTextField input = new JTextField();
                double[] edge = EdgeCalculator.calc(
                        nc1.getX() + NODE_SIZE / 2.0,
                        nc1.getY() + NODE_SIZE / 2.0,
                        nc2.getX() + NODE_SIZE / 2.0,
                        nc2.getY() + NODE_SIZE / 2.0,
                        NODE_SIZE / 2.0
                );
                input.setBounds((int) (edge[0] + edge[2]) / 2, (int) (edge[1] + edge[3]) / 2, 30, 20);
                add(input);
                input.requestFocus();
                input.addKeyListener(new KeyListener() {
                    @Override
                    public void keyPressed(KeyEvent keyEvent) {
                        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
                            graph.disconnect(nc1.node, nc2.node);
                            remove(input);
                            return;
                        }
                        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                            String weight = input.getText();
                            if (weight.length() > 0) {
                                try {
                                    graph.setWeight(nc1.node, nc2.node, Integer.parseInt(weight));
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            remove(input);
                            repaint();
                        }
                    }

                    @Override
                    public void keyTyped(KeyEvent keyEvent) {
                    }

                    @Override
                    public void keyReleased(KeyEvent keyEvent) {
                    }
                });
                graph.connectNodes(nc1.node, nc2.node);
            }
            resetNodes();
        }
    }

    public void resetNodes() {
        for (Component c : getComponents()) {
            if (c instanceof NodeComponent) {
                ((NodeComponent) c).reset(true);
            }
        }
        connectNodes.clear();
    }

    public Graph<String> getGraph() {
        return graph;
    }

    public void graphFromString(String string) {
        removeAll();
        Graph<String> newGraph = new Graph<>(this::repaint);
        Scanner scanner = new Scanner(string);
        boolean readingEdges = false;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.length() == 0) {
                readingEdges = true;
                continue;
            }

            String[] lineArr = line.split(" ");

            if (readingEdges) {
                newGraph.connectNodes(Integer.parseInt(lineArr[0]), Integer.parseInt(lineArr[1]), lineArr[2].equals("null") ? null : Integer.parseInt(lineArr[2]));
            } else {
                Graph<String>.Node newNode = newGraph.addNode(lineArr[2]);
                addNodeComponent(newNode, Integer.parseInt(lineArr[0]), Integer.parseInt(lineArr[1]));
            }
        }

        initGraph(newGraph);
        resetNodes();
        repaint();
    }

    public String graphToString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Component comp : getComponents()) {
            if (comp instanceof NodeComponent) {
                NodeComponent nc = (NodeComponent) comp;
                nc.node.visited = false;
                stringBuilder.append(nc.getX()).append(" ").append(nc.getY()).append(" ").append(nc.node.value).append('\n');
            }
        }
        stringBuilder.append("\n");

        List<Graph<String>.Node> nodes = graph.getNodes();
        for (int i = 0; i < nodes.size() - 1; i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                Graph<String>.Node n1 = nodes.get(i);
                Graph<String>.Node n2 = nodes.get(j);
                Graph<String>.Neighbour neighbour = n1.neighbours.stream().filter(n -> n.node.equals(n2)).findFirst().orElse(null);
                if (neighbour != null) {
                    stringBuilder
                            .append(nodes.indexOf(n1))
                            .append(" ")
                            .append(nodes.indexOf(n2))
                            .append(" ")
                            .append(neighbour.weight == null ? "" : neighbour.weight)
                            .append("\n");
                }
            }
        }

        return stringBuilder.toString();
    }

    private void addNodeComponent(Graph<String>.Node node, int x, int y) {
        add(new NodeComponent(this, node, NODE_SIZE, x, y));
    }

    private void drawEdge(Graphics2D g, NodeComponent nc1, NodeComponent nc2, Integer weight) {
        double[] edge = EdgeCalculator.calc(
                nc1.getX() + NODE_SIZE / 2.0,
                nc1.getY() + NODE_SIZE / 2.0,
                nc2.getX() + NODE_SIZE / 2.0,
                nc2.getY() + NODE_SIZE / 2.0,
                NODE_SIZE / 2.0
        );
        g.setStroke(new BasicStroke(2));
        g.setColor(new Color(0, 0, 0, 10));
        g.drawLine((int) edge[0], (int) edge[1], (int) edge[2], (int) edge[3]);
        if (weight != null) {
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.setColor(Color.gray);
            g.drawString(String.valueOf(weight), (int) (edge[0] + edge[2]) / 2, (int) (edge[1] + edge[3]) / 2);
            g.setColor(new Color(0, 0, 0, 10));
        }
    }

    public void clear() {
        removeAll();
        repaint();
    }

    // Mouse events
    @Override
    public void mousePressed(MouseEvent e) {
        Component c = getComponentAt(e.getPoint());
        if (c != this) {
            return;
        }

        if (!connectNodes.isEmpty()) {
            connectNodes.get(0).reset(false);
            connectNodes.clear();
            repaint();
            return;
        }

        if (SwingUtilities.isRightMouseButton(e)) {
            Graph<String>.Node newNode = graph.addNode("");
            addNodeComponent(newNode, e.getX() - NODE_SIZE / 2, e.getY() - NODE_SIZE / 2);
            repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }
}
