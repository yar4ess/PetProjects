package CommandManager;
import CollectionManager.CollectionManager;
import InputManager.Console;

/**
 * Clear - очищает коллекцию
 */
public class Clear extends Command{
    private final CollectionManager collectionManager;
    public Clear(Console console, CollectionManager collectionManager, CommandManager commandManager){
        super("clear", "очистить коллекцию");
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
                    collectionManager.getCollection().clear();
                return new CommandMessage("Коллекция успешно очищена");
            }
            else {return new CommandMessage(400, "Коллекция уже очищена(");}
        }
        else {
            return new CommandMessage(400,"Неправильное количество аргументов!)");
        }
    }
}
