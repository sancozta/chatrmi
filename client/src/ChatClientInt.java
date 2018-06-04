import java.rmi.*;
import java.util.*;

public interface ChatClientInt extends Remote {

    //ENVIAR MENSAGEM
    public void sendmessage(String mensagem) throws RemoteException;

    //ENVIAR ARQUIVO
    public boolean sendfile(String filename, byte[] data, int len) throws RemoteException;
    
    //VERIFICAR RECEBIMENTO DE ARQUIVO
    public boolean receivefile(ChatServerInt a, String filename) throws RemoteException;
    
    //RETORNA O NOME DO CLIENTE
    public String getname() throws RemoteException;

    //ATUALIZAR CHAT
    public void updatelist(Vector users) throws RemoteException;
    
}
