package flashcardquiz;

import java.util.ArrayList;

public class FRQuizDriver {
  public static void main(String[] args) {
    Lesson myLesson = new Lesson("My Lesson");
    myLesson.addCard(new Card("1 + 2", "3"));
    myLesson.addCard(new Card("\\x -> x", "id"));
    myLesson.addCard(new Card("fix $ const 5", "5"));

    FRQuiz myFRQuiz = new FRQuiz(myLesson);
    String frqRepr = myFRQuiz.dump();
    System.out.println(frqRepr);
    myFRQuiz = FRQuiz.load(frqRepr);
    myFRQuiz.setLesson(myLesson);
    System.out.println(myFRQuiz.getQuestion());
    System.out.println(myFRQuiz.submitResponse("3"));
    System.out.println(myFRQuiz.getQuestion());
    System.out.println(myFRQuiz.submitResponse("id"));
    System.out.println(myFRQuiz.getQuestion());
    System.out.println(myFRQuiz.submitResponse("5"));
    System.out.println(myFRQuiz.submitResponse("5"));
    System.out.println(myFRQuiz.submitResponse("5"));
    System.out.println("\nscore: " + myFRQuiz.getScore() + "/" + myFRQuiz.getTotal());
    System.out.println("you missed the following:");
    for (Card c: myFRQuiz.getMissedCards()) {
      System.out.println(c.getFront() + " -- " + c.getBack());
    }
  }
}
