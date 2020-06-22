package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.task;

public interface Completion<T> {

    void onComplete(T arg);

}
