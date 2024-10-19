import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {

    public static void main(String[] args) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("input.json"));

            JSONObject keys = (JSONObject) jsonObject.get("keys");
            int n = ((Long) keys.get("n")).intValue();
            int k = ((Long) keys.get("k")).intValue();

            Map<Integer, BigInteger> points = new HashMap<>();
            for (int i = 1; i <= n; i++) {
                if (jsonObject.containsKey(String.valueOf(i))) {
                    JSONObject root = (JSONObject) jsonObject.get(String.valueOf(i));
                    int x = i;
                    int base = Integer.parseInt((String) root.get("base"));
                    String value = (String) root.get("value");
                    BigInteger y = new BigInteger(value, base);
                    points.put(x, y);
                }
            }

            BigInteger secret = findConstantTerm(points, k);
            System.out.println("The secret is: " + secret);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static BigInteger findConstantTerm(Map<Integer, BigInteger> points, int k) {
        BigInteger result = BigInteger.ZERO;
        for (Map.Entry<Integer, BigInteger> entry : points.entrySet()) {
            int xi = entry.getKey();
            BigInteger yi = entry.getValue();
            BigInteger li = BigInteger.ONE;

            for (Map.Entry<Integer, BigInteger> other : points.entrySet()) {
                int xj = other.getKey();
                if (xi != xj) {
                    li = li.multiply(BigInteger.valueOf(-xj)).divide(BigInteger.valueOf(xi - xj));
                }
            }
            result = result.add(yi.multiply(li));
        }
        return result;
    }
}
