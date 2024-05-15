package flashcardquiz;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

class FileStore extends Store {
  private File file;

  public FileStore(String path) {
    setPath(path);
  }

  public String getPath() {
    return file.getPath();
  }

  public void setPath(String path) {
    file = new File(path);
  }

  public void dump() {
    try {
      PrintWriter pw = new PrintWriter(file);
      CabinetWriter cw = new CabinetWriter(pw);

      cw.putInt(cards.size());
      for (Card c: cards) {
        cw.putString(c.dump());
      }
      cw.putIntArray(cardIds);
      cw.putInt(nextCardId);

      cw.putInt(quizzes.size());
      for (Quiz q: quizzes) {
        if (q instanceof MCQuiz) {
          cw.putString("MC");
          cw.putString(((MCQuiz) q).dump());
        } else if (q instanceof FRQuiz) {
          cw.putString("FR");
          cw.putString(((FRQuiz) q).dump());
        } else {
          System.out.println("[warn] FileStore.dump unknown quiz instance");
        }
      }
      cw.putIntArray(quizIds);
      cw.putInt(nextQuizId);

      cw.putInt(lessons.size());
      for (Lesson l: lessons) {
        cw.putString(l.dump());
      }
      cw.putIntArray(lessonIds);
      cw.putInt(nextLessonId);
      for (ArrayList<Integer> ids: lessonCardIds) {
        cw.putIntArray(ids);
      }
      for (ArrayList<Integer> ids: lessonQuizIds) {
        cw.putIntArray(ids);
      }

      pw.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public void load() {
    try {
      Scanner scan = new Scanner(file);
      CabinetReader cr = new CabinetReader(scan);
      
      int numCards = cr.getInt();
      cards = new ArrayList<>();
      for (int i = 0; i < numCards; i++) {
        Card c = Card.load(cr.getString());
        cards.add(c);
      }
      cardIds = cr.getIntArray();
      nextCardId = cr.getInt();

      int numQuizzes = cr.getInt();
      quizzes = new ArrayList<>();
      for (int i = 0; i < numQuizzes; i++) {
        String type = cr.getString();
        if (type.equals("MC")) {
          Quiz q = MCQuiz.load(cr.getString());
          quizzes.add(q);
        } else if (type.equals("FR")) {
          Quiz q = FRQuiz.load(cr.getString());
          quizzes.add(q);
        } else {
          System.out.println("[warn] FileStore.load unknown quiz instance");
        }
      }
      quizIds = cr.getIntArray();
      nextQuizId = cr.getInt();

      int numLessons = cr.getInt();
      lessons = new ArrayList<>();
      for (int i = 0; i < numLessons; i++) {
        Lesson l = Lesson.load(cr.getString());
        lessons.add(l);
      }
      lessonIds = cr.getIntArray();
      nextLessonId = cr.getInt();
      lessonCardIds = new ArrayList<>();
      for (int i = 0; i < numLessons; i++) {
        ArrayList<Integer> cardIds = cr.getIntArray();
        lessonCardIds.add(cardIds);
        Lesson l = lessons.get(i);
        for (int id: cardIds) {
          l.addCard(getCard(id));
        }
      }
      lessonQuizIds = new ArrayList<>();
      for (int i = 0; i < numLessons; i++) {
        ArrayList<Integer> unsafeQuizIds = cr.getIntArray();
        ArrayList<Integer> quizIds = new ArrayList<>();
        for (int id: unsafeQuizIds) {
          Quiz q = getQuiz(id);
          if (q != null) { // due to deleted refs
            quizIds.add(id);
          } else {
            System.out.println("[debug] FileStore.load skipped deleted ref");
          }
        }
        lessonQuizIds.add(quizIds);
        Lesson l = lessons.get(i);
        for (int id: quizIds) {
          Quiz q = getQuiz(id);
          q.setLesson(l);
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
