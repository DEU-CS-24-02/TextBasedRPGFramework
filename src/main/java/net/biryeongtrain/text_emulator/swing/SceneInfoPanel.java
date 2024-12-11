package net.biryeongtrain.text_emulator.swing;

import net.biryeongtrain.text_emulator.GameManager;
import net.biryeongtrain.text_emulator.level.Scene;

import javax.swing.*;
import java.awt.*;

public class SceneInfoPanel extends JPanel {
    JLabel SceneNameDataLabel = new JLabel("");
    JLabel SceneWeightDataLabel = new JLabel("");
    public SceneInfoPanel() {
        this.setBackground(Color.BLACK);
        setLayout(null);

        // 패널 제목 표시
        JLabel panelNameLabel = new JLabel("Scene Info");
        panelNameLabel.setFont(new Font("Serif", Font.BOLD, 30));
        panelNameLabel.setForeground(Color.WHITE);
        panelNameLabel.setSize(200, 40);
        panelNameLabel.setLocation(10, 0);
        add(panelNameLabel);


        // 데이터 제목 표시
        JLabel SceneNameLabel = new JLabel("NAME:");
        SceneNameLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        SceneNameLabel.setForeground(Color.WHITE);
        SceneNameLabel.setSize(100, 40);
        SceneNameLabel.setLocation(10, 50);
        add(SceneNameLabel);

        JLabel SceneWeightLabel = new JLabel("WEIGHT:");
        SceneWeightLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        SceneWeightLabel.setForeground(Color.WHITE);
        SceneWeightLabel.setSize(100, 40);
        SceneWeightLabel.setLocation(10, 90);
        add(SceneWeightLabel);

        SceneNameDataLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        SceneNameDataLabel.setForeground(Color.WHITE);
        SceneNameDataLabel.setSize(190, 40);
        SceneNameDataLabel.setLocation(110, 50);
        add(SceneNameDataLabel);

        SceneWeightDataLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        SceneWeightDataLabel.setForeground(Color.WHITE);
        SceneWeightDataLabel.setSize(190, 40);
        SceneWeightDataLabel.setLocation(110, 90);
        add(SceneWeightDataLabel);
    }

    public void UpdateData() {
        GameManager manager = GameManager.getInstance();
        SceneNameDataLabel.setText(manager.getCurrentScene().id().getPath());
        SceneWeightDataLabel.setText(manager.getCurrentScene().selector().weight() + "");
    }
}
