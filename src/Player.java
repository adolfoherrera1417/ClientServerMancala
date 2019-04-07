public class Player {
  boolean isComputer;
  int playerNum;

  public Player(boolean isComp, int num) {
    isComputer = isComp;
    playerNum = num;
  }

  public void SetPlayer(int num) {
    playerNum = num;
  }
}
