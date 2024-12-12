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
    static JPanel ItemArea;
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
        scrollPane.setSize(320, 295);
        scrollPane.setLocation(0, 50);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        ItemArea = new JPanel();
        scrollPane.setViewportView(ItemArea);
        ItemArea.setBackground(Color.BLACK);
        ItemArea.setLayout(new GridLayout(30, 1));


        add(scrollPane);
    }

    class ItemUseButton extends JButton {
        public ItemUseButton(ItemStack stack) {
            JButton newButton = new JButton();
            newButton.setSize(320,50);
            int itemCount = stack.getCount();
            newButton.setText(stack.getItem().getName());
            if(itemCount > 1) {
                newButton.setText(stack.getItem().getName() + " " + stack.getCount() + "개");
            }
            newButton.addActionListener(e -> ItemUse(stack));
            ItemArea.add(newButton);
            setVisible(true);
        }
    }

    void ItemUse(ItemStack stack) {
        stack.Use();
        UpdateData();
    }

    public void UpdateData() {
        ItemArea.removeAll();
        Player player = GameManager.getInstance().getPlayer();
        PlayerInventory playerInventory = player.getInventory();
        List<ItemStack> stacks = new ArrayList<>(playerInventory.getStacks());
        stacks.sort(Comparator.comparing(s -> s.getItem().getName()));
        for (ItemStack Item : stacks)
            new ItemUseButton(Item);
        this.repaint();
    }
}