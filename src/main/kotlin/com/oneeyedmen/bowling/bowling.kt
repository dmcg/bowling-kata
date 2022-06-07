package com.oneeyedmen.bowling


fun <E> Iterable<E>.replacing(old: E, new: E) =
    map {
        if (it == old) new else it
    }
