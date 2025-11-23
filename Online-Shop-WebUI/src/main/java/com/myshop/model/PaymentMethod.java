
package com.myshop.model;

import java.time.YearMonth;

public class PaymentMethod {
    private int id;
    private int userId;
    private String brand;     // e.g., VISA/MC
    private String last4;     // masked
    private int expMonth;     // 1..12
    private int expYear;      // yyyy
    private String token;     // opaque (never PAN)
    private boolean isDefault;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getLast4() { return last4; }
    public void setLast4(String last4) { this.last4 = last4; }

    public int getExpMonth() { return expMonth; }
    public void setExpMonth(int expMonth) { this.expMonth = expMonth; }

    public int getExpYear() { return expYear; }
    public void setExpYear(int expYear) { this.expYear = expYear; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean aDefault) { isDefault = aDefault; }

    public boolean isExpired() {
        YearMonth ym = YearMonth.of(expYear, expMonth);
        return ym.isBefore(YearMonth.now());
    }

    @Override
    public String toString() {
        return "PaymentMethod{brand=" + brand + ", last4=**** **** **** " + last4 +
               ", exp=" + expMonth + "/" + expYear + ", default=" + isDefault + "}";
    }
}
