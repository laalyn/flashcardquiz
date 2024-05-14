package flashcardquiz;

public class QuizDriver {
  public static void main(String[] args) {
    Lesson myLesson = new Lesson("My Lesson");
    myLesson.addCard(new Card("1 + 2", "3"));
    myLesson.addCard(new Card("\\x -> x", "id"));
    myLesson.addCard(new Card("fix $ const 5", "5"));
    Quiz myQuiz = new Quiz(myLesson);
    System.out.println(myQuiz.getCurrentCard().getFront());
    System.out.println("correct!");
    myQuiz.submitCorrect();
    System.out.println(myQuiz.getCurrentCard().getFront());
    myQuiz.submitIncorrect();
    System.out.println("wrong!");
    System.out.println(myQuiz.getCurrentCard().getFront());
    myQuiz.submitCorrect();
    System.out.println("correct!");
    System.out.println("\nscore: " + myQuiz.getScore() + "/" + myQuiz.getTotal());
    System.out.println("you missed the following:");
    for (Card c: myQuiz.getMissedCards()) {
      System.out.println(c.getFront() + " -- " + c.getBack());
    }
  }
}
