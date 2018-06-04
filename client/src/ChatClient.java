import java.util.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ChatClient extends UnicastRemoteObject implements ChatClientInt {
    
    //NOME DO USUARIO
    private String name;
    
    //INTERFACE DO CHAT
    private StartClient gui;

    //CONSTRUTOR
    public ChatClient(String client) throws RemoteException {
        this.name = client;
    }

    //ENVIANDO MENSAGEM
    @Override
    public void sendmessage(String msg) throws RemoteException {
        this.gui.writepanel(msg);
    }
    
    //ATUALIZAR PESSOAS ONLINE
    @Override
    public void updatelist(Vector users) throws RemoteException {
        this.gui.updatelist(users);
    }
    
    //RETORNAR NOME DO USUARIO LOGADO
    @Override
    public String getname() throws RemoteException {
        return name;
    }
    
    //REGISTRANDO INTERFACE
    public void setgui(StartClient gui) {
        this.gui = gui;
    }
    
    //ENVIANDO ARQUIVO
    @Override
    public boolean sendfile(String filename, byte[] data, int len) throws RemoteException {

        try {

            //INSTANCIANDO ARQUIVO
            File file = new File(filename);

            //CRIAR O ARQUIVO
            file.createNewFile();

            //CRIAR UM FLUXO DE GRAVACAO OU LEITURA DE DADOS
            FileOutputStream out = new FileOutputStream(file, true);

            //REALIZA A GRAVACAO DOS DADOS NO ARQUIVO
            out.write(data, 0, len);

            //GARANTIDO A GRAVACAO
            out.flush();

            //FECHA FLUXO DE GRAVACAO
            out.close();

            //LOG DO SERVIDOR
            System.out.println("[server] Um arquivo foi enviado !");

        } catch (Exception err) {

            //LOG
            err.printStackTrace();

        }

        return true;

    }

    //ENVIANDO ARQUIVO PARA USUARIOS
    @Override
    public boolean receivefile(ChatServerInt user, String filename) throws RemoteException {

        try {

            //INSTANCIA UM ARQUIVO
            File file = new File(filename);

            //ABRE UM ARQUIVO PARA LEITURA DE DADOS
            FileInputStream in = new FileInputStream(file);

            //DETERMINANDO LIMITE DE LEITURA DO ARQUIVO
            byte[] data = new byte[1024 * 1024];

            //LENDO TAMANHO DO ARQUIVO
            int len = in.read(data);

            if (len > 0) {
                //ENVIANDO ARQUIVO
                user.sendfile(file.getName(), data, len);
            }

            //PUBLICANDO ENVIO DO ARQUIVO
            user.publish("[" + name + "] Enviou um arquivo ! Arquivo: " + filename);

        } catch (Exception err) {

            //PUBLICANDO MENSAGEM DE ERRO
            user.publish("[server] Erro ao enviar arquivo para o servidor !");

            return false;

        }

        return true;

    }
}
