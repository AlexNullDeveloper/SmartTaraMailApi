package ru.smarttara.catalogs;

/**
 * Created by ZXCASD on 17.07.2016.
 */
public class Email {
    private String e_mail;
    private Long is_sended;
    private String company_name;
    private String phone_number;
    private Long category_id;
    private java.sql.Date adding_date;

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public Long getIs_sended() {
        return is_sended;
    }

    public void setIs_sended(Long is_sended) {
        this.is_sended = is_sended;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    public java.sql.Date getAdding_date() {
        return adding_date;
    }

    public void setAdding_date(java.sql.Date adding_date) {
        this.adding_date = adding_date;
    }
}
