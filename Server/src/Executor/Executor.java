package Executor;
import CommandManager.*;
import InputManager.*;

public class Executor {
    private final CommandManager commandManager;
    public Executor(Console console, CommandManager commandManager) {
        this.commandManager = commandManager;
    }
    private CommandMessage launchCommand(String[] userCommand, Object obj) {
        if (userCommand[0].isEmpty()) return new CommandMessage("ок");
        var command = commandManager.getCommands().get(userCommand[0]);
        if (command == null)
            return new CommandMessage(400, "Команда '" + userCommand[0] + "' не найдена. Наберите 'help' для справки");

        var resp = command.execution(userCommand[1], obj);
        if (resp == null) return new CommandMessage(503, "503");
        return resp;
    }

    public Object executeCommand(String s, Object obj) {
        String[] userCommand;
        userCommand = (s.replace('\n',' ').replace('\r',' ') + " ").split(" ", 2);
        userCommand[1] = userCommand[1].trim();
        System.out.println("$ "+userCommand[0]);
        return launchCommand(userCommand, obj);
    }
}