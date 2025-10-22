package edu.bu.cs611.core;/* Player.java — player state (name/score/turn), name input, unique display initial. */

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player {
    private String name;
    private float score;     
    private boolean turn;    
    private Character customInitial;

    public Player(String name) {
        this(name, 0.0f, false);
    }

    public Player(String name, float score, boolean turn) {
        this.name = (name == null || name.trim().isEmpty()) ? "Player" : name.trim();
        this.score = score;
        this.turn = turn;
        this.customInitial = null;
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
        return this.turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    @Override
    public String toString() {
        return "Player{name='" + name + "', score=" + score + ", turn=" + turn + "}";
    }

    public static void promptMultiplePlayersForNames(List<Player> players){
        int count = 1;
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);

            while (true) {
                String m = "What is your name player " + count + "?";
                p.promptForName(m); 

                // enforce unique names (case-insensitive)
                boolean nameTaken = false;
                for (int j = 0; j < i; j++) {
                    if (p.getName().equalsIgnoreCase(players.get(j).getName())) {
                        System.out.println("Name already taken. Please choose another name.");
                        nameTaken = true;
                        break;
                    }
                }
                if (nameTaken) continue;

                // assign a unique display initial (first unused letter from their name)
                Set<Character> used = new HashSet<>();
                for (int j = 0; j < i; j++) used.add(players.get(j).getInitial());

                char desired = pickInitialFromName(p.getName(), used);
                char defaultInitial = Character.toUpperCase(p.getName().trim().charAt(0));
                if (desired != defaultInitial) {
                    p.setCustomInitial(desired);
                    System.out.println("Note: another player uses '" + defaultInitial +
                            "'. We'll display you as '" + desired + "'.");
                }

                break; 
            }

            count++;
        }
    }

    public void promptForName(String message) {
        System.out.println(message);
        String s = Game.scanner.nextLine().trim();

        if (s.equals(Game.RESERVED_QUIT_KEYWORD)) {  
            System.out.println("User triggered a quit. Closing game.");
            System.exit(0);
        }

        this.setName(s.isEmpty() ? "Player" : s);
    }

    public void setCustomInitial(char c) {
        this.customInitial = Character.toUpperCase(c);
    }

    public char getInitial() {
        if (customInitial != null) return customInitial;
        String nameSafe = (this.name == null || this.name.trim().isEmpty()) ? "?" : this.name.trim();
        return Character.toUpperCase(nameSafe.charAt(0));
    }

    private static char pickInitialFromName(String name, Set<Character> used) {
        if (name == null) name = "";
        String n = name.toUpperCase();

        for (int k = 0; k < n.length(); k++) {
            char ch = n.charAt(k);
            if (ch >= 'A' && ch <= 'Z' && !used.contains(ch)) return ch;
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            if (!used.contains(c)) return c;
        }
        return 'A'; 
    }

    



}
