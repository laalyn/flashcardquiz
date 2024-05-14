package flashcardquiz;

import java.util.ArrayList;

class Store {
  private ArrayList<Card> cards;
  private ArrayList<Integer> cardIds;
  private int nextCardId;

  private ArrayList<Lesson> lessons;
  private ArrayList<Integer> lessonIds;
  private int nextLessonId;
  private ArrayList<ArrayList<Integer>> lessonCardIds;

  public Store() {
    cards = new ArrayList<>();
    cardIds = new ArrayList<>();
    nextCardId = 0;

    lessons = new ArrayList<>();
    lessonIds = new ArrayList<>();
    nextLessonId = 0;
    lessonCardIds = new ArrayList<>();
  }

  public int postCard(Card card) {
    cards.add(card);
    cardIds.add(nextCardId);
    return nextCardId++;
  }

  public boolean replaceCard(int cardId, Card card) {
    int i = findCard(cardId);
    if (i >= 0) {
      cards.set(i, card);
      return true;
    }
    return false;
  }

  private int findCard(int cardId) {
    Finder f = new Finder(cardIds);
    return f.find(cardId);
  }

  public Card getCard(int cardId) {
    int i = findCard(cardId);
    if (i >= 0)
      return cards.get(i);
    else
      return null;
  }

  public boolean deleteCard(int cardId) {
    int i = findCard(cardId);
    if (i >= 0) {
      cards.remove(i);
      cardIds.remove(i);
      return true;
    }
    return false;
  }

  // TODO add quizzes here

  private ArrayList<Integer> postCardsFromLesson(Lesson lesson, ArrayList<Integer> existingCardIds) {
    ArrayList<Integer> cardIds = new ArrayList<>();
    for (int i = 0; i < lesson.countCards(); i++) {
      Card c = lesson.getCard(i);
      if (i < existingCardIds.size()) {
        int cardId = existingCardIds.get(i);
        if (getCard(cardId) == c) { // yes, we want to use == instead of .equals()
          System.out.println("[debug] Store.postCardsFromLesson matched existing card with id " + cardId);
          cardIds.add(cardId);
          continue;
        }
        System.out.println("[warn] Store.postCardsFromLesson did not match existing card with id " + existingCardIds.get(i));
      }
      cardIds.add(postCard(c));
    }
    return cardIds;
  }

  public int postLesson(Lesson lesson) {
    return postLesson(lesson, new ArrayList<>());
  }

  public int postLesson(Lesson lesson, ArrayList<Integer> existingCardIds) {
    lessons.add(lesson);
    lessonIds.add(nextLessonId);
    ArrayList<Integer> cardIds = postCardsFromLesson(lesson, existingCardIds);
    lessonCardIds.add(cardIds);
    return nextLessonId++;
  }

  public boolean replaceLesson(int lessonId, Lesson lesson) {
    return replaceLesson(lessonId, lesson, new ArrayList<>(), true);
  }

  public boolean replaceLesson(int lessonId, Lesson lesson, ArrayList<Integer> existingCardIds) {
    return replaceLesson(lessonId, lesson, existingCardIds, true);
  }

  public boolean replaceLesson(int lessonId, Lesson lesson, ArrayList<Integer> existingCardIds, boolean cascade) {
    int i = findLesson(lessonId);
    if (i >= 0) {
      if (cascade) deleteCardsFromLesson(lessonId);
      lessons.set(i, lesson);
      lessonCardIds.set(i, postCardsFromLesson(lesson, existingCardIds));
      return true;
    }
    return false;
  }

  private int findLesson(int lessonId) {
    Finder f = new Finder(lessonIds);
    return f.find(lessonId);
  }

  public Lesson getLesson(int lessonId) {
    int i = findLesson(lessonId);
    if (i >= 0) {
      Lesson ref = lessons.get(i);
      Lesson proj = new Lesson(ref.getName(), ref.getDescription());
      for (int id: lessonCardIds.get(i)) {
        System.out.println("[debug] Store.getLesson retrieved existing card with id " + id);
        proj.addCard(getCard(id));
      }
      return proj;
    } else return null;
  }

  private boolean deleteCardsFromLesson(int lessonId) {
    int i = findLesson(lessonId);
    if (i >= 0) {
      ArrayList<Integer> cardIds = lessonCardIds.get(i);
      for (int id: cardIds) {
        System.out.println("[debug] Store.deleteCardsFromLesson cascade delete card with id " + id);
        deleteCard(id);
      }
      return true;
    }
    return false;
  }

  public boolean deleteLesson(int lessonId) {
    return deleteLesson(lessonId, true);
  }

  public boolean deleteLesson(int lessonId, boolean cascade) {
    int i = findLesson(lessonId);
    if (i >= 0) {
      if (cascade) deleteCardsFromLesson(lessonId);
      lessons.remove(i);
      lessonIds.remove(i);
      lessonCardIds.remove(i);
      return true;
    }
    return false;
  }
}
