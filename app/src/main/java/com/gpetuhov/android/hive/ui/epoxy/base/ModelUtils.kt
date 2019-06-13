package com.gpetuhov.android.hive.ui.epoxy.base

import android.widget.CheckBox

fun CheckBox.bind(initialValue: Boolean, onClick: (Boolean) -> Unit) {
    isChecked = initialValue
    setOnClickListener { view -> onClick((view as CheckBox).isChecked) }
}