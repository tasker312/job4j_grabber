package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {

    private static final Map<String, String> MONTHS = Map.ofEntries(
            Map.entry("янв", "1"),
            Map.entry("фев", "2"),
            Map.entry("мар", "3"),
            Map.entry("апр", "4"),
            Map.entry("май", "5"),
            Map.entry("июн", "6"),
            Map.entry("июл", "7"),
            Map.entry("авг", "8"),
            Map.entry("сен", "9"),
            Map.entry("окт", "10"),
            Map.entry("ноя", "11"),
            Map.entry("дек", "12")
    );

    @Override
    public LocalDateTime parse(String date) {
        LocalDateTime now = LocalDateTime.now();
        if (date.contains("сегодня")) {
            date = date.replace("сегодня",
                    String.format("%d %d %d",
                            now.getDayOfMonth(),
                            now.getMonthValue(),
                            now.getYear() % 100)
            );
        } else if (date.contains("вчера")) {
            now = now.minusDays(1);
            date = date.replace("вчера",
                    String.format("%d %d %d",
                            now.getDayOfMonth(),
                            now.getMonthValue(),
                            now.getYear() % 100)
            );
        } else {
            String month = date.substring(date.indexOf(' ') + 1, date.indexOf(' ') + 4);
            date = date.replaceFirst("[а-я]{3}", MONTHS.get(month));
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d M yy, HH:mm");
        return LocalDateTime.parse(date, formatter);
    }
}
