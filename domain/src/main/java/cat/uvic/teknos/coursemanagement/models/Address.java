package cat.uvic.teknos.coursemanagement.models;

public interface Address {
    int getId();
    void setId(int id);
    String getZip();
    void setZip(String zip);
    String getStreet();
    void setStreet(String street);
}
