# News

## Technologies
* Spring boot
* Maven 3.9.4 version [Download Maven](https://maven.apache.org/docs/3.9.4/release-notes.html)
* Java 17 version
* Migration Liquibase

## Setup
* Set Maven in setting in Intellij Idea -> File | Settings | Build, Execution, Deployment | Build Tools | Maven
* Set JDK in project settings
* Build project
* Set settings in application.yaml
```yaml
blacklist:
  words:          #Настраиваемый черный список слов для заголовков статей. Указывать с новой строки через -, - word
threads-pool:
  size: 4         #Количество используемых потоков для ThreadPoolTaskExecutor
buffer-articles:
  limit: 4        #Лимит размера буффера для статей с каждого сайта
total-articles:
  limit: 16       #Общий лимит на скачивание статей
```
