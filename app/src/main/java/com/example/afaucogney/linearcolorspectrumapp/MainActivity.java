package com.example.afaucogney.linearcolorspectrumapp;

import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import fr.afaucogney.android.Rainbower;
import fr.afaucogney.android.fakear.data.DataListProvider;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                doStuff();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void doStuff() {
        ImageView iv1 = (ImageView) findViewById(R.id.iv1);
        ImageView iv2= (ImageView) findViewById(R.id.iv2);
        ImageView iv3 = (ImageView) findViewById(R.id.iv3);

//        Rainbower s = new Rainbower(this, 0, 100, R.drawable.maskRainbow);


//        iv.setImageBitmap(s.getBitmapSpectrum(iv.getWidth(), iv.getHeight(), DataListProvider.generateRandomValueTable(100, 0, 100, 20)));

        new Rainbower.Builder()
                .fromSize(new Point(iv1.getWidth(),iv1.getHeight()))
                .setDataBounds(0, 100)
                .setLowLevelColor(Rainbower.RED)
                .setHighLevelColor(Rainbower.GREEN)
                .withMask(R.drawable.mask)
                .setMaskAtScale(true)
                .bindWithDataSet(DataListProvider.generateRandomValueTable(100, 0, 100, 20))
                .build(getApplicationContext())
                .bindWithImageView(iv1);

        new Rainbower.Builder()
                .fromImageViewSize(iv2)
                .setDataBounds(0, 1000)
                .setLowLevelColor(Rainbower.GREEN)
                .setHighLevelColor(Rainbower.MAGENTA)
                .withMask(R.drawable.mask)
                .setMaskAtScale(false)
                .bindWithDataSet(DataListProvider.generateRandomValueTable(1000, 0, 1000, 20))
                .build(getApplicationContext())
                .bindWithImageView(iv2);
//
        new Rainbower.Builder()
                .fromImageViewSize(iv3)
                .setDataBounds(0, 100)
                .setLowLevelColor(Rainbower.RED)
                .setHighLevelColor(Rainbower.MAGENTA)
                .bindWithDataSet(DataListProvider.generateRandomValueTable(100, 0, 100, 20))
                .build(getApplicationContext())
                .bindWithImageView(iv3);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
