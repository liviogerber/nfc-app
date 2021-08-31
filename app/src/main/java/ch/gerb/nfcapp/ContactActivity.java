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
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Livio on 11.02.2016.
 */
public class ContactActivity extends Activity {

    private Context _context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Das wird aus ausgeführt wenn die Activity erstellt wird.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);

        if(_context == null) { // fragt ab ob das Object nicht ist.
            _context = this; // _context wird mit dieser klasse gefüllt
        }

        Button buttonBack = (Button)findViewById(R.id.contact_button_back); // sucht Button mit einer ID und speichert ihn in eine Variable.
        buttonBack.setOnClickListener(new View.OnClickListener() { // Da wird die Methode zum Button hinzugefügt.
            @Override
            public void onClick(View view) { // Diese Methhode wird ausgeführt wenn der Button gedrückt wird.
                finish();
            }
        });

        Button buttonWriteTag = (Button)findViewById(R.id.contact_button_writetag); // sucht Button mit einer ID und speichert ihn in eine Variable.
        buttonWriteTag.setOnClickListener(new View.OnClickListener() { // Da wird die Methode zum Button hinzugefügt.
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) { // Diese Methhode wird ausgeführt wenn der Button gedrückt wird.


                String vcarddata = "BEGIN:VCARD\nVERSION:3.0\n"; //Der start für die Visitenkarte wird in eine Variable geschriben.

                EditText name = (EditText)findViewById(R.id.contact_edittext_name);// nimmt die Textbox von der Ansicht und setzt sie in eine Variable
                EditText forename = (EditText)findViewById(R.id.contact_edittext_forename);// nimmt die Textbox von der Ansicht und setzt sie in eine Variable
                if(!name.getText().toString().equals("") && !forename.getText().toString().equals("")) { // Wird gefragt ob etwas in der Textbox steht
                    vcarddata += "FN:" + forename.getText().toString() + " " + name.getText().toString() +"\n"; // Der Name und Vorname wird der Visitenkarte hinzugeführt
                }

                EditText company = (EditText)findViewById(R.id.contact_edittext_company);// nimmt die Textbox von der Ansicht und setzt sie in eine Variable
                if(!company.getText().toString().equals("")) { // Wird gefragt ob etwas in der Textbox steht
                    vcarddata += "ORG:" + company.getText().toString() + "\n"; //Die Firma wird der Visitenkarte hinzugeführt
                }

                EditText tel = (EditText)findViewById(R.id.contact_edittext_tel);// nimmt die Textbox von der Ansicht und setzt sie in eine Variable
                if(!tel.getText().toString().equals("")) { // Wird gefragt ob etwas in der Textbox steht
                    vcarddata += "TEL:" + tel.getText().toString() + "\n"; //Die Telephonenumber wird der Visitenkarte hinzugeführt
                }

                EditText email = (EditText)findViewById(R.id.contact_edittext_email);// nimmt die Textbox von der Ansicht und setzt sie in eine Variable
                if(!email.getText().toString().equals("")) { // Wird gefragt ob etwas in der Textbox steht
                    vcarddata += "EMAIL:" + email.getText().toString() + "\n"; //Die Email adresse wird der Visitenkarte hinzugeführt
                }

                EditText adr = (EditText)findViewById(R.id.contact_edittext_adr);// nimmt die Textbox von der Ansicht und setzt sie in eine Variable
                EditText place = (EditText)findViewById(R.id.contact_edittext_place);// nimmt die Textbox von der Ansicht und setzt sie in eine Variable
                if(!adr.getText().toString().equals("") && !place.getText().toString().equals("")) {  // Wird gefragt ob etwas in der Textbox steht
                    vcarddata += "ADR:" + adr.getText().toString() + "\n" + place.getText().toString() + "\n"; //Die Adresse wird der Visitenkarte hinzugeführt
                }

                vcarddata += "END:VCARD"; //Das Ende der Visitenkarte wird hinzugefügt.

                NdefRecord[] ndefRecords = new NdefRecord[]{NdefRecord.createMime("text/vcard", vcarddata.getBytes())}; //erstellt ein Visitenkarte NdefRecord.

                Intent intent = new Intent(_context, NFCWriterActivity.class); // erstellt eine umgebung für Wechsel vom Activity.
                intent.putExtra("NdefMessage", new NdefMessage(ndefRecords).toByteArray()); // NdefMessage wird beim Wechsel vom Activity weiter gegeben.
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
