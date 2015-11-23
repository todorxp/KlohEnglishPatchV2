package com.mg.uros.klohenglishpatchv2;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;


public class MainActivity extends ActionBarActivity {


    final static String GAME_DATA_PATH = Environment.getExternalStorageDirectory() + "/Android/data/com.nexon.loh.korgooglev1/files/at2/cacheroot/gamedata/mobilebundles/";
    final static String BACKUP_PATH = Environment.getExternalStorageDirectory() + "/Android/data/com.mg.uros.klohenglishpatchv2/backup/";
    final static String UPDATES_PATH = Environment.getExternalStorageDirectory() + "/Android/data/com.mg.uros.klohenglishpatchv2/updates/";
    final static String KOREAN_UPDATES_PATH = Environment.getExternalStorageDirectory() + "/Android/data/com.mg.uros.klohenglishpatchv2/updates/korean";
    final static String ENGLISH_UPDATES_PATH = Environment.getExternalStorageDirectory() + "/Android/data/com.mg.uros.klohenglishpatchv2/updates/english";
    final static String UPDATES_PATCH_ZIP_PATH = Environment.getExternalStorageDirectory() + "/Android/data/com.mg.uros.klohenglishpatchv2/updates/patch.zip";
    final static String TEMP_PATH = Environment.getExternalStorageDirectory() + "/Android/data/com.mg.uros.klohenglishpatchv2/temp/";
    final static String TEMP_PATCH_ZIP_PATH = Environment.getExternalStorageDirectory() + "/Android/data/com.mg.uros.klohenglishpatchv2/temp/patch.zip";
    final static String PREFS_NAME = "com.mg.uros.klohenglishpatchv3";

    File GAME_DATA_FOLDER = new File(GAME_DATA_PATH);
    File BACKUP_FOLDER = new File(BACKUP_PATH);
    File UPDATES_FOLDER = new File(UPDATES_PATH);
    File KOREAN_UPDATES_FOLDER = new File(KOREAN_UPDATES_PATH);
    File ENGLISH_UPDATES_FOLDER = new File(ENGLISH_UPDATES_PATH);
    File TEMP_FOLDER = new File(TEMP_PATH);
    File UPDATES_PATCH_ZIP_FILE = new File(UPDATES_PATCH_ZIP_PATH);
    File TEMP_PATCH_ZIP_FILE = new File(TEMP_PATCH_ZIP_PATH);


    private SharedPreferences settings = null;
    private TextView statusValue = null;
    private boolean isInternetPresent ;
    private boolean firstRun;
    private ConnectionDetector cd;
    private FileManager fileManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fileManager = new FileManager(MainActivity.this);
        settings = getSharedPreferences(PREFS_NAME, 0);
        statusValue = (TextView) findViewById(R.id.status_text_value);
        firstRun = settings.getBoolean("firstRun", true);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        if (firstRun) {
            Log.w("activity", "first time");
            SharedPreferences.Editor editor = settings.edit(); // Open the editor for our settings
            editor.putBoolean("firstRun", false); // It is no longer the first run
            editor.putString("status", "Default");
            editor.commit(); // Save all changed settings


        } else {
            String getStatus = settings.getString("status", "Default");
            Log.w("activity", "second time");
            statusValue.setText(getStatus);

        }

