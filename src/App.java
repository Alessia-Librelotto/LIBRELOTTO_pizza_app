import com.google.gson.Gson;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class App {

    public OkHttpClient client;
    public App() {
        client = new OkHttpClient();
    }

    public void doGet() throws IOException {
        Request request = new Request.Builder()
                .url("https://crudcrud.com/api/1c835d68c86142c0bc10d6ad2e1fdf02/pizze")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            Headers responseHeaders = response.headers();
            for (int i = 0; i < responseHeaders.size(); i++) {
                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }

            String json = response.body().string();
            Gson gson = new Gson();
            Pizza[] pizze = gson.fromJson(json, Pizza[].class);
            for(Pizza pizza : pizze) {
                System.out.println(pizza.toString());
            }
        }

    }

    public void run() {
        try{
            doGet();
        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    //PER CASA
    //faccio un prompt e inserisco la post
}
