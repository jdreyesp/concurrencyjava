package com.examples.jdreyesp.concurrency.clientsidelocking;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
public class ClientSideLockingController {

    public List<String> list = Collections.synchronizedList(new ArrayList<>());

    @RequestMapping(value = "/list", headers = "Content-Type=application/json", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public void putOnList(@RequestBody ClientSideLockingBody body) throws AlreadyOnListException {
        synchronized (list) {
            String element = body.getElement();
            boolean absent = !list.contains(element);
            if(absent) {
                list.add(element);
            } else {
                throw new AlreadyOnListException();
            }
        }
    }

    @RequestMapping(value = "/list")
    public List<String> getElements() {
        return list;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    class AlreadyOnListException extends Exception {
    }
}
