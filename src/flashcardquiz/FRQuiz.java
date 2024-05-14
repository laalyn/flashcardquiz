package flashcardquiz;

import java.util.ArrayList;

class FRQuiz extends Quiz {
  private boolean caseSensitive;

  public FRQuiz(Lesson lesson, ArrayList<Integer> order, int progress, ArrayList<Integer> missed, boolean caseSensitive) {
    super(lesson, order, progress, missed);
    this.caseSensitive = caseSensitive;
  }

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

  public String dump() {
    CabinetWriter cw = new CabinetWriter();
    cw.putBool(caseSensitive);
    return dump(cw);
  }

  public static FRQuiz load(String repr) {
    CabinetReader cr = new CabinetReader(repr);
    boolean caseSensitive = cr.getBool();
    // FRQuiz frq = (FRQuiz) load(cr);
    // frq.caseSensitive = caseSensitive;
    ArrayList<Integer> order = cr.getIntArray();
    int progress = cr.getInt();
    ArrayList<Integer> missed = cr.getIntArray();
    return new FRQuiz(null, order, progress, missed, caseSensitive);
  }
}
