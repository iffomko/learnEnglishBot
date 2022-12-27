package org.matmech.bot;

/**
 * Общий интерфейс для всех ботов с обязательным методом <i>start</i>, который запускает всего бота
 */
public interface Bot {
    /**
     * Запускает бота
     */
    void start();
}
