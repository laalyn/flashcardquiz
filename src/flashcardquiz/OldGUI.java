package flashcardquiz;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class OldGUI implements ActionListener {
  private static final String PATH = "flashcardquiz.dat";
  private static final Font HEAVY = new Font("SansSerif", Font.BOLD, 18);
  // private static final Font BOLD = new Font("SansSerif", Font.BOLD, 14);
  // private static final Font NORMAL = new Font("SansSerif", Font.PLAIN, 14);

  FileStore store;
  Integer selectedLessonId;

  JButton saveAndExitButton;

  JTextField lessonNameTextField;
  JTextField lessonDescriptionTextField;
  JButton createLessonButton;

  ArrayList<JButton> selectLessonButtons;

  public OldGUI() {
    store = new FileStore(PATH);
    File f = new File(PATH);
    if (f.exists() && !f.isDirectory()) {
      store.load();
      System.out.println("[info] OldGUI loaded store");
    }
    selectedLessonId = null;

    JFrame frame = new JFrame("Flashcard Quiz");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400, 300);

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    JPanel saveAndExitPanel = saveAndExitPanel();
    saveAndExitPanel.setMaximumSize(new Dimension(300, 30));

    JPanel createLessonPanel = createLessonPanel();
    createLessonPanel.setMaximumSize(new Dimension(300, 150));

    JPanel lessonsPanel = lessonsPanel();
    // lessonsPanel.setMaximumSize(new Dimension(300, 300));
    JScrollPane scrollPane = new JScrollPane(lessonsPanel);
    scrollPane.setMaximumSize(new Dimension(300, 300));

    panel.add(saveAndExitPanel);
    panel.add(createLessonPanel);
    panel.add(scrollPane);

    frame.add(panel);
    frame.setVisible(true);
  }

  private JPanel saveAndExitPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(1, 0));

    saveAndExitButton = new JButton("Save and Exit");
    saveAndExitButton.addActionListener(this);

    panel.add(saveAndExitButton);
    return panel;
  }

  private JPanel createLessonPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(0, 1));

    JLabel label = new JLabel("Create New Lesson");
    label.setFont(HEAVY);
    JLabel nameLabel = new JLabel("Name");
    lessonNameTextField = new JTextField();
    JLabel descriptionLabel = new JLabel("Description");
    lessonDescriptionTextField = new JTextField();
    createLessonButton = new JButton("Create");
    createLessonButton.addActionListener(this);

    panel.add(label);
    panel.add(nameLabel);
    panel.add(lessonNameTextField);
    panel.add(descriptionLabel);
    panel.add(lessonDescriptionTextField);
    panel.add(createLessonButton);
    panel.setBackground(Color.GREEN);
    return panel;
  }

  private JPanel lessonPanel(int lessonId) {
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(0, 1));

    Lesson l = store.getLesson(lessonId);

    return panel;
  }

  private JPanel lessonsPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(0, 1));

    JLabel label = new JLabel("My Lessons");
    label.setFont(HEAVY);
    panel.add(label);

    selectLessonButtons = new ArrayList<>();
    for (int id: store.getLessonIds()) {
      Lesson l = store.getLesson(id);
      JButton button = new JButton(l.getName());
      selectLessonButtons.add(button);
      panel.add(button);
    }

    return panel;
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == saveAndExitButton) {
      System.out.println("[debug] OldGUI.actionPerformed save and exit");
      store.dump();
      System.out.println("[info] OldGUI.actionPerformed dumped store");
      System.exit(0);
    } else if (e.getSource() == createLessonButton) {
      System.out.println("[debug] OldGUI.actionPerformed create lesson");
      String name = lessonNameTextField.getText();
      String description = lessonDescriptionTextField.getText();
      Lesson l = new Lesson(name, description);
      store.postLesson(l);
    }
  }

  public static void main(String[] args) {
    new OldGUI();
  }
}
