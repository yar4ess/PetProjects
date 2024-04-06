package CommandManager;
import InputManager.*;

import ClientManager.*;
import Models.Movie;

import java.util.stream.Collectors;
import java.util.LinkedList;

public class Show extends Command {
    private final Console console;
    private final Responser rpns;

    public Show(Console console, Responser tcpManager) {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.console = console;
        this.rpns = tcpManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean execute(String[] arguments) {
        if (!arguments[1].isEmpty()) {
            console.println("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
            return false;
        }
        var s=((LinkedList<Movie>) rpns.send("show").getMessageObj()).stream().map(x->x.toString()).collect(Collectors.joining("\n"));
        if (s.isEmpty())
            console.println("Коллекция пуста!");
        else
            console.println(s);
        return true;
    }
}