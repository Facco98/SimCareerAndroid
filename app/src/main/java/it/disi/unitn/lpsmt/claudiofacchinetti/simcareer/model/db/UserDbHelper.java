package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserDbHelper extends SQLiteOpenHelper {

    private static String SQL_CREATE_ENTRIES = "CREATE TABLE " + UserContract.TABLE_NAME +" ( " +
            UserContract.COLUMN_NAME_NAME +" TEXT NOT NULL,"+
            UserContract.COLUMN_NAME_EMAIL+" TEXT PRIMARY KEY NOT NULL," +
            UserContract.COLUMN_NAME_SURNAME+" TEXT NOT NULL," +
            UserContract.COLUMN_NAME_BIRTH_DATE+ " DATE NOT NULL,"+
            UserContract.COLUMN_NAME_FAVOURITE_CAR+" TEXT NOT NULL," +
            UserContract.COLUMN_NAME_FAVOURITE_CIRCUIT+" TEXT NOT NULL," +
            UserContract.COLUMN_NAME_FAVOURITE_RACE+" TEXT NOT NULL," +
            UserContract.COLUMN_NAME_LIVING+" TEXT NOT NULL," +
            UserContract.COLUMN_NAME_HATED_CIRCUIT+" TEXT NOT NULL," +
            UserContract.COLUMN_NAME_AVATAR+" BLOB DEFAULT NULL," +
            UserContract.COLUMN_NAME_PASSWORD+" TEXT NOT NULL" +
            ");";

    private static String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + UserContract.TABLE_NAME +";";

    private static Integer VERSION = 1;
    private static String DB_NAME = "User.db";

    public UserDbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public User getUserFromEmailAndPassword(@NonNull String email, @NonNull String password, @Nullable SQLiteDatabase db) throws NoSuchAlgorithmException, ParseException {

        SQLiteDatabase database = db;
        if( database == null )
            database = this.getReadableDatabase();

        Cursor c = database.query(UserContract.TABLE_NAME,
                new String[]{ UserContract.COLUMN_NAME_NAME, UserContract.COLUMN_NAME_SURNAME, UserContract.COLUMN_NAME_EMAIL,
                    UserContract.COLUMN_NAME_LIVING, UserContract.COLUMN_NAME_BIRTH_DATE, UserContract.COLUMN_NAME_FAVOURITE_CAR, UserContract.COLUMN_NAME_FAVOURITE_CIRCUIT,
                    UserContract.COLUMN_NAME_FAVOURITE_RACE, UserContract.COLUMN_NAME_HATED_CIRCUIT,
                    UserContract.COLUMN_NAME_AVATAR },
                UserContract.COLUMN_NAME_EMAIL + " = ? AND " + UserContract.COLUMN_NAME_PASSWORD + " = ?",
                new String[]{ email, new String(MessageDigest.getInstance("SHA256").digest(password.getBytes()), StandardCharsets.UTF_8)},
                null,
                null,
                null,
                "1"
                );
        if( c.getCount() == 0 )
            return null;
        else {

            c.moveToNext();
            String name = c.getString(0), surname = c.getString(1), living = c.getString(3),
                    favCar = c.getString(5), favCir = c.getString(6), favRace = c.getString(7),
                    hatedCir = c.getString(8);
            byte[] avatar = c.getBlob(9);
            String avatarString = Base64.encodeToString(avatar, Base64.DEFAULT);
            Date bDate = new SimpleDateFormat("E MMM dd hh:mm:ss z yyyy", Locale.US).parse(c.getString(4));
            User u = new User(email, name, surname, living, bDate, favCar, favRace, favCir,hatedCir, avatarString);
            return u;

        }
    }

    public long addUser(@NonNull User user, String password, @Nullable SQLiteDatabase db) throws NoSuchAlgorithmException, IOException {

        SQLiteDatabase database = db;
        if (database == null)
            database = this.getWritableDatabase();

        ContentValues stmt = new ContentValues();
        stmt.put(UserContract.COLUMN_NAME_NAME, user.getName());
        stmt.put(UserContract.COLUMN_NAME_SURNAME, user.getSurname());
        stmt.put(UserContract.COLUMN_NAME_EMAIL, user.getEmail());
        stmt.put(UserContract.COLUMN_NAME_LIVING, user.getLiving());
        stmt.put(UserContract.COLUMN_NAME_FAVOURITE_RACE, user.getFavouriteRace());
        stmt.put(UserContract.COLUMN_NAME_FAVOURITE_CIRCUIT, user.getFavouriteCircuit());
        stmt.put(UserContract.COLUMN_NAME_FAVOURITE_CAR, user.getFavouriteCar());
        stmt.put(UserContract.COLUMN_NAME_HATED_CIRCUIT, user.getHatedCircuit());
        stmt.put(UserContract.COLUMN_NAME_PASSWORD, new String(MessageDigest.getInstance("SHA256").digest(password.getBytes()), StandardCharsets.UTF_8));

        Bitmap bmp = user.getAvatar();
        if (bmp != null){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            //stream.close();
            stmt.put(UserContract.COLUMN_NAME_AVATAR, byteArray);
        }  else {
            stmt.put(UserContract.COLUMN_NAME_AVATAR, new byte[0]);
        }

        stmt.put(UserContract.COLUMN_NAME_BIRTH_DATE, user.getBirthDate().toString());
        return database.insert(UserContract.TABLE_NAME, null, stmt);

    }
}
