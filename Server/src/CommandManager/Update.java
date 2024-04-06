package CommandManager;
import CollectionManager.CollectionManager;
import InputManager.*;
import Models.Movie;
/**
 * Команда Update - обновляет значение элемента коллекции, id которого равен заданному.
 */
public class Update extends Command{
    private final CollectionManager collectionManager;
    private final String path;
    public Update(Console console, CollectionManager collectionManager, CommandManager commandManager, String path) {
        super("update ID {element}", "обновить значение элемента коллекции, id которого равен заданному");
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
        if (args.isEmpty()) return new CommandMessage(400, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
        int id;
        try { id = Integer.parseInt(args.trim()); } catch (NumberFormatException e) { return new CommandMessage(400, "ID не распознан"); }
        if (collectionManager.byId(id) == null || !collectionManager.getCollection().contains(collectionManager.byId(id))) {
            return new CommandMessage(400, "Не существующий ID");
        }
        boolean exist = collectionManager.getCollection().stream()
                .anyMatch(movie -> movie.getId() == id);
        if (exist){
            var a = (Movie) obj;
            if (a.validate()){
                collectionManager.remove(id);
                a.setId(id);
                collectionManager.add(a);
                collectionManager.saveCollection(path);
                return new CommandMessage("Элемент с ID = " + id + " успешно обновлён!");
            } else {
                return new CommandMessage("Поля невалид");
            }
        } else {
            return new CommandMessage("Такого ID не существует, используйте команду add {element}");
        }
    }
}
