package com.pharmacy.pos.notifications.dto;

import com.pharmacy.pos.notifications.entity.Notification.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private Long id;
    private Long organizationId;
    private Long userId;
    private NotificationType type;
    private String title;
    private String message;
    private boolean read;
    private String metadata;
    private String actionUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}