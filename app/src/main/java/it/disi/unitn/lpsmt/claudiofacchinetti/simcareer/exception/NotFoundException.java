package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.exception;

public class NotFoundException extends Exception {

    public NotFoundException( String id ){

        super("Champshionshid not found for ID: " + id);

    }

}
