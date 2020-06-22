package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.exception;

public class InvalidCarException extends Exception {

    public InvalidCarException(){

        super("Car is not allowed in this championship");

    }

}
