package com.emilioschepis.qrsync.ui.codelist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import arrow.core.Either
import com.emilioschepis.qrsync.model.QSCode
import com.emilioschepis.qrsync.model.QSError
import com.emilioschepis.qrsync.repository.IFirestoreRepository

class CodeListViewModel(private val firestore: IFirestoreRepository) : ViewModel() {

    private val mutableLoading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = mutableLoading

    val collection: LiveData<Either<QSError, List<QSCode>>>
        get() {
            mutableLoading.value = true
            return Transformations.map(firestore.retrieveCollection()) {
                mutableLoading.value = false
                it
            }
        }
}