package org.example.roomreservation.model.dto;

public class EventDTO {
    private String id;
    private String title;
    private String start;
    private String end;

    public EventDTO(String id, String title, String start, String end) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
    }
}
