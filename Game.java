// Imports
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Game {
    // Instance variables
    private int currentPlayer;
    private String[] playerIds;
    private UnoDeck deck;
    // This arraylist of an arraylist keeps track of all the player's hands
    private ArrayList<ArrayList<UnoCard>> playerHand;
    private ArrayList<UnoCard> stockpile;
    private UnoCard.Color validColor;
    private UnoCard.Card validCard;
    // Keeps track of the game direction especially with the reverse cards.
    boolean gameDirection;

    // Parameter constructor
    public Game(String[] pids) {
        deck = new UnoDeck();
        deck.deckShuffle();
        stockpile = new ArrayList<UnoCard>();
        playerIds = pids;
        currentPlayer = 0;
        gameDirection = false;
        playerHand = new ArrayList<ArrayList<UnoCard>>();
        // In this for loop, a player draws 7 cards from the deck in the deck variable and loops for however many players are playing.
        for (int i = 0; i < pids.length; i++) {
            ArrayList<UnoCard> hand = new ArrayList<UnoCard>(Arrays.asList(deck.drawACard(7)));
            playerHand.add(hand);
        }
    }

    // Methods
    public void start(Game game) {
        UnoCard card = deck.drawACard();
        validColor = card.getColor();
        validCard = card.getCard();
        // If the game starts with a wild card, a wild draw four, or a draw two, start the game over.
        if (card.getCard() == UnoCard.Card.WildCard) {
            start(game);
        }
        if (card.getCard() == UnoCard.Card.WildDrawFour || card.getCard() == UnoCard.Card.DrawTwo) {
            start(game);
        }
        // If the game starts with a skip card, then whoever goes first gets skipped.
        if (card.getCard() == UnoCard.Card.Skip) {
            JLabel message = new JLabel(playerIds[currentPlayer] + " was skipped!");
            message.setFont(new Font("Arial", Font.BOLD, 48));
            JOptionPane.showMessageDialog(null, message);
        }
        if (card.getCard() == UnoCard.Card.Reverse) {
            JLabel message = new JLabel(playerIds[currentPlayer] + " the game direction changed!");
            message.setFont(new Font("Arial", Font.BOLD, 48));
            JOptionPane.showMessageDialog(null, message);
            // The "^=" operator flips the true and false values.
            gameDirection ^= true;
            currentPlayer = playerIds.length - 1;
        }
        // If the game direction is false, then the direction is clockwise. If true, then it's set counter-clockwise.
        if (!gameDirection) {
            currentPlayer = (currentPlayer + 1) % playerIds.length;
        } else if (gameDirection) {
            currentPlayer = (currentPlayer - 1) % playerIds.length;
            if (currentPlayer == -1) {
                currentPlayer = playerIds.length - 1;
            }
        }
        stockpile.add(card);
    }

    public UnoCard getTopCard() {
        return new UnoCard(validColor, validCard);
    }

    public ImageIcon getTopCardImage() {
        return new ImageIcon(validCard + "-" + validCard + ".png");
    }

    public boolean isGameOver() {
        for (String player : this.playerIds) {
            // if any players' hand is empty, it returns true, meaning the game is over.
            if (hasEmptyHand(player)) {
                return true;
            }
        }
        return false;
    }

    public String getCurrentPlayer() {
        // Returns the index position of the current player from the playerIds array.
        return this.playerIds[this.currentPlayer];
    }

    public String getPreviousPlayer(int i) {
        // Returns the index position of the last player who played from the playerIds array.
        int index = this.currentPlayer + i;
        if (index == -1) {
            index = playerIds.length - 1;
        }
        return this.playerIds[index];
    }

    public String[] getPlayers() {
        // Returns all players.
        return playerIds;
    }

    public ArrayList<UnoCard> getPlayerHand(String pid) {
        // Returns a single player's hand.
        int index = Arrays.asList(playerIds).indexOf(pid);
        return playerHand.get(index);
    }

    public int getPlayerHandSize(String pid) {
        // Returns the number of cards in a player's hand.
        return getPlayerHand(pid).size();
    }

    public UnoCard getPlayerCard(String pid, int choice) {
        // Returns a single card from a player's hand.
        ArrayList<UnoCard> hand = getPlayerHand(pid);
        return hand.get(choice);
    }

    public boolean hasEmptyHand(String pid) {
        // Checks if a player's hand is empty.
        return getPlayerHand(pid).isEmpty();
    }

    public boolean validCardPlay(UnoCard card) {
        // Checks if a player's card has the correct color or value.
        return card.getColor() == validColor || card.getCard() == validCard;
    }

    public void checkPlayerTurn(String pid) throws InvalidPlayerTurnException {
        // Checks if the correct player has their turn.
        if (!Objects.equals(this.playerIds[this.currentPlayer], pid)) {
            throw new InvalidPlayerTurnException("ERROR: You can't go. REASON: It's not " + pid + "'s turn.", pid);
        }
    }

    public void submitDraw(String pid) { // throws InvalidPlayerTurnException
        // Checks if the deck is empty before making a player draw cards,
        // If true, replace the current deck with the stockpile and shuffle the deck, then the player draws cards.
        // If false, the if statement is skipped and the player draws however many cards.
        if (deck.isEmpty()) {
            deck.replaceDeckWith(stockpile);
            deck.deckShuffle();
        }
        getPlayerHand(pid).add(deck.drawACard());
        if (!gameDirection) {
            currentPlayer = (currentPlayer + 1) % playerIds.length;
        } else if (gameDirection) {
            currentPlayer = (currentPlayer - 1) % playerIds.length;
            if (currentPlayer == -1) {
                currentPlayer = playerIds.length - 1;
            }
        }
    }
    public void setCardColor(UnoCard.Color color) {
        // Changes the card color.
        validColor = color;
    }
    public void submitPlayerCard(String pid, UnoCard card, UnoCard.Color declaredColor) throws InvalidColorSubmissionException, InvalidCardSubmissionException, InvalidPlayerTurnException {
        checkPlayerTurn(pid);
        ArrayList<UnoCard> pHand = getPlayerHand(pid);
        if (!validCardPlay(card)) {
            if (card.getColor() == UnoCard.Color.Wild) {
                validColor = card.getColor();
                validCard = card.getCard();
            }
            if (card.getColor() != validColor) {
                JLabel message = new JLabel("Invalid player move, expected color: " + validColor + ", but got color " + card.getColor());
                message.setFont(new Font("Arial", Font.BOLD, 48));
                JOptionPane.showMessageDialog(null, message);
                throw new InvalidColorSubmissionException("Invalid player move, expected color: " + validColor + ", but got color " + card.getColor(), card.getColor(), validColor);
            } else if (card.getCard() != validCard) {
                JLabel message2 = new JLabel("Invalid player move, expected value: " + validCard + ", but got color " + card.getCard());
                message2.setFont(new Font("Arial", Font.BOLD, 48));
                JOptionPane.showMessageDialog(null, message2);
                throw new InvalidCardSubmissionException("Invalid player move, expected value: " + validCard + ", but got color " + card.getCard(), card.getCard(), validCard);
            }
        }
        pHand.remove(card);
        if (hasEmptyHand(this.playerIds[currentPlayer])) {
            JLabel message3 = new JLabel(this.playerIds[currentPlayer] + " won the game! Thank you for playing!");
            message3.setFont(new Font("Arial", Font.BOLD, 48));
            JOptionPane.showMessageDialog(null, message3);
            System.exit(0);
        }
        validColor = card.getColor();
        validCard = card.getCard();
        stockpile.add(card);
        if (!gameDirection) {
            currentPlayer = (currentPlayer + 1) % playerIds.length;
        } else if (gameDirection) {
            currentPlayer = (currentPlayer - 1) % playerIds.length;
            if (currentPlayer == -1) {
                currentPlayer = playerIds.length - 1;
            }
        }
        if (card.getColor() == UnoCard.Color.Wild) {
            validColor = declaredColor;
        }
        // Makes the next player draw two cards.
        if (card.getCard() == UnoCard.Card.DrawTwo) {
            pid = playerIds[currentPlayer];
            getPlayerHand(pid).add(deck.drawACard());
            getPlayerHand(pid).add(deck.drawACard());
            JLabel message4 = new JLabel(pid + " drew 2 cards!");
        }
        // Makes the next player draw four cards.
        if (card.getCard() == UnoCard.Card.WildDrawFour) {
            pid = playerIds[currentPlayer];
            getPlayerHand(pid).add(deck.drawACard());
            getPlayerHand(pid).add(deck.drawACard());
            getPlayerHand(pid).add(deck.drawACard());
            getPlayerHand(pid).add(deck.drawACard());
            JLabel message5 = new JLabel(pid + " drew 4 cards!");
        }
        // Skips the next player's turn.
        if (card.getCard() == UnoCard.Card.Skip) {
            JLabel message6 = new JLabel(playerIds[currentPlayer] + " was skipped!");
            message6.setFont(new Font("Arial", Font.BOLD, 48));
            JOptionPane.showMessageDialog(null, message6);
            if (!gameDirection) {
                currentPlayer = (currentPlayer + 1) % playerIds.length;
            } else if (gameDirection) {
                currentPlayer = (currentPlayer - 1) % playerIds.length;
                if (currentPlayer == -1) {
                    currentPlayer = playerIds.length - 1;
                }
            }
        }
        // Changes the direction of the game
        if (card.getCard() == UnoCard.Card.Reverse) {
            JLabel message7 = new JLabel(pid + " changed the game direction!");
            message7.setFont(new Font("Arial", Font.BOLD, 48));
            JOptionPane.showMessageDialog(null, message7);
            gameDirection ^= true;
            if (gameDirection) {
                currentPlayer = (playerIds.length - 2) % playerIds.length;
                if (currentPlayer == -1) {
                    currentPlayer = playerIds.length - 2;
                }
                if (currentPlayer == -2) {
                    currentPlayer = playerIds.length - 2;
                }
            } else if (!gameDirection) {
                currentPlayer = (currentPlayer + 2) % playerIds.length;
            }
        }
    }
}
// These extra classes are used for creating custom exceptions and throwing custom errors.
class InvalidPlayerTurnException extends Exception {
    // Instance variable
    String playerId;

    // Parameter constructor
    public InvalidPlayerTurnException(String message, String pid) {
        super(message);
        playerId = pid;
    }

    // Method
    public String getPid() {
        return playerId;
    }
}
class InvalidColorSubmissionException extends Exception {
    // Instance variables
    private UnoCard.Color expected;
    private UnoCard.Color actual;

    // Parameter constructor
    public InvalidColorSubmissionException(String message, UnoCard.Color actual, UnoCard.Color expected) {
        this.actual = actual;
        this.expected = expected;
    }
}
class InvalidCardSubmissionException extends Exception {
    // Instance variables
    private UnoCard.Card expected;
    private UnoCard.Card actual;

    // Parameter constructor
    public InvalidCardSubmissionException(String message, UnoCard.Card actual, UnoCard.Card expected) {
        this.actual = actual;
        this.expected = expected;
    }
}