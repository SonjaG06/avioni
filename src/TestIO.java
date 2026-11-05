import Fajlovi.CsvHandler;
import Modeli.*;
import Fajlovi.CsvHandler;
import Exception.MyExceptions.*;
import java.io.IOException;

public class TestIO {

    public static void main(String[] args) {
        AirportDatabase adb = new AirportDatabase();
        FlightDatabase fdb = new FlightDatabase();

        // 1. Ucitavanje ispravnog fajla
        try {
            CsvHandler.read("test.csv", adb, fdb);
            System.out.println("Ucitano " + adb.getAllAirports().size() + " aerodroma:");
            for (Airport a : adb.getAllAirports()) {
                System.out.println("  " + a.getCode() + " - " + a.getName() + " (" + a.getX() + ", " + a.getY() + ")");
            }
            System.out.println();
            System.out.println("Ucitano " + fdb.getAllFlights().size() + " letova:");
            for (Flight f : fdb.getAllFlights()) {
                System.out.println("  " + f.getFrom().getCode() + " -> " + f.getTo().getCode()
                        + " u " + f.getVremePolaska() + ", traje " + f.getTrajanjeLeta() + " min");
            }
        } catch (FileFormatException e) {
            System.out.println("Greska u formatu: " + e.getMessage() + " (fajl: " + e.getFileName() + ")");
        } catch (IOException e) {
            System.out.println("Greska pri citanju fajla: " + e.getMessage());
        }

        System.out.println("\n----------\n");

        // 2. Test upisivanja - pisemo pa ponovo citamo
        try {
            CsvHandler.write("test_output.csv", adb, fdb);
            System.out.println("Sacuvano u test_output.csv");

            AirportDatabase adb2 = new AirportDatabase();
            FlightDatabase fdb2 = new FlightDatabase();
            CsvHandler.read("test_output.csv", adb2, fdb2);

            if (adb2.getAllAirports().size() == adb.getAllAirports().size()
                    && fdb2.getAllFlights().size() == fdb.getAllFlights().size()) {
                System.out.println("Round-trip OK: procitano isto koliko je i upisano.");
            } else {
                System.out.println("Round-trip NEUSPEH: brojevi se ne poklapaju.");
            }
        } catch (Exception e) {
            System.out.println("Greska pri upisu ili ponovnom citanju: " + e.getMessage());
        }

        System.out.println("\n----------\n");

        // 3. Test sa nepostojecim fajlom
        try {
            AirportDatabase adb3 = new AirportDatabase();
            FlightDatabase fdb3 = new FlightDatabase();
            CsvHandler.read("ne_postoji.csv", adb3, fdb3);
            System.out.println("GRESKA: trebalo je da baci izuzetak jer fajl ne postoji.");
        } catch (IOException e) {
            System.out.println("Ocekivana greska (fajl ne postoji): " + e.getMessage());
        } catch (FileFormatException e) {
            System.out.println("Format greska: " + e.getMessage());
        }

        System.out.println("\n----------\n");

        // 4. Test sa pokvarenim fajlom - napravi rucno test_bad.csv sa lošim podacima
        // npr:
        // # AIRPORTS
        // CODE,NAME,X,Y
        // XX,Los kod ima samo dva slova,10,20
        try {
            AirportDatabase adb4 = new AirportDatabase();
            FlightDatabase fdb4 = new FlightDatabase();
            CsvHandler.read("test_bad.csv", adb4, fdb4);
            System.out.println("GRESKA: trebalo je da baci izuzetak jer je fajl los.");
        } catch (FileFormatException e) {
            System.out.println("Ocekivana format greska: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO greska: " + e.getMessage());
        }
    }
}