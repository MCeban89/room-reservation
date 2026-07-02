package org.example.roomreservation.service;

import lombok.AllArgsConstructor;
import org.example.roomreservation.model.dto.RoomRequestDTO;
import org.example.roomreservation.model.entity.Room;
import org.example.roomreservation.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    public List<Room> getRooms()
    {
        return roomRepository.findByActiveTrue();
    }

    public List<Room> getAvailableRooms(LocalDateTime start, LocalDateTime end, Integer minCapacity) {
        return roomRepository.findAvailableRooms(start,end,minCapacity);
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    public void addRoom(RoomRequestDTO dto)
    {
        if(roomRepository.existsByName(dto.getName())){
            throw new IllegalArgumentException("Room already exists");
        }

        Room room = Room.builder()
                .name(dto.getName())
                .capacity(dto.getCapacity())
                .floor(dto.getFloor())
                .description(dto.getDescription())
                .hasProjector(dto.getHasProjector())
                .hasWhiteboard(dto.getHasWhiteboard())
                .hasVideoConference(dto.getHasVideoConference())
                .active(true)
                .build();

        roomRepository.save(room);
    }

    public void updateRoom(long id, RoomRequestDTO dto){
        Room room = roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room not found"));

        room.setName(dto.getName());
        room.setCapacity(dto.getCapacity());
        room.setFloor(dto.getFloor());
        room.setDescription(dto.getDescription());
        room.setHasProjector(dto.getHasProjector());
        room.setHasWhiteboard(dto.getHasWhiteboard());
        room.setHasVideoConference(dto.getHasVideoConference());
        room.setActive(true);

        roomRepository.save(room);
    }

    public void deleteRoom(Long roomId)
    {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.setActive(false);
        roomRepository.save(room);
    }
}
