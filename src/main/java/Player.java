public class Player {
    private String name;
    private float score;     
    private boolean turn;    

    public Player(String name) {
        this(name, 0.0f, false);
    }

    public Player(String name, float score, boolean turn) {
        this.name = (name == null || name.trim().isEmpty()) ? "Player" : name.trim();
        this.score = score;
        this.turn = turn;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name.trim();
        }
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void incrementScore() {
        this.score += 1.0f;
    }

    public boolean getTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    @Override
    public String toString() {
        return "Player{name='" + name + "', score=" + score + ", turn=" + turn + "}";
    }

    public void promptForName() {
        System.out.println("What is your name young buck?");
        String s = Game.scanner.nextLine().trim();

        if (s.equals(Game.RESERVED_QUIT_KEYWORD)) {  
            System.out.println("User triggered a quit. Closing game.");
            System.exit(0);
        }

        this.setName(s.isEmpty() ? "Player" : s);
}


}
