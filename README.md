# cbf-assignment
# CBF Programming Assignment

## Overview

In this assignment, you will implement a content-based recommender as a LensKit recommender algorithm.
LensKit provides a command-line tool to produce recommendations from a user; your task is to implement
the logic of the recommender itself.

There are 2 parts to this assignment, implementing two variants of a TF-IDF recommender.

## Downloads and Resources

- Project template (in TRACS)
- [LensKit Example Project](http://github.com/lenskit/lenskit-hello) (not required, but may be useful for reference)
- [LensKit documentation](http://lenskit.org/documentation)
- [JavaDoc for included code](http://cs.txstate.edu/~m_e114/recsys/asn1/)

There are also 3 videos you will likely find useful: the LensKit introduction, the example walkthrough,
and the assignment video itself.

Additionally, you will need:

- [Java](http://java.oracle.com) — download the Java 8 JDK.  On Linux, install the OpenJDK 'devel' package (you will need the devel package to have the compiler).
- An IDE; I recommend [IntelliJ IDEA](http://jetbrains.com/idea/) Community Edition.
- The [LensKit binary distribution](https://bintray.com/lenskit/lenskit-releases/lenskit/2.2-M2/view) if you want to use its command-line tools directly.

## Notation

Here's the mathematical notation we are using:

$\vec u$
:   The user's vector (in this assignment, the user profile vector).
$\vec i$
:   The item vector.
$I(u)$
:   The set of items rated by user u.
$u_t$, $i_t$
:   User u's or item i's score for tag t
$r_{ui}$
:   User u's rating for item i.
$\mu_u$
:   The average of user u's ratings.

## Part 1: TF-IDF Recommender with Unweighted Profiles (85 points)

Start by downloading the project template. This is a Gradle project; you can import it into your IDE directly (IntelliJ users can open the build.gradle file as a project).  The code should compile as-is; you can test this by running the `build` Gradle target from your IDE, or running `./gradlew build` at the command line.

There are 3 things you need to implement to complete the first part of the assignment:

Compute item-tag vectors (the model)
:   For this task, you need to modify the model builder (`TFIDFModelBuilder`, your modifications go in the get() method) to compute the unit-normalized TF-IDF vector for each movie in the data set. We provide the skeleton of this; TODO comments indicate where you need to implement missing pieces. When this piece is done, the model should contain a mapping of item IDs to TF-IDF vectors, normalized to unit vectors, for each item.

Build user profile for each query user
:   The `UserProfileBuilder` interface defines classes that take a user's history – a list of ratings — and produce a vector representing that user's profile.  For part 1, the profile should be the sum of the item-tag vectors of all items the user has rated positively (>= 3.5 stars); this implementation goes in `ThresholdUserProfileBuilder`.

Generate item scores for each user
:   The heart of the recommendation process in many LensKit recommenders is the score method of the item scorer, in this case `TFIDFItemScorer`. Modify this method to score each item by using cosine similarity: the score for an item is the cosine between that item's tag vector and the user's profile vector. Cosine similarity is defined as follows:

    $$cos(u,i) = \frac{\vec u \cdot \vec i}{\|\vec u\|_2 \|\vec i\|_2} = \frac{\sum_t u_t i_t}{\sqrt{\sum_t u^2_t} \sqrt{\sum_t i^2_t}}$$

There are three ways to run your program.

-   To run it from the IDE, run the `org.grouplens.lenskit.cli.Main` class (included in LensKit - it is automatically pulled in by the Gradle build) with the following arguments:

    ```
    recommend -c etc/basic.groovy 42
    ```

-   To run it from the command line using Gradle, run:

    ```
    ./gradlew recommendBasic -PuserId=42
    ```

-   To run it from the command line with the LensKit command-line tools, first build (`./gradlew build`), and then:

    ```
    lenskit recommend -C build/classes/main -c etc/basic.groovy 42
    ```

Try different user IDs.

### Example Output for Unweighted User Profile

The following example gives actual outputs for user 42 in the data set. It was executed using `./gradlew recommendBasic -PuserId=42` in a Unix-like console.

```
recommendations for user 42:
  862: 0.287
  557: 0.251
  11: 0.196
  1892: 0.191
  9741: 0.182
  807: 0.180
  812: 0.180
  752: 0.177
  141: 0.175
  1891: 0.170
```

## Part 2: Weighted User Profile (15 points)

For this part, adapt your solution from Part 1 to compute weighted user profiles.  Put your weighted user profile code in `WeightedUserProfileBuilder`.

In this variant, rather than just summing the vectors for all positively-rated items, compute a weighted sum of the item vectors for all items, with weights being based on the user's rating. Your solution should implement the following formula:

$$\vec u = \sum_{i \in I(u)} (r_{ui} - \mu_u) \vec i$$

### Example Output for Weighted User Profile

The following example gives actual outputs for 5 user IDs in the data set. It was produced by running `./gradlew recommendWeighted -PuserId=42` in a Unix-like console.

```
recommendations for user 42:
  13: 0.095
  862: 0.090
  1422: 0.070
  1892: 0.060
  1891: 0.057
  11: 0.056
  568: 0.053
  581: 0.051
  7443: 0.049
  812: 0.046
```

## Submitting

Submit your code as a zip file to TRACS.

To create this zip file, please use the pre-created archive functionality in the Gradle build:

```
./gradlew prepareSubmission
```

This will ensure that your submission contains all required files.  It will produce a submission file in `build/distributions`.
