package net.biryeongtrain.text_emulator.swing;

import javax.swing.*;
import java.awt.*;

public class SimpleTextRPG extends JFrame {
    Dimension centerSize = new Dimension(640, 680);

    public SimpleTextRPG() {
        // 창 설정
        setTitle("Simple Text RPG");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        Container c = getContentPane();

        //텍스트 패널 위치 및 크기 설정
        TextAreaPanel textAreaPanel = new TextAreaPanel();
        textAreaPanel.setSize(centerSize);
        textAreaPanel.setLocation((1280 - centerSize.width) / 2,0);
        c.add(textAreaPanel);

        // 창 크기 설정 및 표시
        setSize(1280, 720);
        setVisible(true);
    }
}


