package CommandManager;

import java.util.HashMap;
import java.util.Map;
public class CommandManager {
    private final Map<String, String> CommandList = new HashMap<>();
    private final Map<String, Command> Commands = new HashMap<>();
    public Map<String, String> getCommandList() {
        return CommandList;
    }
    public Map<String, Command> getCommands() {
        return Commands;
    }
    public void addCommand(String name, Command command){
        Commands.put(name, command);
    }
    public void addCommandList(String name, String description){
        CommandList.put(name, description);
    }

}
