package CommandManager;
import CollectionManager.*;
import InputManager.*;
import Models.Movie;
import static CollectionManager.IDManager.GetNewId;

/**
 * Add - добавляет элемент в коллекцию
 */
public class Add extends Command {
    private final CollectionManager collectionManager;
    private final String path;
    public Add(Console console, CollectionManager collectionManager, CommandManager commandManager, String path) {
        super("add {element}", " добавить новый элемент в коллекцию");
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
        if (!args.isEmpty()) return new CommandMessage(400, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
        Movie d = (Movie) obj;
        if (d != null && d.validate()) {
            d.setId(GetNewId());
            collectionManager.add(d);
            collectionManager.saveCollection(path);
            return new CommandMessage("Фильм успешно добавлен!");
        } else {
            return new CommandMessage("Поля ");
        }
    }
}
