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
