package com.bignerdranch.android.geoquiz

import androidx.annotation.StringRes

/*
We use the data keyword for all model classes in this book.
Doing so clearly indicates that the class is meant to hold data.
Also, the compiler does extra work for data classes that makes your life easier,
 such as automatically defining useful functions like equals(), hashCode(), and a nicely formatted toString().
 */
data class Question(@StringRes val textResId :Int, val answer:Boolean)