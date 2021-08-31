package ch.gerb.nfcapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Livio on 13.02.2016.
 */
public class ErrorActivity extends Activity {
    public static void Error(Activity activity, String tag, String message) {
        Intent errorIntent = new Intent(activity, ErrorActivity.class); // erstellt eine umgebung für Wechsel vom Activity.
        if(tag != null)message = tag + ":" + message;// fragt ob tag etwas drin ist.
        errorIntent.putExtra("Error", message); // übergibt was an das ErrorActivity mit einer id.
        activity.startActivity(errorIntent); // Wechselt Activity
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) { // Wird ausgeführt wen man ein Knopf am Smartphone drückt.
        if (keyCode == KeyEvent.KEYCODE_BACK ) { // Wird abgefragt ob es der zurück knopf ist.
             BacktoMainMenu();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {  // Das wird aus ausgeführt wenn die Activity erstellt wird.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.error); // hier wird die Ansicht(View) der Activity gesetzt.

        Button buttonbacktomainmenu = (Button)findViewById(R.id.error_button_backtomainmenu); // sucht Button mit einer ID und speichert ihn in eine Variable.
        buttonbacktomainmenu.setOnClickListener(new View.OnClickListener() { // Da wird die Methode zum Button hinzugefügt.
            @Override
            public void onClick(View view) { // Diese Methhode wird ausgeführt wenn der Button gedrückt wird.
                BacktoMainMenu();
            }
        });

        Button buttonagain = (Button)findViewById(R.id.error_button_again); // sucht Button mit einer ID und speichert ihn in eine Variable.
        buttonagain.setOnClickListener(new View.OnClickListener() { // Da wird die Methode zum Button hinzugefügt.
            @Override
            public void onClick(View view) { // Diese Methhode wird ausgeführt wenn der Button gedrückt wird.
                finish();// beendet das Activity.
            }
        });

        EditText editText = (EditText)findViewById(R.id.error_editText_errormessage); // sucht EditText mit einer ID und speichert ihn in eine Variable.
        editText.setText(getIntent().getStringExtra("Error"), TextView.BufferType.EDITABLE); // Der Text wird gezetzt.
        editText.setKeyListener(null); // macht das man die Textbox nicht beschreiben kann.
    }

    private void BacktoMainMenu() {
        Intent mainMenuIntent = new Intent(this,MainMenuActivity.class); // erstellt eine umgebung für Wechsel vom Activity.
        mainMenuIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Löscht die History von dem App.
        mainMenuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Macht ein neuer Task.
        startActivity(mainMenuIntent); // Wechselt Activity
    }
}
