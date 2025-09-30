package com.tcherney.postroid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

open class UserAPIViewModel(private val apiDao: UserAPICollectionDao) : ViewModel() {
    val userAPIs: Flow<List<UserAPICollection>> = apiDao.getAll()
    fun addAPI(userAPICollection: UserAPICollection) {
        viewModelScope.launch {
            apiDao.insertAll(userAPICollection.internalCollection!!)
        }
    }

    fun deleteAPI(userAPICollection: UserAPICollection) {
        viewModelScope.launch {
            apiDao.delete(userAPICollection.internalCollection!!)
        }
    }
}