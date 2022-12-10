package com.example.quizappdiploma.fragments.viewmodels.helpers

// https://gist.github.com/JoseAlcerreca/5b661f1800e1e654f07cc54fe87441af
open class LiveDataEvent<out T>(private val content: T)
{
    private var hasBeenHandled = false

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}