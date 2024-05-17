package ApiConnection;

import Currency.Currency;
import com.google.gson.*;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Api {
    private static final String API_KEY = "5ec18c4a06492ef14aef75f8";
    private static final String baseString = "https://v6.exchangerate-api.com/v6/";
    private static final Gson gson = new Gson();
    private static HttpClient client;
    private static final Map<String,String> currencies = new HashMap<>();
    private static final File history = new File("history.txt");
    private static final String DATE_FORMAT = "dd/MM/yyyy - HH:mm:ss";
    public Api() {
        Api.client = HttpClient.newHttpClient();
        if (Api.currencies.isEmpty()) {
            fetchCodes();
        }
    }

    public String request(String url, String key) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            return String.valueOf(Api.gson.fromJson(client.send(req, HttpResponse.BodyHandlers.ofString()).body(), JsonObject.class).get(key));
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Float getConversionResult(Currency c1, Currency c2, Float valueToConvert) {
        try {
            Instant instant = Instant.now();
            String formattedInstant = DateTimeFormatter.ofPattern(DATE_FORMAT).withZone(ZoneId.systemDefault()).format(instant);
            BufferedWriter bw = new BufferedWriter(new FileWriter(history,true));
            String url = String.format("%s%s/pair/%s/%s/%f",baseString,API_KEY,c1.code(),c2.code(),valueToConvert);
            Float conversionResult = Float.parseFloat(request(url,"conversion_result"));
            String toWrite = String.format("%s // %.4f [%s] ==> %.4f [%s]",formattedInstant,valueToConvert,c1.code(),conversionResult,c2.code());
            bw.write(toWrite);
            bw.newLine();
            bw.close();
            return conversionResult;
        } catch (NullPointerException | IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Float getConversionRate(Currency c1, Currency c2) {
        try {
            String url = String.format("%s%s/pair/%s/%s",baseString,API_KEY,c1.code(),c2.code());
            return Float.parseFloat(request(url,"conversion_rate"));
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private void fetchCodes() {
        String url = String.format("%s%s/codes",baseString,API_KEY);
        JsonArray jsonArray = Api.gson.fromJson(request(url,"supported_codes"),JsonArray.class);
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonArray currentArray = jsonArray.get(i).getAsJsonArray();
            currencies.put(currentArray.get(0).getAsString(),currentArray.get(1).getAsString());
        }
    }

    public boolean isValidCode(String code) {
        return currencies.containsKey(code);
    }

    public Currency getCurrency(String code) {
        if (isValidCode(code)) {
            return new Currency(code,currencies.get(code));
        }
        return null;
    }
}
