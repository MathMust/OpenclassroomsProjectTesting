package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageResponseTest {

    @Test
    void constructor_shouldInitializeMessage() {
        MessageResponse response = new MessageResponse("Hello");

        assertEquals("Hello", response.getMessage());
    }

    @Test
    void setMessage_shouldUpdateMessage() {
        MessageResponse response = new MessageResponse("Hello");

        response.setMessage("Updated");

        assertEquals("Updated", response.getMessage());
    }
}
