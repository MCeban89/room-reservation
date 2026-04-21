package org.example.roomreservation.controller;

import lombok.AllArgsConstructor;
import org.example.roomreservation.model.entity.Room;
import org.example.roomreservation.repository.RoomRepository;
import org.example.roomreservation.service.RoomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/room")
@AllArgsConstructor
public class RoomController {
    private RoomService roomService;


    @GetMapping("/rooms")
    public List<Room> getRooms()
    {
        return roomService.getRooms();
    }

    @GetMapping("/rooms/{id}")
    public Room findRoomById(@PathVariable Long id)
    {
       return roomService.getRoomById(id);
    }

    @PutMapping("/add")
    public void addRoom(@RequestBody Room room)
    {
        roomService.addRoom(room);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id)
    {
        roomService.delete(id);
    }

}
