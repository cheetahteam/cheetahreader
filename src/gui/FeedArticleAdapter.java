package gui;

import java.io.IOException;

import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tokentest.Article;
import com.example.tokentest.FeedManager;
import com.example.tokentest.R;

//change 1
import android.widget.Button;
import android.view.View.OnClickListener;

public class FeedArticleAdapter extends ArrayAdapter<Article> {
	private Activity activity;
	private FeedManager _feedManager = FeedManager.getInstance();
	private static LayoutInflater inflater = null;
	private final int rowResourceId;

	public FeedArticleAdapter(Activity activity, int textViewResourceId,
			FeedManager fm) {
		// _feedManager = FeedManager.getInstance();

		super(activity, textViewResourceId, fm.getArticles());
		_feedManager = fm;
		rowResourceId = textViewResourceId;

		try {
			this.activity = activity;

			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		} catch (Exception e) {

		}
	}

	public int getCount() {
		return _feedManager.feedArticles.size();
	}

	/*
	 * public Article getItem(Article position) { return position; }
	 * 
	 * public long getItemId(int position) { return position; }
	 * 
	 * public static class ViewHolder { public TextView display_name; public
	 * TextView display_number;
	 * 
	 * }
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView = inflater.inflate(rowResourceId, parent, false);
		// ImageView imageView = (ImageView)
		// rowView.findViewById(R.id.imageView);
		TextView textView = (TextView) rowView.findViewById(R.id.textView);

		// String imageFile = Model.GetbyId(id).IconFile;

		final Article article = _feedManager.feedArticles.get(position);

		textView.setText(article.getTitle());

		// change 1
		final Button markread = (Button) rowView.findViewById(R.id.markread);

		textView.setText(article.getTitle());

		// change 1

		textView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String url = article.getPeekLink();
				Intent intent = new Intent(activity, WebViewActivity.class);
				intent.putExtra("URL", url);

				v.getContext().startActivity(intent);

			}

		});

		markread.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (v == markread) {
					// if( markread.getBackground() )

					// if(markread.getTag() =="0")
					_feedManager.actionManager.addAction(article.toggleMarkRead());
					if (article.getMarkread() == false) {

						// if (
						// markread.getBackground().getConstantState()==markread.getResources().getDrawable(R.drawable.blue).getConstantState())
						// {

						markread.setBackgroundResource(R.drawable.listviewbuttons2);
						// }

					} else {
						// if (
						// markread.getBackground().getConstantState()==markread.getResources().getDrawable(R.drawable.red).getConstantState())
						// {

						markread.setBackgroundResource(R.drawable.listviewbuttons);
						// }
						// markread.setTag("0");

					}
				}

			}
		});

		// get input stream
		// InputStream ims = null;

		/*
		 * try { ims = activity.getAssets().open(imageFile); } catch
		 * (IOException e) { e.printStackTrace(); } // load image as Drawable
		 * Drawable d = Drawable.createFromStream(ims, null); // set image to
		 * ImageView imageView.setImageDrawable(d);
		 */

		rowView.setTag(position);

		return rowView;

	}
}