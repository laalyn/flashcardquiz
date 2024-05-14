package flashcardquiz;

import java.util.ArrayList;

class Lesson {
  private String name;
  private String description;
  private ArrayList<Card> cards;

  public Lesson() {
    this("", "");
  }

  public Lesson(String name) {
    this(name, "");
  }

  public Lesson(String name, String description) {
    this.name = name;
    this.description = description;
    cards = new ArrayList<>();
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public int countCards() {
    return cards.size();
  }

  public Card getCard(int index) {
    return cards.get(index);
  }

  public void addCard(Card card) {
    cards.add(card);
  }

  public void removeCard(int index) {
    cards.remove(index);
  }
}
