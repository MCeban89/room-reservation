package org.example.roomreservation.controller;

import lombok.AllArgsConstructor;
import org.example.roomreservation.model.dto.RoomRequestDTO;
import org.example.roomreservation.model.entity.Room;
import org.example.roomreservation.service.RoomService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController()
@RequestMapping("/rooms")
@AllArgsConstructor
public class RoomController {
    private RoomService roomService;


    @GetMapping
    public List<Room> getRooms() { return roomService.getRooms();}

    @GetMapping("/available")
    public List<Room> getAvailableRooms(@RequestParam LocalDateTime start,
                                        @RequestParam LocalDateTime end,
                                        @RequestParam(defaultValue = "1") Integer minCapacity) {
        return roomService.getAvailableRooms(start,end,minCapacity);
    }

    @GetMapping("/{id}")
    public Room findRoomById(@PathVariable Long id) { return roomService.getRoomById(id);}

    @PostMapping
    public void addRoom(@RequestBody RoomRequestDTO room)
    {
        roomService.addRoom(room);
    }

    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable Long id)
    {
        roomService.deleteRoom(id);
    }

    @PutMapping("/{id}")
    public void updateRoom(@PathVariable Long id, @RequestBody RoomRequestDTO dto){roomService.updateRoom(id, dto);}

}
