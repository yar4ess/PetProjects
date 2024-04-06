package CommandManager;
import java.util.Arrays;
import java.util.ArrayList;
public class GetCommands extends Command {
    private final CommandManager commandManager;
    public GetCommands(CommandManager commandManager) {
        super("get_commands", "вывести список серверных команд");
        this.commandManager = commandManager;
    }
    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    public CommandMessage execution(String arguments, Object obj) {
        if (arguments == null) return new CommandMessage(400, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
        var s = new ArrayList<>(Arrays.asList(
                commandManager.getCommands().values().stream().filter(
                        command-> !command.getName().equals("get_commands") &&
                                !command.getName().equals("is_id_exist") &&
                                !command.getName().equals("save") &&
                                !command.getName().equals("load") &&
                                !command.getName().equals("show")
                ).map(
                        command -> new String[]{
                                (command.getName()+" ").split(" ",2)[0],
                                command.getName(),
                                (command.getName()+" ").split(" ",2)[1].trim().replace(' ', ','),
                                command.getDescription()
                        }
                ).toArray()));
        return new CommandMessage("OK",s);
    }
}