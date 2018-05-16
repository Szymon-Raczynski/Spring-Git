package sda.poznan.pl.errorHanding;

public class SDAException extends RuntimeException {

    public SDAException() {
        super();
    }

    public SDAException(String message) {
        super(message);
    }
}
