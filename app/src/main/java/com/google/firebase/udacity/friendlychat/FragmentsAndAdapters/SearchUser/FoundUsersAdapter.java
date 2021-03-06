package com.google.firebase.udacity.friendlychat.FragmentsAndAdapters.SearchUser;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.udacity.friendlychat.Managers.App.FragmentsManager;
import com.google.firebase.udacity.friendlychat.Managers.Database.ConversationRequest;
import com.google.firebase.udacity.friendlychat.Objects.User;
import com.google.firebase.udacity.friendlychat.R;

import java.util.ArrayList;
import java.util.List;

public class FoundUsersAdapter extends RecyclerView.Adapter<FoundUsersAdapter.FoundUserViewHolder> {

	private List<User> users;
	private Context context;

	FoundUsersAdapter(Context context) {
		users = new ArrayList<>();
		this.context = context;
	}

	@Override
	public FoundUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);

		return new FoundUserViewHolder(v);
	}

	@Override
	public void onBindViewHolder(final FoundUserViewHolder holder, final int position) {

		holder.bind(users.get(position));
	}

	@Override
	public int getItemCount() {
		return users.size();
	}

	public void pushUser(User user) {
		users.add(user);
		notifyItemInserted(users.size() - 1);
	}

	public void clear() {
		users.clear();
		notifyDataSetChanged();
	}

	class FoundUserViewHolder extends RecyclerView.ViewHolder {

		TextView userNameTextView;
		ImageView userAvatarImageView;
		ConstraintLayout userItemLayout;

		FoundUserViewHolder(View view) {
			super(view);
			userNameTextView = view.findViewById(R.id.username);
			userAvatarImageView = view.findViewById(R.id.avatar);
			userItemLayout = view.findViewById(R.id.user_item_layout);
		}

		void bind(User user) {
			setText(user);

			String contactAvatar = "http://digitalspyuk.cdnds.net/17/25/980x490/landscape-1498216547-avatar-neytiri.jpg";
			String checkAvatar = user.avatarUri;

			if (checkAvatar != null && !checkAvatar.equals("null"))
				contactAvatar = checkAvatar;

			Glide.with(context).load(contactAvatar).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(userAvatarImageView);

			onLayoutClick(user);
		}

		private void setText(User user) {
			userNameTextView.setText(user.User_Name);
		}

		private void onLayoutClick(User user) {

			userItemLayout.setOnClickListener(v ->

					ConversationRequest.checkIfConversationExists(user.User_ID, context)
							.subscribe(key -> {
										FragmentsManager.startMessageFragment((AppCompatActivity) context, key);
										Log.i("Conv ID", key);
									},
									Throwable::printStackTrace,
									() -> Log.i("User and conv search", "complete"))
			);
		}
	}
}
