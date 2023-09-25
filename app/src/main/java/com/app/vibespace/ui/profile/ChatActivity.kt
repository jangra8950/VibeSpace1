package com.app.vibespace.ui.profile


import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.vibespace.Enums.ApiStatus
import com.app.vibespace.R
import com.app.vibespace.adapter.ChatAdapter
import com.app.vibespace.databinding.ActivityChatBinding
import com.app.vibespace.models.profile.ChatItemModel
import com.app.vibespace.models.profile.ChatRequest
import com.app.vibespace.util.CommonFuctions.Companion.loadImage
import com.app.vibespace.util.hideKeyboard
import com.app.vibespace.util.showToast
import com.app.vibespace.viewModel.profile.ChatItemViewModel
import com.google.gson.Gson
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration
import dagger.hilt.android.AndroidEntryPoint
import info.mqtt.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.io.UnsupportedEncodingException
import java.util.Date


@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private var selfId: String = ""
    private var topic: String = "vibespace/dev/connection/chat/"
    private lateinit var client: MqttAndroidClient
    private lateinit var adap: ChatAdapter
    private var mess:String?=""

    // private lateinit var adapter: ChatItemAdapter
    private val model: ChatItemViewModel by viewModels()
    private var chatList = ArrayList<ChatItemModel.Data.Chat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=DataBindingUtil.setContentView(this,R.layout.activity_chat)
        val otherUserId = intent.getStringExtra("data").toString()
        val name = intent.getStringExtra("name").toString()
        val image = intent.getStringExtra("image").toString()
         mess = intent.getStringExtra("mess")
        Log.i("WQWQWQWQW","level1 , $mess")



        loadImage(this,image,binding.ivAvatar)

        binding.tvBlocked.text = name
        getChat(otherUserId)

//        val layoutManager=LinearLayoutManager(this)
//        layoutManager.reverseLayout=true
//        binding.recyclerview.layoutManager= layoutManager
//        adapter= ChatItemAdapter(chatList,this,otherUserId)
//        binding.recyclerview.adapter=adapter


        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout=true
        binding.recyclerview.layoutManager = layoutManager

        adap= ChatAdapter(chatList,this,otherUserId)
        binding.recyclerview.adapter=adap

        val decoration = StickyRecyclerHeadersDecoration(adap)
        binding.recyclerview.addItemDecoration(decoration)



        binding.back.setOnClickListener {
            onBackPressed()
        }

    }



    private fun getChat(str:String) {
        model.chat(str).observe(this){response->
            when(response.status){
                ApiStatus.SUCCESS -> {
                    binding.shimmerLayout.startShimmer()
                    selfId= response.data?.data?.currentUser!!.userId
                    chatList.clear()
                    chatList.addAll(response?.data.data.chatList.reversed())
                    val cId=response.data.data.currentUser.conversationId
                    val channel=topic+cId.replace(".","")

                    Log.i("TRRTTARRA",channel)
                    mqtt(channel,str,cId.replace(".",""))

                    adap.notifyDataSetChanged()
                    binding.shimmerLayout.visibility=View.GONE
                    binding.recyclerview.visibility=View.VISIBLE
                }
                ApiStatus.ERROR -> {
                    response.message?.let { it1 -> showToast(this, it1) }
                }
                ApiStatus.LOADING -> {

                }
            }

        }
    }

    fun checkValue(): Boolean {
        if (binding.tvText.text?.trim()?.isEmpty()!!) {
            showToast(applicationContext,"Enter Valid message ")
            return false
        }
        return true
    }

    private fun mqtt(channel:String,otherUserId:String,cId:String) {
        val clientId = MqttClient.generateClientId()
        client = MqttAndroidClient(
            this.applicationContext,"tcp://3.7.75.87:1883",
            clientId
        )

        try {
            val options = MqttConnectOptions()
            options.userName = "xperf"
            options.password = "xperf-d".toCharArray()
            val token = client.connect(options)
            token.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    subscribe(channel,otherUserId)

                    Log.d("Connect", "Connect Successfully")

                    Log.i("WQWQWQWQW","level2 , $mess")
                    if( !mess.isNullOrEmpty())

                        publishAndAddToChatList(
                            mess!!,
                            channel,
                            otherUserId,
                            cId
                        )

                    binding.ivSend.setOnClickListener {

                        if(checkValue()) {

                            publishAndAddToChatList(
                                binding.tvText.text?.trim().toString(),
                                channel,
                                otherUserId,
                                cId
                            )
                            hideKeyboard(it)
                        }
                    }
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {

                    Log.d("Connect", "Connection Failed")
                }
            }
        } catch (e: MqttException) {
            e.printStackTrace()
        }

    }

    private fun subscribe(channel:String,otherUserId:String){
        Log.d("Subsscribe", "Message Arrived Successfully")
        try{

            client.subscribe(channel,1)

            client.setCallback(object: MqttCallback {
                override fun connectionLost(cause: Throwable?) {

                }

                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    Log.d("Connect", "Subscribe topic: ${message.toString()}")
                    val data = Gson().fromJson(message.toString(), ChatItemModel.Data.Chat::class.java)
                    if (data.status=="") {
                        chatList.add(0, data)
                        adap.notifyItemInserted(0)
                        binding.recyclerview.smoothScrollToPosition(0)
                    }
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {

                }

            })

        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun publishAndAddToChatList(mess: String, channel: String, otherUserId: String,cId:String) {

        try {
            val sentChat=ChatRequest(
                message = mess,
                receiverId = otherUserId,
                senderId = selfId,
                sentAt = Date().time,
                eventType = "chat",
                type = "text",
                connectionsId = cId,
                chatType = "text",
                isRead = false,
                isDelivered = false,
                status = ""
            )

            val data = MqttMessage(Gson().toJson(sentChat).toByteArray())

            client.publish(channel, data, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d("Connect", "Message Sent Successfully")
                    binding.tvText.setText("")
                    binding.tvText.clearFocus()
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d("Connect", "Message Not Sent")
                }
            })
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }


}