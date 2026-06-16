package org.example.roomreservation.service;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.ProdId;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CalendarService {

    public byte[] generateCalendarEvent(String summary, String description, LocalDateTime start, LocalDateTime end) throws IOException {
        VEvent event = new VEvent();
        event.add(new net.fortuna.ical4j.model.property.Summary(summary));
        event.add(new net.fortuna.ical4j.model.property.Description(description));
        event.add(new net.fortuna.ical4j.model.property.Uid(UUID.randomUUID().toString()));
        event.add(new net.fortuna.ical4j.model.property.DtStart<>(start));
        event.add(new net.fortuna.ical4j.model.property.DtEnd<>(end));

        Calendar calendar = new Calendar();
        calendar.add(new ProdId(UUID.randomUUID().toString()));
        calendar.add(event);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CalendarOutputter outputter = new CalendarOutputter();
        outputter.output(calendar, outputStream);

        return outputStream.toByteArray();

    }
}
