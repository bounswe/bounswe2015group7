package tr.edu.boun.cmpe.sculture.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tr.edu.boun.cmpe.sculture.R;
import tr.edu.boun.cmpe.sculture.activity.SearchActivity;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    private Activity mActivity;
    private ArrayList<ArrayList<String>> mDataset;
    public int pos;
    String id;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView storyTitle;
        private ItemClickListener clickListener;
        public TextView storyLastUpdatedAt;
        public LinearLayout lnrLayout;

        public ViewHolder(View v) {
            super(v);
            storyTitle = (TextView) v.findViewById(R.id.storyTitle);
            storyLastUpdatedAt = (TextView) v.findViewById(R.id.storyLastUpdatedAt);
            lnrLayout = (LinearLayout)v.findViewById(R.id.searchedStory);
            v.setOnClickListener(this);

        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }
        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition());
        }
    }






    public void add(int position, ArrayList<String> item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    /*public void remove(String name, String email) {
        for(int i = 0; i < mDataset.size(); i++){
            if(mDataset.get(i).get(0).equals(name) && mDataset.get(i).get(1).equals(email)){
                mDataset.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }*/

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapter(ArrayList<ArrayList<String>> myDataset, Activity activity) {
        mDataset = myDataset;
        mActivity = activity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.searched_story, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    public String StringToDate(String ISODate){

        String dtStart = ISODate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
           Date date = format.parse(ISODate);
            return date.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "null";
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        pos = position;
        holder.storyTitle.setText(mDataset.get(position).get(0));
        id = mDataset.get(position).get(2);
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (mActivity != null && mActivity instanceof SearchActivity) {
                    ((SearchActivity)mActivity).sendToNextActivity(mDataset.get(position).get(2));
                }
            }
        });






        /*holder.storyTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // remove(name, email);
                    int itemPosition = this.;

                    if (mActivity != null && mActivity instanceof SearchActivity) {
                    ((SearchActivity)mActivity).sendToNextActivity(id);
                }
            }
        });*/
            //holder.storyTags.setText(mDataset.get(position).get(1));
           // holder.storyContent.setText(mDataset.get(position).get(7));
            //holder.storyOwner.setText(mDataset.get(position).get(3));
            //holder.storyCreatedAt.setText(StringToDate(mDataset.get(position).get(4)));
            //holder.storyLastEditor.setText(mDataset.get(position).get(5));
            holder.storyLastUpdatedAt.setText("Last Updated Date: "+mDataset.get(position).get(1).substring(0,10)+" " +mDataset.get(position).get(1).substring(11,19) );




    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}