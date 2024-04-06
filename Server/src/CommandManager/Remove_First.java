package CommandManager;

import CollectionManager.CollectionManager;
import InputManager.Console;
import static CollectionManager.IDManager.RemoveId;

/**
 * Remove_First - удаляет первый элемент из коллекции
 */
public class Remove_First extends Command{
    private final CollectionManager collectionManager;
    private final String path;
    public Remove_First(Console console, CollectionManager collectionManager, CommandManager commandManager, String path){
        super("remove_first", "удалить первый элемент из коллекции");
        commandManager.addCommandList(getName(), getDescription());
        this.collectionManager = collectionManager;
        this.path = path;
    }
    @Override
    public CommandMessage execution(String args, Object obj){
        if ((args == null || args.isEmpty())){
            if (!collectionManager.getCollection().isEmpty()) {
                RemoveId(collectionManager.getCollection().getFirst().getId());
                collectionManager.getCollection().removeFirst();
                collectionManager.saveCollection(path);
                return new CommandMessage("Первый элемент коллекции успешно удалён");
            }
            else {return new CommandMessage(400, "Коллекция пустая(");}
        }
        else {
            return new CommandMessage(400,"Неправильное количество аргументов!)");
        }
    }
}
