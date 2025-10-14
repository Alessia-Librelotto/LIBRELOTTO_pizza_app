public class Pizza {
    public String nome;
    public String ingredienti;
    public double prezzo;

    public Pizza(String nome, String ingredienti, double prezzo) {
        this.nome = nome;
        this.ingredienti = ingredienti;
        this.prezzo = prezzo;
    }

    @Override
    public String toString(){
        return "Nome: " + nome + "  Ingredienti: " + ingredienti + "  Prezzo: " + prezzo;
    }
}
