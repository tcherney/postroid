package com.tcherney.postroid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

open class UserAPIViewModel(private val collectionDao: UserAPICollectionDao, private val apiDao: UserAPIDao) : ViewModel() {
    val userAPIs: Flow<List<UserAPICollection>> = collectionDao.getAll()
    fun addAPI(userAPICollection: UserAPICollection) {
        viewModelScope.launch {
            val newID = collectionDao.insertAll(userAPICollection.internalCollection!!)
            userAPICollection.internalCollection.collectionID = newID[0]
            userAPICollection.userAPIs.forEach {
                val newAPIID = apiDao.insertAll(it)
                it.collectionID = newID[0]
                it.apiID = newAPIID[0]
            }
        }
    }

    fun updateAPI(userAPICollection: UserAPICollection) {
        viewModelScope.launch {
            userAPICollection.userAPIs.forEach {
                it.apiID = apiDao.upsertAPI(it)
                it.collectionID = userAPICollection.internalCollection!!.collectionID
            }
        }
    }

    fun deleteAPI(userAPICollection: UserAPICollection) {
        viewModelScope.launch {
            userAPICollection.userAPIs.forEach {
                apiDao.delete(it)
            }
            collectionDao.delete(userAPICollection.internalCollection!!)
        }
    }
}