package CommandManager;
import java.util.LinkedHashMap;
import java.util.Map;
public class CommandManager {
    private final Map<String, Command> commands = new LinkedHashMap<>();
    /**
     * Добавляет команду.
     * @param commandName Название команды.
     * @param command Команда.
     */
    public void addCommand(String commandName, Command command) {
        commands.put(commandName, command);
    }

    /**
     * @return Словарь команд.
     */
    public Map<String, Command> getCommands() {
        return commands;
    }

    public void clear() {
        for (var e:commands.keySet().toArray(new String[0]))
            if (e.charAt(0) == '$')
                commands.remove(e);
    }
}