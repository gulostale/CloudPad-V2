package com.jatar_000.cloudpad.notes.android.fragment.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jatar_000.cloudpad.notes.R;

public class AboutFragmentLicenseTab extends Fragment {
    public static final String GNU_GENERAL_PUBLIC_LICENSE = "https://github.com/gulostale/cloudpad/blob/master/LICENSE";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about_license_tab, container, false);
        Button button = (Button) v.findViewById(R.id.about_app_license_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GNU_GENERAL_PUBLIC_LICENSE)));
            }
        });
        ((TextView) v.findViewById(R.id.about_app_icon_disclaimer)).setMovementMethod(LinkMovementMethod.getInstance());
        return v;
    }
}