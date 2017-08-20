package a2dv606_aa223de.assignment2.My_Countries;

import android.app.ActionBar;
import android.preference.PreferenceFragment;
import android.os.Bundle;

import a2dv606_aa223de.assignment2.R;

public class MyCountry_Preference_fragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.simple_pref);


    }
}
