package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("d M yy, HH:mm");

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
        if (date.contains("сегодня") || date.contains("вчера")) {
            String day = date.substring(0, date.indexOf(','));
            LocalDateTime now = "вчера".equals(day) ? LocalDateTime.now().minusDays(1) : LocalDateTime.now();
            date = date.replace(day,
                    String.format("%d %d %d", now.getDayOfMonth(), now.getMonthValue(), now.getYear() % 100));
        } else {
            String month = date.substring(date.indexOf(' ') + 1, date.indexOf(' ') + 4);
            date = date.replaceFirst("[а-я]{3}", MONTHS.get(month));
        }
        return LocalDateTime.parse(date, FORMATTER);
    }
}
