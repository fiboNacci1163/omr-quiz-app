package model;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class Quiz implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;

    private String title;
    private List<Question> questions;

    public Quiz() {
        title = "";
        questions = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public void removeQuestion(int index) {
        questions.remove(index);
    }

    public void removeQuestion(Question question) {
        questions.remove(question);
    }

    public List<Question> getQuestions() {
        return questions;
    }


    public void saveQuiz() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("db/quiz.ser"))) {
            out.writeObject(this);
            JOptionPane.showMessageDialog(null, "Quiz saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error saving quiz: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadQuiz() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("db/quiz.ser"))) {
            Quiz loadedQuiz = (Quiz) in.readObject();
            title = loadedQuiz.getTitle();
            questions = loadedQuiz.getQuestions();
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Error loading quiz: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public int gradeQuiz(int[] selectedChoices) {
        int score = 0;

        // Compare selected choices with correct answers
        for (int i = 0; i < selectedChoices.length; i++) {
            out.println(questions.get(i).getAnswer());
            if (selectedChoices[i] == questions.get(i).getAnswer()) {
                score++;
            }
        }

        // Display the score

        return score;
    }

}
