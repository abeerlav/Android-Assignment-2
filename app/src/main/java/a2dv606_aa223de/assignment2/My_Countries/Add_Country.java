package a2dv606_aa223de.assignment2.My_Countries;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import a2dv606_aa223de.assignment2.R;

/**
 * Created by Abeer on 2015-08-20.
 */

public class Add_Country extends Activity {
    private EditText country_editText, year_editText;
    private Button addButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__country);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        year_editText = (EditText) findViewById(R.id.entery_year);
        country_editText = (EditText) findViewById(R.id.enter_country);
        addButton = (Button) findViewById(R.id.add_country_button);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String country = "";
                int year = 0;
                try {
                    country = country_editText.getText().toString();
                    year = Integer.valueOf(year_editText.getText().toString());
                }catch (NumberFormatException e){
                    Toast.makeText(Add_Country.this, R.string.input_msg, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent result = new Intent();
                result.putExtra("YEAR", year);
                result.putExtra("COUNTRY", country);
                setResult(RESULT_OK, result);
                finish();
                }


        });
    }


}
