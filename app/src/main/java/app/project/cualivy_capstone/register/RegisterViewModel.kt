package app.project.cualivy_capstone.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.project.cualivy_capstone.api.ApiConfig
import app.project.cualivy_capstone.response.Register
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel:ViewModel() {
    private val _error = MutableLiveData(true)
    val error: LiveData<Boolean> = _error

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private val TAG = RegisterViewModel::class.java.simpleName
    }

    fun register( guid: String, fullname: String, email: String, password: String, token: String, createdat:String, updatedat : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().register(guid, fullname, email, password, token, createdat, updatedat)
        client.enqueue(object : Callback<Register> {
            override fun onResponse(
                call: Call<Register>,
                response: Response<Register>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _error.value = false
                    _message.value = responseBody.message
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = JSONObject(errorBody.toString()).getString("message")
                    _error.value = true
                    _message.value = errorMessage
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Register>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}