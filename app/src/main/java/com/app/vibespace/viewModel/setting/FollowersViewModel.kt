package com.app.vibespace.viewModel.setting

import androidx.lifecycle.ViewModel
import com.app.vibespace.service.MyRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FollowersViewModel @Inject constructor(
    private val repo: MyRepo,
    private val resources: android.content.res.Resources
):ViewModel(){

}