package ar.edu.itba.paw.webapp.dto;

public class NotificationDto {
    private long unread;

    public static NotificationDto fromNumber(long unreadCount) {
        NotificationDto dto = new NotificationDto();
        dto.unread = unreadCount;
        return dto;
    }

    public long getUnread() {
        return unread;
    }

    public void setUnread(long unread) {
        this.unread = unread;
    }
}
