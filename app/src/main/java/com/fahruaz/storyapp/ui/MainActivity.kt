package com.fahruaz.storyapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.fahruaz.storyapp.R
import com.fahruaz.storyapp.Response.ListStoryItem
import com.fahruaz.storyapp.ViewModelFactory
import com.fahruaz.storyapp.adapter.LoadingStateAdapter
import com.fahruaz.storyapp.adapter.PagingStoriesAdapter
import com.fahruaz.storyapp.database.StoryDatabase
import com.fahruaz.storyapp.databinding.ActivityMainBinding
import com.fahruaz.storyapp.preferences.UserPreference
import com.fahruaz.storyapp.viewmodels.MainViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = obtainViewModel(this)

        val db = StoryDatabase.getDatabase(this)

        if (onLine(this)){
            mainViewModel.getUser().observe(this) { user ->
                Log.e("token", LoginActivity.token)
                if (user.token?.isEmpty() == true) {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                LoginActivity.token = user.token.toString()
                setStoriesData()
            }
        }else{
            val dao = db.storyDao()
            lifecycleScope.launch{
                dao.getAllStoryOffline().collect {
                    listStories.addAll(it)
                }
            }
        }



        binding.rvStories.layoutManager = LinearLayoutManager(this)

        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun onLine(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        setStoriesData()
            if (AddStoryActivity.upload){
                listStories.clear()
                mainViewModel.getStoryPaging().observe(this){
                    storiesAdapter.submitData(lifecycle, it)
                }
                storiesAdapter.notifyDataSetChanged()
            }
            AddStoryActivity.upload = false
    }


    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val pref = UserPreference.getInstance(dataStore)
        return ViewModelProvider(activity, ViewModelFactory(pref, this))[MainViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.logout -> {
                mainViewModel.logout()
            }
            R.id.story -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
            R.id.maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
            }
        }
        return true
    }

    private fun getStoryByName(data: ListStoryItem): ListStoryItem? {
        var selectedStory: ListStoryItem? = null

        for(story in listStories) {
            if(story.name == data.name && story.photoUrl == data.photoUrl
                && story.description == data.description)
                selectedStory = story
        }

        return selectedStory
    }

    private fun moveIntent(story: ListStoryItem){
        val data = getStoryByName(story)

        val moveWithObjectIntent = Intent(this@MainActivity, DetailStoryActivity::class.java)
        moveWithObjectIntent.putExtra(DetailStoryActivity.EXTRA_STORY, data)
        startActivity(moveWithObjectIntent)
    }

    private fun setStoriesData(){
        storiesAdapter = PagingStoriesAdapter {
            moveIntent(it)
        }

        binding.rvStories.layoutManager = LinearLayoutManager(this)
        binding.rvStories.adapter = storiesAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storiesAdapter.retry()
            }
        )
        mainViewModel.listStories.observe(this) {
            storiesAdapter.submitData(lifecycle, it)
        }
    }


    companion object{
        var listStories : ArrayList<ListStoryItem> = ArrayList()
        lateinit var mainViewModel: MainViewModel
        lateinit var storiesAdapter: PagingStoriesAdapter
    }
}