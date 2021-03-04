package Main1;

public class Bank {

    private  int acc_no;
    private int bal;

    public Bank(int acc_no, int bal) {
        this.acc_no = acc_no;
        this.bal = bal;
    }

    public int getAcc_no() {
        return acc_no;
    }

    public void setAcc_no(int acc_no) {
        this.acc_no = acc_no;
    }

    public int getBal() {
        return bal;
    }

    public void setBal(int bal) {
        this.bal = bal;
    }

}
