package tech.dat250project.model;

public class DeviceVote {
    private String address;
    private int red;
    private int green;

    public DeviceVote() {}

    public DeviceVote(String address, int red, int green) {
        this.address = address;
        this.red = red;
        this.green = green;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }
}
