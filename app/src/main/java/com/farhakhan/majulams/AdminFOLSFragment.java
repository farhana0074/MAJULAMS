package com.farhakhan.majulams;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.farhakhan.majulams.model_classes.AdminDepLeaves;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminFOLSFragment extends Fragment {


    public AdminFOLSFragment() { }

    private FirebaseDatabase mFirebaseDb;
    private DatabaseReference mDbReference;
    private RecyclerView recyclerViewFOLS ;
    private LinearLayoutManager linearLayoutManagerFOLS;
    FirebaseRecyclerAdapter <AdminDepLeaves, DepLeavesViewHolder>recyclerAdapterFOLS ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_fols, container, false);
        TextView tvFOLS = view.findViewById(R.id.tv_name_FOLS);
        tvFOLS.setText("Faculty of Life Sciences");

        mFirebaseDb=FirebaseDatabase.getInstance();
        mDbReference=mFirebaseDb.getReference();

        recyclerViewFOLS = view.findViewById(R.id.recycler_view_FOLS);
        linearLayoutManagerFOLS = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerViewFOLS.setLayoutManager(linearLayoutManagerFOLS);
        final Query queryFOLS = mDbReference.child("FOLS");
        FirebaseRecyclerOptions<AdminDepLeaves> optionsFOLS =
                new FirebaseRecyclerOptions.Builder<AdminDepLeaves>()
                        .setQuery(queryFOLS, AdminDepLeaves.class)
                        .build();
        recyclerAdapterFOLS = new FirebaseRecyclerAdapter<AdminDepLeaves, DepLeavesViewHolder>(optionsFOLS) {
            @Override
            protected void onBindViewHolder(@NonNull final DepLeavesViewHolder holder, int position, @NonNull AdminDepLeaves model) {
                final String DepNameFromKey = this.getRef(position).getKey();
                holder.DepName.setText("Department of "+DepNameFromKey);

                Query queryHL= ((DatabaseReference) queryFOLS).child(DepNameFromKey).child("EmployeeLeaves")
                        .child("HalfLeaves");
                queryHL.orderByChild("LeaveApprovalStatus").equalTo("Pending")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String st = String.valueOf(dataSnapshot.getChildrenCount());
                                holder.PendingHL.setText(st);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }});

                queryHL.orderByChild("LeaveApprovalStatus").equalTo("Processed By HoD")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String st = String.valueOf(dataSnapshot.getChildrenCount());
                                holder.ProcessedHL.setText(st);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }});

                Query queryFL= ((DatabaseReference) queryFOLS).child(DepNameFromKey).child("EmployeeLeaves")
                        .child("FullLeaves");
                queryFL.orderByChild("LeaveApprovalStatus").equalTo("Pending")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String st = String.valueOf(dataSnapshot.getChildrenCount());
                                holder.PendingFL.setText(st);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }});

                queryFL.orderByChild("LeaveApprovalStatus").equalTo("Processed By HoD")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String st = String.valueOf(dataSnapshot.getChildrenCount());
                                holder.ProcessedFL.setText(st);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }});

                Query queryLWP= ((DatabaseReference) queryFOLS).child(DepNameFromKey).child("EmployeeLeaves")
                        .child("LeavesWithoutPay");
                queryLWP.orderByChild("LeaveApprovalStatus").equalTo("Pending")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String st = String.valueOf(dataSnapshot.getChildrenCount());
                                holder.PendingLWP.setText(st);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }});

                queryLWP.orderByChild("LeaveApprovalStatus").equalTo("Processed By HoD")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String st = String.valueOf(dataSnapshot.getChildrenCount());
                                holder.ProcessedLWP.setText(st);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }});

                Query querySL= ((DatabaseReference) queryFOLS).child(DepNameFromKey).child("EmployeeLeaves")
                        .child("SummerLeaves");
                querySL.orderByChild("LeaveApprovalStatus").equalTo("Pending")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String st = String.valueOf(dataSnapshot.getChildrenCount());
                                holder.PendingSL.setText(st);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }});

                querySL.orderByChild("LeaveApprovalStatus").equalTo("Processed By HoD")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String st = String.valueOf(dataSnapshot.getChildrenCount());
                                holder.ProcessedSL.setText(st);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }});


            }

            @NonNull
            @Override
            public DepLeavesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.admin_dep_details_item, viewGroup, false);

                return new DepLeavesViewHolder(view);
            }
        };
        recyclerViewFOLS.setAdapter(recyclerAdapterFOLS);
        return view;
    }

    @Override
    public void onStart() {
        recyclerAdapterFOLS.startListening();
        super.onStart();
    }

    public static class DepLeavesViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView DepName;
        TextView PendingHL;
        TextView ProcessedHL;
        TextView PendingFL;
        TextView ProcessedFL;
        TextView PendingLWP;
        TextView ProcessedLWP;
        TextView PendingSL;
        TextView ProcessedSL;

        public DepLeavesViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            DepName = itemView.findViewById(R.id.txtI_Dep_name);
            PendingHL = itemView.findViewById(R.id.tv_pending_HL);
            ProcessedHL = itemView.findViewById(R.id.tv_processed_HL);
            PendingFL = itemView.findViewById(R.id.tv_pending_FL);
            ProcessedFL = itemView.findViewById(R.id.tv_processed_FL);
            PendingLWP = itemView.findViewById(R.id.tv_pending_LWP);
            ProcessedLWP = itemView.findViewById(R.id.tv_processed_LWP);
            PendingSL = itemView.findViewById(R.id.tv_pending_SL);
            ProcessedSL = itemView.findViewById(R.id.tv_processed_SL);
        }
    }

}
