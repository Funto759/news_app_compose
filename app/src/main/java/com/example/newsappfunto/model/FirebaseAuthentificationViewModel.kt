package com.example.newsappfunto.model

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsappfunto.dao.NewsArticlesDatabase
import com.example.newsappfunto.data.Articles
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import androidx.core.content.edit
import com.example.newsappfunto.data.User

@HiltViewModel
class FirebaseAuthentificationViewModel@Inject constructor(val db: NewsArticlesDatabase,private val prefs: SharedPreferences): ViewModel() {
    val auth : FirebaseAuth = FirebaseAuth.getInstance()
    val personCollectionRef = Firebase.firestore.collection("Articles")
    val userCollectionRef = Firebase.firestore.collection("User")

    var _articlesFlow = MutableStateFlow<FirebaseViewState>(FirebaseViewState.IDLE)
    val articlesFlow = _articlesFlow.asStateFlow()
    var _signInStatus = MutableStateFlow<FirebaseViewState>(FirebaseViewState.UnAuthenticated)
    val signUpStatus = _signInStatus.asStateFlow()
    var _userStatus = MutableStateFlow<FirebaseViewState>(FirebaseViewState.IDLE)
    val userStatus = _userStatus.asStateFlow()

    init {
        checkAuthStatus()
        auth.addAuthStateListener { checkAuthStatus() }
    }

    init {
        checkAuthStatus()
        auth.addAuthStateListener { checkAuthStatus() }
//        retrieveArticlesListener()
    }
    fun retrieveArticlesCategoryListener(category: String?) {
        personCollectionRef
            .whereEqualTo("category",category)
            .orderBy("artId")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                _articlesFlow.value = FirebaseViewState.Error(it.message.toString())
                println(it.message.toString())
                return@addSnapshotListener
            }
            querySnapshot?.let {
                val articleList = it.documents.mapNotNull { doc ->
                    doc.toObject(Articles::class.java)
                }
                _articlesFlow.value = FirebaseViewState.Success(articleList)
            }
        }
    }

    fun retrieveArticlesSingleListener(url: String?) {
        personCollectionRef
            .whereEqualTo("url",url)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                _articlesFlow.value = FirebaseViewState.Error(it.message.toString())
                println(it.message.toString())
                return@addSnapshotListener
            }
            querySnapshot?.let {
                val articleList = it.documents.mapNotNull { doc ->
                    doc.toObject(Articles::class.java)
                }
                _articlesFlow.value = FirebaseViewState.Success(articleList)
            }
        }
    }

    fun retrieveArticlesListener() {
        personCollectionRef
            .orderBy("artId")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                _articlesFlow.value = FirebaseViewState.Error(it.message.toString())
                return@addSnapshotListener
            }
            querySnapshot?.let {
                val articleList = it.documents.mapNotNull { doc ->
                    doc.toObject(Articles::class.java)
                }
                _articlesFlow.value = FirebaseViewState.Success(articleList)
            }
        }
    }
    fun retrieveArticlesSearchListener(content: String?) {
        personCollectionRef
            .whereEqualTo("content",content)
            .orderBy("artId")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                _articlesFlow.value = FirebaseViewState.Error(it.message.toString())
                return@addSnapshotListener
            }
            querySnapshot?.let {
                val articleList = it.documents.mapNotNull { doc ->
                    doc.toObject(Articles::class.java)
                }
                _articlesFlow.value = FirebaseViewState.Success(articleList)
            }
        }
    }

    fun retrieveArticles(category: String?){
        viewModelScope.launch(Dispatchers.IO) {
            _articlesFlow.value = FirebaseViewState.Loading(true)
            try {
                val querySnapshot = personCollectionRef

                    .get()
                    .await()
                val articleList = querySnapshot.documents.mapNotNull { doc ->
                    doc.toObject(Articles::class.java)
                }
                _articlesFlow.value = FirebaseViewState.Success(articleList)

            } catch (e: Exception){
                _articlesFlow.value = FirebaseViewState.Error(e.message.toString())
            }
        }
    }

    fun saveFire(articles: Articles){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentId = prefs.getInt("article_counter1", 0)

                val nextId = currentId + 1

                // 4️⃣ Attach to your article data class
                val articleWithId = articles.copy(artId = nextId)
                personCollectionRef.add(articleWithId).await()
                prefs.edit() {
                    putInt("article_counter1", nextId)
                }

            } catch (e: Exception) {
                _articlesFlow.value = FirebaseViewState.Error(e.message.toString())
            }
        }
    }

    fun checkAuthStatus(){
        viewModelScope.launch {
            if (auth.currentUser == null){
                _signInStatus.value = FirebaseViewState.UnAuthenticated
            }else{
                _signInStatus.value = FirebaseViewState.Authenticated
            }
        }
    }

    fun login(email:String,password:String){
        if (email.isEmpty() || password.isEmpty()){
            _signInStatus.value = FirebaseViewState.Error("Email or password is empty")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            _signInStatus.value = FirebaseViewState.Loading(true)
            try {
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            _signInStatus.value = FirebaseViewState.Authenticated
                            println("login successful")
                        }else{
                            _signInStatus.value = FirebaseViewState.Error(task.exception?.message ?: "Something went wrong")

                            println("login failed")
                        }
                    }
            } catch (e: SecurityException){
                _signInStatus.value = FirebaseViewState.Error(e.message.toString())

            }
        }
    }


    fun signUp(user: User){
        if (user.email.isEmpty() || user.password.isEmpty() || user.password.isEmpty() || user.firstname.isEmpty() || user.lastname.isEmpty() || user.phoneNumber.isEmpty()){
            _signInStatus.value = FirebaseViewState.Error("Email or password is empty")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            _signInStatus.value = FirebaseViewState.Loading(true)
            try {
                auth.createUserWithEmailAndPassword(user.email,user.password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            _signInStatus.value = FirebaseViewState.Authenticated
                            createUser(user)
                            println("sign up successful")
                        }else{
                            _signInStatus.value = FirebaseViewState.Error(task.exception?.message ?: "Something went wrong")
                            println("sign up failed")
                        }
                    }
            } catch (e: SecurityException){
                _signInStatus.value = FirebaseViewState.Error(e.message.toString())

            }
        }
    }

    fun signOut(){
        auth.signOut()
        _signInStatus.value = FirebaseViewState.UnAuthenticated
    }

    fun createUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userCollectionRef.add(user).await()
            }catch (e: Exception){
                println(e.message)
            }
        }
    }

    fun retrieveUser() {
        userCollectionRef
            .whereEqualTo("email",auth.currentUser?.email)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                firebaseFirestoreException?.let {
                    _userStatus.value = FirebaseViewState.Error(it.message.toString())
                    return@addSnapshotListener
                }
                querySnapshot?.let {
                    val user = it.documents.firstOrNull()?.toObject(User::class.java)
                    if (user != null) {
                        _userStatus.value = FirebaseViewState.User(user)
                    }
                }
            }
    }

    sealed class FirebaseViewState(){
        data class Success(val articles:List<Articles>):FirebaseViewState()
        data class User(val user:com.example.newsappfunto.data.User): FirebaseViewState()
        object Authenticated:FirebaseViewState()
        object UnAuthenticated:FirebaseViewState()
        data class Loading(val loading:Boolean):FirebaseViewState()
        data class Error(val message:String):FirebaseViewState()
        object IDLE:FirebaseViewState()

    }
}