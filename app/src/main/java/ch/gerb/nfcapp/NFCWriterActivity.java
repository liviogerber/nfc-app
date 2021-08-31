package ch.gerb.nfcapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Livio on 11.02.2016.
 */
public class NFCWriterActivity extends Activity {
    private NfcAdapter _nfcAdapter = null;
    private NdefMessage _ndefMessage;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { // Wird ausgeführt wen man ein Knopf am Smartphone drückt.
        if (keyCode == KeyEvent.KEYCODE_BACK ) { // Wird abgefragt ob es der zurück knopf ist.
            disableTagWriteMode(); // Deaktivirt den schreib mode.
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Das wird aus ausgeführt wenn die Activity erstellt wird.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfcwriter); // hier wird die Ansicht(View) der Activity gesetzt.

        Button buttonCancel = (Button)findViewById(R.id.nfcwriter_cancel_button); // sucht Button mit einer ID und speichert ihn in eine Variable.
        buttonCancel.setOnClickListener(new View.OnClickListener() { // Da wird die Methode zum Button hinzugefügt.
            @Override
            public void onClick(View view) { // Diese Methhode wird ausgeführt wenn der Button gedrückt wird.
            disableTagWriteMode(); // Deaktivirt den schreib mode.
            finish(); // Beended das Activity.
            }
        });

        try {
            _ndefMessage = new NdefMessage(getIntent().getByteArrayExtra("NdefMessage"));
        } catch (FormatException e) { //Das wird Ausgeführt wen ein Error passiert.
            Log.e(this.getClass().getName(), "Übergabeparameter konnte nicht gefunden werden", e); // Speichert Error im Log
            ErrorActivity.Error(this, null, "Ein Fehler ist aufgetretten!"); // Zeigt den Error an.
        }

        if(_nfcAdapter == null) { // Fragt ab ob das Object nicht ist.
            _nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        }
    }

    @Override
    protected void onResume() { // Wird ausgeführt wenn das Activity mit der Arbeit wieder aufnimmt.
        super.onResume();
        enableTagWriteMode(); // Aktivirt den schreib mode.
    }

    @Override
    protected void onPause() { // Wird ausgeführt wenn das Activity mit der Arbeit eine Pause macht.
        super.onPause();
        disableTagWriteMode(); // Deaktivirt den schreib mode.
    }

    @Override
    protected void onDestroy() { // Das wird aus ausgeführt wenn die Activity Zerstört wird.
        super.onDestroy();

        if(_nfcAdapter != null) { // Fragt ab ob das Object alles andere als nicht ist.
            _nfcAdapter = null; // Das Object wird zu nichts gemacht.
        }
    }

    private void enableTagWriteMode() {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,new Intent(this, NFCWriterActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0); //Hier wird eine beschreibung von einer beschribung für ein Activity erstellt.
        IntentFilter[] intentFilters = new IntentFilter[] { new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED), new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED) }; // Hier wird eine beschreibung von den Intent Werte erstellt.
        String[][] techList = new String[][] {new String[] { NdefFormatable.class.getName() }, new String[] { Ndef.class.getName() } }; //Hier wird eine NFC technologie liste Erstellt
        _nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, techList); // Hier wird aktiviert das die anderen Nfc Apps die NFC Tags nicht mehr bekommen.
    }

    private void disableTagWriteMode() {
        _nfcAdapter.disableForegroundDispatch(this);// Hier wird deaktiviert das die Blockade für die anderen NFC Apps auf gehoben.
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String action = intent.getAction();
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) { // Wird gefragt ob es im Schreib mode ist oder wenn die Intent Action gleich ist wie ACTION_NDEF_DISCOVERED.
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG); // Gibt das Eingescannte NFC zurück.
            if (writeTag(detectedTag ,_ndefMessage)) {
                Toast.makeText(this, "NFC Tag wurde beschrieben", Toast.LENGTH_LONG).show();
                BacktoMainMenu();  // Beended das Activity und geht zum Haubtmenü.
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) { // Wird gefragt ob es im Schreib mode ist oder wenn die Intent Action gleich ist wie ACTION_TECH_DISCOVERED.
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG); // Gibt das Eingescannte NFC zurück.
            if (writeTag(detectedTag ,_ndefMessage)) {
                Toast.makeText(this, "NFC Tag wurde beschrieben", Toast.LENGTH_LONG).show();
                BacktoMainMenu();  // Beended das Activity und geht zum Haubtmenü.
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private boolean writeTag(Tag tag, NdefMessage message)  {
        try {
            Ndef ndef = Ndef.get(tag); //Macht möglich, Ndefmessages durch Ndef auf das Tag zu schreiben
            if (ndef != null) { // Fragt ab ob das Objekt nichts ist.
                ndef.connect(); // Verbindet sich mit dem NFC Tag.
                if(!ndef.isWritable()) {
                    ErrorActivity.Error(this, null, "NFC Tag kann nicht beschrieben werden."); // Zeigt den Error an.
                    return false;
                }
                if(ndef.getMaxSize() < message.getByteArrayLength()) {
                    ErrorActivity.Error(this, null, "NFC Tag ist zu klein, Sie möchten zu viele Daten speichern."); // Zeigt den Error an.
                    return false;
                }
                ndef.writeNdefMessage(message); // Beschreibt es mit der Nachricht.
                ndef.close(); // Das schliesst die Verbindung.
                return true;
            } else {
                //Hier wird herkommen wenn NDEF nichts ist und hier wird das NFC Tag Formatiert.
                NdefFormatable format = NdefFormatable.get(tag); //Macht möglich, Ndefmessages durch NdefFormatable auf das Tag zu schreiben
                if (format != null) { // Fragt ab ob das Object nicht ist.
                    format.connect(); // Verbindet sich mit dem NFC Tag.
                    format.format(message); // Beschreibt es mit der Nachricht.
                    format.close(); // Das Schlist die Verbindung.
                    return true;
                }
            }
        } catch (Exception e) { //Das wird Ausgeführt wen ein Error passiert.
            Log.e(this.getClass().getName(), "Undefinierbarer Fehler beim Schreiben des NFC Tags", e); // Speichert Error im Log
            ErrorActivity.Error(this, null, "Ein Fehler ist aufgetretten, versuchen Sie es erneut"); // Zeigt den Error an.
            return false;
        }
        ErrorActivity.Error(this, null, "Diese NFC Technologie wird nicht unterschtützt."); // Zeigt den Error an.
        return false;
    }

    private void BacktoMainMenu() {
        Intent mainMenuIntent = new Intent(this,MainMenuActivity.class); // erstellt eine umgebung für Wechsel vom Activity.
        mainMenuIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Löscht die History von dem App
        mainMenuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Macht ein neuer Task.
        startActivity(mainMenuIntent); // Wechselt Activity
    }
}
