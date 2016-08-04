package com.opendev.buket.club.engineer;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.opendev.buket.club.R;
import com.opendev.buket.club.tools.PreferenceCache;

import java.io.IOException;

/**
 * Created by vernau on 8/4/16.
 */
public class EngineerDialogFragment extends DialogFragment {

    EditText editText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_engineer_menu, null);
        editText = (EditText) view.findViewById(R.id.engineer_token);

        editText.setText(PreferenceCache.getString(getActivity(), PreferenceCache.KEY_GCM_TOKEN));

        builder.setView(view)
                .setPositiveButton(R.string.engineer_push, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        doGcmSend();
                    }
                })
                .setNegativeButton(R.string.engineer_close, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EngineerDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    protected void doGcmSend() {
        final Activity activity = this.getActivity();
        final Message.Builder messageBuilder = new Message.Builder();

        String restrictedPackageName = "com.opendev.buket.club";

        messageBuilder.restrictedPackageName(restrictedPackageName.trim());

        final String apiKey = getString(R.string.sender_api_key);
        final String registrationId = PreferenceCache.getString(getActivity(), PreferenceCache.KEY_GCM_TOKEN);

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                GcmServerSideSender sender = new GcmServerSideSender(apiKey);
                try {
                    Thread.sleep(5000);
                    sender.sendHttpPlaintextDownstreamMessage(registrationId,
                            messageBuilder.build());

                } catch (final IOException e) {
                    return e.getMessage();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    Toast.makeText(activity,
                            "send message failed: " + result,
                            Toast.LENGTH_LONG).show();
                }
            }
        }.execute();

    }
}
