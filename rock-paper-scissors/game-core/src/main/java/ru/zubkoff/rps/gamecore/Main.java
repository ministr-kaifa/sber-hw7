package ru.zubkoff.rps.gamecore;

import java.nio.file.Path;

public class Main {
  public static void main(String[] args) {
    new CliRPSTournament(new RPSPlayerManager(Path.of(".\\rock-paper-scissors\\game-core\\src\\main\\resources"))).startCliTournament();
  }
}
