package Keygenerator;


import java.security.SecureRandom;
import bigjava.math.BigInteger;

/**
 * Created by user on 2016-07-09.
 */

public class primeGenerator {

    static public BigInteger getPrimeNumber(int bits)
    {
        SecureRandom ran = new SecureRandom();

        BigInteger c = new BigInteger(bits, ran);

        for (; ; )
        {
            if (c.isProbablePrime(1) == true) break;

            c = c.subtract(new BigInteger("1"));
        }
        return (c);
    }
}
