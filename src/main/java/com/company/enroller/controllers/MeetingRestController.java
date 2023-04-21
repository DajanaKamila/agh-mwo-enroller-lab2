package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController{

    @Autowired
    MeetingService meetingService;

    @Autowired
    ParticipantService participantService;

//    @RequestMapping(value = "", method = RequestMethod.GET)
//    public ResponseEntity<?> getMeetings(){
//        Collection<Meeting> meetings = meetingService.getAll();
//		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
//    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting) {

        if (meetingService.findById(meeting.getId()) != null) {
            return new ResponseEntity<String>(
                    "Unable to create. A meeting with id " + meeting.getId() + " already exist.",
                    HttpStatus.CONFLICT);
        }
        meetingService.add(meeting);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id) {
        Meeting meetingToDelete = meetingService.findById(id);
        if(meetingToDelete == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meetingService.delete(meetingToDelete);
        return new ResponseEntity<Meeting>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody Meeting updatedMeeting) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meeting.setId(updatedMeeting.getId());
        meetingService.update(meeting);
        return new ResponseEntity<Meeting>(HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings(@RequestParam(value = "title", defaultValue = "") String title,
                                             @RequestParam(value = "description", defaultValue = "") String description,
                                             @RequestParam(value = "participant", defaultValue = "") Participant participant,
                                            @RequestParam(value = "sortMode", defaultValue = "") String sortMode){
        Collection<Meeting> meetings = meetingService.findMeetings(title, description, participant, sortMode);

        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }




    /*
    POST localhost:8080/meetings/2/participants

    RequestBody:
    {"login" : "user"}
    sprawdzić czy użylpwnik intnieje w systemie (mamy już zrobione szukanie ich de facto)
    meeting.add
    update meetingu, bo się zmienili użytkownicy

    DELETE localhost:8080/meetings/2/participants/user2
    jeśli nie ma takiego użytkownika, to user not found

    potrzebne 2 autowire - meeting service i participant service

     */



}
