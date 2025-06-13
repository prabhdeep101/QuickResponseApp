package com.example.quickresponseapp.contacts

// Extension property to force a 'when' expression to be exhaustive
val <T> T.exhaustive: T
    get() = this