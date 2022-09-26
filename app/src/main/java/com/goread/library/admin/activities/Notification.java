package com.goread.library.admin.activities;

public class Notification {
    String NotificcationId;
    String NotificationTitle;
    String NotificationBody;

    public Notification() {

    }

    public Notification(String notificcationId, String notificationTitle, String notificationBody) {
        NotificcationId = notificcationId;
        NotificationTitle = notificationTitle;
        NotificationBody = notificationBody;
    }

    public void setNotificcationId(String notificcationId) {
        NotificcationId = notificcationId;
    }

    public void setNotificationTitle(String notificationTitle) {
        NotificationTitle = notificationTitle;
    }

    public void setNotificationBody(String notificationBody) {
        NotificationBody = notificationBody;
    }

    public String getNotificcationId() {
        return NotificcationId;
    }

    public String getNotificationTitle() {
        return NotificationTitle;
    }

    public String getNotificationBody() {
        return NotificationBody;
    }
}
