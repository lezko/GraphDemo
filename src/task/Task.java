package task;

import graph.Graph;

import java.util.Arrays;
import java.util.List;
import java.awt.*;

public class Task {

    public static String calcShortest(Graph<String> graph) {
        int[][] dist = new int[graph.size()][graph.size()];
        for (int i = 0; i < dist.length; i++) {
            for (int j = 0; j < dist.length; j++) {
                dist[i][j] = 10000;
            }
        }
        List<Graph<String>.Node> nodes = graph.getNodes();
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.size(); j++) {
                if (i == j) {
                    dist[i][j] = 0;
                    continue;
                }
                int finalJ = j;
                Graph<String>.Neighbour neighbour = nodes.get(i).neighbours.stream().filter(n -> n.node.equals(nodes.get(finalJ))).findFirst().orElse(null);
                if (neighbour != null) {
                    dist[i][j] = neighbour.weight;
                }
            }
        }
        for (int k = 0; k < dist.length; k++) {
            for (int i = 0; i < dist.length; i++) {
                for (int j = 0; j < dist.length; j++) {
                    if (dist[i][j] > dist[i][k] + dist[k][j] && i != j) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }
        StringBuilder output = new StringBuilder();
        int resIndex = 0, sum = Integer.MAX_VALUE;
        for (int i = 0; i < dist.length; i++) {
            int tmpSum = Arrays.stream(dist[i]).reduce(0, Integer::sum);
            if (tmpSum < sum) {
                sum = tmpSum;
                resIndex = i;
            }
        }
        output
                .append("Result vertex: ")
                .append(nodes.get(resIndex).value)
                .append("\n");
        for (int i = 0; i < dist.length; i++) {
            output
                    .append(nodes.get(resIndex).value)
                    .append(" -> ")
                    .append(nodes.get(i).value)
                    .append(" = ")
                    .append(dist[resIndex][i])
                    .append("\n");
        }

        output
                .append("\n")
                .append("Path summary: ")
                .append(sum);

        return output.toString();
    }

    public static void paintTwoColors(Graph<String> graph) throws Exception {
        try {
            for (Graph<String>.Node node: graph.getNodes()) {
                if (node.getColor() != Color.red && node.getColor() != Color.green) {
                    node.setColor(Color.red);
                    paintDfs(node);
                }
            }
        } catch (Exception e) {
            graph.getNodes().forEach(Graph.Node::resetColor);
            throw e;
        }
    }

    private static void paintDfs(Graph<String>.Node node) throws Exception {
        for (Graph<String>.Neighbour neighbourObj: node.neighbours) {
            Graph<String>.Node neighbour = neighbourObj.node;
            if (neighbour.getColor() != Color.red && neighbour.getColor() != Color.green) {
                if (node.getColor() == Color.red) {
                    neighbour.setColor(Color.green);
                } else {
                    neighbour.setColor(Color.red);
                }
                paintDfs(neighbour);
            } else if (node.getColor() == neighbour.getColor()) {
                throw new Exception("Incorrect graph");
            }
        }
    }
}
