package Modeli;
import Tajmeri.Time;
import Exception.MyExceptions.*;

public class Flight {
    private Airport from;
    private Airport to;
    private Time vremePolaska;
    int trajanjeLeta;

    public Flight(Airport from, Airport to, Time vremePolaska, int trajanjeLeta) throws InvalidInputException {
        proveriTrajanje(trajanjeLeta);
        this.from = from;
        this.to = to;
        this.vremePolaska = vremePolaska;
        this.trajanjeLeta = trajanjeLeta;
    }

    public Airport getFrom() {
        return from;
    }

    public Airport getTo() {
        return to;
    }

    public Time getVremePolaska() {
        return vremePolaska;
    }

    public int getTrajanjeLeta() {
        return trajanjeLeta;
    }

    public Time getVremeSletenja() throws InvalidInputException {
        int ukupnoMinuta = vremePolaska.toMinuti() + trajanjeLeta;
        return new Time((ukupnoMinuta / 60) % 24, ukupnoMinuta % 60);
    }

    private void proveriTrajanje(int trajanjeMin) throws InvalidInputException {
        if (trajanjeMin <= 0) {
            throw new InvalidInputException("Trajanje leta mora biti veće od 0 minuta.");
        }
    }

}
