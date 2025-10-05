package com.tcherney.postroid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

open class UserAPIViewModel(private val collectionDao: UserAPICollectionDao, private val apiDao: UserAPIDao) : ViewModel() {
    val userAPIs: Flow<List<UserAPICollection>> = collectionDao.getAll()
    fun addAPICollection(userAPICollection: UserAPICollection) {
        viewModelScope.launch {
            collectionDao.insertParentAndChild(userAPICollection.internalCollection!!, userAPICollection.userAPIs[0])
        }
    }

    fun addAPI(userAPICollection: UserAPICollection) {
        viewModelScope.launch {
            apiDao.upsertAPI(UserAPI(collectionID = userAPICollection.internalCollection!!.collectionID))
        }
    }
    fun updateCollectionWithAPI(userAPICollection: UserAPICollection, userAPI: UserAPI) {
        viewModelScope.launch {
            collectionDao.upsertAPI(userAPICollection.internalCollection!!)
            apiDao.upsertAPI(userAPI)
        }
    }
    fun updateCollection(userAPICollection: UserAPICollection) {
        viewModelScope.launch {
            collectionDao.upsertAPI(userAPICollection.internalCollection!!)
            userAPICollection.userAPIs.forEach {
                apiDao.upsertAPI(it)
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