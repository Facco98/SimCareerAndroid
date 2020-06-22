package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.exception;

public class RemoteNotInitializedException extends Exception {

    public RemoteNotInitializedException(){
        super("Remote is not initialized; call the init method");
    }

}
