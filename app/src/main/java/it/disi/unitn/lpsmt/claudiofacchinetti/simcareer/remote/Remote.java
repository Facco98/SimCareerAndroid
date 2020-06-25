package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.exception.InvalidCarException;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.exception.LoginException;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.exception.NotFoundException;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.Gallery;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.User;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.championship.Championship;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.championship.PilotSubscriptionItem;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.db.UserDbHelper;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.ranking.ChampionshipRanking;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.task.BackgroundTask;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.remote.task.Completion;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.util.Constants;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.util.FileUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Remote {

    private static String TAG ="RemoteAPI";

    private static UserDbHelper userDbHelper;
    private static SQLiteDatabase writableDatabase;
    private static Context applicationContext;

    private Remote(){

    }

    public static void login(@NonNull String email, @NonNull String password, @NonNull Completion<Result<User>> completion) {

        try {
            User u = userDbHelper.getUserFromEmailAndPassword(email, password, writableDatabase);
            if( u != null )
                completion.onComplete(new Result<>(u));
            else
                completion.onComplete(new Result<>(new LoginException("Email o password errata")));
        } catch (Exception e) {
            completion.onComplete(new Result<>(e));
        }

    }

    public static void register(@NonNull User user, @NonNull String password, @Nullable Completion<Result<Boolean>> completion){

        if( userDbHelper != null ){

            try {
                long i = userDbHelper.addUser(user, password, writableDatabase);
                Log.d("DBINSERT", ""+i);
                if(completion != null)
                    completion.onComplete(new Result<>(true));
            } catch ( Exception e ){

                if( completion != null )
                    completion.onComplete(new Result<Boolean>(e));
                else
                    e.printStackTrace();
            }


        }

    }

    public static void init(@NonNull Context context){

        applicationContext = context;
        userDbHelper = new UserDbHelper(context);
        writableDatabase = userDbHelper.getWritableDatabase();
        FileUtil.checkExistance(applicationContext, "campionati", "classifiche");

    }

    public static void getChampionshipsList(@NonNull Completion<Result<List<Championship>>> completion){

        BackgroundTask<Void, List<Championship>> task = new BackgroundTask<Void, List<Championship>>((voids) -> {

            InputStream is = null;
            try {
                is = (InputStream) FileUtil.openFile(applicationContext, "campionati", true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                Gson gson = new Gson();
                Championship.Wrapper wrapper = gson.fromJson(reader, Championship.Wrapper.class);
                reader.close();
                return new Result<List<Championship>>(wrapper.getChampionships());
            } catch (IOException e) {
                return new Result<>(e);
            }

        }, completion);
        task.execute(new Void[1]);

    }

    public static void getChampionshipById(@NonNull String id, @NonNull Completion<Result<Championship>> completion){

        BackgroundTask<Void, Championship> task = new BackgroundTask<>((voids) -> {

            InputStream is = null;
            try {
                is = (InputStream) FileUtil.openFile(applicationContext, "campionati.json", true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                Gson gson = new Gson();
                Championship.Wrapper wrapper = gson.fromJson(reader, Championship.Wrapper.class);
                reader.close();
                Championship result = null;
                Iterator<Championship> it = wrapper.getChampionships().iterator();
                while( it.hasNext() && result == null ){
                    Championship championship = it.next();
                    if( championship.getId().equals(id) )
                        result = championship;
                }
                if( result == null )
                    return new Result<>(new NotFoundException(id));
                else
                    return new Result<>(result);
            } catch (IOException e) {
                return new Result<>(e);
            }


        }, completion);
        task.execute(new Void[1]);

    }

    public static void loadBitmapFromAssets(@NonNull String fullPath, @NonNull Completion<Result<Bitmap>> completion) {

        BackgroundTask<Void, Bitmap> loadImgTask = new BackgroundTask<Void, Bitmap>((voids) -> {

            InputStream is = null;
            try {
                is = applicationContext.getAssets().open(fullPath);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                return new Result<>(bmp);
            } catch (IOException e) {

                return new Result(e);

            }


        }, completion);

        loadImgTask.execute(new Void[1]);

    }


    public static void getBitmapFromURL(@NonNull  String src, @NonNull Completion<Result<Bitmap>> completion) {


        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Result<Bitmap> res = new Result<Bitmap>(myBitmap);
            completion.onComplete(res);
        } catch (Exception e) {
            Result<Bitmap> res = new Result<Bitmap>(e);
            completion.onComplete(res);
        }


    }

    public static void subscribeUserToChampionship( @NonNull Championship championship, @NonNull User user,
                                                    @NonNull String team, @NonNull String car,
                                                    @Nullable Completion<Result<Integer>> completion ) {

        PilotSubscriptionItem subscriptionItem = new PilotSubscriptionItem(user.getName() + " " + user.getSurname(), team, car);
        try {
            championship.addSubscription(subscriptionItem);
            Completion<Result<List<Championship>>> retrieveCompletion = (result) -> {

                if( result.getError() != null ){

                    if( completion != null )
                        completion.onComplete( new Result<>(result.getError()) );

                } else if ( result.getContent() != null ){

                    rewriteChampionshipList(result.getContent(), championship, completion);
                }

            };
            getChampionshipsList(retrieveCompletion);
        } catch (InvalidCarException e) {
            Result<Integer> result = new Result<>(e);
            if( completion != null)
                completion.onComplete(result);
        }


    }


    public static void removeSubscritionToChampionship(@NonNull Championship championship, @NonNull User user,
                                                       @Nullable Completion<Result<Integer>> completion) {

        championship.removeUserSubscription(user);
        Completion<Result<List<Championship>>> retrieveCompletion = (result) -> {

            if( result.getError() != null ){

                if( completion != null )
                    completion.onComplete(new Result<>(result.getError()));

            } else if( result.getContent() != null ){

                rewriteChampionshipList(result.getContent(), championship, completion);

            }

        };
        getChampionshipsList(retrieveCompletion);

    }

    private static void rewriteChampionshipList(List<Championship> content, Championship championship, Completion<Result<Integer>> completion) {

        boolean completed = false;
        for( int i = 0; i < content.size() && !completed; i++ ){

            if( content.get(i).getId().equals(championship.getId()) ) {
                content.set(i, championship);
                completed = true;
            }

        }

        if( completed ) {

            BackgroundTask<Void, Integer> writingTask = new BackgroundTask<>((voids) -> {
                try {
                    Gson gson = new Gson();
                    Championship.Wrapper wrapper = new Championship.Wrapper(content);
                    FileUtil.writeToFile(applicationContext, "campionati", gson.toJson(wrapper),
                            false);
                    return new Result<>(0);
                } catch (IOException e) {
                    return new Result<>(e);
                }
            }, completion);
            writingTask.execute(new Void[1]);
        }




    }

    public static void loadChampionshipsUserIsSubscribed(@NonNull User user,
                                                         @NonNull Completion<Result<List<Championship>>> completion){

        Completion<Result<List<Championship>>> retrieveCompletion = (result) -> {

          if( result.getError() != null ){

              completion.onComplete(result);

          } else if( result.getContent() != null ){

              BackgroundTask<Void, List<Championship>> filterTask = new BackgroundTask<>((voids) -> {
                  List<Championship> championships = new ArrayList<>();
                  for( Championship c : result.getContent() ) {
                      try {
                          if (c != null && c.userPartecipates(user) != null && c.userPartecipates(user).equals(true))
                              championships.add(c);
                      } catch (ParseException e) {

                      }
                  }
                  return new Result<List<Championship>>(championships);
              }, completion);
              filterTask.execute(new Void[1]);
          }

        };

        getChampionshipsList(retrieveCompletion);


    }

    public static void loadRankingForChampionship(@NonNull Championship championship,
                                                  @NonNull Completion<Result<ChampionshipRanking>> completion) {

        BackgroundTask<Void, ChampionshipRanking> task = new BackgroundTask<Void, ChampionshipRanking>((voids) -> {

            InputStream is = null;
            try {
                is = (InputStream) FileUtil.openFile(applicationContext, "classifiche", true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                Gson gson = new Gson();
                ChampionshipRanking.Wrapper wrapper = gson.fromJson(reader, ChampionshipRanking.Wrapper.class);
                reader.close();
                ChampionshipRanking item = null;
                for( ChampionshipRanking ranking : wrapper.getChampionshipRankings() )
                    if( ranking.getId().equals(championship.getId())) {
                        item = ranking;
                    }

                if( item == null )
                   return new Result<>(new NotFoundException(championship.getId()));
                else
                    return new Result<ChampionshipRanking>(item);
            } catch (IOException e) {
                return new Result<>(e);
            }

        }, completion);
        task.execute(new Void[1]);

    }

    public static void loadGalleries(@NonNull Completion<Result<List<Gallery>>> completion){

        BackgroundTask<Void, List<Gallery>> loadGalleriesTaks = new BackgroundTask<>((voids) -> {

            try {
                String[] paths = applicationContext.getAssets().list(Constants.ASSETS_GALLERY_NAME);
                ArrayList<Gallery> galleries = new ArrayList<>();
                if (paths != null) {
                    for (String path : paths) {

                        String[] galleryImages = applicationContext.getAssets().list(Constants.ASSETS_GALLERY_NAME+"/"+path);
                        List<String> galleryContentPaths = new ArrayList<>();
                        if (galleryImages != null) {
                            for (String content : galleryImages) {

                                if (!content.equals(Constants.ASSETS_GALLERY_THUMB))
                                    galleryContentPaths.add(content);

                            }
                        }
                        galleries.add(new Gallery(path,galleryContentPaths));


                    }
                }

                return new Result<List<Gallery>>(galleries);
            } catch ( Exception ex ){

                return new Result<List<Gallery>>(ex);

            }

        }, completion);

        try {
            loadGalleriesTaks.execute(new Void[1]).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void editChampionship(@NonNull Championship championship, @Nullable Completion<Result<Integer>> completion) {

        getChampionshipsList((result) ->{

            if( result.getError() != null ){

                if( completion != null )
                    completion.onComplete(new Result<Integer>(result.getError()));
                else
                    result.getError().printStackTrace();

            } else if( result.getContent() != null ){

                if( completion != null )
                    rewriteChampionshipList(result.getContent(), championship, completion);
                else
                    rewriteChampionshipList(result.getContent(), championship, (res) -> {

                        if( res.getError() != null )
                            res.getError().printStackTrace();

                    });


            }

        });


    }
}
