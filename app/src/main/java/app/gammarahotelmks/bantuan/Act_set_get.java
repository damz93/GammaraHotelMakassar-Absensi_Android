package app.gammarahotelmks.bantuan;


public class Act_set_get {
    private static String usname;

    private static String dept;
    private static String nama;
    private static String kategori;



    public String getusnme() {
        return usname;
    }

    public void setusnme(final String usnmex) {
        this.usname = usnmex;
    }
    public String getdept() {
        return dept;
    }

    public void setdept(final String deptx) {
        this.dept = deptx;
    }


    public String getkategori() {
        return kategori;
    }

    public void setkategori(final String kategorix) {
        this.kategori = kategorix;
    }
    public String getnama() {
        return nama;
    }

    public void setnama(final String namax) {
        this.nama = namax;
    }
}
