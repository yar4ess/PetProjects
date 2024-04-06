package CommandManager;
import CollectionManager.CollectionManager;
import CollectionManager.SortManager;
import InputManager.Console;

/**
 * Min_by_name - вывод элемент, поля name которого минимально
 */
public class Min_by_name extends Command{
    private final CollectionManager collectionManager;
    public Min_by_name(Console console, CollectionManager collectionManager, CommandManager commandManager){
        super("min_by_name", "вывести любой объект из коллекции, значение поля name которого является минимальным");
        commandManager.addCommandList(getName(), getDescription());
        this.collectionManager = collectionManager;
    }
    /**
     * Выполнение команды
     * @return Успешность выполнения команды и сообщение об успешности.
     */
    @Override
    public CommandMessage execution(String args, Object obj){
        if ((args == null || args.isEmpty())){
            if (!collectionManager.getCollection().isEmpty()) {
                SortManager sortManager = new SortManager();
                collectionManager.getCollection().sort(sortManager.SortCollectionByName());
                return new CommandMessage("Минимальный по имени элемент коллекции" +
                        "\nЭлемент коллекции с минимальным именем успешно выведен" +
                        "\n" + collectionManager.getCollection().getFirst());
            }
            else {return new CommandMessage(400, "Коллекция пустая(");}
        }
        else {
            return new CommandMessage(400, "Неправильное количество аргументов!)");
        }
    }
}
