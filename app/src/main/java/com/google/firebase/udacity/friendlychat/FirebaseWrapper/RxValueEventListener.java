package com.google.firebase.udacity.friendlychat.FirebaseWrapper;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

public class RxValueEventListener {

	static AtomicInteger count = new AtomicInteger(0);

	public static Flowable<DataSnapshot> observeValueEvent(final Query query, BackpressureStrategy backpressureStrategy) {
		return Flowable.create(emitter -> {

			ValueEventListener eventListener = new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot) {
					emitter.onNext(dataSnapshot);
				}

				@Override
				public void onCancelled(DatabaseError databaseError) {
				}
			};

			emitter.setCancellable(() -> {
				query.removeEventListener(eventListener);
				Log.i("ValueEventListener", count.decrementAndGet() + " Cancelling");
			});

			query.addValueEventListener(eventListener);
			count.incrementAndGet();

		}, backpressureStrategy);
	}

}
