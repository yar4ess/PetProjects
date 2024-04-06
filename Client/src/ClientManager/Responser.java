package ClientManager;
import CommandManager.CommandMessage;


public class Responser {
    private final Client client;
    public Responser(Client client) {
        this.client= client;
    }
    public CommandMessage send(String s, Object obj) {
        var r=((CommandMessage)client.send(s,obj));
        if (r==null) {
            try{ Thread.sleep(3000); } catch (Exception e){ System.out.print(e); System.exit(1); }
            r = send(s, obj);
        }
        return r;
    }
    public CommandMessage send(String s) {
        return send(s, null);
    }
}