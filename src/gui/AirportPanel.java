package gui;
import Modeli.*;
import Exception.MyExceptions.*;
import Tajmeri.NeaktivnostTajmer;

import java.awt.*;

public class AirportPanel extends Panel {
    private AirportDatabase airportDb;
    private TextField kodPolje = new TextField(5);
    private TextField nazivPolje = new TextField(15);
    private TextField xPolje = new TextField(5);
    private TextField yPolje = new TextField(5);
    private Label poruka = new Label(" ");
    private NeaktivnostTajmer tajmer;
    private Panel tabela = new Panel();

    public AirportPanel(AirportDatabase airportDb, NeaktivnostTajmer tajmer) {
        this.airportDb = airportDb;
        this.tajmer=tajmer;
        setLayout(new BorderLayout());

        add(napraviFormu(), BorderLayout.NORTH);
        add(tabela, BorderLayout.CENTER);

        osveziTabelu();
    }

    private Panel napraviFormu() {
        Panel forma = new Panel(); // podrazumevani FlowLayout
        forma.add(new Label("Kod:"));
        forma.add(kodPolje);
        forma.add(new Label("Naziv:"));
        forma.add(nazivPolje);
        forma.add(new Label("X:"));
        forma.add(xPolje);
        forma.add(new Label("Y:"));
        forma.add(yPolje);

        Button dodajBtn = new Button("Dodaj aerodrom");
        dodajBtn.addActionListener(e -> dodajAerodrom());
        forma.add(dodajBtn);

        Panel omotac = new Panel(new BorderLayout());
        omotac.add(forma, BorderLayout.NORTH);
        omotac.add(poruka, BorderLayout.SOUTH);
        return omotac;
    }

    private void dodajAerodrom() {
        tajmer.resetuj();
        try {
            String kod = kodPolje.getText().trim().toUpperCase();
            String naziv = nazivPolje.getText().trim();
            int x = Integer.parseInt(xPolje.getText().trim());
            int y = Integer.parseInt(yPolje.getText().trim());

            Airport a = new Airport(kod, naziv, x, y);
            airportDb.add(a);

            poruka.setText("Dodat aerodrom " + kod + ".");
            osveziTabelu();
            ocistiPolja();

        } catch (NumberFormatException ex) {
            poruka.setText("Greška: X i Y moraju biti celi brojevi.");
        } catch (InvalidInputException | DuplicateAirportException ex) {
            poruka.setText("Greška: " + ex.getMessage());
        }
    }

    private void ocistiPolja() {
        kodPolje.setText("");
        nazivPolje.setText("");
        xPolje.setText("");
        yPolje.setText("");
    }

    public void osveziTabelu() {
        tabela.removeAll();
        tabela.setLayout(new GridLayout(0, 4));

        tabela.add(new Label("Kod"));
        tabela.add(new Label("Naziv"));
        tabela.add(new Label("X"));
        tabela.add(new Label("Y"));
        tabela.add(new Label("Obrisi"));

        for (Airport a : airportDb.getAllAirports()) {
            tabela.add(new Label(a.getCode()));
            tabela.add(new Label(a.getName()));
            tabela.add(new Label(String.valueOf(a.getX())));
            tabela.add(new Label(String.valueOf(a.getY())));
            /*Button ukloni = new Button("Ukloni");
            tabela.add(ukloni);*/
        }

        tabela.validate();
        tabela.repaint();
    }


}
