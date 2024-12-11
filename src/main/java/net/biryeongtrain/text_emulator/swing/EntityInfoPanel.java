package net.biryeongtrain.text_emulator.swing;

import net.biryeongtrain.text_emulator.Main;
import net.biryeongtrain.text_emulator.entity.*;
import net.biryeongtrain.text_emulator.registry.Registries;
import net.biryeongtrain.text_emulator.utils.identifier.Identifier;
import net.biryeongtrain.text_emulator.level.scene.ActionType;

import javax.swing.*;
import java.awt.*;;

public class EntityInfoPanel extends JPanel {
    JPanel DataLabels;
    JLabel NameDataLabel = new JLabel("unknown");
    JLabel HealthDataLabel = new JLabel("0");
    JLabel DamageDataLabel = new JLabel("0");
    JLabel ArmorDataLabel = new JLabel("0");
    JLabel panelNameLabel;
    public EntityInfoPanel() {
        setBackground(Color.BLACK);
        setLayout(null);

        DataLabels = new JPanel();
        DataLabels.setLayout(null);
        DataLabels.setSize(320, 360);
        DataLabels.setBackground(Color.BLACK);
        DataLabels.setLocation(0, 40);
        add(DataLabels);

        // 패널 제목 표시
        panelNameLabel = new JLabel("Enemy Info");
        panelNameLabel.setFont(new Font("Serif", Font.BOLD, 30));
        panelNameLabel.setForeground(Color.WHITE);
        panelNameLabel.setSize(200, 40);
        panelNameLabel.setLocation(10, 0);
        add(panelNameLabel);

        // 데이터 제목 표시
        JLabel NameLabel = new JLabel("NAME:");
        NameLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        NameLabel.setForeground(Color.WHITE);
        NameLabel.setSize(100, 40);
        NameLabel.setLocation(10, 10);
        DataLabels.add(NameLabel);

        JLabel HealthLabel = new JLabel("HEALTH:");
        HealthLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        HealthLabel.setForeground(Color.WHITE);
        HealthLabel.setSize(100, 40);
        HealthLabel.setLocation(10, 50);
        DataLabels.add(HealthLabel);

        JLabel DamageLabel = new JLabel("DAMAGE:");
        DamageLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        DamageLabel.setForeground(Color.WHITE);
        DamageLabel.setSize(100, 40);
        DamageLabel.setLocation(10, 90);
        DataLabels.add(DamageLabel);

        JLabel ArmorLabel = new JLabel("ARMOR:");
        ArmorLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        ArmorLabel.setForeground(Color.WHITE);
        ArmorLabel.setSize(100, 40);
        ArmorLabel.setLocation(10, 130);
        DataLabels.add(ArmorLabel);

        // 데이터 값
        NameDataLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        NameDataLabel.setForeground(Color.WHITE);
        NameDataLabel.setSize(190, 40);
        NameDataLabel.setLocation(110, 10);
        DataLabels.add(NameDataLabel);

        HealthDataLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        HealthDataLabel.setForeground(Color.WHITE);
        HealthDataLabel.setSize(190, 40);
        HealthDataLabel.setLocation(110, 50);
        DataLabels.add(HealthDataLabel);

        DamageDataLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        DamageDataLabel.setForeground(Color.WHITE);
        DamageDataLabel.setSize(190, 40);
        DamageDataLabel.setLocation(110, 90);
        DataLabels.add(DamageDataLabel);

        ArmorDataLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        ArmorDataLabel.setForeground(Color.WHITE);
        ArmorDataLabel.setSize(190, 40);
        ArmorDataLabel.setLocation(110, 130);
        DataLabels.add(ArmorDataLabel);
    }

    public void UpdateDate() {
        DataLabels.setVisible(false);
        panelNameLabel.setText("No Enemy");
    }

    public void UpdateData(Entity entity) {
        DataLabels.setVisible(true);
        panelNameLabel.setText("Enemy Info");
        NameDataLabel.setText(entity.getType().getKey().getPath());
        HealthDataLabel.setText(Math.round(entity.getHealth()) + "");
        DamageDataLabel.setText(Math.round(entity.getDamage()) + "");
        ArmorDataLabel.setText(Math.round(entity.getArmor()) + "");
    }
}
