package Modeli;
import java.util.ArrayList;
import java.util.List;
import Exception.MyExceptions.*;

public class AirportDatabase {
    private List<Airport> aerodromi = new ArrayList<>();

    public void add(Airport a) throws DuplicateAirportException {
        for (Airport existing : aerodromi) {
            if (existing.getCode().equals(a.getCode())) {
                throw new DuplicateAirportException("Aerodrom sa kodom " + a.getCode() + " već postoji.");
            }
        }
        aerodromi.add(a);
    }

    public void clear(){
        aerodromi.clear();
    }
    public void ukloni(Airport a){
        aerodromi.remove(a);
    }

    public Airport findByCode(String code) {
        for (Airport a : aerodromi) {
            if (a.getCode().equals(code)){
                return a;
            }
        }
        return null; //nije pronadjen
    }

    public List<Airport> getAllAirports() {
        return aerodromi;
    }
}
