package gui;

import javax.swing.*;
import java.awt.*;

public class TextOutputPanel extends JPanel {
    private final int DEFAULT_WIDTH = 200;
    private final JTextArea outputArea = new JTextArea();

    public TextOutputPanel() {
        setPreferredSize(new Dimension(DEFAULT_WIDTH, getHeight()));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        outputArea.setLineWrap(true);
        outputArea.setBackground(null);
        outputArea.setEditable(false);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
        add(outputArea);
    }

    public JTextArea getOutputArea() {
        return outputArea;
    }
}
