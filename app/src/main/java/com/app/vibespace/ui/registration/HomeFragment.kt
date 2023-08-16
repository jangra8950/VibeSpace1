package com.app.vibespace.ui.registration

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.R
import com.app.vibespace.databinding.FragmentHomeBinding
import com.app.vibespace.models.profile.UserUpdateModel
import com.app.vibespace.models.registration.GetPeopleModel
import com.app.vibespace.util.showToast
import com.app.vibespace.viewModel.registration.HomeViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.style.atmosphere.generated.atmosphere
import com.mapbox.maps.extension.style.expressions.dsl.generated.color
import com.mapbox.maps.extension.style.expressions.dsl.generated.literal
import com.mapbox.maps.extension.style.layers.generated.skyLayer
import com.mapbox.maps.extension.style.layers.properties.generated.ProjectionName
import com.mapbox.maps.extension.style.layers.properties.generated.SkyType
import com.mapbox.maps.extension.style.projection.generated.projection
import com.mapbox.maps.extension.style.style
import com.mapbox.maps.extension.style.terrain.generated.terrain
import com.mapbox.maps.plugin.Plugin
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.AnnotationSourceOptions
import com.mapbox.maps.plugin.annotation.ClusterOptions
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.util.concurrent.Executors


@AndroidEntryPoint
class HomeFragment : Fragment() {
    var data: UserUpdateModel?=null
    private lateinit var binding:FragmentHomeBinding
    private val model:HomeViewModel by viewModels()
    private var peopleList=ArrayList<GetPeopleModel.Data.User>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
          if(!::binding.isInitialized){
              binding=DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
          }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel=model
        binding.fragment=this
        binding.lifecycleOwner=this
        getPeopleList()
    }


    private fun getPeopleList() {
       activity?.let {
           model.getPeople().observe(it){response->
               when(response.status){
                   ApiStatus.SUCCESS ->{
                       peopleList.clear()
                       peopleList.addAll(response.data?.data?.userList!!)

                       setInsetMapStyle()
                   }
                   ApiStatus.ERROR ->{
                       response.message?.let { it1 -> showToast(requireActivity(), it1) }
                   }
                   ApiStatus.LOADING -> {

                   }
               }
           }
       }
    }


    private fun addAnnotationToMap() {
        bitmapFromDrawableRes(
            requireActivity(),
            R.drawable.ic_avatar
        )?.let {
            val annotationApi = binding.mapView.annotations
            val pointAnnotationManager = annotationApi.createPointAnnotationManager()

            peopleList.forEach {data->
                val executor = Executors.newSingleThreadExecutor()
                var image: Bitmap? = null
                executor.execute {

                    val `in` = java.net.URL(data.mascotIcon).openStream()
                    image = BitmapFactory.decodeStream(`in`)

                    val point=Point.fromLngLat(data.location.lng, data.location.lat)
                    //  val iconImage = loadImageFromUrl(data.mascotIcon)
                    if(image!=null){
                        val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                            .withIconImage(image!!)
                            .withIconSize(0.3)
                            .withPoint(point)

                        pointAnnotationManager.create(pointAnnotationOptions)
                    }
                }

            }
//            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
//                .withPoint(Point.fromLngLat(75.76, 30.74))
//                .withIconImage(it)
        //           pointAnnotationManager.create(pointAnnotationOptions)
        }
    }
    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
// copying drawable object to not manipulate on the same reference
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }


    private fun setInsetMapStyle() {
        binding.mapView.getMapboxMap().loadStyleUri(
                styleUri = STYLE_URL
        ) {

           // addAnnotationToMap()
            addMap()
        }
    }


    private fun addMap() {
        val annotationApi = binding.mapView.annotations

        val annotationConfig = AnnotationConfig(
            annotationSourceOptions = AnnotationSourceOptions(
                clusterOptions = ClusterOptions(
                    textColorExpression = color(Color.YELLOW),
                    textSize = 20.0,
                    circleRadiusExpression = literal(25.0),
                    colorLevels = listOf(
                        Pair(100, Color.RED),
                        Pair(50, Color.BLUE),
                        Pair(0, Color.BLACK)
                    )
                )
            )
        )
        val pointAnnotationManager = annotationApi.createPointAnnotationManager(annotationConfig)

        val coroutineScope = CoroutineScope(Dispatchers.Main) // Specify the dispatcher you need

        peopleList.forEach { data ->
            val point = Point.fromLngLat(data.location.lng, data.location.lat)

            coroutineScope.launch {
                try {
                    val image = loadImageFromUrl(data.mascotIcon)

                    if (image != null) {
                        val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                            .withIconImage(image)
                            .withIconSize(0.3)
                            .withPoint(point)

                        pointAnnotationManager.create(pointAnnotationOptions)
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