import java.rmi.*;
import java.util.*;

public interface ChatServerInt extends Remote {
    
    //REGISTRA SESSAO DO USUARIO
    public boolean login(ChatClientInt user) throws RemoteException;
    
    //RETIRAR SESSAO DO USUARIO
    public boolean logout(ChatClientInt user) throws RemoteException;
    
    //VALIDAR NICKNAME
    public boolean nickisvalid(String nickname) throws RemoteException;
    
    //PUBLICA MESSAGEM RECEBIDA AOS USUARIOS CONCTADOS
    public void publish(String msg) throws RemoteException;
    
    //ENVIA MESSAGEM PRIVATA PARA USUARIO UNICO
    public void sendprivate(ChatClientInt usersend, String msg) throws RemoteException;
    
    //VERIFICAR CONEXAO E RETORNA USUARIO CONECTADOS
    public Vector getconnected() throws RemoteException;
    
    //RECEBER ARQUIVO DE USUARIO
    public boolean receivefile(String filename, byte[] data, int len) throws RemoteException;

}