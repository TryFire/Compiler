package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Yuriko on 2016/12/9.
 */
public class DrawFrame extends JFrame {
    private static final int screenX = 1000;
    private static final int screanY = 800;
    public boolean isInited = false;
    DrawPanel panel;
    public boolean isLunched = false;
    public void draw(ArrayList<Point> p) {
        if (!isInited) {
            isInited = true;
        }
        panel.draw(p);
        System.out.println("draw");
    }
    public void lunchFrame() {

        setBounds(400, 150, screenX, screanY);
        setResizable(false);
        setVisible(true);
        panel = new DrawPanel();
        panel.setBackground(new Color(57, 57, 57));
        add(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        isLunched = true;
    }
}
