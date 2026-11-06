package com.dsavisualizer.algorithms;

/**
 * ABSTRACT CLASS: the common part of EVERY algorithm in the project.
 *
 * It stores what all algorithms have: a name, a subtitle, an intro text and
 * the pseudocode lines. The fields are PRIVATE (encapsulation); other classes
 * read them through the public getters.
 *
 * You cannot write "new Algorithm(...)": it is abstract. Only its child
 * classes (SortAlgorithm, SearchAlgorithm and then BubbleSort, LinearSearch,
 * ...) are created.
 */
public abstract class Algorithm {

    private final String displayName;
    private final String subtitle;
    private final String intro;
    private final String[] code;

    // protected: only child classes can call this constructor (with super(...))
    protected Algorithm(String displayName, String subtitle,
                        String intro, String[] code) {
        this.displayName = displayName;
        this.subtitle = subtitle;
        this.intro = intro;
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getIntro() {
        return intro;
    }

    public String[] getCode() {
        return code;
    }
}
