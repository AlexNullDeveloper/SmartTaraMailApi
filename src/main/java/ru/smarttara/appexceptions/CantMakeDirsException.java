package ru.smarttara.appexceptions;

/**
 * class that display situation when user can't create
 * directories, maybe cause user dont have admin priveleges
 *
 * @author a.talismanov on 08.07.2016.
 */
public class CantMakeDirsException extends Exception {

    /**
     * Constructs class with message
     * @param s diagnostic message
     */
    public CantMakeDirsException(String s) {
        super(s);
    }


    /**
     * Just constructs class
     */
    public CantMakeDirsException() {
    }
}
