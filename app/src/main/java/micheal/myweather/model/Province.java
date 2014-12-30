package micheal.myweather.model;

/**
 * Created by Administrator on 2014/12/27.
 */
public class Province {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id ;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    private  String provinceName;
    private  String provinceCode;
}
