package flashcardquiz;

import java.util.Scanner;
import java.io.PrintWriter;
import java.io.StringWriter;

class CabinetUtils {
  public static String escape(String str) {
    str += "\n.";
    Scanner scan = new Scanner(str);
    String out = "";
    int numLines = 0;
    while (scan.hasNextLine()) {
      String line = scan.nextLine();
      out += line + "\n";
      numLines++;
    }
    out = numLines + "\n" + out;
    return out;
  }

  public static String unescape(Scanner scan) {
    int numLines = scan.nextInt();
    scan.nextLine();
    String out = "";
    for (int i = 0; i < numLines; i++) {
      // NOTE might convert some alternative forms of newline to \n
      String line = scan.nextLine();
      out += (i == 0 ? "" : "\n") + line;
    }
    if (out.substring(out.length() - 1).equals("."))
      return out.substring(0, out.length() - 2);
    else {
      System.out.println("[warn] Cabinet.unescape malformed string");
      return out;
    }
  }
}
