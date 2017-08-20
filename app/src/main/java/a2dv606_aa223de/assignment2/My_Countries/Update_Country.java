package a2dv606_aa223de.assignment2.My_Countries;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import a2dv606_aa223de.assignment2.R;

public class Update_Country extends Activity {

    private int year;
    private String country;
    private EditText yearText,countryText;
    private  Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_country);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

         yearText = (EditText) findViewById(R.id.entery_year);
         countryText = (EditText) findViewById(R.id.enter_country);
         updateButton = (Button) findViewById(R.id.update_country_button);


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    country = countryText.getText().toString();
                      year = Integer.valueOf(yearText.getText().toString());
                }catch (NumberFormatException e){
                    Toast.makeText(Update_Country.this, R.string.input_msg, Toast.LENGTH_SHORT).show();
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
