package org.ema.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import org.ema.activities.student.StudentGameActivity;

public class EndGameDialog extends DialogFragment{

    private static final String TAG = "FinishGameDialog";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        int points = StudentGameActivity.getPoints();
        StringBuilder finalMessage = new StringBuilder();
        finalMessage.append(getResources().getString(org.ema.R.string.you)).append(" ")
                .append(getResources().getString(org.ema.R.string.made)).append(" ")
                .append(points).append(" ").append(getResources().getString(org.ema.R.string.points));

        builder.setMessage(finalMessage.toString())
                .setPositiveButton(org.ema.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "Thank you!");
                        StudentGameActivity.setPoints(0);
                        getActivity().finish();
                    }
                });
        return builder.create();
    }
}
