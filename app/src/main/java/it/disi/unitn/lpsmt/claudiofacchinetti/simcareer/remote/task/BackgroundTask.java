package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.task;

import android.os.AsyncTask;
import androidx.annotation.NonNull;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.Result;

public class BackgroundTask<P,R> extends AsyncTask<P, Void, Void> {

    private Result<R> result;
    private Completion<Result<R>> completion;
    private Progression<P,R> progress;

    public BackgroundTask(@NonNull Progression<P,R> progress, @NonNull Completion<Result<R>> completion){
        this.completion = completion;
        this.progress = progress;
    }

    @Override
    protected Void doInBackground(P... params) {
        this.result = this.progress.progress(params);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        this.completion.onComplete(this.result);
    }
}
