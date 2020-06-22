package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.task;

import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.Result;

public interface Progression<P,R> {

    Result<R> progress(P... params);

}
