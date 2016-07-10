package appexceptions;

/**
 * @author a.talismanov on 08.07.2016.
 */
public class CantMakeDirsException extends RuntimeException {
    public CantMakeDirsException(String s) {
        super(s);
    }

    public CantMakeDirsException() {
    }
}
