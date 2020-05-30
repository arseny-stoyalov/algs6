package com.company.gui;

import com.company.gui.drawables.NodeView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Optional;

public class AppView {

    private final DisplayComponent display = new DisplayComponent();

    private final JFrame main = new JFrame("Path Searching");

    private final JTextArea txtWeight = new JTextArea();

    private final JLabel txtRes = new JLabel();

    public void createView() {

        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setMinimumSize(new Dimension(200, 200));

        MouseListener onPressed = new DisplayMouseListener();
        JButton btnUndo = new JButton("Undo");
        txtWeight.setColumns(5);
        txtWeight.setText("5");
        display.addMouseListener(onPressed);
        btnUndo.addActionListener(e -> {
                    display.removeLastDrawn();
                    txtRes.setText("");
                    display.repaint();
                }
        );

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(new JLabel("Enter weight here:"));
        bottom.add(txtWeight);
        bottom.add(new JLabel("Shortest Path:"));
        bottom.add(txtRes);

        main.add(display, BorderLayout.CENTER);
        main.add(bottom, BorderLayout.NORTH);
        main.add(btnUndo, BorderLayout.SOUTH);

        main.pack();
        main.setLocationRelativeTo(null);
        main.setVisible(true);
    }

    private class DisplayMouseListener implements MouseListener {

        Point start;

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            start = e.getPoint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Point end = e.getPoint();
            if (start.equals(end)) {
                Optional<NodeView> probableNode = display.occupiedBy(end);
                if (probableNode.isPresent()) {
                    String pathLength = display.selectNode(probableNode.get());
                    txtRes.setText(pathLength == null ? "" : pathLength);
                }
                display.addNode(end, display.DEFAULT_NODE_COLOR);
                if (probableNode.isPresent())
                    display.repaint();
            } else {
                int weight = -1;
                try {
                    weight = Integer.parseInt(txtWeight.getText());
                    if (weight <= 0) throw new NumberFormatException();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(main,
                            "Please a valid integer weight value"
                            , "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                if (weight > 0)
                    display.addEdge(start, end, Integer.parseInt(txtWeight.getText()));
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

    }

}
