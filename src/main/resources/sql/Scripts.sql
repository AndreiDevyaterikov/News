create table if not exists news_articles
(
    id integer primary key,
    title varchar not null,
    news_site varchar not null,
    published_date timestamp not null,
    article varchar not null
)