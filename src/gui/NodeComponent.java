package gui;

import graph.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NodeComponent extends JComponent implements MouseListener, MouseMotionListener {
    private int size;
    private int x;
    private int y;

    protected Graph<String>.Node node;
    protected GraphPanel container;

    private Color borderColor = Color.black;
    private int borderThickness = 3;

    private enum NodeState {
        SETUP,
        CONNECTING,
        DEFAULT
    }
    private NodeState state;

//    public NodeComponent(GraphPanel container, Graph.Node node, int size, int x, int y, int value) {
//        this.container = container;
//        this.node = node;
//        this.size = size;
//        this.x = x;
//        this.y = y;
//        this.value = value;
//
//        setLayout(new BorderLayout());
//        setBounds(x, y, size, size);
//
//        addMouseListener(this);
//        addMouseMotionListener(this);
//
//        state = NodeState.DEFAULT;
//        initState();
//    }

    public NodeComponent(GraphPanel container, Graph<String>.Node node, int size, int x, int y) {
        this.container = container;
        this.node = node;
        this.size = size;

        setPosition(x, y);

        setPreferredSize(new Dimension(size, size));
        setLayout(new BorderLayout());

        addMouseListener(this);
        addMouseMotionListener(this);

        state = NodeState.SETUP;
        initState();
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        setBounds(x, y, size, size);
    }

    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        super.paint(g);
        g.setStroke(new BasicStroke(borderThickness));
        g.setColor(borderColor);
        g.drawOval(0, 0, size, size);
        g.setColor(new Color(node.getColor().getRed(), node.getColor().getGreen(), node.getColor().getBlue(), 150));
        g.fillOval(0, 0, size, size);
    }

    private void initState() {
        removeAll();

        switch (state) {
            case SETUP:
                JTextField textField = new JTextField();
                textField.setFont(new Font("Arial", Font.BOLD, 8));
                textField.setHorizontalAlignment(JTextField.CENTER);
                setLayout(null);
                textField.setBounds(size / 5, size / 4, size / 5 * 3, size / 2);

                NodeComponent currentNode = this;
                textField.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent focusEvent) {}

                    @Override
                    public void focusLost(FocusEvent focusEvent) {
                        if (state == NodeState.SETUP) {
                            container.remove(currentNode);
                            node.remove();
                        }
                    }
                });
                textField.addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent keyEvent) {}

                    @Override
                    public void keyPressed(KeyEvent keyEvent) {
                        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
                            container.remove(currentNode);
                            node.remove();
                            return;
                        }
                        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                            node.value = textField.getText();
                            state = NodeState.DEFAULT;
                            initState();
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent keyEvent) {}
                });

                add(textField);
                SwingUtilities.invokeLater(textField::requestFocus);

                break;

            case CONNECTING:
                setLayout(new BorderLayout());
                JLabel label = new JLabel(String.valueOf(node.value));
                label.setHorizontalAlignment(JLabel.CENTER);
                add(label);
                borderThickness = 4;
                repaint();
                container.performConnection(this);
                break;

            case DEFAULT:
                setLayout(new BorderLayout());
                label = new JLabel(String.valueOf(node.value));
                label.setHorizontalAlignment(JLabel.CENTER);
                add(label);
                borderThickness = 2;
                repaint();
                break;
        }

        revalidate();
        repaint();
    }

    public void reset(boolean resetColor) {
        state = NodeState.DEFAULT;
        if (resetColor) {
            node.resetColor();
        }
        initState();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (SwingUtilities.isLeftMouseButton(mouseEvent) && state == NodeState.DEFAULT) {
            state = NodeState.CONNECTING;
            initState();
        }
        if (SwingUtilities.isRightMouseButton(mouseEvent) && state == NodeState.DEFAULT) {
            container.remove(this);
            node.remove();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        setPosition(x + e.getX() - size / 2, y + e.getY() - size / 2);
        container.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {}

    @Override
    public void mousePressed(MouseEvent mouseEvent) {}

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {}

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}
}
