package org.matmech.db;

import java.sql.*;

import org.matmech.dataSaver.DataSaver;
import org.matmech.db.bll.GroupsDBSource;
import org.matmech.db.bll.UsersDBSource;
import org.matmech.db.bll.DictonaryDBSource;
import org.matmech.db.bll.WordsDBSource;
import org.matmech.db.models.Users;
import org.matmech.db.models.Groups;
import org.matmech.db.models.Dictonary;
import org.matmech.db.models.Words;
import org.matmech.db.repository.DBConnection;

public class DBHandler {
    private DBConnection dbConnection = null;
    private UsersDBSource usersDBSource;
    private GroupsDBSource groupsDBSource;
    private DictonaryDBSource dictonaryDBSource;
    private WordsDBSource wordsDBSource;

    public DBHandler(String DB_URL, String DB_USERNAME, String DB_PASSWORD) {
        dbConnection = new DBConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        usersDBSource = new UsersDBSource();
        groupsDBSource = new GroupsDBSource();
        dictonaryDBSource = new DictonaryDBSource();
        wordsDBSource = new WordsDBSource();
    }

    /**
     * Подключение к базе данных
     * @param DB_URL - url адрес базы данных
     * @param DB_USERNAME - пользователь в базед данных
     * @param DB_PASSWORD - пароль пользователя
     */
    public void setDbConnection(String DB_URL, String DB_USERNAME, String DB_PASSWORD) {
        dbConnection = new DBConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    /**
     * <p>Добавляет нового пользователя в базу данных</p>
     *
     * @param firstname - имя человека
     * @param surname - фамилия человека
     * @param tag - username человека
     */
    public String usersInsert(String firstname, String surname, String tag) {
        try {
            Users user = new Users();
            Dictonary dictonary = new Dictonary();

            user.setFirstname(firstname);
            user.setSurname(surname);
            user.setTag(tag);

            boolean answer = usersDBSource.regUser(user, dbConnection);

            if (answer) {
                // создание словаря для пользователя
                dictonary.setUserId(usersDBSource.getUserIdByTag(user, dbConnection));
                dictonaryDBSource.createDictonary(dictonary, dbConnection);

                return "Привет, добро пожаловать в нашего бота для изучения английского языка!\n"
                        + "Список всех команд можете посмотреть с помощью /help";
            }

            return "Вы уже зарегистрированны в нашей системе";
        } catch (SQLException e) {
            System.out.println("Не удалось зарегистрировать пользователя");
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Выводит всех пользователей на экран
     */
    public void getAllUsers() {
        try {
            usersDBSource.getAllUsers(dbConnection);
        } catch (SQLException e) {
            System.out.println("Не удалось получить пользователей");
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>Добавляет слово в базу данных</p>
     *
     * @param wordValue - слово, переданное в параметрах
     * @param wordTranslate - перевод этого слова
     * @param group - параметр, который означает
     * @param tag - username человека
     */
    public String wordAdd(String wordValue, String wordTranslate, String group, String tag) {
        try {
            Words words = new Words();
            Users users = new Users();
            Dictonary dictonary = new Dictonary();
            Groups groups = new Groups();

            words.setWordTranslate(wordTranslate);
            words.setWordValue(wordValue);

            users.setTag(tag);

            dictonary.setUserId(usersDBSource.getUserIdByTag(users, dbConnection));
            words.setDictonaryId(dictonaryDBSource.getDictonaryId(dictonary, dbConnection));

            groups.setTitle(group);
            groups.setDictonaryId(dictonaryDBSource.getDictonaryId(dictonary, dbConnection));

            groupsDBSource.createGroup(groups, dbConnection);

            words.setGroupId(groupsDBSource.getGroupId(groups, dbConnection));
            return wordsDBSource.wordAdd(words, dbConnection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>Показать перевод слова</p>
     *
     * @param wordValue - слово, переданное в параметрах
     */
    public String translateWord(String wordValue) {
        Words words = new Words();

        words.setWordValue(wordValue);

        return wordsDBSource.translate(words, dbConnection);
    }

    /**
     * <p>Изменить параметр слова</p>
     *
     * @param wordValue - слово, переданное в параметрах
     * @param wordParam - параметр для изменения, переданное в параметрах
     * @param paramValue - значение изменяемого параметра, переданное в параметрах
     */
    //если ломается обернуть в isExist()
    public String edit(String wordValue, String wordParam, String paramValue){
        Words words = new Words();
        words.setWordValue(wordValue);
        switch (wordParam){
            case("group"):
                try {
                    Groups groups = new Groups();
                    groups.setTitle(paramValue);
                    // вытягиваю dictionary_id из слова

                    int dictionaryID = Integer.parseInt(wordsDBSource.getDictonaryId(words, dbConnection));
                    groups.setDictonaryId(dictionaryID);
                    // поиск по d_id group_id с title из paramValue

                    int groupID = groupsDBSource.getGroupId(groups, dbConnection);

                    if (groupID == -1)
                        return "Такой группы не существует! Создайте группу, пожалуйста!";

                    words.setGroupId(groupID);

                    return wordsDBSource.editGroupId(words, dbConnection);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            case("translation"):
                words.setWordTranslate(paramValue);
                return wordsDBSource.editTranslation(words,dbConnection);
        }
        return "Этот параметр не подлежит изменению.\n Полный список команд можете посмотреть с помощью /help";
    }

    /**
     * <p>Узнать группу слова</p>
     *
     * @param wordValue - слово, переданное в параметрах
     */
    public String getGroup(String wordValue) {
        try {
            Words words = new Words();

            words.setWordValue(wordValue);
            words.setDictonaryId(Integer.parseInt(wordsDBSource.getDictonaryId(words, dbConnection)));

            return groupsDBSource.getGroupTitle(words.getDictonaryId(), dbConnection);
        } catch (SQLException e) {
            System.out.println("Не удалось получить группу");
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>Удаляет слово</p>
     *
     * @param wordValue - слово, переданное в параметрах
     */
    public String deleteWord(String wordValue) {
        Words words = new Words();

        words.setWordValue(wordValue);

        return wordsDBSource.deleteWord(words, dbConnection);
    }

    /**
     * Устанавливает пользователю режим тестирования
     * @param testMode - режим тестирования, который желает установить пользователь
     * @param tag - тег пользователя
     */
    public String setMode(String testMode, String tag) {
        return switch (testMode) {
            case "easy", "difficult" -> {
                Users user = new Users();
                user.setTestMode(testMode);
                user.setTag(tag);

                yield usersDBSource.setMode(user, dbConnection);
            }
            default -> "Вы выбрали не существующий режим тестирования. Надо выбрать либо easy, либо difficult!";
        };
    }

    /**
     * Возвращает режим тестирования пользователя
     * @param tag - тег пользователя
     */
    public String getMode(String tag) {
        Users user = new Users();
        user.setTag(tag);

        String testMode = usersDBSource.getMode(user, dbConnection);

        if (testMode != null)
            return "Режим тестирования = " + testMode;

        return "Произошла ошибка: у вас не установлен режим тестирования\nУстановить вы его можете с помощью /set_mode";
    }
}
