package flashcardquiz;

import java.util.ArrayList;

class FRQuiz extends Quiz {
  private boolean caseSensitive;

  public FRQuiz(Lesson lesson) {
    this(lesson, false);
  }

  public FRQuiz(Lesson lesson, boolean caseSensitive) {
    super(lesson);
    this.caseSensitive = caseSensitive;
  }

  public Boolean submitResponse(String response) {
    if (!isCompleted()) {
      String answer = getCurrentCard().getBack();
      if (caseSensitive ? response.trim().equals(answer) : response.trim().equalsIgnoreCase(answer)) {
        submitCorrect();
        return true;
      } else {
        submitIncorrect();
        return false;
      }
    } else return null;
  }
}
