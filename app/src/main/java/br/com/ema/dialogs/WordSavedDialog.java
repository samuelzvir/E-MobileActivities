/*
 * Copyright 2017 Samuel Yuri Zvir
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.ema.dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import br.com.ema.R;
import br.com.ema.activities.admin.CreateTextActivity;
import br.com.ema.activities.admin.MenuActivity;
import br.com.ema.activities.admin.WordsManagementActivity;

public class WordSavedDialog extends DialogFragment {
    private static final String TAG = "WordSavedDialog";
    private CreateTextActivity createTextActivity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.areYouSure)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "Save word");
                        Intent intent = new Intent(createTextActivity, WordsManagementActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        getActivity().finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "User cancelled the the registering of the new word.");
                    }
                });
        return builder.create();
    }

    public void setCreateTextActivity(CreateTextActivity createTextActivity) {
        this.createTextActivity = createTextActivity;
    }
}
