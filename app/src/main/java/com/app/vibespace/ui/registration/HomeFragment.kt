package com.app.vibespace.ui.registration

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.models.profile.UserUpdateModel
import com.app.vibespace.models.registration.GetPeopleModel
import com.app.vibespace.util.showToast
import com.app.vibespace.viewModel.registration.HomeViewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class HomeFragment : Fragment() {
    var data: UserUpdateModel? = null

    //    private lateinit var binding:FragmentHomeBinding
    private val model: HomeViewModel by viewModels()
    private var peopleList = ArrayList<GetPeopleModel.Data.User>()
    private lateinit var mapView: MapView
    private lateinit var mapboxMap: MapboxMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapView = MapView(
            inflater.context,
            // Use TextureView as render surface for the MapView, for smooth transitions following holding views, e.g. in a ViewPager.
            MapInitOptions(inflater.context, textureView = true, styleUri = STYLE_URL)
        )
        return mapView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapboxMap = mapView.getMapboxMap()
        getPeopleList()

    }


    private fun getPeopleList() {
        activity?.let {
            model.getPeople().observe(it) { response ->
                when (response.status) {
                    ApiStatus.SUCCESS -> {
                        peopleList.clear()
                        peopleList.addAll(response.data?.data?.userList!!)
                        addMap()
                    }

                    ApiStatus.ERROR -> {
                        response.message?.let { it1 -> showToast(requireActivity(), it1) }
                    }

                    ApiStatus.LOADING -> {

                    }
                }
            }
        }
    }


    private fun addMap() {

        peopleList.forEach { data ->
            val pointAnnotationManager = mapView.annotations.createPointAnnotationManager()
            val point = Point.fromLngLat(data.location.lng, data.location.lat)

            lifecycleScope.launch {
                try {
                    val image = loadImageFromUrl(data.mascotIcon)

                    if (image != null) {
                        val pointAnnotationOptions: PointAnnotationOptions =
                            PointAnnotationOptions()
                                .withIconImage(image)
                                .withIconSize(0.3)
                                .withPoint(point)

                        pointAnnotationManager.create(pointAnnotationOptions)

                        pointAnnotationManager.addClickListener(OnPointAnnotationClickListener {
                            val action =
                                HomeFragmentDirections.actionHomeFragmentToOtherUserProfileFragment(
                                    data = data.userId
                                )
                            findNavController().navigate(action)
                            true
                        })
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    private suspend fun loadImageFromUrl(imageUrl: String): Bitmap? = withContext(Dispatchers.IO) {
        return@withContext try {
            val `in` = java.net.URL(imageUrl).openStream()
            BitmapFactory.decodeStream(`in`)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        private const val STYLE_URL = "mapbox://styles/zimblecode/cll5n0h4b00kn01pm7sfvbl6g"
    }
}