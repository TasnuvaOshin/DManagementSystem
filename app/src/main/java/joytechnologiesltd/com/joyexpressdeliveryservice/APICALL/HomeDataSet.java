package joytechnologiesltd.com.joyexpressdeliveryservice.APICALL;

public class HomeDataSet {

public String id;
    public String driver_id;
    public String delivery_id;
    public String merchant_id;
    public String status;
    public String created_at;
    public String rec_name;
    public String rec_number;
    public String rec_address;
    public String rec_zone;
    public String amount;
    public String instruction;
    public String merchant_name;
    public String merchant_email;
    public String merchant_phone;
    public String current_lat;
    public String current_lon;

    public String getCurrent_lat() {
        return current_lat;
    }

    public void setCurrent_lat(String current_lat) {
        this.current_lat = current_lat;
    }

    public String getCurrent_lon() {
        return current_lon;
    }

    public void setCurrent_lon(String current_lon) {
        this.current_lon = current_lon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(String delivery_id) {
        this.delivery_id = delivery_id;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getRec_name() {
        return rec_name;
    }

    public void setRec_name(String rec_name) {
        this.rec_name = rec_name;
    }

    public String getRec_number() {
        return rec_number;
    }

    public void setRec_number(String rec_number) {
        this.rec_number = rec_number;
    }

    public String getRec_address() {
        return rec_address;
    }

    public void setRec_address(String rec_address) {
        this.rec_address = rec_address;
    }

    public String getRec_zone() {
        return rec_zone;
    }

    public void setRec_zone(String rec_zone) {
        this.rec_zone = rec_zone;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getMerchant_email() {
        return merchant_email;
    }

    public void setMerchant_email(String merchant_email) {
        this.merchant_email = merchant_email;
    }

    public String getMerchant_phone() {
        return merchant_phone;
    }

    public void setMerchant_phone(String merchant_phone) {
        this.merchant_phone = merchant_phone;
    }

    public HomeDataSet() {
    }

    public HomeDataSet(String id, String driver_id, String delivery_id, String merchant_id, String status, String created_at, String rec_name, String rec_number, String rec_address, String rec_zone, String amount, String instruction, String merchant_name, String merchant_email, String merchant_phone,String current_lat,String current_lon) {
        this.id = id;
        this.driver_id = driver_id;
        this.delivery_id = delivery_id;
        this.merchant_id = merchant_id;
        this.status = status;
        this.created_at = created_at;
        this.rec_name = rec_name;
        this.rec_number = rec_number;
        this.rec_address = rec_address;
        this.rec_zone = rec_zone;
        this.amount = amount;
        this.instruction = instruction;
        this.merchant_name = merchant_name;
        this.merchant_email = merchant_email;
        this.merchant_phone = merchant_phone;
        this.current_lat = current_lat;
        this.current_lon = current_lon;
    }
}
