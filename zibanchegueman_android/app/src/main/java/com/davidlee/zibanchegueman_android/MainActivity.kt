package com.davidlee.zibanchegueman_android

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.davidlee.zibanchegueman_android.func.ChangeStatusBarColorFunc
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance() // Firebase 계정 인스턴스 가져오기


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ChangeStatusBarColorFunc.updateStatusBarColor(this, R.color.colorWhite)
        setContentView(R.layout.activity_main)


        main_progressBar.visibility = View.GONE

        initButtons()


    }


    fun initButtons(){

        login_button_login.setOnClickListener {

            //로그인하는 함수를 실행시킨다.

            main_progressBar.visibility = View.VISIBLE
            performLogin()

        }

        login_resetpassword_button.setOnClickListener{
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
            //비밀번호 찾기 버튼을 눌렀을 때 비밀번호 찾는 페이지로 넘어간다.

            login_register_button.setOnClickListener {

                //RegisterActivity class로 들어간다. 화면 이동

                val intent = Intent(this, RegisterActivity::class.java)

                startActivity(intent)
            }


        }



    }

    fun performLogin(){
        val email = email_edittext_login.text.toString()
        val password = password_edittext_login.text.toString()

        //이메일과 비밀번호가 비었다면 입력해달라는 메세지
        if(email.isEmpty() || password.isEmpty()){

            //progress bar 숨기기
            main_progressBar.visibility = View.GONE

            Toast.makeText(this, "이메일과 비밀번호를 입력해주세요! ", Toast.LENGTH_SHORT).show()
            return
        }

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                else {
                    val intent = Intent(this, HubActivity::class.java)
                    startActivity(intent)
                    finish()


                    //progress bar 숨기기
                    main_progressBar.visibility = View.GONE
                    /*
                    // 이메일이 인증되어있는지 확인하기
                    if (mAuth.currentUser!!.isEmailVerified) {
                        val uid = mAuth.currentUser!!.uid

                        ///////////////////////////////////////////////////////////////////
                        val intent = Intent(this, HubActivity::class.java)
                        startActivity(intent)
                        finish()


                        //progress bar 숨기기
                        main_progressBar.visibility = View.GONE
                    }
                    else{
                        //인증이 안되어있다면 다시한번 메일을 보낸다.
                        mAuth.currentUser?.sendEmailVerification()
                            ?.addOnCompleteListener {

                                //progress bar 숨기기
                                main_progressBar.visibility = View.GONE


                                //인증이 안되어있다면 다시한번 메일을 보낸다.
                                ShowDialog("이메일 인증 필요", "이메일을 인증해주세요.\n" +
                                        "이메일로 인증 메일이 발송되었습니다.", "확인")

                                //Log.d("RegisterActivity", "Successfully created user with uid: ${it.result?.user?.uid}")

                                //그럴일 없겠지만 실패하면 이 메세지를 띄워서 관리자한테 문의하도록한다.
                                //Toast.makeText(this, "로그인에 실패했습니다.\n관리자에게 문의바랍니다.", Toast.LENGTH_SHORT).show()
                                // 인증이 안되었을 때 메일을 보내게 된다.

                            }



                    }
                */}

            }
            //로그인 실패했을 때 뜨는 것
            .addOnFailureListener {
                ShowDialog("로그인 실패", "로그인에 실패했습니다.\n사유 : ${it.message}", "확인")
                email_edittext_login.text = null
                password_edittext_login.text = null

                //progress bar 숨기기
                main_progressBar.visibility = View.GONE

            }



    }

    fun ShowDialog(title : String, message : String, Posbutton : String){

        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(Posbutton) { dialog, which -> }

        /*
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            Toast.makeText(applicationContext,
                android.R.string.yes, Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            Toast.makeText(applicationContext,
                android.R.string.no, Toast.LENGTH_SHORT).show()
        }

        builder.setNeutralButton("Maybe") { dialog, which ->
            Toast.makeText(applicationContext,
                "Maybe", Toast.LENGTH_SHORT).show()
        }
        */
        builder.show()

    }

 /////
}
