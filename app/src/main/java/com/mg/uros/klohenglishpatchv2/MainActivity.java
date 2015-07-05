package com.mg.uros.klohenglishpatchv2;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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


    final static String dataDir = Environment.getExternalStorageDirectory() + "/Android/data/com.nexon.loh.korgooglev1/files/at2/cacheroot/gamedata/mobilebundles/";
    final static String backupDir = Environment.getExternalStorageDirectory() + "/Android/data/com.mg.uros.klohenglishpatchv2/backup/";
    final static String updatesDir = Environment.getExternalStorageDirectory() + "/Android/data/com.mg.uros.klohenglishpatchv2/updates/";
    final static String koreanUpdatesDir = Environment.getExternalStorageDirectory() + "/Android/data/com.mg.uros.klohenglishpatchv2/updates/korean";
    final static String englishUpdatesDir = Environment.getExternalStorageDirectory() + "/Android/data/com.mg.uros.klohenglishpatchv2/updates/english";
    final static String patch = Environment.getExternalStorageDirectory() + "/Android/data/com.mg.uros.klohenglishpatchv2/updates/patch.zip";
    final static String tempDir = Environment.getExternalStorageDirectory() + "/Android/data/com.mg.uros.klohenglishpatchv2/temp/";
    final static String tempPatch = Environment.getExternalStorageDirectory() + "/Android/data/com.mg.uros.klohenglishpatchv2/temp/patch.zip";


    File dataFolder = new File(dataDir);
    File backupFolder = new File(backupDir);
    File patchFile = new File(patch);
    File updateFolder = new File(updatesDir);
    File tempFolder = new File (tempDir);
    File tempFile = new File(tempPatch);
    File koreanUpdatedFolder = new File (koreanUpdatesDir);
    File englishUpdatedFolder = new File (englishUpdatesDir);



    FileManager fileManager = new FileManager(MainActivity.this);
    SharedPreferences settings = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        String PREFS_NAME = "com.mg.uros.klohenglishpatchv2";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        TextView statusValue =  (TextView) findViewById(R.id.status_text_value);

        boolean firstRun = settings.getBoolean("firstRun", true); // Is it first run? If not specified, use "true"


        if (firstRun) {
            Log.w("activity", "first time");

            SharedPreferences.Editor editor = settings.edit(); // Open the editor for our settings
            editor.putBoolean("firstRun", false); // It is no longer the first run
            editor.putString("status", "Default");
            editor.commit(); // Save all changed settings



            try {
                new File(updatesDir).mkdirs();
                new File(tempDir).mkdirs();
                new File(koreanUpdatesDir).mkdirs();
                new File(englishUpdatesDir).mkdirs();
                fileManager.copyAssets("koreanpatch", updatesDir);
                fileManager.unzip(patchFile, updateFolder);
                fileManager.copyDirectory(dataFolder, backupFolder);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "SD card is not present or kloh is not installed", Toast.LENGTH_LONG).show();
            }

        } else {
            String getStatus = settings.getString("status", "Default");
            Log.w("activity", "second time");


           statusValue.setText(getStatus);

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
        try {
            fileManager.copyDirectory(englishUpdatedFolder,dataFolder);
            Toast.makeText(MainActivity.this,"You have successfully applied English patch",Toast.LENGTH_LONG).show();

            String PREFS_NAME = "com.mg.uros.klohenglishpatchv2";
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            TextView statusValue =  (TextView) findViewById(R.id.status_text_value);

            statusValue.setText("English");

            SharedPreferences.Editor editor = settings.edit();
            editor.putString("status", "English");
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "You have failed to apply English patch", Toast.LENGTH_LONG).show();
        }

    }

    public void onRestoreKoreanClick(View view) {

        try {
            fileManager.copyDirectory(koreanUpdatedFolder,dataFolder);
            Toast.makeText(MainActivity.this,"You have successfully restored Korean files",Toast.LENGTH_LONG).show();
            String PREFS_NAME = "com.mg.uros.klohenglishpatchv2";
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            TextView statusValue =  (TextView) findViewById(R.id.status_text_value);
            statusValue.setText("Korean");

            SharedPreferences.Editor editor = settings.edit();
            editor.putString("status", "Korean");
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "You have failed to restore korean files", Toast.LENGTH_LONG).show();
        }




    }

    public void onRestoreBackupClick(View view) {
        try {
            fileManager.copyDirectory(backupFolder, dataFolder);
            Toast.makeText(MainActivity.this,"You have successfully restored backup of your files",Toast.LENGTH_LONG).show();
            String PREFS_NAME = "com.mg.uros.klohenglishpatchv2";
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            TextView statusValue =  (TextView) findViewById(R.id.status_text_value);

            statusValue.setText("Backup");
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("status", "Backup");
            editor.commit();

        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "You have failed to restore backup files", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void onBackupFilesClick(View view) {

        try {
            fileManager.copyDirectory(dataFolder, backupFolder);
            Toast.makeText(MainActivity.this,"You have successfully made backup of your files",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void alertDialog(final Boolean result)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);

        if (result)
        {
            builder1.setTitle("Updates not found !");
            builder1.setMessage("You have the latest version of translation files");
        }
        else
        {
            builder1.setTitle("New update found !");
            builder1.setMessage("Click OK to get new updated translation files");
        }


        builder1.setCancelable(true);

        builder1.setNeutralButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        if (!result)
                        {
                            try {

                                boolean deleted = patchFile.delete();
                                fileManager.copyDirectory(tempFolder,updateFolder);
                                deleted = tempFile.delete();
                                fileManager.DeleteRecursive(koreanUpdatedFolder);
                                fileManager.DeleteRecursive(englishUpdatedFolder);
                                new File(koreanUpdatesDir).mkdirs();
                                new File(englishUpdatesDir).mkdirs();
                                fileManager.unzip(patchFile,updateFolder);
                                Toast.makeText(MainActivity.this, "You have successfully updated translation files, use patch option again", Toast.LENGTH_LONG).show();

                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "You have failed to update translation files", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    public void onCheckUpdateClick(View view) {


        final  ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
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
                .write(new File(tempPatch))
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        // download done...
                        // do stuff with the File or error
                        progressDialog.cancel();

                        try {
                            boolean compare1and2 = FileUtils.contentEquals(patchFile, file);
                            Log.v("compare", String.valueOf(compare1and2));

                            alertDialog(compare1and2);


                        } catch (IOException e2) {
                            e2.printStackTrace();
                            Toast.makeText(MainActivity.this, "You have failed to check for updates", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
