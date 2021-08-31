package ch.gerb.nfcapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

/**
 * Created by Livio on 11.02.2016.
 */
public class URLURIActivity extends Activity {

    private Context _context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Das wird aus ausgeführt wenn die Activity erstellt wird.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.urluri); // hier wird die Ansicht(View) der Activity gesetzt.

        if(_context == null) { // fragt ab ob das Object nicht ist.
            _context = this; // _context wird mit dieser klasse gefüllt
        }

        final EditText editText = (EditText)findViewById(R.id.urluri_edittext_data); // sucht EditText mit einer ID und speichert ihn in eine Variable.

        Button buttonBack = (Button)findViewById(R.id.urluri_button_back); // sucht Button mit einer ID und speichert ihn in eine Variable.
        buttonBack.setOnClickListener(new View.OnClickListener() { // Da wird die Methode zum Button hinzugefügt.
            @Override
            public void onClick(View view) { // Diese Methhode wird ausgeführt wenn der Button gedrückt wird.
                finish();
            }
        });

        Button buttonWriteTag = (Button)findViewById(R.id.urluri_button_writetag); // sucht Button mit einer ID und speichert ihn in eine Variable.
        buttonWriteTag.setOnClickListener(new View.OnClickListener() { // Da wird die Methode zum Button hinzugefügt.
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) { // Diese Methhode wird ausgeführt wenn der Button gedrückt wird.
                String urluri = editText.getText().toString();

                if (!(urluri.contains("http://") || urluri.contains("https://"))) { // fragt ob es kein http:// oder https://
                    urluri = "http://" + urluri; //Hier wird ein http:// vorne hinzugefügt
                }

                NdefRecord[] ndefRecords = new NdefRecord[]{ NdefRecord.createUri(urluri)}; // NdefRecord wird hier erstellt

                Intent intent = new Intent(_context, NFCWriterActivity.class); // erstellt eine umgebung für Wechsel vom Activity
                intent.putExtra("NdefMessage", new NdefMessage(ndefRecords).toByteArray()); // Ndefmessage wird beim Wechsel vom Activity weiter gegeben.
                startActivity(intent); // Wechselt Activity
            }
        });
    }

    @Override
    protected void onDestroy() { // Das wird aus ausgeführt wenn die Activity Zerstört wird.
        super.onDestroy();
        if(_context != null) { // fragt ab ob das Object alles andere als nicht ist.
            _context = null; // das Object wird zu nichts gemacht
        }
    }
}
