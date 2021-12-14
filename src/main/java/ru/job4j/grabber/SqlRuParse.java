package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {

    private final DateTimeParser dateTimeParser;

    public SqlRuParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> list(String link) {
        List<Post> posts = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(link).get();
            Elements elements = doc.select(".postslisttopic");
            elements.forEach(el -> {
                String href = el.child(0).attr("href");
                posts.add(detail(href));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post detail(String url) {
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
            post = new Post(title, url, desc, dateTimeParser.parse(created));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }
}
