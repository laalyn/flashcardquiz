package flashcardquiz;

import java.util.ArrayList;

public class StoreDriver {
  public static void main(String[] args) {
    // Store s = new Store();
    // System.out.println(s.postCard(new Card("0", "guys")));
    // System.out.println(s.postCard(new Card("1", "guy")));
    // System.out.println(s.postCard(new Card("2", "guys")));
    // System.out.println(s.getCard(0).getFront());
    // System.out.println(s.getCard(1).getFront());
    // System.out.println(s.getCard(2).getFront());
    // s.deleteCard(0);
    // s.deleteCard(2);
    // System.out.println(s.postCard(new Card("2+", "guys")));
    // // System.out.println(s.getCard(0).getFront());
    // System.out.println(s.getCard(1).getFront());
    // // System.out.println(s.getCard(2).getFront());
    // System.out.println(s.getCard(3).getFront());
    // System.out.println(s.postCard(new Card("3", "guys")));

    // Lesson l = new Lesson();
    // l.addCard(s.getCard(1));
    // l.addCard(s.getCard(3));
    // Card c1 = l.getCard(0);
    // int lessonId = s.postLesson(l);
    // s.deleteLesson(lessonId);
    // lessonId = s.postLesson(l);
    // System.out.println("lesson id = " + lessonId);
    // Lesson lp = s.getLesson(lessonId);
    // Card c2 = lp.getCard(0);
    // ArrayList<Integer> cardIds = new ArrayList<>();
    // cardIds.add(1);
    // cardIds.add(3);
    // lessonId = s.postLesson(l, cardIds);
    // System.out.println("lesson id = " + lessonId);
    // lp = s.getLesson(lessonId);
    // Card c3 = lp.getCard(0);
    // System.out.println("c1: " + c1);
    // System.out.println("c2: " + c2);
    // System.out.println("c3: " + c3);
    // System.out.println("c1 == c2 = " + (c1 == c2));
    // System.out.println("c1 == c3 = " + (c1 == c3));
    // System.out.println("c2 == c3 = " + (c2 == c3));
    // // above will all be true because its still the same object

    Store s = new Store();
    System.out.println("created a store");

    Lesson l = new Lesson("a lesson");
    String lRepr = l.dump();
    l = Lesson.load(lRepr);
    Card fc = new Card("1 + 2", "3");
    String fcRepr = fc.dump();
    fc = Card.load(fcRepr);
    l.addCard(fc);
    l.addCard(new Card("\\x -> x", "id"));
    l.addCard(new Card("fix $ const 5", "5"));
    System.out.println("created a lesson, created three cards, added three cards to lesson");
    System.out.println("first card has representation:\n" + fcRepr);

    int lessonId = s.postLesson(l);
    System.out.println("posted lesson and cards");

    l = s.getLesson(lessonId);

    MCQuiz mcq = new MCQuiz(l);
    String mcqRepr = mcq.dump();
    mcq = MCQuiz.load(mcqRepr);
    mcq.setLesson(l);
    System.out.println("created quiz (mcq) from lesson");

    int quizId = s.postQuiz(mcq, lessonId);
    System.out.println("posted quiz (mcq) using existing lesson id " + lessonId);

    mcq = s.getMCQuiz(quizId);

    System.out.println("question: " + mcq.getQuestion());
    for (String choice: mcq.getChoices()) {
      System.out.println("\tchoice: " + choice);
    }
    System.out.println("\tsubmission: 0; " + mcq.submitChoice(0));

    System.out.println("question: " + mcq.getQuestion());
    for (String choice: mcq.getChoices()) {
      System.out.println("\tchoice: " + choice);
    }
    System.out.println("\tsubmission: 0; " + mcq.submitChoice(0));

    System.out.println("question: " + mcq.getQuestion());
    for (String choice: mcq.getChoices()) {
      System.out.println("\tchoice: " + choice);
    }
    System.out.println("\tsubmission: 0; " + mcq.submitChoice(0));

    System.out.println("question: " + mcq.getQuestion());
    for (String choice: mcq.getChoices()) {
      System.out.println("\tchoice: " + choice);
    }
    System.out.println("\tsubmission: 0; " + mcq.submitChoice(0));

    System.out.println("\nscore: " + mcq.getScore() + "/" + mcq.getTotal());
    System.out.println("you missed the following:");
    for (Card c: mcq.getMissedCards()) {
      System.out.println(c.getFront() + " -- " + c.getBack());
    }
  }
}
