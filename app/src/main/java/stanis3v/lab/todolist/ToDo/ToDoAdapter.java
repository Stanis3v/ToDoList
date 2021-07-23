package stanis3v.lab.todolist.ToDo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;
import stanis3v.lab.todolist.R;
import stanis3v.lab.todolist.MainActivity;

public class ToDoAdapter extends BaseAdapter {

    private final MainActivity context;
    private final int layout;
    private final List<ToDo> toDoList;

    public ToDoAdapter(MainActivity context, int layout, List<ToDo> toDoList) {
        this.context = context;
        this.layout = layout;
        this.toDoList = toDoList;
    }

    @Override
    public int getCount() {
        return toDoList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private static class ViewHolder {
        TextView txtTitle, txtDes, txtRemind;
        ImageButton btnDone;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);

            holder.txtTitle = convertView.findViewById(R.id.textTitle);
            holder.txtDes = convertView.findViewById(R.id.textDescription);
            holder.txtRemind = convertView.findViewById(R.id.textRemind);
            holder.btnDone = convertView.findViewById(R.id.done);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ToDo toDo=toDoList.get(position);

        holder.txtTitle.setText(toDo.getTitle());
        holder.txtDes.setText(toDo.getDes());
        holder.txtRemind.setText(toDo.getRemind());

        holder.btnDone.setOnClickListener(v -> context.ShowDialogDone(toDo.getId()));

        return convertView;
    }
}