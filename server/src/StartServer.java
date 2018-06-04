
import java.rmi.*;
import java.rmi.registry.LocateRegistry;

public class StartServer {

    //IP ONDE O SERVIDOR E DISPONIBILIZADO
    static String servername = "127.0.0.1";

    //PORTA ONDE O SERVIDOR E DISPONIBILIZADO
    static int serverport = 1099;

    public static void main(String[] args) {

        try {

            //REGISTRANDO PORTA DO SERVIDOR
            LocateRegistry.createRegistry(serverport);

            //CRIANDO INSTANCIA DO SERVIDOR
            ChatServerInt server = new ChatServer();

            //NOMENTADO O SERVIDOR
            Naming.rebind("rmi://" + servername + "/chat", server);

            //RETORNADO MENSAGEM DE ATIVACAO
            System.out.println("[server] Servidor Rodando !");

            //RETORNANDO ENDERECO DO SERVIDOR
            System.out.println("[server] Servidor Disponivel no Ip " + servername);

        } catch (Exception err) {

            //RETORNADO MENSAGEM DE ERRO
            System.out.println("# Erro ao subir servidor !");
            System.out.println("# " + err);

        }

    }

}
