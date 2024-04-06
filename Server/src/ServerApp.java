import InputManager.*;
import Executor.Executor;
import DumpManager.*;
import CommandManager.*;
import Server.Server;
import CollectionManager.*;

public class ServerApp {
    private static final int PORT = 45000;
    public static void main(String[] args) {
        var console = new DefaultConsole();
        if (args.length == 0) {
            console.println(
                    "Введите имя загружаемого файла как аргумент командной строки");
            System.exit(1);
        }
        var collectionManager = new CollectionManager();
        var dumpManager = new DumpManager(collectionManager);
        if (!dumpManager.readMoviesFromXmlFile(args[0])) {
            System.exit(1);
        }
        if (PORT<0) { System.out.println("Такого порта не существует"); System.exit(1); }
        var commandManager = new CommandManager();
        commandManager.addCommand("info", new Info(console, collectionManager, commandManager));
        commandManager.addCommand("show", new Show(console, collectionManager, commandManager));
        commandManager.addCommand("get_commands", new GetCommands(commandManager));
        commandManager.addCommand("clear", new Clear(console, collectionManager,  commandManager));
        commandManager.addCommand("add", new Add(console, collectionManager, commandManager, args[0]));
        commandManager.addCommand("print_ascending", new Print_Ascending(console, collectionManager, commandManager));
        commandManager.addCommand("min_by_name", new Min_by_name(console, collectionManager, commandManager));
        commandManager.addCommand("sum_of_budget", new Sum_of_budget(console, collectionManager, commandManager));
        commandManager.addCommand("remove_first", new Remove_First(console, collectionManager, commandManager, args[0]));
        commandManager.addCommand("remove_by_id", new Remove_by_id(console, collectionManager, commandManager, args[0]));
        commandManager.addCommand("update", new Update(console, collectionManager, commandManager, args[0]));
        commandManager.addCommand("add_if_max", new Add_if_max(console, collectionManager, commandManager, args[0]));
        commandManager.addCommand("remove_greater", new Remove_greater(console, collectionManager, commandManager, args[0]));
        commandManager.addCommand("group_by", new Group_by(console, collectionManager, commandManager));
        commandManager.addCommand("randomMovies", new RandomMovies(console, collectionManager, commandManager, args[0]));
        var executor = new Executor(console, commandManager);
        var server = new Server(PORT, executor::executeCommand);
        server.start();
    }
}
