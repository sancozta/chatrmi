import java.rmi.*;
import java.util.*;

public interface ChatClientInt extends Remote {

    //ENVIAR MENSAGEM
    public void sendmessage(String mensagem) throws RemoteException;
    
    //RETORNA O NOME DO CLIENTE
    public String getname() throws RemoteException;
    
    //ATUALIZAR CHAT
    public void updatelist(Vector users) throws RemoteException;
    
    //RECEBE UM ARQUIVO ENVIADO POR OUTRO USUARIO
    public boolean receivefile(String filename, byte[] data, int len) throws RemoteException;
    
    //ENVIAR ARQUIVO PARA USUARIOS CONECTADOS A UM SERVIDOR
    public boolean sendfile(ChatServerInt server, String filename) throws RemoteException;

}
