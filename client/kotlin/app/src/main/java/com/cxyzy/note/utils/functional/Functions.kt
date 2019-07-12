package com.cxyzy.note.utils.functional

typealias Supplier<T> = () -> T

interface Consumer<T> {

    fun accept(t: T)
}