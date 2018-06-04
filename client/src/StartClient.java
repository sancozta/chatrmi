
import java.util.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class StartClient {

    //INSTANCIA DO CLIENTE
    private ChatClient client;

    //INSTANCIA PARA COMUNICACAO COM O SERVIDOR
    private ChatServerInt server;

    //FRAME DE SAIDA
    public JTextArea textarea;

    //INPUTS DE ENTRADA
    public JTextField sendbox, ipconect, nameconect;

    //BUTTOM DE CONEXAO
    public JButton bconnect, bsendbox, bsendfile, breceivefile;

    //COMPONETE PARA LISTAR USUARIOS LOGADOS
    public JList listconnected;

    //FRAME
    public JFrame frame;

    //INCIANDO CLASSE
    public static void main(String[] args) {

        //INSTANCIANDO
        StartClient client = new StartClient();

    }

    //ENVIAR ALERTA PARA USUARIO
    public void notication(String msg) {

        //CRIAR POP DE NOTICACAO
        JOptionPane.showMessageDialog(frame, msg);

    }

    //REALIZA A CONEXAO DO CLIENT COM O SERVIDOR
    public void connect() {

        if (bconnect.getText().equals("Conectar")) {

            if (nameconect.getText().length() == 0) {
                notication("É necessário informar seu ninckname !");
                return;
            }

            if (ipconect.getText().length() == 0) {
                notication("É necessário informar o ip do servidor !");
                return;
            }

            try {

                //INSTANCIAR USUARIO
                client = new ChatClient(nameconect.getText().toLowerCase());

                //REGISTRANDO INSTANCIA DE GUI
                client.setgui(this);

                //ENCOTRANDO O SERVIDOR
                server = (ChatServerInt) Naming.lookup("rmi://" + ipconect.getText().toLowerCase() + "/chat");

                //REALIZANDO A CONEXAO COM O SERVIDOR
                server.login(client);

                //ATUALIZAR USUARIOS CONECTADOS
                updatelist(server.getconnected());

                //MUDAR FUNCAO DO BUTTON
                bconnect.setText("Desconectar");

                //BLOQUEANDO INPUT
                nameconect.setEditable(false);

                //BLOQUEANDO INPUT
                ipconect.setEditable(false);

            } catch (Exception err) {

                //LOG
                err.printStackTrace();

                //NOTIFICACAO PARA O USUARIO
                notication("Erro ao realizar conexão !");

            }

        } else {

            try {

                //RETIRAR SESSAO DO USUARIO
                server.logout(client);

            } catch (Exception err) {

                //LOG
                err.printStackTrace();

            }

            bconnect.setText("Conectar");

            //DESBLOQUEANDO  INPUT
            nameconect.setEditable(true);

            //DESBLOQUEANDO INPUT
            ipconect.setEditable(true);

        }

    }

    public void sendtext() {

        if (bconnect.getText().equals("Conectar")) {
            notication("Você deve se conectar antes !");
            return;
        }

        //COLOCANDO MSG NO PADRAO DO CHAT
        String msg = "[" + nameconect.getText() + "] " + sendbox.getText();

        sendbox.setText("");

        try {

            //PUBLICA MSG A TODOS
            server.publish(msg);

        } catch (Exception err) {

            //LOG
            err.printStackTrace();

            //NOTIFICACAO PARA O USUARIO
            notication("A Mensagem não foi enviada !");

        }

    }

    //REGISTRADO DE HISTORICO DE MENSAGENS NO PAINEL DO CHAT
    public void writepanel(String msg) {

        if (textarea.getText().equals("")) {

            //INICIANDO ESCRITA NO PANEL
            textarea.setText(msg);

        } else {

            //ADICIONANDO LINHAS AO PANEL
            textarea.setText(textarea.getText() + "\n" + msg);

        }

    }

    public void updatelist(Vector vusers) {

        //INSTANCIA DE LISTA
        DefaultListModel list = new DefaultListModel();

        if (vusers != null) {

            //PERCORENDO LISTA DE USUARIOS CONECTADOS
            for (int i = 0; i < vusers.size(); i++) {

                try {

                    //CAPTURANDO NICKNAME 
                    String nickname = ((ChatClientInt) vusers.get(i)).getname();

                    //ADICIONADO NICKNAME A LISTA
                    list.addElement(nickname);

                } catch (Exception err) {

                    //EXCECOES
                    err.printStackTrace();

                }

            }

        }

        //ATUALIZANDO LISTA NA GUI
        listconnected.setModel(list);

    }

    //CONSTRUTOR
    public StartClient() {

        //TITULO DA JANELA
        frame = new JFrame("Chat - Sistemas Distribuidos");

        //CRIANDO PONTOS DO LAYOUT
        JPanel main = new JPanel();
        JPanel top = new JPanel();
        JPanel cent = new JPanel();
        JPanel bottom = new JPanel();

        //BUTTONS
        bconnect = new JButton("Conectar");
        bsendbox = new JButton("Enviar Mensagem");
        bsendfile = new JButton("Baixar Arquivo");
        breceivefile = new JButton("Enviar Arquivo");

        //TEXTS
        ipconect = new JTextField();
        sendbox = new JTextField();
        nameconect = new JTextField();
        textarea = new JTextArea();
        listconnected = new JList();

        //DEFININDO FORMATO DO LAYOUT
        main.setLayout(new BorderLayout(5, 5));
        top.setLayout(new GridLayout(1, 0, 5, 5));
        bottom.setLayout(new GridLayout(1, 0, 5, 5));

        //cn.setLayout(new BorderLayout(5, 5));
        cent.setLayout(new GridBagLayout());
        GridBagConstraints centbag = new GridBagConstraints();

        //LAYOUT DE CIMA
        top.add(new JLabel("Nickname: "));
        top.add(nameconect);
        top.add(new JLabel("Endereço do servidor: "));
        top.add(ipconect);
        top.add(bconnect);

        centbag.fill = GridBagConstraints.BOTH;
        centbag.weightx = 1.0;
        cent.add(new JLabel(" "), centbag);
        cent.add(new JLabel(" "), centbag);
        cent.add(new JLabel(" "), centbag);

        centbag.gridwidth = GridBagConstraints.REMAINDER;
        cent.add(new JLabel(" "), centbag);

        centbag.gridwidth = GridBagConstraints.RELATIVE;
        centbag.weightx = 0.0;
        centbag.weighty = 1.0;
        centbag.insets = new Insets(0, 0, 0, 5);

        //LAYOUT DO CENTRO
        cent.add(new JScrollPane(textarea), centbag);

        centbag.insets = new Insets(0, 0, 0, 0);
        centbag.gridwidth = GridBagConstraints.REMAINDER;

        //LAYOUY DA DIREITA
        cent.add(listconnected, centbag);

        centbag.gridwidth = GridBagConstraints.REMAINDER;
        centbag.weighty = 0.0;
        cent.add(new JLabel(" "), centbag);

        //LAYOUT DE BAIXO
        bottom.add(sendbox);
        bottom.add(bsendbox);
        bottom.add(bsendfile);
        bottom.add(breceivefile);

        //LAYOUT RAIZ
        main.add(top, BorderLayout.NORTH);
        main.add(cent, BorderLayout.CENTER);
        main.add(bottom, BorderLayout.SOUTH);
        main.setBorder(new EmptyBorder(10, 10, 10, 10));

        //OUVINTE DO BOTAO DE CONEXAO
        bconnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connect();
            }
        });

        //OUVINTE DO BOTAO DE ENVIO DE MENSAGEM
        bsendbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendtext();
            }
        });

        //OUVINTE DO BOTAO DE BAIXAR ARQUIVO
        bsendfile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String filename = sendbox.getText();
                    server.receivefile(client, filename);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //OUVINTE DO BOTAO DE ENVIAR ARQUIVO
        breceivefile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    client.receivefile(server, sendbox.getText());
                } catch (RemoteException err) {
                    err.printStackTrace();
                }
            }
        });

        //OUVINTE DO SENDBOX DE MENSAGENS
        sendbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendtext();
            }
        });

        //DEFINIDO PROPRIEDADES DA JANELA
        frame.setContentPane(main);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setVisible(true);

    }
}
