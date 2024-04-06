import ClientManager.Client;
import ClientManager.Responser;
import CommandManager.*;
import InputManager.DefaultConsole;
import Executor.Executor;

public class ClientApp {
    private static final int PORT = 45000;
    public static void main(String[] args) {
        var console = new DefaultConsole();
        Client client = new Client("localhost", PORT);
        client.start();
        var rpns = new Responser(client);
        var commandManager = new CommandManager() {{
            addCommand("help", new Help(console, this));
            addCommand("execute_script", new ExecuteScript(console));
            addCommand("exit", new Exit(console));
            addCommand("show", new Show(console, rpns));
        }};
        new Executor(console, commandManager, rpns).interactiveMode();
    }
}