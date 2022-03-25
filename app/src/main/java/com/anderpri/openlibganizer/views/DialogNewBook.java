package com.anderpri.openlibganizer.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.anderpri.openlibganizer.R;

import java.util.Objects;

// CUSTOM DIALOG
// https://gist.github.com/codinginflow/11e5acb69a91db8f2be0f8e495505d12

public class DialogNewBook extends AppCompatDialogFragment {
    private EditText editTextUsername;
    private DialogNewBookListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view)
                .setTitle(R.string.add_dialog_title)
                //.setMessage("Mensaje")
                .setNegativeButton(R.string.add_dialog_cancel, (dialogInterface, i) -> {})
                .setPositiveButton(R.string.add_dialog_ok, (dialogInterface, i) -> {
                    String username = editTextUsername.getText().toString();
                    listener.addBook(username);
                });

        editTextUsername = view.findViewById(R.id.mISBN);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogNewBookListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context +
                    "must implement ExampleDialogListener");
        }
    }

    public interface DialogNewBookListener {
        void addBook(String mISBN);
    }
}