import java.util.Scanner;

public final class Main {
    private static final Scanner in = Game.scanner;

    public static void main(String[] args) {
        System.out.println("\n--- Welcome to CS611 Games ---");
        System.out.println("You can quit anytime by typing '" + Game.RESERVED_QUIT_KEYWORD + "'.\n");

        while (true) {
            System.out.println("Select a game:");
            System.out.println("[1] Sliding Puzzle");
            System.out.println("[2] Dots & Boxes");
            System.out.println("[3] Quit");
            System.out.print("Enter choice: ");

            String s = in.nextLine().trim();
            if (exitIfQuit(s)) return;

            int choice;
            try { choice = Integer.parseInt(s); }
            catch (NumberFormatException e) { System.out.println("Invalid choice.\n"); continue; }

            switch (choice) {
                case 1: SliderGame.runSlider(); break;
                case 2: DABGame.runDAB(); break;
                case 3:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice.\n");
            }
        }
    }



    // Local QUIT check (since Game.quitIfRequested is instance/protected)
    private static boolean exitIfQuit(String s) {
        if (s.equals(Game.RESERVED_QUIT_KEYWORD)) {
            System.out.println("User triggered a quit. Closing game.");
            return true; // exit main
        }
        return false;
    }
}
