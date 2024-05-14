package flashcardquiz;

import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class CabinetDriver {
  static String repr(String str) {
    String out = "|| ";
    for (int c: str.toCharArray()) {
      out += c + ". ";
    }
    return out + "||";
  }

  public static void main(String[] args) {
    // int[] charCodes = {
    //     32, 9, 10, 8195, 8203, 160, 83, 111, 109, 101, 8287, 84, 101, 120, 116,
    //     8232, 8233, 0, 7, 8, 9, 11, 12, 13, 27, 127, 159,
    //     128512, 65021, 34, 39, 92, 47, 38, 60, 62, 123, 125, 91, 93, 40, 41, 59,
    //     58, 44, 46, 63, 33, 64, 35, 36, 37, 94, 42, 45, 95, 43, 61, 96, 126, 124
    // };

    // StringBuilder morbidString = new StringBuilder();

    // // Construct the string from character codes
    // for (int code : charCodes) {
    //     morbidString.append((char) code);
    // }

    // String og = morbidString.toString();
    // // System.out.println(og);
    // String escaped = Cabinet.escape(og);
    // // System.out.println(escaped);
    // String unescaped = Cabinet.unescape(new Scanner(escaped));
    // // System.out.println(unescaped);
    // System.out.println(repr(og));
    // System.out.println(repr(escaped));
    // System.out.println(repr(unescaped));
    // System.out.println(og.equals(unescaped));

    try {
      PrintWriter pw = new PrintWriter("out");
      CabinetWriter cw = new CabinetWriter(pw);
      cw.putString("  \n\n \n\n");
      cw.putString("");
      cw.putString(" ");
      cw.putString(" hey guys ");
      pw.close();

      Scanner scan = new Scanner(new File("out"));
      CabinetReader cr = new CabinetReader(scan);
      System.out.println(repr(cr.getString()));
      System.out.println(repr(cr.getString()));
      System.out.println(repr(cr.getString()));
      System.out.println(repr(cr.getString()));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
