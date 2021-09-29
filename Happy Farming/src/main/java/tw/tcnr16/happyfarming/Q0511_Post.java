package tw.tcnr16.happyfarming;

public class Q0511_Post {
    public String Name;
    public String ProduceOrg ;
    public String ContactTel;
    public String posterThumbnailUrl;
    public String SalePlace;
    public String Feature;
    public String Zipcode;

    public Q0511_Post(String Name, String ProduceOrg, String ContactTel,
                      String posterThumbnailUrl, String SalePlace, String Feature) {

        this.Name = Name;
        this.ProduceOrg = ProduceOrg;
        this.ContactTel = ContactTel;
        this.posterThumbnailUrl = posterThumbnailUrl;
        this.SalePlace = SalePlace;
        this.Feature = Feature;
    }
}
