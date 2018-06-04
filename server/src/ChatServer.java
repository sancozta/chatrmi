
import java.util.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ChatServer extends UnicastRemoteObject implements ChatServerInt {

    //VETOR DE USUARIOS CONECTADOS
    private Vector users = new Vector();

    //ARQUIVO A SER ENVIADO
    private String file = "";

    //CONSTRUTOR DA CLASS
    public ChatServer() throws RemoteException {
    }

    //REGISTRAR USUARIO NA CONEXAO
    @Override
    public boolean login(ChatClientInt user) throws RemoteException {

        //LOG DO SERVIDOR
        System.out.println("[server] O usuario " + user.getname() + " se conectou !");

        //AVISAR O USUARIO
        user.sendmessage("[server] Conectado com sucesso !");

        //AVISAR OS OUTROS USUARIOS
        publish("[server] O usuario " + user.getname() + " acabou de se conectar !");

        //REGISTRAR USUARIO
        users.add(user);

        //ATUALIZAR LISTA DE USUARIOS NO CHAT
        publishusers();

        return true;

    }

    //VALIDAR NICKNAME
    public boolean nickisvalid(String nickname) {

        boolean isvalid = true;

        for (int i = 0; i < users.size(); i++) {
            try {

                //CAPTURANDO PROXIMO USUARIO DA LISTA
                ChatClientInt user = (ChatClientInt) users.get(i);

                //VERIFICANDO IGUALDADE
                if (nickname.equals(user.getname())) {
                    isvalid = false;
                    break;
                }

            } catch (Exception e) {

                //LOG
                System.out.println("[server] Erro verificar nickname !");

            }
        }

        return isvalid;

    }

    //RETIRAR SESSAO DO USUARIO
    @Override
    public boolean logout(ChatClientInt user) throws RemoteException {

        //LOG DO SERVIDOR
        System.out.println("[server] O usuario " + user.getname() + " se desconectou !");

        //AVISAR O USUARIO
        user.sendmessage("[server] Desconectado com sucesso !");

        //AVISAR OS OUTROS USUARIOS
        publish("[server] O usuario " + user.getname() + " acabou de se desconectar !");

        //REGISTRAR USUARIO
        users.remove(user);

        //ATUALIZAR LISTA DE USUARIOS NO CHAT
        publishusers();

        return true;

    }

    //ATUALIZAR LISTA DE USUARIOS ONLINE DOS CLIENTES
    public void publishusers() {

        for (int i = 0; i < users.size(); i++) {
            try {

                //CAPTURANDO PROXIMO USUARIO DA LISTA
                ChatClientInt user = (ChatClientInt) users.get(i);

                //ENVIANDO MENSSAGEM PARA USUARIO
                user.updatelist(users);

            } catch (Exception e) {

                //LOG
                System.out.println("[server] Erro ao atualizar usuarios !");

            }
        }

    }

    //PUBLICAR MESSAGEM PARA TODOS OS USUARIOS CONECTADOS
    @Override
    public void publish(String msg) throws RemoteException {

        for (int i = 0; i < users.size(); i++) {
            try {

                //CAPTURANDO PROXIMO USUARIO DA LISTA
                ChatClientInt user = (ChatClientInt) users.get(i);

                //ENVIANDO MENSSAGEM PARA USUARIO
                user.sendmessage(msg);

            } catch (Exception e) {

                //LOG
                System.out.println("[server] Erro ao enviar messagem de cliente !");

            }
        }

    }

    //ENVIAR MENSAGEM PRIVADA PARA USUARIO
    @Override
    public void sendprivate(ChatClientInt usersend, String msg) {

        String nick = msg.substring(0, msg.indexOf(":"));
        System.out.println("[server] Usuario " + nick + " identificado !");

        for (int i = 0; i < users.size(); i++) {
            try {

                //CAPTURANDO PROXIMO USUARIO DA LISTA
                ChatClientInt user = (ChatClientInt) users.get(i);

                //ENVIANDO MENSSAGEM PARA USUARIO
                if (nick.equals(user.getname())) {
                    usersend.sendmessage("["+usersend.getname()+"] "+msg.substring(msg.indexOf(":")+1));
                    user.sendmessage("["+usersend.getname()+"] "+msg.substring(msg.indexOf(":")+1));
                }

            } catch (Exception e) {

                //LOG
                System.out.println("[server] Erro ao enviar messagem de cliente !");

            }
        }

    }

    //ENVIANDO ARQUIVO PUBLICAMENTE
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

    //RETORNA USUARIOS CONECTADOS
    @Override
    public Vector getconnected() throws RemoteException {
        return users;
    }

    //FAZER DOWNLOAD DE ARQUIVO
    @Override
    public boolean receivefile(ChatClientInt user, String filename) throws RemoteException {

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
                //SALVANDO ARQUIVO PARA O USUARIO
                user.sendfile(file.getName(), data, len);
            }

            //REGISTRANDO MESSAGE PARA O USUARIO
            user.sendmessage("[message] O arquivo " + filename + " foi salvo !");

        } catch (Exception err) {

            //LOG DO SERVIDOR
            System.out.println("[server] Erro ao retornar arquivo a usuario !");

            //ENVIANDO MENSSAGEM AO USUARIO
            user.sendmessage("[notice] Erro ao recuperar arquivo !");

            return false;

        }

        return true;

    }

}
