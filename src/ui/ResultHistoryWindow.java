package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

import static java.lang.System.out;
import static ui.SwingHelper.*;

public class ResultHistoryWindow {

    public static void show() {
        JFrame frame = new JFrame("Result History");

        JScrollPane scrollPane = new JScrollPane(createFileListPanel("result"));
        frame.add(scrollPane, BorderLayout.NORTH);

        scrollPane.setBorder(new EmptyBorder(16, 16, 16, 16));

        packScaleWindow(frame, 1.25, 1.5);
        centerShowWindow(frame);
    }

    private static Component createFileListPanel(String directoryPath) {
        File directory = new File(directoryPath);
        String[] files = directory.list((dir, name) -> new File(dir, name).isFile());

        if (files == null) {
            return new JLabel("No files found");
        }

        JPanel listPanel = new JPanel(new GridLayout(0, 3));
        listPanel.add(new JLabel("Quiz Name"));
        listPanel.add(new JLabel("Student ID"));
        listPanel.add(new JLabel(""));

        for (String s : files) {
            out.println();
            String[] ss = s.substring(0, s.length()-4).split(" - ");
            String quizName = ss[0];
            String studentID = ss[1];

            listPanel.add(new JLabel(quizName));
            listPanel.add(new JLabel(studentID));
            JButton viewButton = new JButton("View");
            listPanel.add(viewButton);

            viewButton.addActionListener(e -> ScanOMRWindow.showResult("result/"+s));
        }

        return listPanel;

    }
}
