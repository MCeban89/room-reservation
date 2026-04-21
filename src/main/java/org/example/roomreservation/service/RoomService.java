package org.example.roomreservation.service;

import lombok.AllArgsConstructor;
import org.example.roomreservation.model.entity.Room;
import org.example.roomreservation.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@AllArgsConstructor
public class RoomService {
    private RoomRepository roomRepository;

    public List<Room> getRooms()
    {
        return roomRepository.findAll();
    }

    public Room getRoomById(@PathVariable Long id)
    {
        return roomRepository.findById(id).orElse(null);
    }

    public void addRoom(Room room)
    {
        roomRepository.save(room);
    }

    public void delete(Long roomId)
    {
        roomRepository.deleteById(roomId);
    }
}
