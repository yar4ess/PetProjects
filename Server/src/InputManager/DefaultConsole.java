package InputManager;
public class DefaultConsole implements Console {
    @Override
    public void println(Object o){
        System.out.println(o);
    }
}
