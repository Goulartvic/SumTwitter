package com.example.sumtwitter.utils

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import io.reactivex.MaybeEmitter
import io.reactivex.annotations.NonNull

class RxHandler<T> private constructor(private val emitter: MaybeEmitter<in T>) :
    OnSuccessListener<T>, OnFailureListener, OnCompleteListener<T> {

    override fun onSuccess(res: T?) {
        if (res != null || res is Void) {
            Log.d("Success", res.toString())
            emitter.onSuccess(res)
        } else {
            Log.e("Error", "Observables can't emit null values")
            emitter.onError(NoSuchElementException("Observables can't emit null values"))
        }
    }

    override fun onComplete(@NonNull task: Task<T>) {
        Log.d("Complete", task.toString())
        emitter.onComplete()
    }

    override fun onFailure(@NonNull e: Exception) {
        Log.e("Error", e.localizedMessage, e)

        if (!emitter.isDisposed)
            emitter.onError(e)
    }

    companion object {

        fun <T> assignOnTask(emitter: MaybeEmitter<in T>, task: Task<T>) {
            val handler = RxHandler(emitter)
            task.addOnSuccessListener(handler)
            task.addOnFailureListener(handler)
            try {
                task.addOnCompleteListener(handler)
            } catch (t: Throwable) {
                // ignore
            }

        }
    }
}