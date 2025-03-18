import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmycaterer.R

class UpdatePackageAdapter(
    private val menuNames: List<String>, // List of strings (menu item names)
    private val onDeleteClick: (String) -> Unit // Callback with menu item name
) : RecyclerView.Adapter<UpdatePackageAdapter.PackageViewHolder>() {

    inner class PackageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.menuItemName)
        val deleteButton: Button = itemView.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.update_pakage_list, parent, false)
        return PackageViewHolder(view)
    }

    override fun onBindViewHolder(holder: PackageViewHolder, position: Int) {
        val currentName = menuNames[position]
        holder.nameTextView.text = currentName

        holder.deleteButton.setOnClickListener {
            onDeleteClick(currentName) // Pass the name when delete is clicked
        }
    }

    override fun getItemCount(): Int = menuNames.size
}
