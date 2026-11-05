package gui;
import Modeli.*;
import Exception.MyExceptions.*;
import java.awt.*;
import Tajmeri.*;

public class FlightPanel extends Panel{
    private AirportDatabase airportDb;
    private FlightDatabase flightDb;
    private NeaktivnostTajmer tajmer;
    private Choice odPolje = new Choice();
    private Choice doPolje = new Choice();
    private TextField vremePolje = new TextField(5);
    private TextField trajanjePolje = new TextField(5);
    private Label poruka = new Label(" ");

    private Panel tabela = new Panel();

    public FlightPanel(AirportDatabase airportDb, FlightDatabase flightDb, NeaktivnostTajmer tajmer) {
        this.airportDb = airportDb;
        this.flightDb = flightDb;
        this.tajmer=tajmer;
        setLayout(new BorderLayout());

        add(napraviFormu(), BorderLayout.NORTH);
        add(tabela, BorderLayout.CENTER);

        osveziAerodrome();
        osveziTabelu();
    }

    private Panel napraviFormu() {
        Panel forma = new Panel();
        forma.add(new Label("Od:"));
        forma.add(odPolje);
        forma.add(new Label("Do:"));
        forma.add(doPolje);
        forma.add(new Label("Vreme (HH:mm):"));
        forma.add(vremePolje);
        forma.add(new Label("Trajanje (min):"));
        forma.add(trajanjePolje);

        Button dodajBtn = new Button("Dodaj let");
        dodajBtn.addActionListener(e -> dodajLet());
        forma.add(dodajBtn);

        Panel omotac = new Panel(new BorderLayout());
        omotac.add(forma, BorderLayout.NORTH);
        omotac.add(poruka, BorderLayout.SOUTH);
        return omotac;
    }

    public void osveziAerodrome() {
        odPolje.removeAll();
        doPolje.removeAll();
        for (Airport a : airportDb.getAllAirports()) {
            odPolje.add(a.getCode());
            doPolje.add(a.getCode());
        }
    }

    private void dodajLet() {
        tajmer.resetuj();
        if (odPolje.getItemCount() == 0 || doPolje.getItemCount() == 0) {
            poruka.setText("Greška: prvo dodaj bar dva aerodroma.");
            return;
        }
        try {
            String odKod = odPolje.getSelectedItem();
            String doKod = doPolje.getSelectedItem();
            Airport od = airportDb.findByCode(odKod);
            Airport doA = airportDb.findByCode(doKod);

            Time vreme = Time.readFromString(vremePolje.getText().trim());
            int trajanje = Integer.parseInt(trajanjePolje.getText().trim());

            Flight f = new Flight(od, doA, vreme, trajanje);
            flightDb.add(f);

            poruka.setText("Dodat let " + odKod + " -> " + doKod + ".");
            osveziTabelu();
            vremePolje.setText("");
            trajanjePolje.setText("");

        } catch (NumberFormatException ex) {
            poruka.setText("Greška: trajanje mora biti ceo broj.");
        } catch (InvalidInputException ex) {
            poruka.setText("Greška: " + ex.getMessage());
        }
    }

    public void osveziTabelu() {
        tabela.removeAll();
        tabela.setLayout(new GridLayout(0, 4));

        tabela.add(new Label("Od"));
        tabela.add(new Label("Do"));
        tabela.add(new Label("Polazak"));
        tabela.add(new Label("Trajanje (min)"));

        for (Flight f : flightDb.getAllFlights()) {
            tabela.add(new Label(f.getFrom().getCode()));
            tabela.add(new Label(f.getTo().getCode()));
            tabela.add(new Label(f.getVremePolaska().toString()));
            tabela.add(new Label(String.valueOf(f.getTrajanjeLeta())));
        }

        tabela.validate();
        tabela.repaint();
    }

}
