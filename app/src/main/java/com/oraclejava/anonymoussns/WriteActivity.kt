package com.oraclejava.anonymoussns

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.oraclejava.anonymoussns.databinding.ActivityWriteBinding
import com.oraclejava.anonymoussns.model.Comment
import com.oraclejava.anonymoussns.model.Post
import com.squareup.picasso.Picasso

class WriteActivity : AppCompatActivity() {

    private lateinit var binding:ActivityWriteBinding

    // 현재 선택된 배경이미지의 포지션을 저장하는 변수
    var currentBgPosition = 0

    val bgList = mutableListOf(
        "android.resource://com.oraclejava.anonymoussns/drawable/sora",
        "android.resource://com.oraclejava.anonymoussns/drawable/sora2",
        "android.resource://com.oraclejava.anonymoussns/drawable/sora3",
        "android.resource://com.oraclejava.anonymoussns/drawable/sora4",
        "android.resource://com.oraclejava.anonymoussns/drawable/sora5",
        "android.resource://com.oraclejava.anonymoussns/drawable/sora6"
    )

    // 글쓰기 모드를 저장하는 변수
    var mode = "post"

    // 댓글쓰기인 경우 글의 ID
    var postId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 전달받은 intent 에서 댓글 모드인지 확인한다.
        intent.getStringExtra("mode")?.let {
            mode = intent.getStringExtra("mode")!!
            postId = intent.getStringExtra("postId")!!
        }

        // recyclerView 에서 사용할 레이아웃 매니저를 생성한다.
        val layoutManager = LinearLayoutManager(this@WriteActivity)
        // recyclerView 를 횡으로 스크롤 할것이므로 layoutManager 의 방향을 HORIZONTAL 로 설정한다.
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        // recyclerView 에 레이아웃 매니저를 방금 생성한 layoutManger 로 설정한다.
        binding.recyclerView.layoutManager = layoutManager
        // recyclerView 에 adapter 를 설정한다.
        binding.recyclerView.adapter = MyAdapter()
        // 공유하기 버튼이 클릭된 경우에 이벤트리스너를 설정한다.
        binding.sendButton.setOnClickListener {
            // 메세지가 없는 경우 토스트 메세지로 알림.
            if (TextUtils.isEmpty(binding.input.text)) {
                Toast.makeText(applicationContext, "메세지를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (mode == "post") {

                // Firebase 의 Posts 참조에서 객체를 저장하기 위한 새로운 카를 생성하고 참조를 newRef 에 저장
                val newRef = FirebaseDatabase.getInstance().getReference("Posts").push()
                // Post 객체 생성
                val post = Post(
                    newRef.key.toString(),
                    getMyId(),
                    binding.input.text.toString(),
                    ServerValue.TIMESTAMP,
                    bgList[currentBgPosition]
                )

                // Post 객체를 새로 생성한 참조에 저장
                newRef.setValue(post)
                // 저장성공 토스트 알림을 보여주고 Activity 종료
                Toast.makeText(applicationContext, "공유되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            } else {

                // Firebase 의 Posts 참조에서 객체를 저장하기 위한 새로운 카를 생성하고 참조를 newRef 에 저장
                val newRef = FirebaseDatabase.getInstance().getReference("Comments/$postId").push()
                val comment = Comment(
                    newRef.key.toString(),
                    postId,
                    getMyId(),
                    binding.input.text.toString(),
                    ServerValue.TIMESTAMP,
                    bgList[currentBgPosition]
                    )

                newRef.setValue(comment)
                // 저장성공 토스트 알림을 보여주고 Activity 종료
                Toast.makeText(applicationContext, "공유되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    /**
     * 디바이스의 ID 를 반환하는 메소드
     * 글쓴 사람의 ID 를 인식합니다.
     */
    fun getMyId(): String {
        return "user"
    }

    /**
     * RecyclerView 에서 사용하는 View 홀더 클래스
     */
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById(R.id.imageView) as ImageView
    }

    /**
     * RecyclerView 의 어댑터 클래스
     */
    inner class MyAdapter : RecyclerView.Adapter<MyViewHolder>() {
        /**
         * RecyclerView 에서 각 Row(행)에서 그릴 ViewHolder 를 생성할때 불리는 메소드
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            // RecyclerView 에서 사용하는 ViewHolder 클래스를 card_background 레이아웃 리소스 파일을 사용하도록 생성한다.
            return MyViewHolder(LayoutInflater.from(this@WriteActivity).inflate(R.layout.card_background, parent, false))
        }

        /**
         * RecyclerView 에서 몇개의 행을 그릴지 기준이 되는 메소드
         */
        override fun getItemCount(): Int {
            //
            return bgList.size
        }

        /**
         * 각 행의 포지션에서 그려야할 ViewHolder UI 에 데이터를 적용하는 메소드
         */
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            // 이미지 로딩 라이브러리인 피카소 객체로 뷰홀더에 존재하는 imageView 에 이미지 로딩
            Picasso.get()
                .load(Uri.parse(bgList[position]))
                .fit()
                .centerCrop()
                .into(holder.imageView)
            // 각 배경화면 행이 클릭된 경우에 이벤트 리스너 설정
            holder.itemView.setOnClickListener {
                // 선택된 배경의 포지션을 currentBgPosition 에 저장
                currentBgPosition = position
                // 이미지 로딩 라이브러리인 피카소 객체로 뷰홀더에 존재하는 글쓰기 배경 이미지뷰에 이미지 로딩
                Picasso.get()
                    .load(Uri.parse(bgList[position]))
                    .fit()
                    .centerCrop()
                    .into(binding.writeBackground)
            }
        }
    }
}