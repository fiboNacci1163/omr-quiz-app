package ui;

import static ui.SwingHelper.*;

import javax.swing.*;
import java.awt.*;

public class FirstWindow {
    public static void show() {
        JFrame frame = new JFrame("OMR Based Quiz App");

        JPanel panel = new JPanel(new GridBagLayout());
        frame.add(panel);

        JButton createButton = new JButton("Create Quiz");
        scaleFont(createButton, 2);
        createButton.addActionListener(e -> CreateQuizWindow.show());

        JButton takeQuiz = new JButton("Take Quiz");
        scaleFont(takeQuiz, 2);
        takeQuiz.addActionListener(e -> {
            TakeQuizWindow.show();
        });


        JButton scanOMR = new JButton("Scan OMR");
        scaleFont(scanOMR, 2);
        scanOMR.addActionListener(e -> ScanOMRWindow.show());

        JButton viewResult = new JButton("View Result");
        scaleFont(viewResult, 2);
        viewResult.addActionListener(e -> ResultHistoryWindow.show());

        GBC gbc = new GBC().pad(32).insets(32).fill(GBC.HORIZONTAL);
        panel.add(createButton, gbc.x(0).y(0));
        panel.add(takeQuiz, gbc.x(1).y(0));
        panel.add(scanOMR, gbc.x(0).y(1));
        panel.add(viewResult, gbc.x(1).y(1));

        packScaleWindow(frame, 1.25, 1.5);
        centerShowWindow(frame);
    }
}
