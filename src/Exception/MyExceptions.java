package Exception;

public class MyExceptions {
    public static class InvalidInputException extends Exception {
        public InvalidInputException(String message) {
            super(message);
        }
    }
    public static class DuplicateAirportException extends Exception {
        public DuplicateAirportException(String message) {
            super(message);
        }
    }
    public static class FileFormatException extends Exception {
        private String fileName;
        public FileFormatException(String message, String fileName) {
            super(message);
            this.fileName = fileName;
        }
        public String getFileName() { return fileName; }
    }
}