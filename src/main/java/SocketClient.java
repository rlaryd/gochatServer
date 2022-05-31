import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class SocketClient {

    HashMap<String, DataOutputStream> clients;
    private ServerSocket ServerSocket = null;

    public static void main(String[] args) {
        new SocketClient().start();
    }

    public SocketClient() {
        clients = new HashMap<String, DataOutputStream>();
        Collections.synchronizedMap(clients);
    }

    private void start() {
        int port = 8888;
        Socket socket = null;

        try {
            ServerSocket = new ServerSocket(port);
            System.out.println("접속 대기중");
            while (true) {
                socket = ServerSocket.accept();
                InetAddress ip = socket.getInetAddress();
                System.out.println("ip : " + ip + " connected");
                new MultiThread(socket).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    class MultiThread extends Thread {

        Socket socket = null;

        String key = null;
        String id = null;
        String room = null;

        DataInputStream input;
        DataOutputStream output;

        public MultiThread(Socket socket) {
            this.socket = socket;
            try {
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
            }
        }
        public void run() {
            try {
                key = input.readUTF();
                clients.put(key, output);
                //key_cut = key.substring(key.lastIndexOf("////")+1);
                String[] key_array = key.split("/",2);
                //System.out.println(Arrays.toString(key_array));
                id = key_array[0];
                room = key_array[1];
                System.out.println("받은 키값 : " + key);
                System.out.println("id : " + id + "가 " + room + "에 접속하였습니다.");

                while (input != null) {
                    try {
                        // 받아온 string 형태의 json
                        String temp = input.readUTF();
                        System.out.println("받아온 json : " + temp);
                        // String 을 Json 으로 변환
                        JSONParser parser = new JSONParser();
                        JSONObject jsonObject = (JSONObject) parser.parse(temp);

                        String get_id = (String) jsonObject.get("id");
                        System.out.println("json 받아오고 그 내부의 아이디 추출 : " + get_id);

                        //
                        sendMsg(temp, room);

                    } catch (IOException e) {
                        sendMsg("No message to ", room);
                        break;
                    }
                }

            } catch (Exception e) {
                System.out.println(e);
                //e.printStackTrace();
            }


        }
    }

    private void sendMsg(String msg, String room) {

        ArrayList<String> key_array = new ArrayList<>();
        DataOutputStream output = null;
        OutputStream dos = null;

        Iterator<String> it = clients.keySet().iterator();

        for (Map.Entry<String, DataOutputStream> entry : clients.entrySet()) {
            if (entry.getKey().contains("/"+room)) {
                String the_key = entry.getKey();
                key_array.add(the_key);
            }
        }
        for (String s : key_array) {
            dos = clients.get(s);
            System.out.println("보낸 json : " + msg);
            try {
                output = new DataOutputStream(dos);
                output.writeUTF(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    } // sendMsg()


}
