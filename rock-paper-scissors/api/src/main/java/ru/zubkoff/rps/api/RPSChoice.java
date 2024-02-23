package ru.zubkoff.rps.api;

public enum RPSChoice {
  ROCK("rock"),
  PAPER("paper"),
  SCISSORS("scissors");

  private final String move;

  RPSChoice(String move) {
      this.move = move;
  }

  @Override
  public String toString() {
    return move;
  }

  public boolean canBeat(RPSChoice other) {
    return this == ROCK && other == SCISSORS ||
      this == SCISSORS && other == PAPER ||
      this == PAPER && other == ROCK;
  }
}