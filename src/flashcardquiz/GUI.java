package flashcardquiz;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;

public class GUI implements ActionListener {
  public static final String STORE_PATH = "flashcardquiz.dat";

  private static final Font HEADING1 = new Font("SansSerif", Font.BOLD, 32);
  private static final Font HEADING2 = new Font("SansSerif", Font.BOLD, 24);
  private static final Font HEADING3 = new Font("SansSerif", Font.BOLD, 18);
  private static final Font HEADING4 = new Font("SansSerif", Font.BOLD, 16);
  private static final Font HEADING1_LITE = new Font("SansSerif", Font.PLAIN, 32);
  private static final Font HEADING2_LITE = new Font("SansSerif", Font.PLAIN, 24);
  private static final Font HEADING3_LITE = new Font("SansSerif", Font.PLAIN, 18);
  private static final Font HEADING4_LITE = new Font("SansSerif", Font.PLAIN, 16);

  FileStore store;
  Integer selectedLessonId;
  Integer selectedQuizId;

  JPanel panel;

  JButton saveAndQuitButton;

  JTextField lessonNameTextField;
  JTextField lessonDescriptionTextField;
  JButton createLessonButton;

  ArrayList<JButton> selectLessonButtons;

  JButton deselectLessonButton;
  JButton deleteLessonButton;

  ArrayList<JButton> deleteCardButtons;

  JTextField cardFrontTextField;
  JTextField cardBackTextField;
  JButton createCardButton;

  ArrayList<JButton> selectQuizButtons;

  JTextField mcqNumChoicesTextField;
  JButton createMCQuizButton;

  JCheckBox frqCaseSensitiveCheckBox;
  JButton createFRQuizButton;

  JButton deselectQuizButton;
  JButton deleteQuizButton;

  ArrayList<JButton> choiceButtons;

  JTextField responseTextField;
  JButton submitResponseButton;

  public GUI() {
    store = new FileStore(STORE_PATH);
    File f = new File(STORE_PATH);
    if (f.exists() && !f.isDirectory()) {
      store.load();
      System.out.println("[info] GUI loaded store");
    }

    JFrame frame = new JFrame("Flashcard Quiz");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(1280, 800);

    panel = new JPanel();
    rerender();

    frame.add(panel);
    frame.setVisible(true);
  }

  public void rerender() { // kinda wasteful but oh well
    panel.removeAll();
    panel.revalidate();
    panel.repaint();

    panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    panel.add(leftPanel());
    panel.add(centerPanel());
    if (selectedLessonId != null)
      panel.add(rightPanel());
  }

