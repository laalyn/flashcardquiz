package flashcardquiz;

import java.util.ArrayList;

class MCQuiz extends Quiz {
  private int numChoices;
  private ArrayList<Integer> choices;

  public MCQuiz(Lesson lesson, ArrayList<Integer> order, int progress, ArrayList<Integer> missed, int numChoices, ArrayList<Integer> choices) {
    super(lesson, order, progress, missed);
    this.numChoices = numChoices;
    this.choices = choices;
  }

  public MCQuiz(Lesson lesson) {
    this(lesson, 4);
  }

  public MCQuiz(Lesson lesson, int numChoices) {
    super(lesson);
    this.numChoices = numChoices;
    generateChoices();
  }

  public ArrayList<String> getChoices() {
    ArrayList<String> choices = new ArrayList<>();
    for (int i: this.choices) {
      choices.add(lesson.getCard(i).getBack());
    }
    return choices;
  }

  public void generateChoices() {
    choices = new ArrayList<>();
    if (!isCompleted()) {
      int cur = getCurrentIndex();
      ArrayList<Integer> indices = new ArrayList<>();
      for (int i = 0; i < getTotal(); i++) {
        if (i != cur) {
          indices.add(i);
        }
      }

      int correctChoice = (int) (Math.random() * numChoices);
      for (int i = 0; i < numChoices; i++) {
        if (i == correctChoice) {
          choices.add(cur);
        } else if (!indices.isEmpty()) {
          int j = (int) (Math.random() * indices.size());
          choices.add(indices.get(j));
          indices.remove(j);
        }
      }
    }
  }

  public Boolean submitChoice(int choice) {
    if (!isCompleted() && 0 <= choice && choice < numChoices) {
      if (choices.get(choice) == getCurrentIndex()) {
        submitCorrect();
        generateChoices();
        return true;
      } else {
        submitIncorrect();
        generateChoices();
        return false;
      }
    } else return null;
  }

  public String dump() {
    CabinetWriter cw = new CabinetWriter();
    cw.putInt(numChoices);
    cw.putIntArray(choices);
    return dump(cw);
  }

  public static MCQuiz load(String repr) {
    CabinetReader cr = new CabinetReader(repr);
    int numChoices = cr.getInt();
    ArrayList<Integer> choices = cr.getIntArray();
    // MCQuiz mcq = (MCQuiz) load(cr);
    // mcq.numChoices = numChoices;
    // mcq.choices = choices;
    ArrayList<Integer> order = cr.getIntArray();
    int progress = cr.getInt();
    ArrayList<Integer> missed = cr.getIntArray();
    return new MCQuiz(null, order, progress, missed, numChoices, choices);
  }
}
