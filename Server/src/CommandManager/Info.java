package CommandManager;
import CollectionManager.CollectionManager;
import InputManager.*;

public class Info extends Command{
    private final CollectionManager collectionManager;
    public Info(Console console, CollectionManager collectionManager, CommandManager commandManager){
        super("info", "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
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
            return new CommandMessage("Данные о коллекции\nТип - LinkedList<Movie>\nДата инициализации - " + collectionManager.getLastInitTime() + "\nКоличество элементов - " + collectionManager.getCollection().size() + "\nДата сохранения - " + collectionManager.getLastSaveTime() + "\nДанные о коллекции успешно выведены");
        } else {
            return new CommandMessage(400,
                    "Неправильное количество аргументов!)");
        }
    }
}
