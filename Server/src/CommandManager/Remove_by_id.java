package CommandManager;

import CollectionManager.CollectionManager;
import InputManager.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Remove_by_id - удаляет элемент из коллекции по Id
 */
public class Remove_by_id extends Command{
    private final CollectionManager collectionManager;
    private final String path;
    public Remove_by_id(Console console, CollectionManager collectionManager, CommandManager commandManager, String path) {
        super("remove_by_id ID", "удалить элемент с указанным ID");
        commandManager.addCommandList(getName(), getDescription());
        this.collectionManager = collectionManager;
        this.path = path;
    }
    /**
     * Выполнение команды
     * @return Успешность выполнения команды и сообщение об успешности.
     */
    @Override
    public CommandMessage execution(String args, Object obj) {
        if (args == null || args.isEmpty()) {
            return new CommandMessage(400, "Неправильное количество аргументов!)");
        }
        else{
            int id = Integer.parseInt(args.split(" ")[0]);
            if (collectionManager.getCollection().isEmpty()) {return new CommandMessage(400, "Все элементы коллекции уже удалены");}
            AtomicBoolean exist = new AtomicBoolean(false);
            collectionManager.getCollection().stream()
                    .filter(movie -> movie.getId() == id)
                    .findFirst()
                    .ifPresent(movie -> {
                        collectionManager.remove(id);
                        exist.set(true);
                    });
            if (!exist.get()){
                return new CommandMessage(400, "Такого ID не существует");
            } else {
                collectionManager.saveCollection(path);
                return new CommandMessage("Элемент коллекции с данным ID успешно удалён");
            }
        }
    }
}
