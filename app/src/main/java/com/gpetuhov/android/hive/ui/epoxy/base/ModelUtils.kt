package com.gpetuhov.android.hive.ui.epoxy.base

import android.widget.CheckBox
import android.widget.RadioButton

fun CheckBox.bind(initialValue: Boolean, onClick: (Boolean) -> Unit) {
    isChecked = initialValue
    setOnClickListener { view -> onClick((view as CheckBox).isChecked) }
}

fun RadioButton.bind(initialValue: Boolean, onClick: () -> Unit) {
    isChecked = initialValue
    setOnClickListener { onClick() }
}