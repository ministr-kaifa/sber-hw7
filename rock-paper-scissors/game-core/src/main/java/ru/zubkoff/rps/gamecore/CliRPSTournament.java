package ru.zubkoff.rps.gamecore;

public class CliRPSTournament extends RPSTournament {

  public CliRPSTournament(RPSPlayerManager playerManager) {
    super(playerManager);
  }

  public void startCliTournament() {
    while(!isOver()) {
      nextMatch();
    }
    System.out.println("Победитель турнира: " + getWinnerName());
  }

  @Override
  public RPSMatchReport nextMatch() {
    var preMatchInfo = getPreMatchInfo();
    System.out.println("В левом углу ринга: " + preMatchInfo.firstPlayerName());
    System.out.println("В правом углу ринга: " + preMatchInfo.secondPlayerName());
    var postMatchInfo = super.nextMatch();
    for (int i = 0; i < postMatchInfo.firstPlayerChoices().size(); i++) {
      var p1Choice = postMatchInfo.firstPlayerChoices().get(i);
      var p2Choice = postMatchInfo.secondPlayerChoices().get(i);
      System.out.println(postMatchInfo.firstPlayerName() + ": " + p1Choice + "\t" + p2Choice + " :" + postMatchInfo.secondPlayerName());
    }
    System.out.println("Победитель матча: " + postMatchInfo.winnerName().get());
    return postMatchInfo;
  }
  
}
