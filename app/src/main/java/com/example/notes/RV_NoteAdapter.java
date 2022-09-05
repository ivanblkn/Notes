package com.example.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;


public class RV_NoteAdapter extends RecyclerView.Adapter<RV_NoteAdapter.ViewHolder> {
    NoteSourceInterface noteSource;
    private OnItemClickListener itemClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public RV_NoteAdapter(NoteSourceInterface noteSource) {
        this.noteSource = noteSource;
    }

    @NonNull
    @Override
    public RV_NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_card, parent, false);

        return new RV_NoteAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RV_NoteAdapter.ViewHolder holder, int position) {
        holder.setData(noteSource.getCardNote(position));
    }

    @Override
    public int getItemCount() {
        return noteSource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private TextView addInformation;
        private AppCompatImageView imgImportant;
        private AppCompatImageView doneField;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title_nc);
            description = itemView.findViewById(R.id.description_nc);
            imgImportant = itemView.findViewById(R.id.card_logo);
            doneField = itemView.findViewById(R.id.card_done);
            addInformation = itemView.findViewById(R.id.add_information);

            itemView.findViewById(R.id.card_note_cont).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (itemClickListener != null)
                        itemClickListener.onItemClick(view, position);

                }
            });
        }

        public void setData(Note noteData) {
            title.setText(noteData.getName());
            description.setText(noteData.getDescription());
            imgImportant.setImageResource(noteData.getImportance().getColorRes());
            if (noteData.getState()) {
                doneField.setImageResource(R.drawable.check_do);
            } else {
                doneField.setImageResource(R.drawable.is_not_do);
            }
            String str_addInformation = "Заметка создана: " + noteData.getDateOfCreate().toString();
            if (noteData.getRemindDate() != null)
                str_addInformation += " Установлено напоминание: " + noteData.getRemindDate().toString();
            addInformation.setText(str_addInformation);
        }


    }

}
