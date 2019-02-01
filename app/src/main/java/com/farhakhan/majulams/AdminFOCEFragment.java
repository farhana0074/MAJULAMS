package com.farhakhan.majulams;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

public class AdminFOCEFragment extends Fragment {

    public AdminFOCEFragment() {
    }

    private FirebaseDatabase mFirebaseDb;
    private DatabaseReference mDbReference;
    private RecyclerView recyclerViewFOCE ;
    private LinearLayoutManager linearLayoutManagerFOCE;
    FirebaseRecyclerAdapter <AdminDepLeaves, DepLeavesViewHolder>recyclerAdapterFOCE ;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_foce, container, false);
        TextView tvFOCE = view.findViewById(R.id.tv_name_FOCE);
        tvFOCE.setText("Faculty of Computing & Engineering");
        String FOCE = "FOCE";

        mFirebaseDb=FirebaseDatabase.getInstance();
        mDbReference=mFirebaseDb.getReference();

        recyclerViewFOCE = view.findViewById(R.id.recycler_view_FOCE);
        linearLayoutManagerFOCE = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerViewFOCE.setLayoutManager(linearLayoutManagerFOCE);
        final Query queryFOCE = mDbReference.child("FOCE");
        FirebaseRecyclerOptions<AdminDepLeaves> optionsFOCE =
                new FirebaseRecyclerOptions.Builder<AdminDepLeaves>()
                        .setQuery(queryFOCE, AdminDepLeaves.class)
                        .build();
        final Bundle bundle = new Bundle();
        bundle.putString("Faculty", FOCE);
        recyclerAdapterFOCE = new FirebaseRecyclerAdapter<AdminDepLeaves, DepLeavesViewHolder>(optionsFOCE) {
            @Override
            protected void onBindViewHolder(@NonNull final DepLeavesViewHolder holder, int position, @NonNull AdminDepLeaves model) {
              final String DepNameFromKey = this.getRef(position).getKey();
                holder.DepName.setText("Department of "+DepNameFromKey);

                Query queryHL= ((DatabaseReference) queryFOCE).child(DepNameFromKey).child("EmployeeLeaves")
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

                Query queryFL= ((DatabaseReference) queryFOCE).child(DepNameFromKey).child("EmployeeLeaves")
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

                Query queryLWP= ((DatabaseReference) queryFOCE).child(DepNameFromKey).child("EmployeeLeaves")
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

                Query querySL= ((DatabaseReference) queryFOCE).child(DepNameFromKey).child("EmployeeLeaves")
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

                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bundle.putString("DepNameFromKey",DepNameFromKey);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        AdminDepWiseLeaves adminDepWiseLeaves = new AdminDepWiseLeaves();
                        adminDepWiseLeaves.setArguments(bundle);
                        fragmentTransaction.add(R.id.admin_foce, adminDepWiseLeaves).addToBackStack(null)
                                .commit();
                    }
                });

            }

            @NonNull
            @Override
            public DepLeavesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.admin_dep_details_item, viewGroup, false);

                return new DepLeavesViewHolder(view);
            }
        };
        recyclerViewFOCE.setAdapter(recyclerAdapterFOCE);
        return view;
    }

    @Override
    public void onStart() {
        recyclerAdapterFOCE.startListening();
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
