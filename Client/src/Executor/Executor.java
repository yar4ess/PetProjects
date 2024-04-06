package Executor;
import ClientManager.Responser;
import CommandManager.*;
import InputManager.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
public class Executor {

    public enum ExitCode {
        OK,
        ERROR,
        EXIT
    }

    private final Console console;
    private final CommandManager commandManager;
    private final Responser rpns;
    private final List<String> scriptStack = new ArrayList<>();
    private int lengthRecursion = -1;

    public Executor(Console console, CommandManager commandManager, Responser rpns) {
        this.console = console;
        this.commandManager = commandManager;
        this.rpns = rpns;
    }

    /**
     * Интерактивный режим
     */
    public void interactiveMode() {
        try {
            ExitCode commandStatus;
            String[] userCommand = {"", ""};

            do {
                console.prompt();
                userCommand = (console.readln().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                commandStatus = launchCommand(userCommand);
            } while (commandStatus != ExitCode.EXIT);

        } catch (NoSuchElementException exception) {
            console.errorMessage("Пользовательский ввод не обнаружен!");
        } catch (IllegalStateException exception) {
            console.errorMessage("Непредвиденная ошибка!");
        }
    }


    /**
     * Режим для запуска скрипта.
     * @param argument Аргумент скрипта
     * @return Код завершения.
     */
    public ExitCode scriptMode(String argument) {
        String[] userCommand = {"", ""};
        ExitCode commandStatus;
        scriptStack.add(argument);
        if (!new File(argument).exists()) {
            console.errorMessage("Файл не существет!");
            return ExitCode.ERROR;
        }
        if (!Files.isReadable(Paths.get(argument))) {
            console.errorMessage("Прав для чтения нет!");
            return ExitCode.ERROR;
        }
        try (Scanner scriptScanner = new Scanner(new File(argument))) {
            if (!scriptScanner.hasNext()) throw new NoSuchElementException();
            console.selectFileScanner(scriptScanner);

            do {
                userCommand = (console.readln().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                while (console.isCanReadln() && userCommand[0].isEmpty()) {
                    userCommand = (console.readln().trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                }
                console.println(console.getPrompt() + String.join(" ", userCommand));
                var needLaunch = true;
                if (userCommand[0].equals("execute_script")) {
                    var recStart = -1;
                    var i = 0;
                    for (String script : scriptStack) {
                        i++;
                        if (userCommand[1].equals(script)) {
                            if (recStart < 0) recStart = i;
                            if (lengthRecursion < 0) {
                                console.selectConsoleScanner();
                                console.println("Была замечена рекурсия! Введите максимальную глубину рекурсии (0..500)");
                                while (lengthRecursion < 0 || lengthRecursion > 500) {
                                    try { console.print("> "); lengthRecursion = Integer.parseInt(console.readln().trim()); } catch (NumberFormatException e) { console.println("длина не распознан"); }
                                }
                                console.selectFileScanner(scriptScanner);
                            }
                            if (i > recStart + lengthRecursion || i > 500)
                                needLaunch = false;
                        }
                    }
                }
                commandStatus = needLaunch ? launchCommand(userCommand) : ExitCode.OK;
            } while (commandStatus == ExitCode.OK && console.isCanReadln());

            console.selectConsoleScanner();
            if (commandStatus == ExitCode.ERROR && !(userCommand[0].equals("execute_script") && !userCommand[1].isEmpty())) {
                console.println("Проверьте скрипт на корректность введенных данных!");
            }

            return commandStatus;
        } catch (FileNotFoundException exception) {
            console.errorMessage("Файл со скриптом не найден!");
        } catch (NoSuchElementException exception) {
            console.errorMessage("Файл со скриптом пуст!");
        } catch (IllegalStateException exception) {
            console.errorMessage("Непредвиденная ошибка!");
            System.exit(0);
        } finally {
            scriptStack.remove(scriptStack.size() - 1);
        }
        return ExitCode.ERROR;
    }

    /**
     * Launchs the command.
     * @param userCommand Команда для запуска
     * @return Код завершения.
     */
    private ExitCode launchCommand(String[] userCommand) {
        commandManager.clear();
        for (var e:(ArrayList<String[]>)rpns.send("get_commands").getMessageObj())
            commandManager.addCommand("$"+e[0], new DefaultCommand(e, console, rpns));

        if (userCommand[0].isEmpty()) return ExitCode.OK;
        var command = commandManager.getCommands().get(userCommand[0]);
        if (command == null) command = commandManager.getCommands().get('$'+userCommand[0]);

        if (command == null) {
            console.errorMessage("Команда '" + userCommand[0] + "' не найдена. Наберите 'help' для справки");
            return ExitCode.ERROR;
        }
        switch (userCommand[0]) {
            case "exit" -> {
                if (!commandManager.getCommands().get("exit").execute(userCommand)) return ExitCode.ERROR;
                else return ExitCode.EXIT;
            }
            case "execute_script" -> {
                if (!commandManager.getCommands().get("execute_script").execute(userCommand)) return ExitCode.ERROR;
                else return scriptMode(userCommand[1]);
            }
            default -> { if (!command.execute(userCommand)) return ExitCode.ERROR; }
        };

        return ExitCode.OK;
    }
}
