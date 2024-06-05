public class Main {
    public static void main(String[] args) {
        // This test1 variable has the values of a card color being red, and a card value being zero.
        UnoCard test1 = new UnoCard(UnoCard.Color.Red, UnoCard.Card.Zero);
        // This test2 variable generates a deck with nothing in it.
        UnoDeck test2 = new UnoDeck();
        // This prints test1's card color, which would be red.
        System.out.println(test1.getColor());
        // This prints test1's card value, which would be zero.
        System.out.println(test1.getCard());
        // This prints the entirety of test1, to check if it's the right card. It will print "Red Zero" because I set the values that way.
        System.out.println(test1.toCheck());
        // Checks if the deck is empty, which it should be because I didn't make a call to add any cards to the Uno deck.
        System.out.print (test2.isEmpty());
    }
}