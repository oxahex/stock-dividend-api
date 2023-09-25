package com.oxahex.stockdividendapi;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Arrays;

@SpringBootApplication
public class StockDividendApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockDividendApiApplication.class, args);

        Connection connection = Jsoup.connect("https://finance.yahoo.com/quote/O/history?period1=1690848000&period2=1693526400&interval=1d&filter=history&frequency=1d&includeAdjustedClose=true");
        try {
            Document document = connection.get();
            Elements elems = document.getElementsByAttributeValue("data-test", "historical-prices");
            Element table = elems.get(0);
            Element tbody = table.getElementsByTag("tbody").get(0);

            for (Element e : tbody.children()) {
                String txt = e.text();
                if (!txt.endsWith("Dividend")) continue;

                String[] splits = txt.split(" ");

                String month = splits[0];
                int day = Integer.parseInt(splits[1].replace(",", ""));
                int year = Integer.parseInt(splits[2]);
                String dividend = splits[3];

                System.out.println(year + "/" + month + "/" + day + " -> " + dividend);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}