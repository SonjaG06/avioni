package Fajlovi;
import java.io.*;
import java.util.Scanner;

import Modeli.*;
import Exception.MyExceptions.*;
import Tajmeri.Time;

public class CsvHandler {
    public static void read(String putanja, AirportDatabase airportDb, FlightDatabase flightDb) throws FileFormatException, IOException {
        Scanner sc = new Scanner(new File(putanja));
       // String linija;
        int brojLinije = 0;
        boolean citamLetove = false;

        while (sc.hasNextLine()) {
            String linija = sc.nextLine().trim();
            brojLinije++;

            if (linija.isEmpty()) continue;

            if (linija.startsWith("#")) {
                if (linija.toUpperCase().contains("FLIGHT")) citamLetove = true;
                else citamLetove = false;
                continue;
            }

            if (linija.startsWith("CODE,") || linija.startsWith("FROM,")) continue;

            String[] delovi = linija.split(",");

            try {
                if (!citamLetove) {
                    int x = Integer.parseInt(delovi[2]);
                    int y = Integer.parseInt(delovi[3]);
                    Airport a = new Airport(delovi[0], delovi[1], x, y);
                    airportDb.add(a);
                } else {
                    Airport od = airportDb.findByCode(delovi[0]);
                    Airport doAerodroma = airportDb.findByCode(delovi[1]);
                    if (od == null || doAerodroma == null) {
                        sc.close();
                        throw new FileFormatException("Ne postoji aerodrom " + delovi[0] + " ili " + delovi[1] + " (red " + brojLinije + ")", putanja);
                    }
                    Time v = Time.readFromString(delovi[2]);
                    int trajanje = Integer.parseInt(delovi[3]);
                    Flight f = new Flight(od, doAerodroma, v, trajanje);
                    flightDb.add(f);
                }
            }
            catch (InvalidInputException e) {
                sc.close();
                throw new FileFormatException(e.getMessage() + " (red " + brojLinije + ")", putanja);
            }
            catch (DuplicateAirportException e) {
                sc.close();
                throw new FileFormatException(e.getMessage() + " (red " + brojLinije + ")", putanja);
            }
            catch (NumberFormatException e) {
                sc.close();
                throw new FileFormatException("Neispravan broj u redu " + brojLinije, putanja);
            }
            catch (ArrayIndexOutOfBoundsException e) {
                sc.close();
                throw new FileFormatException("Red " + brojLinije + " nema dovoljno kolona.", putanja);
            }
        }
        sc.close();
    }

    public static void write(String putanja, AirportDatabase airportDb, FlightDatabase flightDb) throws IOException {
        FileWriter fw = new FileWriter(putanja);

        fw.write("# AIRPORTS\n");
        fw.write("CODE,NAME,X,Y\n");
        for (Airport a : airportDb.getAllAirports()) {
            fw.write(a.getCode() + "," + a.getName() + "," + a.getX() + "," + a.getY() + "\n");
        }

        fw.write("# FLIGHTS\n");
        fw.write("FROM,TO,DEPARTURE,DURATION\n");
        for (Flight f : flightDb.getAllFlights()) {
            fw.write(f.getFrom().getCode() + "," + f.getTo().getCode() + ","
                    + f.getVremePolaska().toString() + "," + f.getTrajanjeLeta() + "\n");
        }

        fw.close();
    }
}
