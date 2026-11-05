package Modeli;

import java.util.ArrayList;
import java.util.List;

public class FlightDatabase {
    private List<Flight> letovi = new ArrayList<>();

    public void add(Flight f) {
        letovi.add(f);
    }

    public List<Flight> getAllFlights() {
        return letovi;
    }

    public void clear(){
        letovi.clear();
    }

//    public List<Flight> getByAirport(Airport a) {
//        List<Flight> rezultat = new ArrayList<>();
//        for (Flight f : letovi) {
//            if (f.getFrom().equals(a)) rezultat.add(f);
//        }
//        return rezultat;
//    }
}
