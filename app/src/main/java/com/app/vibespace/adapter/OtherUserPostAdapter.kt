package com.app.vibespace.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.app.vibespace.R
import com.app.vibespace.models.profile.PostListModel
import com.app.vibespace.util.CommonFuctions

import com.app.vibespace.util.CommonFuctions.Companion.loadImage

class OtherUserPostAdapter(private val mList:ArrayList<PostListModel.Data.Post>,
                           var context: Context, private val change: Changes
): RecyclerView.Adapter<OtherUserPostAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val caption: TextView = itemView.findViewById(R.id.tvCaption)
        val name: TextView = itemView.findViewById(R.id.tvName)
        val likeCount: TextView = itemView.findViewById(R.id.tvLikeCount)
        val commentCount: TextView = itemView.findViewById(R.id.tvCommentCount)
        val pic: AppCompatImageView = itemView.findViewById(R.id.ivMap)
       // val mascot: AppCompatImageView = itemView.findViewById(R.id.ivAvatar)
        val menu: ImageView = itemView.findViewById(R.id.ivMore)
//        val typeText: AppCompatEditText =itemView.findViewById(R.id.tvTypeComment)
        val chatAct: AppCompatImageView = itemView.findViewById(R.id.ivChatAct)
        val chatNew: AppCompatImageView = itemView.findViewById(R.id.ivChatNew)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.layout_post_recycler_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=mList[position]
        holder.name.text= "${item.userDetails.firstName} ${item.userDetails.lastName}"
        holder.caption.text= item.caption
        holder.likeCount.text= item.likeCount.toString()
        holder.commentCount.text=item.commentCount.toString()
       // holder.typeText.visibility=View.GONE
        Log.i("CXCXCCX",item.userDetails.profilePic+",   ,"+"$position")

       // loadImage(context,item.userDetails.mascotIcon,holder.mascot)
        loadImage(context,item.userDetails.profilePic,holder.pic)

        holder.menu.setOnClickListener {
            showMenu(holder.menu,context,position,item.userId)
        }

        holder.chatAct.setOnClickListener {
            change.chat(item.userId,item.userDetails.profilePic,item.userDetails.firstName+" "+item.userDetails.lastName, item.caption)
        }

        holder.chatNew.setOnClickListener {
            showDialogLogOut(context,item.caption,item.postVisibility)
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

    private fun showDialogLogOut(context: Context, caption: String, postVisibility: String){
        CommonFuctions.dialog = Dialog(context)
        CommonFuctions.dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        CommonFuctions.dialog?.setContentView(R.layout.layout_logout_confirm)
        CommonFuctions.dialog?.setCancelable(false)
        CommonFuctions.dialog!!.findViewById<AppCompatTextView>(R.id.tvConfirmation).text="Are you sure to want to add this vibe to your Profile?"
        CommonFuctions.dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        CommonFuctions.dialog!!.findViewById<Button>(R.id.btnYes).setOnClickListener {
            change.vibe(caption,postVisibility)
            CommonFuctions.dialog!!.dismiss()
        }
        CommonFuctions.dialog!!.findViewById<Button>(R.id.btnNo).setOnClickListener {
            CommonFuctions.dialog!!.dismiss()
        }
        CommonFuctions.dialog?.show()
    }

    interface Changes{
        fun block(userId: String,position: Int)
        fun chat(userId:String,image:String,name:String, mess:String)
        fun vibe(caption:String, postVisibility:String)
    }

}