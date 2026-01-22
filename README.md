А. Ветка "main" содержит общую для consumer и producer сущность Message.
Б. Ветка "consumer" содержит потребителя для автоматической отправки (через Kafka) - класс MessageHandler и для ручной отправки - класс MessageController.
В. Ветка "producer" содержит поставщика сообщений. 
Producer имеет следующие "EndPoints":
А. Для автоматической отправки сообщений: 
А1) @DeleteMapping("/users/{id}")
    public UserDtoResult delete(@PathVariable Integer id)
А2) @PostMapping("/users")
    public UserDtoResult create(@RequestBody @Validated UserDtoCreateAndUpdate userCreateDto)

Б. Для ручной отправки сообщений:
Б1) @DeleteMapping("/users/message/{id}")
    public UserDtoResult deleteWithManualMessageSending(@PathVariable Integer id)
Б2) @PostMapping("/users/message")
    public UserDtoResult createWithManualMessageSending(@RequestBody @Validated UserDtoCreateAndUpdate userCreateDto)

UserDtoCreateAndUpdate создается на основе Json = {"name":"Example", "email":"example@gmail.com", "age":99}.

Отправка запросов производилась через PostMan.
Для корректной демонсирации работы приложения должен быть запущен потребитель отправлений на электронную почту (MailHog) - ccылка в настройках consumer.
Для producer зафиксирован порт 8080.
Для consumer зафиксирован порт 8181.

Задание
Боковая панель

Домашнее задание 5
Название курса
Домашнее задание 5
Требуемые условия завершения
 Выполнено: Просмотреть  Выполнено: Дать ответ на задание
Реализовать микросервис(notification-service) для отправки сообщения на почту при удалении или добавлении пользователя.

Использовать необходимые модули spring и kafka.
При удалении или создании юзера приложение, реализованное до этого(user-service), должно отправлять сообщение в kafka, в котором содержится информация об операции(удаление или создание) и email юзера.
Новый микросервис(notification-service) должен получить сообщение из kafka и отправить сообщение на почту юзера в зависимости от операции: удаление - Здравствуйте! Ваш аккаунт был удалён. Создание - Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.
Также отдельно добавить API, которая будет отправлять сообщение на почту(почти тот же функционал, что и через кафку).
Написать интеграционные тесты для проверки отправки сообщения на почту.
