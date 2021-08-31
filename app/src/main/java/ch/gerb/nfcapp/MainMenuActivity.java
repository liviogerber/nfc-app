package ch.gerb.nfcapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Livio on 11.02.2016.
 */
public class MainMenuActivity extends Activity {

    private Context _context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Das wird aus ausgeführt wenn die Activity erstellt wird.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu); // hier wird die Ansicht(View) der Activity gesetzt.

        NfcManager nfcManager = (NfcManager)getSystemService(Context.NFC_SERVICE); // nimmt den NFC Manager vom Android und speichert es in einer Variable.
        NfcAdapter nfcAdapter = nfcManager.getDefaultAdapter(); // nimmt den NFC Adapter vom NFC Manager und speichert es in einer Variable.
        if(nfcAdapter != null) // Da wird gefragt ob das Smartphone NFC unterstützt.
        {
            //Hier unterstützt das Smartphone NFC .
            if(!nfcAdapter.isEnabled()) //Da wird gefragt ob das Smartphone NFC eingeschaltet ist.
            {
                Toast.makeText(this, "Ihr NFC ist ausschalten.", Toast.LENGTH_LONG).show();
                finish(); // Beended das Activity
            }
        } else {
            finish(); // Beended das Activity
        }

        if(_context == null) { // fragt ab ob das Object nicht ist.
            _context = this; // _context wird mit dieser klasse gefüllt
        }

        Button buttonText = (Button)findViewById(R.id.mainmenu_button_text); // sucht Button mit einer ID und speichert ihn in eine Variable.
        buttonText.setOnClickListener(new View.OnClickListener() { // Da wird die Methode zum Button hinzugefügt.
            @Override
            public void onClick(View view) { // Diese Methhode wird ausgeführt wenn der Button gedrückt wird.
                startActivity(new Intent(_context, TextActivity.class)); // Wechselt zu einer anderen Activity.
            }
        });

        Button buttonURLURI = (Button)findViewById(R.id.mainmenu_button_urluri); // sucht Button mit einer ID und speichert ihn in eine Variable.
        buttonURLURI.setOnClickListener(new View.OnClickListener() { // Da wird die Methode zum Button hinzugefügt.
            @Override
            public void onClick(View view) { // Diese Methhode wird ausgeführt wenn der Button gedrückt wird.
                startActivity(new Intent(_context, URLURIActivity.class)); // Wechselt zu einer anderen Activity.
            }
        });

        Button buttonContact = (Button)findViewById(R.id.mainmenu_button_contact); // sucht Button mit einer ID und speichert ihn in eine Variable.
        buttonContact.setOnClickListener(new View.OnClickListener() { // Da wird die Methode zum Button hinzugefügt.
            @Override
            public void onClick(View view) { // Diese Methhode wird ausgeführt wenn der Button gedrückt wird.
                startActivity(new Intent(_context, ContactActivity.class)); // Wechselt zu einer anderen Activity.
            }
        });

        Button buttonWlanNetwork = (Button)findViewById(R.id.mainmenu_button_wlannetwork); // sucht Button mit einer ID und speichert ihn in eine Variable.
        buttonWlanNetwork.setOnClickListener(new View.OnClickListener() { // Da wird die Methode zum Button hinzugefügt.
            @Override
            public void onClick(View view) { // Diese Methhode wird ausgeführt wenn der Button gedrückt wird.
                startActivity(new Intent(_context, WlanNetworkActivity.class)); // Wechselt zu einer anderen Activity.
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
