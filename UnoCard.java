public class UnoCard {
    // Instance Variables
    // Enumeration is a special class that groups unchangeable variables.
    enum Color {
        // Colors that represent final variables.
        Red, Blue, Green, Yellow, Wild;
        // Array of enumeration of all colors.
        private static final Color[] colors = Color.values();
        // Our getter method.
        public static Color getColor(int i) {
            return Color.colors[i];
        }
    }
    enum Card {
        // Cards that represent final variables.
        Zero, One, Two, Three, Four, Five, Six, Seven, Eight, Nine, DrawTwo, Skip, Reverse, WildCard, WildDrawFour;
        // Array of enumeration of all cards.
        private static final Card[] cards = Card.values();
        // Our getter method.
        public static Card getCard(int i) {
            return Card.cards[i];
        }
    }
    // These variables have finals on them because we're not changing their values later.
    private final Color color;
    private final Card card;
    // Parameter constructor.
    public UnoCard(final Color color, final Card card) {
        this.color = color;
        this.card = card;
    }
    // Methods
    // Getter method for colors.
    public Color getColor() {
        return this.color;
    }
    // Getter method for cards.
    public Card getCard() {
        return this.card;
    }
    // A method for testing our cards.
    public String toCheck() {
        return color + " " + card;
    }
}