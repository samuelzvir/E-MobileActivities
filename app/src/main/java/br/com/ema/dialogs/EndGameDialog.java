package br.com.ema.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import br.com.ema.R;
import br.com.ema.activities.student.StudentGameActivity;

public class EndGameDialog extends DialogFragment{

    private static final String TAG = "FinishGameDialog";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        int points = StudentGameActivity.getPoints();
        StringBuilder finalMessage = new StringBuilder();
        finalMessage.append(getResources().getString(R.string.you)).append(" ")
                .append(getResources().getString(R.string.made)).append(" ")
                .append(points).append(" ").append(getResources().getString(R.string.points));

        builder.setMessage(finalMessage.toString())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "Thank you!");
                        StudentGameActivity.setPoints(0);
                        getActivity().finish();
                    }
                });
        return builder.create();
    }
}
