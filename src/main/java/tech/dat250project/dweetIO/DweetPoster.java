package tech.dat250project.dweetIO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import tech.dat250project.model.Poll;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class DweetPoster {
    private String dweetIOPath = "https://dweet.io/dweet/for/DAT250_TEST";
    private URL url = null;
    private HttpURLConnection con = null;


    public DweetPoster(){
    }

    public void publish(Poll p){
        if (this.url == null) try {
            this.url = new URL(this.dweetIOPath);
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            this.con = (HttpURLConnection) this.url.openConnection();
            this.con.setRequestMethod("POST");
        }catch (Exception e){
            e.printStackTrace();
        }

        this.con.setRequestProperty("Accept", "application/json");
        this.con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        this.con.setDoOutput(true);

        Gson gson = new Gson();
        String json = gson.toJson(p);

        try(DataOutputStream os = new DataOutputStream(con.getOutputStream())) {
            byte[] input = json.getBytes("utf-8");
            os.write(input, 0, input.length);
            os.flush();
            os.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        //If we don't receive the input stream this does not work
        try {
            this.con.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        con.disconnect();
        con = null;
    }
}
