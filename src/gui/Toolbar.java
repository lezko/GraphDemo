package gui;

import config.Config;
import task.Task;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Toolbar extends JPanel {

    public Toolbar(GraphPanel graphPanel, TextOutputPanel outputPanel) {
        setPreferredSize(new Dimension(getWidth(), 100));
        setBorder(BorderFactory.createLineBorder(Color.black, 2));
        setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton buttonSolve = new JButton("solve");
        buttonSolve.addActionListener(e -> {
            try {
                task.Task.paintTwoColors(graphPanel.getGraph());
                graphPanel.repaint();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        buttonSolve.setPreferredSize(new Dimension(150, 40));
        add(buttonSolve);

        JButton buttonReset = new JButton("Reset");
        buttonReset.setPreferredSize(new Dimension(150, 40));
        buttonReset.addActionListener(e -> graphPanel.resetNodes());
        add(buttonReset);

        JButton buttonCalcShortest = new JButton("Calc Shortest");
        buttonCalcShortest.setPreferredSize(new Dimension(150, 40));
        buttonCalcShortest.addActionListener(e -> outputPanel.getOutputArea().setText(Task.calcShortest(graphPanel.getGraph())));
        add(buttonCalcShortest);

        // Open and save
        JFileChooser openFileChooser = new JFileChooser();
        JFileChooser saveFileChooser = new JFileChooser();
        openFileChooser.setCurrentDirectory(new File(Config.currentDir));
        saveFileChooser.setCurrentDirectory(new File(Config.currentDir));

        JButton buttonGraphToString = new JButton("To String");
        buttonGraphToString.setPreferredSize(new Dimension(150, 40));
        buttonGraphToString.addActionListener(e -> {
            if (saveFileChooser.showOpenDialog(graphPanel) == JFileChooser.APPROVE_OPTION) {
                String path = saveFileChooser.getSelectedFile().getPath();
                try {
                    PrintWriter out = new PrintWriter(path);
                    out.println(graphPanel.graphToString());
                    out.close();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        add(buttonGraphToString);

        JButton buttonGraphFromString = new JButton("From String");
        buttonGraphFromString.setPreferredSize(new Dimension(150, 40));
        buttonGraphFromString.addActionListener(e -> {
            if (openFileChooser.showOpenDialog(graphPanel) == JFileChooser.APPROVE_OPTION) {
                String path = openFileChooser.getSelectedFile().getPath();
                try {
                    graphPanel.graphFromString(Files.readString(Path.of(path)));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        add(buttonGraphFromString);

        // Clear
        JButton buttonClear = new JButton("Clear");
        buttonClear.setPreferredSize(new Dimension(150, 40));
        buttonClear.setForeground(Color.red);
        buttonClear.addActionListener(e -> {
            graphPanel.clear();
            outputPanel.getOutputArea().setText("");
        });
        add(buttonClear);
    }
}
