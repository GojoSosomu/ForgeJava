import presentation.outside.launcher.ConsoleLauncher;
import presentation.outside.launcher.LaunchPlayer;
import presentation.outside.launcher.SwingLauncher;

public class App {
    public static void main(String[] args) {
        if(args.length == 0) {
            printHelperAndExits();
        }

        var options = args[0].toLowerCase();

        switch (options) {
            case "--no-gui-console" -> LaunchPlayer.play(new ConsoleLauncher());
            case "--yes-gui-swing" -> LaunchPlayer.play(new SwingLauncher());
            default -> {
                System.out.println(
                    "Sorry, either wrong option or not currently supporterd"
                );
            }
        }
    }

    private static void printHelperAndExits() {
        System.out.println(
            "To run the program, here's the commands that you can copy\n" +
            ".\\gradlew :app:run --args=\"--no-gui-console\"       -> Console UI\n" +
            ".\\gradlew :app:run --args=\"--yes-gui-swing\"        -> Swing UI\n" +
            ".\\gradlew :app:run --args=\"--yes-gui-fx\"           -> JavaFX UI\n" +
            "please restart the program again with an option"
        );
        System.exit(0);
    }
}