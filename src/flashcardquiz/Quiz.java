package flashcardquiz;

import java.util.ArrayList;

class Quiz {
  protected Lesson lesson;
  private ArrayList<Integer> order;
  private int progress;
  private ArrayList<Integer> missed;

  public Quiz(Lesson lesson) {
    this.lesson = lesson;
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

  public ArrayList<Card> getMissedCards() {
    ArrayList<Card> cards = new ArrayList<>();
    for (int i: missed) {
      cards.add(lesson.getCard(i));
    }
    return cards;
  }
}
