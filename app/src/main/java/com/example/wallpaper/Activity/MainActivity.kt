package com.example.wallpaper.Activity


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wallpaper.Adapter.Wallpaper_Rcv_Adapter
import com.example.wallpaper.Client.APIClient
import com.example.wallpaper.Interface.ApiInterface
import com.example.wallpaper.PhotosItem
import com.example.wallpaper.WallpaperModel
import com.example.wallpaper.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: Wallpaper_Rcv_Adapter
    var List = ArrayList<PhotosItem>()
    var page = 1
    private  val TAG = "MainActivity"
    lateinit var search:String


    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSearch.setOnClickListener {

            search = binding.editSearch.text.toString()



            callApi(search,page)


        }

        binding.setOnScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // on scroll change we are checking when users scroll as bottom.
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                // in this method we are incrementing page number,
                // making progress bar visible and calling get data method.
                page++
                binding.progrebar.setVisibility(View.VISIBLE)
                callApi(search ,page)


            }
        })

    }

    private fun callApi(search:String, page1: Int) {


        var apiInterface = APIClient.getapiClient().create(ApiInterface::class.java)
        apiInterface.getWallpaper(search ,page1.toString(), APIClient.KEY)
            .enqueue(object : Callback<WallpaperModel> {
                override fun onResponse(
                    call: Call<WallpaperModel>,
                    response: Response<WallpaperModel>
                ) {


                    //             List = response.body()?.photos as ArrayList<PhotosItem>
                    List.addAll(response.body()?.photos as ArrayList<PhotosItem>)
                    adapter = Wallpaper_Rcv_Adapter(List)
                    binding.recycleView.layoutManager = GridLayoutManager(this@MainActivity, 3)
                    binding.recycleView.adapter = adapter
                    binding.progrebar.visibility = View .GONE

                    Log.e(TAG, "onResponse: =============="+response.body() )

                }

                override fun onFailure(call: Call<WallpaperModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: ======== "+t.message )
                }


            })

    }


}

