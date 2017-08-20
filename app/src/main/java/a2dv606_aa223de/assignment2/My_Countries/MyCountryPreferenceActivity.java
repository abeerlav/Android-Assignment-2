package a2dv606_aa223de.assignment2.My_Countries;

import android.app.ActionBar;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceActivity;
import android.provider.Settings;

import java.util.List;

import a2dv606_aa223de.assignment2.R;

public class MyCountryPreferenceActivity  extends PreferenceActivity {
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


    }

    @Override
    protected boolean isValidFragment (String fragmentName) {
        return (MyCountry_Preference_fragment.class.getName().equals(fragmentName));
    }
}