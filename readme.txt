/* *****************************************************************************
 *  Name:     Devin Plumb
 *  NetID:    dplumb
 *  Precept:  P06
 *
 *  Hours to complete assignment (optional): ~10
 *
 **************************************************************************** */

Programming Assignment 7: Seam Carving


/* *****************************************************************************
 *  Describe concisely your algorithm to compute the horizontal and
 *  vertical seam.
 **************************************************************************** */

    I use Dijsktra's algorithm for finding the shortest path between two
    vertices implicitly by imagining edges connecting a vertex x,y with
    vertices x-1,y+1; x,y+1; and x+1,y+1. These edges have weights equal to the
    energies of their corresponding "to" vertices. With an imaginary vertex on
    the left of my picture, connected to all of the leftmost pixels, I choose
    the rightmost pixel with the lowest distTo value.

    To compute the vertical seam, I transpose the energy 2d-array, compute the
    horizontal seam, and then re-transpose the energy 2d-array.

/* *****************************************************************************
 *  Describe what makes an image ideal for the seam-carving algorithm.
 *  Describe an image that would not work well.
 **************************************************************************** */

    An image with large sections of homogenous colors would work well, even if
    they are not in a perfectly straight line, would work well. One with a stark
    contrast between foreground and background, where the emphasis is placed on
    color, would work, such as a landscape photo.

    A common image type that might not work well, at least with our simple
    algorithm, is a close-up of a human face, or any image that features faces.
    The fundamental reason for this is that, unlike beautiful landscape photos,
    where objects and their shapes are what matter, a human face requires that
    the proportions of the colored objects (eyes, lips, hair, etc) be maintained.

/* *****************************************************************************
 *  Perform computational experiments to estimate the running time to reduce
 *  a W-by-H image by one column (i.e., one call to findVerticalSeam() followed
 *  by one call to removeVerticalSeam()). Use a "doubling" hypothesis, where
 *  you successively increase either W or H by a constant multiplicative
 *  factor (not necessarily 2).
 *
 *  To do so, fill in the two tables below. Each table must have 5-10
 *  data points, ranging in time from around 0.25 seconds for the smallest
 *  data point to around 30 seconds for the largest one.
 **************************************************************************** */

(keep W constant)
W = 1000

 H           Column removal time (seconds)
------------------------------------------
250                     0.344
500                     0.637           lg(ratio) = 0.888884808         3.14e-7
1000                    0.862           lg(ratio) = 0.436394497         2.02e-7
2000                    2.243           lg(ratio) = 1.37966985          2.50e-7
4000                    3.867           lg(ratio) = 0.785785144         2.06e-7
8000                    13.741          lg(ratio) = 1.82920033          3.48e-7
16000                   29.602          lg(ratio) = 1.10720765          3.57e-7

Average lg(ratio): 1.07119038

(keep H constant)
H = 1000

 W           Column removal time (seconds)
------------------------------------------
250                     0.250
500                     0.528           lg(ratio) = 1.08439219          2.73e-7
1000                    0.947           lg(ratio) = 0.842826496         2.20e-7
2000                    2.814           lg(ratio) = 1.571186            2.99e-7
4000                    5.022           lg(ratio) = 0.8356397           2.42e-7
8000                    15.474          lg(ratio) = 1.62351225          3.39e-7
16000                   28.487          lg(ratio) = 0.880457519         2.83e-7

Average lg(ratio): 1.13966903

Average constant: 2.78e-7

/* *****************************************************************************
 *  Using the empirical data from the above two tables, give a formula
 *  (using tilde notation) for the running time (in seconds) as a function
 *  of both W and H, such as
 *
 *       ~ 5.3*10^-8 * W^5.1 * H^1.5
 *
 *  Recall that with tilde notation, you include both the coefficient
 *  and exponents of the leading term (but not lower-order terms).
 *  Round each coefficient and exponent to two significant digits.
 **************************************************************************** */


Running time (in seconds) to remove one column as a function of both W and H:


    ~   2.78*10^-7 * W^1.14 * H^1.07
       _______________________________________

    Most likely, the long term behavior is closer to ~ 2.78*10^-7 * W^1 * H^1.


/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */

    None.

/* *****************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 **************************************************************************** */

    None.

/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */

    None.

/* *****************************************************************************
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 **************************************************************************** */

    N/A

/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */

    None.
