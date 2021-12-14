package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

public class SqlRuParse {
    public Post load(String url) {
        Post post = null;
        try {
            Document doc = Jsoup.connect(url).get();
            Element vacancy = doc.selectFirst(".msgTable");
            String title = vacancy.selectFirst(".messageHeader").ownText();
            String desc = vacancy.select(".msgBody").get(1).html()
                    .replaceAll("<br>", "\n")
                    .replaceAll("</?b>", "");
            String created = vacancy.selectFirst(".msgFooter").text();
            created = created.substring(0, created.indexOf(':') + 3);
            post = new Post(title, url, desc, new SqlRuDateTimeParser().parse(created));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }
}
