package gui;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import Exception.MyExceptions.*;
import Modeli.*;
import Fajlovi.*;
import Tajmeri.NeaktivnostTajmer;

public class MainWindow extends Frame implements ActionListener{
    private CardLayout cardLayout = new CardLayout();
    private Panel sadrzaj = new Panel(cardLayout);
    private AirportDatabase airportDb = new AirportDatabase();
    private FlightDatabase flightDb = new FlightDatabase();
    private FlightPanel flightPanel;
    private AirportPanel airportPanel;
    private NeaktivnostTajmer tajmer;
    private MapaPanel mapaPanel;

    public MainWindow() {
        super("Aerodromi");
        setSize(900, 600);
        setLayout(new BorderLayout());
        tajmer = new NeaktivnostTajmer(this);
        dodajMeni();
        dodajDugmadZaPrebacivanje();
        dodajPanele();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });

        setVisible(true);
    }
    private void dodajMeni() {
        MenuBar trakaMenija = new MenuBar();
        Menu fajlMeni = new Menu("Fajl");

        fajlMeni.add("Sačuvaj CSV");
        fajlMeni.add("Učitaj CSV");
        fajlMeni.add("Sačuvaj JSON");
        fajlMeni.add("Učitaj JSON");
        fajlMeni.addActionListener(this);

        trakaMenija.add(fajlMeni);
        setMenuBar(trakaMenija);
    }

    private void dodajDugmadZaPrebacivanje() {
        Panel traka = new Panel();
        Button aerodromiBtn = new Button("Aerodromi");
        Button letoviBtn = new Button("Letovi");
        Button mapaBtn = new Button("Mapa");

        aerodromiBtn.addActionListener(e -> {
            tajmer.resetuj();
            cardLayout.show(sadrzaj, "aerodromi");
        });
        letoviBtn.addActionListener(e -> {
            tajmer.resetuj();
            flightPanel.osveziAerodrome();
            cardLayout.show(sadrzaj, "letovi");
        });
        mapaBtn.addActionListener(e -> {
            tajmer.resetuj();
            mapaPanel.repaintMapa();
            cardLayout.show(sadrzaj, "mapa");
        });

        traka.add(aerodromiBtn);
        traka.add(letoviBtn);
        traka.add(mapaBtn);
        add(traka, BorderLayout.NORTH);
    }

    private void dodajPanele() {
        airportPanel = new AirportPanel(airportDb, tajmer);
        sadrzaj.add(airportPanel, "aerodromi");

        flightPanel = new FlightPanel(airportDb, flightDb, tajmer);
        sadrzaj.add(flightPanel, "letovi");

        mapaPanel = new MapaPanel(airportDb, tajmer);
        sadrzaj.add(mapaPanel, "mapa");

        add(sadrzaj, BorderLayout.CENTER);

    }
    private void sacuvaj(boolean json) {
        String ekstenzija = json ? "*.json" : "*.csv";
        FileDialog fd = new FileDialog(this, "Sačuvaj kao " + (json ? "JSON" : "CSV"), FileDialog.SAVE);
        fd.setFile(ekstenzija);
        fd.setVisible(true);
        if (fd.getFile() == null) return;

        String putanja = fd.getDirectory() + fd.getFile();
        try {
            if (json) JsonHandler.write(putanja, airportDb, flightDb);
            else CsvHandler.write(putanja, airportDb, flightDb);
            prikaziPoruku("Sačuvano", "Podaci su sačuvani u:\n" + putanja);
        } catch (IOException ex) {
            prikaziPoruku("Greška", "Ne mogu da sačuvam fajl:\n" + ex.getMessage());
        }
    }

    private void ucitaj(boolean json) {
        FileDialog fd = new FileDialog(this, "Učitaj " + (json ? "JSON" : "CSV"), FileDialog.LOAD);
        fd.setVisible(true);
        if (fd.getFile() == null) return;

        String putanja = fd.getDirectory() + fd.getFile();
        airportDb.clear();
        flightDb.clear();
        try {
            if (json) JsonHandler.read(putanja, airportDb, flightDb);
            else CsvHandler.read(putanja, airportDb, flightDb);
            prikaziPoruku("Učitano", "Podaci su uspešno učitani.");
        } catch (FileFormatException ex) {
            prikaziPoruku("Greška u formatu", ex.getMessage());
        } catch (IOException ex) {
            prikaziPoruku("Greška", "Ne mogu da pročitam fajl:\n" + ex.getMessage());
        }
        airportPanel.osveziTabelu();
        flightPanel.osveziAerodrome();
        flightPanel.osveziTabelu();
        mapaPanel.osveziListu();
    }


    private void prikaziPoruku(String naslov, String tekst) {
        Dialog dijalog = new Dialog(this, naslov, true);
        dijalog.setLayout(new BorderLayout());
        dijalog.setSize(350, 150);
        dijalog.setLocationRelativeTo(this);

        TextArea tekstualnoPolje = new TextArea(tekst);
        tekstualnoPolje.setEditable(false);
        dijalog.add(tekstualnoPolje, BorderLayout.CENTER);

        Button ok = new Button("OK");
        ok.addActionListener(e -> dijalog.setVisible(false));
        dijalog.add(ok, BorderLayout.SOUTH);

        dijalog.setVisible(true);
        dijalog.dispose();
    }
    public void actionPerformed(ActionEvent e) {
        tajmer.resetuj();
        switch (e.getActionCommand()) {
            case "Sačuvaj CSV" -> sacuvaj(false);
            case "Učitaj CSV" -> ucitaj(false);
            case "Sačuvaj JSON" -> sacuvaj(true);
            case "Učitaj JSON" -> ucitaj(true);
        }
    }
}
