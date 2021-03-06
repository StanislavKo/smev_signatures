/**
 * 
 */
package com.agiliumlabs.smev.ws.model;

/**
 * @author roman
 *
 */
public enum Status {

    /**
     * Запрос
     * 
     */
    REQUEST,

    /**
     * Результат
     * 
     */
    RESULT,

    /**
     * Мотивированныи\u0306 отказ
     * 
     */
    REJECT,

    /**
     * Ошибка при ФЛК
     * 
     */
    INVALID,

    /**
     * Сообщение-квиток о приеме
     * 
     */
    ACCEPT,

    /**
     * Запрос данных/результатов
     * 
     */
    PING,

    /**
     * В обработке
     * 
     */
    PROCESS,

    /**
     * Уведомление об ошибке
     * 
     */
    NOTIFY,

    /**
     * Техническии\u0306 сбои\u0306
     * 
     */
    FAILURE,

    /**
     * Отзыв заявления
     * 
     */
    CANCEL,

    /**
     * Возврат состояния
     * 
     */
    STATE,

    /**
     * Передача пакетного сообщения
     * 
     */
    PACKET;
	
}
