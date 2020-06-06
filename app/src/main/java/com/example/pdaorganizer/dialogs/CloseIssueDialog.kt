package com.example.pdaorganizer.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment

class CloseIssueDialog : AppCompatDialogFragment() {
    lateinit var listener :OrganizerDialogListener

    fun setMyListener(l: OrganizerDialogListener){
        listener = l
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("lose issue?")
            .setPositiveButton("ok", DialogInterface.OnClickListener { dialog, which ->
                listener.onOk()
            })
            .setNegativeButton("back", DialogInterface.OnClickListener { dialog, which ->

            })
        return builder.create()
    }
}