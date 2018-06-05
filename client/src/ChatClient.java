
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

    //RECEBENDO ARQUIVO ENVIADO
    @Override
    public boolean receivefile(String filename, byte[] data, int len) throws RemoteException {

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
            sendmessage("[server] Você recebeu o arquivo " + file.getName() + " !");

        } catch (Exception err) {

            //LOG
            err.printStackTrace();

        }

        return true;

    }

    //ENVIANDO ARQUIVO PARA USUARIOS CONECTADOS
    @Override
    public boolean sendfile(ChatServerInt server, String filename) throws RemoteException {

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

                //SALVANDO O ARQUIVO NO SERVIDOR
                server.receivefile(file.getName(), data, len);

                //ENVIANDO ARQUIVO PARA OS USUARIOS CONECTADOS
                Vector users = server.getconnected();

                for (int i = 0; i < users.size(); i++) {
                    try {

                        //CAPTURANDO PROXIMO USUARIO DA LISTA
                        ChatClientInt user = (ChatClientInt) users.get(i);

                        //ENVIANDO MENSSAGEM PARA USUARIO
                        if (!name.equals(user.getname())) {
                            user.receivefile(file.getName(), data, len);
                        }

                    } catch (Exception err) {

                        //EXCECOES
                        err.printStackTrace();

                        //PUBLICANDO MENSAGEM DE ERRO
                        sendmessage("[server] Erro na tentativa de envio de arquivo !");

                    }
                }

            }

            //PUBLICANDO ENVIO DO ARQUIVO
            server.publish("[" + name + "] Enviei o arquivo " + file.getName() + " !");

        } catch (Exception err) {

            //EXCECOES
            err.printStackTrace();

            //PUBLICANDO MENSAGEM DE ERRO
            sendmessage("[server] Erro na tentativa de envio de arquivo !");

            return false;

        }

        return true;

    }

}
