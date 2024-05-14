package flashcardquiz;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import static flashcardquiz.CabinetUtils.escape;

class CabinetWriter {
  private PrintWriter pw;
  private StringWriter sw;

  public CabinetWriter() {
    sw = new StringWriter();
    pw = new PrintWriter(sw);
  }

  public CabinetWriter(PrintWriter pw) {
    this.pw = pw;
    this.sw = null;
  }

  public String getOutput() {
    if (sw != null) return sw.toString();
    return null;
  }

  public void putString(String str) {
    pw.print("str\n" + escape(str));
  }

  public void putBool(boolean p) {
    // pre means predicate
    pw.print("pre\n" + escape((p ? 1 : 0) + ""));
  }

  public void putInt(int n) {
    pw.print("int\n" + escape(n + ""));
  }

  public void putIntArray(ArrayList<Integer> ns) {
    CabinetWriter cw = new CabinetWriter();
    cw.putInt(ns.size());
    for (int n: ns)
      cw.putInt(n);
    pw.print("iar\n" + escape(cw.getOutput()));
  }
}
