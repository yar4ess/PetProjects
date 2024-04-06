package CommandManager;

import CollectionManager.CollectionManager;
import CollectionManager.SortManager;
import InputManager.*;
import Models.Movie;

import java.util.Collections;

import static CollectionManager.IDManager.GetNewId;

/**
 * Add_if_max - добавляет элемент в коллекцию, если он больше чем наибольший элемент коллекции
 */
public class Add_if_max extends Command{
    private final Console console;
    private final CollectionManager collectionManager;
    private final String path;
    public Add_if_max(Console console, CollectionManager collectionManager, CommandManager commandManager, String path){
        super("add_if_max {element}", "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции");
        commandManager.addCommandList(getName(), getDescription());
        this.collectionManager = collectionManager;
        this.console = console;
        this.path = path;
    }
    /**
     * Выполнение команды
     * @return Успешность выполнения команды и сообщение об успешности.
     */
    @Override
    public CommandMessage execution(String args, Object obj){
        if ((args == null || args.isEmpty())) {
            console.println("* Создание потенциально нового Movie:");
            Movie a = (Movie) obj;
            if (a.validate()) {
                a.setId(GetNewId());
                collectionManager.add(a);
                SortManager sortManager = new SortManager();
                collectionManager.getCollection().sort(sortManager.SortCollection());
                if (collectionManager.getCollection().getLast().equals(a)){
                    return new CommandMessage("Элемент успешно добавлен в коллекцию");
                } else {
                    collectionManager.remove(a.getId());
                    collectionManager.saveCollection(path);
                    return new CommandMessage(400, "Элемент не подошёл под условия");
                }
            } else
                return new CommandMessage(400, "Поля movie не валидны! Movie не создан!");
        }
        else{
            return new CommandMessage(400,"Неправильное количество аргументов!)");
        }
    }
}
