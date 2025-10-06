package com.tcherney.postroid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

open class UserAPIViewModel(private val collectionDao: UserAPICollectionDao, private val apiDao: UserAPIDao) : ViewModel() {
    val userAPIs: Flow<List<UserAPICollection>> = collectionDao.getAll()
    fun addAPICollection(userAPICollection: UserAPICollection): Job {
        return viewModelScope.launch {
            collectionDao.insertParentAndChild(userAPICollection.internalCollection!!, UserAPI())
        }
    }

    fun addAPI(userAPICollection: UserAPICollection): Job {
        return viewModelScope.launch {
            apiDao.upsertAPI(UserAPI(collectionID = userAPICollection.internalCollection!!.collectionID))
        }
    }
    fun updateCollectionWithAPI(userAPICollection: UserAPICollection, userAPI: UserAPI): Job {
        return viewModelScope.launch {
            collectionDao.upsertAPI(userAPICollection.internalCollection!!)
            apiDao.upsertAPI(userAPI)
        }
    }
    fun updateCollection(userAPICollection: UserAPICollection): Job {
        return viewModelScope.launch {
            collectionDao.upsertAPI(userAPICollection.internalCollection!!)
            userAPICollection.userAPIs.forEach {
                apiDao.upsertAPI(it)
            }
        }
    }

    fun deleteAPI(userAPICollection: UserAPICollection): Job {
        return viewModelScope.launch {
            userAPICollection.userAPIs.forEach {
                apiDao.delete(it)
            }
            collectionDao.delete(userAPICollection.internalCollection!!)
        }
    }
}