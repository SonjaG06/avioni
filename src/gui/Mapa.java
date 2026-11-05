package gui;
import java.awt.*;
import Modeli.AirportDatabase;
import Modeli.Airport;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import Tajmeri.NeaktivnostTajmer;

public class Mapa extends Canvas{
    private AirportDatabase airportDb;
    private Airport selektovan = null;
    private boolean crvenoVidljivo = false;
    private NeaktivnostTajmer tajmer;


    public Mapa(AirportDatabase airportDb, NeaktivnostTajmer tajmer) {
        this.airportDb = airportDb;
        this.tajmer = tajmer;
        setBackground(Color.white);

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                obradiKlik(e.getX(), e.getY());
            }
        });

        Timer trepci = new Timer(true);
        trepci.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (selektovan != null) {
                    crvenoVidljivo = !crvenoVidljivo;
                    repaint();
                }
            }
        }, 500, 500);

    }

    private int izracunajPx(Airport a) {
        return (int) ((a.getX() + 180) / 360.0 * getWidth());
    }

    private int izracunajPy(Airport a) {
        return (int) ((90 - a.getY()) / 180.0 * getHeight());
    }

    private void obradiKlik(int klikX, int klikY) {
        for (Airport a : airportDb.getAllAirports()) {
            if (!a.isVidljiv()) continue;
            int px = izracunajPx(a);
            int py = izracunajPy(a);

            if (Math.abs(klikX - px) <= 10 && Math.abs(klikY - py) <= 10) {
                if (a == selektovan) {
                    selektovan = null;
                    tajmer.nastavi();

                }
                else {
                    selektovan = a;
                    tajmer.pauziraj();

                    crvenoVidljivo = true;
                }
                repaint();
                return;
            }
        }
    }

    public void paint(Graphics g) {
        for (Airport a : airportDb.getAllAirports()) {
            if (!a.isVidljiv()) continue;
            int px = izracunajPx(a);
            int py = izracunajPy(a);

            if (a == selektovan && crvenoVidljivo) {
                g.setColor(Color.red);
            }
            else {
                g.setColor(Color.gray);
            }
            g.fillRect(px - 5, py - 5, 10, 10);

            g.setColor(Color.black);
            g.drawString(a.getCode(), px + 8, py + 4);
        }

    }

}
