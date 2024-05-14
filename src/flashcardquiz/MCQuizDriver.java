package flashcardquiz;

import java.util.ArrayList;

public class MCQuizDriver {
  public static void main(String[] args) {
    Lesson myLesson = new Lesson("My Lesson");
    myLesson.addCard(new Card("1 + 2", "3"));
    myLesson.addCard(new Card("\\x -> x", "id"));
    myLesson.addCard(new Card("fix $ const 5", "5"));

    MCQuiz myMCQuiz = new MCQuiz(myLesson);
    System.out.println(myMCQuiz.getQuestion());
    for (String choice: myMCQuiz.getChoices()) {
      System.out.println(choice);
    }
    System.out.println(myMCQuiz.submitChoice(0));

    System.out.println(myMCQuiz.submitChoice(0));
    System.out.println(myMCQuiz.submitChoice(0));
    System.out.println(myMCQuiz.submitChoice(0));
    System.out.println(myMCQuiz.submitChoice(0));
    System.out.println(myMCQuiz.submitChoice(0));
    System.out.println(myMCQuiz.submitChoice(0));
    System.out.println(myMCQuiz.submitChoice(0));

    System.out.println("\nscore: " + myMCQuiz.getScore() + "/" + myMCQuiz.getTotal());
    System.out.println("you missed the following:");
    for (Card c: myMCQuiz.getMissedCards()) {
      System.out.println(c.getFront() + " -- " + c.getBack());
    }

    ArrayList<Integer> stuff = new ArrayList<>();
    stuff.add(3);
    stuff.add(1);
    stuff.add(2);
    Finder f = new Finder(stuff);
    // System.out.println(f.find(1));
  }
}
