import com.google.gson.Gson;
import de.vandermeer.asciitable.AsciiTable;
import okhttp3.*;

import java.io.IOException;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App {

    public OkHttpClient client;
    public App() {
        client = new OkHttpClient();
    }

    private static final String BASE_URL = "https://crudcrud.com/api/b9b3aae0d3164e719c9705da53f624e9/pizze";

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

            if(operation < 1 || operation > 5) {
                System.out.println("Sbagliato riprova");
                continue;
            }

            switch(operation){
                case 1:
                    try{
                        //Leggi tutto
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
                        //Leggi singola pizza
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
                    try {
                        //Crea una nuova pizza
                        createPizza();
                    }catch(Exception e){
                        System.out.println("Errore nella creazione: " + e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        //Modifica una pizza già esistente
                        updatePizza();
                    }catch(Exception e){
                        System.out.println("Errore nell'aggiornamento: " + e.getMessage());
                    }
                    break;

                case 5:
                    try {
                        //Elimina una pizza
                        deletePizza();
                    }catch(Exception e){
                        System.out.println("Errore nell'eliminazione: " + e.getMessage());
                    }
                    break;
                    default:

            }
        }
    }

    public Pizza[] getAllPizze() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            //deserializza il json body (conversione testo in oggetto)
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
                .url(BASE_URL + "/" + id)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String json = response.body().string();
            //System.out.println(json);

            Gson gson = new Gson();
            Pizza pizza = gson.fromJson(json, Pizza.class);
            return pizza;
        }
    }

    public void createPizza() throws IOException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Inserisci il nome della pizza:");
        String nome = sc.nextLine();

        System.out.println("Inserisci gli ingredienti (separati da virgola):");
        String ingredienti = sc.nextLine();

        System.out.println("Inserisci il prezzo:");
        double prezzo = sc.nextDouble();
        sc.nextLine();

        Pizza nuovaPizza = new Pizza(nome, ingredienti, prezzo);
        Gson gson = new Gson();
        String json = gson.toJson(nuovaPizza);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                json
        );


        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Errore creazione pizza: " + response);
            }
            System.out.println("Pizza creata con successo!");
        }
    }

    public void updatePizza() throws IOException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Inserisci l'ID della pizza da aggiornare:");
        String id = sc.nextLine();

        System.out.println("Inserisci il nuovo nome:");
        String nome = sc.nextLine();

        System.out.println("Inserisci i nuovi ingredienti:");
        String ingredienti = sc.nextLine();

        System.out.println("Inserisci il nuovo prezzo:");
        double prezzo = sc.nextDouble();
        sc.nextLine();

        Pizza pizzaAggiornata = new Pizza(nome, ingredienti, prezzo);
        Gson gson = new Gson();
        String json = gson.toJson(pizzaAggiornata);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                json
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "/" + id)
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Errore aggiornamento pizza: " + response);
            }
            System.out.println("Pizza aggiornata con successo!");
        }
    }

    public void deletePizza() throws IOException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Inserisci l'ID della pizza da eliminare:");
        String id = sc.nextLine();

        Request request = new Request.Builder()
                .url(BASE_URL + "/" + id)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Errore eliminazione pizza: " + response);
            }
            System.out.println("Pizza eliminata con successo!");
        }
    }

    public void run() {
            testSqlite();
            //return;
            menu();
        return;
    }

    public void testSqlite() {
        String DB_URL = "jdbc:sqlite:test.db";

        try{
            java.sql.Connection conn = DriverManager.getConnection(DB_URL);
            if(conn != null){
                System.out.println("Connessione al database avvenuta con successo!");
            }

            String sqlCreateTable = """
                CREATE TABLE IF NOT EXISTS pizze(
                    id VARCHAR(50) PRIMARY KEY,
                    nome VARCHAR(50),
                    ingredienti VARCHAR(200),
                    prezzo DOUBLE);
                """;

            Statement statement = conn.createStatement();
            statement.execute(sqlCreateTable);
            System.out.println("La tabella pizze è stata creata con successo!");

            String sqlInsert = "INSERT INTO pizze VALUES (?,?,?,?);";

            PreparedStatement insertStatement = conn.prepareStatement(sqlInsert);
            insertStatement.setString(1, "abcdef");
            insertStatement.setString(2, "Margherita");
            insertStatement.setString(3, "Mozzarella, Pomodoro");
            insertStatement.setDouble(4, 6.0);
            insertStatement.execute();

        }catch(SQLException e){
            System.out.println("Errore" + e.getMessage());
        }

    }
}
