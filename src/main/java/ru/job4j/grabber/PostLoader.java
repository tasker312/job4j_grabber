package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

public class PostLoader {
    public Post load(String url) throws Exception {
        Document doc = Jsoup.connect(url).get();
        Element vacancy = doc.selectFirst(".msgTable");
        Post post = new Post();
        String desc = vacancy.select(".msgBody").get(1).html()
                .replaceAll("<br>", "\n")
                .replaceAll("</?b>", "");
        post.setDescription(desc);
        String created = vacancy.select(".msgFooter").get(0).text();
        created = created.substring(0, created.indexOf(':') + 3);
        post.setCreated(new SqlRuDateTimeParser().parse(created));
        return post;
    }
}
