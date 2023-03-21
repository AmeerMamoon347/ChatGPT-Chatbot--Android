package com.example.chatgpt

import android.Manifest
import android.content.ClipData.Item
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.messages.MessagesList
import com.stfalcon.chatkit.messages.MessagesListAdapter
import org.json.JSONObject
import java.security.Permission
import java.security.Permissions
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    lateinit var enterTv: EditText
    lateinit var sendIcon: ImageView
    lateinit var voiceIcon:ImageView
    lateinit var messageList: MessagesList
    lateinit var user: User
    lateinit var chatGpt: User
    lateinit var messagesListAdapter: MessagesListAdapter<Messages>
    lateinit var ttSpeach: TextToSpeech
    lateinit var speecRecognizer: SpeechRecognizer
    var isSpeech = false;

    //     Api key
  

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        enterTv = findViewById(R.id.enterTv)
        sendIcon = findViewById(R.id.sendIconBtn);
        messageList = findViewById(R.id.messagesList);
        voiceIcon = findViewById(R.id.voiceIconBtn);

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#448EE2")))


        val imageLoader: ImageLoader = object : ImageLoader {
            override fun loadImage(imageView: ImageView?, url: String?, payload: Any?) {
                Picasso.get().load(url).into(imageView)
            }
        }

        messagesListAdapter = MessagesListAdapter<Messages>("1", imageLoader);
        messageList.setAdapter(messagesListAdapter)

        user = User("1", "Ameer Mamoon", "");
        chatGpt = User("2", "ChatGPT", "");

        sendIcon.setOnClickListener {

            var Input = enterTv.text.toString()

            val messages = Messages("m1", Input, user, Calendar.getInstance().time, "")
            messagesListAdapter.addToStart(messages, true)

            var input = Input.toLowerCase()

            if (input.startsWith("generate an image") || input.startsWith("generate image")
                || input.startsWith("create an image") || input.startsWith("create image")
            ) {
                Toast.makeText(
                    this,
                    "Please wait! it might take time due to load on server!",
                    Toast.LENGTH_SHORT
                ).show()
                generateImages(Input)
            } else {
                performAction(Input)
            }

            enterTv.text.clear()
        }


        //text to speech
        ttSpeach = TextToSpeech(this, TextToSpeech.OnInitListener {
            if (it != TextToSpeech.ERROR) {
                ttSpeach.setLanguage(Locale.UK)
            }
        })



        speecRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        var recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault())
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)


        voiceIcon.setOnClickListener {

            voiceRecordPermission()
            speecRecognizer.startListening(recognizerIntent)
        }

        speecRecognizer.setRecognitionListener(object: RecognitionListener{
            override fun onReadyForSpeech(p0: Bundle?) {
                    voiceIcon.setBackgroundColor(resources.getColor(android.R.color.holo_red_light))
            }

            override fun onBeginningOfSpeech() {

            }

            override fun onRmsChanged(p0: Float) {

            }

            override fun onBufferReceived(p0: ByteArray?) {

            }

            override fun onEndOfSpeech() {
                voiceIcon.setBackgroundColor(resources.getColor(R.color.skyBlue))
            }

            override fun onError(p0: Int) {

            }

            override fun onResults(result: Bundle?) {
               var arrayResult = result?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

                var Input = arrayResult?.get(0)

                val messages = Messages("m1", Input!!, user, Calendar.getInstance().time, "")
                messagesListAdapter.addToStart(messages, true)

                var input = Input.toLowerCase()

                if (input.startsWith("generate an image") || input.startsWith("generate image")
                    || input.startsWith("create an image") || input.startsWith("create image")
                ) {
                    Toast.makeText(applicationContext, "Please wait! it might take time due to load on server!", Toast.LENGTH_SHORT).
                    show()
                    generateImages(Input)
                } else {
                    performAction(Input)
                }
            }

            override fun onPartialResults(p0: Bundle?) {

            }

            override fun onEvent(p0: Int, p1: Bundle?) {

            }

        })


    }

    private fun voiceRecordPermission() {
       if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
       {
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
               requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO),101)
           }
       }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true;
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.voice) {
            if (isSpeech) {
                item.setIcon(R.drawable.voice_disable_icon)
                isSpeech = false;
                ttSpeach.stop()
            } else {
                item.setIcon(R.drawable.voice_able_icon)
                isSpeech = true;
            }
        }
        return true
    }



    fun performAction(input:String)
    {
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.openai.com/v1/completions"

// Request a string response from the provided URL.

         var jsonObj = JSONObject()
         jsonObj.put("prompt",input)
         jsonObj.put("model","text-davinci-003")

        // 0-2-> lower the value, more accurate result, higher the value, it would be more creative and less accurate
         jsonObj.put("temperature",0)
         jsonObj.put("max_tokens",2000) // max text it will generate

        var jsonRequest = object : JsonObjectRequest(Request.Method.POST,url,jsonObj,
            Response.Listener<JSONObject> { response ->

                var answer = response.getJSONArray("choices").getJSONObject(0).getString("text")

                val message = Messages("m2",answer.trim(),chatGpt,Calendar.getInstance().time,"")
                messagesListAdapter.addToStart(message,true)
                messageList.setAdapter(messagesListAdapter)

                //for speech
                if(isSpeech)
                {
                    ttSpeach.speak(answer,TextToSpeech.QUEUE_FLUSH,null,null)
                }

            },
            Response.ErrorListener { Toast.makeText(this,"That didn't work!",Toast.LENGTH_SHORT) })

        {
            override fun getHeaders(): MutableMap<String, String> {
                var map = HashMap<String,String>()
                map.put("Content-Type","application/json")
                map.put("Authorization", "Bearer "+apiKey)

                return map
            }
        }

        jsonRequest.setRetryPolicy(object :RetryPolicy{
            override fun getCurrentTimeout(): Int {
                // 60 * (1000 miliSecond) -> 60 * 1 sec -> 60 sec(It will wait till 60 sec for response then time out)
                return 60000;
            }

            override fun getCurrentRetryCount(): Int {
                    return 15; // It will try 15 times for generating results
            }

            override fun retry(error: VolleyError?) {
                TODO("Not yet implemented")
            }

        })



// Add the request to the RequestQueue.
        queue.add(jsonRequest)
    }



    fun generateImages(input:String)
    {
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.openai.com/v1/images/generations"

// Request a string response from the provided URL.

        var jsonObj = JSONObject()
        jsonObj.put("prompt",input)
        jsonObj.put("n",2)
        jsonObj.put("size","1024x1024")


        var jsonRequest = object : JsonObjectRequest(Request.Method.POST,url,jsonObj,
            Response.Listener<JSONObject> { response ->

                val jsonArr = response.getJSONArray("data")

                 for(i in 0..jsonArr.length()-1)
                 {
                     var ansImageUrl = jsonArr.getJSONObject(i).getString("url")

                     val message = Messages("m2","",chatGpt,Calendar.getInstance().time,ansImageUrl)
                     messagesListAdapter.addToStart(message,true)
                     messageList.setAdapter(messagesListAdapter)

                     Log.d("image",ansImageUrl)
                 }



            },
            Response.ErrorListener {  Toast.makeText(this,"That didn't work!",Toast.LENGTH_SHORT) })

        {
            override fun getHeaders(): MutableMap<String, String> {
                var map = HashMap<String,String>()
                map.put("Content-Type","application/json")
                map.put("Authorization", "Bearer "+apiKey)

                return map
            }
        }

        jsonRequest.setRetryPolicy(object :RetryPolicy{
            override fun getCurrentTimeout(): Int {
                // 60 * (1000 miliSecond) -> 60 * 1 sec -> 60 sec(It will wait till 60 sec for response then time out)
                return 60000;
            }

            override fun getCurrentRetryCount(): Int {
                return 15; // It will try 15 times for generating results
            }

            override fun retry(error: VolleyError?) {
                TODO("Not yet implemented")
            }

        })



// Add the request to the RequestQueue.
        queue.add(jsonRequest)
    }


}
