package com.examples.jdreyesp.concurrency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Usage of client-side locking for a synchronized list.
 * This usage is more efficient than synchronizing the whole method.
 *
 * For example, if we are coding a servlet, a synchronized servlet method would hang connections to the web server and
 * affecting the user experience. Client-side locking (to the list in this example) would avoid that problem.
 */
public class ClientSideLocking {

    public List<String> list = Collections.synchronizedList(new ArrayList<>());

    public void putOnList(String element) throws AlreadyOnListException {
        synchronized (list) {
            boolean absent = !list.contains(element);
            if(absent) {
                list.add(element);
            } else {
                throw new AlreadyOnListException();
            }
        }
    }

    public List<String> getElements() {
        return list;
    }

    class AlreadyOnListException extends Exception {
    }
}
