package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.util;

import android.content.Context;
import android.util.Log;

import java.io.*;
import java.util.List;

public class FileUtil {

    public static final String TAG = "FileUtil";

    public static void writeToFile(Context applicationContext, String fileName, String string, boolean append) throws IOException {

        OutputStream os = (OutputStream) openFile(applicationContext, fileName, false);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
        writer.write(string);
        writer.newLine();
        writer.flush();;

    }

    public static void checkExistance(Context applicationContext, String... files) {

        for( String fileName : files ){

            File file = new File(applicationContext.getExternalFilesDir(null), fileName+".json");
            if( !file.exists() ) {
                try {

                    copyFile(applicationContext, fileName);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        }

    }

    public static Closeable openFile(Context applicationContext, String fileName, boolean input) throws FileNotFoundException {

        File file = new File(applicationContext.getExternalFilesDir(null), fileName+".json");
        if( input )
            return new FileInputStream(file);
        else
            return new FileOutputStream(file);
    }

    public static void copyFile( Context applicationContext, String fileName ) throws IOException {

        OutputStream os = (OutputStream) openFile(applicationContext, fileName, false);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
        InputStream is = applicationContext.getResources().openRawResource(
                applicationContext.getResources().getIdentifier(fileName,
                        "raw", applicationContext.getPackageName()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String read = null;

        while( ( read = reader.readLine() ) != null ){

            writer.write(read);
            writer.newLine();
            writer.flush();

        }

        reader.close();
        writer.close();

        Log.i(TAG, fileName +" file copied");

    }


}
