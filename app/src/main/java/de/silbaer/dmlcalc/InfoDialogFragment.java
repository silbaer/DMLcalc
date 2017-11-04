package de.silbaer.dmlcalc;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoDialogFragment extends DialogFragment {


    public InfoDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Context context = getActivity();
        Resources res = getResources();
        AssetManager assetManager = context.getAssets();
        StringBuilder sb = new StringBuilder();

        try {
            InputStreamReader is = new InputStreamReader(assetManager.open("info.html"));
            BufferedReader reader = new BufferedReader(is);
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        String summary = String.format(sb.toString(),  BuildConfig.VERSION_NAME);

        // create a WebView with the current stats
        WebView webView = new WebView(context);
        webView.loadData(summary, "text/html", null);


        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.action_info);

   //     String text = String.format(res.getString(R.string.txt_Info_Message), BuildConfig.VERSION_NAME);
   //     CharSequence styledText = Html.fromHtml(text);
   //     builder.setMessage(text );
        builder.setView(webView);

        builder.setPositiveButton(R.string.txt_button_about_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_about_dlg, container, false);
//        WebView tv = (WebView) v.findViewById(R.id.aboutText);
//
//
//
//        // Watch for button clicks.
//        Button button = (Button)v.findViewById(R.id.about_button_ok);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // When button is clicked, call up to owning activity.
//              //  ((FragmentDialog)getActivity()).showDialog();
//            }
//        });
//
//        return v;
//    }




}
