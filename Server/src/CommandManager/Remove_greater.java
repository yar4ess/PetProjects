package CommandManager;

import CollectionManager.CollectionManager;
import CollectionManager.SortManager;
import InputManager.*;
import Models.Movie;
import java.util.Iterator;

import static CollectionManager.IDManager.GetNewId;

/**
 * Remove_greater - удаляет из коллекции все элементы, превышающие заданный
 */
public class Remove_greater extends Command{
    private final CollectionManager collectionManager;
    private final String path;
    public Remove_greater(Console console, CollectionManager collectionManager, CommandManager commandManager, String path){
        super("remove_greater", " удалить из коллекции все элементы, превышающие заданный");
        commandManager.addCommandList(getName(), getDescription());
        this.collectionManager = collectionManager;
        this.path = path;
    }
    /**
     * Выполнение команды
     * @return Успешность выполнения команды и сообщение об успешности.
     */
    @Override
    public CommandMessage execution(String args, Object obj){
        if ((args == null || args.isEmpty())) {
            Movie a = (Movie) obj;
            a.setId(GetNewId());
            if (a.validate()) {
                collectionManager.add(a);
                SortManager sortManager = new SortManager();
                collectionManager.getCollection().sort(sortManager.SortCollection());
                boolean checker = false;
                int count = 0;
                Iterator<Movie> iterator = collectionManager.getCollection().iterator();
                while (iterator.hasNext()) {
                    Movie b = iterator.next();
                    if (b.equals(a)) {
                        checker = true;
                    }
                    if (checker){
                        iterator.remove();
                        count ++;
                    }
                }
                collectionManager.saveCollection(path);
                return new CommandMessage("Успешно удалено " + (count - 1) + " элементов");
            } else
                return new CommandMessage(400,"Поля movie не валидны! Movie не создан!");
        }
        else{
            return new CommandMessage(400, "Неправильное количество аргументов!)");
        }
    }
}
