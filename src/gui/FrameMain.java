package gui;

import graph.Graph;

import javax.swing.*;
import java.awt.*;

public class FrameMain extends JFrame {
    private final GraphPanel graphPanel;

    public FrameMain(int width, int height) {
        setTitle("Graph Demo");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setPreferredSize(new Dimension(width, height));
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        graphPanel = new GraphPanel();
        TextOutputPanel outputPanel = new TextOutputPanel();

        Graph<String> graph = new Graph<>(graphPanel::repaint);
        graphPanel.initGraph(graph);

        Toolbar toolbar = new Toolbar(graphPanel, outputPanel);

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        add(graphPanel, c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0;
        add(outputPanel, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 0;
        add(toolbar, c);

        pack();
        setVisible(true);
    }
}
