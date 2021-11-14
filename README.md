# job4j Агрегатор вакансий

Система запускается по расписанию. Период запуска указывается в настройках - app.properties.

Первый сайт sql.ru. В нем есть раздел job. Программа считывает все вакансии относящиеся к Java и записывает их в базу.

Доступ к интерфейсу через REST API.

[![Build Status](https://app.travis-ci.com/tasker312/job4j_grabber.svg?branch=main)](https://app.travis-ci.com/tasker312/job4j_grabber)
[![codecov](https://codecov.io/gh/tasker312/job4j_grabber/branch/main/graph/badge.svg?token=SCJKUQXW38)](https://codecov.io/gh/tasker312/job4j_grabber)
