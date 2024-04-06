package CommandManager;

import CollectionManager.CollectionManager;
import CollectionManager.SortManager;
import InputManager.Console;
/**
 * Print_Ascending - выводит элементы коллекции в порядке возрастания
 */
public class Print_Ascending extends Command{
    private final CollectionManager collectionManager;
    public Print_Ascending(Console console, CollectionManager collectionManager, CommandManager commandManager){
        super("print_ascending", "вывести элементы коллекции в порядке возрастания");
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
                collectionManager.getCollection().sort(sortManager.SortCollection());
                return new CommandMessage("Отсортированные элементы коллекции успешно выведены" + collectionManager.getCollection());
            }
            else {return new CommandMessage(400, "Коллекция пустая(");}
        }
        else {
            return new CommandMessage(400,"Неправильное количество аргументов!)");
        }
    }
}