  public JPanel leftPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setPreferredSize(new Dimension(380, 800));
    panel.setMaximumSize(new Dimension(3000, 10000)); // TODO tune this
    panel.setBackground(Color.BLUE);
    panel.add(aboutPanel());
    panel.add(createLessonPanel());
    panel.add(lessonsScrollPane());
    return panel;
  }

  public JPanel rightPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setPreferredSize(new Dimension(380, 800));
    panel.setMaximumSize(new Dimension(3000, 10000)); // TODO tune this
    panel.setBackground(Color.BLUE);
    panel.add(createCardPanel());
    panel.add(createMCQuizPanel());
    panel.add(createFRQuizPanel());
    panel.add(quizzesScrollPane());
    return panel;
  }

  public JPanel centerPanel() {
    JPanel panel;
    if (selectedQuizId != null) {
      panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
      panel.setPreferredSize(new Dimension(900, 800));
      panel.setBackground(Color.LIGHT_GRAY);
      panel.add(quizButtonRowPanel());

      Quiz q = store.getQuiz(selectedQuizId);
      Boolean isFrq;
      if (q instanceof MCQuiz) {
        isFrq = false;
      } else if (q instanceof FRQuiz) {
        isFrq = true;
      } else {
        System.out.println("[warn] GUI.centerPanel unknown instance");
        isFrq = null; // eh
      }

      Lesson l = store.getLesson(selectedLessonId);
      if (q.isCompleted()) {
        JLabel titleLabel = new JLabel("Review missed questions from " + l.getName());
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setFont(HEADING1);
        JLabel subtitleLabel = new JLabel("Quiz Format: " + (isFrq ? "FRQ" : "MCQ") + "  Correct: " + q.getScore() + "/" + q.getProgress());
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitleLabel.setFont(HEADING3);
        panel.add(titleLabel);
        panel.add(subtitleLabel);
        panel.add(reviewScrollPane());
      } else {
        JLabel titleLabel = new JLabel((isFrq ? "FRQ" : "MCQ") + " of " + l.getName());
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setFont(HEADING1);
        JLabel subtitleLabel = new JLabel("Progress: " + q.getProgress() + "/" + q.getTotal() + "  Correct: " + q.getScore() + "/" + q.getProgress());
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitleLabel.setFont(HEADING3);
        panel.add(titleLabel);
        panel.add(subtitleLabel);
        if (isFrq) {
          panel.add(frqScrollPane());
        } else {
          panel.add(mcqScrollPane());
        }
      }
    } else if (selectedLessonId != null) {
      panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
      panel.setPreferredSize(new Dimension(900, 800));
      panel.setBackground(Color.GREEN);
      panel.add(lessonButtonRowPanel());

      Lesson l = store.getLesson(selectedLessonId);
      JLabel nameLabel = new JLabel(l.getName());
      nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
      nameLabel.setFont(HEADING1);
      JLabel descriptionLabel = new JLabel(l.getDescription());
      descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
      descriptionLabel.setFont(HEADING3);

      panel.add(nameLabel);
      panel.add(descriptionLabel);
      panel.add(cardsScrollPane());
    } else {
      panel = new JPanel(new BorderLayout());
      panel.setPreferredSize(new Dimension(900, 800));
      panel.setBackground(Color.GREEN);
      JLabel label = new JLabel("Please select a lesson to continue", SwingConstants.CENTER);
      label.setFont(HEADING3);
      panel.add(label);
    }
    return panel;
  }

  public JPanel aboutPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.setBackground(Color.RED);

    JLabel label1 = new JLabel("Flashcard Quiz");
    label1.setFont(HEADING1);

    JLabel label2 = new JLabel("Created for AP CSA");
    label2.setFont(HEADING3);

    saveAndQuitButton = new JButton("Save and Quit");
    saveAndQuitButton.addActionListener(this);

    panel.add(label1);
    panel.add(label2);
    panel.add(saveAndQuitButton);
    return panel;
  }

  public JPanel createLessonPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.setBackground(Color.CYAN);

    JLabel label = new JLabel("Create Lesson");
    label.setFont(HEADING2);

    JLabel nameLabel = new JLabel("Name");
    nameLabel.setFont(HEADING3);
    lessonNameTextField = new JTextField();
    lessonNameTextField.setMaximumSize(new Dimension(1000, 30));

    JLabel descriptionLabel = new JLabel("Description");
    descriptionLabel.setFont(HEADING3);
    lessonDescriptionTextField = new JTextField();
    lessonDescriptionTextField.setMaximumSize(new Dimension(1000, 30));
    createLessonButton = new JButton("Create");
    createLessonButton.addActionListener(this);

    panel.add(label);
    panel.add(nameLabel);
    panel.add(lessonNameTextField);
    panel.add(descriptionLabel);
    panel.add(lessonDescriptionTextField);
    panel.add(createLessonButton);
    return panel;
  }

  public JPanel createCardPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.setBackground(Color.CYAN);

    JLabel label = new JLabel("Add Card");
    label.setFont(HEADING2);

    JLabel frontLabel = new JLabel("Term");
    frontLabel.setFont(HEADING3);
    cardFrontTextField = new JTextField();
    cardFrontTextField.setMaximumSize(new Dimension(1000, 30));

    JLabel backLabel = new JLabel("Definition");
    backLabel.setFont(HEADING3);
    cardBackTextField = new JTextField();
    cardBackTextField.setMaximumSize(new Dimension(1000, 30));
    createCardButton = new JButton("Create");
    createCardButton.addActionListener(this);

    panel.add(label);
    panel.add(frontLabel);
    panel.add(cardFrontTextField);
    panel.add(backLabel);
    panel.add(cardBackTextField);
    panel.add(createCardButton);
    return panel;
  }

  public JPanel createMCQuizPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.setBackground(Color.CYAN);

    JLabel label = new JLabel("Practice MCQ");
    label.setFont(HEADING2);

    JLabel numChoicesLabel = new JLabel("Number of Choices");
    numChoicesLabel.setFont(HEADING3);
    mcqNumChoicesTextField = new JTextField();
    mcqNumChoicesTextField.setMaximumSize(new Dimension(300, 30));

    createMCQuizButton = new JButton("Create");
    createMCQuizButton.addActionListener(this);

    panel.add(label);
    panel.add(numChoicesLabel);
    panel.add(mcqNumChoicesTextField);
    panel.add(createMCQuizButton);
    return panel;
  }

  public JPanel createFRQuizPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.setBackground(Color.CYAN);

    JLabel label = new JLabel("Practice FRQ");
    label.setFont(HEADING2);

    frqCaseSensitiveCheckBox = new JCheckBox("Case Sensitive");

    createFRQuizButton = new JButton("Create");
    createFRQuizButton.addActionListener(this);

    panel.add(label);
    panel.add(frqCaseSensitiveCheckBox);
    panel.add(createFRQuizButton);
    return panel;
  }

  public JPanel lessonEntryPanel(int lessonId) {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.setMaximumSize(new Dimension(1000, 60));
    panel.setBackground(Color.MAGENTA);

    JButton button = new JButton("Select");
    button.addActionListener(this);
    selectLessonButtons.add(button); // hmmm

    Lesson l = store.getLesson(lessonId);
    JLabel label = new JLabel("[" + lessonId + "] " + l.getName());
    label.setFont(HEADING4);

    panel.add(button);
    panel.add(label);
    return panel;
  }

  public JPanel quizEntryPanel(int quizId) {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.setMaximumSize(new Dimension(1000, 60));
    panel.setBackground(Color.MAGENTA);

    JButton button = new JButton("Select");
    button.addActionListener(this);
    selectQuizButtons.add(button); // hmmm

    JLabel label;
    Quiz q = store.getQuiz(quizId);
    if (q instanceof MCQuiz) {
      label = new JLabel("[" + quizId + "] MCQ -- " + q.getScore() + "/" + q.getProgress() + " " + (q.isCompleted() ? "(completed)" : "(" + (q.getTotal() - q.getProgress()) + " remaining)"));
    } else if (q instanceof FRQuiz) {
      label = new JLabel("[" + quizId + "] FRQ -- " + q.getScore() + "/" + q.getProgress() + " " + (q.isCompleted() ? "(completed)" : "(" + (q.getTotal() - q.getProgress()) + " remaining)"));
    } else {
      System.out.println("[warn] GUI.quizEntryPanel unknown instance");
      label = null;
    }
    label.setFont(HEADING4);

    panel.add(button);
    panel.add(label);
    return panel;
  }

  public JScrollPane lessonsScrollPane() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.setBackground(Color.YELLOW);

    JLabel label = new JLabel("My Lessons");
    label.setFont(HEADING2);
    panel.add(label);

    selectLessonButtons = new ArrayList<>();
    for (int id: store.getLessonIds()) {
      panel.add(lessonEntryPanel(id));
    }

    JScrollPane scrollPane = new JScrollPane(panel);
    scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
    return scrollPane;
  }

  public JScrollPane quizzesScrollPane() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.setBackground(Color.YELLOW);

    JLabel label = new JLabel("Past Quizzes");
    label.setFont(HEADING2);
    panel.add(label);

    selectQuizButtons = new ArrayList<>();
    for (int id: store.getLessonQuizIds(selectedLessonId)) {
      panel.add(quizEntryPanel(id));
    }

    JScrollPane scrollPane = new JScrollPane(panel);
    scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
    return scrollPane;
  }

  public JPanel lessonButtonRowPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.setMaximumSize(new Dimension(1000, 40));
    panel.setBackground(Color.MAGENTA);

    deselectLessonButton = new JButton("Close");
    deselectLessonButton.addActionListener(this);

    deleteLessonButton = new JButton("Delete");
    deleteLessonButton.setForeground(Color.RED);
    deleteLessonButton.addActionListener(this);

    panel.add(deselectLessonButton);
    panel.add(deleteLessonButton);

    return panel;
  }

  public JPanel quizButtonRowPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.setMaximumSize(new Dimension(1000, 40));
    panel.setBackground(Color.MAGENTA);

    deselectQuizButton = new JButton("Exit");
    deselectQuizButton.addActionListener(this);

    deleteQuizButton = new JButton("Discard");
    deleteQuizButton.setForeground(Color.RED);
    deleteQuizButton.addActionListener(this);

    panel.add(deselectQuizButton);
    panel.add(deleteQuizButton);

    return panel;
  }

  public JScrollPane cardsScrollPane() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.setBackground(Color.ORANGE);

    deleteCardButtons = new ArrayList<>();
    int i = 1; // why??
    for (int id: store.getLessonCardIds(selectedLessonId)) {
      Card c = store.getCard(id);

      // JLabel label = new JLabel("[" + id + "] Card " + i); // just don't show id :)
      JLabel label = new JLabel("Card " + i);
      label.setFont(HEADING2);

      JLabel frontLabel = new JLabel(c.getFront());
      frontLabel.setFont(HEADING3);

      JLabel backLabel = new JLabel(c.getBack());
      backLabel.setFont(HEADING3_LITE);

      JButton button = new JButton("Delete");
      button.setForeground(Color.RED);
      button.addActionListener(this);
      deleteCardButtons.add(button);

      panel.add(label);
      panel.add(frontLabel);
      panel.add(backLabel);
      panel.add(button);

      i++;
    }

    JScrollPane scrollPane = new JScrollPane(panel);
    scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
    return scrollPane;
  }

  public JScrollPane mcqScrollPane() { // not actually meant to scroll but oh well
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.setBackground(Color.YELLOW);

    MCQuiz mcq = store.getMCQuiz(selectedQuizId);
    String question = mcq.getQuestion();
    JLabel label = new JLabel(question);
    label.setFont(HEADING1_LITE);
    panel.add(label);

    ArrayList<String> choices = mcq.getChoices();
    choiceButtons = new ArrayList<>();
    int i = 1;
    for (String choice: choices) {
      JLabel headerLabel = new JLabel("Choice " + i);
      headerLabel.setFont(HEADING2);

      JLabel choiceLabel = new JLabel(choice);
      choiceLabel.setFont(HEADING2_LITE);

      JButton button = new JButton("Submit");
      button.addActionListener(this);
      choiceButtons.add(button);

      panel.add(headerLabel);
      panel.add(choiceLabel);
      panel.add(button);

      i++;
    }

    JScrollPane scrollPane = new JScrollPane(panel);
    scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
    return scrollPane;
  }

  public JScrollPane frqScrollPane() { // not actually meant to scroll but oh well
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.setBackground(Color.YELLOW);

    FRQuiz frq = store.getFRQuiz(selectedQuizId);
    String question = frq.getQuestion();
    JLabel label = new JLabel(question);
    label.setFont(HEADING1_LITE);
    responseTextField = new JTextField();
    responseTextField.setMaximumSize(new Dimension(10000, 30));
    submitResponseButton = new JButton("Submit");
    submitResponseButton.addActionListener(this);

    panel.add(label);
    panel.add(responseTextField);
    panel.add(submitResponseButton);

    JScrollPane scrollPane = new JScrollPane(panel);
    scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
    return scrollPane;
  }

  public JScrollPane reviewScrollPane() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.setBackground(Color.ORANGE);

    Quiz q = store.getQuiz(selectedQuizId);
    if (q.getMissedCardIndices().isEmpty()) {
      JLabel label = new JLabel("Congratulations! There is nothing to review.");
      label.setFont(HEADING2_LITE);

      panel.add(label);
    }
    for (int i: q.getMissedCardIndices()) {
      Card c = store.getLesson(selectedLessonId).getCard(i);

      JLabel label = new JLabel("Card " + i);
      label.setFont(HEADING2);

      JLabel frontLabel = new JLabel(c.getFront());
      frontLabel.setFont(HEADING3);

      JLabel backLabel = new JLabel(c.getBack());
      backLabel.setFont(HEADING3_LITE);

      panel.add(label);
      panel.add(frontLabel);
      panel.add(backLabel);
    }

    JScrollPane scrollPane = new JScrollPane(panel);
    scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
    return scrollPane;
  }

  public void actionPerformed(ActionEvent e) {
    Object src = e.getSource();
    if (src instanceof JButton) {
      JButton button = (JButton) src;
      if (button == saveAndQuitButton) {
        System.out.println("[debug] GUI.actionPerformed save and quit");
        store.dump();
        System.out.println("[info] GUI.actionPerformed dumped store");
        System.exit(0);
      } else if (button == createLessonButton) {
        System.out.println("[debug] GUI.actionPerformed create lesson");
        String name = lessonNameTextField.getText();
        String description = lessonDescriptionTextField.getText();
        Lesson l = new Lesson(name, description);
        selectedLessonId = store.postLesson(l);
        selectedQuizId = null;
        rerender();
      } else if (button == deselectLessonButton) {
        System.out.println("[debug] GUI.actionPerformed deselect lesson");
        selectedLessonId = null;
        rerender();
      } else if (button == deleteLessonButton) {
        System.out.println("[debug] GUI.actionPerformed delete lesson");
        store.deleteLesson(selectedLessonId);
        selectedLessonId = null;
        rerender();
      } else if (button == createCardButton) {
        System.out.println("[debug] GUI.actionPerformed create card");
        String front = cardFrontTextField.getText();
        String back = cardBackTextField.getText();
        Card c = new Card(front, back);
        Lesson l = store.getLesson(selectedLessonId);
        l.addCard(c);
        // NOTE not fast but whatever
        store.replaceLesson(selectedLessonId, l);
        rerender();
      } else if (button == createMCQuizButton) {
        System.out.println("[debug] GUI.actionPerformed create mcq");
        Lesson l = store.getLesson(selectedLessonId);
        MCQuiz mcq;
        try {
          int numChoices = Integer.parseInt(mcqNumChoicesTextField.getText());
          mcq = new MCQuiz(l, numChoices);
        } catch (NumberFormatException nfe) {
          // nfe.printStackTrace();
          mcq = new MCQuiz(l);
        }
        selectedQuizId = store.postQuiz(mcq, selectedLessonId);
        rerender();
      } else if (button == createFRQuizButton) {
        System.out.println("[debug] GUI.actionPerformed create frq");
        Lesson l = store.getLesson(selectedLessonId);
        boolean caseSensitive = frqCaseSensitiveCheckBox.isSelected();
        FRQuiz frq = new FRQuiz(l, caseSensitive);
        selectedQuizId = store.postQuiz(frq, selectedLessonId);
        rerender();
      } else if (button == deselectQuizButton) {
        System.out.println("[debug] GUI.actionPerformed deselect quiz");
        selectedQuizId = null;
        rerender();
      } else if (button == deleteQuizButton) {
        System.out.println("[debug] GUI.actionPerformed delete quiz");
        store.deleteQuiz(selectedQuizId);
        selectedQuizId = null;
        rerender();
      } else if (button == submitResponseButton) {
        System.out.println("[debug] GUI.actionPerformed submit response");
        FRQuiz frq = store.getFRQuiz(selectedQuizId);
        String response = responseTextField.getText();
        frq.submitResponse(response);
        rerender();
      } else {
        if (choiceButtons != null) {
          for (int i = 0; i < choiceButtons.size(); i++) {
            if (button == choiceButtons.get(i)) {
              System.out.println("[debug] GUI.actionPerformed chose");
              MCQuiz mcq = store.getMCQuiz(selectedQuizId);
              mcq.submitChoice(i);
              rerender();
            }
          }
        }
        if (selectQuizButtons != null) {
          for (int i = 0; i < selectQuizButtons.size(); i++) {
            if (button == selectQuizButtons.get(i)) {
              System.out.println("[debug] GUI.actionPerformed select quiz");
              selectedQuizId = store.getLessonQuizIds(selectedLessonId).get(i); // assumption
              rerender();
            }
          }
        }
        if (deleteCardButtons != null) {
          for (int i = 0; i < deleteCardButtons.size(); i++) {
            if (button == deleteCardButtons.get(i)) {
              System.out.println("[debug] GUI.actionPerformed delete card");
              Lesson l = store.getLesson(selectedLessonId);
              l.removeCard(i);
              // NOTE not fast but whatever
              store.replaceLesson(selectedLessonId, l);
              rerender();
            }
          }
        }
        for (int i = 0; i < selectLessonButtons.size(); i++) {
          if (button == selectLessonButtons.get(i)) {
            System.out.println("[debug] GUI.actionPerformed select lesson");
            selectedLessonId = store.getLessonIds().get(i); // assumption
            selectedQuizId = null;
            rerender();
          }
        }
      }
    }
  }

  public static void main(String[] args) {
    new GUI();
  }
}
