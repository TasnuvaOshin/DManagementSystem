package joytechnologiesltd.com.joyexpressdeliveryservice.APICALL;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import joytechnologiesltd.com.joyexpressdeliveryservice.R;


public class HomeViewHolder extends RecyclerView.ViewHolder {
    public TextView status;
    public TextView order_id;
    public TextView order_date;
    public TextView rec_name;
    public TextView rec_amount;
    public TextView merchant_name,merchant_id,zone,instruction,address;
    public ImageButton call_button,more_button;
    LinearLayout linearLayout_details_card;
    RelativeLayout relativeLayout;

    Button ressche,compl,cance,retu;

    public HomeViewHolder(@NonNull View itemView) {
        super(itemView);

        status = itemView.findViewById(R.id.status);
        order_id = itemView.findViewById(R.id.order_id);
        order_date = itemView.findViewById(R.id.order_date);
        rec_name = itemView.findViewById(R.id.rec_name);
        rec_amount = itemView.findViewById(R.id.rec_amount);
        call_button = itemView.findViewById(R.id.call_button);
        linearLayout_details_card = itemView.findViewById(R.id.details_card);
        relativeLayout = itemView.findViewById(R.id.relative_layout);
        more_button = itemView.findViewById(R.id.more_button);
        merchant_name = itemView.findViewById(R.id.marchant_name);
        merchant_id = itemView.findViewById(R.id.marchant_id);
        zone = itemView.findViewById(R.id.zone);
        instruction = itemView.findViewById(R.id.instruction);
        address = itemView.findViewById(R.id.rec_address);

        ressche = itemView.findViewById(R.id.button_rescheduled);
        compl = itemView.findViewById(R.id.button_complete);
        cance = itemView.findViewById(R.id.button_canceled);
        retu = itemView.findViewById(R.id.button_returned);
    }
}
