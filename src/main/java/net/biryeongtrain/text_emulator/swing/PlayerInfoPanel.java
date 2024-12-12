package net.biryeongtrain.text_emulator.swing;

import java.awt.*;
import javax.swing.*;

import net.biryeongtrain.text_emulator.GameManager;
import net.biryeongtrain.text_emulator.entity.Player;
import net.biryeongtrain.text_emulator.entity.PlayerInventory;

public class PlayerInfoPanel extends JPanel {
    JLabel playerHealthDataLabel = new JLabel("0");
    JLabel playerKarmaDataLabel = new JLabel("0");
    JLabel playerGoldDataLabel = new JLabel("0");
    JLabel playerDamageDataLabel = new JLabel("0");
    JLabel playerArmorDataLabel = new JLabel("0");

    public PlayerInfoPanel() {
        this.setBackground(Color.BLACK);
        setLayout(null);

        // 패널 제목 표시
        JLabel panelNameLabel = new JLabel("Player Info");
        panelNameLabel.setFont(new Font("Serif", Font.BOLD, 30));
        panelNameLabel.setForeground(Color.WHITE);
        panelNameLabel.setSize(200, 40);
        panelNameLabel.setLocation(10, 0);
        add(panelNameLabel);

        // 데이터 제목 표시
        JLabel playerHealthLabel = new JLabel("HEALTH:");
        playerHealthLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        playerHealthLabel.setForeground(Color.WHITE);
        playerHealthLabel.setSize(100, 40);
        playerHealthLabel.setLocation(10, 50);
        add(playerHealthLabel);

        JLabel playerKarmaLabel = new JLabel("KARMA:");
        playerKarmaLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        playerKarmaLabel.setForeground(Color.WHITE);
        playerKarmaLabel.setSize(100, 40);
        playerKarmaLabel.setLocation(10, 90);
        add(playerKarmaLabel);

        JLabel playerGoldLabel = new JLabel("GOLD:");
        playerGoldLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        playerGoldLabel.setForeground(Color.WHITE);
        playerGoldLabel.setSize(100, 40);
        playerGoldLabel.setLocation(10, 130);
        add(playerGoldLabel);

        JLabel playerDamageLabel = new JLabel("DAMAGE:");
        playerDamageLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        playerDamageLabel.setForeground(Color.WHITE);
        playerDamageLabel.setSize(100, 40);
        playerDamageLabel.setLocation(10, 170);
        add(playerDamageLabel);

        JLabel playerArmorLabel = new JLabel("ARMOR:");
        playerArmorLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        playerArmorLabel.setForeground(Color.WHITE);
        playerArmorLabel.setSize(100, 40);
        playerArmorLabel.setLocation(10, 210);
        add(playerArmorLabel);

        // 데이터 값
        playerHealthDataLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        playerHealthDataLabel.setForeground(Color.WHITE);
        playerHealthDataLabel.setSize(190, 40);
        playerHealthDataLabel.setLocation(110, 50);
        add(playerHealthDataLabel);

        playerKarmaDataLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        playerKarmaDataLabel.setForeground(Color.WHITE);
        playerKarmaDataLabel.setSize(190, 40);
        playerKarmaDataLabel.setLocation(110, 90);
        add(playerKarmaDataLabel);

        playerGoldDataLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        playerGoldDataLabel.setForeground(Color.WHITE);
        playerGoldDataLabel.setSize(190, 40);
        playerGoldDataLabel.setLocation(110, 130);
        add(playerGoldDataLabel);

        playerDamageDataLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        playerDamageDataLabel.setForeground(Color.WHITE);
        playerDamageDataLabel.setSize(190, 40);
        playerDamageDataLabel.setLocation(110, 170);
        add(playerDamageDataLabel);

        playerArmorDataLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        playerArmorDataLabel.setForeground(Color.WHITE);
        playerArmorDataLabel.setSize(190, 40);
        playerArmorDataLabel.setLocation(110, 210);
        add(playerArmorDataLabel);

    }

    //플레이어 정보를 라벨에 업데이트하는 메소드
    public void UpdateData() {
        Player player = GameManager.getInstance().getPlayer();
        PlayerInventory playerInventory = player.getInventory();
        playerHealthDataLabel.setText(Math.round(player.getHealth()) + "");
        playerKarmaDataLabel.setText(playerInventory.getGold() + "");
        playerGoldDataLabel.setText(playerInventory.getReputation() + "");
        playerDamageDataLabel.setText(Math.round(player.getDamage()) + "");
        playerArmorDataLabel.setText(Math.round(player.getArmor()) + "");
    }
}
