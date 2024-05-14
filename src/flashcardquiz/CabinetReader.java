package flashcardquiz;

import java.util.Scanner;

import static flashcardquiz.CabinetUtils.unescape;

class CabinetReader {
  private Scanner scan;

  public CabinetReader(Scanner scan) {
    this.scan = scan;
  }

  public String getString() {
    String type = scan.next();
    scan.nextLine();
    if (type.equals("str")) {
      return unescape(scan);
    } else {
      System.out.println("[warn] ReadCabinet.getString wrong type");
      return null;
    }
  }
}
