package Tajmeri;
import Exception.MyExceptions.*;


public class Time {
    private int sat;
    private int minut;

    public Time(int sat, int minut) throws InvalidInputException{
        proveriVreme(sat, minut);
        this.sat = sat;
        this.minut = minut;
    }

    public int toMinuti() {
        return sat * 60 + minut;
    }

    private void proveriVreme(int sat, int minut) throws InvalidInputException {
        if (sat < 0 || sat > 23 || minut < 0 || minut > 59) {
            throw new InvalidInputException("Neispravno vreme: " + sat + ":" + minut);
        }
    }

    public static Time readFromString(String s) throws InvalidInputException {
        String[] delovi = s.split(":");
        if (delovi.length != 2) throw new InvalidInputException("Format mora biti HH:mm");
        try {
            return new Time(Integer.parseInt(delovi[0]), Integer.parseInt(delovi[1]));
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Sat i minut moraju biti brojevi.");
        }
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d", sat, minut);
    }
}
