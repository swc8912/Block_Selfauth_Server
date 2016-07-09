package Model;

/**
 * Created by user on 2016-07-09.
 */
public class AuthInfo {
    private String key;			// value of key
    private String primeNum;	// multiplication of prime numbers
    private String date;		// date of record

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getPrimeNum() {
        return primeNum;
    }
    public void setPrimeNum(String primeNum) {
        this.primeNum = primeNum;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
