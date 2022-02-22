package com.example.employeedirectoryapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeedirectoryapplication.R;
import com.example.employeedirectoryapplication.model.Employee;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ItemHolder> {

private Context context;
private List<Employee> list = new ArrayList<>();
private int images = R.drawable.ic_placeholder;

public EmployeeAdapter(Context context) {
        this.context = context;
        }

public void addList(List<Employee> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
        }

public Employee removeItem(int position) {
        Employee item = null;
        try {
        item = list.get(position);
        list.remove(position);
        notifyItemRemoved(position);
        } catch (Exception e) {
        }
        return item;
        }

public void clearItem() {
        try {
        list.clear();
        notifyDataSetChanged();
        } catch (Exception e) {
        }
        }

@Override
public EmployeeAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        return new ItemHolder(view);
        }

@Override
public void onBindViewHolder(final ItemHolder holder, final int position) {
final Employee employee = list.get(position);
        if (employee.getProfile_image() == null) {
        holder.mImgView.setImageResource(images);
        }
        holder.mTxtName.setText(employee.getName());

        holder.mTxtCompany.setText((String.valueOf(employee.getCompany().getName())));


/*        holder.mItem.setOnLongClickListener(new View.OnLongClickListener() {
@Override
public boolean onLongClick(View v) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Id: " + employee.getId() +
        " Name: " + employee.getName() +
        " Age: " + employee.getEmployee_age() +
        " Salary: " + employee.getEmployee_salary());
        shareIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(shareIntent, "Share Employee Details"));
        return false;
        }
        });*/

        }

@Override
public int getItemCount() {
        return list.size();
        }

class ItemHolder extends RecyclerView.ViewHolder {

    private RelativeLayout mItem;
    private CircleImageView mImgView;
    private TextView mTxtName, mTxtCompany;

    ItemHolder(View itemView) {
        super(itemView);
        mItem = itemView.findViewById(R.id.item);
        mImgView = itemView.findViewById(R.id.imgProfile);
        mTxtName = itemView.findViewById(R.id.txtName);
        mTxtCompany = itemView.findViewById(R.id.txtCompanyName);
    }
}
}