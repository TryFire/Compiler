package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Yuriko on 2016/12/9.
 */
public class DrawPanel extends Panel {
    private int x = 0, y = 0;
    ArrayList<Point> points;
    @Override
    public void paint(Graphics g) {
        Color color = g.getColor();
        g.setColor(Color.YELLOW);
        for (int i = 0; i < points.size(); i++) {
            g.fillOval(points.get(i).x, points.get(i).y, 2, 2);
        }
        g.setColor(color);
        System.out.println("X:" + this.x + "Y:" + this.y);
    }
    @Override
    public void update(Graphics g) {
        System.out.println("update");
    }
    public void draw(ArrayList<Point> p) {
        points = p;
        this.repaint();
        System.out.println("draw");
    }
}
