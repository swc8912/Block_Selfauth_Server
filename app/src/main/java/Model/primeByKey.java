package Model;

import java.util.ArrayList;

import bigjava.math.BigInteger;

/**
 * Created by user on 2016-07-10.
 */
public class primeByKey {
    public String key;
    public ArrayList<BigInteger> primeNumbers;

    public primeByKey(String key){
        this.key = key;
        primeNumbers = new ArrayList<BigInteger>();

    }
    public void add(BigInteger num){
        primeNumbers.add(num);
    }

    public String getMultiplyPrime(){
        BigInteger temp = new BigInteger("256");
        for(BigInteger num : primeNumbers){
            temp = temp.multiply(num);
        }

        return temp.toString();
    }
}
