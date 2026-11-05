package Fajlovi;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Modeli.*;
import Exception.MyExceptions.*;
import Tajmeri.Time;

public class JsonHandler {
    public static void read(String putanja, AirportDatabase airportDb, FlightDatabase flightDb) throws FileFormatException, IOException {
        StringBuilder sb = new StringBuilder();
        Scanner sc = new Scanner(new File(putanja));
        while (sc.hasNextLine()) {
            sb.append(sc.nextLine()).append("\n");
        }
        sc.close();
        String sadrzaj = sb.toString();
        parseAirports(sadrzaj, airportDb, putanja);
        parseFlights(sadrzaj, airportDb, flightDb, putanja);
    }
    public static void parseAirports(String sadrzaj, AirportDatabase db, String putanja) throws FileFormatException{
        int start = sadrzaj.indexOf("airports");
        if (start == -1) throw new FileFormatException("Nema polja 'airports' u fajlu.", putanja);
        int otv = sadrzaj.indexOf("[", start);
        int zat = nadjiZatvorenuZagradu(sadrzaj, otv, '[', ']');
        String tekst = sadrzaj.substring(otv + 1, zat); //dobija sve airports u formatima {...},{}

        List<String> objekti = razdvoj(tekst); //lista gde je svaki objekat jedan aerodrom
        int i = 0;
        for (String objekat : objekti) {
            i++;
            try {
                String code = pronadjiDeoTeksta(objekat, "code");
                String name = pronadjiDeoTeksta(objekat, "name");
                int x = pronadjiBroj(objekat, "x");
                int y = pronadjiBroj(objekat, "y");
                db.add(new Airport(code, name, x, y));
            }
            catch (InvalidInputException | DuplicateAirportException e) {
                throw new FileFormatException(e.getMessage() + " (aerodrom br. " + i + ")", putanja);
            }
            catch (NumberFormatException e) {
                throw new FileFormatException("Los broj kod aerodroma br. " + i, putanja);
            }
        }
    }

    public static void parseFlights(String tekst, AirportDatabase airportDb, FlightDatabase flightDb, String putanja) throws FileFormatException{
        int start = tekst.indexOf("flights");
        if (start == -1)
            throw new FileFormatException("Nema polja flights u fajlu.", putanja);

        int otv = tekst.indexOf("[", start);
        int zat = nadjiZatvorenuZagradu(tekst, otv, '[', ']');
        String blok = tekst.substring(otv + 1, zat);

        List<String> objekti = razdvoj(blok);
        int i = 0;
        for (String o : objekti) {
            i++;
            try {
                String fromCode = pronadjiDeoTeksta(o, "from");
                String toCode = pronadjiDeoTeksta(o, "to");
                Airport od = airportDb.findByCode(fromCode);
                Airport doA = airportDb.findByCode(toCode);
                if (od == null || doA == null) {
                    throw new FileFormatException("Nepoznat aerodrom u letu br. " + i, putanja);
                }
                Time vreme = Time.readFromString(pronadjiDeoTeksta(o, "departure"));
                int trajanje = pronadjiBroj(o, "duration");
                flightDb.add(new Flight(od, doA, vreme, trajanje));
            } catch (InvalidInputException e) {
                throw new FileFormatException(e.getMessage() + " (let br. " + i + ")", putanja);
            } catch (NumberFormatException e) {
                throw new FileFormatException("Los broj kod leta br. " + i, putanja);
            }
        }
    }


    private static List<String> razdvoj(String tekst){
        List<String> rezultat = new ArrayList<>();
        int i = 0;
        while (i < tekst.length()) {
            if (tekst.charAt(i) == '{') {
                int kraj = nadjiZatvorenuZagradu(tekst, i, '{', '}');
                if (kraj == -1) break;
                rezultat.add(tekst.substring(i, kraj + 1));
                i = kraj + 1;
            }
            else {
                i++;
            }
        }
        return rezultat;
    }

    private static int nadjiZatvorenuZagradu(String tekst, int start, char otv, char zat) {
        int brojac = 1;
        int i = start + 1;
        while (i < tekst.length() && brojac > 0) {
            if (tekst.charAt(i) == otv) brojac++;
            else if (tekst.charAt(i) == zat) brojac--;
            if (brojac == 0) return i;
            i++;
        }
        return -1;
    }

    private static String pronadjiDeoTeksta(String obj, String kljuc){ //sluzi mi da nadjem kljuc u tekstu obj
        int index = obj.indexOf(kljuc);
        int prviNavodnici = obj.indexOf("\"", index+kljuc.length()+1);
        int krajNavodnika = obj.indexOf("\"", prviNavodnici + 1);
        return obj.substring(prviNavodnici+1, krajNavodnika);
    }

    private static int pronadjiBroj(String obj, String kljuc) throws NumberFormatException {
        int idx = obj.indexOf(kljuc);
        if (idx == -1) throw new NumberFormatException("Nema " + kljuc);
        int dveTacke = obj.indexOf(":", idx);
        int zarez = obj.indexOf(",", dveTacke); //ako nema zarez ipsilon je
        int zagrada = obj.indexOf("}", dveTacke);
        int kraj;
        if (zarez == -1) {
            kraj = zagrada;
        }
        else if (zagrada == -1) kraj = zarez;
        else kraj = Math.min(zarez, zagrada);
        return Integer.parseInt(obj.substring(dveTacke + 1, kraj).trim());
    }

    public static void write(String putanja, AirportDatabase airportDb, FlightDatabase flightDb) throws IOException {
        FileWriter fw = new FileWriter(putanja);

        fw.write("{\"airports\":[\n");
        List<Airport> aerodromi = airportDb.getAllAirports();
        for (int i = 0; i < aerodromi.size(); i++) {
            Airport a = aerodromi.get(i);
            fw.write("{\"code\":\"" + a.getCode() + "\",");
            fw.write("\"name\":\"" + a.getName() + "\",");
            fw.write("\"x\":" + a.getX() + ",");
            fw.write("\"y\":" + a.getY() + "}");
            if (i < aerodromi.size() - 1) fw.write(",");
            fw.write("\n");
        }
        fw.write("],\n\"flights\":[\n");

        List<Flight> letovi = flightDb.getAllFlights();
        for (int i = 0; i < letovi.size(); i++) {
            Flight f = letovi.get(i);
            fw.write("{\"from\":\"" + f.getFrom().getCode() + "\",");
            fw.write("\"to\":\"" + f.getTo().getCode() + "\",");
            fw.write("\"departure\":\"" + f.getVremePolaska().toString() + "\",");
            fw.write("\"duration\":" + f.getTrajanjeLeta() + "}");
            if (i < letovi.size() - 1) fw.write(",");
            fw.write("\n");
        }
        fw.write("]\n}\n");

        fw.close();
    }
}
