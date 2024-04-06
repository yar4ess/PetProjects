package CommandManager;

import CollectionManager.CollectionManager;
import InputManager.Console;
import Models.Movie;

/**
 * Sum_of_budget - команда, которая сумму всех бюджетов
 */
public class Sum_of_budget extends Command{
    private final CollectionManager collectionManager;

    public Sum_of_budget(Console console, CollectionManager collectionManager, CommandManager commandManager){
        super("sum_of_budget", "вывести сумму значений поля budget для всех элементов коллекции");
        commandManager.addCommandList(getName(), getDescription());
        this.collectionManager = collectionManager;
    }
    /**
     * Выполнение команды
     * @return Успешность выполнения команды и сообщение об успешности.
     */
    public CommandMessage execution(String args, Object object){
        if ((args == null || args.isEmpty())){
            if (!collectionManager.getCollection().isEmpty()) {
                double sum = collectionManager.getCollection().stream()
                        .mapToDouble(Movie::getBudget)
                        .sum();
                return new CommandMessage("Сумма бюджетов всех элементов коллекции = " + sum + '\n' + "Сумма бюджетов успешно выведена");
            }
            else {return new CommandMessage(400, "Коллекция пустая(");}
        }
        else {
            return new CommandMessage("Неправильное количество аргументов!)");
        }
    }
}
