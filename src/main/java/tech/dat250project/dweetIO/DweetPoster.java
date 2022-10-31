package tech.dat250project.dweetIO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import tech.dat250project.model.Poll;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class DweetPoster {
    private String dweetIOPath = "https://dweet.io/dweet/for/DAT250_FJJ";
    private URL url = null;
    private HttpURLConnection con = null;


    public DweetPoster(){
    }

    public void publish(Poll p){
        if (url == null) try {
            url = new URL(dweetIOPath);
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
        }catch (Exception e){
            e.printStackTrace();
        }

        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        Gson gson = new Gson();
        String json = gson.toJson(p);
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = json.getBytes("utf-8");
            os.write(input, 0, input.length);
            os.flush();
            os.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        con.disconnect();
        con = null;
    }
}
