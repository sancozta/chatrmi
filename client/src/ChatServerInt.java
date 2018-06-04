import java.rmi.*;
import java.util.*;

public interface ChatServerInt extends Remote {
    
    //REGISTRA USUARIO
    public boolean login(ChatClientInt user) throws RemoteException;
    
    //RETIRAR SESSAO DO USUARIO
    public boolean logout(ChatClientInt user) throws RemoteException;
    
    //VALIDAR NICKNAME
    public boolean nickisvalid(String nickname) throws RemoteException;
    
    //PUBLICA MESSAGEM RECEBIDA AOS USUARIOS CONCTADOS
    public void publish(String msg) throws RemoteException;
    
    //ENVIA MESSAGEM PRIVATA PARA USUARIO UNICO
    public void sendprivate(ChatClientInt usersend, String msg) throws RemoteException;
    
    //ENVIAR ARQUIVO
    public boolean sendfile(String filename, byte[] data, int len) throws RemoteException;

    //VERIFICAR RECEBIMENTO DE ARQUIVO
    public boolean receivefile(ChatClientInt user, String filename) throws RemoteException;
    
    //VERIFICAR CONEXAO E RETORNA USUARIO CONECTADOS
    public Vector getconnected() throws RemoteException;
    
}