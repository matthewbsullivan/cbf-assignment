package edu.umn.cs.recsys.cbf;

import org.grouplens.lenskit.data.event.Rating;
import org.grouplens.lenskit.data.history.UserHistory;
import org.grouplens.lenskit.vectors.SparseVector;

import javax.annotation.Nonnull;

/**
 * Builds a user profile from the user's ratings and the content model.  This is split
 * into a separate class so that we can have 2 different ones &mdash; weighted and
 * unweighted &mdash; and use the same code for the rest of the process.
 */
public interface UserProfileBuilder {
    /**
     * Create a user profile (weights over tags).
     *
     * @param history The user's history (their ratings).
     * @return A vector of tag weights describing the user's preferences.  The tag IDs are as per
     * the {@link edu.umn.cs.recsys.cbf.TFIDFModel}.
     */
    SparseVector makeUserProfile(@Nonnull UserHistory<Rating> history);
}
