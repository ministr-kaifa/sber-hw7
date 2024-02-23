package ru.zubkoff.rps.gamecore;

import java.util.List;
import java.util.Optional;

import ru.zubkoff.rps.api.RPSChoice;

/**
 * Представляет собой отчет о матче в игру Камень-Ножницы-Бумага, содержащий информацию о прошедшем или предстоящем матче.
 *
 * Если матч ожидается (isOver = false), информация о выборах игроков (firstPlayerChoices и secondPlayerChoices) и
 * победителе (winnerName) не будет предоставлена.
 *
 * @param firstPlayerName     Имя класса первого игрока, участвующего в матче
 * @param secondPlayerName    Имя класса второго игрока, участвующего в матче
 * @param isOver              Указывает, завершен ли матч (true) или ожидается (false).
 * @param firstPlayerChoices  Список выборов, сделанных первым игроком во время матча (если доступно).
 * @param secondPlayerChoices Список выборов, сделанных вторым игроком во время матча (если доступно).
 * @param winnerName          Имя победителя матча
 */
public record RPSMatchReport(String firstPlayerName, String secondPlayerName,
                             boolean isOver,
                             List<RPSChoice> firstPlayerChoices, List<RPSChoice> secondPlayerChoices,
                             Optional<String> winnerName) {

}
