package ru.zubkoff.rps.gamecore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import ru.zubkoff.rps.api.RPSChoice;
import ru.zubkoff.rps.api.RPSPlayer;



public class RPSTournament {
  private static final int ROUNDS_TO_WIN = 3;

  private final RPSPlayerManager playerManager;
  private final Iterator<String> jarFileNames;
  private RPSPlayer firstPlayer;
  private RPSPlayer secondPlayer;
  private boolean isOver;

  public RPSTournament(RPSPlayerManager playerManager) {
    this.playerManager = playerManager;

    var allJarFileNames = playerManager.getAllJarFileNames();
    if(allJarFileNames.size() < 2) {
      throw new RuntimeException("must be at least 2 players in tournament");
    }
    isOver = false;
    jarFileNames = allJarFileNames.iterator();
    firstPlayer = playerManager.loadPlayer(jarFileNames.next());
    secondPlayer = playerManager.loadPlayer(jarFileNames.next());
  }

  public RPSMatchReport getPreMatchInfo() {
    return new RPSMatchReport(firstPlayer.getClass().getName(), secondPlayer.getClass().getName(), 
      false, List.of(), List.of(), Optional.empty());
  }

  public RPSMatchReport nextMatch() {
    var firstPlayerChoises = new ArrayList<RPSChoice>();
    var secondPlayerChoises = new ArrayList<RPSChoice>();
    int firstPlayerWins = 0;
    int secondPlayerWins = 0;
    while (firstPlayerWins < ROUNDS_TO_WIN && secondPlayerWins < ROUNDS_TO_WIN) {
      var firstPlayerChoice = firstPlayer.nextChoice();
      var secondPlayerChoice = secondPlayer.nextChoice();
      if(firstPlayerChoice.canBeat(secondPlayerChoice)) {
        firstPlayerWins++;
      } else if (secondPlayerChoice.canBeat(firstPlayerChoice)) {
        secondPlayerWins++;
      }
      firstPlayerChoises.add(firstPlayerChoice);
      secondPlayerChoises.add(secondPlayerChoice);
    }
    var winner = firstPlayerWins == ROUNDS_TO_WIN? firstPlayer : secondPlayer;
    var matchReport = new RPSMatchReport(firstPlayer.getClass().getName(), secondPlayer.getClass().getName(), 
      true, firstPlayerChoises, secondPlayerChoises, Optional.of(winner.getClass().getName()));
    firstPlayer = winner;
    if(jarFileNames.hasNext()) {
      secondPlayer = playerManager.loadPlayer(jarFileNames.next());
    } else {
      isOver = true;
    }
    return matchReport;
  }

  public boolean isOver() {
    return isOver;
  }

  public String getWinnerName() {
    if(isOver()) {
      return firstPlayer.getClass().getName();
    } else {
      throw new RuntimeException("tournament is not over");
    }
  }
  
}
