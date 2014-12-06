package com.quitevis.dailyselfie;

import java.util.List;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SelfieAdapter extends BaseAdapter {
	private final List<Uri> source;
	private final Context context;
	private TextView path;
	private ImageView image;
	public SelfieAdapter(List<Uri> values, Context context) {
		source = values;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return source.size();
	}

	@Override
	public Object getItem(int arg0) {
		return source.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View old, ViewGroup parent) {
		View view = null;
		Uri uri = source.get(position);
		
		if (old == null) {
		    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    view = inflater.inflate(R.layout.selfierow, parent, false);
		    path = (TextView) view.findViewById(R.id.txtPath);
		    image = (ImageView) view.findViewById(R.id.imgSelfie);
		}
		else {
			view = old;
		}
		
		path.setText(source.get(position).getLastPathSegment());
		Picasso.with(context).load(uri).resize(50, 50).into(image);		
		return view;
	}

	public void add(Uri path) {
		source.add(path);
	}
}
