package flashcardquiz;

import java.util.ArrayList;

class Finder {
  private ArrayList<Integer> nums;

  public Finder(ArrayList<Integer> nums) {
    this.nums = nums;
  }

  public int find(int n) {
    // TODO use binary search
    for (int i = 0; i < nums.size(); i++) {
      if (nums.get(i).equals(n)) return i;
    }
    return -1;
  }
}
