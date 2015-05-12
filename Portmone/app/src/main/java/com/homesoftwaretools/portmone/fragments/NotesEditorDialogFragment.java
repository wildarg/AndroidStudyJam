package com.homesoftwaretools.portmone.fragments;/*
 * Created by Wild on 12.05.2015.
 */

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.homesoftwaretools.portmone.R;
import com.homesoftwaretools.portmone.tasks.UpdateNotesTask;

public class NotesEditorDialogFragment extends DialogFragment {

    public static final String EXTRA_NOTES = "EXTRA_NOTES";
    private EditText notesEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notes_editor, container, false);
        view.findViewById(R.id.doneButton).setOnClickListener(new DoneButtonClickListener());
        notesEditText = (EditText) view.findViewById(R.id.notesEditText);
        notesEditText.setText(getArguments().getString(EXTRA_NOTES));
        return view;
    }

    private class DoneButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new UpdateNotesTask(getActivity(), notesEditText.getText().toString()).execute();
            dismiss();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
