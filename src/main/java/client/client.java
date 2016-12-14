package client;

/**
 * Created by Ксения on 12.12.2016.
 */


        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.*;
        import java.net.Socket;
        import java.util.Random;

public class client {
    public static void main(String[] args) throws IOException, JSONException {
        Socket s = new Socket("127.0.0.1", 1201);
        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String msgin = "", msgout = "";

        msgout = getName();
        dout.writeUTF(msgout);
        while (true) {
            msgin = din.readUTF();
            System.out.println(msgin);

            if (msgin.equals("сканировать")) {
                String resuln = Scan();

                dout.writeUTF(resuln);
                System.out.println("завершено");
            }
        }
    }

    static String getName() {
        int myName;
        Random random = new Random();
        myName = random.nextInt();
        return Integer.toString(myName);
    }


    static String Scan() throws JSONException, IOException {
        String line;
        JSONArray ar = new JSONArray();
        JSONObject elemJson ;

        Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\SysWOW64\\wbem\\" + "wmic product get name,version /format:CSV");
        BufferedReader input =
                new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((line = input.readLine()) != null) {
            elemJson= new JSONObject();

            try {
                if (!line.split(",")[1].equals("Name"))
                    elemJson = new JSONObject();
                elemJson.put("name", line.split(",")[1]);
                elemJson.put("version", line.split(",")[2]);
                ar.put(elemJson);
            } catch (Exception ex) {
                continue;
            }

        }
        input.close();

        return ar.toString();
    }

}




