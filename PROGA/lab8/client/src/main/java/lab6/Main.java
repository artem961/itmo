package lab6;


import javafx.stage.Stage;

import javafx.application.Application;
import lab6.ui.AppManager;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        AppManager.init(stage);
        AppManager.getInstance().sceneManager.switchScene("Start");
    }

    public static void main(String... args) {
        launch(args);
    }



    /*
    public static void main(String... args) {
        
        Console console = new StandartConsole();
        CommandManager commandManager = new CommandManager();
        NetworkManager networkManager = null;
        try {
            networkManager = new NetworkManager();
            console.writeln("Подключение с сервером установлено!");
        } catch (NetworkException e) {
            console.writeln(e.getMessage());
            exit(0);
        }
        AuthManager authManager = new AuthManager(console, networkManager);
        Controller controller = new Controller(console, networkManager, commandManager, authManager);

        commandManager.registerCommand(new ExecuteScript(console, controller, commandManager));
        commandManager.registerCommand(new Exit(networkManager));

         authManager.auth();
        controller.run();
    }
     */
}
