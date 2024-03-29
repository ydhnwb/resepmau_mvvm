package com.ydhnwb.resepmau_mvvm.utilities

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean


class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val mPending = AtomicBoolean(false)

    @MainThread
    fun observer(owner : LifecycleOwner, observer: Observer<T>){
        if(hasActiveObservers()){
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        }
        super.observe(owner, Observer<T> {
            if (mPending.compareAndSet(true, false)) {
                    observer.onChanged(it)
            }
        })
    }

    @MainThread
    override fun setValue(@Nullable t: T?) {
        mPending.set(true)
        super.setValue(t)
    }

    @MainThread
    fun call() {
        setValue(null)
    }

    companion object {
        private val TAG = "SingleLiveEvent"
    }
}