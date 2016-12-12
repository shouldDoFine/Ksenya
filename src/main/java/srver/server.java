package srver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class server {

    private static HashMap<String, Connection> connections;

    public static void main(String[] args) throws IOException, JSONException, InterruptedException {
        connections = new HashMap<String, Connection>();
        Accept Acc;
        Acc = new Accept();
        Acc.start();
        String s1;
        System.out.println("presss any key to start scanning");
        Scanner sc = new Scanner(System.in);
        s1 = sc.nextLine();
        ComandClientsToScan();
        ArrayList<PO> list=  GetAnsver();
        scanForVulners(list);
    }
    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            String jsonText = sb.toString();
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    static void readVulners(JSONObject json) throws JSONException, InterruptedException {
        Thread.sleep(1000);
        JSONObject jo=json.getJSONObject("data");
        JSONArray p=jo.getJSONArray("search");
        for (int i=0;i <p.length(); i++){
            JSONObject obj=p.getJSONObject(i).getJSONObject("_source");
            vulnerability vuln=new vulnerability(obj.getString("lastseen"), obj.getString("references"), obj.getString("description"), obj.getString("edition"), obj.getString("published"),
                    obj.getString("title"), obj.getString("type"), obj.getString("objectVersion"),  obj.getString("bulletinFamily"),
                    obj.getString("cvelist"), obj.getString("modified"), obj.getString("href"), obj.getString("id"), p.getJSONObject(i).getString("_score"));
            System.out.println(vuln.toString());
            System.out.println("end"+ i);
        }
    }

    public static  void scanForVulners(ArrayList<PO> list) throws IOException, JSONException, InterruptedException {
        JSONObject json;
        for(PO a:list){
            System.out.println(a.getName()+" "+a.getVersion());
            json = readJsonFromUrl("https://vulners.com/api/v3/search/lucene/?query=affectedSoftware.name:"+a.getName().replaceAll(" ","%20")+"%20AND%20affectedSoftware.version:%22"+a.getVersion()+"%22");
            readVulners(json);
            json=readJsonFromUrl("https://vulners.com/api/v3/search/lucene/?query=affectedPackage.packageName:"+a.getName().replaceAll(" ","%20")+"%20AND%20affectedPackage.packageVersion:%22"+a.getVersion()+"%22");
            readVulners(json);
        }
    }
    public static ArrayList<PO> GetAnsver() {
        ArrayList<PO> POlist = new ArrayList<PO>();
        String ansver;
        try {
            for (String key : connections.keySet()) {

                ansver = connections.get(key).getIn().readUTF();
                JSONArray p = new JSONArray(ansver);
                for (int i = 0; i < p.length(); i++) {
                    POlist.add(new PO(key, p.getJSONObject(i).getString("name"), p.getJSONObject(i).getString("version")));

                }

            }

        //    System.out.println(POlist);
        } catch (Exception e) {
        }

        return POlist;
    }

    public static void ComandClientsToScan() {
        String msgout = "сканировать";
        if (connections.size()!=0) {
            synchronized (connections) {
                for (String key : connections.keySet()) {
                    try {
                        connections.get(key).getOut().writeUTF(msgout);
                        connections.get(key).getOut().flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else{
            System.out.println("подключений не обнаруженно");
        }

    }


    private static class Accept extends Thread {
        ServerSocket ss;

        public Accept() throws IOException {
            ss = new ServerSocket(1201);
        }

        @Override
        public void run() {
            while (true) {
                Socket s = null;
                try {
                    s = ss.accept();
                    synchronized (connections) {
                        Connection con = new Connection(s);
                        String name = con.getIn().readUTF();
                        System.out.println("обнаружено подключение "+ name);
                        connections.put(name, con);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private static class Connection {
        DataInputStream in;
        DataOutputStream out;

        Socket socket;

        public Connection(Socket socket) throws IOException {
            this.socket = socket;

            try {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());


            } catch (IOException e) {
                e.printStackTrace();

            }
        }

        public DataInputStream getIn() {
            return in;
        }

        public DataOutputStream getOut() {
            return out;
        }

    }



}

