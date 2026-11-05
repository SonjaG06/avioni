package Modeli;

import Exception.MyExceptions.*;

public class Airport {
    private String code;
    private String name;
    private int x;
    private int y;
    private boolean vidljiv = true;

    public Airport(String code, String name, int x, int y) throws InvalidInputException {
        proveriKod(code);
        proveriKoordinate(x, y);
        this.code = code;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    private void proveriKod(String code) throws InvalidInputException {
        if (code == null || !code.matches("[A-Z]{3}")) {
            throw new InvalidInputException("Kod aerodroma mora imati tačno 3 velika slova (npr. BEG).");
        }
    }

    private void proveriKoordinate(int x, int y) throws InvalidInputException {
        if (x < -180 || x > 180) {
            throw new InvalidInputException("X koordinata mora biti između -180 i 180.");
        }
        if (y < -90 || y > 90) {
            throw new InvalidInputException("Y koordinata mora biti između -90 i 90.");
        }
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public int getX() { return x; }
    public int getY() { return y; }

    public boolean isVidljiv() { return vidljiv; }
    public void setVidljiv(boolean vidljiv) { this.vidljiv = vidljiv; }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Airport)) return false;
        return code.equals(((Airport) obj).code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}