// Imports
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class UnoDeck {
    // Instance variables
    private UnoCard[] amountOfCards;
    private int cardsInDeck;
    // Default constructor.
    public UnoDeck() {
        // Generates an amount of cards in the Uno deck.
        amountOfCards = new UnoCard[108];
    }
    // Methods
    public void reset() {
        // We're adding the values from the enumeration from the color class into the array.
        UnoCard.Color[] colors = UnoCard.Color.values();
        cardsInDeck = 0;
        // This outer for loop gives each card all the colors except for wild.
        for (int i = 0; i < colors.length - 1; i++) {
            // An array of our colors.
            UnoCard.Color setColor = colors[i];
            // This generates a zero card.
            amountOfCards[cardsInDeck++] = new UnoCard(setColor, UnoCard.Card.getCard(0));
            // This first inner for loop generates two of the same cards of 1-9.
            for(int j = 1; j < 10; j++) {
                amountOfCards[cardsInDeck++] = new UnoCard(setColor, UnoCard.Card.getCard(j));
                amountOfCards[cardsInDeck++] = new UnoCard(setColor, UnoCard.Card.getCard(j));
            }
            // An array of our action cards for our second inner for loop.
            UnoCard.Card[] actionCards = new UnoCard.Card[] {UnoCard.Card.DrawTwo, UnoCard.Card.Skip, UnoCard.Card.Reverse};
            // This second inner for loop generates two of the same action cards.
            for (UnoCard.Card card : actionCards) {
                amountOfCards[cardsInDeck++] = new UnoCard(setColor, card);
                amountOfCards[cardsInDeck++] = new UnoCard(setColor, card);
            }  
            // An array of our wild cards for our third inner for loop.
            UnoCard.Card[] wildCards = new UnoCard.Card[] {UnoCard.Card.WildCard, UnoCard.Card.WildDrawFour};
            // This third inner for loop generates our wild cards.
            for (UnoCard.Card card : wildCards) {
                for(int k = 0; k < 4; k++) {
                    amountOfCards[cardsInDeck++] = new UnoCard(UnoCard.Color.Wild, card);
                }
            }
        }
    }
    // This method takes the current discard pile and creates a new uno deck of however many cards were there.
    public void replaceDeckWith(ArrayList<UnoCard> cards) {
        this.amountOfCards = cards.toArray(new UnoCard[cards.size()]);
        this.cardsInDeck = this.amountOfCards.length;
    }
    // Checks if the cards in the deck is empty, which will return true if it is, false otherwise.
    public boolean isEmpty() {
        return cardsInDeck == 0;
    }
    public void deckShuffle() {
        int a = amountOfCards.length;
        Random shuffle = new Random();
        for (int i = 0; i < amountOfCards.length; i++) {
            // Get a random number to draw a random card at the index of the random number.
            int randomNumber = i + shuffle.nextInt(a - i);
            UnoCard randomCard = amountOfCards[randomNumber];
            // Replace the current card with the card at the index of the random number.
            amountOfCards[randomNumber] = amountOfCards[i];
            // Then, place the random card at the top of the deck, and then so on and so forth.
            amountOfCards[i] = randomCard;
        }
    }
    // Draws a card from the top of the deck. If it can't, then this error I made will show up instead of an Illegal Argument.
    public UnoCard drawACard() throws IllegalArgumentException {
        if (isEmpty()) {
            throw new IllegalArgumentException("ERROR: Cannot draw a card. REASON: No cards in current deck.");
        }
        return amountOfCards[--cardsInDeck];
    }
    // With gui, this  returns the image of the card. If it can't, then this error I made will show up instead of an Illegal Argument.
    public ImageIcon drawACardImage() throws IllegalArgumentException {
        if (isEmpty()) {
            throw new IllegalArgumentException("ERROR: Cannot draw a card. REASON: No cards in current deck.");
        }
        return new ImageIcon(amountOfCards[--cardsInDeck].toString() + ".png");
    }
    public UnoCard[] drawACard(int num) {
        // An error if you try to draw a negative amount of cards.
        if (num < 0) {
            throw new IllegalArgumentException("ERROR: Tried to draw " + num + " card(s). REASON: You must draw a positive amount of cards.");
        }
        // An error if you try to draw more cards then the amount in the deck.
        if (num > cardsInDeck) {
            throw new IllegalArgumentException("ERROR: Tried to draw " + num + " card(s). REASON: You cannot draw " + cardsInDeck + " or more card(s) then there are in the deck.");
        }
        // A new array is created to make the player draw a num amount of cards.
        UnoCard[] temp = new UnoCard[num];
        for (int i = 0; i < num; i++) {
            temp[i] = amountOfCards[--cardsInDeck];
        }
        return temp;
    }
}