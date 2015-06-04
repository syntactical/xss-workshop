package com.thoughtworks.appsec.xssDemo.controllers;

import com.thoughtworks.appsec.xssDemo.Constants;
import com.thoughtworks.appsec.xssDemo.GuestBook;
import com.thoughtworks.appsec.xssDemo.GuestBookEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GuestBookServiceController {

    private GuestBook guestBook;

    @Autowired
    public GuestBookServiceController(GuestBook guestBook) {
        this.guestBook = guestBook;
    }

    @RequestMapping(value="/service/deleteEntries",  method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public DeleteResult deleteAll(HttpServletResponse response, HttpSession session) {
        return doDelete(response, session);
    }

    @RequestMapping(value="/service/entries",  method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public DeleteResult deleteAllRest(HttpServletResponse response, HttpSession session) {
        return doDelete(response, session);
    }

    private DeleteResult doDelete(final HttpServletResponse response, final HttpSession session) {
        UserState userState = (UserState) session.getAttribute(Constants.USER_STATE_SESSION_ATTRIBUTE);
        if (userState == null || !userState.isLoggedIn()) {
            try {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission for this operation");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return new DeleteResult(0);
        }
        return new DeleteResult(guestBook.clearEntries());
    }

    @RequestMapping(value="/service/entries", method= RequestMethod.POST)
    public void postEntry(@RequestParam String content) {
        guestBook.addEntry(content);
    }

    @RequestMapping(value="/service/entries", method= RequestMethod.GET)
    public SearchResult findEntries(@RequestParam(defaultValue = "") String filter) {

        List<GuestBookEntry> filtered = new ArrayList<>();
        for (GuestBookEntry entry : guestBook.getEntries()) {
            if (entry.getContents().contains(filter)) {
                filtered.add(entry);
            }
        }

        return new SearchResult(filtered, filter);
    }

    public static class SearchResult {
        private final List<GuestBookEntry> found;
        private final String filter;

        public SearchResult(final List<GuestBookEntry> found, final String filter) {
            this.found = found;
            this.filter = filter;
        }

        public List<GuestBookEntry> getFound() {
            return found;
        }

        public String getFilter() {
            return filter;
        }
    }

    public static class DeleteResult {
        private final int count;

        public DeleteResult(final int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }
    }
}
