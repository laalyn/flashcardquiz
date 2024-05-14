package flashcardquiz;

class Card {
  private String front;
  private String back;

  public Card(String front, String back) {
    this.front = front;
    this.back = back;
  }

  public String getFront() {
    return front;
  }

  public String getBack() {
    return back;
  }

  public String dump() {
    CabinetWriter cw = new CabinetWriter();
    cw.putString(front);
    cw.putString(back);
    return cw.getOutput();
  }

  public static Card load(String repr) {
    CabinetReader cr = new CabinetReader(repr);
    String front = cr.getString();
    String back = cr.getString();
    return new Card(front, back);
  }
}
