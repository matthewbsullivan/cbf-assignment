package edu.umn.cs.recsys.cbf;

import org.grouplens.lenskit.data.event.Rating;
import org.grouplens.lenskit.data.history.RatingVectorUserHistorySummarizer;
import org.grouplens.lenskit.data.history.UserHistory;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;
import org.grouplens.lenskit.vectors.VectorEntry;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Build a user profile from all positive ratings.
 */
public class WeightedUserProfileBuilder implements UserProfileBuilder {
    /**
     * The tag model, to get item tag vectors.
     */
    private final TFIDFModel model;

    @Inject
    public WeightedUserProfileBuilder(TFIDFModel m) {
        model = m;
    }

    @Override
    public SparseVector makeUserProfile(@Nonnull UserHistory<Rating> history) {
        // Create a new vector over tags to accumulate the user profile
        MutableSparseVector profile = model.newTagVector();
        // Fill it with 0's initially - they don't like anything
        profile.fill(0);

        // Convert the user's ratings to a vector (makes normalization easier).
        MutableSparseVector ratingVector =
                RatingVectorUserHistorySummarizer.makeRatingVector(history)
                                                 .mutableCopy();

        // TODO Normalize the user's ratings
        double average;
        average = ratingVector.sum() / ratingVector.size();
        // TODO Build the user's weighted profile
        for(Rating r: history)
        {
            SparseVector iVec = model.getItemVector(r.getItemId());
            profile.addScaled(iVec, r.getValue() - average);
        }

        // The profile is accumulated, return it.
        // It is good practice to return a frozen vector.
        return profile.freeze();
    }
}
