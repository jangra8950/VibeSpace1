package com.app.vibespace.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.app.vibespace.R
import com.app.vibespace.databinding.LayoutFollowersListBinding
import com.app.vibespace.models.setting.FollowersModel
import com.app.vibespace.util.CommonFuctions
import com.app.vibespace.util.CommonFuctions.Companion.loadImage
import java.util.ArrayList

class FollowersAdapter(private val context: Context, private val change:Follow
): RecyclerView.Adapter<FollowersAdapter.ViewHolder>()  {
    var mList=ArrayList<FollowersModel.Data.Follower>()
    var followType="follower"
    inner class ViewHolder(var binding: LayoutFollowersListBinding): RecyclerView.ViewHolder(binding.root){

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersAdapter.ViewHolder {
        val view=  LayoutFollowersListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowersAdapter.ViewHolder, position: Int) {
        if (followType=="follower") {
            holder.binding.btnRemove.visibility = View.VISIBLE
            holder.binding.cgGroup.visibility = View.GONE
        }else{
            holder.binding.btnRemove.visibility = View.GONE
            holder.binding.cgGroup.visibility = View.VISIBLE
        }
        val item=mList[position]
        holder.binding.tvName.text=item.userDetails.firstName
        loadImage(context,item.userDetails.profilePic,holder.binding.ivAvatar)

        holder.binding.btnRemove.setOnClickListener {
            showDialogLogOut(context,item.connectId,position)
        }

       holder.binding.ivMenu.setOnClickListener {
           showMenu(holder.binding.ivMenu,context,position,item.userDetails.userId)
       }
    }

    private fun showMenu(
        deletePost: ImageView,
        context: Context,
        position: Int,
        userId: String
    ) {
        val popupMenu = PopupMenu(context, deletePost)

        popupMenu.menuInflater.inflate(R.menu.menu_items, popupMenu.menu)
        popupMenu.menu.findItem(R.id.reportPost).isVisible=false

        popupMenu.setOnMenuItemClickListener {
            change.block(userId,position)
            true
        }

        try {
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menuPopupHelper = popup.get(popupMenu)
            val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
            val setForceIcons = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.java)
            setForceIcons.invoke(menuPopupHelper, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // Showing the popup menu
        popupMenu.show()
    }

    private fun showDialogLogOut(context: Context, userId: String, position: Int){
        CommonFuctions.dialog = Dialog(context)
        CommonFuctions.dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        CommonFuctions.dialog?.setContentView(R.layout.layout_logout_confirm)
        CommonFuctions.dialog?.setCancelable(false)
        CommonFuctions.dialog!!.findViewById<AppCompatTextView>(R.id.tvConfirmation).text="Are you sure to want to unfollow this User?"
        CommonFuctions.dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        CommonFuctions.dialog!!.findViewById<Button>(R.id.btnYes).setOnClickListener {
            change.unfollow(userId,position)
            CommonFuctions.dialog!!.dismiss()
        }
        CommonFuctions.dialog!!.findViewById<Button>(R.id.btnNo).setOnClickListener {
            CommonFuctions.dialog!!.dismiss()
        }
        CommonFuctions.dialog?.show()
    }

    fun updateData(list:ArrayList<FollowersModel.Data.Follower>,follow:String){
        mList.clear()
        mList.addAll(list)
        followType=follow
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
       return mList.size
    }

    interface Follow{
        fun unfollow(id:String,position: Int)
        fun block(userId: String,position: Int)
    }

}