import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

import static java.lang.System.out;

public class Client extends JFrame {
    Socket socket;
    BufferedReader reader;
    PrintWriter writer;
    //Declare Gui Components
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea("Text Area ");
    private  JTextField messageInput = new JTextField();
    Font font = new Font("Roboto",Font.BOLD,25);

    public Client(){
        try {
            out.println("Sending Request to Server");
            socket = new Socket("127.0.0.1",7908);
            out.println("Connected...");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
            //Create Gui
            createGUI();
            handleEvents();

            startReading();
            //startWriting();

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == 10){
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me :" +contentToSend+ "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }

            }
        });
    }

    private void createGUI() {
        this.setTitle("ClientMessenger[END]");
        this.setSize(600,750);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Coding for Components
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        ImageIcon icon = new ImageIcon("E:\\ChatApp\\chatApp\\src\\Client.png");
        Image image = icon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        ImageIcon scaledIcon = new ImageIcon(image);
        heading.setIcon(scaledIcon);
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);

        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        //coding for frame
        this.setLayout(new BorderLayout());
        // Adding components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(messageInput,BorderLayout.SOUTH);
                //messageArea,
        this.add(jScrollPane,BorderLayout.CENTER);


        this.setVisible(true);


    }


    public void startReading() {
        Runnable r1 =() ->{
            try {
                out.println("Reader Started");
                while (true&& !socket.isClosed() ){
                    String message = reader.readLine();
                    if(message.equals("bye")){
                        out.println("Server stopped chatting");
                        JOptionPane.showMessageDialog(this,"Server Terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                   // out.println("Server :"+message);
                    messageArea.append("Server :" +message+ "\n");
                }

            }catch (Exception e) {
                out.println("Connection Closed");
            }
        };
        new Thread(r1).start();
    }
    public void startWriting() {
        Runnable r2 =() ->{
            try {
                out.println("Writer Started");
                while (true && !socket.isClosed()){
                    BufferedReader reader = new BufferedReader((new InputStreamReader(System.in)));
                    String message = reader.readLine();
                    writer.println(message);
                    writer.flush();
                    if(message.equals("bye")){
                        socket.close();
                        break;
                    }

                }out.println("Connection Closed");
            }catch (Exception e){
                out.println("Connection Closed");
            }
        };
        new Thread( r2).start();
    }


    public static void main(String[] args) {
        out.println("This is Server going to start client");
        new Client();

    }
}
