package ui;

import model.OMRIO;
import model.Question;
import model.Quiz;
import ui.SwingHelper.GBC;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static model.OMRIO.createOMR;
import static ui.SwingHelper.centerShowWindow;
import static ui.SwingHelper.packScaleWindow;

public class TakeQuizWindow {
    private static JPanel questionPanel;
    private static int[] selectedChoices;
    private static JLabel omrImageLabel;
    private static JTextField studentIdField;

    private static JFrame frame;
    private static Quiz quiz;
    private static BufferedImage omrImage;

    public static void show() {
        quiz = new Quiz();
        quiz.loadQuiz();

        frame = new JFrame("Take Quiz");

        createStudentIdInputAndSubmitButton();
        createQuestionPanel(quiz);

        omrImageLabel = new JLabel();
        omrImageLabel.setVerticalAlignment(JLabel.NORTH);

        JPanel omrPanel = new JPanel(new GridBagLayout());
        GBC gbc = new GBC().x(0).y(GBC.RELATIVE).weightx(1);
        omrPanel.add(new JLabel("Generate OMR Image"), gbc);
        omrPanel.add(omrImageLabel, gbc);
        omrPanel.add(new JPanel(), gbc.weighty(1));
        frame.add(omrPanel, BorderLayout.EAST);

        generateAndDisplayOMR();

        packScaleWindow(frame, 1.25, 1);
        centerShowWindow(frame);
    }

    private static void createStudentIdInputAndSubmitButton() {
        JPanel studentPanel = new JPanel();
        studentPanel.add(new JLabel("Student ID:"));
        studentIdField = new JTextField(20);
        studentPanel.add(studentIdField);
        frame.add(studentPanel, BorderLayout.NORTH);

                JButton submitButton = new JButton("Submit");
        submitButton.setEnabled(false);

        studentIdField.addActionListener(e -> {
            boolean idFilled = !studentIdField.getText().isEmpty();
            submitButton.setEnabled(idFilled);
            generateAndDisplayOMR();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        submitButton.addActionListener(e -> {
            quiz.gradeQuiz(selectedChoices);
            String filename = quiz.getTitle() + " - " + studentIdField.getText();
            OMRIO.saveImage(omrImage, filename);
        });

    }


    private static void createQuestionPanel(Quiz quiz) {
        selectedChoices = new int[quiz.getQuestions().size()];

        questionPanel = new JPanel(new GridBagLayout());
        JScrollPane scrollPane = new JScrollPane(questionPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);

        frame.add(scrollPane, BorderLayout.CENTER);

        scrollPane.setPreferredSize(new Dimension(320, 480));

        loadQuestions(quiz.getQuestions());
    }

    private static void loadQuestions(List<Question> questions) {
        ButtonGroup[] buttonGroups = new ButtonGroup[questions.size()];
        GBC gbc = new GBC().x(0).y(GBC.RELATIVE).weightx(1).anchor(GBC.WEST).insets(8);

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            JLabel questionLabel = new JLabel((i + 1) + ". " + question.getQuestion());
            questionPanel.add(questionLabel, gbc);

            ButtonGroup group = new ButtonGroup();
            buttonGroups[i] = group;
            JPanel optionsPanel = new JPanel(new GridLayout(2, 2));

            String[] options = question.getOptions();
            for (int j = 0; j < options.length; j++) {
                JRadioButton optionButton = new JRadioButton(options[j]);
                int selectedOption = j + 1;
                int finalI = i;
                optionButton.addActionListener(e -> {
                    selectedChoices[finalI] = selectedOption;
                    generateAndDisplayOMR();
                });
                group.add(optionButton);
                optionsPanel.add(optionButton);
            }
            questionPanel.add(optionsPanel, gbc);
        }

        questionPanel.add(new JPanel(), gbc.weighty(1));
        questionPanel.revalidate();
        questionPanel.repaint();
    }


    private static void generateAndDisplayOMR() {
        String omrLabel = "Quiz Title: " + quiz.getTitle() + "        Student ID: " + studentIdField.getText();
        omrImage = createOMR(omrLabel, selectedChoices);

        ImageIcon omrIcon = new ImageIcon(omrImage);
        omrImageLabel.setIcon(omrIcon);
        frame.revalidate();          frame.repaint();         }

    public static void main(String[] args) {
        show();
    }

}
