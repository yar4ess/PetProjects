package CommandManager;

import ClientManager.Responser;
import InputManager.Console;
import InputManager.Enter;
import Models.Movie;

import java.util.HashMap;
public class DefaultCommand extends Command {
    private interface ArgExecuter { boolean execute(String s, Console cons, Responser rpns); }
    private interface AskExecuter { Object execute(Console cons); }

    private final Console console;
    private final Responser rpns;
    private final String args;
    private static final HashMap<String, ArgExecuter> argsMap = new HashMap<>();
    private static final HashMap<String, AskExecuter> enterMap = new HashMap<>();

    static {
        argsMap.put("field", DefaultCommand::argsField);
        argsMap.put("N", DefaultCommand::argsN);
        argsMap.put("ID", DefaultCommand::argsID);
        enterMap.put("{element}", DefaultCommand::enterMovie);
    }

    public DefaultCommand(String[] command, Console console, Responser rpns) {
        super(command[1], command[3]);
        args = command[2];
        this.console = console;
        this.rpns = rpns;


    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
//    @Override
    public boolean execute(String[] args) {
        var argCommandNotUsed = !args[1].isEmpty();
        Object sendObj = null;
        if (!this.args.isEmpty())
            for (var arg: this.args.split(","))
                if (argsMap.get(arg) != null) {
                    if (!argsMap.get(arg).execute(args[1].trim(), console, rpns))
                        return false;
                    argCommandNotUsed=false;
                } else if (enterMap.get(arg) != null) {
                    if (argCommandNotUsed) break;
                    sendObj = enterMap.get(arg).execute(console);
                } else
                    console.errorMessage("Неизвестный тип аргумента: "+arg);
        if (argCommandNotUsed) {
            console.println("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
            return false;
        }
        var res = rpns.send(args[0]+" "+args[1], sendObj);
        console.println(res.getMessage());
        return res.getExitCode()<300;
    }

    private static boolean argsField(String s, Console cons, Responser rpns) {
        return true;
    }

    private static boolean argsN(String s, Console cons, Responser rpns) {
        var N = -1;
        try { N = Integer.parseInt(s); } catch (NumberFormatException e) { cons.println("N не распознан"); return false; }
        if (N < 1) { cons.println("N < 1"); return false; }
        return true;
    }

    private static boolean argsID(String s, Console cons, Responser rpns) {
        return true;
    }

    public static Object enterMovie(Console cons) {
        try {
            Movie d = Enter.enterMovie(cons);

            if (d != null && d.validate()) {
                return d;
            } else {
                cons.println("Некорректные данные");
                return null;
            }
        } catch (Enter.EnterBreak e) {
            cons.println("Останов");
            return null;
        }
    }
}