package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.exception;

public class EmptyFieldException extends Throwable {

    public final String fieldName;

    public EmptyFieldException( String fieldName ){

        this.fieldName = fieldName;

    }

}
