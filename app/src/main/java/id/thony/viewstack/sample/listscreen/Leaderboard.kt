package id.thony.viewstack.sample.listscreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import id.thony.viewstack.sample.NameViewKey
import id.thony.viewstack.sample.R
import id.thony.viewstack.sample.databinding.ListViewItemBinding
import id.thony.viewstack.sample.databinding.ViewLeaderboardBinding
import id.thony.viewstack.sample.navigator
import id.thony.viewstack.sample.view.Layout
import kotlinx.parcelize.Parcelize
import kotlin.reflect.KClass

@Parcelize
class LeaderboardKey : NameViewKey("Leaderboard") {
    override fun buildView(context: Context): View = LeaderboardView(context)
}

class LeaderboardView(context: Context) : Layout<ViewLeaderboardBinding>(context) {
    override val bindingClass: KClass<ViewLeaderboardBinding>
        get() = ViewLeaderboardBinding::class

    init {
        val viewAdapter = MyAdapter(Array(10) { "Person ${it + 1}" }) { userProfile ->
            navigator.push(UserProfileKey(userProfile.name))
        }

        binding.leaderboardList.run {
            setHasFixedSize(true)
            adapter = viewAdapter
        }
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = ListViewItemBinding.bind(itemView)

    fun bind(userProfile: MyUserProfile, onItemClickListener: (MyUserProfile) -> Unit) {
        binding.userNameText.text = userProfile.name
        binding.userAvatarImage.setImageResource(userProfile.avatarImage)
        binding.root.setOnClickListener {
            onItemClickListener.invoke(userProfile)
        }
    }

    companion object {
        fun create(parent: ViewGroup): MyViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.list_view_item, parent, false)
            return MyViewHolder(view)
        }
    }
}

data class MyUserProfile(val name: String, @DrawableRes val avatarImage: Int)

class MyAdapter(
    private val myDataset: Array<String>,
    private val onItemClickListener: (MyUserProfile) -> Unit
) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myUserProfile =
            MyUserProfile(myDataset[position], listOfAvatars[position % listOfAvatars.size])
        holder.bind(myUserProfile, onItemClickListener)
    }

    override fun getItemCount(): Int = this.myDataset.size

}

private val listOfAvatars = listOf(
    R.drawable.avatar_1_raster,
    R.drawable.avatar_2_raster,
    R.drawable.avatar_3_raster,
    R.drawable.avatar_4_raster,
    R.drawable.avatar_5_raster,
    R.drawable.avatar_6_raster
)
