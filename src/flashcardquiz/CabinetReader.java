package flashcardquiz;

import java.util.Scanner;
import java.util.ArrayList;

import static flashcardquiz.CabinetUtils.unescape;

class CabinetReader {
  private Scanner scan;

  public CabinetReader(String data) {
    scan = new Scanner(data);
  }

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

  public Boolean getBool() {
    String type = scan.next();
    scan.nextLine();
    if (type.equals("pre")) {
      return Integer.parseInt(unescape(scan)) != 0;
    } else {
      System.out.println("[warn] ReadCabinet.getBool wrong type");
      return null;
    }
  }

  public Integer getInt() {
    String type = scan.next();
    scan.nextLine();
    if (type.equals("int")) {
      return Integer.parseInt(unescape(scan));
    } else {
      System.out.println("[warn] ReadCabinet.getInt wrong type");
      return null;
    }
  }

  public ArrayList<Integer> getIntArray() {
    String type = scan.next();
    scan.nextLine();
    if (type.equals("iar")) {
      CabinetReader cr = new CabinetReader(unescape(scan));
      ArrayList<Integer> out = new ArrayList<>();
      int size = cr.getInt();
      for (int i = 0; i < size; i++) {
        int n = cr.getInt();
        out.add(n);
      }
      return out;
    } else {
      System.out.println("[warn] ReadCabinet.getIntArray wrong type");
      return null;
    }
  }
}
