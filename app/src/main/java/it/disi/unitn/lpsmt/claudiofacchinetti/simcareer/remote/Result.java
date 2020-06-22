package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote;

import androidx.annotation.Nullable;

public class Result<T> {

    @Nullable
    private T content;

    @Nullable
    private Exception error;

    public Result(Exception e){

        this.content = null;
        this.error = e;

    }

    public Result(T content){

        this.content = content;
        this.error = null;

    }

    public boolean isError(){

        return this.error != null;

    }

    public boolean isSuccessfull(){

        return this.error == null;

    }

    public T getContent(){

        return this.content;

    }

    public Exception getError() {

        return this.error;

    }

}