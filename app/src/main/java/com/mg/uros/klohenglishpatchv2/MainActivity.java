package com.mg.uros.klohenglishpatchv2;

import android.content.res.AssetManager;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MainActivity extends ActionBarActivity {


   final static String dataDir = Environment.getExternalStorageDirectory() + "/Android/data/com.nexon.loh.korgooglev1/files/at2/cacheroot/gamedata/mobilebundles/";
    final static String backupDir = Environment.getExternalStorageDirectory() + "/Android/data/com.mg.uros.klohenglishpatchv2/backup/";

    File dataFolder = new File(dataDir);
    File backupFolder = new File(backupDir);

    private void copyAssets(String scrDir) {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list(scrDir);
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        for(String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(scrDir + "/" + filename);
                File outFile = new File(dataDir, filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }








    // If targetLocation does not exist, it will be created.
    public void copyDirectory(File sourceLocation , File targetLocation)
            throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists() && !targetLocation.mkdirs()) {
                throw new IOException("Cannot create dir " + targetLocation.getAbsolutePath());
            }

            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {

            // make sure the directory we plan to store the recording in exists
            File directory = targetLocation.getParentFile();
            if (directory != null && !directory.exists() && !directory.mkdirs()) {
                throw new IOException("Cannot create dir " + directory.getAbsolutePath());
            }

            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    // Make backup

        try {
            copyDirectory(dataFolder,backupFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

      if (id == R.id.exit_menu_item) {

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onEnglishPatchClick(View view) {

        copyAssets("englishpatch");
        Toast.makeText(MainActivity.this,"You have successfully applied English patch",Toast.LENGTH_LONG).show();
    }

    public void onRestoreKoreanClick(View view) {

        copyAssets("koreanpatch");
        Toast.makeText(MainActivity.this,"You have successfully restored Korean files",Toast.LENGTH_LONG).show();

    }

    public void onRestoreBackupClick(View view) {
        try {
            copyDirectory(backupFolder,dataFolder);
            Toast.makeText(MainActivity.this,"You have successfully restored backup of your files",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onBackupFilesClick(View view) {

        try {
            copyDirectory(dataFolder,backupFolder);
            Toast.makeText(MainActivity.this,"You have successfully made backup of your files",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
