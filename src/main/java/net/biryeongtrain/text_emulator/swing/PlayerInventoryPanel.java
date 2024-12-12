package net.biryeongtrain.text_emulator.swing;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.biryeongtrain.text_emulator.GameManager;
import net.biryeongtrain.text_emulator.entity.Player;
import net.biryeongtrain.text_emulator.entity.PlayerInventory;
import net.biryeongtrain.text_emulator.item.ItemStack;

public class PlayerInventoryPanel extends JPanel {
    static JScrollPane scrollPane;
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

        scrollPane = new JScrollPane();
        scrollPane.getViewport().setBackground(Color.BLACK);
        scrollPane.setSize(320, 360);
        scrollPane.setLocation(0, 50);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane);
    }

    static class ItemUseButton extends JButton {
        public ItemUseButton(ItemStack stack) {
            JButton newButton = new JButton();
            newButton.setText(stack.getItem().getName() + " " + stack.getCount() + "개");
            newButton.addActionListener(e -> stack.Use());
            scrollPane.add(newButton);
            setVisible(true);
        }
    }

    public void UpdateData() {
        Player player = GameManager.getInstance().getPlayer();
        PlayerInventory playerInventory = player.getInventory();
        List<ItemStack> stacks = new ArrayList<>(playerInventory.getStacks());
        stacks.sort(Comparator.comparing(s -> s.getItem().getName()));
        for (ItemStack Item : stacks)
            new ItemUseButton(Item);
    }
}