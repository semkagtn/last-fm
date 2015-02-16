package com.semkagtn.lastfm.userwalker;

import com.semkagtn.lastfm.utils.RequestWrapper;
import de.umass.lastfm.User;

/**
 * Created by semkagtn on 2/14/15.
 */
public interface UserWalker {

    User nextUser() throws RequestWrapper.RequestException;
}
