package blog.home.deepinsight.humanepedagogues;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import blog.home.deepinsight.humanepedagogues.AspirantsFragment.OnListFragmentInteractionListener;
import blog.home.deepinsight.humanepedagogues.db.Aspirant;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Aspirant} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyAspirantsRecyclerViewAdapter extends RecyclerView.Adapter<MyAspirantsRecyclerViewAdapter.ViewHolder> {

    private List<Aspirant> mAspirants;
    private final OnListFragmentInteractionListener mListener;

    public MyAspirantsRecyclerViewAdapter(List<Aspirant> aspirants, OnListFragmentInteractionListener listener) {
        mAspirants = aspirants;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_aspirants, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mAspirants.get(position);
        holder.mIdView.setText(mAspirants.get(position).studentId.toString());
        holder.mContentView.setText(mAspirants.get(position).studentName);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAspirants.size();
    }

    public void setAspirants(List<Aspirant> aspirants) {
        mAspirants = aspirants;

        for (Aspirant aspirant : mAspirants) {
            if (aspirant.teacherId.equals(MainActivity.instance.currentTeacherId))
                aspirant.finalScore = aspirant.studentScore;
        }

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Aspirant mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
