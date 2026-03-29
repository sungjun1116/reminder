package sungjun.ai.reminder.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reminder_list")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReminderList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String color;

    private String icon;

    @Column(name = "group_id")
    private Long groupId;

    @Column(nullable = false)
    private Integer sortOrder;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static ReminderList create(String name, String color, String icon, Long groupId, Integer sortOrder) {
        ReminderList list = new ReminderList();
        list.name = name;
        list.color = color;
        list.icon = icon;
        list.groupId = groupId;
        list.sortOrder = sortOrder;
        list.createdAt = LocalDateTime.now();
        return list;
    }

    public void update(String name, String color, String icon, Long groupId) {
        this.name = name;
        this.color = color;
        this.icon = icon;
        this.groupId = groupId;
    }

    public void updateSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
