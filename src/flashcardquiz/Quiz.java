package flashcardquiz;

import java.util.ArrayList;

class Quiz {
  protected Lesson lesson;
  private ArrayList<Integer> order;
  private int progress;
  private ArrayList<Integer> missed;

  // NOTE for internal use
  // but you don't even need this because you can access private Quiz in Quiz
  public Quiz(Lesson lesson, ArrayList<Integer> order, int progress, ArrayList<Integer> missed) {
    this.lesson = lesson;
    this.order = order;
    this.progress = progress;
    this.missed = missed;
  }

  public Quiz(Lesson lesson) {
    setLesson(lesson);
    ArrayList<Integer> indices = new ArrayList<>();
    for (int i = 0; i < lesson.countCards(); i++) {
      indices.add(i);
    }
    order = new ArrayList<>();
    while (!indices.isEmpty()) {
      int i = (int) (Math.random() * indices.size());
      order.add(indices.get(i));
      indices.remove(i);
    }
    progress = 0;
    missed = new ArrayList<>();
  }

  // NOTE be careful
  public void setLesson(Lesson lesson) {
    this.lesson = lesson;
  }

  public ArrayList<Integer> getOrder() {
    return order;
  }

  public Lesson getLesson() {
    return lesson;
  }

  public int getProgress() {
    return progress;
  }

  public int getTotal() {
    return lesson.countCards();
  }

  public boolean isCompleted() {
    return getProgress() >= getTotal();
  }

  protected int getCurrentIndex() {
    if (isCompleted())
      return -1;
    else
      return order.get(progress);
  }

  protected Card getCurrentCard() {
    if (isCompleted())
      return null;
    else
      return lesson.getCard(order.get(progress));
  }

  public String getQuestion() {
    if (isCompleted())
      return null;
    else
      return getCurrentCard().getFront();
  }

  protected void submitCorrect() {
    if (!isCompleted()) {
      progress++;
    }
  }

  protected void submitIncorrect() {
    if (!isCompleted()) {
      missed.add(order.get(progress));
      progress++;
    }
  }

  public int getScore() {
    return progress - missed.size();
  }

  public ArrayList<Integer> getMissedCardIndices() {
    return missed;
  }

  public ArrayList<Card> getMissedCards() {
    ArrayList<Card> cards = new ArrayList<>();
    for (int i: missed) {
      cards.add(lesson.getCard(i));
    }
    return cards;
  }

  public String dump(CabinetWriter cw) {
    // NOTE ignoring lesson
    cw.putIntArray(order);
    cw.putInt(progress);
    cw.putIntArray(missed);
    return cw.getOutput();
  }

  // public static Quiz load(CabinetReader cr) {
  //   ArrayList<Integer> order = cr.getIntArray();
  //   int progress = cr.getInt();
  //   ArrayList<Integer> missed = cr.getIntArray();
  //   return new Quiz(null, order, progress, missed);
  // }
}
