package flashcardquiz;

import java.util.ArrayList;

public class StoreDriver {
  public static void main(String[] args) {
    Store s = new Store();
    System.out.println(s.postCard(new Card("0", "guys")));
    System.out.println(s.postCard(new Card("1", "guy")));
    System.out.println(s.postCard(new Card("2", "guys")));
    System.out.println(s.getCard(0).getFront());
    System.out.println(s.getCard(1).getFront());
    System.out.println(s.getCard(2).getFront());
    s.deleteCard(0);
    s.deleteCard(2);
    System.out.println(s.postCard(new Card("2+", "guys")));
    // System.out.println(s.getCard(0).getFront());
    System.out.println(s.getCard(1).getFront());
    // System.out.println(s.getCard(2).getFront());
    System.out.println(s.getCard(3).getFront());
    System.out.println(s.postCard(new Card("3", "guys")));

    Lesson l = new Lesson();
    l.addCard(s.getCard(1));
    l.addCard(s.getCard(3));
    Card c1 = l.getCard(0);
    int lessonId = s.postLesson(l);
    s.deleteLesson(lessonId);
    lessonId = s.postLesson(l);
    System.out.println("lesson id = " + lessonId);
    Lesson lp = s.getLesson(lessonId);
    Card c2 = lp.getCard(0);
    ArrayList<Integer> cardIds = new ArrayList<>();
    cardIds.add(1);
    cardIds.add(3);
    lessonId = s.postLesson(l, cardIds);
    System.out.println("lesson id = " + lessonId);
    lp = s.getLesson(lessonId);
    Card c3 = lp.getCard(0);
    System.out.println("c1: " + c1);
    System.out.println("c2: " + c2);
    System.out.println("c3: " + c3);
    System.out.println("c1 == c2 = " + (c1 == c2));
    System.out.println("c1 == c3 = " + (c1 == c3));
    System.out.println("c2 == c3 = " + (c2 == c3));
    // above will all be true because its still the same object
  }
}
