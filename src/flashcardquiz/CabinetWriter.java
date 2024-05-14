package flashcardquiz;

import java.io.PrintWriter;

import static flashcardquiz.CabinetUtils.escape;

class CabinetWriter {
  private PrintWriter pw;

  public CabinetWriter(PrintWriter pw) {
    this.pw = pw;
  }

  public void putString(String str) {
    pw.print("str\n" + escape(str));
  }
}