        if (!GAME_DATA_FOLDER.isDirectory() || !GAME_DATA_FOLDER.exists()) {
            Log.v("DIR_CHECK", "FOLDER NOT FOUND !");

            String msg =  "Files for Korean Legion Of Heroes are not detected, game is not installed or files are moved away from default location.";
            CustomDialog(true, msg, "ERROR !");

        }
        if (!UPDATES_FOLDER.isDirectory() || !BACKUP_FOLDER.isDirectory() || !TEMP_FOLDER.isDirectory()) {
            FolderSetup();
        }


    }
    @Override
    public void onBackPressed() {

        joinGuildDialog();

    }

    private void SetStatus(String status){

        statusValue.setText(status);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("status", status);
        editor.commit();

    }

    private void joinGuildDialog()
    {
        String msg = "Thanks for using KLOH English patch.\nThis apk has been made by Dirdra , translation files are by Rellim, quests from Chapter 4 are translated by SaMMy\nFor more awesomeness join Tús Nua guild and be in touch with friendly people from all around the world !, 티아미스뭘트 server (1st on list).";
        CustomDialog(true, msg, "EXIT");
    }

    private void CustomDialog(boolean killapp,String message, String title)
    {
        CustomDialog cd = new CustomDialog(MainActivity.this);
        cd.show();
        cd.setTitle(title);
        cd.setMessage(message);
        cd.setCancelable(false);
        if (killapp) cd.killapp = true;
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

            joinGuildDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onEnglishPatchClick(View view) {
        try {
            fileManager.copyDirectory(ENGLISH_UPDATES_FOLDER, GAME_DATA_FOLDER);
            Toast.makeText(MainActivity.this, "You have successfully applied English patch", Toast.LENGTH_LONG).show();
            SetStatus("English");

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "You have failed to apply English patch", Toast.LENGTH_LONG).show();
        }

    }

    public void onRestoreKoreanClick(View view) {

        try {
            fileManager.copyDirectory(KOREAN_UPDATES_FOLDER, GAME_DATA_FOLDER);
            Toast.makeText(MainActivity.this, "You have successfully restored Korean files", Toast.LENGTH_LONG).show();
            SetStatus("Korean");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "You have failed to restore korean files", Toast.LENGTH_LONG).show();
        }


    }

    public void onRestoreBackupClick(View view) {
        try {
            fileManager.copyDirectory(BACKUP_FOLDER, GAME_DATA_FOLDER);
            Toast.makeText(MainActivity.this, "You have successfully restored backup of your files", Toast.LENGTH_LONG).show();
            SetStatus("Backup");

        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "You have failed to restore backup files", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void onBackupFilesClick(View view) {

        try {
            fileManager.copyDirectory(GAME_DATA_FOLDER, BACKUP_FOLDER);
            Toast.makeText(MainActivity.this, "You have successfully made backup of your files", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onCleanInstallClick(View view) {

       try {
            for(File f: GAME_DATA_FOLDER.listFiles())
                if(f.getName().startsWith("text_"))
                    f.delete();

            fileManager.copyDirectory(ENGLISH_UPDATES_FOLDER, GAME_DATA_FOLDER);
            Toast.makeText(MainActivity.this, "You have successfully deleted all current game text files and applied English patch", Toast.LENGTH_LONG).show();
            SetStatus("English");

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "You have failed to apply English patch", Toast.LENGTH_LONG).show();
        }


    }


    public void UpdatesDialog(final Boolean result) {

        if (result) {

            CustomDialog(false, "You have the latest version of translation files", "Updates not found !");

        } else {
            CustomDialog(false, "Click OK to get new updated translation files", "New update found !");
            ApplyUpdate();

        }
    }

    private void ApplyUpdate()
    {
        try {

            boolean deleted = UPDATES_PATCH_ZIP_FILE.delete();
            fileManager.copyDirectory(TEMP_FOLDER, UPDATES_FOLDER);
            deleted = TEMP_PATCH_ZIP_FILE.delete();
            fileManager.DeleteRecursive(KOREAN_UPDATES_FOLDER);
            fileManager.DeleteRecursive(ENGLISH_UPDATES_FOLDER);
            new File(KOREAN_UPDATES_PATH).mkdirs();
            new File(ENGLISH_UPDATES_PATH).mkdirs();
            fileManager.unzip(UPDATES_PATCH_ZIP_FILE, UPDATES_FOLDER);
            fileManager.copyDirectory(ENGLISH_UPDATES_FOLDER, GAME_DATA_FOLDER);

            SetStatus("English");
            Toast.makeText(MainActivity.this, "You have successfully updated translation files, English patch has been applied to game files", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "You have failed to update translation files", Toast.LENGTH_LONG).show();
        }
    }




    private void FolderSetup ()
    {
        try {
            new File(UPDATES_PATH).mkdirs();
            new File(TEMP_PATH).mkdirs();
            new File(KOREAN_UPDATES_PATH).mkdirs();
            new File(ENGLISH_UPDATES_PATH).mkdirs();
            fileManager.copyAssets("koreanpatch", UPDATES_PATH);
            fileManager.unzip(UPDATES_PATCH_ZIP_FILE, UPDATES_FOLDER);
            fileManager.copyDirectory(GAME_DATA_FOLDER, BACKUP_FOLDER);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Something is wrong with device storage", Toast.LENGTH_LONG).show();
        }
    }

    public void onCheckUpdateClick(View view) {

        if(isInternetPresent){
        final  ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, AlertDialog.THEME_HOLO_DARK);
        progressDialog.setTitle("Updates");
        progressDialog.setMessage("Checking for updates.....Please wait....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        final ProgressBar progressBar = new ProgressBar(MainActivity.this);
        progressDialog.show();

        // http://download1515.mediafire.com/jnctcl7clfgg/olon1araglodi5n/patch.zip
        // "http://metal-genesis.com/wp-content/uploads/2015/07/patch.zip"

        Ion.with(MainActivity.this)
                .load("http://metal-genesis.com/wp-content/uploads/2015/07/patch.zip")
                // have a ProgressBar get updated automatically with the percent
                .progressBar(progressBar)
                // and a ProgressDialog
                .progressDialog(progressDialog)
                // can also use a custom callback
                .progress(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {
                        System.out.println("" + downloaded + " / " + total);

                        progressDialog.setMax((int)total);
                        progressDialog.setProgress((int)downloaded);


                    }
                })
                .write(new File(TEMP_PATCH_ZIP_PATH))
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        // download done...
                        // do stuff with the File or error
                        progressDialog.cancel();

                        try {
                            boolean compare1and2 = FileUtils.contentEquals(UPDATES_PATCH_ZIP_FILE, file);
                            Log.v("compare", String.valueOf(compare1and2));

                            UpdatesDialog(compare1and2);


                        } catch (IOException e2) {
                            e2.printStackTrace();
                            Toast.makeText(MainActivity.this, "You have failed to check for updates", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    else{
            String msg =  "Can't  connect to the Internet, check your connection";
            CustomDialog(false, msg, "ERROR !");
    }
}}
