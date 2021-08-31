package ch.gerb.nfcapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Created by Livio on 11.02.2016.
 */
public class TextActivity extends Activity {

    private Context _context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Das wird aus ausgeführt wenn die Activity erstellt wird.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text); // hier wird die Ansicht(View) der Activity gesetzt.

        if(_context == null) { // fragt ab ob das Object nicht ist.
            _context = this;
        }

        final EditText editText = (EditText)findViewById(R.id.text_edittext_data);

        Button buttonBack = (Button)findViewById(R.id.text_button_back); // sucht Button mit einer ID und speichert ihn in eine Variable.
        buttonBack.setOnClickListener(new View.OnClickListener() { // Da wird die Methode zum Button hinzugefügt.
            @Override
            public void onClick(View view) { // Diese Methhode wird ausgeführt wenn der Button gedrückt wird.
                finish();
            }
        });

        Button buttonWriteTag = (Button)findViewById(R.id.text_button_writetag); // sucht Button mit einer ID und speichert ihn in eine Variable.
        buttonWriteTag.setOnClickListener(new View.OnClickListener() { // Da wird die Methode zum Button hinzugefügt.
            @Override
            public void onClick(View view) { // Diese Methhode wird ausgeführt wenn der Button gedrückt wird.

                NdefRecord record = null;
                try
                {
                    record = createTextRecord(null, editText.getText().toString());
                }
                catch (UnsupportedEncodingException e)
                {
                    Log.e(TextActivity.class.getName(), "Fehler im TextActivity", e);
                    ErrorActivity.Error((TextActivity)_context, null, "Ein Fehler ist aufgetretten!"); // Zeigt den Error an.
                }

                NdefRecord[] ndefRecords = new NdefRecord[] { record }; // erstellt ein NdefRecord mit dem Text.

                Intent intent = new Intent(_context, NFCWriterActivity.class); // erstellt eine umgebung für Wechsel vom Activity
                intent.putExtra("NdefMessage", new NdefMessage(ndefRecords).toByteArray()); // Ndefmessage wird beim Wechsel vom Activity weiter gegeben.
                startActivity(intent); // Wechselt Activity
            }
        });
    }

    //Das habe ich kopiert von https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/nfc/NdefRecord.java
    public static NdefRecord createTextRecord(String languageCode, String text) throws UnsupportedEncodingException {
        if (text == null) throw new NullPointerException("text is null");
        byte[] textBytes = text.getBytes("UTF-8");
        byte[] languageCodeBytes = null;
        if (languageCode != null && !languageCode.isEmpty()) {
            languageCodeBytes = languageCode.getBytes("US-ASCII");
        } else {
            languageCodeBytes = Locale.getDefault().getLanguage().getBytes("US-ASCII");
        }
        // We only have 6 bits to indicate ISO/IANA language code.
        if (languageCodeBytes.length >= 64) {
            throw new IllegalArgumentException("language code is too long, must be <64 bytes.");
        }
        ByteBuffer buffer = ByteBuffer.allocate(1 + languageCodeBytes.length + textBytes.length);
        byte status = (byte) (languageCodeBytes.length & 0xFF);
        buffer.put(status);
        buffer.put(languageCodeBytes);
        buffer.put(textBytes);
        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, null, buffer.array());
    }

    @Override
    protected void onDestroy() { // Das wird aus ausgeführt wenn die Activity Zerstört wird.
        super.onDestroy();
        if(_context != null) { // fragt ab ob das Object alles andere als nicht ist.
            _context = null; // das Object wird zu nichts gemacht
        }
    }
}
