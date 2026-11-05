package gui;
import java.awt.*;
import Modeli.AirportDatabase;
import Modeli.Airport;
import Tajmeri.NeaktivnostTajmer;

public class MapaPanel extends Panel{
    private AirportDatabase airportDb;
    private Mapa mapa;
    private Panel lista = new Panel();

    public MapaPanel(AirportDatabase airportDb, NeaktivnostTajmer tajmer) {
        this.airportDb = airportDb;
        setLayout(new BorderLayout());

        mapa = new Mapa(airportDb, tajmer);
        add(mapa, BorderLayout.CENTER);

        ScrollPane scroll = new ScrollPane();
        scroll.add(lista);
        scroll.setPreferredSize(new Dimension(220, 0));
        add(scroll, BorderLayout.EAST);

        osveziListu();
    }

    public void osveziListu() {
        lista.removeAll();
        lista.setLayout(new GridLayout(0, 1));

        for (Airport a : airportDb.getAllAirports()) {
            Checkbox cb = new Checkbox(
                    a.getCode() + " - " + a.getName() + " (" + a.getX() + "," + a.getY() + ")",
                    a.isVidljiv());
            cb.addItemListener(e -> {
                a.setVidljiv(cb.getState());
                mapa.repaint();
            });
            lista.add(cb);
        }

        lista.validate();
        lista.repaint();
    }

    public void repaintMapa() {
        mapa.repaint();
    }

}
