package com.fahruaz.storyapp.viewmodels


import androidx.lifecycle.*
import com.fahruaz.storyapp.Response.RegisterResponse
import com.fahruaz.storyapp.api.ApiConfig
import com.fahruaz.storyapp.models.UserModel
import com.fahruaz.storyapp.preferences.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val pref: UserPreference) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String> = _toast

    fun registerUser(user: UserModel) {
        _isLoading.value = true
        val service = ApiConfig().getApiService().registerUser(user.name!!, user.email!!, user.password!!)

        service.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _toast.value = responseBody.message
                        setUser(user)
                    }
                }
                else
                    _toast.value = response.message()
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _toast.value = "Gagal instance Retrofit"
            }
        })
    }

    fun setUser(user: UserModel){
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

}