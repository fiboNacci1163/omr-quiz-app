package ui;

import model.Question;
import model.Quiz;
import ui.SwingHelper.GBC;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static ui.SwingHelper.centerShowWindow;

public class CreateQuizWindow {
    private static Quiz quiz;
    private static JPanel mainPanel;
    private static JPanel listPanel;


    public static void show() {

        quiz = new Quiz();
        quiz.loadQuiz();

        JFrame frame = new JFrame("Create Quiz");
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(16, 16, 16, 16));
        frame.add(mainPanel);

        createQuestionEditor();

        mainPanel.add(
                new JSeparator(SwingConstants.VERTICAL),
                new GBC().x(GBC.RELATIVE).y(GBC.RELATIVE).height(GBC.REMAINDER)
                        .fill(GBC.VERTICAL).insets(0, 16, 0, 16));

        createQuestionList();


        frame.pack();
        centerShowWindow(frame);
    }

    private static void createQuestionEditor() {

        JLabel quizTitleLabel = new JLabel("Quiz Title");
        JLabel questionDetailsLabel = new JLabel("Question Details");
        JLabel questionTextLabel = new JLabel("Question Text");
        JLabel answerOptionsLabel = new JLabel("Answer Options");
        JLabel correctAnswerLabel = new JLabel("Correct Answer");

        GBC gbc = new GBC().x(0).y(GBC.RELATIVE).anchor(GBC.EAST).insets(0, 0, 0, 16);
        mainPanel.add(quizTitleLabel, gbc);
        mainPanel.add(questionDetailsLabel, gbc.pady(64));
        gbc.pady(0);
        mainPanel.add(questionTextLabel, gbc);
        mainPanel.add(answerOptionsLabel, gbc);
        mainPanel.add(correctAnswerLabel, gbc);

        JTextField quizTitleField = new JTextField(quiz.getTitle(), 20);
        JSeparator questionDetailsSeperator = new JSeparator();
        JTextField questionTextField = new JTextField();
        JTextField answerOptionsField = new JTextField();
        JTextField correctAnswerField = new JTextField();

        gbc = new GBC().x(1).y(GBC.RELATIVE).width(2).weightx(0.1).fill(GBC.HORIZONTAL);
        mainPanel.add(quizTitleField, gbc);
        mainPanel.add(questionDetailsSeperator, gbc);
        mainPanel.add(questionTextField, gbc);
        mainPanel.add(answerOptionsField, gbc);
        mainPanel.add(correctAnswerField, gbc);

        mainPanel.add(new JPanel(), new GBC().x(1).y(5).weightx(0.1));

        JButton addQuestionButton = new JButton("Add Question");
        JButton saveQuizButton = new JButton("Save Quiz");

        gbc = new GBC().x(2).fill(GBC.HORIZONTAL).pad(16);
        mainPanel.add(addQuestionButton, gbc.y(6).insets(16, 0, 16, 0));
        mainPanel.add(saveQuizButton, gbc.y(7).insets(0));

        mainPanel.add(new JPanel(), new GBC().y(8).weighty(1));

        addQuestionButton.addActionListener(e -> {
            String quizTitle = quizTitleField.getText().trim();
            String questionText = questionTextField.getText().trim();
            String answerOptions = answerOptionsField.getText().trim();
            String correctAnswer = correctAnswerField.getText().trim();

            if (quizTitle.isEmpty() || questionText.isEmpty() || answerOptions.isEmpty() || correctAnswer.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Please fill in all fields.", "Incomplete Data", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String[] optionsArray = answerOptions.split("\\s*,\\s*");
            if (optionsArray.length != 4) {
                JOptionPane.showMessageDialog(mainPanel, "Please enter exactly 4 options, separated by commas.", "Invalid Format", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int answerIndex;
            try {
                answerIndex = Integer.parseInt(correctAnswer);
                if (answerIndex < 1 || answerIndex > 4) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainPanel, "Please enter a valid answer index (1-4).", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            quiz.setTitle(quizTitle);
            quiz.addQuestion(new Question(questionText, optionsArray, answerIndex));
            updateListPanel();

            questionTextField.setText("");
            answerOptionsField.setText("");
            correctAnswerField.setText("");
        });
        saveQuizButton.addActionListener(e -> quiz.saveQuiz());
    }

    private static void createQuestionList() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(640, 0));

        mainPanel.add(scrollPane,
                new GBC().x(GBC.RELATIVE).y(GBC.RELATIVE).height(GBC.REMAINDER)
                        .weightx(1).weighty(1).fill(GBC.BOTH));

        listPanel = new JPanel(new GridBagLayout());
        listPanel.setPreferredSize(new Dimension(320, 0));
        listPanel.setBorder(new EmptyBorder(16, 16, 16, 16));

        scrollPane.setViewportView(listPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);

        updateListPanel();
    }

    private static void updateListPanel() {
        listPanel.removeAll();

        GBC gbc = new GBC().x(0).y(GBC.RELATIVE).weightx(1).fill(GBC.HORIZONTAL).insets(8);
        listPanel.add(new JLabel("Added questions"), gbc);
        listPanel.add(new JSeparator(), gbc);

        int i = 0;
        for (Question question : quiz.getQuestions()) {
            JPanel questionPanel = new JPanel(new GridBagLayout());
            JLabel questionLabel = new JLabel(++i + ". " + question.getQuestion());

            JButton removeButton = new JButton("✖");
            removeButton.addActionListener(e -> {
                quiz.removeQuestion(question);
                updateListPanel();
            });

            questionPanel.add(questionLabel, new GBC().weightx(1).anchor(GBC.WEST));
            questionPanel.add(removeButton);
            listPanel.add(questionPanel, gbc);
        }

        listPanel.add(Box.createVerticalGlue(), gbc.weighty(1));
        listPanel.revalidate();
        listPanel.repaint();
    }

    public static void main(String[] args) {
        show();
    }
}
