# Бот для изучения английских слов

## Задача 1

Сделать бота, который будет вести диалог с пользователем. Подключить к платформе Telegram

**Пример:**

-- Привет!

-- Привет, <Имя>

**DONE**

## Задача 2

Сделать для бота новую команду `/word_add` с тремя параметрами: `<слово> <перевод> <группа>`.  Параметр слово получает слово на английском, которое надо добавить, параметр обязательный. Параметры перевод получает слово перевод на русском, параметр обязательный. Параметр группа определяет группу для слова, обязательный параметр

Также сделать команду `/translate <слово>` - показать перевод слова из базы данных

Написать реализацию команды `/remove <слово>`, которая удаляет команду из словаря. Если слово было в словаре, то возвращает сообщение слово было удалено из словаря, а если нет, то возвращает слово не было удалено из словаря, так как его там не было

Сделать команду `/edit <слово> <какой параметр поменять> <значение>` - меняет значение у параметра слова из базы данных. Поменять можно только группу и перевод. Название параметра нужно указывать на английском языке, соответственно, перевод -  translation, группа - group. Значение параметра должно соответствовать всем требованиям, которые были приведены выше. Если слова нет в словаре, то вернет редактирование не удалось выполнить, так как слова нет в словаре - прежде чем поменять что-то в нем, добавьте его в словарь, а если редактирование выполнилось успешно, то возвращает вы успешно изменили <название параметра>

Сделать команду `/get_group <слово`> - которое возвращает группу слова

Примеры:
`/word_add start начать глаголы` - добавляет слово в базу данных
`/translate start` -  возвращает начать
`/translate начать` - возвращает start
`/edit start translation старт` - изменяет значения слова и возвращает вы изменили перевод слова
`/remove start` - возвращает сообщение слово было удалено из вашего словаря
`/get_group start` - вернет Ошибку! Слова нет в словаре!

В чем интерес:
1. Научимся работать с базой данных PostreSQL

**DONE**

## 3 задача

**Нужно сделать:**

    1. Сделать рефакторинг архитектуры RequestHandler, чтобы он запрашивал значения параметров постепенно
    
    2. Переписать под новый код старые команды: `/word_add`, `/translate`, `/edit`, `/remove` и `/get_group`
    
    3. Подключить бота к платформе VK
    
    4. Сделать тестирование

Команды для тестов:
`/test Параметры: <группа>, <количество слов>, <режим>` - тест по словам из базы данных. Параметры группа, количество слов - не обязательны. После ввода команды /test, бот сам запрашивает эти данные
Если значение параметра группа будет слово Все, то тестирование будет по всем группам
Если значение параметра количество слов будет слово По всем, то тестирование будет длиться бесконечно, пока не встретится команда /stop
Если значение параметра количество слов будет слово Стандартное, то тест будет по 10 словам
Тест запускается в easy или difficult режиме. Режим бот сам запрашивает у пользователя. Если пользователь ввел вместо easy, difficult - Стандартный, то режим будет установлен по-умолчанию. По-умолчанию установлен easy режим

Примеры:
`/test`

==> Введите группу, по которой хотите произвести тестирование. Если хотите тестирование по всем словам, то напишите Все:
==> Глаголы
==> Введите количество слов. Если произвести тестирование по всем словам, то напишите По всем. Если хотите стандартное количество слов, то 
        напишите Стандартное
==> 1
==> Введите сложность теста (easy или difficult). Если хотите стандартную сложность, то напишите Стандартный:
==> Easy

может вывести:
Укажите перевод слова start:
 1. Начать
 2. Закончить
 3. Делать
 4. Работать

**DONE**

## 4 задача

Накопилось очень много мусора, значит пора сделать рефакторинг проекта
А что конкретно надо сделать:
    1. Убрать передачу информации из/в базы данных из строчного представления в более удобный. Например, взаимодействовать через объекты
    2. Все строковые константы вынести в enum
    3. Всю валидацию параметров сделать через try {...} catch {...}
    4. Поправить структурированную обработку ошибок:
        4.1. В некоторых местах сделать принудительный контроль за ошибками. Например, в репозитории, который работает с базой данных напрямую
        4.2. Дальше классов-сервисов не должны сыпаться ошибки. В них можно попробовать их как-нибудь обработать
    5. Убрать DBHandler, вместо него теперь будут ENUMs
    6. Оптимизировать SQL запросы
    7. Написать user friendly текста

## 5 задача

Сделать сбор статистики по тестированию. Статистику можно получить на текущий момент за: день, неделю, месяц, год
        Статистика дня начинается с 00:00 текущего числа
        Статистика недели начинается с 00:00 текущего понедельника
        Статистика месяца начинается с 00:00 1 дня текущего месяца
        Статистика года начинается с 00:00 1 января текущего года
        Получить статистику можно по команде: `/statistic`. При вводе этой команды появляется сообщение: Для получения статистики выберите какую именно хотите получить статистику. И там 4 кнопки с надписями: За день, За неделю, За месяц, За год

       Также должна быть рассылка статистики по окончании дня, недели, месяца, года.
       Статистика должна содержать в себе сначала: рейтинг пользователя в тестах, а затем первые 5 людей из рейтинга,
       а в конце небольшая мотивашка

Также сделать команду для вывода всех слов, которые есть в словаре у пользователя, в консоль: `/words`

Пример

- `/statistic`
- Для получения статистики выберите какую именно хотите получить статистику. Кнопки *За день*, *За неделю*, *За год*
- Выбирает за день
- Статистика за день:
   Вы находитесь 187 в рейтинге всех пользователей: ответили правильно на 57 из 80 вопросов за 160
   1 место <Имя> <Фамилия> ответил на 100 из 100 вопросов за 321 секунду
   2 место <Имя> <Фамилия> ответил на 160 из 160 вопросов за 400 секунд
   3 место <Имя> <Фамилия> ответил на 79 из 80 вопросов за 259 секунд
   4 место <Имя> <Фамилия> ответил на 88 из 90 вопросов за 281 секунду
   5 место <Имя> <Фамилия> ответил на 59 из 62 вопроса за 223 секунды
   Вы почти догнали 186 пользователя в рейтинге! Вам не хватило всего ответа на 1 вопрос!

## 6 задача

Переписать проект на стек Spring/Hibernate
