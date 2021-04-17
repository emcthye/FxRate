package io.github.emcthye.fxrate.entity;

import androidx.core.util.Pair;

import com.squareup.moshi.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrencyEntity {

    public String base;
    public String date;
    public List<Pair<String,String>> rates = new ArrayList<>();
    public Map<String,String> historyRate = new HashMap<>();
    public static Map<String,String> currencyName = new HashMap<>();

    static {
        currencyName.put("AUD", "Australian Dollar");
        currencyName.put("BGN", "Bulgarian Lev");
        currencyName.put("BRL", "Brazilian Real");
        currencyName.put("CAD", "Canadian Dollar");
        currencyName.put("CHF", "Swiss Franc");
        currencyName.put("CNY", "Yuan");
        currencyName.put("CZK", "Czech Koruna");
        currencyName.put("DKK", "Danish Krone");
        currencyName.put("EUR", "Euro");
        currencyName.put("GBP", "Pound Sterling");
        currencyName.put("HKD", "Hong Kong Dollar");
        currencyName.put("HRK", "Croatian Kuna");
        currencyName.put("HUF", "Forint");
        currencyName.put("IDR", "Rupiah");
        currencyName.put("ILS", "New Israeli Shekel");
        currencyName.put("INR", "Indian Rupee");
        currencyName.put("ISK", "Iceland Krona");
        currencyName.put("JPY", "Yen");
        currencyName.put("KRW", "South Korean Won");
        currencyName.put("MXN", "Mexican Peso");
        currencyName.put("NOK", "Norwegian Krone");
        currencyName.put("NZD", "New Zealand Dollar");
        currencyName.put("PHP", "Philippine Peso");
        currencyName.put("PLN", "PZloty");
        currencyName.put("RON", "Leu");
        currencyName.put("RUB", "Russian Ruble");
        currencyName.put("SEK", "Swedish Krona");
        currencyName.put("SGD", "Singapore Dollar");
        currencyName.put("THB", "Baht");
        currencyName.put("TRY", "Turkish Lira");
        currencyName.put("USD", "US Dollar");
        currencyName.put("ZAR", "Rand");
    }

    @Override
    public String toString() {
        return rates.toString() + ' ' + base + ' ' + date + ' ' + historyRate.toString();
    }
}
