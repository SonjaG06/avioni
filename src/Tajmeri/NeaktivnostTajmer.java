package Tajmeri;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;


public class NeaktivnostTajmer {
    private Frame prozor;
    private int sekundeNeaktivnosti = 0;
    private boolean upozorenjePrikazano = false;
    private boolean pauzirano = false;


    public NeaktivnostTajmer(Frame prozor) {
        this.prozor = prozor;

        Timer tajmer = new Timer(true);
        tajmer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                proveri();
            }
        }, 1000, 1000);
    }

    public void resetuj() {
        sekundeNeaktivnosti = 0;
        upozorenjePrikazano = false;
        pauzirano = false;
    }

    private void proveri() {
        if (pauzirano) return;
        sekundeNeaktivnosti++;

        if (sekundeNeaktivnosti == 55 && !upozorenjePrikazano) {
            upozorenjePrikazano = true;
            prikaziUpozorenje();
        }

        if (sekundeNeaktivnosti >= 60) {
            prozor.dispose();
            System.exit(0);
        }
    }

    private void prikaziUpozorenje() {
        Dialog dijalog = new Dialog(prozor, "Neaktivnost", false);
        dijalog.setLayout(new BorderLayout());
        dijalog.setSize(300, 120);
        dijalog.setLocationRelativeTo(prozor);

        dijalog.add(new Label("Program se gasi za 5 sekundi zbog neaktivnosti."), BorderLayout.CENTER);

        Button nastaviBtn = new Button("Nastavi rad");
        nastaviBtn.addActionListener(e -> {
            resetuj();
            dijalog.setVisible(false);
            dijalog.dispose();
        });
        dijalog.add(nastaviBtn, BorderLayout.SOUTH);

        dijalog.setVisible(true);

    }

    public void pauziraj() {
        pauzirano = true;
    }

    public void nastavi() {
        pauzirano = false;
    }

}
