package com.mg.uros.klohenglishpatchv2;


import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;



public class MainActivity extends ActionBarActivity {


   final static String dataDir = Environment.getExternalStorageDirectory() + "/Android/data/com.nexon.loh.korgooglev1/files/at2/cacheroot/gamedata/mobilebundles/";
    final static String backupDir = Environment.getExternalStorageDirectory() + "/Android/data/com.mg.uros.klohenglishpatchv2/backup/";

    File dataFolder = new File(dataDir);
    File backupFolder = new File(backupDir);

    FileManager fileManager = new FileManager(MainActivity.this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

        fileManager.copyAssets("englishpatch", dataDir);
        Toast.makeText(MainActivity.this,"You have successfully applied English patch",Toast.LENGTH_LONG).show();
    }

    public void onRestoreKoreanClick(View view) {

        fileManager.copyAssets("koreanpatch",dataDir);
        Toast.makeText(MainActivity.this,"You have successfully restored Korean files",Toast.LENGTH_LONG).show();

    }

    public void onRestoreBackupClick(View view) {
        try {
           fileManager.copyDirectory(backupFolder,dataFolder);
            Toast.makeText(MainActivity.this,"You have successfully restored backup of your files",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onBackupFilesClick(View view) {

        try {
           fileManager.copyDirectory(dataFolder,backupFolder);
            Toast.makeText(MainActivity.this,"You have successfully made backup of your files",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
