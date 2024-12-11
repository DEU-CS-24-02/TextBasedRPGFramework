package net.biryeongtrain.text_emulator.swing;

import javax.swing.*;
import java.awt.*;

public class PlayerInventoryPanel extends JPanel {
    public PlayerInventoryPanel() {
        setBackground(Color.BLACK);
        setLayout(null);

        // 패널 제목 표시
        JLabel panelNameLabel = new JLabel("Inventory");
        panelNameLabel.setFont(new Font("Serif", Font.BOLD, 30));
        panelNameLabel.setForeground(Color.WHITE);
        panelNameLabel.setSize(200, 40);
        panelNameLabel.setLocation(10, 0);
        add(panelNameLabel);

//        JScrollPane scrollPane = new JScrollPane();
//        scrollPane.setSize(320, 360);
//        scrollPane.setLocation(0, 50);
//        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
//        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//        add(scrollPane);
    }
}