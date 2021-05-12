package com.example.xogame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import com.example.xogame.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {
 lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        callBack()
    }
    private fun callBack() {
        val intent = Intent(this,Game::class.java)
        binding.run{
            radio.setOnCheckedChangeListener { radioGroup, i ->
                continueBtn.isEnabled=true
                if(i==xBtn.id) {
                    oLetterImage.alpha = 0.5f
                    xLetterImage.alpha = 1.0f
                }
                else {
                    xLetterImage.alpha=0.5f
                    oLetterImage.alpha=1.0f
                }
            }
            withAi.setOnClickListener {
                Setting.Mode=Mode.Ai
                setVisible()
            }
            withFriend.setOnClickListener {
                Setting.Mode=Mode.Friend
                setVisible()
            }
            continueBtn.setOnClickListener {
                var idBtnChecked = radio.checkedRadioButtonId
                Setting.playerChar = if (idBtnChecked == xBtn.id) XO.X else XO.O
                startActivity(intent)
                Log.i("MM", "player ${Setting.playerChar}")
                Log.i("MM", "mode ${Setting.Mode}")
            }
        }

    }
    private fun setVisible(){
        binding.apply {
            xBtn.visibility=View.VISIBLE
            oBtn.visibility=View.VISIBLE
            withAi.visibility=View.INVISIBLE
            withFriend.visibility=View.INVISIBLE
            textMode.visibility=View.INVISIBLE
            textPlayer.visibility=View.VISIBLE
            radio.visibility=View.VISIBLE
            continueBtn.visibility=View.VISIBLE
            ConstraintSet().apply {
                clone(root)
                connect(xLetterImage.id,ConstraintSet.TOP,textPlayer.id,ConstraintSet.BOTTOM,40)
                applyTo(root)
            }
        }
    }
}