package ch.gerb.nfcapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by Livio on 11.02.2016.
 */
public class WlanNetworkActivity extends Activity {
    //public final short AUTH_TYPE_OPEN = 0x0001;
    public final short AUTH_TYPE_WPA_PSK = 0x0002;
    //public final short AUTH_TYPE_WPA_EAP = 0x0008;
    //public final short AUTH_TYPE_WPA2_EAP = 0x0010;
    public final short AUTH_TYPE_WPA2_PSK = 0x0020;

    private Context _context = null;
    private short _authenticationType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Das wird aus ausgeführt wenn die Activity erstellt wird.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wlannetwork); // hier wird die Ansicht(View) der Activity gesetzt.

        if(_context == null) { // fragt ab ob das Object nicht ist.
            _context = this; // _context wird mit dieser klasse gefüllt
        }

        final Spinner authenticationTypeSpinner = (Spinner) findViewById(R.id.wlannetwork_spinner_authenticationtype); // gibt denn Spinner vom layout in eine Variable
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinnerauthenticationtype_array, android.R.layout.simple_spinner_item);// erstellt die das was angezeigt wird.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // setzt den Style von der Auswahlliste.
        authenticationTypeSpinner.setAdapter(adapter); // setzt den Adapter.
        authenticationTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // Setzt den OnItemSelectedListener
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) { //Wird aus geführt wenn man ein anderes Item im Spinner auswählt.
                switch (pos) // Hier wird pos Variable in den switch gesetzt.
                {
                    /*default: // wird ausgeführt wenn es nichts von den anderen vier ist.
                        _authenticationType = AUTH_TYPE_OPEN; // setzt die Verschlüsselung auf vom wlan auf Offen.
                        break;*/
                    default: // wird ausgeführt wenn die pos Variable 1 ist.
                        _authenticationType = AUTH_TYPE_WPA_PSK;  // setzt die Verschlüsselung auf vom wlan auf WPA PSK.
                        break;
                    case 1: // wird ausgeführt wenn die pos Variable 2 ist.
                        _authenticationType = AUTH_TYPE_WPA2_PSK;  // setzt die Verschlüsselung auf vom wlan auf WPA2 PSK.
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        Button buttonBack = (Button)findViewById(R.id.wlannetwork_button_back); // sucht Button mit einer ID und speichert ihn in eine Variable.
        buttonBack.setOnClickListener(new View.OnClickListener() { // Da wird die Methode zum Button hinzugefügt.
            @Override
            public void onClick(View view) { // Diese Methhode wird ausgeführt wenn der Button gedrückt wird.
                finish();
            }
        });

        Button buttonWriteTag = (Button)findViewById(R.id.wlannetwork_button_writetag); // sucht Button mit einer ID und speichert ihn in eine Variable.
        buttonWriteTag.setOnClickListener(new View.OnClickListener() { // Da wird die Methode zum Button hinzugefügt.

            @Override
            public void onClick(View view) { // Diese Methhode wird ausgeführt wenn der Button gedrückt wird.
                Intent intent = new Intent(_context, NFCWriterActivity.class); // erstellt eine umgebung für Wechsel vom Activity
                intent.putExtra("NdefMessage", generateNdefMessage(((EditText) findViewById(R.id.wlannetwork_editText_ssid)).getText().toString(), _authenticationType, ((EditText) findViewById(R.id.wlannetwork_edittext_password)).getText().toString()).toByteArray()); // Ndefmessage generiert und zum beschreiben weiter gegeben.
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


    //Das habe ich von diesem link kopiert.
    //https://github.com/bparmentier/WiFiKeyShare/blob/master/app/src/main/java/be/brunoparmentier/wifikeyshare/utils/NfcUtils.java
    private NdefMessage generateNdefMessage(String ssid , short authType, String networkKey) {
        byte[] payload = generateNdefPayload(ssid , authType, networkKey);
        NdefRecord mimeRecord = new NdefRecord( NdefRecord.TNF_MIME_MEDIA, "application/vnd.wfa.wsc".getBytes(Charset.forName("US-ASCII")), new byte[0], payload);
        return new NdefMessage(new NdefRecord[] { mimeRecord });
    }

    //Das habe ich von diesem link kopiert.
    //https://github.com/bparmentier/WiFiKeyShare/blob/master/app/src/main/java/be/brunoparmentier/wifikeyshare/utils/NfcUtils.java
    private byte[] generateNdefPayload(String ssid, short authType, String networkKey) {

        short ssidSize = (short)ssid.getBytes().length;
        short networkKeySize = (short)networkKey.getBytes().length;

        ByteBuffer buffer = ByteBuffer.allocate(18 + ssidSize + networkKeySize);
        buffer.putShort((short)0x100e);
        buffer.putShort((short)(14 + ssidSize + networkKeySize));

        buffer.putShort((short) 0x1045);
        buffer.putShort(ssidSize);
        buffer.put(ssid.getBytes());

        buffer.putShort((short)0x1003);
        buffer.putShort((short)2);
        buffer.putShort(authType);

        buffer.putShort((short) 0x1027);
        buffer.putShort(networkKeySize);
        buffer.put(networkKey.getBytes());

        return buffer.array();
    }
}
