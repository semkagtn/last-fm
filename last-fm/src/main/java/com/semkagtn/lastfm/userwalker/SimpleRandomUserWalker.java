package com.semkagtn.lastfm.userwalker;

import com.semkagtn.lastfm.utils.RequestWrapper;
import de.umass.lastfm.User;

import java.util.Random;

import static com.semkagtn.lastfm.utils.RequestWrapper.request;

/**
 * Created by semkagtn on 3/4/15.
 */
public class SimpleRandomUserWalker implements UserWalker {

    private static final int ID_BOUND = 65_000_000;
    private Random random = new Random();

    private final String apiKey;

    public SimpleRandomUserWalker(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public User nextUser() throws RequestWrapper.RequestException {
        User user = null;
        while (user == null) {
            int randomId = random.nextInt(ID_BOUND);
            try {
                user = request(User::getInfo, String.valueOf(randomId), apiKey);
            } catch (RequestWrapper.RequestException e) {
                continue;
            }
        }
        return user;
    }
}
