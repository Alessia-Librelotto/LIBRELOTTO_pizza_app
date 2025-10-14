import com.google.gson.Gson;
import de.vandermeer.asciitable.AsciiTable;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App {

    public OkHttpClient client;
    public App() {
        client = new OkHttpClient();
    }

    public void menu(){
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("MENU'");
            System.out.println("1. Leggi tutto");
            System.out.println("2. Leggi singola pizza");
            System.out.println("3. Crea pizza");
            System.out.println("4. Aggiorna pizza");
            System.out.println("5. Elimina pizza");

            int operation = -1;

            try{
                operation = sc.nextInt();
                sc.nextLine();
            }catch(InputMismatchException e){
                System.out.println("Non è un numero. Digita un numero");
                sc.nextLine();
                continue;
            }

            if(operation < 1 || operation > 4) {
                System.out.println("Sbagliato riprova");
                continue;
            }

            //System.out.println("Bravo");

            switch(operation){
                case 1:
                    try{
                        Pizza[] pizze = getAllPizze();
                        AsciiTable asciiTable = new AsciiTable();
                        asciiTable.addRule();
                        asciiTable.addRow("Nome", "Ingredienti", "Prezzo");
                        asciiTable.addRule();
                        for(Pizza pizza : pizze){
                            asciiTable.addRow(pizza.nome, pizza.ingredienti, pizza.prezzo);
                            asciiTable.addRule();
                        }
                        System.out.println(asciiTable.render());
                    }catch (Exception e){
                        System.out.println("E' avvenuto un errore " + e.getClass());
                    }
                    break;
                case 2:
                    try {
                        System.out.println("Inserisci l'ID della pizza");
                        String id = sc.nextLine();
                        Pizza pizza = getPizza(id);
                        AsciiTable asciiTable = new AsciiTable();
                        asciiTable.addRule();
                        asciiTable.addRow("Nome", "Ingredienti", "Prezzo");
                        asciiTable.addRule();
                        asciiTable.addRow(pizza.nome, pizza.ingredienti, pizza.prezzo);
                        asciiTable.addRule();
                        System.out.println(asciiTable.render());
                    }catch(Exception e){
                        System.out.println("E' avvenuto un errore" + e.getMessage());
                    }
                    break;
                case 3:

                case 4:

                    default:

            }
        }
    }

    public Pizza[] getAllPizze() throws IOException {
        Request request = new Request.Builder()
                .url("https://crudcrud.com/api/4785f11850b24426b2f4d5095ac19077/pizze")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            //stampa l'header
//            Headers responseHeaders = response.headers();
//            for (int i = 0; i < responseHeaders.size(); i++) {
//                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//            }

            //deserializza il json body
            String json = response.body().string();
            Gson gson = new Gson();
            Pizza[] pizze = gson.fromJson(json, Pizza[].class);
            for (Pizza pizza : pizze) {
                System.out.println(pizza.toString());
            }
            return pizze;
        }

    }

    public Pizza getPizza(String id) throws IOException {
        Request request = new Request.Builder()
                .url("https://crudcrud.com/api/4785f11850b24426b2f4d5095ac19077/pizze/" + id)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            System.out.println(response.body().string());
            Gson gson = new Gson();
            Pizza pizza = gson.fromJson(response.body().string(), Pizza.class);

            return pizza;
        }
    }

    // PER CASA
    // Faccio la funzione createPizza con richiesta POST
    // usando il costruttore di Pizza.java

    // Provo PUT
    public void run() {
            menu();
    }
}
