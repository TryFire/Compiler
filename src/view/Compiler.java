package view;

import grammerAna.ENode;
import grammerAna.Parser;
import meanAna.Compter;
import tool.ReadTool;
import wordAna.Token;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * Created by Yuriko on 2016/12/8.
 */
public class Compiler extends JFrame {
    private static final int screenX = 1000;
    private static final int screanY = 800;
    private JMenuBar menubar;
    private JMenu menuFile;
    private JMenu menuRun;


    JTextPane codePane;

    ArrayList<Point> points;

    public Compiler() {
    }



    public void lunchFrame() {
        setBounds(400, 150, screenX, screanY);
        setResizable(false);
        setVisible(true);
        setLayout(null);
        setTitle("绘图语言编译器");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });


        codePane = new JTextPane();
        codePane.setSize(screenX, screanY);
        codePane.setLocation(0, 0);
        codePane.setFont(new Font("Consolas", Font.BOLD, 16));
        codePane.setBackground(new Color(57, 57, 57));
        codePane.setForeground(new Color(200, 200, 200));
        codePane.setSelectedTextColor(new Color(200, 200, 200));
        codePane.setSelectionColor(new Color(45, 88, 147));
        codePane.setCaretColor(new Color(220, 220, 220));
        codePane.setMargin(new Insets(20, 20, 20, 20));
        //codePane.setMinimumSize(new Dimension((screenX / 2), 200));

        menubar = new JMenuBar();
        menuRun = new JMenu("Run");
        JMenuItem itemRun = new JMenuItem("Run");
        menuRun.add(itemRun);
        menubar.add(menuRun);
        this.setJMenuBar(menubar);

        itemRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ArrayList<Token> tokens;
                wordAna.Scanner scanner = new wordAna.Scanner();
                tokens = scanner.run(codePane.getText());
        /*System.out.printf("%10s%10s%10s%10s\n","类别", "原始输入", "值", "函数地址");
        for (int i = 0; i < tokens.size(); i++) {
            tokens.get(i).display();
        }
        System.out.print("total lines:" + scanner.getLines());*/
                Parser parser = new Parser();
                parser.run(tokens, new Compter());
            }
        });

        add(codePane);
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //isLunched = true;

    }

}
